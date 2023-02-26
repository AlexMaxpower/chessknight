package ru.coolspot.chessknight.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class NodeTest {
    @Test
    public void shouldEqualsNodesWhenDistNotEquals() {
        assertEquals(new Node(1, 1, 1,null), new Node(1,1,2,null),
                "Клетки должны совпадать");
    }

    @Test
    public void shouldNotEqualsNodesWhenDistNotEquals() {
        assertNotEquals(new Node(1, 2, 1, null), new Node(2,1,1, null),
                "Клетки не должны совпадать");
    }
}
