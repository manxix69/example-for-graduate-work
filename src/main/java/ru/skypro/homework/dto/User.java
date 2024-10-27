package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class User {

    @Schema(description = "id пользователя")
    private Integer id;            // id пользователя
    @Schema(description = "логин пользователя")
    private String email;       // логин пользователя
    @Schema(description = "имя пользователя")
    private String firstName;   // имя пользователя
    @Schema(description = "фамилия пользователя")
    private String lastName;    // фамилия пользователя
    @Schema(description = "телефон пользователя")
    private String phone;       // телефон пользователя
    @Schema(description = "роль пользователя" , subTypes = Role.class)
    private Role role;          // роль пользователя
    @Schema(description = "ссылка на аватар пользователя")
    private String image;       // ссылка на аватар пользователя

}