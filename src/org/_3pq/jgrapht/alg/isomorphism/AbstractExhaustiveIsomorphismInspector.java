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
 * AbstractExhaustiveIsomorphismInspector.java
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
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.util.PrefetchIterator;
import org._3pq.jgrapht.util.equivalence.UniformEquivalenceComparator;
import org._3pq.jgrapht.util.equivalence.EquivalenceComparator;
import org._3pq.jgrapht.util.permutation.CollectionPermutationIter;


/**
 * The current algorithms do not support graph with multiple edges (Multigraph / Pseudograph)
 * For the maintainer: The reason for it is the use of GraphOrdering which currently only
 * supports partial graph types.
 *  
 *
 *	@author Assaf Lehr
 *	@since	May 20, 2005 ver5.3
 *	
 *
 */
abstract class AbstractExhaustiveIsomorphismInspector implements GraphIsomorphismInspector
{
	public static EquivalenceComparator edgeDefaultIsomorphismComparator= 
		new UniformEquivalenceComparator();
	public static EquivalenceComparator vertexDefaultIsomorphismComparator= 
		new UniformEquivalenceComparator();
	
	
	protected EquivalenceComparator edgeComparator;
	protected EquivalenceComparator vertexComparator;
	
	protected Graph graph1;
	protected Graph graph2;
	
	private PrefetchIterator nextSupplier;
	private boolean wasIsomorphismFound;
	
	//kept as member , to ease computations
	private GraphOrdering lableGraph1;
	private LinkedHashSet graph1VertexSet;
	private LinkedHashSet graph2EdgeSet;
	private CollectionPermutationIter vertexPermuteIter;
	private Set currVertexPermutation; //filled every iteration , used in the result relation.
		

	/**
	 * @param graph1
	 * @param graph2
	 * @param vertexChecker	eq. group checker for vertexes . If null , UniformEquivalenceComparator
	 * will be used as default (always return true)
	 * @param edgeChecker eq. group checker for edges . If null , UniformEquivalenceComparator
	 * will be used as default (always return true)
	 */
	public AbstractExhaustiveIsomorphismInspector(Graph graph1,Graph graph2,EquivalenceComparator vertexChecker,
			EquivalenceComparator edgeChecker)
	{ 
		this.graph1 = graph1;
		this.graph2 = graph2;
		
		if (vertexChecker!=null)
		{
			this.vertexComparator = vertexChecker;
		}
		else
		{
			this.vertexComparator = vertexDefaultIsomorphismComparator;
		}

		//	Unlike vertexes , edges can , and have better performance , when not tested for Equivalence,
		//	so if the user did not supply one , use null.
				
				
				if (edgeChecker!=null)
				{
					this.edgeComparator = edgeChecker;
				}
		//		else
		//		{
		//			this.edgeComparator = edgeDefaultIsomorphismComparator;
		//		}
		
		init();
	} 
	/**
	 * constructor. Uses the default comparators.
	 * @see ExhaustiveIsomorphismInspector(Graph,Graph,EquivalenceComparator,EquivalenceComparator)
	 */
	public AbstractExhaustiveIsomorphismInspector(Graph graph1,Graph graph2)
	{
		this(	graph1,
				graph2,
				edgeDefaultIsomorphismComparator,
				vertexDefaultIsomorphismComparator
			);
	}
	/**
	 * inits needed data-structures , among them:
	 * <li> LabelsGraph which is a created in the image of graph1
	 * <li> vertexPermuteIter which is created after the vertexes were divided to
	 * euqivalance groups. This saves order-of-magnitude in performance , because
	 * the number of possible permutations dramatically decreases.
	 * <p>for example: if the eq.group are even/odd - only two groups.
	 * A graph with consist of 10 nodes of which 5 are even , 5 are odd , will need
	 * to test 5!*5! (14,400) instead of 10! (3,628,800). 
	 * <p> besides the EquivalenceComparator`s supplied by the user , we also use
	 * predefined topological comparators. 
	 *  
	 */
	private void init()
	{
		this.nextSupplier = new PrefetchIterator(new AbstractExhaustiveIsomorphismInspector.NextFunctor());
		
		this.graph1VertexSet = new LinkedHashSet(this.graph1.vertexSet());
		

		//vertexPermuteIter will be null , if there is no match
		this.vertexPermuteIter = createPermutationIterator(this.graph1VertexSet,
															this.graph2.vertexSet());
		
		
		this.lableGraph1 = new GraphOrdering(this.graph1VertexSet,this.graph1.edgeSet());
		
		
		this.graph2EdgeSet = new LinkedHashSet(this.graph2.edgeSet());
	}
	/**
	 * create the permuatation iterator for vertexSet2 . 
	 * the subclasses may make it depending on equality groups or use vertexSet1 for it.
	 * @param vertexSet1 [i] may be reordered
	 * @param vertexSet2 [i] may not.
	 * @return
	 */
	protected abstract CollectionPermutationIter createPermutationIterator(Set vertexSet1,Set vertexset2);
	
	
	
	
	/**
	 * 1. creates a LablesGraph of graph1 which will serve as a source to all
	 * the comparecent which will follow.<p>
	 * 2. extract the edge array of graph2, it will be permanent too.<p>
	 * 3. for each permutation of the vertexes of graph2, test :<p>
	 * 	3.1. vertixes<p>
	 * 	3.2. edges (in labelsgraph)<p>
	 * 
	 * Implementation Notes and considurations:
	 * Lets see a trival example: graph of strings "A","B","C" with two edges A->B,B->C
	 * lets assume for this example that the vertexes comparator always return true , meaning String value
	 * does not matter , only the graph structure does. so "D" "E" "A" with D->E->A will be isomorphic , but
	 * "A","B,"C" with A->B,A->C will not. <p>
	 * First lets extract the important info for isomorphism from the graph. we don`t care what the vertexes
	 * are , we care that there are 3 of them with edges from first to second and from second to third.
	 * so the source LabelsGraph will be: vertexes:[1,2,3] edges:[[1->2],[2->3]]
	 * Now we will do several permutations of D,E,A . few examples:
	 * 									D->E , E->A 
	 * [1,2,3]=[A,D,E] so edges are: 	2->3 , 3->1 . does it match the source? NO.  
	 * [1,2,3]=[D,A,E] so edges are:	1->3 , 3->2 . no match either.
	 * [1,2,3]=[D,E,A] so edges are:	1->2 , 2->3 . MATCH FOUND !
	 * Trivial algorithm: We will iterate on all permutations [abc][acb][bac][bca][cab][cba]. (n! of them,3!=6)
	 * for each , first compare vertexes using the VertexComparator(always true). then see that the edges are 
	 * in the exact order 1st->2nd , 2nd->3rd . if we found a match stop and return true , otherwise return false; 
	 * We will compare by vetexes and edges by their order (1st,2nd,3rd,etc) only.
	 * 
	 * Two graphs are the same , by this order,  if:
	 * 1. for each i , sourceVertexArray[i] equequivilent to targetVertexArray[i]
	 * 2. for each vertex , the edges which start in it (it is the source) goes to the same ordered 
	 * vertex. For multiple ones , count them too.
	 *
	 * @return IsomorphismRelation for a permutation found . null if no permutation was isomorphic
	 */
	private IsomorphismRelation findNextIsomorphicGraph()
	{
		boolean result=false;
		IsomorphismRelation resultRelation=null;
		if (this.vertexPermuteIter!=null)
		{
			//System.out.println("Souce  LabelsGraph="+this.lableGraph1);
			while (this.vertexPermuteIter.hasNext())
			{
				currVertexPermutation = this.vertexPermuteIter.getNextSet();
				//comapre vertexes
				if (!areVertexSetsOfTheSameEqualityGroup(this.graph1VertexSet,currVertexPermutation))
				{
					continue;	//this one is not iso, so try the next one
				}
				//compare edges
				GraphOrdering currPermuteGraph = new GraphOrdering(currVertexPermutation,this.graph2EdgeSet);
				//System.out.println("target LablesGraph="+currPermuteGraph);
				if (this.lableGraph1.equalsByEdgeOrder(currPermuteGraph))
				{
					
					//create result object .
					resultRelation= new IsomorphismRelation(
							graph1VertexSet.toArray(),
							currVertexPermutation.toArray(),
							(Graph)graph1,
							(Graph)graph2
							);
					//if the edge comparator exist , check euqivalce by it
					boolean edgeEq = areAllEdgesEquivivalant(resultRelation,this.edgeComparator);
					if (edgeEq) //only if euqivalant
					{
						result=true;
						break;
					}
					
				}
				
			}
		}
		
		if (result==true)
		{
			return resultRelation;
		}
		else
		{
			return null;
		}
	}
	

	
	/** 
	 * will be called on every two sets of vertexes returned by the permutation 
	 * iterator. from findNextIsomorphicGraph().
	 * should make sure that the two sets are euqivalent.
	 * Subclasses may decide to implements it as an always true methods only if they
	 * make sure that the permutationIterator will always be already 
	 * euqivalent.
	 * @param vertexSet1
	 * @param vertexSet2
	 * @return
	 */
	protected abstract boolean areVertexSetsOfTheSameEqualityGroup(Set vertexSet1,Set vertexSet2);

	
	/**
	 * for each edge in g1, get the Correspondence edge and test them
	 * @param resultRelation
	 * @param edgeComparator if null , always return true.
	 * @return
	 */
	protected boolean areAllEdgesEquivivalant(IsomorphismRelation resultRelation,EquivalenceComparator edgeComparator)
	{
		boolean checkResult=true;
		
		if (edgeComparator==null)
		{
			//nothing to check
			return true;
		}
		
		
		try
		{
			Set edgeSet = this.graph1.edgeSet();
		
			for (Iterator iter = edgeSet.iterator(); iter.hasNext();) 
			{
				Edge currEdge = (Edge) iter.next();
				Edge correspondingEdge = (Edge)resultRelation.getCorrespondence(currEdge,true);
					
				//if one edge test fail , fail the whole method
				if (!edgeComparator.equivalenceCompare(	currEdge,
													correspondingEdge,
													this.graph1,
													this.graph2))
				{
					checkResult=false;
					break;
				}
			}
		}
		catch (IllegalArgumentException illegal)
		{
			checkResult=false;
		}
		
		
		
		
		return checkResult;
	}
	
	
	
	
	
	
	
	/**
	 * return nextElement() casted as IsomorphismRelation
	 */
	public IsomorphismRelation nextIsoRelation()
	{
		return (IsomorphismRelation) next();
	}
	/**
	 * Efficency: The value is known after the first check for isomorphism
	 * activated on this class and returned there after in O(1).
	 * If called on a new ("virgin") class , it activate 1 iso-check.  
	 * @return
	 */
	public boolean isIsomorphic() 
	{
		return !(this.nextSupplier.isEnumerationStartedEmpty());
	}
	
	/* (non-Javadoc)
	 * @see java.util.Enumeration#hasMoreElements()
	 */
	public boolean hasNext() {
		boolean result = this.nextSupplier.hasMoreElements();
		
		return result;
		
	}
	/**
	 * @see java.util.Iterator#next()
	 * 
	 */
	public Object next() {
		return this.nextSupplier.nextElement();
	}
	
	private class NextFunctor implements PrefetchIterator.NextElementFunctor
	{
		public Object nextElement() throws NoSuchElementException {
			IsomorphismRelation resultRelation = findNextIsomorphicGraph();
			if (resultRelation!=null)
			{
				//if it worked , even once , chage the flag to true
				wasIsomorphismFound=true;
				return resultRelation;
			}
			else 
			{
				throw new NoSuchElementException("IsomorphismInspector does not have any more elements");
			}
		}
		
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	public void remove() {
		throw new UnsupportedOperationException("remove() method is not supported in AdaptiveIsomorphismInspectorFactory."
				+ " There is no meaning to removing an isomorphism result.");
		

	}

	
	
	
	
	
	

	
}
	
	
