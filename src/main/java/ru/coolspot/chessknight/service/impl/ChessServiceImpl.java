package ru.coolspot.chessknight.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.coolspot.chessknight.exception.ValidationException;
import ru.coolspot.chessknight.model.Node;
import ru.coolspot.chessknight.service.ChessService;
import ru.coolspot.chessknight.util.ChessUtil;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

@Slf4j
@Service
public class ChessServiceImpl implements ChessService {

    @Override
    public Integer getCount(String widthS, String heightS, String start, String end) {
        int width;
        int height;
        try {
            width = Integer.parseInt(widthS);
        } catch (NumberFormatException e) {
            throw new ValidationException("Ширина доски должна быть числом!");
        }
        try {
            height = Integer.parseInt(heightS);
        } catch (NumberFormatException e) {
            throw new ValidationException("Высота доски должна быть числом!");
        }

        if (width < 1 || height < 1) {
            throw new ValidationException("Размеры доски должны быть больше нуля!");
        }
        Node startNode = ChessUtil.stringToNode(start);
        Node endNode = ChessUtil.stringToNode(end);
        log.info("width= {}, height= {}, startNode= {}, endNode= {}",
                width, height, startNode, endNode);
        return findShortestDistance(startNode, endNode, width, height);
    }

    private Integer findShortestDistance(Node start, Node end, int width, int height) {
        if (start.getX() < 0 || start.getX() > width - 1) {
            throw new ValidationException("Стартовая позиция по горизонтали невозможна!");
        }
        if (start.getY() < 0 || start.getY() > height - 1) {
            throw new ValidationException("Стартовая позиция по вертикали невозможна!");
        }
        if (end.getX() < 0 || end.getX() > width - 1) {
            throw new ValidationException("Конечная позиция по горизонтали невозможна!");
        }
        if (end.getY() < 0 || end.getY() > height - 1) {
            throw new ValidationException("Конечная позиция по вертикали невозможна!");
        }

        Set<Node> visited = new HashSet<>();

        final int[] row = {2, 2, -2, -2, 1, 1, -1, -1};
        final int[] col = {-1, 1, 1, -1, 2, -2, 2, -2};

        Queue<Node> q = new ArrayDeque<>();
        q.add(start);

        while (!q.isEmpty()) {
            Node node = q.poll();

            int x = node.getX();
            int y = node.getY();
            int dist = node.getDist();

            if (x == end.getX() && y == end.getY()) {
                log.info("Минимальное количество ходов = {}", dist);
                return dist;
            }

            if (!visited.contains(node)) {
                visited.add(node);

                for (int i = 0; i < row.length; i++) {
                    int x1 = x + row[i];
                    int y1 = y + col[i];

                    if ((x1 >= 0 && x1 < width - 1) && (y1 >= 0 && y1 < height - 1)) {
                        q.add(new Node(x1, y1, dist + 1));
                    }
                }
            }
        }

        log.info("Конечное положение недостижимо!");
        return -1;
    }
}
