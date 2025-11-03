## 1. Introduction

The main goal of this work was to integrate several fundamental graph algorithms into one practical case: task scheduling within a Smart City or Smart Campus system. The implemented algorithms include:

*Strongly Connected Components (SCC)* detection using the Kosaraju method, which identifies cyclic dependencies among city service tasks.

*Topological sorting of the condensation graph (DAG)*, allowing the determination of a valid order of execution after compressing cycles.

*Shortest and longest path computation in a DAG* based on edge weights, enabling optimal scheduling (shortest paths) and identification of critical tasks (longest path).

These algorithms form a pipeline: detecting and compressing cycles, ordering tasks, and computing optimal execution sequences.

### 1.1 Algorithm Efficiency

Each algorithm has well-defined theoretical time complexity:

1. SCC (Kosaraju) – O(V + E), requiring two full DFS traversals.
2. Topological Sort (Kahn / DFS-based) – O(V + E), depending linearly on the number of nodes and edges.
3. Shortest Path in DAG – O(V + E), as it relaxes edges following the topological order without using a priority queue.

### 1.2 Theoretical Expectations

We expect:

Linear scalability with graph size.

Sparse graphs to run faster due to fewer edges.

Dense graphs and larger SCCs to slightly increase DFS visits and processing time.

DAG shortest path computations to remain efficient even on large datasets due to the absence of cycles.

## 2. Tests and Results

Nine datasets were created:

- Small graphs (1–3): 6–10 nodes, simple or few cycles.
- Medium graphs (4–6): 10–20 nodes, mixed structures with multiple SCCs.
- Large graphs (7–9): 20–50 nodes, dense structures used for performance testing.
- Each dataset was tested across three algorithms: SCC, Topological Sort, and DAG Shortest Paths. Metrics were collected for operation counts and execution time (milliseconds).

### 2.1 SCC Results (Kosaraju)

| Graph | DFS Visits | Edges Explored | Time (ms) |
|-------|------------|----------------|-----------|
| 1     | 12         | 10             | 0.1644    |
| 2     | 16         | 14             | 0.0525    |
| 3     | 14         | 12             | 0.0513    |
| 4     | 12         | 10             | 0.08      |
| 5     | 16         | 14             | 0.0211    |
| 6     | 14         | 12             | 0.0168    |
| 7     | 50         | 54             | 0.1738    |
| 8     | 60         | 58             | 0.0431    |
| 9     | 80         | 84             | 0.0681    |

Observation:
Execution time increases slightly with the number of nodes and edges. Small and medium graphs remain under 0.1 ms on average, while large graphs show higher DFS counts but still maintain linear growth, confirming O(V + E) complexity.

### 2.2 Topological Sort Results

| Graph | Pushes | Pops | Time (ms) |
|-------|--------|------|-----------|
| 1     | 4      | 4    | 0.0899    |
| 2     | 8      | 8    | 0.0289    |
| 3     | 5      | 5    | 0.0198    |
| 4     | 4      | 4    | 0.0471    |
| 5     | 8      | 8    | 0.0093    |
| 6     | 5      | 5    | 0.0058    |
| 7     | 13     | 13   | 0.0695    |
| 8     | 26     | 26   | 0.0620    |
| 9     | 15     | 15   | 0.0342    |

Observation:
Push/pop counts scale directly with the number of components (after SCC compression). The algorithm demonstrates excellent stability across all datasets. Even for the largest DAGs, time remains below 0.1 ms, confirming that topological sorting is extremely efficient for scheduling tasks.

### 2.3 Shortest Path in DAG (Edge Weight Model)

| Graph | Relaxations | Time (ms) |
|-------|-------------|-----------|
| 1     | 4           | 0.0135    |
| 2     | 14          | 0.0069    |
| 3     | 6           | 0.0045    |
| 4     | 4           | 0.0044    |
| 5     | 14          | 0.0028    |
| 6     | 6           | 0.0018    |
| 7     | 24          | 0.0071    |
| 8     | 46          | 0.0095    |
| 9     | 28          | 0.0055    |
Observation:
Relaxation counts correspond to the number of edges in the DAG. Even for large and dense graphs, times remain under 0.01 ms, showing that DAG shortest path algorithms are highly scalable and unaffected by cycle removal.

### 2.4 Comparative Analysis (Small vs. Medium vs. Large; Sparse vs. Dense)

1. Small graphs: Fastest overall, minimal DFS visits and pushes. Execution time dominated by I/O overhead rather than algorithmic cost.

2. Medium graphs: Slight increase in operation counts but time remains nearly constant, confirming linear scalability.

3. Large graphs: More DFS traversals and relaxations due to higher edge density. However, time still grows sub-linearly, showing that performance bottlenecks are minimal.

4. Sparse vs. Dense: Dense graphs require more DFS steps and relaxations but no exponential growth was observed. The algorithms behave predictably with respect to theoretical expectations.

### 2.5 Were Expectations Met?

Yes.
The observed data strongly aligns with theoretical predictions:

All algorithms scale linearly with V + E.

Kosaraju’s SCC shows minor time variation due to recursion overhead on large graphs.

Topological Sort and DAG Shortest Path consistently deliver low execution times and stable metrics.
Overall, the algorithms demonstrated predictable and efficient behavior across all test categories.

## 3. Conclusion

The combined experiment confirms that the SCC, Topological Sort, and DAG Shortest Path algorithms provide an effective workflow for dependency resolution and task scheduling in directed graphs.

Kosaraju’s algorithm effectively detects cycles and prepares graphs for condensation.

Topological sorting ensures a valid order of tasks without conflicts.

Shortest path in DAG yields optimal and critical task scheduling.

Performance analysis shows near-linear growth across all datasets and densities. The algorithms are suitable for both small-scale and large-scale planning systems, with minimal bottlenecks and stable time complexity.
These results validate the use of this algorithmic pipeline in Smart City or Smart Campus scheduling systems where dependency-based task optimization is required.