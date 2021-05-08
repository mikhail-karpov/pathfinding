package com.mikhailkarpov.pathfinding;

import java.awt.geom.Point2D;
import java.util.Map;

public class EuclideanHeuristicFunction implements HeuristicFunction<Node> {

    private final Map<Node, Point2D.Double> coordinates;

    public EuclideanHeuristicFunction(Map<Node, Point2D.Double> coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public double distance(Node a, Node b) {

        final Point2D.Double aCoordinates = coordinates.get(a);
        final Point2D.Double bCoordinates = coordinates.get(b);

        if (aCoordinates == null) {
            throw new IllegalArgumentException("Not found: " + a);
        }

        if (bCoordinates == null) {
            throw new IllegalArgumentException("Not found: " + b);
        }

        return aCoordinates.distance(coordinates.get(b));
    }
}
