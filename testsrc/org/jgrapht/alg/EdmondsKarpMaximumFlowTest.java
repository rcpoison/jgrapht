package org.jgrapht.alg;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

import junit.framework.*;

public final class EdmondsKarpMaximumFlowTest
	extends TestCase
{

	/**
	 * .
	 */
	public void testLogic()
	{
		runTest(
				new int[]{3, 1, 4, 3, 2, 8, 2, 5, 7},
				new int[]{1, 4, 8, 2, 8, 6, 5, 7, 6},
				new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1},
				new int[]{3},
				new int[]{6},
				new double[]{2});
	}
	
	private void runTest(
			int[] tails,
			int[] heads,
			double[] capacities,
			int[] sources,
			int[] sinks,
			double[] expectedResults)
	{		
		assertTrue(tails.length == heads.length);
		assertTrue(tails.length == capacities.length);
		DirectedWeightedMultigraph<Integer, DefaultWeightedEdge> network = new DirectedWeightedMultigraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		int m = tails.length;
		for (int i = 0; i < m; i++)
		{
			network.addVertex(tails[i]);
			network.addVertex(heads[i]);
			DefaultWeightedEdge e = network.addEdge(tails[i], heads[i]);
			network.setEdgeWeight(e, capacities[i]);
		}
		assertTrue(sources.length == sinks.length);
		int q = sources.length;
		for (int i = 0; i < q; i++)
		{
			network.addVertex(sources[i]);
			network.addVertex(sinks[i]);
		}
		EdmondsKarpMaximumFlow<Integer, DefaultWeightedEdge> solver = new EdmondsKarpMaximumFlow<Integer, DefaultWeightedEdge>(network);
		assertTrue(solver.getCurrentSource() == null);
		assertTrue(solver.getCurrentSink() == null);
		for (int i = 0; i < q; i++)
		{
			solver.calculateMaximumFlow(sources[i], sinks[i]);
			assertTrue(solver.getCurrentSource().equals(sources[i]));
			assertTrue(solver.getCurrentSink().equals(sinks[i]));
			double flowValue = solver.getMaximumFlowValue();
			Map<DefaultWeightedEdge, Double> flow = solver.getMaximumFlow();
			assertEquals(expectedResults[i], flowValue, EdmondsKarpMaximumFlow.DEFAULT_EPSILON);
			for (DefaultWeightedEdge e: network.edgeSet())
			{
				assertTrue(flow.containsKey(e));
			}
			for (DefaultWeightedEdge e: flow.keySet())
			{
				assertTrue(network.containsEdge(e));
			}
		}
	}
}
