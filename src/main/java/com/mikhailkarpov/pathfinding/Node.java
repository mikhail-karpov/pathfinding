package com.mikhailkarpov.pathfinding;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Node {

    private final int id;
    private Set<Node> connections = new HashSet<>();
    private double x;
    private double y;

    public Node(int id) {
        this.id = id;
    }

    public Node(int id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void addConnection(Node node) {
        this.connections.add(node);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return id == node.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                '}';
    }
}
