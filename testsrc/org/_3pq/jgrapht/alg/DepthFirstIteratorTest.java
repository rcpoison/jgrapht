/*
 * Created on Jul 29, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org._3pq.jgrapht.alg;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.GraphFactory;
import org._3pq.jgrapht.alg.DepthFirstIterator;

import junit.framework.TestCase;

/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DepthFirstIteratorTest extends TestCase {
	public void testDirectedGraph() {
		GraphFactory graphFactory = GraphFactory.getFactory();
		DirectedGraph graph = graphFactory.createDirectedGraph();
		String v1 = "1";
		graph.addVertex(v1);
		String v2 = "2";
		graph.addVertex(v2);
		String v3 = "3";
		graph.addVertex("3");
		String v4 = "4";
		graph.addVertex("4");
		String v5 = "5";
		graph.addVertex("5");
		String v6 = "6";
		graph.addVertex("6");
		String v7 = "7";
		graph.addVertex("7");
		String v8 = "8";
		graph.addVertex("8");
		String v9 = "9";
		graph.addVertex("9");
		graph.addEdge(v1, v2);
		graph.addEdge(v1, v3);
		graph.addEdge(v2, v4);
		graph.addEdge(v3, v5);
		graph.addEdge(v3, v6);
		graph.addEdge(v5, v6);
		graph.addEdge(v5, v7);
		graph.addEdge(v6, v1);
		graph.addEdge(v7, v8);
		graph.addEdge(v7, v9);
		graph.addEdge(v8, v2);
		graph.addEdge(v9, v4);
		DepthFirstIterator iterator = new DepthFirstIterator(graph, v1);
		StringBuffer result = new StringBuffer();
		for (; iterator.hasNext();) {
			result.append((String) iterator.next());
		}
		String s = result.toString();
		System.out.println(s);
		assertEquals("136579482", s);
	}
}
