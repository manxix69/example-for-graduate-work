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
import ru.skypro.homework.utils.LogShifter;

import java.io.IOException;

/**
 * Класс конвертирует одни сущности, связанные с пользователем, в другие
 */
@Slf4j
@Service
public class UserMapper {

    private final static Logger logger = LoggerFactory.getLogger(UserMapper.class);
    private final static LogShifter shifter = LogShifter.getLogShifter();

    /**
     * {@link Register} -> {@link UserEntity}
     * @param dto {@link Register}
     * @return entity class {@link UserEntity}
     */
    public static UserEntity mapFromRegisterToUserEntity(Register dto) {
        shifter.shiftLog(logger,"start method mapFromRegisterToUserEntity: {}", dto);

        UserEntity entity = new UserEntity();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setPhone(dto.getPhone());
        entity.setRole(dto.getRole());

        shifter.shiftBackLog(logger, "end method mapFromRegisterToUserEntity: {}", entity);
        return entity;
    }

    /**
     * {@link UserEntity} -> {@link User}
     * @param entity {@link UserEntity}
     * @return dto class {@link User}
     */
    public static User mapFromUserEntityToUser(UserEntity entity) {
        shifter.shiftLog(logger,"start method mapFromUserEntityToUser: {}", entity);

        User dto = new User();
        dto.setId(entity.getId());
        dto.setEmail(entity.getUsername());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        if (entity.getPhoto() != null) {
            dto.setImage(Constants.URL_PHOTO_CONSTANT + entity.getPhoto().getId());
        }

        shifter.shiftBackLog(logger,"end method mapFromUserEntityToUser: {}", dto);
        return dto;
    }

    /**
     * {@link UserEntity} -> {@link UpdateUser}
     * @param entity {@link UserEntity}
     * @return dto class {@link UpdateUser}
     */
    public static UpdateUser mapFromUserEntityToUpdateUser(UserEntity entity) {
        shifter.shiftLog(logger,"start method mapFromUserEntityToUpdateUser: {}", entity);

        UpdateUser dto = new UpdateUser();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhone(entity.getPhone());

        shifter.shiftBackLog(logger,"end method mapFromUserEntityToUpdateUser: {}", dto);
        return dto;
    }

    /**
     * {@link MultipartFile} -> {@link PhotoEntity}
     * @param image {@link MultipartFile}
     * @return {@link PhotoEntity}
     */
    public PhotoEntity mapMuptipartFileToPhoto(MultipartFile image) {
        shifter.shiftLog(logger,"Запущен метод сервиса mapMuptipartFileToPhoto: {}", image.getName());

        PhotoEntity photo = new PhotoEntity();
        try {
            photo.setData(image.getBytes());
            photo.setMediaType(image.getContentType());
            photo.setFileSize(image.getSize());
        } catch (IOException e) {
            throw new RuntimeException("Ошибка конвертации MultipartFile в PhotoEntity, " +
                    "место ошибки - userMapper.mapMultiPartFileToPhoto()");
        }

        shifter.shiftBackLog(logger,"end method mapMuptipartFileToPhoto: {}", photo);
        return photo;
    }
}