package ru.coolspot.chessknight.model;

import lombok.*;

@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Node {

    final int x;
    final int y;
    int dist;

}