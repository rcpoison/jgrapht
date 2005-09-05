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
 * EquivalenceSetCreator.java
 * -----------------
 * (C) Copyright 2005, by Assaf Lehr and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.util.equivalence;

import java.util.*;


/**
 * @author Assaf
 * @since Jul 21, 2005
 */
public class EquivalenceSetCreator
{

    //~ Static fields/initializers --------------------------------------------

    private static final EqGroupSizeComparator groupSizeComparator =
        new EqGroupSizeComparator();

    //~ Methods ---------------------------------------------------------------

    /**
     * Checks for equivalance groups in the aElementsArray. Returns an ordered
     * array of them, where the smallest one is the first in the array.
     *
     * @param aElementsArray
     * @param aEqComparator
     */
    public static EquivalenceSet [] createEqualityGroupOrderedArray(
        Object [] aElementsArray,
        EquivalenceComparator aEqComparator,
        Object aContext)
    {
        ArrayList arrayList = new ArrayList();

        HashMap map =
            createEqualityGroupMap(aElementsArray, aEqComparator, aContext);
        // each of the map value is a list with one or more groups in it.
        // Object[] array = map.values().toArray();
        // for (int i = 0; i < array.length; i++)
        // {
        // List list = (List)array[i];

        for (Iterator collectionIter = map.values().iterator();
            collectionIter.hasNext();) {
            List list = (List) collectionIter.next();
            for (Iterator listIter = list.iterator(); listIter.hasNext();) {
                arrayList.add(listIter.next());
            }
        }

        // now we got all the eq. groups  in an array list. we need to sort
        // them
        EquivalenceSet [] resultArray = new EquivalenceSet [arrayList.size()];
        arrayList.toArray(resultArray);
        Arrays.sort(resultArray, groupSizeComparator);
        return resultArray;
    }

    /**
     * The data structure we use to store groups is a map, where the key is
     * eqGroupHashCode, and the value is list, containing one or more eqGroup
     * which match this hash.
     *
     * @param aElementsArray
     * @param aEqComparator
     *
     * @return a hashmap with key=group hashcode , value = list of eq.groups
     *         which match that hash.
     */
    private static HashMap createEqualityGroupMap(
        Object [] aElementsArray,
        EquivalenceComparator aEqComparator,
        Object aComparatorContext)
    {
        HashMap equalityGroupMap = new HashMap(aElementsArray.length);

        for (int i = 0; i < aElementsArray.length; i++) {
            int hashcode =
                aEqComparator.equivalenceHashcode(
                    aElementsArray[i],
                    aComparatorContext);
            Object mapValue = equalityGroupMap.get(Integer.valueOf(hashcode));

            // determine the type of value. It can be null(no value yet) ,
            // EquivalenceSet(there
            // is one already with that hash) , or a list of EquivalenceSet
            if (mapValue == null) {
                // create list with one elemnt in it
                List list = new LinkedList();
                list.add(new EquivalenceSet(
                        aElementsArray[i],
                        aEqComparator,
                        aComparatorContext));

                // This is the first one .add it to the map , in an eqGroup
                equalityGroupMap.put(Integer.valueOf(hashcode), list);
            } else if (mapValue instanceof List) {
                List list = (List) mapValue;
                boolean eqWasFound = false;

                // we need to check the groups in the list. If none are good,
                // create a new one
                for (Iterator iter = list.iterator(); iter.hasNext();) {
                    EquivalenceSet eqGroup = (EquivalenceSet) iter.next();
                    if (eqGroup.equivalentTo(
                            aElementsArray[i],
                            aComparatorContext)) {
                        // add it to the list and break
                        eqGroup.add(aElementsArray[i]);
                        eqWasFound = true;
                        break;
                    }
                }

                // if no match was found add it to the list as a new group
                if (!eqWasFound) {
                    list.add(new EquivalenceSet(
                            aElementsArray[i],
                            aEqComparator,
                            aComparatorContext));
                }
            } else {
                throw new RuntimeException(
                    "The equalityGroupMap value may only be null or List");
            }
        }

        return equalityGroupMap;
    }

    //~ Inner Classes ---------------------------------------------------------

    /**
     * Functor used to order groups by size (number of elements in the group)
     * from the smallest to the biggest. If they have the same size, uses the
     * hashcode of the group to compare from the smallest to the biggest. Note
     * that it is inconsistent with equals(). See Object.equals() javadoc.
     *
     * @author Assaf
     * @since Jul 22, 2005
     */
    private static class EqGroupSizeComparator implements Comparator
    {
        /**
         * compare by size , then (if size equal) by hashcode
         *
         * @see java.util.Comparator#compare(java.lang.Object,
         *      java.lang.Object)
         */
        public int compare(java.lang.Object arg1, java.lang.Object arg2)
        {
            int eqGroupSize1 = ((EquivalenceSet) arg1).size();
            int eqGroupSize2 = ((EquivalenceSet) arg2).size();
            if (eqGroupSize1 > eqGroupSize2) {
                return 1;
            } else if (eqGroupSize1 < eqGroupSize2) {
                return -1;
            } else { // size equal , compare hashcodes
                int eqGroupHash1 = ((EquivalenceSet) arg1).hashCode();
                int eqGroupHash2 = ((EquivalenceSet) arg2).hashCode();
                if (eqGroupHash1 > eqGroupHash2) {
                    return 1;
                } else if (eqGroupHash1 < eqGroupHash2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }
}
