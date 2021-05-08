package com.mikhailkarpov.pathfinding;

import java.util.List;

public interface PathFinder<N> {

    List<N> findPath(N source, N target);
}
