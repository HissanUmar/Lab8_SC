package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConcreteVerticesGraph implements Graph<String> {
    
    // List of vertices representing the graph structure.
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Constructor for creating an empty graph.
    public ConcreteVerticesGraph() {
        checkRep();
    }
    
    // Abstraction function and Representation invariant for ConcreteVerticesGraph:
    // AF(vertices) = a directed graph where each Vertex in vertices represents a node
    //    in the graph, and each edge in Vertex's edges represents an edge in the graph.
    // RI: vertices is not null, does not contain null elements,
    //    and no two vertices have the same label.

    // Checks representation invariant to maintain graph integrity.
    private void checkRep() {
        assert vertices != null : "vertices list should not be null";
        Set<String> labels = new HashSet<>();
        for (Vertex v : vertices) {
            assert v != null : "vertex should not be null";
            assert labels.add(v.getLabel()) : "duplicate vertex label";
        }
    }

    @Override
    public boolean add(String vertex) {
        // Adds a vertex to the graph if it doesn't already exist.
        if (findVertex(vertex) == null) {
            vertices.add(new Vertex(vertex));
            checkRep();
            return true;
        }
        return false;
    }
    
    @Override
    public int set(String source, String target, int weight) {
        // Adds or updates an edge between two vertices with the specified weight.
        // Returns the previous weight of the edge, or 0 if it was not present.
        Vertex sourceVertex = findOrAddVertex(source);
        Integer previousWeight = sourceVertex.getEdges().get(target);
        sourceVertex.addEdge(target, weight);
        checkRep();
        return previousWeight == null ? 0 : previousWeight;
    }
    
    @Override
    public boolean remove(String vertex) {
        // Removes the specified vertex and all its associated edges from the graph.
        Vertex v = findVertex(vertex);
        if (v != null) {
            vertices.remove(v);
            for (Vertex vert : vertices) {
                vert.removeEdge(vertex);
            }
            checkRep();
            return true;
        }
        return false;
    }
    
    @Override
    public Set<String> vertices() {
        // Returns a set of all vertex labels in the graph.
        Set<String> vertexLabels = new HashSet<>();
        for (Vertex v : vertices) {
            vertexLabels.add(v.getLabel());
        }
        return Collections.unmodifiableSet(vertexLabels);
    }
    
    @Override
    public Map<String, Integer> sources(String target) {
        // Finds and returns all vertices with edges pointing to the specified target vertex.
        Map<String, Integer> sources = new HashMap<>();
        for (Vertex v : vertices) {
            if (v.getEdges().containsKey(target)) {
                sources.put(v.getLabel(), v.getEdges().get(target));
            }
        }
        return sources;
    }
    
    @Override
    public Map<String, Integer> targets(String source) {
        // Returns all edges and their weights originating from the specified source vertex.
        Vertex v = findVertex(source);
        return v == null ? Collections.emptyMap() : v.getEdges();
    }
    
    @Override
    public String toString() {
        // Creates a string representation of the graph showing each vertex and its edges.
        StringBuilder sb = new StringBuilder();
        for (Vertex v : vertices) {
            sb.append(v.toString()).append("\n");
        }
        return sb.toString();
    }
    
    // Searches for a vertex by label; returns the vertex if found, else null.
    private Vertex findVertex(String label) {
        for (Vertex v : vertices) {
            if (v.getLabel().equals(label)) {
                return v;
            }
        }
        return null;
    }
    
    // Finds a vertex by label, or creates and adds a new one if it doesn't exist.
    private Vertex findOrAddVertex(String label) {
        Vertex v = findVertex(label);
        if (v == null) {
            v = new Vertex(label);
            vertices.add(v);
        }
        return v;
    }
}

class Vertex {
    private final String label;  // Unique identifier for this vertex.
    private final Map<String, Integer> edges = new HashMap<>();  // Edges with weights.

    // Constructor for creating a vertex with a specific label.
    public Vertex(String label) {
        this.label = label;
    }
    
    // Abstraction function and Representation invariant for Vertex:
    // AF(label, edges) = a node labeled 'label' with directed edges and weights in 'edges'.
    // RI: label != null, edges != null, no edge weight is negative.

    // Ensures representation invariant holds for the vertex.
    private void checkRep() {
        assert label != null : "label should not be null";
        assert edges != null : "edges map should not be null";
        for (int weight : edges.values()) {
            assert weight >= 0 : "edge weights must be non-negative";
        }
    }
    
    public String getLabel() {
        return label;
    }
    
    public boolean addEdge(String target, int weight) {
        // Adds or updates an edge to the target vertex with the specified weight.
        if (weight < 0) {
            throw new IllegalArgumentException("Edge weight cannot be negative");
        }
        if (weight == 0) {
            return edges.remove(target) != null;
        } else {
            edges.put(target, weight);
            checkRep();
            return true;
        }
    }
    
    public boolean removeEdge(String target) {
        // Removes the edge to the target vertex, if it exists.
        return edges.remove(target) != null;
    }
    
    public Map<String, Integer> getEdges() {
        // Returns an unmodifiable view of edges from this vertex.
        return Collections.unmodifiableMap(edges);
    }
    
    @Override
    public String toString() {
        return label + " edges: " + edges;
    }
}
