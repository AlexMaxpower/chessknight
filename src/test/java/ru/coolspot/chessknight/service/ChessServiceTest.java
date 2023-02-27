package ru.coolspot.chessknight.service;

import org.junit.jupiter.api.Test;
import ru.coolspot.chessknight.exception.ValidationException;
import ru.coolspot.chessknight.service.impl.ChessServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChessServiceTest {
    final ChessService cs = new ChessServiceImpl();

    @Test
    public void shouldReturnCorrectDistance() {
        assertEquals("1", cs.getCount("8", "8", "B3", "A1"),
                "Дистанция должна быть равна 1 ходу");
    }

    @Test
    public void shouldExceptionWhenWidthZero() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("0", "8", "B3", "A1"));
        assertEquals("Размеры доски должны быть больше нуля!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenHeightZero() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("8", "0", "B3", "A1"));
        assertEquals("Размеры доски должны быть больше нуля!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenWidthAndHeightZero() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("0", "0", "B3", "A1"));
        assertEquals("Размеры доски должны быть больше нуля!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenWidthNotPositive() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("-1", "1", "B3", "A1"));
        assertEquals("Размеры доски должны быть больше нуля!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenHeightNotPositive() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("1", "-1", "B3", "A1"));
        assertEquals("Размеры доски должны быть больше нуля!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenWidthAndHeightNotPositive() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("-1", "-1", "B3", "A1"));
        assertEquals("Размеры доски должны быть больше нуля!",
                exp.getMessage());
    }

    @Test
    public void shouldReturnZeroWhenStartEqualsEnd() {
        assertEquals("0", cs.getCount("8", "8", "A1", "A1"),
                "Дистанция должна быть равна 0");
    }

    @Test
    public void shouldExceptionWhenStartNotOnBoardHorizontal() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("2", "2", "C3", "A1"));
        assertEquals("Стартовая позиция по горизонтали невозможна!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenEndNotOnBoardHorizontal() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("2", "2", "A1", "C3"));
        assertEquals("Конечная позиция по горизонтали невозможна!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenStartNotOnBoardVertical() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("2", "2", "A3", "A1"));
        assertEquals("Стартовая позиция по вертикали невозможна!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenEndNotOnBoardVertical() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("2", "2", "A1", "A3"));
        assertEquals("Конечная позиция по вертикали невозможна!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenWidthNotNumber() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("A", "2", "A1", "A3"));
        assertEquals("Ширина доски должна быть числом!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenHeightNotNumber() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("2", "A", "A1", "A3"));
        assertEquals("Высота доски должна быть числом!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenWidthBlank() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("", "2", "A1", "A3"));
        assertEquals("Ширина доски должна быть числом!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenHeightBlank() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> cs.getCount("2", "", "A1", "A3"));
        assertEquals("Высота доски должна быть числом!",
                exp.getMessage());
    }

    @Test
    public void shouldReturnNegativeWhenEndUnreachable() {
        assertEquals("-1", cs.getCount("2", "3", "A1", "B2"),
                "Дистанция должна быть равна 0");
    }
}
