package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.exception.PasswordIsNotMatchException;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.test.utils.Constant;

import java.io.IOException;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {
    @Autowired
    private UserServiceImpl userService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PhotoRepository photoRepository;
    @MockBean
    private UserMapper userMapper;
    @Autowired
    private ImageServiceImpl imageService;
    @Autowired
    private PasswordEncoder encoder;

    private final UserEntity TEST_USER = Constant.TEST_USER;
    private final String TEST_OLD_PASSWORD = Constant.TEST_PASSWORD;
    private final String TEST_NEW_PASSWORD = Constant.TEST_NEW_PASSWORD;
    private final NewPassword TEST_NEW_PASSWORD_DTO = Constant.TEST_NEW_PASSWORD_DTO;
    private final UpdateUser TEST_UPDATE_USER = Constant.TEST_UPDATE_USER;

    private Authentication TEST_AUTHENTICATION = Constant.TEST_AUTHENTICATION;
    private MultipartFile TEST_FILE = Constant.TEST_MULTIPART_FILE;
    private PhotoEntity TEST_PHOTO = Constant.TEST_PHOTO_ENTITY;

    @BeforeEach
    private void init() throws IOException {
        Constant.reloadFields(TEST_USER, encoder);
        Constant.reloadFields(TEST_NEW_PASSWORD_DTO, encoder);
        Constant.reloadFields(TEST_UPDATE_USER);

        TEST_FILE = Constant.reloadFields(TEST_FILE
                , "1.jpg"
                , ".\\src\\main\\resources\\images\\photos\\1.jpg"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , new byte[]{1, 0, 1, 0, 1, 1, 1, 1, 0});

        TEST_PHOTO = Constant.reloadFields(TEST_FILE);
        TEST_AUTHENTICATION = Constant.reloadFields(TEST_AUTHENTICATION, TEST_USER);
    }

    @Test
    public void trySetPasswordIsNotMatch() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);

        TEST_USER.setPassword(encoder.encode("NOT MATCH PASSWORD!"));

        Assertions.assertThrows(PasswordIsNotMatchException.class
                , () -> userService.setPassword(TEST_NEW_PASSWORD_DTO, TEST_AUTHENTICATION)
        );
    }

    @Test
    public void setPassword() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(null);

        userService.setPassword(TEST_NEW_PASSWORD_DTO, TEST_AUTHENTICATION);

        Assertions.assertFalse(encoder.matches(
                        encoder.encode(TEST_NEW_PASSWORD_DTO.getNewPassword())
                        , TEST_USER.getPassword()
                )
        );
    }

    @Test
    public void tryGetUserNotFound() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(null);

        Assertions.assertThrows(UserNotFoundException.class
                , () -> userService.getUser(TEST_AUTHENTICATION.getName())
        );
    }

    @Test
    public void getUser() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(null);

        Assertions.assertEquals(userService.getUser(TEST_AUTHENTICATION.getName()), TEST_USER);
    }

    @Test
    public void updateUser() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(null);

        Assertions.assertEquals(userService.updateUser(TEST_UPDATE_USER, TEST_AUTHENTICATION), TEST_USER);
    }

    @Test
    public void updateUserImage() throws IOException {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(null);
        Mockito.when(photoRepository.save(Mockito.any(PhotoEntity.class))).thenReturn(null);
        Mockito.when(userMapper.mapMuptipartFileToPhoto(Mockito.any(MultipartFile.class))).thenReturn(TEST_PHOTO);

        TEST_PHOTO.setId(1);

        userService.updateUserImage(TEST_FILE, TEST_AUTHENTICATION);

        Assertions.assertEquals(TEST_PHOTO, TEST_USER.getPhoto());
    }

}
