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
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -----------------
 * CollectionPermutationIter.java
 * -----------------
 * (C) Copyright 2005, by Assaf Lehr and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.util.permutation;

import java.util.*;

import org._3pq.jgrapht.util.*;


/**
 * Given a container with elements (Collection,Enumeration,array) defines a
 * permutation iterator which returns, on each iteration, a differnt
 * permutation of the source container. You may choose a different container
 * type(Collection/Array/etc) for each next call. It will continue as if they
 * were the same iterator.
 *
 * @author Assaf
 * @since May 20, 2005
 */
// TODO hb 280106 Convert internals to generics
public class CollectionPermutationIter<E>
{

    //~ Instance fields -------------------------------------------------------

    private ArrayPermutationsIter permOrder;
    private Object [] sourceArray;

    /**
     * change everry calculation.can be retrieved publicly
     */
    private int [] currPermutationArray;

    //~ Constructors ----------------------------------------------------------

    /**
     * Note: the Set interface does not guarantee iteration order. This method
     * iterates on the set to get the initial order and after that the data
     * will be saved internally in another (ordered) container. So, remeber
     * that the Initial order can be different from the objectSet.toString()
     * method. If you want it to be the same, use a LinkedHashSet , or use the
     * array constructor.
     *
     * @param objectsSet
     */
    public CollectionPermutationIter(Set objectsSet)
    {
        this(objectsSet.toArray());
    }

    public CollectionPermutationIter(Object [] objectsArray)
    {
        this.permOrder = new IntegerPermutationIter(objectsArray.length);
        this.sourceArray = new Object [objectsArray.length];
        System.arraycopy(
            objectsArray,
            0,
            this.sourceArray,
            0,
            objectsArray.length);
    }

    /**
     * Uses a permArray like [1,1,1,2] where some of the permutations are not
     * relevant. Here there will be 4 permutations (only the '2' position is
     * important)
     *
     * @param objectsArray
     * @param permuter
     */
    public CollectionPermutationIter(
        Object [] objectsArray,
        ArrayPermutationsIter permuter)
    {
        this.permOrder = permuter;
        this.sourceArray = new Object [objectsArray.length];
        System.arraycopy(
            objectsArray,
            0,
            this.sourceArray,
            0,
            objectsArray.length);
    }

    //~ Methods ---------------------------------------------------------------

    public boolean hasNext()
    {
        return this.permOrder.hasNextPermutaions();
    }

    /**
     * On first call, returns the source as an array; on any other call
     * thereafter, a new permutation
     *
     * @return null if we overflowed! the array otherwise
     */
    public Object [] getNextArray()
    {
        Object [] permutationResult; // will hold the array result
        if (this.permOrder.hasNextPermutaions()) {
            this.currPermutationArray = this.permOrder.nextPermutation();
            permutationResult = new Object [this.sourceArray.length];

            // Example : this.sourceArray = ["A","B","C","D"]
            // perOrder:                  = [ 1 , 0 , 3 , 2 ]
            // result  :                  = ["B","A","D","C"]
            for (int i = 0; i < permutationResult.length; i++) {
                permutationResult[i] =
                    this.sourceArray[this.currPermutationArray[i]];
            }
        } else {
            return permutationResult = null;
        }

        return permutationResult;
    }

    /**
     * Wrap result to a Set.
     *
     * @return null if we overflowed! the set otherwise
     */
    public Set<E> getNextSet()
    {
        Object [] result = getNextArray();
        if (result == null) {
            return null;
        } else // wrap in a SET
        {
            Set<E> resultSet = new LinkedHashSet(Arrays.asList(result));
            return resultSet;
        }
    }

    public int [] getCurrentPermutationArray()
    {
        return this.currPermutationArray;
    }

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("Permutation int[]=");
        sb.append(ArrayUtil.toString(getCurrentPermutationArray()));

        Object [] permutationResult = new Object [this.sourceArray.length];

        // Example : this.sourceArray = ["A","B","C","D"]
        // perOrder:                  = [ 1 , 0 , 3 , 2 ]
        // result  :                  = ["B","A","D","C"]
        for (int i = 0; i < permutationResult.length; i++) {
            permutationResult[i] =
                this.sourceArray[this.currPermutationArray[i]];
        }
        sb.append("\nPermutationSet Source Object[]=");
        sb.append(ArrayUtil.toString(this.sourceArray));
        sb.append("\nPermutationSet Result Object[]=");
        sb.append(ArrayUtil.toString(permutationResult));
        return sb.toString();
    }
}
