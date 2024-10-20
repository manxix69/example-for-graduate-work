package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.controller.AdController;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exception.UserAlreadyExistException;
import ru.skypro.homework.exception.WrongPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final MyUserDetailService myUserDetailService;

    private final Logger logger = LoggerFactory.getLogger(AdController.class);
    
    public AuthServiceImpl(PasswordEncoder passwordEncoder,
                           UserRepository userRepository,
                           MyUserDetailService myUserDetailService) {
        this.encoder = passwordEncoder;
        this.userRepository = userRepository;
        this.myUserDetailService = myUserDetailService;
    }

    /**
     * Метод авторизации проверяет, существует ли в базе данных пользователь с
     * указанными параметрами метода - логином и паролем.
     * Метод {@link MyUserDetailService#loadUserByUsername(String userName)} проверяет наличие пользователя
     * с указанным логином в БД. И если такого пользователя нет, то выбрасывает исключение
     * {@link UsernameNotFoundException}. Если пользователь найден, то он возвращается из метода.
     * <p>Далее метод <b>encoder.matches()</b> сравнивает пароли.
     * Если проверка пройдена, то метод возвращает true, если же нет, то метод выбрасывает исключение
     * {@link WrongPasswordException}.</p>
     *
     * @param userName
     * @param password
     * @return true - если пользователь существует в БД, и пароли совпадают;
     * {@link UsernameNotFoundException} - если пользователь не найден в БД;
     * {@link WrongPasswordException} - если пароли не совпадают.
     */
    @Override
    public boolean login(String userName, String password) {
        logger.info("Запущен метод сервиса login");
        UserDetails userDetails = myUserDetailService.loadUserByUsername(userName);
        if (!encoder.matches(password, userDetails.getPassword())) {
            throw new WrongPasswordException("Неверный пароль");
        }
        return true;
    }

    /**
     * Метод регистрирует нового пользователя.
     * <p>Метод получает ДТО {@link Register} из контроллера. Далее ДТО конвертируется методом
     * {@link UserMapper#mapFromRegisterToUserEntity(Register)} в сущность {@link UserEntity}.</p>
     * <p>Если пользователь с таким логином ({@link UserEntity#getUserName()}) в репозитории найден,
     * то выбрасывается исключение {@link UserAlreadyExistException}.</p>
     * <p>Если проверка пройдена успешно и такого логина нет в базе данных, то в таком случае
     * пароль кодируется, сохраняется в сущность {@link UserEntity}. А сама сущность сохраняется в
     * базе данных.</p>
     *
     * @param register
     * @return true в случае, если пользователь уникален и сохранен в БД;
     * в случае, если проверка на уникальность логина не пройдена, выбрасывается исключение
     * {@link UserAlreadyExistException}.
     */
    @Override
    public boolean register(Register register) {
        logger.info("Запущен метод сервиса register ,{}", register);
        UserEntity user = UserMapper.mapFromRegisterToUserEntity(register);
        logger.info("Запущен UserMapper.mapFromRegisterToUserEntity ,{}", user);
        if (userRepository.existsByUsername( user.getUsername() )) {
            throw new UserAlreadyExistException("Такой пользователь существует");
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

}