package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(
            tags = "Авторизация",
            summary = "Авторизация пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь авторизован",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Пользователь не авторизован (unauthorized)",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        logger.info("Запущен метод контроллера: login {}", login.getUsername());

        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @Operation(
            tags = "Регистрация",
            summary = "Регистрация пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content()
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Запрос к серверу содержит синтаксическую ошибку(bad request)",
                            content = @Content()
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Register register) {
        logger.info("За запущен метод контроллера: register : {}", register.getUsername());

        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}