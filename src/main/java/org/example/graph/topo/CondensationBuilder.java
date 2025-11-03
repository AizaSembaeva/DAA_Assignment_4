package org.example.graph.topo;

import java.util.*;
import org.example.model.*;

public class CondensationBuilder {
    public static List<Set<Integer>> build(int n, int[] compId, List<List<Edge>> adj, int compCount) {
        List<Set<Integer>> dag = new ArrayList<>();
        for (int i = 0; i < compCount; i++) {
            dag.add(new LinkedHashSet<>());
        }

        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                int from = compId[e.from()];
                int to = compId[e.to()];
                if (from != to) {
                    dag.get(from).add(to);
                }
            }
        }
        return dag;
    }

    public static List<Map<String, Integer>> edgesAsList(List<Set<Integer>> dag) {
        List<Map<String, Integer>> edges = new ArrayList<>();
        for (int i = 0; i < dag.size(); i++) {
            for (int j : dag.get(i)) {
                Map<String, Integer> e = new LinkedHashMap<>();
                e.put("from", i);
                e.put("to", j);
                edges.add(e);
            }
        }
        return edges;
    }

    public static int edgeCount(List<Set<Integer>> dag) {
        int count = 0;
        for (Set<Integer> edges : dag) count += edges.size();
        return count;
    }
}
