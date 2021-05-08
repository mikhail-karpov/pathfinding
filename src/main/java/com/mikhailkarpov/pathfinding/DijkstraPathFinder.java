package com.mikhailkarpov.pathfinding;

import com.google.common.graph.ValueGraph;

import java.util.List;

public class DijkstraPathFinder implements PathFinder<Node> {

    private final PathFinder<Node> delegate;

    public DijkstraPathFinder(ValueGraph<Node, Double> graph) {
        this.delegate = new AStarPathFinder(graph, (a, b) -> 0.0);
    }

    public List<Node> findPath(Node source, Node target) {
        return delegate.findPath(source, target);
    }
}
