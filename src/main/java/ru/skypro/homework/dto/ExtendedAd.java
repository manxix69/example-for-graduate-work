package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExtendedAd {

    @Schema(description = "id объявления")
    private Integer pk;             // id объявления
    @Schema(description = "имя автора объявления")
    private String authorFirstName; // имя автора объявления
    @Schema(description = "фамилия автора объявления")
    private String authorLastName;  // фамилия автора объявления
    @Schema(description = "описание объявления")
    private String description;     // описание объявления
    @Schema(description = "логин автора объявления")
    private String email;           // логин автора объявления
    @Schema(description = "ссылка на картинку объявления")
    private String image;           // ссылка на картинку объявления
    @Schema(description = "телефон автора объявления")
    private String phone;           // телефон автора объявления
    @Schema(description = "цена объявления")
    private Integer price;          // цена объявления
    @Schema(description = "заголовок объявления")
    private String title;           // заголовок объявления

}