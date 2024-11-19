package graph;

import java.util.*;

/**
 * Represents a mutable directed graph where each vertex is a String.
 */
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>(); // Stores unique vertices in the graph
    private final List<Edge> edges = new ArrayList<>();   // List of directed edges in the graph

    // Abstraction function and Representation invariant for ConcreteEdgesGraph:
    // AF(vertices, edges) = a directed graph where each element in 'vertices' represents a node,
    //    and each Edge in 'edges' signifies a directed link with weight between two nodes.
    // RI: vertices and edges are both non-null, edges contain only valid entries, each edge's
    //    source and target exist in vertices, and no two edges share identical source-target pairs.

    private void checkRep() {
        assert vertices != null : "Vertices set should not be null";
        assert edges != null : "Edges list should not be null";

        // Validate each edge: non-null and its vertices exist in the vertex set
        for (Edge edge : edges) {
            assert edge != null : "Edge should not be null";
            assert vertices.contains(edge.getSource()) : "Edge source must exist in vertices";
            assert vertices.contains(edge.getTarget()) : "Edge target must exist in vertices";
        }

        // Ensure uniqueness of each edge based on its source-target pairing
        Set<String> edgePairs = new HashSet<>();
        for (Edge edge : edges) {
            String edgePair = edge.getSource() + "->" + edge.getTarget();
            assert edgePairs.add(edgePair) : "Duplicate edges with same source-target are not allowed";
        }
    }

    /**
     * Adds a vertex to the graph if it is not already present.
     * @param vertex the vertex to be added
     * @return true if the vertex was added, false otherwise
     */
    @Override
    public boolean add(String vertex) {
        boolean added = vertices.add(vertex);
        checkRep();  // Verify representation invariant after modification
        return added;
    }
    
    /**
     * Sets or updates an edge between two vertices with the specified weight.
     * Removes the edge if weight is zero; otherwise, adds or updates it.
     * @param source the source vertex
     * @param target the target vertex
     * @param weight the weight of the edge
     * @return the previous weight of the edge, or 0 if the edge did not exist
     */
    @Override
    public int set(String source, String target, int weight) {
        int previousWeight = 0;
        
        // Locate and remove existing edge with the same source and target
        for (Iterator<Edge> it = edges.iterator(); it.hasNext();) {
            Edge edge = it.next();
            if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
                previousWeight = edge.getWeight();
                it.remove();
                break;
            }
        }

        // Add new edge if weight is positive
        if (weight > 0) {
            edges.add(new Edge(source, target, weight));
        }

        // Ensure both source and target vertices are in the set
        add(source);
        add(target);
        
        checkRep();  // Check representation invariant
        return previousWeight;
    }

    /**
     * Removes a vertex and all associated edges from the graph.
     * @param vertex the vertex to be removed
     * @return true if the vertex was removed, false otherwise
     */
    @Override
    public boolean remove(String vertex) {
        boolean removed = vertices.remove(vertex);
        
        // Remove all edges involving the specified vertex
        edges.removeIf(edge -> edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));
        
        checkRep();
        return removed;
    }

    /**
     * Retrieves all vertices in the graph.
     * @return a read-only set of all vertices
     */
    @Override
    public Set<String> vertices() {
        return Collections.unmodifiableSet(vertices);
    }

    /**
     * Returns a mapping of sources and their respective edge weights for a specified target.
     * @param target the target vertex
     * @return a map of source vertices and their edge weights to the target
     */
    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = new HashMap<>();
        
        for (Edge edge : edges) {
            if (edge.getTarget().equals(target)) {
                sources.put(edge.getSource(), edge.getWeight());
            }
        }
        
        return sources;
    }

    /**
     * Returns a mapping of targets and their respective edge weights for a specified source.
     * @param source the source vertex
     * @return a map of target vertices and their edge weights from the source
     */
    @Override
    public Map<String, Integer> targets(String source) {
        Map<String, Integer> targets = new HashMap<>();
        
        for (Edge edge : edges) {
            if (edge.getSource().equals(source)) {
                targets.put(edge.getTarget(), edge.getWeight());
            }
        }
        
        return targets;
    }

    /**
     * Provides a string representation of the graph, listing vertices and edges.
     * @return string describing the vertices and edges
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices: ").append(vertices).append("\nEdges:\n");
        for (Edge edge : edges) {
            sb.append(edge.toString()).append("\n");
        }
        return sb.toString();
    }
}

/**
 * Immutable class representing a directed edge in a graph with a source,
 * target, and weight.
 */
class Edge {
    private final String source; // Source vertex of the edge
    private final String target; // Target vertex of the edge
    private final int weight;    // Weight of the edge

    // Abstraction function and Representation invariant for Edge:
    // AF(source, target, weight) = an edge directed from 'source' to 'target' with a given 'weight'.
    // RI: source and target are non-null, weight must be non-negative

    public Edge(String source, String target, int weight) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target cannot be null");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be non-negative");
        }
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    /**
     * @return the source vertex of the edge
     */
    public String getSource() {
        return source;
    }

    /**
     * @return the target vertex of the edge
     */
    public String getTarget() {
        return target;
    }

    /**
     * @return the weight of the edge
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Provides a string representation of the edge in "source -> target (weight)" format.
     * @return string representation of the edge
     */
    @Override
    public String toString() {
        return source + " -> " + target + " (" + weight + ")";
    }
}
