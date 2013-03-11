package org.jgrapht;

import static org.junit.Assert.*;

import java.util.List;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class simpletest 
{
	public class TestEdge extends DefaultEdge implements Deactivateable
	{
		private static final long serialVersionUID = 1L;
		public boolean active = true;
				
		public boolean isActive() 
		{
			return active;
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public final void test() 
	{
		Graph<String, TestEdge> graph = new DefaultDirectedGraph<String, TestEdge>
		(
				new EdgeFactory<String, TestEdge>()
				{
					@Override
					public TestEdge createEdge(String sourceVertex, String targetVertex) 
					{
						return new TestEdge();
					}
				}
		);
		
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");
		
		graph.addEdge("a", "b");
		graph.addEdge("b", "c");
		TestEdge edge = graph.addEdge("c", "d");
		edge.active = false;
				
		List<TestEdge> edges = DijkstraShortestPath.findPathBetween(graph, "a", "c");
		
		assertNotNull(edges);
		assertEquals(2, edges.size());
		assertTrue(graph.getEdgeSource(edges.get(0)) == "a");
		assertTrue(graph.getEdgeTarget(edges.get(0)) == "b");
		assertTrue(graph.getEdgeTarget(edges.get(1)) == "c");
		
		edges = DijkstraShortestPath.findPathBetween(graph, "a", "d");
		assertNull(edges);
		
		edge.active = true;
		
		edges = DijkstraShortestPath.findPathBetween(graph, "a", "d");
		
		assertNotNull(edges);
		assertEquals(3, edges.size());
		assertTrue(graph.getEdgeSource(edges.get(0)) == "a");
		assertTrue(graph.getEdgeTarget(edges.get(0)) == "b");
		assertTrue(graph.getEdgeTarget(edges.get(1)) == "c");
		assertTrue(graph.getEdgeTarget(edges.get(2)) == "d");
	}
}
