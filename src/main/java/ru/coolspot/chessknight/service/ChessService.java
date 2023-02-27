package ru.coolspot.chessknight.service;

public interface ChessService {

    String getCount(String width, String height, String start, String end);

    byte[] getImage(String width, String height, String start, String end);

    String getWay(String width, String height, String start, String end);

}
