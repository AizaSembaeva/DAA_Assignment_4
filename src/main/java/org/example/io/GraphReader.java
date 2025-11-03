package org.example.io;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Graph;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphReader {
    public static List<Graph> loadAll(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(new File(path));

        List<Graph> list = new ArrayList<>();

        if (root.has("graphs")) {
            for (JsonNode gNode : root.get("graphs")) {
                int n = gNode.get("n").asInt();
                Graph g = new Graph(n);

                JsonNode edges = gNode.get("edges");
                if (edges != null && edges.isArray()) {
                    for (JsonNode e : edges) {
                        int u = e.get("u").asInt();
                        int v = e.get("v").asInt();
                        double w = e.has("w") ? e.get("w").asDouble() : 1.0;
                        g.addEdge(u, v, w);
                    }
                }
                list.add(g);
            }
        } else {
            int n = root.get("n").asInt();
            Graph g = new Graph(n);

            JsonNode edges = root.get("edges");
            if (edges != null && edges.isArray()) {
                for (JsonNode e : edges) {
                    int u = e.get("u").asInt();
                    int v = e.get("v").asInt();
                    double w = e.has("w") ? e.get("w").asDouble() : 1.0;
                    g.addEdge(u, v, w);
                }
            }
            list.add(g);
        }

        return list;
    }
}
