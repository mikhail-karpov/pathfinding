package com.mikhailkarpov.pathfinding;

public interface HeuristicFunction<N> {

    double distance(N a, N b);
}
