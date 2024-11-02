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
import ru.skypro.homework.exception.PhotoOnDatabaseIsAbsentException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.test.utils.Constant;

import java.io.IOException;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class PhotoServiceImplTests {
    @Autowired
    private PhotoServiceImpl photoService;
    @MockBean
    private PhotoRepository photoRepository;
    @Autowired
    private ImageServiceImpl imageService;

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
//        Constant.reloadFields(TEST_USER, encoder);
//        Constant.reloadFields(TEST_NEW_PASSWORD_DTO, encoder);
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
    public void tryGetPhoto() {
        Mockito.when(photoRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(PhotoOnDatabaseIsAbsentException.class
                , () -> photoService.getPhoto(1)
        );
    }

    @Test
    public void getPhoto() throws IOException {
        Mockito.when(photoRepository.findById(1)).thenReturn(Optional.of(TEST_PHOTO));

        TEST_PHOTO.setId(1);
        TEST_PHOTO.setFilePath(".\\src\\main\\resources\\images\\photos\\1.jpg");

        Assertions.assertEquals(photoService.getPhoto(1).length, TEST_PHOTO.getData().length);
    }
}
