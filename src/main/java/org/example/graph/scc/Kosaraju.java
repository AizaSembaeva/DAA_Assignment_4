package org.example.graph.scc;

import org.example.metrics.PerformanceTracker;
import org.example.model.*;
import java.util.*;


public class Kosaraju {
    private final Graph graph;
    private final PerformanceTracker metrics;
    private boolean[] visited;
    private Deque<Integer> order;
    private List<List<Integer>> components;
    private int[] compId;

    public Kosaraju(Graph graph, PerformanceTracker metrics) {
        this.graph = graph;
        this.metrics = metrics;
    }

    public List<List<Integer>> run() {
        metrics.startTimer();

        int n = graph.size();
        visited = new boolean[n];
        order = new ArrayDeque<>();
        components = new ArrayList<>();
        compId = new int[n];
        Arrays.fill(compId, -1);

        for (int v = 0; v < n; v++) {
            if (!visited[v]) dfsFirst(v);
        }

        Arrays.fill(visited, false);
        Graph transposed = graph.transpose();

        while (!order.isEmpty()) {
            int v = order.pop();
            if (!visited[v]) {
                List<Integer> comp = new ArrayList<>();
                dfsSecond(transposed, v, components.size(), comp);
                components.add(comp);
            }
        }

        metrics.stopTimer();
        return components;
    }

    private void dfsFirst(int v) {
        visited[v] = true;
        metrics.incDfsVisits();
        for (Edge e : graph.getAdj().get(v)) {
            int w = e.to();
            metrics.incEdgesExplored();
            if (!visited[w]) dfsFirst(w);
        }
        order.push(v);
    }

    private void dfsSecond(Graph g, int v, int compIndex, List<Integer> comp) {
        visited[v] = true;
        compId[v] = compIndex;
        comp.add(v);
        metrics.incDfsVisits();

        for (Edge e : g.getAdj().get(v)) {
            int w = e.to();
            metrics.incEdgesExplored();
            if (!visited[w]) dfsSecond(g, w, compIndex, comp);
        }
    }

    public List<List<Integer>> getComponents() { return components; }
    public int[] getComponentIds() { return compId; }
    public int count() { return components.size(); }
}
