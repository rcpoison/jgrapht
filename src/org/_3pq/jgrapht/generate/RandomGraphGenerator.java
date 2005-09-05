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
 * RandomGraphGenerator.java
 * -----------------
 * (C) Copyright 2005, by Barak Naveh and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.generate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.VertexFactory;
import org._3pq.jgrapht.graph.DefaultDirectedGraph;
import org._3pq.jgrapht.graph.DirectedMultigraph;
import org._3pq.jgrapht.graph.Multigraph;
import org._3pq.jgrapht.graph.Pseudograph;
import org._3pq.jgrapht.graph.SimpleDirectedGraph;
import org._3pq.jgrapht.graph.SimpleGraph;

/**
 * This Generator creates a random topology graph of a specified number of vertexes and edges.
 * An instance of this generator 
 * will always return the same graph-topology in calls to generateGraph() .
 * The vertexes can be different ( depends on the VertexFactory implementation)
 * <p>However , two instances which uses the same 
 * constructor parameters will have two different random graphs (note: there is a possibilty that two 
 * instances will create the same results , but it unlikely , as using java random method)
 *	@author Assaf Lehr
 *	@since	Aug 6, 2005
 *
 */
public class RandomGraphGenerator implements GraphGenerator {
	protected int numOfVertexes;
	protected int numOfEdges;
	protected Random randomizer;
	private long randomizerSeed;
	
	/** This class is used to generate edge topology for a graph.
	 *  
	 * 	@author Assaf
	 *	@since	Aug 6, 2005
	 */
	public interface EdgeTopologyFactory
	{
		/** Two different calls to the createEdges() with the same parameters must result
		 * in the generation of the same. But if the randomizer is different , it should , usually,
		 * create different edge topology.
		 * 
		 * @param targetGraph - guranteed to start with zero edges. 
		 * @param orderToVertexMap - key=Integer of vertex order . between zero to numOfVertexes (exclusive).
		 * 							 value = vertex from the graph. unique.
		 * @param numberOfEdges	- to create in the graph
		 * @param randomizer
		 *  
		 */
		public void createEdges(Graph targetGraph , Map orderToVertexMap, int numberOfEdges , Random randomizer);
		
		/** 
		 * checks if the graph can contain the givven numberOfEdges according to the graph type
		 * restrictions.
		 * for example: <i> #V means number of vertexes in graph
		 * <li> a Simple Graph , can have max of #V*(#V-1)/2 edges.
		 * etc 
		 * @param targetGraph guranteed to start with zero edges.
		 * @param nuumberOfEdges
		 * @return
		 */
		public boolean isNumberOfEdgesValid(Graph targetGraph , int numberOfEdges);
		
	}
	
	/**
	 * Default implementation of the EdgeTopologyFactory interface.
	 * randomly chooses an edge and try to add it. If the add fails from any
	 * reason (like: self edge / multiple edges  in unpermitted graph type) it 
	 * will just choose another and try again. 
	 * Performance :
	 * <li> when the number of possible edges becomes slim , this class
	 * will have a very poor performance , cause it will not use gready methods to 
	 * choose them. for example : In simple graph , if #V = N (#x = number Of x)
	 * and we want full mesh #edges= N*(N-1)/2 , the first added edges will do so
	 * quickly (O(1) , the last will take O(N^2).
	 * So , do not use it in this kind of graphs.
	 * <li> If the numberOfEdges is bigger than what the graph can add , there will
	 * be an infinite loop here. It is not tested.
	 *   
	 * 
	 * 
	 *	@author Assaf
	 *	@since	Aug 6, 2005
	 *
	 */
	public class DefaultEdgeTopologyFactory implements EdgeTopologyFactory
	{
		public void createEdges(Graph targetGraph , Map orderToVertexMap, int numberOfEdges , Random randomizer)
		{
			int iterationsCounter=0;
			int edgesCounter=0;
			while (edgesCounter < numberOfEdges)
			{
				//randomizer.nextInt(int n) return a number between zero (includsive) and n(exclusive)
				Object startVertex 	= orderToVertexMap.get(Integer.valueOf(randomizer.nextInt(numOfVertexes)));
				Object endVertex 	= orderToVertexMap.get(Integer.valueOf(randomizer.nextInt(numOfVertexes)));
				try
				{
					Edge resultEdge = targetGraph.addEdge(startVertex,endVertex);
					if (resultEdge!=null)
					{
						edgesCounter++;
					}
				}
				catch (Exception e)
				{
					//do nothing.just ignore the edge
				}
				

				iterationsCounter++;
			}
		}

		/**
		 * checks if the numOfEdges is smalelr than the Max edges according to
		 * the following table:<p>
		 * 
		 * <table border=1 cellpadding=5>
		 * <tr align="center">
		 * 		<th>Graph Type</th>
		 * 		<th><i>Directed / UnDirected</i></th>
		 * 		<th><i>multiple edges 		</i></th>
		 * 		<th><i>loops				</i></th>
		 * 		<th><i>Max Edges			</i></th>
		 * 		
		 *  
		 * </tr>
		 * <tr align="center">  
		 * 		<td>SimpleGraph</td>
		 * 		<td>UnDirected</td>
		 * 		<td> - </td>
		 * 		<td> - </td>
		 * 		<td> N(N-1)/2 </td>
		 * </tr>
		 * <tr align="center">  
		 * 		<td>Multigraph</td>
		 * 		<td>UnDirected</td>
		 * 		<td> + </td>
		 * 		<td> - </td>
		 * 		<td> Infinite </td>
		 * </tr>
		 * <tr align="center">  
		 * 		<td>Pseudograph</td>
		 * 		<td>UnDirected</td>
		 * 		<td> + </td>
		 * 		<td> + </td>
		 * 		<td> Infinite </td>
		 * </tr>
		 * <tr align="center">  
		 * 		<td>SimpleDirectedGraph</td>
		 * 		<td>Directed</td>
		 * 		<td> - </td>
		 * 		<td> - </td>
		 * 		<td> N (N-1) </td>
		 * </tr>
		 * <tr align="center">  
		 * 		<td>DefaultDirectedGraph</td>
		 * 		<td>Directed</td>
		 * 		<td> - </td>
		 * 		<td> + </td>
		 * 		<td> N*(N-1)+ N = N^2</td>
		 * </tr>
		 * <tr align="center">  
		 * 		<td>DirectedMultigraph</td>
		 * 		<td>Directed</td>
		 * 		<td> + </td>
		 * 		<td> + </td>
		 * 		<td> Infinite </td>
		 * </tr>

		 * </table>
	
		 * @see org._3pq.jgrapht.generate.RandomGraphGenerator.EdgeTopologyFactory#isNumberOfEdgesValid(org._3pq.jgrapht.Graph, int)
		 */
		public boolean isNumberOfEdgesValid(Graph targetGraph, int numberOfEdges) 
		{
			boolean result;
			
			boolean infinite=false;
			int numOfVertexes = targetGraph.vertexSet().size();
			int maxAllowedEdges =  getMaxEdgesForVertexNum(targetGraph);
			if (maxAllowedEdges == -1)
			{
				infinite=true;
			}
			
			
			
			if (true==infinite)
			{
				result=true; 
			}
			else if (numberOfEdges <= maxAllowedEdges  )
			{
				result= true;
			}
			else
			{
				result= false;
			}
			return result;
		}
		
		/** return max edges for that graph. If it is infinite return -1 instead
		 * 
		 * @param targetGraph
		 * @param numOfVertexes
		 * @return
		 */
		public int getMaxEdgesForVertexNum(Graph targetGraph)
		{
			int maxAllowedEdges=0;
			if (targetGraph instanceof SimpleGraph)
			{
				maxAllowedEdges = numOfVertexes * (numOfVertexes - 1 ) /2;
			}
			else if (targetGraph instanceof SimpleDirectedGraph )
			{
				maxAllowedEdges = numOfVertexes * (numOfVertexes - 1 ) ;
			}
			else if (targetGraph instanceof DefaultDirectedGraph )
			{
				maxAllowedEdges = numOfVertexes * numOfVertexes  ;
			}
			else if (
						(targetGraph instanceof Multigraph)
					||	(targetGraph instanceof Pseudograph)
					||	(targetGraph instanceof DirectedMultigraph)
					)
				
			{
				maxAllowedEdges=-1; //infinite
			}
			else 
			{
				throw new ClassCastException("cannot find the appropriate graph type");
			}
			return maxAllowedEdges;
		}
	
	}
	
	
	
	public RandomGraphGenerator( int aNumOfVertexes , int aNumOfEdges ) {
        if ((numOfVertexes < 0) || (numOfEdges<0)) {
            throw new IllegalArgumentException("must be non-negative");
        }
        this.numOfVertexes = aNumOfVertexes;
        this.numOfEdges = aNumOfEdges;
        
         
         this.randomizerSeed = chooseRandomSeedOnce();
         this.randomizer = new Random(this.randomizerSeed);
        
    }
	/** should be called only once on creation.
	 *  chooses a seed which can be used later to reset the randomizer
	 * before each method call.
	 * This implementation uses copied the java.util.Random constructor.
	 * cause there is not getSeed() there , and seed is protected.
	 *  @author Assaf
	 *	@since	Aug 6, 2005
	 *
	 */
	private long chooseRandomSeedOnce()
	{
		long seedUniquifier = 8682522807148012L;
		
		return (++seedUniquifier + System.nanoTime());
	     
	}
	/** reset it to generate the same random stream 
	 */
	private void resetRandomSeed()
	{
		this.randomizer.setSeed(this.randomizerSeed);
	}

	
	/** (non-Javadoc)
	 * @see org._3pq.jgrapht.generate.GraphGenerator#generateGraph(org._3pq.jgrapht.Graph, org._3pq.jgrapht.VertexFactory, java.util.Map)
	 * @throws IllegalArgumentException if the aNumOfEdges passed in the constructor ,
	 * cannot be created on a graph of the concrete type with aNumOfVertexes.
	 * org._3pq.jgrapht.generate.RandomGraphGenerator.DefaultEdgeTopologyFactory#isNumberOfEdgesValid(org._3pq.jgrapht.Graph, int)
	 * 
	 */
	public void generateGraph(Graph target, VertexFactory vertexFactory,Map resultMap) 
	{
		resetRandomSeed();
		
		//key = generation order (1st,2nd,3rd,...) value=vertex Object
		//will be used later
		Map orderToVertexMap = new HashMap(this.numOfVertexes);
		
		for (int i=0;i<this.numOfVertexes;i++)
		{
			Object currVertex = vertexFactory.createVertex();
			target.addVertex(currVertex);
			orderToVertexMap.put(Integer.valueOf(i),currVertex);
		}
		
		//use specific type of edge factory , depending of the graph type 
		// and edge density
		EdgeTopologyFactory edgesFactory=edgeTopologyFactoryChooser(target,numOfEdges);
		if (!edgesFactory.isNumberOfEdgesValid(target,numOfEdges))
		{
			
			
			throw new IllegalArgumentException("numOfEdges is not valid for the graph type " 
					+ "\n-> Invalid number Of Edges=" +numOfEdges +" for:" 
					+ " graph type=" +target.getClass() 
					+" ,number Of Vertexes=" +this.numOfVertexes
					
					+ "\n-> Advice: For the Max value , check the javadoc for"
					+" org._3pq.jgrapht.generate.RandomGraphGenerator.DefaultEdgeTopologyFactory");
			
		}
		
		edgesFactory.createEdges(target,orderToVertexMap,this.numOfEdges,this.randomizer);
	}
	
	/**
	 * return concrete EdgeTopologyFactory , depending on graph type and
	 * numOfEdges
	 * @param target
	 * @return
	 */
	private EdgeTopologyFactory edgeTopologyFactoryChooser(Graph target,int numOfEdges)
	{
		return new DefaultEdgeTopologyFactory();
	}
	
	
	
	
	

}
