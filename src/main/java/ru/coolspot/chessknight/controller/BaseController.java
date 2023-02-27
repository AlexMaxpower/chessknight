package ru.coolspot.chessknight.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.coolspot.chessknight.service.ChessService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/horse/rest")
@RequiredArgsConstructor
@Slf4j
public class BaseController {

    private final ChessService chessService;

    @GetMapping("/count")
    public String getCount(@RequestParam String width, @RequestParam String height,
                            @RequestParam String start, @RequestParam String end,
                            HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}'", request.getRemoteAddr(), request.getRequestURI());
        return chessService.getCount(width, height, start, end);
    }

    @GetMapping("/way")
    public String getWay(@RequestParam String width, @RequestParam String height,
                            @RequestParam String start, @RequestParam String end,
                            HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}'", request.getRemoteAddr(), request.getRequestURI());
        return chessService.getWay(width, height, start, end);
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(@RequestParam String width, @RequestParam String height,
                                           @RequestParam String start, @RequestParam String end,
                                           HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}'", request.getRemoteAddr(), request.getRequestURI());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(chessService.getImage(width, height, start, end));
    }
}