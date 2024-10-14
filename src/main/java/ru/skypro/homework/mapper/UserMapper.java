package ru.skypro.homework.mapper;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.contstants.Constants;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.service.impl.PhotoServiceImpl;

import java.io.IOException;

/**
 * Класс конвертирует одни сущности, связанные с пользователем, в другие
 */
@Slf4j
@Service
public class UserMapper {

    private final Logger logger = LoggerFactory.getLogger(UserMapper.class);


    /**
     * {@link Register} -> {@link UserEntity}
     * @param dto {@link Register}
     * @return entity class {@link UserEntity}
     */
    public static UserEntity mapFromRegisterToUserEntity(Register dto) {
        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());
        return entity;

    }

    /**
     * {@link UserEntity} -> {@link User}
     * @param entity {@link UserEntity}
     * @return dto class {@link User}
     */
    public static User mapFromUserEntityToUser(UserEntity entity) {
        User dto = new User();
        dto.setId(entity.getId());
        dto.setEmail(entity.getUsername());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setImage(Constants.URL_PHOTO_CONSTANT + entity.getPhoto().getId());
        return dto;
    }

    /**
     * {@link UserEntity} -> {@link UpdateUser}
     * @param entity {@link UserEntity}
     * @return dto class {@link UpdateUser}
     */
    public static UpdateUser mapFromUserEntityToUpdateUser(UserEntity entity) {
        UpdateUser dto = new UpdateUser();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        return dto;
    }

    /**
     * {@link MultipartFile} -> {@link PhotoEntity}
     * @param image {@link MultipartFile}
     * @return {@link PhotoEntity}
     */
    public PhotoEntity mapMuptipartFileToPhoto(MultipartFile image) {
        logger.info("Запущен метод сервиса mapMuptipartFileToPhoto");

        PhotoEntity photo = new PhotoEntity();
        try {
            photo.setData(image.getBytes());
            photo.setMediaType(image.getContentType());
            photo.setFileSize(image.getSize());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка конвертации MultipartFile в PhotoEntity, " +
                    "место ошибки - userMapper.mapMultiPartFileToPhoto()");
        }
        return photo;
    }
}