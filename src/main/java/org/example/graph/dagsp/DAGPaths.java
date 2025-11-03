package org.example.graph.dagsp;

import org.example.metrics.PerformanceTracker;
import org.example.model.Edge;
import java.util.*;

public class DAGPaths {
    private final int n;
    private final List<List<Edge>> adj;
    private final PerformanceTracker metrics;

    public DAGPaths(int n, List<Edge> edges, PerformanceTracker metrics) {
        this.n = n;
        this.metrics = metrics;
        this.adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (Edge e : edges) adj.get(e.from()).add(e);
    }

    public Result shortestPaths(int source, List<Integer> topoOrder) {
        metrics.startTimer();

        double[] dist = new double[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        for (int u : topoOrder) {
            if (dist[u] != Double.POSITIVE_INFINITY) {
                for (Edge e : adj.get(u)) {
                    metrics.incRelaxations();
                    if (dist[e.to()] > dist[u] + e.weight()) {
                        dist[e.to()] = dist[u] + e.weight();
                        parent[e.to()] = u;
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (Double.isInfinite(dist[i]) || Double.isNaN(dist[i])) {
                dist[i] = -1.0;
            }
        }

        metrics.stopTimer();
        return new Result(dist, parent);
    }

    public Result longestPaths(int source, List<Integer> topoOrder) {
        metrics.startTimer();

        double[] dist = new double[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Double.NEGATIVE_INFINITY);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        for (int u : topoOrder) {
            if (dist[u] != Double.NEGATIVE_INFINITY) {
                for (Edge e : adj.get(u)) {
                    metrics.incRelaxations();
                    double cand = dist[u] + e.weight();
                    if (cand > dist[e.to()]) {
                        dist[e.to()] = cand;
                        parent[e.to()] = u;
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (Double.isInfinite(dist[i]) || Double.isNaN(dist[i])) {
                dist[i] = -1.0;
            }
        }

        metrics.stopTimer();
        return new Result(dist, parent);
    }

    public static List<Integer> reconstructPath(int target, int[] parent) {
        List<Integer> path = new ArrayList<>();
        for (int v = target; v != -1; v = parent[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        return path;
    }

    public static class Result {
        public final double[] dist;
        public final int[] parent;

        public Result(double[] dist, int[] parent) {
            this.dist = dist;
            this.parent = parent;
        }

        public double getDistance(int v) { return dist[v]; }
    }
}
