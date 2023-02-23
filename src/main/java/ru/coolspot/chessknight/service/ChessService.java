package ru.coolspot.chessknight.service;

public interface ChessService {

    Integer getCount(String width, String height, String start, String end);

    byte[] getImage(String width, String height, String start, String end);

}
