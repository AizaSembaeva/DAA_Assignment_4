package org.example.metrics;

public class PerformanceTracker {
    private long dfsVisits;
    private long edgesExplored;
    private long pushes;
    private long pops;
    private long relaxations;

    private double timeMs;
    private long startNano;

    public void incDfsVisits() { dfsVisits++; }
    public void incEdgesExplored() { edgesExplored++; }
    public void incPushes() { pushes++; }
    public void incPops() { pops++; }
    public void incRelaxations() { relaxations++; }

    public void startTimer() {
        startNano = System.nanoTime();
    }

    public void stopTimer() {
        timeMs = (System.nanoTime() - startNano) / 1_000_000.0;
    }

    public long getDfsVisits() { return dfsVisits; }
    public long getEdgesExplored() { return edgesExplored; }
    public long getPushes() { return pushes; }
    public long getPops() { return pops; }
    public long getRelaxations() { return relaxations; }
    public double getTimeMs() { return timeMs; }

    public void reset() {
        dfsVisits = edgesExplored = pushes = pops = relaxations = 0;
        timeMs = 0.0;
    }

    @Override
    public String toString() {
        return String.format(
                "dfs=%d, edges=%d, pushes=%d, pops=%d, relax=%d, time=%.3f ms",
                dfsVisits, edgesExplored, pushes, pops, relaxations, timeMs
        );
    }
}
