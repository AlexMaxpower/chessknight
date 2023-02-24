package ru.coolspot.chessknight.model;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "dist")
@Getter
@ToString
public class Node {

    final int x;
    final int y;
    int dist;
}