package ru.skypro.homework.test.utils;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;

import java.io.IOException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class Constant {
    public static final UserEntity TEST_USER = new UserEntity();
    public static final Register TEST_REGISTER = new Register();
    public static final String TEST_PASSWORD = "1234567890";
    public static final String TEST_NEW_PASSWORD = "9876543210";
    public static final NewPassword TEST_NEW_PASSWORD_DTO = new NewPassword();
    public static final UpdateUser TEST_UPDATE_USER = new UpdateUser();

    public static PhotoEntity TEST_PHOTO_ENTITY = null;
    public static Authentication TEST_AUTHENTICATION = null;
    public static MultipartFile TEST_MULTIPART_FILE = null;


    public static void reloadFields(UserEntity userEntity, PasswordEncoder encoder) {
        userEntity.setId(1);
        userEntity.setUsername("user@mail.ru");
        userEntity.setPassword(encoder.encode(TEST_PASSWORD));
        userEntity.setFirstName("IVAN");
        userEntity.setLastName("IVANOV");
        userEntity.setPhone("+7(000)-000-00-00");
        userEntity.setRole(Role.USER);
        userEntity.setPhoto(null);
        userEntity.setFilePath(null);
    }

    public static void reloadFields(Register register) {
        register.setUsername("user@mail.ru");
        register.setPassword(TEST_PASSWORD);
        register.setFirstName("IVAN");
        register.setLastName("IVANOV");
        register.setPhone("+7(000)-000-00-00");
        register.setRole(Role.USER);
    }

    public static Authentication reloadFields(Authentication testAuthentication, UserEntity entity) {
        testAuthentication = new UsernamePasswordAuthenticationToken(entity.getUsername(), entity);
        return TEST_AUTHENTICATION = testAuthentication;
    }

    public static MultipartFile reloadFields(MultipartFile multipartFile
            , String name
            , String originalFilename
            , String contentType
            , byte[] content) {

        MultipartFile createdFile = new MockMultipartFile(name, originalFilename, contentType, content);
        return TEST_MULTIPART_FILE = createdFile;
    }

    public static PhotoEntity reloadFields(MultipartFile file) throws IOException {
        PhotoEntity photo = new PhotoEntity();
        photo.setData(file.getBytes());
        photo.setMediaType(file.getContentType());
        photo.setFileSize(file.getSize());
        return TEST_PHOTO_ENTITY = photo;
    }

    public static void reloadFields(NewPassword testNewPasswordDto, PasswordEncoder encoder) {
        testNewPasswordDto.setCurrentPassword(TEST_PASSWORD); // берем закодированный пароль
        testNewPasswordDto.setNewPassword(TEST_NEW_PASSWORD);
    }

    public static void reloadFields(UpdateUser testUpdateUser) {
        testUpdateUser.setFirstName("ANDREY"); //Меняем данные пользователя на данные из DTO updateUser
        testUpdateUser.setLastName("ANDREEV");
        testUpdateUser.setPhone("+7(000)-000-00-22");
    }
}
