package ru.coolspot.chessknight.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.coolspot.chessknight.exception.ValidationException;
import ru.coolspot.chessknight.model.Node;
import ru.coolspot.chessknight.service.ChessService;
import ru.coolspot.chessknight.util.ChessUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;
import java.util.*;

@Slf4j
@Service
public class ChessServiceImpl implements ChessService {

    private static final int CAGE = 50;

    @Value("${cache:false}")
    private String cache;

    private int width;
    private int height;
    private Node startNode;
    private Node endNode;
    private String filename;

    @Override
    public String getCount(String widthS, String heightS, String start, String end) {
        validateAndSet(widthS, heightS, start, end);
        return findShortestDistance(null).get("count");
    }

    @Override
    public String getWay(String widthS, String heightS, String start, String end) {
        validateAndSet(widthS, heightS, start, end);
        return findShortestDistance(null).get("way");
    }

    @Override
    public byte[] getImage(String widthS, String heightS, String start, String end) {
        validateAndSet(widthS, heightS, start, end);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        if (cache.equalsIgnoreCase("true")) {
            log.info("Ищем файл {} с результатами", filename + ".png");
            try {
                byte[] resultImage = Files.readAllBytes(Paths.get(filename + ".png"));
                log.info("Отдаем картинку из файла {}", filename + ".png");
                return resultImage;
            } catch (IOException e) {
                log.info("Ошибка получения данных");
            }
        }

        int imageWidth = (width + 2) * CAGE;
        int imageHeight = (height + 2) * CAGE;

        try {

            BufferedImage bi = new BufferedImage(imageWidth, imageHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D imageBoard = bi.createGraphics();
            imageBoard.setColor(new Color(5, 8, 153));
            imageBoard.fill3DRect(0, 0, imageWidth, imageHeight, true);
            imageBoard.setColor(new Color(200, 214, 225));
            imageBoard.fill3DRect(CAGE, CAGE, imageWidth - 2 * CAGE, imageHeight - 2 * CAGE, true);
            imageBoard.setColor(new Color(117, 171, 188));
            for (int y = height; y > 0; y--) {
                setText(imageBoard, -1, y - 1, String.valueOf(y), Color.WHITE);
                setText(imageBoard, width, y - 1, String.valueOf(y), Color.WHITE);
                for (int x = y % 2; x < width; x += 2) {
                    imageBoard.fill(new Rectangle((x + 1) * CAGE, y * CAGE, CAGE, CAGE));
                    if (x != 0) {
                        setText(imageBoard, x, -1, String.valueOf((char) (x + 65)), Color.WHITE);
                        setText(imageBoard, x - 1, -1, String.valueOf((char) (x + 64)), Color.WHITE);
                        setText(imageBoard, x, height, String.valueOf((char) (x + 65)), Color.WHITE);
                        setText(imageBoard, x - 1, height, String.valueOf((char) (x + 64)), Color.WHITE);
                    }
                }
            }

            setText(imageBoard, startNode.getX(), startNode.getY(), "♞", Color.BLACK);
            findShortestDistance(imageBoard);
            ImageIO.write(bi, "PNG", baos);
            if (cache.equalsIgnoreCase("true")) {
                log.info("Кеширование картинки в файл {}", filename + ".png");
                ImageIO.write(bi, "PNG", new File(filename + ".png"));
            }
        } catch (IOException ie) {
            log.error("Записать файл не удалось!");
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

        if (width > 26 || height > 26) {
            throw new ValidationException("Размеры доски не должны быть больше 26!");
        }

        startNode = ChessUtil.stringToNode(start);
        endNode = ChessUtil.stringToNode(end);

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

        log.info("width= {}, height= {}, startNode= {}, endNode= {}",
                width, height, startNode, endNode);
        filename = widthS + "-" + heightS + "-" + start + "-" + end;
    }

    private Map<String, String> findShortestDistance(Graphics2D image) {

        Map<String, String> resultMap = new HashMap<>();

        if (cache.equalsIgnoreCase("true") && image == null) {
            log.info("Ищем файл {} с результатами", filename + ".txt");
            try (BufferedReader fileReader = new BufferedReader(new FileReader(filename + ".txt",
                    StandardCharsets.UTF_8))) {
                resultMap.put("count", fileReader.readLine());
                resultMap.put("way", fileReader.readLine());
                log.info("Отдаем данные из файла: count= {} way= {}",
                        resultMap.get("count"), resultMap.get("way"));
                return resultMap;
            } catch (FileNotFoundException e) {
                log.info("Файл {} не найден!", filename + ".txt");
            } catch (IOException e) {
                log.info("Ошибка получения данных");
            }
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
                    setText(image, x, y, "♞", Color.MAGENTA);
                }
                List<String> way = new ArrayList<>();
                Node point = node.getNode();
                while (point != null && point.getNode() != null) {
                    if (image != null) {
                        setText(image, point.getX(), point.getY(),
                                String.valueOf(point.getDist()), Color.BLACK);
                    }
                    way.add(ChessUtil.nodeToString(point));
                    point = point.getNode();
                }
                way.add(ChessUtil.nodeToString(startNode));
                Collections.reverse(way);

                log.info("Минимальное количество ходов = {}", dist);
                resultMap.put("count", String.valueOf(dist));
                StringBuilder wayToLog = new StringBuilder();
                for (String str : way) {
                    wayToLog.append(str).append(" -> ");
                }
                wayToLog.append(ChessUtil.nodeToString(endNode));
                log.info("Путь: {}", wayToLog);
                resultMap.put("way", wayToLog.toString());
                if (cache.equalsIgnoreCase("true")) {
                    log.info("Кеширование результатов в файл {}", filename + ".txt");
                    try (FileWriter fileWriter = new FileWriter(filename + ".txt", StandardCharsets.UTF_8)) {
                        fileWriter.write(dist + "\n");
                        fileWriter.write(wayToLog.toString());
                    } catch (IOException e) {
                        log.error("Записать файл не удалось!");
                    }
                }
                return resultMap;
            }

            if (!visited.contains(node)) {
                visited.add(node);

                for (int i = 0; i < row.length; i++) {
                    int x1 = x + row[i];
                    int y1 = y + col[i];

                    if ((x1 >= 0 && x1 < width) && (y1 >= 0 && y1 < height)) {
                        q.add(new Node(x1, y1, dist + 1, node));
                    }
                }
            }
        }

        log.info("Конечное положение недостижимо!");
        resultMap.put("count", "-1");
        resultMap.put("way", "-1");
        return resultMap;
    }

    private void setText(Graphics2D image, int x, int y, String message, Color color) {
        int size = 40;
        int paddingX = message.length() < 2 && !message.equals("♞") ? 14 : 2;
        int paddingY = 10;
        if (x < 0 || y < 0 || x == width || y == height) {
            size = 20;
            paddingX = 20;
            paddingY = 20;
        }
        Font font = new Font("TimesRoman", Font.PLAIN, size);
        image.setFont(font);
        image.setPaint(color);
        image.drawString(message, x * CAGE + paddingX + CAGE, (height - y) * CAGE - paddingY + CAGE);
    }
}
