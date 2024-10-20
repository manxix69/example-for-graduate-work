package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skypro.homework.dto.Comment;
import ru.skypro.homework.dto.Comments;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.CommentEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;
    private final UserServiceImpl userService;
    private final Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);

    public CommentServiceImpl(CommentRepository commentRepository,
                              CommentMapper commentMapper,
                              AdRepository adRepository,
                              UserRepository userRepository,
                              UserServiceImpl userService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Метод получает список всех комментариев объявления
     *
     * @param id - id объявления
     * @return возвращает DTO - список моделей комментариев
     */
    @Transactional
    @Override
    public Comments getComments(Integer id) {
        logger.info("Запущен метод CommentServiceImpl.getComments(): {}" , id);

        List<Comment> comments = commentRepository.findByAdId(id).stream()
                .map(comment -> commentMapper.mapToCommentDto(comment))
                .collect(Collectors.toList());

        logger.info("выполнен метод CommentServiceImpl.getComments(): {}" , comments.size());
        return new Comments(comments.size(), comments);
    }

    /**
     * Метод добавляет комментарий к объявлению
     *
     * @param id                    - id объявления
     * @param createOrUpdateComment - DTO модель класса {@link CreateOrUpdateComment}
     * @param username              - логин пользователя
     * @return DTO модель комментария
     */
    @Transactional
    @Override
    public Comment addComment(Integer id, CreateOrUpdateComment createOrUpdateComment, String username) {
        logger.info("Запущен метод CommentServiceImpl.addComment(): {}, {}, {}" , id, createOrUpdateComment, username);

        UserEntity author = userService.getUser(username);//todo заменить метод на getUser
        AdEntity ad = adRepository.findById(id).orElse(null);

        CommentEntity commentEntity = new CommentEntity(); //Создаем сущность comment и заполняем поля
        commentEntity.setAuthor(author);
        commentEntity.setAd(ad);
        commentEntity.setText(createOrUpdateComment.getText());
        commentEntity.setCreatedAt(System.currentTimeMillis());

        commentRepository.save(commentEntity); //Сохраняем сущность commentEntity в БД

        author.getComments().add(commentEntity); //Заполняем поле с комментариями у пользователя и сохраняем в БД
        userRepository.save(author);

        Integer avatarId = author.getPhoto().getId();
        Comment commentDTO = new Comment(); //Создаем возвращаемую сущность ДТО comment и заполняем поля
        commentDTO.setAuthor(author.getId());
        commentDTO.setAuthorImage("/photo/image/" + avatarId);
        commentDTO.setAuthorFirstName(author.getFirstName());
        commentDTO.setCreatedAt(commentEntity.getCreatedAt());
        commentDTO.setPk(commentRepository.findFirstByText(createOrUpdateComment.getText()).getId());
        commentDTO.setText(commentRepository.findFirstByText(createOrUpdateComment.getText()).getText());

        logger.info("Выполнен метод CommentServiceImpl.addComment(): {}, {}, {}" , commentDTO, author, commentEntity);
        return commentDTO;
    }

    /**
     * Метод удаляет комментарий
     *
     * @param commentId - id комментария
     * @param username  - логин пользователя
     * @return строку с результатом выполнения метода
     */
    @Override
    public String deleteComment(Integer commentId, String username) {
        logger.info("Запущен метод CommentServiceImpl.deleteComment(): {}, {}" , commentId, username);

        Optional<CommentEntity> comment = commentRepository.findById(commentId);
        if (comment.isPresent()) {
            UserEntity author = userService.getUser(username);
            if (author.getRole().equals(Role.ADMIN)) {
                commentRepository.delete(comment.get());
                return "комментарий удален";
            } else if (author.getRole().equals(Role.USER)) {
                if (comment.get().getAuthor().getUsername().equals(author.getUsername())) {
                    commentRepository.delete(comment.get());
                    return "комментарий удален";
                } else {
                    return "forbidden"; //'403' For the user deletion is forbidden
                }
            }
        }
        logger.info("Выполнен метод CommentServiceImpl.deleteComment()");
        return "not found"; //'404' Comment not found
    }

    /**
     * Метод обновляет комментарий
     *
     * @param commentId             - id комментария
     * @param createOrUpdateComment - DTO модель класса {@link CreateOrUpdateComment}
     * @param username              - логин пользователя
     * @return строку с результатом выполнения метода
     */
    @Transactional
    @Override
    public Comment updateComment(Integer commentId, CreateOrUpdateComment createOrUpdateComment, String username) {
        logger.info("Запущен метод CommentServiceImpl.updateComment(): {}, {}, {}" , commentId, createOrUpdateComment, username);

        Optional<CommentEntity> commentOptional = commentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            CommentEntity comment = commentOptional.get();
            UserEntity author = userService.getUser(username);
            if (author.getComments().contains(comment)) {
                comment.setText(createOrUpdateComment.getText());
                commentRepository.save(comment);
                return commentMapper.mapToCommentDto(comment); //'200' Ok, comment updated
            } else {
                return commentMapper.mapToCommentDto(comment); //'403' For the user update is forbidden
            }
        }
        logger.info("Выполнен метод CommentServiceImpl.updateComment(): {}" , commentOptional);
        return null; //'404' Comment not found
    }
}