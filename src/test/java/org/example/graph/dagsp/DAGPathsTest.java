package org.example.graph.dagsp;

import org.example.model.Edge;
import org.example.metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DAGPathsTest {

    private List<Edge> buildSampleEdges() {
        return List.of(
                new Edge(0, 1, 2),
                new Edge(0, 2, 4),
                new Edge(1, 3, 7),
                new Edge(2, 3, 1)
        );
    }

    @Test
    void testShortestPaths() {
        PerformanceTracker m = new PerformanceTracker();
        List<Edge> edges = buildSampleEdges();
        DAGPaths dag = new DAGPaths(4, edges, m);
        List<Integer> topo = List.of(0, 1, 2, 3);

        var result = dag.shortestPaths(0, topo);

        assertEquals(0.0, result.getDistance(0), 1e-6);
        assertEquals(2.0, result.getDistance(1), 1e-6);
        assertEquals(4.0, result.getDistance(2), 1e-6);
        assertEquals(5.0, result.getDistance(3), 1e-6);
        assertTrue(m.getRelaxations() > 0);
        assertTrue(m.getTimeMs() > 0);
    }

    @Test
    void testLongestPaths() {
        PerformanceTracker m = new PerformanceTracker();
        List<Edge> edges = buildSampleEdges();
        DAGPaths dag = new DAGPaths(4, edges, m);
        List<Integer> topo = List.of(0, 1, 2, 3);

        var result = dag.longestPaths(0, topo);

        assertEquals(0.0, result.getDistance(0), 1e-6);
        assertEquals(2.0, result.getDistance(1), 1e-6);
        assertEquals(4.0, result.getDistance(2), 1e-6);
        assertEquals(9.0, result.getDistance(3), 1e-6);
    }

    @Test
    void testReconstructPathShortest() {
        PerformanceTracker m = new PerformanceTracker();
        List<Edge> edges = buildSampleEdges();
        DAGPaths dag = new DAGPaths(4, edges, m);
        List<Integer> topo = List.of(0, 1, 2, 3);

        var result = dag.shortestPaths(0, topo);
        List<Integer> path = DAGPaths.reconstructPath(3, result.parent);

        assertEquals(List.of(0, 2, 3), path, "Shortest path should go through 0→2→3");
    }

    @Test
    void testReconstructPathLongest() {
        PerformanceTracker m = new PerformanceTracker();
        List<Edge> edges = buildSampleEdges();
        DAGPaths dag = new DAGPaths(4, edges, m);
        List<Integer> topo = List.of(0, 1, 2, 3);

        var result = dag.longestPaths(0, topo);
        List<Integer> path = DAGPaths.reconstructPath(3, result.parent);

        assertEquals(List.of(0, 1, 3), path, "Longest (critical) path should be 0→1→3");
    }

    @Test
    void testSingleNodeDAG() {
        List<Edge> edges = List.of();
        PerformanceTracker m = new PerformanceTracker();
        DAGPaths dag = new DAGPaths(1, edges, m);
        List<Integer> topo = List.of(0);

        var shortest = dag.shortestPaths(0, topo);
        var longest = dag.longestPaths(0, topo);

        assertEquals(0.0, shortest.getDistance(0), 1e-6);
        assertEquals(0.0, longest.getDistance(0), 1e-6);
    }

    @Test
    void testDisconnectedDAG() {
        List<Edge> edges = List.of();
        PerformanceTracker m = new PerformanceTracker();
        DAGPaths dag = new DAGPaths(2, edges, m);
        List<Integer> topo = List.of(0, 1);

        var shortest = dag.shortestPaths(0, topo);
        var longest = dag.longestPaths(0, topo);

        assertEquals(0.0, shortest.getDistance(0), 1e-6);
        assertEquals(-1, shortest.getDistance(1), 1e-6);
        assertEquals(0.0, longest.getDistance(0), 1e-6);
        assertEquals(-1.0, longest.getDistance(1), 1e-6);
    }

    @Test
    void testMetricsCounted() {
        PerformanceTracker m = new PerformanceTracker();
        List<Edge> edges = buildSampleEdges();
        DAGPaths dag = new DAGPaths(4, edges, m);
        List<Integer> topo = List.of(0, 1, 2, 3);

        dag.shortestPaths(0, topo);
        assertTrue(m.getRelaxations() > 0, "Relaxations should be counted");
        assertTrue(m.getTimeMs() >= 0, "Execution time must be measured");
    }
}