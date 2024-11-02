package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class Comment {

    @Schema(description = "id автора комментария")
    private Integer author;         // id автора комментария
    @Schema(description = "ссылка на аватар автора комментария")
    private String authorImage;     // ссылка на аватар автора комментария
    @Schema(description = "имя создателя комментария")
    private String authorFirstName; // имя создателя комментария
    @Schema(description = "дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970")
    private Long createdAt;         // дата и время создания комментария в миллисекундах с 00:00:00 01.01.1970
    @Schema(description = "id комментария")
    private Integer pk;             // id комментария
    @Schema(description = "текст комментария")
    private String text;            // текст комментария

}