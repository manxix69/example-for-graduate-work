package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Comments {

    @Schema(description = "общее количество комментариев")
    private int count;              // общее количество комментариев
    @Schema(subTypes = Comment.class)
    private List<Comment> results;  // Comment[]

}