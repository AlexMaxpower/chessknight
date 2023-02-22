package ru.coolspot.chessknight.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.coolspot.chessknight.service.ChessService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(path = "/horse/rest/count")
@RequiredArgsConstructor
@Slf4j
public class CountController {

    private final ChessService chessService;

    @GetMapping
    public Integer getCount(@RequestParam String width, @RequestParam String height,
                            @RequestParam String start, @RequestParam String end,
                            HttpServletRequest request) {
        log.info("{}: Запрос к эндпоинту '{}'", request.getRemoteAddr(), request.getRequestURI());
        return chessService.getCount(width, height, start, end);
    }
}