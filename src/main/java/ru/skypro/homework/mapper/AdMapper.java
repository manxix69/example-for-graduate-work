package ru.skypro.homework.mapper;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.controller.UserController;
import ru.skypro.homework.contstants.Constants;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.exception.UserNotFoundException;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;

@Service
@AllArgsConstructor
public class AdMapper {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    private final Logger logger = LoggerFactory.getLogger(AdMapper.class);


    /**
     * Entity -> dto mapping
     *
     * @param entity AdEntity entity class
     * @return Ad dto class
     */
    public Ad mapToAdDto(AdEntity entity) {
        logger.info("start method mapToAdDto: {}", entity);

        Ad dto = new Ad();
        dto.setAuthor(entity.getAuthor().getId());
        dto.setImage(Constants.URL_PHOTO_CONSTANT + entity.getPhoto().getId());
        dto.setPk(entity.getId());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());

        logger.info("end method mapToAdDto: {}", dto);
        return dto;
    }

    /**
     * Dto -> entity mapping without image.
     * Image will be saved separately because it needs a created ad with id.
     *
     * @param dto CreateAds dto class
     * @return AdEntity entity class
     */
    public AdEntity mapToAdEntity(CreateOrUpdateAd dto, String username) {
        logger.info("start method mapToAdEntity: {}, {}", dto, username);

        UserEntity author = userRepository.findByUsername(username);
        if (author == null) {
            throw new UserNotFoundException("User not found");
        }
        AdEntity entity = new AdEntity();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setAuthor(author);

        logger.info("end method mapToAdEntity: {}", entity);
        return entity;
    }

    /**
     * AdEntity entity -> ExtendedAd dto mapping
     *
     * @param entity AdEntity entity class
     * @return ExtendedAd dto class
     */
    public ExtendedAd mapToExtendedAdDto(AdEntity entity) {
        logger.info("start method mapToExtendedAdDto: {}", entity);

        ExtendedAd dto = new ExtendedAd();
        dto.setPk(entity.getId());
        dto.setAuthorFirstName(entity.getAuthor().getFirstName());
        dto.setAuthorLastName(entity.getAuthor().getLastName());
        dto.setDescription(entity.getDescription());
        dto.setEmail(entity.getAuthor().getUsername());
        dto.setImage(Constants.URL_PHOTO_CONSTANT + entity.getPhoto().getId());
        dto.setPhone(entity.getAuthor().getPhone());
        dto.setPrice(entity.getPrice());
        dto.setTitle(entity.getTitle());

        logger.info("end method mapToExtendedAdDto: {}", dto);
        return dto;
    }

    /**
     * Метод конвертирует {@link MultipartFile} в сущность {@link PhotoEntity}
     * @param image
     * @return photo сущность {@link PhotoEntity}
     * @throws IOException
     */
    public PhotoEntity mapMultipartFileToPhoto(MultipartFile image) throws IOException {
        logger.info("start method mapMultipartFileToPhoto: {}", image);

        PhotoEntity photo = new PhotoEntity();
        photo.setData(image.getBytes());
        photo.setMediaType(image.getContentType());
        photo.setFileSize(image.getSize());

        logger.info("end method mapMultipartFileToPhoto: {}", photo);
        return photo;
    }
}