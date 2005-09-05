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
 * AllIsomorphismTests.java
 * -----------------
 * (C) Copyright 2005, by Assaf Lehr and Contributors.
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org._3pq.jgrapht.alg.isomorphism;

import junit.framework.*;

import org._3pq.jgrapht.generate.*;
import org._3pq.jgrapht.util.equivalence.*;
import org._3pq.jgrapht.util.permutation.*;


/**
 * @author Assaf
 * @since May 27, 2005
 */
public class AllIsomorphismTests
{

    //~ Methods ---------------------------------------------------------------

    public static void main(String [] args)
    {
        junit.textui.TestRunner.run(AllIsomorphismTests.suite());
    }

    public static Test suite()
    {
        TestSuite suite =
            new TestSuite(
                "Test for org._3pq.jgrapht.alg.isomorphism.tests");

        // $JUnit-BEGIN$
        suite.addTestSuite(IsomorphismInspectorTest.class);
        suite.addTestSuite(CompoundPermutationIterTest.class);
        suite.addTestSuite(RandomGraphGeneratorTest.class);

        suite.addTestSuite(EquivalenceGroupCreatorTest.class);

        // $JUnit-END$
        return suite;
    }
}
