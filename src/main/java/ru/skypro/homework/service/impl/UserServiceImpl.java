package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.exception.PasswordIsNotMatchException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.UpdateUser;

import java.io.IOException;

/**
 * Сервис хранящий логику для управления данными пользователей.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final UserMapper userMapper;
    private final ImageServiceImpl imageService;
    private final PasswordEncoder encoder;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${path.to.photos.folder}")
    private String photoDir;

    public UserServiceImpl(UserRepository userRepository, PhotoRepository photoRepository, UserMapper userMapper, ImageServiceImpl imageService, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.userMapper = userMapper;
        this.imageService = imageService;
        this.encoder = encoder;
    }

    /**
     * Метод обновляет пароль текущего, авторизованного пользователя.
     * <p>Метод получает объект newPass, который содержит два поля со старым и новым паролями.
     * А так же объект {@link Authentication} из которого можно получить логин
     * авторизованного пользователя.</p>
     * Далее метод ищет пользователя с соответствующим логином в репозитории и сохраняет его
     * в переменную userEntity. Логин получаем из объекта {@link Authentication}.
     * <p>Далее, метод делает проверку на совпадение старого пароля, введенного пользователем,
     * и пароля сохраненного в БД. Если пароли совпали, от используя сеттер, в переменную, содержащую пользователя,
     * сохраняется новый пароль. Переменная (объект userEntity) с новым, измененным паролем
     * сохраняется в БД.</p>
     *
     * @param newPass        объект {@link NewPassword}, содержащий старый и новый пароли.
     * @param authentication содержит логин авторизованного пользователя.
     */
    @Override
    public void setPassword(NewPassword newPass, Authentication authentication) {
        logger.info("Запущен метод UserServiceImpl.setPassword(): {}" , newPass.getClass(), authentication.getName());

        String oldPassword = newPass.getCurrentPassword();
        String encodeNewPassword = encoder.encode(newPass.getNewPassword()); //получаем в переменную новый пароль и кодируем его
        UserEntity userEntity = userRepository.findByUsername(authentication.getName()); //Находим в БД сущность авторизованного пользователя используя логин из authentication
        if (!encoder.matches(oldPassword, userEntity.getPassword())) { //проверяем совпадают ли старый пароль, введенный пользователем, и пароль сохраненный в БД
            throw new PasswordIsNotMatchException("Пароли не совпадают");
        } else { //пароли совпадают, а значит устанавливаем новый пароль в соответствующее поле сущности
            userEntity.setPassword(encodeNewPassword);
        }
        logger.info("Выполняем метод UserServiceImpl.setPassword()");
        userRepository.save(userEntity); //сохраняем сущность в БД
    }

    /**
     * Метод возвращает информацию о текущем, авторизованном пользователе.
     * Метод, используя объект {@link Authentication#getName()} как параметр userName,
     * находит в БД {@link UserRepository}, пользователя с соответствующими данными и возвращает его.
     * @param username
     * @return объект userEntity
     */
    @Transactional
    @Override
    public UserEntity getUser(String username) {
        logger.info("Запущен метод UserServiceImpl.getUser(): {}" , username);

        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UserNotFoundException("Пользователя с таким логином в базе данных нет");
        }
        logger.info("выполнен метод UserServiceImpl.getUser(): {}" , user);
        return user;
    }

    /**
     * Метод изменяет данные пользователя, а именно имя, фамилию и номер телефона.
     * <p>В начале метод получает из {@link Authentication} логин авторизованного пользователя
     * и записывает его в переменную.</p>
     * <p>По логину находит данные пользователя в БД и кладет их в сущность user.
     * Сущность user заполняется измененными данными из парамера updateUser.</p>
     * <p>В итоге измененный объект user сохраняется в БД, и он же возвращается из метода.</p>
     *
     * @param updateUser     объект содержащий поля с именем, фамилией и номером телефона.
     * @param authentication
     * @return объект user
     */
    @Transactional
    @Override
    public UserEntity updateUser(UpdateUser updateUser, Authentication authentication) {
        logger.info("Запущен метод UserServiceImpl.updateUser(): {}, {}" , updateUser, authentication.getName());

        String userName = authentication.getName(); //Получаем логин авторизованного пользователя из БД
        UserEntity user = userRepository.findByUsername(userName); //Находим данные авторизованного пользователя
        user.setFirstName(updateUser.getFirstName()); //Меняем данные пользователя на данные из DTO updateUser
        user.setLastName(updateUser.getLastName());
        user.setPhone(updateUser.getPhone());
        userRepository.save(user); //сохраняем измененные данные в БД

        logger.info("Выполнен метод UserServiceImpl.updateUser(): {}" , user);
        return user;
    }

    @Transactional
    @Override
    public void updateUserImage(MultipartFile image, Authentication authentication) throws IOException {
        logger.info("Запущен метод UserServiceImpl.updateUserImage(): {}, {}" , image, authentication.getName());

        UserEntity userEntity = userRepository.findByUsername(authentication.getName()); //достаем пользователя из БД
        userEntity = (UserEntity) imageService.updateEntitiesPhoto(image, userEntity); //заполняем поля и возвращаем

        logger.info("userEntity создано - {}", userEntity != null);
        userRepository.save(userEntity); //сохранение сущности user в БД
    }
}