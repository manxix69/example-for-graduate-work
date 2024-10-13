package ru.skypro.homework.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.User;
import ru.skypro.homework.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserServiceImpl service;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserServiceImpl service,
                           PasswordEncoder passwordEncoder) {
        this.service = service;
        this.encoder = passwordEncoder;
    }

    @Override
    public boolean login(String userName, String password) {
        logger.info("login: {}", userName);
        if (!service.userExistsByUsername(userName)) {
            return false;
        }
        UserDetails userDetails = service.getByUsername(userName);
        return encoder.matches(password, userDetails.getPassword());
    }

    @Override
    public boolean register(Register register) {
        logger.info("register: {}", register);
        if (service.userExistsByUsername(register.getUsername())) {
            return false;
        }
        User user = User.builder()
                        .username(register.getUsername())
                        .email(register.getUsername())
                        .password(encoder.encode(register.getPassword()))
                        .firstName(register.getFirstName())
                        .lastName(register.getLastName())
                        .phone(register.getPhone())
                        .role(register.getRole())
                        .build()
        ;
        service.create(user);
        return true;
    }


}
