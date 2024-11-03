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
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.exception.UserAlreadyExistException;
import ru.skypro.homework.exception.WrongPasswordException;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.test.utils.Constant;

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

    private final UserEntity TEST_USER      = Constant.TEST_USER;
    private final Register TEST_REGISTER    = Constant.TEST_REGISTER;
    private final String TEST_PASSWORD      = Constant.TEST_PASSWORD;

    @BeforeEach
    private void init() {
        Constant.reloadFields(TEST_USER, encoder);
        Constant.reloadFields(TEST_REGISTER);
    }

    @Test
    public void tryLoginNotExistsUser() {
        Assertions.assertThrows(UsernameNotFoundException.class
                , () -> authService.login(TEST_USER.getUsername(), TEST_PASSWORD)
        );
    }

    @Test
    public void tryLoginWithWrongPassword() {
        Mockito.when(userRepository.findByUsername(TEST_USER.getUsername())).thenReturn(TEST_USER);

        Assertions.assertThrows(WrongPasswordException.class
                , () -> authService.login(TEST_USER.getUsername(), "wrong password!")
        );
    }
    @Test
    public void login() {
        Mockito.when(userRepository.findByUsername(TEST_USER.getUsername())).thenReturn(TEST_USER);

        Assertions.assertTrue(() -> authService.login(TEST_USER.getUsername(), TEST_PASSWORD));
    }

    @Test
    public void tryRegisterUserAlreadyExist(){
        Mockito.when(userRepository.existsByUsername(TEST_USER.getUsername())).thenReturn(true);

        Assertions.assertThrows(UserAlreadyExistException.class
                , () -> authService.register(TEST_REGISTER)
        );
    }

    @Test
    public void register(){
        Mockito.when(userRepository.existsByUsername(TEST_USER.getUsername())).thenReturn(false);
        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(null);

        Assertions.assertTrue(() -> authService.register(TEST_REGISTER));
    }
}
