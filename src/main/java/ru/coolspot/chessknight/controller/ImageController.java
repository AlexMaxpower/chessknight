package ru.coolspot.chessknight.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.coolspot.chessknight.service.ChessService;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/horse/rest/image")
@Slf4j
public class ImageController {
    private final ChessService chessService;

    public ImageController(ChessService chessService) {
        this.chessService = chessService;
    }

    @GetMapping
    public ResponseEntity<byte[]> getImage(@RequestParam String width, @RequestParam String height,
                                           @RequestParam String start, @RequestParam String end,
                                           HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}'", request.getRemoteAddr(), request.getRequestURI());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(chessService.getImage(width, height, start, end));
    }
}
