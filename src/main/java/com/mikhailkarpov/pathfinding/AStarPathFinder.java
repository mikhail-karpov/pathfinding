package com.mikhailkarpov.pathfinding;

import com.google.common.graph.ValueGraph;

import java.util.*;

public class AStarPathFinder<N> implements PathFinder<N> {

    private final ValueGraph<N, Double> graph;
    private final HeuristicFunction heuristicFunction;

    public AStarPathFinder(ValueGraph<N, Double> graph, HeuristicFunction heuristicFunction) {
        this.graph = graph;
        this.heuristicFunction = heuristicFunction;
    }

    @Override
    public List<N> findPath(N source, N target) throws PathNotFoundException {

        PriorityQueue<PathSegment<N>> openSet = new PriorityQueue<>();
        Set<N> closedSet = new HashSet<>();
        Map<N, Double> distances = new HashMap<>(); //shortest estimate so far

        //Start from the source node
        openSet.add(new PathSegment<>(source, 0.0));
        distances.put(source, 0.0);

        while (!openSet.isEmpty()) {
            PathSegment<N> currentSegment = openSet.remove();

            //If we have reached the target, trace back path
            if (target.equals(currentSegment.node)) {
                return tracebackPath(currentSegment);
            }

            if (closedSet.contains(currentSegment.node)) {
                continue;
            }
            closedSet.add(currentSegment.node);

            for (N neighbor : graph.adjacentNodes(currentSegment.node)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                double cost = distances.get(currentSegment.node);
                cost += graph.edgeValue(currentSegment.node, neighbor).orElseThrow(() -> {
                    String message = String.format("Connection % - %s not found", currentSegment.node, neighbor);
                    return new PathNotFoundException(message);
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
        String message = String.format("Path from %s to %s not found", source, target);
        throw new PathNotFoundException(message);
    }

    private List<N> tracebackPath(PathSegment<N> currentSegment) {
        List<N> path = new ArrayList<>();
        PathSegment<N> nextSegment = currentSegment;

        while (nextSegment != null) {
            path.add(nextSegment.node);
            nextSegment = nextSegment.previous;
        }

        Collections.reverse(path);
        return path;
    }

    private static class PathSegment<N> implements Comparable<PathSegment<N>> {

        private final N node;
        private final PathSegment<N> previous;
        private final double distance;

        public PathSegment(N node, double distance) {
            this(node, null, distance);
        }

        public PathSegment(N node, PathSegment previous, double distance) {
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
