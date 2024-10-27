package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Login {

    @Schema(minLength = 4, maxLength = 32, description = "логин")
    private String username;
    @Schema(minLength = 8, maxLength = 16, description = "пароль")
    private String password;

}