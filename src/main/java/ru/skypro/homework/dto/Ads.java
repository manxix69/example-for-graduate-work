package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Ads {

    @Schema(description = "общее количество объявлений")
    private int count;          // общее количество объявлений
    @Schema(subTypes = Ad.class)
    private List<Ad> results;   // Ad[]

}