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
 * EquivalenceSet.java
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

import java.util.HashSet;
import java.util.Set;


/**
 * Equivalence Set is a Set of elements which have been determined to be 
 * equivalent using EquivalenceComparator.
 * The class makes sure the set size will be one or more.
 * <li>The group can only be created using the factory method createGroupWithElement().
 * <li>The equals and hashcode of a group uses the EquivalenceComparator on one of the
 * group members , thus it is actually checking whether the "other" is on the same
 * group.
 *	@author Assaf
 *	@since	Jul 21, 2005
 *
 */
public class EquivalenceSet 
{
	/** The comparator used to define the group */ 
	protected EquivalenceComparator eqComparator;
	protected Object comparatorContext;
	
	/** contains the current elements of the group */
	protected Set elementsSet;
	
		
	
	/** 
	 * Private constructor . An empty group cannot be created
	 * A group does not have meaning without an element , because the equal and
	 * hashcode methods cannot work.
	 */
	private EquivalenceSet() {}
	
	/** 
	 * constructs a new EquivalenceSet , filled with the
	 * aElement parameter and a reference to the comparator which is used
	 */
	public EquivalenceSet(Object aElement,EquivalenceComparator aEqComparator,Object aComparatorContext)
	{
		
		this.eqComparator = aEqComparator;
		this.comparatorContext = aComparatorContext;
		
		this.elementsSet = new HashSet();
		this.elementsSet.add(aElement);
	}
	/** return an arbitrary object from the group. There is no gurantee to which will
	 * be returned , and if it will be returned on the next call.
	 * @return
	 */
	public Object getRepresentative()
	{
		return elementsSet.iterator().next();
	}
	public Object getContext()
	{
		return this.comparatorContext;
	}
	public int size()
	{
		return elementsSet.size();
	}
	
	/** add an element to the group.
	 * It does not check it for equivalance . You must make sure it does , using
	 * the equals().
	 *
	 */
	public void add(Object element)
	{
		this.elementsSet.add(element);
	}
	
	public boolean equivalentTo(Object aOther,Object aOtherContext) {
		boolean result =this.eqComparator.equivalenceCompare(
				this.getRepresentative(),aOther, this.comparatorContext, aOtherContext);
		return result;
		
	}
	
	/**
	 * Use the equivalenceCompare() of the comparator to compare
	 * a representation of this group , taken using this.getRepresentative()
	 * and a representation of the other object , which may be the object 
	 * itself , or , if it is an equivalence group too , other.getRepresentative()
	 *  
	 */
	public boolean equals(Object other) {
		Object otherRepresentative= null; 
		Object otherContext=null;
		if (other instanceof EquivalenceSet)
		{
			otherRepresentative = ((EquivalenceSet) other).getRepresentative();
			otherContext = ((EquivalenceSet) other).getContext();
		}
		else
		{
			throw new ClassCastException("can check equal() only of EqualityGroup");
		}
		
		boolean result =this.eqComparator.equivalenceCompare(
				this.getRepresentative(),otherRepresentative, this.comparatorContext, otherContext);
		return result;
	}
	
	/** uses a representative to calculate the group hashcode 
	 * using equivalenceHashcode().
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result=this.eqComparator.equivalenceHashcode(this.getRepresentative(), this.comparatorContext);
		return  result;
	}

	
	public String toString() {
		return "Eq.Group="+this.elementsSet.toString();
	}
	/** 
	 * returns the elements of the group .
	 * The order of the elements in the returned array is not guranteed. In other
	 * words , two calls to the same object may return different order.
	 * @return
	 */
	public Object[] toArray()
	{
		return this.elementsSet.toArray();
	}
	
}
