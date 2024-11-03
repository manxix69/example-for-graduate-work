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
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.test.utils.Constant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class CommentServiceImplTests {
    @Autowired
    private CommentServiceImpl commentService;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private AdRepository adRepository;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private UserServiceImpl userService;
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


    private CommentEntity TEST_COMMENT_ENTITY = Constant.TEST_COMMENT_ENTITY;
    private Comment TEST_COMMENT = Constant.TEST_COMMENT;
    private CreateOrUpdateComment TEST_CREATE_OR_UPDATE_COMMENT = Constant.TEST_CREATE_OR_UPDATE_COMMENT;

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

        TEST_COMMENT = Constant.reloadFields(TEST_COMMENT);
        TEST_COMMENT_ENTITY = Constant.reloadFields(TEST_COMMENT_ENTITY);
        TEST_CREATE_OR_UPDATE_COMMENT = Constant.reloadFields(TEST_CREATE_OR_UPDATE_COMMENT);
    }


    @Test
    public void getComments() {
        Mockito.when(commentRepository.findByAdId(1)).thenReturn(new ArrayList<>());

        Assertions.assertEquals(commentService.getComments(1).getCount(), 0);
    }


    @Test
    public void addComment() throws IOException {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(adRepository.findById(1)).thenReturn(Optional.of(TEST_AD_ENTITY));
        Mockito.when(commentRepository.save(Mockito.any(CommentEntity.class))).thenReturn(null);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(TEST_USER);
        Mockito.when(commentRepository.findFirstByText(TEST_CREATE_OR_UPDATE_COMMENT.getText())).thenReturn(TEST_COMMENT_ENTITY);


        Comment actual = commentService.addComment(1, TEST_CREATE_OR_UPDATE_COMMENT, TEST_AUTHENTICATION.getName());

        Assertions.assertEquals(actual.getAuthor(), TEST_COMMENT.getAuthor());
        Assertions.assertEquals(actual.getAuthorFirstName(), TEST_COMMENT.getAuthorFirstName());
        Assertions.assertEquals(actual.getPk(), TEST_COMMENT.getPk());
        Assertions.assertEquals(actual.getText(), TEST_COMMENT.getText());
    }

    @Test
    public void tryDeleteCommentNotFound() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(null));

        Assertions.assertEquals("not found", commentService.deleteComment(1, TEST_AUTHENTICATION.getName()));
    }

    @Test
    public void tryDeleteCommentForbidden() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(TEST_COMMENT_ENTITY));

        UserEntity user = new UserEntity();
        user.setUsername("other_USER_NAME");
        TEST_COMMENT_ENTITY.setAuthor(user);

        Assertions.assertEquals("forbidden", commentService.deleteComment(1, TEST_AUTHENTICATION.getName()));
    }

    @Test
    public void deleteComment() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(TEST_COMMENT_ENTITY));

        Assertions.assertEquals("комментарий удален", commentService.deleteComment(1, TEST_AUTHENTICATION.getName()));
    }


    @Test
    public void tryUpdateCommentNotFound() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(null));

        Assertions.assertNull(commentService.updateComment(1, TEST_CREATE_OR_UPDATE_COMMENT, TEST_AUTHENTICATION.getName()));
    }

    @Test
    public void updateComment() {
        Mockito.when(userRepository.findByUsername(TEST_AUTHENTICATION.getName())).thenReturn(TEST_USER);
        Mockito.when(commentRepository.findById(1)).thenReturn(Optional.ofNullable(TEST_COMMENT_ENTITY));
        Mockito.when(commentRepository.save(Mockito.any(CommentEntity.class))).thenReturn(null);

        Assertions.assertNotNull(commentService.updateComment(1, TEST_CREATE_OR_UPDATE_COMMENT, TEST_AUTHENTICATION.getName()));
    }

}
