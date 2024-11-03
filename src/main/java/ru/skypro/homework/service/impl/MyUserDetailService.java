package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.controller.AdController;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.utils.LogShifter;

import java.util.List;

@Service
@Slf4j
public class MyUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(MyUserDetailService.class);
    private final LogShifter shifter = LogShifter.getLogShifter();

    public MyUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override//вызываем в методе логин
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        shifter.shiftLog(logger, "Запущен метод MyUserDetailService.loadUserByUsername(): {}", username);

        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            shifter.shiftBackLog(logger, "Пользователь не найден!");
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        List<GrantedAuthority> grantedAuthorityList = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_" + user.getRole());

        shifter.shiftBackLog(logger, "Выполнен метод MyUserDetailService.loadUserByUsername() {}", user);
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .authorities(grantedAuthorityList)
                .build();
    }
}