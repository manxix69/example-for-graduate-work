package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Ad {

    @Schema(description = "id автора объявления")
    private Integer author;        // id автора объявления
    @Schema(description = "ссылка на картинку объявления")
    private String image;       // ссылка на картинку объявления
    @Schema(description = "id объявления")
    private Integer pk;         // id объявления
    @Schema(description = "цена объявления")
    private Integer price;      // цена объявления
    @Schema(description = "заголовок объявления")
    private String title;       // заголовок объявления

}