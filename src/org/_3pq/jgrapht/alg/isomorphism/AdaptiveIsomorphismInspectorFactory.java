/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2005, by Barak Naveh and Contributors.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -----------------
 * AdaptiveIsomorphismInspectorFactory.java
 * -----------------
 * (C) Copyright 2005, by Barak Naveh and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.alg.isomorphism;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.graph.DirectedMultigraph;
import org._3pq.jgrapht.graph.Multigraph;
import org._3pq.jgrapht.graph.Pseudograph;
import org._3pq.jgrapht.util.equivalence.EquivalenceComparator;
import org._3pq.jgrapht.util.equivalence.EquivalenceComparatorChain;
import org._3pq.jgrapht.util.equivalence.EquivalenceComparatorChainBase;

/**
 * This class serves as a factory to GraphIsomorphismInspector concrete implementation.
 * It can be used in two ways:
 * <li> You can can let this class to determine what is the most efficient algorithm
 * for your graph.
 * <li> You can specify the type of your graph (planar / tree / other) and save this
 * class the graph-checking time.
 * 
 * 
 * <p>Note that the concrete implementations are package-private and could should not
 * be created directly.If you are the maintainer of the package, you can add new
 * implementation classes , and add them to the "check-list". 
 * 
 * The current algorithms do not support graph with multiple edges (Multigraph / Pseudograph)
 * 
 * 
 * @author Assaf
 * @since	Jul 17, 2005
 * @see org._3pq.jgrapht.Graph.GraphIsomorphismInspector
 *
 */
public class AdaptiveIsomorphismInspectorFactory {
	
	
	public static final int GRAPH_TYPE_REGULAR = 0;
	public static final int GRAPH_TYPE_PLANAR = 1;
	public static final int GRAPH_TYPE_TREE = 2;
	public static final int GRAPH_TYPE_MULTIGRAPH = 3;
	
	
	/** 
	 * let this class to determine what is the most efficient algorithm
	 * for your graph.
	 * @param graph1
	 * @param graph2
	 * @param vertexChecker may be null
	 * @param edgeChecker may be null
	 * @return
	 */
	public static GraphIsomorphismInspector createIsomorphismInspector(Graph graph1,Graph graph2,EquivalenceComparator vertexChecker,
			EquivalenceComparator edgeChecker)
	{
		int graphType = checkGraphsType(graph1,graph2);
		return createAppropriateConcreteInspector(graphType,graph1,graph2,vertexChecker,edgeChecker);
	}
	
	/**
	 * let this class to determine what is the most efficient algorithm
	 * for your graph.
	 * <p> same as calling createIsomorphismInspector(graph1,graph2,null,null);
	 * @param graph1
	 * @param graph2
	 * @return
	 */
	public static GraphIsomorphismInspector createIsomorphismInspector(Graph graph1,Graph graph2)
	{
		return createIsomorphismInspector(graph1,graph2,null,null);
	}
	
	/**
	 * specify the type of your graph (planar / tree / other) and save this
	 * class the graph-checking time.
	 * @param type  - AdaptiveIsomorphismInspectorFactory.GRAPH_TYPE_XXX
	 * @param graph1
	 * @param graph2
	 * @param vertexChecker - can be null
	 * @param edgeChecker  - can be null
	 * @return
	 */
	public static GraphIsomorphismInspector createIsomorphismInspectorByType(int type,Graph graph1,Graph graph2,EquivalenceComparator vertexChecker,
			EquivalenceComparator edgeChecker)
	{
		return createAppropriateConcreteInspector(type,graph1,graph2,vertexChecker,edgeChecker);
	}
	
	/**
	 * specify the type of your graph (planar / tree / other) and save this
	 * class the graph-checking time.
	 * <p> same as calling  createAppropriateConcreteInspector(graph1,graph2,null,null);
	 * @param type - AdaptiveIsomorphismInspectorFactory.GRAPH_TYPE_XXX
	 * @param graph1
	 * @param graph2
	 * @return
	 */
	
	public static GraphIsomorphismInspector createIsomorphismInspectorByType(int type,Graph graph1,Graph graph2)
	{
		return createAppropriateConcreteInspector(type,graph1,graph2,null,null);
	}
	
	
		
	/** 
	 * checks the graph type , accordingly decides which type of concrete inspector
	 * class to create.
	 * This implementation : creates the ExhaustiveIsomorphismInspector without further
	 * tests , because no other implementations is available in this version.
	 * 
	 * 
	 * default : use ExhaustiveIsomorphismInspectorEquivalenceSets
	 * if there were no comparators . Use PermutationIsomorphismInspector - it will work faster.w
	 * 
	 * @param graph1
	 * @param graph2
	 * @param vertexChecker
	 * @param edgeChecker
	 */
	protected static GraphIsomorphismInspector createAppropriateConcreteInspector(int graphType,Graph graph1,Graph graph2,EquivalenceComparator vertexChecker,
			EquivalenceComparator edgeChecker)
	{
		assertUnsupportedGraphTypes(graph1,graph2);
		GraphIsomorphismInspector currentInspector=null;
		
		switch (graphType) {
		case GRAPH_TYPE_PLANAR:
		case GRAPH_TYPE_TREE:
		case GRAPH_TYPE_REGULAR:
			currentInspector = createTopologicalExhaustiveInspector(graph1,graph2,vertexChecker,edgeChecker);
			break;
		
			
		default:
		//	currentInspector = new EquivalenceIsomorphismInspector(graph1,graph2,vertexChecker,edgeChecker);
		}
		return currentInspector;
	}
	
	/**
	 * checks if one of the graphs is from unsupported graph type and throws
	 * IllegalArgumentException if it is.
	 * The current unsupported are graphs with multiple-edges;
	 * @param graph1
	 * @param graph2
	 * @throws IllegalArgumentException
	 */
	protected static void assertUnsupportedGraphTypes(Graph graph1,Graph graph2) throws IllegalArgumentException
	{
		Graph[] graphArray = new Graph[] {graph1,graph2};
		for (int i = 0; i < graphArray.length; i++) {
			Graph g = graphArray[i];
			if (	(g instanceof Multigraph) 
				 || (g instanceof DirectedMultigraph)
				 || (g instanceof Pseudograph)
				 )
			{
				throw new IllegalArgumentException("graph type not supported for the graph"+g);
			}
		}
		
	}
	
	
	protected static int checkGraphsType(Graph graph1,Graph graph2)
	{
		return GRAPH_TYPE_REGULAR;
	}
	
	/** return ExhaustiveInspector , where the euqivalance comparator is chained with topological
	 * comparator.this implementation uses:
	 * <li> vertex degree size comparator
	 * @return
	 */
	protected static GraphIsomorphismInspector createTopologicalExhaustiveInspector(Graph graph1,Graph graph2,EquivalenceComparator vertexChecker,
			EquivalenceComparator edgeChecker)
	{
		VertexDegreeEquivalenceComparator degreeComparator = new VertexDegreeEquivalenceComparator();
		EquivalenceComparatorChain vertexChainedChecker=new EquivalenceComparatorChainBase(degreeComparator);
		vertexChainedChecker.addComparatorAfter(vertexChecker);
		
		GraphIsomorphismInspector inspector= new EquivalenceIsomorphismInspector(graph1,graph2,vertexChainedChecker,
				 edgeChecker);
		return inspector;
	}
	
	

	

}
