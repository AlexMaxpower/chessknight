package ru.coolspot.chessknight.model;

import lombok.*;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Node {

    final int x;
    final int y;
    int dist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        if (x != node.x) return false;
        return y == node.y;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }
}