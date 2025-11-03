package org.example.model;

public class Edge {
    private final int from;
    private final int to;
    private final double weight;

    public Edge(int from, int to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int from() { return from; }
    public int to() { return to; }
    public double weight() { return weight; }

    @Override
    public String toString() {
        return String.format("%d -> %d (%.2f)", from, to, weight);
    }
}
