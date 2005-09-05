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
 * EquivalenceComparatorChain.java
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

/**
 * A container of comparators , which are tested in a chain until the first 
 * result can be supplied. It implements the EquivalenceComparator and can be
 * and it adds EquivalenceComparator , so containers can include other containers.
 * the first check will use the current
 * comparator and not the new one. 
 * so ,Make sure to use the one which has better performance first.
 * 
 * (according to "Composite" design-pattern)
 *	@author Assaf
 *	@since	Jul 22, 2005
 *
 */
public interface EquivalenceComparatorChain extends EquivalenceComparator {
	
	/** Adds a comparator which will also test equivalence.
	 * equivalenceCompare() - The return value is a logical
	 * AND of the two comparators.the first check will use the current
	 * comparator before the new one. 
	 * Make sure to use the one which has better performance first.
	 * equivalenceHashcode() - The resulting hashes will be rehashed together.
	 * 
	 * This method may be used multiple times to create a long "chain" of
	 * comparators. 
	 */
	public void addComparatorAfter(EquivalenceComparator comparatorAfter);
}
