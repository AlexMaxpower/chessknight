package ru.coolspot.chessknight.util;

import ru.coolspot.chessknight.exception.ValidationException;
import ru.coolspot.chessknight.model.Node;

public class ChessUtil {

    public static Node stringToNode(String str) {
        String xs = str.substring(0, 1);
        if (!xs.matches("[A-z]")) {
            throw new ValidationException("Значение по горизонтали должно быть латинской буквой!");
        }
        String ys = str.substring(1);
        if (!ys.matches("[0-9]+")) {
            throw new ValidationException("Значение по вертикали должно быть числом!");
        }
        int x = str.toUpperCase().charAt(0) - 65;
        int y = Integer.parseInt(ys) - 1;
        if (y < 0) {
            throw new ValidationException("Значение по вертикали должно быть числом больше нуля!");
        }
        return new Node(x, y);
    }
}
