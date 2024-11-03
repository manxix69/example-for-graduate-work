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
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.test.utils.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AdServiceImplTests {
    @Autowired
    private AdServiceImpl adService;
    @MockBean
    private AdRepository adRepository;
    @MockBean
    private PhotoRepository photoRepository;
    @Autowired
    private AdMapper adMapper;
    @Autowired
    private ImageServiceImpl imageService;
    @Autowired
    private UserServiceImpl userService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @MockBean
    private UserMapper userMapper;

    private final UserEntity TEST_USER = Constant.TEST_USER;
    private final String TEST_OLD_PASSWORD = Constant.TEST_PASSWORD;
    private final String TEST_NEW_PASSWORD = Constant.TEST_NEW_PASSWORD;
    private final NewPassword TEST_NEW_PASSWORD_DTO = Constant.TEST_NEW_PASSWORD_DTO;
    private final UpdateUser TEST_UPDATE_USER = Constant.TEST_UPDATE_USER;
    private final CreateOrUpdateAd TEST_CREATE_OR_UPDATE_AD = Constant.TEST_CREATE_OR_UPDATE_AD;
    private final Ad TEST_AD = Constant.TEST_AD;
    private final AdEntity TEST_AD_ENTITY = Constant.TEST_AD_ENTITY;

    private Authentication TEST_AUTHENTICATION = Constant.TEST_AUTHENTICATION;
    private MultipartFile TEST_FILE = Constant.TEST_MULTIPART_FILE;
    private PhotoEntity TEST_PHOTO = Constant.TEST_PHOTO_ENTITY;


    @BeforeEach
    private void init() throws IOException {
        Constant.reloadFields(TEST_USER, encoder);
        Constant.reloadFields(TEST_NEW_PASSWORD_DTO, encoder);
        Constant.reloadFields(TEST_UPDATE_USER);
        Constant.reloadFields(TEST_CREATE_OR_UPDATE_AD);

        TEST_FILE = Constant.reloadFields(TEST_FILE
                , "1.jpg"
                , ".\\src\\main\\resources\\images\\photos\\1.jpg"
                , MediaType.MULTIPART_FORM_DATA_VALUE
                , new byte[]{1, 0, 1, 0, 1, 1, 1, 1, 0});


        TEST_PHOTO = Constant.reloadFields(TEST_FILE);
        TEST_AUTHENTICATION = Constant.reloadFields(TEST_AUTHENTICATION, TEST_USER);

        Constant.reloadFields(TEST_AD);
        Constant.reloadFields(TEST_AD_ENTITY);
    }


    @Test
    public void addAd() throws IOException {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(adRepository.save(Mockito.any(AdEntity.class))).thenReturn(null);

        Mockito.when(photoRepository.save(Mockito.any(PhotoEntity.class))).thenReturn(null);
        Mockito.when(userMapper.mapMuptipartFileToPhoto(Mockito.any(MultipartFile.class))).thenReturn(TEST_PHOTO);

        TEST_PHOTO.setId(1);

        Assertions.assertEquals(adService.addAd(TEST_CREATE_OR_UPDATE_AD, TEST_FILE, TEST_AUTHENTICATION),
                TEST_AD
        );
    }

    @Test
    public void tryRemoveAd() throws IOException {
        Mockito.when(adRepository.findById(1)).thenReturn(Optional.ofNullable(null));
        Assertions.assertFalse(adService.removeAd(1));
    }

    @Test
    public void removeAd() throws IOException {
        Mockito.when(adRepository.findById(1)).thenReturn(Optional.of(TEST_AD_ENTITY));
        Assertions.assertTrue(adService.removeAd(1));
    }

    @Test
    public void updateAds() throws IOException {
        Mockito.when(adRepository.findById(1)).thenReturn(Optional.of(TEST_AD_ENTITY));
        Mockito.when(adRepository.save(Mockito.any(AdEntity.class))).thenReturn(null);

        TEST_AD.setPk(1);

        Assertions.assertEquals(adService.updateAds(1, TEST_CREATE_OR_UPDATE_AD).getAuthor(), TEST_AD.getAuthor());
        Assertions.assertEquals(adService.updateAds(1, TEST_CREATE_OR_UPDATE_AD).getPk(), TEST_AD.getPk());
        Assertions.assertEquals(adService.updateAds(1, TEST_CREATE_OR_UPDATE_AD).getPrice(), TEST_AD.getPrice());
        Assertions.assertEquals(adService.updateAds(1, TEST_CREATE_OR_UPDATE_AD).getTitle(), TEST_AD.getTitle());
    }

    @Test
    public void getAdsMe() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(adRepository.findByAuthor(Mockito.any(UserEntity.class))).thenReturn(new ArrayList<>());

        Assertions.assertEquals(adService.getAdsMe(TEST_AUTHENTICATION.getName()).getCount(), 0);
    }

    @Test
    public void updateImage() throws IOException {
        Mockito.when(adRepository.findById(1)).thenReturn(Optional.of(TEST_AD_ENTITY));
        Mockito.when(adRepository.save(Mockito.any(AdEntity.class))).thenReturn(null);

        Mockito.when(photoRepository.save(Mockito.any(PhotoEntity.class))).thenReturn(null);
        Mockito.when(userMapper.mapMuptipartFileToPhoto(Mockito.any(MultipartFile.class))).thenReturn(TEST_PHOTO);

        TEST_PHOTO.setId(1);

        adService.updateImage(1, TEST_FILE);

        Assertions.assertDoesNotThrow(() -> adService.updateImage(1, TEST_FILE));
    }

    @Test
    public void isAuthorAd() {
        Mockito.when(adRepository.findById(1)).thenReturn(Optional.of(TEST_AD_ENTITY));
        Mockito.when(adRepository.save(Mockito.any(AdEntity.class))).thenReturn(null);

        TEST_AD_ENTITY.setId(1);

        Assertions.assertTrue(adService.isAuthorAd(TEST_AUTHENTICATION.getName(), TEST_AD_ENTITY.getId()));
    }

    @Test
    public void getAllAds() {
        Mockito.when(adRepository.findAll()).thenReturn(new ArrayList<>());

        Assertions.assertEquals(adService.getAllAds().getCount(), 0);
    }

}
