package ru.coolspot.chessknight.servlet;

import lombok.extern.slf4j.Slf4j;
import ru.coolspot.chessknight.exception.ValidationException;
import ru.coolspot.chessknight.service.ChessService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@WebServlet(urlPatterns = "/horse/servlet/count", loadOnStartup = 1)
public class CountServlet extends HttpServlet {
    private final ChessService chessService;

    public CountServlet(ChessService chessService) {
        this.chessService = chessService;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.info("{}: Запрос к эндпоинту '{}'", request.getRemoteAddr(), request.getRequestURI());
        String start = request.getParameter("start");
        String end = request.getParameter("end");
        String height = request.getParameter("height");
        String width = request.getParameter("width");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            out.println(chessService.getCount(width, height, start, end));
        } catch (ValidationException e) {
            out.println("Error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}