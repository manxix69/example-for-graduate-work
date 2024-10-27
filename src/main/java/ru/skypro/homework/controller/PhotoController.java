package ru.skypro.homework.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.service.impl.PhotoServiceImpl;
import ru.skypro.homework.utils.LogShifter;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/photo")
@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
public class PhotoController {
    private final PhotoServiceImpl photoService;

    private final Logger logger = LoggerFactory.getLogger(PhotoController.class);
    private final LogShifter shifter = LogShifter.getLogShifter();

    @GetMapping("/image/{photoId}")
    public ResponseEntity<byte[]> getPhotoFromSource(@PathVariable Integer photoId) throws IOException {
        shifter.log(logger,"Запущен метод контроллера getPhotoFromSource {}", photoId);

        return ResponseEntity.ok(photoService.getPhoto(photoId));
    }
}
