package ru.skypro.homework.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.homework.model.PhotoEntity;
import ru.skypro.homework.repository.PhotoRepository;
import ru.skypro.homework.service.PhotoService;

import java.io.IOException;

import ru.skypro.homework.exception.PhotoOnDatabaseIsAbsentException;
import ru.skypro.homework.exception.PhotoOnPcIsAbsentException;
import ru.skypro.homework.utils.LogShifter;


@Service
@Slf4j
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final ImageServiceImpl imageService;
    private final Logger logger = LoggerFactory.getLogger(PhotoServiceImpl.class);
    private final LogShifter shifter = LogShifter.getLogShifter();

    public PhotoServiceImpl(PhotoRepository photoRepository, ImageServiceImpl imageService) {
        this.photoRepository = photoRepository;
        this.imageService = imageService;
    }

    /**
     * Метод возвращает фото с ПК, а если его там нет по каким-то причинам,
     * то перенаправляет запрос фото в базу данных.
     *
     * @param photoId
     * @return byte[] массив байт
     * @throws IOException
     */
    public byte[] getPhoto(Integer photoId) throws IOException {
        shifter.shiftLog(logger, "Запущен метод PhotoServiceImpl.getPhoto(): {}", photoId);

        byte[] data = null;
        PhotoEntity photo = null;
        try {
            photo = photoRepository.findById(photoId).orElseThrow(PhotoOnDatabaseIsAbsentException::new);
            shifter.log(logger, "Фото найдено - {}", photo.getData() != null);
            data = imageService.getPhotoFromDisk(photo);
            if (data == null) {  //Если картинка запрошенная с ПК не получена по какой-то причине, достаем ее из БД
                data = photoRepository.findById(photoId).orElseThrow(PhotoOnPcIsAbsentException::new).getData();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            shifter.shiftBackLog(logger, "выполнен метод PhotoServiceImpl.getPhoto(): {}, {}", photo, data);
        }
        return data; //Если предыдущее условие не выполнилось и с картинкой все в порядке, то достаем ее с ПК
    }

}