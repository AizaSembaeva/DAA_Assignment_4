package org.example.model;

import java.util.*;

public class Graph {
    private final int V;
    private final List<List<Edge>> adj;

    public Graph(int V) {
        this.V = V;
        this.adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public int size() { return V; }

    public List<List<Edge>> getAdj() { return adj; }

    public void addEdge(int from, int to, double weight) {
        if (from < 0 || from >= V || to < 0 || to >= V)
            throw new IllegalArgumentException("Invalid vertex index");
        adj.get(from).add(new Edge(from, to, weight));
    }

    public Graph transpose() {
        Graph reversed = new Graph(V);
        for (int v = 0; v < V; v++) {
            for (Edge e : adj.get(v)) {
                reversed.addEdge(e.to(), e.from(), e.weight());
            }
        }
        return reversed;
    }
}
