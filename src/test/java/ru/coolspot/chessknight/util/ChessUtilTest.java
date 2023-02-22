package ru.coolspot.chessknight.util;

import org.junit.jupiter.api.Test;
import ru.coolspot.chessknight.exception.ValidationException;
import ru.coolspot.chessknight.model.Node;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ChessUtilTest {

    @Test
    public void shouldReturnNodeWhenCorrectStringToNode() {
        assertEquals(new Node(1, 1), ChessUtil.stringToNode("B2"),
                "Координаты должны быть (1,1)");
    }

    @Test
    public void shouldReturnCorrectNodeWhenCorrectStringToNode() {
        assertEquals(new Node(25, 99), ChessUtil.stringToNode("Z100"),
                "Координаты должны быть (25,99)");
    }

    @Test
    public void shouldExceptionWhenFirstSymbolNotLetter() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> ChessUtil.stringToNode("112"));
        assertEquals("Значение по горизонтали должно быть латинской буквой!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenSecondSymbolNotDigit() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> ChessUtil.stringToNode("AA"));
        assertEquals("Значение по вертикали должно быть числом!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenSecondNumberZero() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> ChessUtil.stringToNode("A0"));
        assertEquals("Значение по вертикали должно быть числом больше нуля!",
                exp.getMessage());
    }

    @Test
    public void shouldExceptionWhenSecondNumberZeroZero() {
        ValidationException exp = assertThrows(ValidationException.class,
                () -> ChessUtil.stringToNode("A00"));
        assertEquals("Значение по вертикали должно быть числом больше нуля!",
                exp.getMessage());
    }

    @Test
    public void shouldReturnCorrectWhenSecondSymbolZeroAndThirdSymbolNotZero() {
        assertEquals(new Node(0, 0), ChessUtil.stringToNode("A01"),
                "Координаты должны быть (0,0)");
    }

    @Test
    public void shouldReturnCorrectWhenSecondSymbolZeroAndLastSymbolNotZero() {
        assertEquals(new Node(0, 0), ChessUtil.stringToNode("A0001"),
                "Координаты должны быть (0,0)");
    }
}
