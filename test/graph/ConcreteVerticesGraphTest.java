package graph;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class ConcreteVerticesGraphTest extends GraphInstanceTest {

    // Returns a new empty instance of the graph
    @Override
    public Graph<String> emptyInstance() {
        return new ConcreteVerticesGraph();
    }

    // Tests that adding a unique vertex succeeds, while duplicate addition fails
    @Test
    public void testAddUniqueVertex() {
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph();
        assertTrue("Vertex X should be added successfully", graph.add("X"));
        assertFalse("Adding vertex X again should return false", graph.add("X"));
    }

    // Tests setting edges and updating or removing them
    @Test
    public void testSetAndUpdateEdge() {
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph();
        graph.add("X");
        graph.add("Y");

        assertEquals("New edge from X to Y with weight 2 should return 0", 0, graph.set("X", "Y", 2));
        assertEquals("Updating edge from X to Y with weight 3 should return previous weight 2", 2, graph.set("X", "Y", 3));
        assertEquals("Setting weight of edge from X to Y to 0 should remove the edge and return previous weight 3", 3, graph.set("X", "Y", 0));
        assertTrue("Edge from X to Y should be removed", graph.targets("X").isEmpty());
    }

    // Tests removal of vertices and any associated edges
    @Test
    public void testVertexRemoval() {
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph();
        graph.add("P");
        graph.add("Q");
        graph.set("P", "Q", 5);

        assertTrue("Vertex Q should be removed", graph.remove("Q"));
        assertFalse("Removing non-existent vertex R should return false", graph.remove("R"));
        assertTrue("Edge from P to Q should also be removed", graph.targets("P").isEmpty());
    }

    // Tests retrieval of vertices and immutability of the set
    @Test
    public void testRetrieveVertices() {
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph();
        graph.add("U");
        graph.add("V");

        Set<String> vertices = graph.vertices();
        assertEquals("Expected vertices count", 2, vertices.size());
        assertTrue("Vertices should contain U and V", vertices.contains("U") && vertices.contains("V"));

        try {
            vertices.add("W");
            fail("Vertices set should be unmodifiable");
        } catch (UnsupportedOperationException e) {
            // expected exception
        }
    }

    // Tests sources for a target vertex with correct weight
    @Test
    public void testSourceVertices() {
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph();
        graph.add("M");
        graph.add("N");
        graph.set("M", "N", 4);

        Map<String, Integer> sources = graph.sources("N");
        assertEquals("N should have one source", 1, sources.size());
        assertTrue("Source should be M with weight 4", sources.containsKey("M") && sources.get("M") == 4);
    }

    // Tests targets for a source vertex with correct weight
    @Test
    public void testTargetVertices() {
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph();
        graph.add("P");
        graph.add("Q");
        graph.set("P", "Q", 3);

        Map<String, Integer> targets = graph.targets("P");
        assertEquals("P should have one target", 1, targets.size());
        assertTrue("Target should be Q with weight 3", targets.containsKey("Q") && targets.get("Q") == 3);
    }

    // Tests that the string representation matches the expected format
    @Test
    public void testGraphToString() {
        ConcreteVerticesGraph graph = new ConcreteVerticesGraph();
        graph.add("J");
        graph.add("K");
        graph.set("J", "K", 3);

        String expectedOutput = "J edges: {K=3}\nK edges: {}\n";
        assertEquals("toString should return correct representation of graph", expectedOutput, graph.toString());
    }

    /*
     * Tests for Vertex class...
     */

    // Tests adding an edge to a vertex and verifying the weight
    @Test
    public void testVertexAddEdge() {
        Vertex vertex = new Vertex("X");
        assertTrue("Adding edge from X to Y with weight 2 should return true", vertex.addEdge("Y", 2));
        assertEquals("Edge weight from X to Y should be 2", (Integer) 2, vertex.getEdges().get("Y"));
    }

    // Tests removing an edge from a vertex and verifying its absence
    @Test
    public void testVertexRemoveEdge() {
        Vertex vertex = new Vertex("R");
        vertex.addEdge("S", 2);
        assertTrue("Removing edge from R to S should return true", vertex.removeEdge("S"));
        assertFalse("Edge from R to S should be removed", vertex.getEdges().containsKey("S"));
    }

    // Tests the string representation of a vertex
    @Test
    public void testVertexStringOutput() {
        Vertex vertex = new Vertex("Q");
        vertex.addEdge("R", 2);
        assertEquals("toString should return correct representation of vertex", "Q edges: {R=2}", vertex.toString());
    }

    // Tests that adding an edge with a negative weight throws an exception
    @Test(expected = IllegalArgumentException.class)
    public void testVertexInvalidEdgeWeight() {
        new Vertex("X").addEdge("Y", -1);
    }
}
