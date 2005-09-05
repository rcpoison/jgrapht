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
 * PermutationIsomorphismInspector.java
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

import java.util.Iterator;
import java.util.Set;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.util.equivalence.EquivalenceComparator;
import org._3pq.jgrapht.util.permutation.CollectionPermutationIter;

/**
 *  Check every possible permutation. 
 *  <p> It does not uses the graph topology to enhance the performance.
 *  It is recommended to use only if there cannot be a useful division 
 *  to equivalence sets.
 *	@author Assaf
 *	@since	Jul 29, 2005
 *
 */
class PermutationIsomorphismInspector extends
		AbstractExhaustiveIsomorphismInspector {
	
	/**
	 * @param graph1
	 * @param graph2
	 * @param vertexChecker	eq. group checker for vertexes . If null , UniformEquivalenceComparator
	 * will be used as default (always return true)
	 * @param edgeChecker eq. group checker for edges . If null , UniformEquivalenceComparator
	 * will be used as default (always return true)
	 */
	public PermutationIsomorphismInspector(Graph graph1,Graph graph2,EquivalenceComparator vertexChecker,
			EquivalenceComparator edgeChecker)
	{ 
		super(graph1,graph2,vertexChecker,edgeChecker);
	} 
	/**
	 * constructor. Uses the default comparators.
	 * @see ExhaustiveIsomorphismInspector(Graph,Graph,EquivalenceComparator,EquivalenceComparator)
	 */
	public PermutationIsomorphismInspector(Graph graph1,Graph graph2)
	{
		super(graph1,graph2);
	}
	
	
	
	
	
	
	/**
	 * create the permuatation iterator , not dependant on equilty group , or the
	 * other vertexset.
	 * @param vertexSet
	 * @return
	 */
	protected CollectionPermutationIter createPermutationIterator(Set vertexSet1,Set vertexSet2)
	{
		return  new CollectionPermutationIter(vertexSet2);	
	}
	
	/** 
	 * 
	 * @param vertexSet1
	 * @param vertexSet2
	 * @return
	 */
	protected boolean areVertexSetsOfTheSameEqualityGroup(Set vertexSet1,Set vertexSet2)
	{
		
			if (vertexSet1.size()!=vertexSet2.size())
			{
				return false;
			}
			Iterator iter2 = vertexSet2.iterator();
			//only check hasNext() of one , cause they are of the same size
			for (Iterator iter1 = vertexSet1.iterator(); iter1.hasNext();) {
				Object vertex1 = (Object) iter1.next();
				Object vertex2 = (Object) iter2.next();
				if (!this.vertexComparator.equivalenceCompare(vertex1,vertex2, this.graph1, this.graph2))
				{
					return false;
				}
				
			}
			return true;
			
		}

}
