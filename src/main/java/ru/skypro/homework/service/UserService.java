package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.model.UserEntity;

public interface UserService {
    UserEntity save(UserEntity user);

    UserEntity create(UserEntity user);

    UserEntity getByUsername(String username);

    UserDetailsService userDetailsService();

    UserEntity getCurrentUser();

    boolean setNewPassword(NewPassword newPassword, Authentication authentication);

    boolean userExistsByUsername(String userName);
}
