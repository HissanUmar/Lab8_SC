package graph;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ConcreteEdgesGraphTest extends GraphInstanceTest {

    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteEdgesGraph();
    }

    // Check adding a vertex to the graph
    @Test
    public void testInsertNode() {
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph();
        assertTrue("Adding node X should return true", graph.add("X"));
        assertFalse("Adding duplicate node X should return false", graph.add("X"));
        assertTrue("Graph should now contain node X", graph.vertices().contains("X"));
    }

    // Check setting an edge between nodes
    @Test
    public void testDefineEdge() {
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph();
        graph.add("X");
        graph.add("Y");

        int prevWeight = graph.set("X", "Y", 8);
        assertEquals("New edge from X to Y should return weight 0", 0, prevWeight);
        assertEquals("Edge weight from X to Y should be 8", 8, (int) graph.targets("X").get("Y"));

        prevWeight = graph.set("X", "Y", 15);
        assertEquals("Previous weight should be 8", 8, prevWeight);
        assertEquals("Edge weight from X to Y should update to 15", 15, (int) graph.targets("X").get("Y"));

        prevWeight = graph.set("X", "Y", 0);
        assertEquals("Removing edge should return previous weight 15", 15, prevWeight);
        assertFalse("Edge from X to Y should now be removed", graph.targets("X").containsKey("Y"));
    }

    // Check removing a vertex from the graph
    @Test
    public void testDeleteNode() {
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph();
        graph.add("X");
        graph.add("Y");
        graph.set("X", "Y", 5);

        assertTrue("Removing node X should return true", graph.remove("X"));
        assertFalse("Node X should no longer exist in graph", graph.vertices().contains("X"));
        assertTrue("Any edges from X should be removed", graph.targets("Y").isEmpty());

        assertFalse("Removing a non-existent node should return false", graph.remove("X"));
    }

    // Check retrieval of all vertices
    @Test
    public void testAllNodes() {
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph();
        graph.add("X");
        graph.add("Y");

        Set<String> nodes = graph.vertices();
        assertEquals("Graph should contain exactly 2 nodes", 2, nodes.size());
        assertTrue("Nodes X and Y should be present", nodes.contains("X") && nodes.contains("Y"));

        try {
            nodes.add("Z");
            fail("Vertices set should be immutable");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    // Check sources leading to a target node
    @Test
    public void testIncomingEdges() {
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph();
        graph.add("X");
        graph.add("Y");
        graph.set("X", "Y", 7);

        Map<String, Integer> incomingEdges = graph.sources("Y");
        assertEquals("Y should have one incoming edge", 1, incomingEdges.size());
        assertTrue("Incoming edge should be from X with weight 7", incomingEdges.containsKey("X") && incomingEdges.get("X") == 7);
    }

    // Check targets from a source node
    @Test
    public void testOutgoingEdges() {
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph();
        graph.add("X");
        graph.add("Y");
        graph.set("X", "Y", 9);

        Map<String, Integer> outgoingEdges = graph.targets("X");
        assertEquals("X should have one outgoing edge", 1, outgoingEdges.size());
        assertTrue("Outgoing edge should be to Y with weight 9", outgoingEdges.containsKey("Y") && outgoingEdges.get("Y") == 9);
    }

    // Verify the string representation of the graph
    @Test
    public void testGraphStringRepresentation() {
        ConcreteEdgesGraph graph = new ConcreteEdgesGraph();
        graph.add("X");
        graph.add("Y");
        graph.set("X", "Y", 6);

        String expected = "Vertices: [X, Y]\nEdges:\nX -> Y (6)\n";
        assertEquals("Graph toString should match expected output", expected, graph.toString());
    }

    // Test the Edge class: constructor, getters, and toString
    @Test
    public void testEdgeAttributes() {
        Edge edge = new Edge("X", "Y", 6);
        assertEquals("Edge source should be X", "X", edge.getSource());
        assertEquals("Edge target should be Y", "Y", edge.getTarget());
        assertEquals("Edge weight should be 6", 6, edge.getWeight());
    }

    // Verify string representation of an edge
    @Test
    public void testEdgeStringOutput() {
        Edge edge = new Edge("X", "Y", 6);
        assertEquals("Edge string should match expected format", "X -> Y (6)", edge.toString());
    }

    // Test the Edge constructor with invalid inputs
    @Test(expected = IllegalArgumentException.class)
    public void testEdgeWithNegativeWeight() {
        new Edge("X", "Y", -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdgeWithNullSource() {
        new Edge(null, "Y", 6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEdgeWithNullTarget() {
        new Edge("X", null, 6);
    }
}
