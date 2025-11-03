package org.example.graph.scc;

import org.example.model.Graph;
import org.example.metrics.PerformanceTracker;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class KosarajuTest {

    @Test
    void testSingleStronglyConnectedComponent() {
        Graph g = new Graph(4);
        g.addEdge(0, 1, 4);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 3, 5);
        g.addEdge(3, 0, 2);

        PerformanceTracker m = new PerformanceTracker();
        Kosaraju scc = new Kosaraju(g, m);
        scc.run();

        var comps = scc.getComponents();
        assertEquals(1, comps.size(), "Graph with full cycle should have 1 SCC");
        assertEquals(Set.of(0, 1, 2, 3), new HashSet<>(comps.get(0)));

        assertTrue(m.getDfsVisits() > 0);
        assertTrue(m.getEdgesExplored() > 0);
    }

    @Test
    void testDisconnectedGraph() {
        Graph g = new Graph(5);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2,5);
        g.addEdge(3, 4, 4);

        PerformanceTracker m = new PerformanceTracker();
        Kosaraju scc = new Kosaraju(g, m);
        scc.run();

        var comps = scc.getComponents();
        assertEquals(5, comps.size(), "Each vertex should be its own SCC");
    }

    @Test
    void testGraphWithTwoCycles() {
        Graph g = new Graph(6);
        g.addEdge(0, 1,5);
        g.addEdge(1, 2,7);
        g.addEdge(2, 0,9);
        g.addEdge(3, 4,2);
        g.addEdge(4, 5,4);
        g.addEdge(5, 3,3);

        PerformanceTracker m = new PerformanceTracker();
        Kosaraju scc = new Kosaraju(g, m);
        scc.run();

        var comps = scc.getComponents();
        assertEquals(2, comps.size(), "Graph should have 2 SCCs");

        Set<Integer> allVertices = new HashSet<>();
        for (var c : comps) allVertices.addAll(c);
        assertEquals(6, allVertices.size());
    }

    @Test
    void testAcyclicGraph() {
        Graph g = new Graph(4);
        g.addEdge(0, 1,6);
        g.addEdge(1, 2,7);
        g.addEdge(2, 3,8);

        PerformanceTracker m = new PerformanceTracker();
        Kosaraju scc = new Kosaraju(g, m);
        scc.run();

        var comps = scc.getComponents();
        assertEquals(4, comps.size(), "Each node should be separate SCC in DAG");
    }

    @Test
    void testEmptyGraph() {
        Graph g = new Graph(0);
        PerformanceTracker m = new PerformanceTracker();
        Kosaraju scc = new Kosaraju(g, m);
        scc.run();

        assertEquals(0, scc.getComponents().size());
    }

    @Test
    void testComponentIdsConsistency() {
        Graph g = new Graph(5);
        g.addEdge(0, 1,6);
        g.addEdge(1, 0,3);
        g.addEdge(2, 3,8);
        g.addEdge(3, 4,11);
        g.addEdge(4, 2,2);

        PerformanceTracker m = new PerformanceTracker();
        Kosaraju scc = new Kosaraju(g, m);
        scc.run();

        int[] ids = scc.getComponentIds();
        assertEquals(5, ids.length);
        assertTrue(ids[0] == ids[1], "0 and 1 should be in the same SCC");
        assertTrue(ids[2] == ids[3] && ids[3] == ids[4], "2,3,4 in one SCC");
        assertNotEquals(ids[0], ids[2], "Different SCC groups should have different IDs");
    }

    @Test
    void testMetricsAfterRun() {
        Graph g = new Graph(3);
        g.addEdge(0, 1,2);
        g.addEdge(1, 2,5);
        g.addEdge(2, 0,3);

        PerformanceTracker m = new PerformanceTracker();
        Kosaraju scc = new Kosaraju(g, m);
        scc.run();

        assertTrue(m.getTimeMs() > 0);
        assertTrue(m.getDfsVisits() > 0);
        assertTrue(m.getEdgesExplored() > 0);
    }
}
