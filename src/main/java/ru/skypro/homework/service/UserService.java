package ru.skypro.homework.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.model.User;

public interface UserService {
    User save(User user);

    User create(User user);

    User getByUsername(String username);

    UserDetailsService userDetailsService();

    User getCurrentUser();

    boolean setNewPassword(NewPassword newPassword, Authentication authentication);

    boolean userExistsByUsername(String userName);
}
