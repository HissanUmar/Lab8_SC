package graph;

import static org.junit.Assert.*;
import java.util.Collections;
import java.util.Set;
import org.junit.Test;

/**
 * Abstract test class for the Graph interface. This class defines the
 * structure of test cases for any concrete implementation of a directed graph.
 */
public abstract class GraphInstanceTest {

    /**
     * Creates and returns an empty instance of the graph.
     * @return a new empty Graph instance
     */
    public abstract Graph<String> emptyInstance();

    /**
     * Ensures that assertions are enabled. 
     * The test will fail if assertions are not enabled in the VM arguments.
     */
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // should throw AssertionError if assertions are enabled
    }

    /**
     * Tests that a newly created graph has no vertices.
     */
    @Test
    public void testInitialVerticesEmpty() {
        assertEquals("New graph should have no vertices",
                Collections.emptySet(), emptyInstance().vertices());
    }

    /**
     * Tests that a single vertex can be added to the graph
     * and that it is present in the set of vertices.
     */
    @Test
    public void testAddVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("Node1");
        assertTrue("Graph should contain the added vertex",
                graph.vertices().contains("Node1"));
    }

    /**
     * Tests that attempting to add a duplicate vertex returns false
     * and does not increase the vertex count.
     */
    @Test
    public void testAddDuplicateVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("Vertex should be added initially", graph.add("NodeX"));
        assertFalse("Adding duplicate vertex should return false", graph.add("NodeX"));
        assertEquals("Graph should not contain duplicate vertices", 1, graph.vertices().size());
    }

    /**
     * Tests setting an edge between two vertices, including adding
     * a new edge with weight and updating an existing edge's weight.
     */
    @Test
    public void testSetEdge() {
        Graph<String> graph = emptyInstance();
        graph.add("StartNode");
        graph.add("EndNode");

        int initialWeight = graph.set("StartNode", "EndNode", 7);
        assertEquals("Expected no prior weight for new edge", 0, initialWeight);

        assertEquals("Expected weight to be updated for edge",
                7, (int) graph.targets("StartNode").get("EndNode"));
    }

    /**
     * Tests that a vertex can be removed and that it no longer
     * exists in the graph afterward.
     */
    @Test
    public void testRemoveVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("RemovableNode");
        assertTrue("Vertex should be successfully removed", graph.remove("RemovableNode"));
        assertFalse("Removed vertex should not be in graph", graph.vertices().contains("RemovableNode"));
    }

    /**
     * Tests the correct mapping of sources and targets for an edge.
     * Verifies that sources and targets return expected nodes and weights.
     */
    @Test
    public void testSourcesAndTargets() {
        Graph<String> graph = emptyInstance();
        graph.add("SourceNode");
        graph.add("TargetNode");
        graph.set("SourceNode", "TargetNode", 4);
        
        Set<String> sourcesForTarget = graph.sources("TargetNode").keySet();
        Set<String> targetsForSource = graph.targets("SourceNode").keySet();
        
        assertTrue("SourceNode should be a source of TargetNode", sourcesForTarget.contains("SourceNode"));
        assertTrue("TargetNode should be a target of SourceNode", targetsForSource.contains("TargetNode"));
        assertEquals("Edge weight from SourceNode to TargetNode should be 4", 
                (Integer) 4, graph.targets("SourceNode").get("TargetNode"));
    }
}
