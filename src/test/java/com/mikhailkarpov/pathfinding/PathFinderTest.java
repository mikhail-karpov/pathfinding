package com.mikhailkarpov.pathfinding;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

/**
 * Tests the implementation of the A* algorithm using the following sample graph:
 *
 * <pre>
 *       A
 *      / \
 *    2/   \3
 *    /     \
 *   / 3   1 \    5
 *  C-----D---E-------B
 *  |      \  |       |
 *  |      4\ |6      |
 *  |        \|       |
 * 2|         F       |15
 *  |         |       |
 *  |         |7      |
 *  |         |       |
 *  G---------H-------I
 *       4        3
 * </pre>
 */
class PathFinderTest {

    @Test
    void test() throws PathNotFoundException {
        //given
        Node a = new Node(1);
        Node b = new Node(2);
        Node c = new Node(3);
        Node d = new Node(4);
        Node e = new Node(5);
        Node f = new Node(6);
        Node g = new Node(7);
        Node h = new Node(8);
        Node i = new Node(9);

        MutableValueGraph<Node, Double> graph = ValueGraphBuilder.undirected().build();
        graph.putEdgeValue(a, c, 2.0);
        graph.putEdgeValue(a, e, 3.0);
        graph.putEdgeValue(b, e, 5.0);
        graph.putEdgeValue(b, i, 15.0);
        graph.putEdgeValue(c, d, 3.0);
        graph.putEdgeValue(c, g, 2.0);
        graph.putEdgeValue(d, e, 1.0);
        graph.putEdgeValue(d, f, 4.0);
        graph.putEdgeValue(e, f, 6.0);
        graph.putEdgeValue(f, h, 7.0);
        graph.putEdgeValue(g, h, 4.0);
        graph.putEdgeValue(h, i, 3.0);

        Map<Node, Point2D.Double> coordinates = new HashMap<>();
        coordinates.put(a, new Point2D.Double(2.41, 6.23));
        coordinates.put(b, new Point2D.Double(8.98, 6.08));
        coordinates.put(c, new Point2D.Double(0.56, 3.36));
        coordinates.put(d, new Point2D.Double(2.98, 3.90));
        coordinates.put(e, new Point2D.Double(4.22, 4.28));
        coordinates.put(f, new Point2D.Double(4.00, 2.60));
        coordinates.put(g, new Point2D.Double(0.00, 0.00));
        coordinates.put(h, new Point2D.Double(4.85, 0.11));
        coordinates.put(i, new Point2D.Double(7.50, 0.00));

        //when
        PathFinder aStarPathFinder = new AStarPathFinder(graph, new EuclideanHeuristicFunction(coordinates));

        //then
        List<Node> expectedDtoH = Arrays.asList(d, c, g, h);
        List<Node> expectedAtoF = Arrays.asList(a, e, d, f);
        List<Node> expectedEtoH = Arrays.asList(e, d, c, g, h);
        List<Node> expectedBtoH = Arrays.asList(b, e, d, c, g, h);
        List<Node> expectedBtoI = Arrays.asList(b, i);

        assertIterableEquals(expectedDtoH, aStarPathFinder.findPath(d, h));
        assertIterableEquals(expectedAtoF, aStarPathFinder.findPath(a, f));
        assertIterableEquals(expectedEtoH, aStarPathFinder.findPath(e, h));
        assertIterableEquals(expectedBtoH, aStarPathFinder.findPath(b, h));
        assertIterableEquals(expectedBtoI, aStarPathFinder.findPath(b, i));
    }
}