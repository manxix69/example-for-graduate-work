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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.exception.WrongPasswordException;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;

import javax.persistence.*;
import java.util.Collection;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTests {
    @Autowired
    private AuthServiceImpl authService;
    @Autowired
    private PasswordEncoder encoder;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailService myUserDetailService;

    private final UserEntity TEST_USER = new UserEntity();

    @BeforeEach
    private void init() {
        TEST_USER.setId(1);
        TEST_USER.setUsername("user@mail.ru");
        TEST_USER.setPassword("1234567890");
        TEST_USER.setFirstName("IVAN");
        TEST_USER.setLastName("IVANOV");
        TEST_USER.setPhone("+7(000)-000-00-00");
        TEST_USER.setRole(Role.USER);
        TEST_USER.setPhoto(null);
        TEST_USER.setFilePath(null);
    }

    @Test
    public void tryLoginNotExistsUser() {
        Assertions.assertThrows(UsernameNotFoundException.class
                , () -> authService.login(TEST_USER.getUsername(), TEST_USER.getPassword())
        );
    }

    @Test
    public void tryLoginWithWrongPassword() {
        Mockito.when(userRepository.findByUsername(TEST_USER.getUsername())).thenReturn(TEST_USER);

        Assertions.assertThrows(WrongPasswordException.class
                , () -> authService.login(TEST_USER.getUsername(), "wrong!")
        );
    }
    @Test
    public void login() {
        Mockito.when(userRepository.findByUsername(TEST_USER.getUsername())).thenReturn(TEST_USER);
        Assertions.assertTrue(() -> authService.login(TEST_USER.getUsername(), TEST_USER.getPassword()));
    }



}
