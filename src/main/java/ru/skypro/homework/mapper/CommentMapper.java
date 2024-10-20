package ru.skypro.homework.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.contstants.Constants;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.model.CommentEntity;


@Service
public class CommentMapper {
    private final Logger logger = LoggerFactory.getLogger(CommentMapper.class);

    /**
     * Entity -> dto mapping
     *
     * @param entity input entity class
     * @return dto class
     */
    public Comment mapToCommentDto(CommentEntity entity) {
        logger.info("start method mapToCommentDto: {}", entity);

        Comment dto = new Comment();
        dto.setAuthor(entity.getAuthor().getId());
        dto.setAuthorImage(Constants.URL_PHOTO_CONSTANT + entity.getAuthor().getPhoto().getId());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setPk(entity.getId());
        dto.setText(entity.getText());

        logger.info("end method mapToCommentDto: {}", dto);
        return dto;
    }
}