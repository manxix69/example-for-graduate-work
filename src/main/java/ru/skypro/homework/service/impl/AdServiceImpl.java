package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.service.AdService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final PhotoRepository photoRepository;
    private final AdMapper adMapper;
    private final ImageServiceImpl imageService;
    private final UserServiceImpl userService;

    private final Logger logger = LoggerFactory.getLogger(AdServiceImpl.class);

    @Value("${path.to.photos.folder}")
    private String photoDir;

    public AdServiceImpl(AdRepository adRepository,
                         PhotoRepository photoRepository,
                         AdMapper adMapper,
                         ImageServiceImpl imageService,
                         UserServiceImpl userService) {
        this.adRepository = adRepository;
        this.photoRepository = photoRepository;
        this.adMapper = adMapper;
        this.imageService = imageService;
        this.userService = userService;
    }

    /**
     * Метод возвращает список всех объявлений в виде DTO {@link Ad}.
     *
     * @return возвращает все объявления из БД
     */
    @Override
    public Ads getAllAds() {
        logger.info("Запущен метод AdServiceImpl.getAllAds() ");

        List<Ad> dtos = adRepository.findAll().stream()
                .map(entity -> adMapper.mapToAdDto(entity))
                .collect(Collectors.toList());

        logger.info("Выполнен метод AdServiceImpl.getAllAds() {}", dtos.size());
        return new Ads(dtos.size(), dtos);
    }

    /**
     * Метод добавляет новое объявление в БД
     *
     * @param properties - DTO модель класса {@link CreateOrUpdateAd};
     * @param image      - фотография объявления
     * @return возвращает объявление в качестве DTO модели
     */
    @Override
    public Ad addAd(CreateOrUpdateAd properties,
                    MultipartFile image,
                    Authentication authentication) throws IOException {
        logger.info("Запущен метод AdServiceImpl.addAd(): {}, {}, {} " , properties, image, authentication.getName());

        AdEntity adEntity = new AdEntity(); //создаем сущность
        adEntity.setTitle(properties.getTitle()); //заполняем поля title, price и description, которые берутся из properties
        adEntity.setPrice(properties.getPrice());
        adEntity.setDescription(properties.getDescription());
        adEntity.setAuthor(userService.getUser(authentication.getName())); //заполняем поле author
        adEntity = (AdEntity) imageService.updateEntitiesPhoto(image, adEntity); //заполняем поля

        adRepository.save(adEntity); //сохранение сущности adEntity в БД

        logger.info("Выполнен метод AdServiceImpl.addAd(): {} " , adEntity);
        return adMapper.mapToAdDto(adEntity); //возврат ДТО Ad из метода
    }


    /**
     * Метод получает информацию об объявлении по id
     *
     * @param id - id объявления
     * @return возвращает DTO модель объявления
     */
    @Override
    public ExtendedAd getAds(Integer id) {
        logger.info("Запущен метод AdServiceImpl.getAds(): {} " , id);

        AdEntity entity = adRepository.findById(id).get();

        logger.info("Выполнен метод AdServiceImpl.getAds(): {} " , entity);
        return adMapper.mapToExtendedAdDto(entity);
    }

    /**
     * Метод удаляет объявление по id
     *
     * @param id - id объявления
     * @return boolean
     */
    @Transactional
    @Override
    public boolean removeAd(Integer id) throws IOException {
        logger.info("Запущен метод AdServiceImpl.removeAd(): {} " , id);

        boolean result;
        AdEntity ad = adRepository.findById(id).get();
        if (ad != null) {
            adRepository.delete(ad); //удаление объявления из БД
            photoRepository.delete(ad.getPhoto()); //удаление фото из БД
            String filePath = ad.getFilePath();  //получение пути из сущности объявления
            Path path = Path.of(filePath); //создание пути к файлу
            Files.delete(path); //удаление файла с ПК
            result = true;
        } else {
            result = false;
        }

        logger.info("Выполнен метод AdServiceImpl.removeAd(): {} " , result);
        return result;
    }

    /**
     * Метод изменяет объявление
     *
     * @param id  - id объявления
     * @param dto - DTO модель класса {@link CreateOrUpdateAd};
     * @return возвращает DTO модель объявления
     */
    @Transactional
    @Override
    public Ad updateAds(Integer id, CreateOrUpdateAd dto) {
        logger.info("Запущен метод AdServiceImpl.updateAds(): {}, {} " , id, dto);
        
        AdEntity entity = adRepository.findById(id).get();

        entity.setTitle(dto.getTitle());
        entity.setPrice(dto.getPrice());
        entity.setDescription(dto.getDescription());

        adRepository.save(entity);

        logger.info("Выполнен метод AdServiceImpl.updateAds(): {} " , entity);
        return adMapper.mapToAdDto(entity);
    }

    /**
     * Метод получает все объявления данного пользователя
     *
     * @param username - логин пользователя
     * @return возвращает DTO - список моделей объявления пользователя
     */
    @Override
    @Transactional
    public Ads getAdsMe(String username) {
        logger.info("Запущен метод AdServiceImpl.getAdsMe(): {}" , username);
        
        UserEntity author = userService.getUser(username);

        List<Ad> ads = null;
        ads = adRepository.findByAuthor(author).stream()
                .map(ad -> adMapper.mapToAdDto(ad))
                .collect(Collectors.toList());
        Ads adsDto = new Ads(ads.size(), ads);

        logger.info("Выполнен метод AdServiceImpl.getAdsMe(): {}" , adsDto);
        return adsDto;
    }

    @Transactional
    @Override
    public void updateImage(Integer id, MultipartFile image) throws IOException {
        logger.info("Запущен метод AdServiceImpl.updateImage(): {}, {}" , id, image);

        AdEntity adEntity = adRepository.findById(id).orElseThrow(RuntimeException::new); //достаем объявление из БД
        adEntity = (AdEntity) imageService.updateEntitiesPhoto(image, adEntity); //заполняю поля и получаю сущность в переменную
        adRepository.save(adEntity); //сохранение сущности user в БД
        logger.info("Выполнен метод AdServiceImpl.updateImage(): {}" , adEntity);
    }

    public boolean isAuthorAd(String username, Integer adId) {
        logger.info("Запущен метод AdServiceImpl.isAuthorAd(): {}, {}" , username, adId);

        AdEntity adEntity = adRepository.findById(adId).orElseThrow(RuntimeException::new);

        logger.info("Выполнен метод AdServiceImpl.isAuthorAd(): {}" , adEntity);
        return adEntity.getAuthor().getUsername().equals(username);
    }
}