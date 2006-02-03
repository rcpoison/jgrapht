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
 * EquivalenceGroupCreatorTest.java
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

import java.util.Arrays;

import junit.framework.*;

import org._3pq.jgrapht.alg.isomorphism.comparators.*;


/**
 * @author Assaf
 * @since Jul 22, 2005
 */
public class EquivalenceGroupCreatorTest extends TestCase
{

    //~ Instance fields -------------------------------------------------------

    // create the groups array as 0 to X (it)
    final int INTEGER_ARRAY_SIZE = 25;

    //~ Methods ---------------------------------------------------------------

    /*
     * @see TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        super.setUp();
    }

    
    public void testUniformGroup()
    {
    	//expecting two seperate groups , one with odd , one with even nubmers"
        testOneComparator(new UniformEquivalenceComparator(),    1      );
        //" expecting 3 seperate groups , one for each mod3
        testOneComparator(new org._3pq.jgrapht.alg.isomorphism.comparators.Mod3GroupComparator(),
            3) ;
    }
    
    public void testOddEvenGroup()
    {
        //" expecting two seperate groups , one with odd , one with even nubmers");
        testOneComparator(new org._3pq.jgrapht.alg.isomorphism.comparators.OddEvenGroupComparator(),
            2);
        //  " expecting 3 seperate groups , one for each mod3");  
        testOneComparator(new org._3pq.jgrapht.alg.isomorphism.comparators.Mod3GroupComparator(),
            3);
          
    }

    /**
     * Using a chain of evenOdd(mod2) and mod3 comparator , should yield the 6
     * groups , which are infact mod6 , examples:
     * <li>mod2 = 0 , mod3 = 0  --> mod6=0 , like : 6 , 12
     * <li>mod2 = 1 , mod3 = 0  --> mod6=1 , like : 7 , 13
     * <li>
     */
    public void testComparatorChain()
    {
        EquivalenceComparatorChain comparatorChain =
            new EquivalenceComparatorChainBase(new OddEvenGroupComparator());
        comparatorChain.appendComparator(new Mod3GroupComparator());

        // for (int i=0 ; i<INTEGER_ARRAY_SIZE ; i++)
        // {
        // System.out.println("hash of "+i+" =
        // "+comparatorChain.equivalenceHashcode(integerArray[i], null));
        //
        //
        // }
        // expecting six seperate groups , with the different mod6 values");
        testOneComparator(
            comparatorChain,
            6);
            
    }
    
    public void testComparatorChainSameComparatorTwice()
    {
        EquivalenceComparatorChain comparatorChain =
            new EquivalenceComparatorChainBase(new OddEvenGroupComparator());
        comparatorChain.appendComparator(new UniformEquivalenceComparator());
        comparatorChain.appendComparator(new OddEvenGroupComparator());

        // still expecting 2 groups "
         testOneComparator(
            comparatorChain,
            2);
            
    }

    private void testOneComparator(
        EquivalenceComparator comparator,
        int expectedNumOfGroups)
   
    {
        Integer [] integerArray = new Integer [INTEGER_ARRAY_SIZE];
        for (int i = 0; i < INTEGER_ARRAY_SIZE; i++) {
            integerArray[i] = Integer.valueOf(i);
        }

        EquivalenceSet [] eqGroupArray =
            EquivalenceSetCreator.createEqualityGroupOrderedArray(
                integerArray,
                comparator,
                null);
        assertEquals(expectedNumOfGroups, eqGroupArray.length);
        //assert the group order size is sorted.
        for (int i = 1; i < eqGroupArray.length; i++) {
			EquivalenceSet set = eqGroupArray[i];
			assertTrue(eqGroupArray[i].size() >= eqGroupArray[i-1].size());
		}
        // System.out.println("\nTesting the EquivalenceSet[] returned from
        // Integer["
        // +INTEGER_ARRAY_SIZE+"] filled with the integers as the indexes. \n"
        // + expectedResult);
        // System.out.println("result size="+eqGroupArray.length);
        // for (int i = 0; i < eqGroupArray.length; i++) {
        // System.out.println(eqGroupArray[i]);
        // }
    }
}
