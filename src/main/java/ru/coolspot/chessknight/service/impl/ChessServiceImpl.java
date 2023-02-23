package ru.coolspot.chessknight.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.coolspot.chessknight.exception.ValidationException;
import ru.coolspot.chessknight.model.Node;
import ru.coolspot.chessknight.service.ChessService;
import ru.coolspot.chessknight.util.ChessUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

@Slf4j
@Service
public class ChessServiceImpl implements ChessService {

    private int width;
    private int height;
    private Node startNode;
    private Node endNode;

    @Override
    public Integer getCount(String widthS, String heightS, String start, String end) {
        validateAndSet(widthS, heightS, start, end);
        return findShortestDistance(null);
    }

    @Override
    public byte[] getImage(String widthS, String heightS, String start, String end) {
        validateAndSet(widthS, heightS, start, end);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int imageWidth = width * 50;
        int imageHeight = height * 50;

        try {

            BufferedImage bi = new BufferedImage(imageWidth, imageHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D imageBoard = bi.createGraphics();

            imageBoard.setColor(new Color(200, 214, 225));
            imageBoard.fill3DRect(0, 0, imageWidth, imageHeight, true);
            imageBoard.setColor(new Color(117, 171, 188));
            for (int y = height; y > 0; y--) {
                for (int x = y % 2; x < width; x += 2) {
                    imageBoard.fill(new Rectangle(x * 50, (y - 1) * 50, 50, 50));
                }
            }

            findShortestDistance(imageBoard);
            setText(imageBoard, startNode.getX(), startNode.getY(), "♞", Color.BLACK);

            ImageIO.write(bi, "PNG", baos);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
        return baos.toByteArray();
    }

    private void validateAndSet(String widthS, String heightS, String start, String end) {
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
        startNode = ChessUtil.stringToNode(start);
        endNode = ChessUtil.stringToNode(end);
        log.info("width= {}, height= {}, startNode= {}, endNode= {}",
                width, height, startNode, endNode);
    }


    private Integer findShortestDistance(Graphics2D image) {
        if (startNode.getX() < 0 || startNode.getX() > width - 1) {
            throw new ValidationException("Стартовая позиция по горизонтали невозможна!");
        }
        if (startNode.getY() < 0 || startNode.getY() > height - 1) {
            throw new ValidationException("Стартовая позиция по вертикали невозможна!");
        }
        if (endNode.getX() < 0 || endNode.getX() > width - 1) {
            throw new ValidationException("Конечная позиция по горизонтали невозможна!");
        }
        if (endNode.getY() < 0 || endNode.getY() > height - 1) {
            throw new ValidationException("Конечная позиция по вертикали невозможна!");
        }

        Set<Node> visited = new HashSet<>();

        final int[] row = {2, 2, -2, -2, 1, 1, -1, -1};
        final int[] col = {-1, 1, 1, -1, 2, -2, 2, -2};

        Queue<Node> q = new ArrayDeque<>();
        q.add(startNode);

        while (!q.isEmpty()) {
            Node node = q.poll();

            int x = node.getX();
            int y = node.getY();
            int dist = node.getDist();

            if (x == endNode.getX() && y == endNode.getY()) {
                if (image != null) {
                    setText(image, x, y, "♞", Color.WHITE);
                }
                log.info("Минимальное количество ходов = {}", dist);
                return dist;
            }

            if (!visited.contains(node)) {
                visited.add(node);
                if (image != null && node.getDist() != 0) {
                    setText(image, x, y, String.valueOf(dist), Color.BLACK);
                }

                for (int i = 0; i < row.length; i++) {
                    int x1 = x + row[i];
                    int y1 = y + col[i];

                    if ((x1 >= 0 && x1 < width) && (y1 >= 0 && y1 < height)) {
                        q.add(new Node(x1, y1, dist + 1));
                    }
                }
            }
        }

        log.info("Конечное положение недостижимо!");
        return -1;
    }

    private void setText(Graphics2D image, int x, int y, String message, Color color) {
        Font font = new Font("TimesRoman", Font.PLAIN, 40);
        image.setFont(font);
        image.setPaint(color);
        image.drawString(message, x * 50 + 6, (height - y) * 50 - 10);
    }
}
