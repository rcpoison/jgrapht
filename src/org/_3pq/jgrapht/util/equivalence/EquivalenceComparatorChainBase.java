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
 * EquivalenceComparatorChainBase.java
 * -----------------
 * (C) Copyright 2005, by Barak Naveh and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.util.equivalence;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 *  This class implements comparator chaining. 
 * <p> Usage examples: 
 * <li> <i> graph-theory , node equivalence:</i> You can create a comparator for
 * the inDegree of a node , another for the total weight of outDegree edges , and
 * a third which checks the business content of the node.  
 * You know that the first topological comparators has dozens of different groups , 
 * but the buisness comparator has only two , and they are hard to check . The
 * best performance will be gained by:
 * <blockquote><code>
 * <p>EquivalenceComparatorChainBase eqChain = new EquivalenceComparatorChainBase(fastNodesDegreeComparator);
 * <p>eqChain.addAfter(ABitSlowerEdgeWeightComparator);
 * <p>eqChain.addAfter(slowestBuisnessContentsComparator);
 * </code></blockquote>
 * 
 * 
 * 
 *  @author Assaf
 *	@since	Jul 22, 2005
 *
 */
public class EquivalenceComparatorChainBase implements
		EquivalenceComparatorChain {
	
	private List chain;
	
	/**
	 * 
	 */
	public EquivalenceComparatorChainBase(EquivalenceComparator firstComaparator) {
		this.chain = new LinkedList();
		this.chain.add(firstComaparator);
		
	}

	/* (non-Javadoc)
	 * @see org._3pq.jgrapht.util.equivalence.EquivalenceComparatorChain#addComparatorAfter(org._3pq.jgrapht.util.equivalence.EquivalenceComparator)
	 */
	public void addComparatorAfter(EquivalenceComparator comparatorAfter) {
		if (comparatorAfter!=null)
		{
			this.chain.add(comparatorAfter);
		}
	}
	
	

	/** 
	 * implements logical AND between the comparators results.  
	 * iterate through the comparators chain until one of them return false.
	 * If none returns it , return true.
	 * @see org._3pq.jgrapht.util.equivalence.EquivalenceComparator#equivalenceCompare(java.lang.Object, java.lang.Object, Object, Object)
	 */
	public boolean equivalenceCompare(Object arg1, Object arg2, Object context1, Object context2) {
		for (ListIterator iter=this.chain.listIterator();iter.hasNext();)
		{
			EquivalenceComparator currentComparator = (EquivalenceComparator)iter.next();
			if (!currentComparator.equivalenceCompare(arg1,arg2, context1, context2))
			{
				return false;
			}
		}
		return true;
	}

	/** 
	 * rehases the concatenation of the results of all single hashcodes.
	 * @see org._3pq.jgrapht.util.equivalence.EquivalenceComparator#equivalenceHashcode(java.lang.Object, Object)
	 */
	public int equivalenceHashcode(Object arg1, Object context) {
		StringBuffer hashStringBuffer = new StringBuffer();
		for (ListIterator iter=this.chain.listIterator();iter.hasNext(); )
		{
			EquivalenceComparator currentComparator = (EquivalenceComparator)iter.next();
			int currentHashCode = currentComparator.equivalenceHashcode(arg1, context);
			hashStringBuffer.append(currentHashCode);
			//add a delimeter only if needed for next
			if (iter.hasNext())
			{
				hashStringBuffer.append('+');
			}
		}
		return hashStringBuffer.toString().hashCode();	
	}

}
