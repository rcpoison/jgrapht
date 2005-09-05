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
 * DefaultGraphMapping.java
 * -----------------
 * (C) Copyright 2005, by Assaf Lehr and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.graph;

import org._3pq.jgrapht.*;

import java.util.Map;

/**
 *  Implementaion of the GraphMapping interface.
 *  The performence of <code>getCorrespondence</code> is as the performence of
 *  the concrete Map class which is passed in the constructor.
 *  For example , using hashmaps , will provide O(1) performence.
 *  
 *  
 *	@author Assaf
 *	@since	Jul 30, 2005
 *
 */
public class DefaultGraphMapping implements GraphMapping {
	
	/** forward / reversemapping . will be lazy filled by two maps:
	 * <li>graphMapping[FORWARD] maps from graph1 to graph2
	 * <li>graphMapping{REVERSE] maps from graph2 to graph1 
	 */
	private Map[] graphMapping;
	
	private static int FORWARD=0;
	private static int REVERSE=1;
	
	private Graph graph1;
	private Graph graph2;
	
	/**
	 * The maps themselves are used.There is no defensive-copy. 
	 * Assumption :  The key and value in the mappings are of valid graph objects.
	 * It is not checked. 
	 * @param g1ToG2
	 * @param g2ToG1
	 * @param g1
	 * @param g2
	 */
	public DefaultGraphMapping(Map g1ToG2,Map g2ToG1,Graph g1,Graph g2)
	{
		this.graph1 = g1;
		this.graph2 = g2;
		this.graphMapping= new Map[2];
		this.graphMapping[FORWARD]=g1ToG2;
		this.graphMapping[REVERSE]=g2ToG1;
		
	}
	
	/** 
	 * two cases:
	 * vertexOfEdge is a vertex - if it is a key in the mapping , returns the value ,
	 * which can be null. If it is not a key or not is the source graph, throws IllegalArgumentException.
	 * 
	 * <p>vertexOfEdge instanceof Edge  - 
	 * if the source and target vertexes are not both:
	 * <li> in the source graph and
	 * <li> in the mapping 
	 * throws IllegalArgumentException.
	 * if source/target is null , throws NullPointerException
	 * otherwise , checks the corresponding vertexes. If one or more does not exist , 
	 * retrun null , otherwise if there is an edge between them in the graph , if so 
	 * return it. If there are more than one , return arbitrary one. If there are none ,
	 * returns null.
	 *  
	 * Assumption: if vertexOfEdge should be treated vertex , it may not be of 
	 * the class "org._3pq.jgrapht.Edge".
	 * (It will fail in cases the org._3pq.jgrapht.Edge is the vertex type)
	 * <p> Performence : as the Map type passed in the constructor
	 * @see org._3pq.jgrapht.GraphMapping#getCorrespondence(java.lang.Object, boolean)
	 */
	public Object getCorrespondence(Object vertexOrEdge, boolean forward) 
	{
		Graph sourceGraph,targetGraph;
		int direction;
		
		if (forward)
		{
			sourceGraph = this.graph1;
			targetGraph=  this.graph2;
			direction = FORWARD;
		}
		else
		{
			sourceGraph = this.graph2;
			targetGraph = this.graph1;
			direction = REVERSE;
		}
	
		Object resultObject=null;
			
		if (vertexOrEdge instanceof Edge)
		{
			Edge currEdge = (Edge) vertexOrEdge;
			Object mappedSourceVertex = getCorrespondenceVertex(currEdge.getSource(),forward);
			Object mappedTargetVertex = getCorrespondenceVertex(currEdge.getTarget(),forward);
			if ((mappedSourceVertex==null)||(mappedTargetVertex==null))
			{
				resultObject = null;
			}
			else
			{
				resultObject =targetGraph.getEdge(mappedSourceVertex,mappedTargetVertex);
			}
			
				
		}
		else	
		{
			resultObject = getCorrespondenceVertex(vertexOrEdge,forward);
		}
		return resultObject;
	}
	
	/**
	 * 
	 * @param keyVertex
	 * @param forward
	 * @return
	 * @throws  IllegalArgumentException if the keyVertex is not found in the sourceGraph , or
	 * if it is not found in the mapping
	 * @throws NullPointerException if the keyVertex is null
	 * 
	 */
	protected Object getCorrespondenceVertex(Object keyVertex, boolean forward) 
	{
		Graph sourceGraph;
		int direction;
		Object resultObject=null;
		
		if (keyVertex==null)
		{
			throw new NullPointerException("keyVertex parameter may not be null!");
		}
		if (forward)
		{
			sourceGraph = this.graph1;
			direction = FORWARD;
		}
		else
		{
			sourceGraph = this.graph2;
			direction = REVERSE;
		}
		
		if (!sourceGraph.containsVertex(keyVertex))
		{
			throw new IllegalArgumentException("The vertex cannot be found in the source graph");
		}
		else if (!this.graphMapping[direction].containsKey(keyVertex))
		{
			throw new IllegalArgumentException("The vertex cannot be found in the mapping");
		}
		else
		{
			resultObject = this.graphMapping[direction].get(keyVertex);
		}
		return resultObject;
		
	}
	
	

}
