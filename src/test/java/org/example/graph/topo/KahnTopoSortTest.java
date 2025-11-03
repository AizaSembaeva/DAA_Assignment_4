package org.example.graph.topo;

import org.example.metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class KahnTopoSortTest {

    @Test
    void testLinearDAG() {
        List<Set<Integer>> dag = new ArrayList<>();
        dag.add(Set.of(1));
        dag.add(Set.of(2));
        dag.add(Set.of(3));
        dag.add(Set.of());

        PerformanceTracker m = new PerformanceTracker();
        KahnTopoSort topo = new KahnTopoSort(dag, m);
        List<Integer> order = topo.order();

        assertEquals(List.of(0, 1, 2, 3), order);
        assertTrue(topo.isDag());
        assertEquals(4, m.getPushes());
        assertEquals(4, m.getPops());
    }

    @Test
    void testBranchingDAG() {
        List<Set<Integer>> dag = new ArrayList<>();
        dag.add(Set.of(1, 2));
        dag.add(Set.of(3));
        dag.add(Set.of(3));
        dag.add(Set.of());

        PerformanceTracker m = new PerformanceTracker();
        KahnTopoSort topo = new KahnTopoSort(dag, m);
        List<Integer> order = topo.order();

        int idx0 = order.indexOf(0);
        int idx1 = order.indexOf(1);
        int idx2 = order.indexOf(2);
        int idx3 = order.indexOf(3);
        assertTrue(idx0 < idx1 && idx0 < idx2);
        assertTrue(idx1 < idx3 && idx2 < idx3);
    }

    @Test
    void testEmptyDAG() {
        List<Set<Integer>> dag = new ArrayList<>();
        PerformanceTracker m = new PerformanceTracker();
        KahnTopoSort topo = new KahnTopoSort(dag, m);
        List<Integer> order = topo.order();

        assertTrue(order.isEmpty());
        assertTrue(topo.isDag());
        assertEquals(0, m.getPushes());
        assertEquals(0, m.getPops());
    }

    @Test
    void testSingleNode() {
        List<Set<Integer>> dag = List.of(Set.of());
        PerformanceTracker m = new PerformanceTracker();
        KahnTopoSort topo = new KahnTopoSort(dag, m);
        List<Integer> order = topo.order();

        assertEquals(List.of(0), order);
        assertEquals(1, m.getPushes());
        assertEquals(1, m.getPops());
    }
}