package com.mikhailkarpov.pathfinding;

import com.google.common.graph.ValueGraph;

import java.util.*;

public class AStarPathFinder implements PathFinder<Node> {

    private final ValueGraph<Node, Double> graph;
    private final HeuristicFunction heuristicFunction;

    public AStarPathFinder(ValueGraph<Node, Double> graph, HeuristicFunction heuristicFunction) {
        this.graph = graph;
        this.heuristicFunction = heuristicFunction;
    }

    public List<Node> findPath(Node source, Node target) {

        PriorityQueue<PathSegment> openSet = new PriorityQueue<>();
        Set<Node> closedSet = new HashSet<>();
        Map<Node, Double> distances = new HashMap<>(); //shortest estimate so far

        //Start from the source node
        openSet.add(new PathSegment(source, 0.0));
        distances.put(source, 0.0);

        while (!openSet.isEmpty()) {
            PathSegment currentSegment = openSet.remove();

            //If we have reached the target, trace back path
            if (target.equals(currentSegment.node)) {
                return tracebackPath(currentSegment);
            }

            if (closedSet.contains(currentSegment.node)) {
                continue;
            }
            closedSet.add(currentSegment.node);

            for (Node neighbor : graph.adjacentNodes(currentSegment.node)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double cost = distances.get(currentSegment.node);
                cost += graph.edgeValue(currentSegment.node, neighbor).orElseThrow(() -> {
                    String message = String.format("Connection % - %s not found", currentSegment.node, neighbor);
                    return new IllegalStateException(message);
                });

                if (!distances.containsKey(neighbor) || distances.get(neighbor) > cost) {
                    distances.put(neighbor, cost);

                    double totalCost = cost + heuristicFunction.distance(neighbor, target);
                    PathSegment neighborSegment = new PathSegment(neighbor, currentSegment, totalCost);

                    openSet.add(neighborSegment);
                }
            }
        }
        // All nodes have been visited, but target was not found
        return Collections.emptyList();
    }

    private List<Node> tracebackPath(PathSegment currentSegment) {
        List<Node> path = new ArrayList<>();
        PathSegment nextSegment = currentSegment;

        while (nextSegment != null) {
            path.add(nextSegment.node);
            nextSegment = nextSegment.previous;
        }

        Collections.reverse(path);
        return path;
    }

    private static class PathSegment implements Comparable<PathSegment> {

        private final Node node;
        private PathSegment previous;
        private final double distance;

        public PathSegment(Node node, double distance) {
            this(node, null, distance);
        }

        public PathSegment(Node node, PathSegment previous, double distance) {
            this.node = node;
            this.previous = previous;
            this.distance = distance;
        }

        @Override
        public int compareTo(PathSegment o) {
            return Double.compare(this.distance, o.distance);
        }

        @Override
        public String toString() {
            String previous = "null";
            if (this.previous != null) {
                previous = this.previous.node.toString();
            }
            return "PathSegment{" +
                    "node=" + node +
                    ", previous=" + previous +
                    ", distance=" + distance +
                    '}';
        }
    }
}
