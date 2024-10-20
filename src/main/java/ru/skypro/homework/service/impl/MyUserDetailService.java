package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.controller.AdController;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;

@Service
@Slf4j
public class MyUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(MyUserDetailService.class);

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override//вызываем в методе логин
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Запущен метод MyUserDetailService.loadUserByUsername(): {}" , username);

        UserEntity user = userRepository.findByUsername(username).get();
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}