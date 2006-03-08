/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2004, by Barak Naveh and Contributors.
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
/* -----------------------------
 * ClosestFirstIteratorTest.java
 * -----------------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id: ClosestFirstIteratorTest.java,v 1.4 2005/05/30 05:37:29 perfecthash Exp
 * $
 *
 * Changes
 * -------
 * 03-Sep-2003 : Initial revision (JVS);
 * 29-May-2005 : Test radius support (JVS);
 *
 */
package org._3pq.jgrapht.traverse;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.edge.DirectedEdge;


/**
 * Tests for ClosestFirstIterator.
 *
 * @author John V. Sichi
 * @since Sep 3, 2003
 */
public class ClosestFirstIteratorTest extends AbstractGraphIteratorTest
{

    //~ Methods ---------------------------------------------------------------

    /**
     * .
     */
    public void testRadius()
    {
        m_result = new StringBuffer();

        DirectedGraph<String, DirectedEdge<String>> graph = createDirectedGraph();

        // NOTE:  pick 301 as the radius because it discriminates
        // the boundary case edge between v7 and v9
        AbstractGraphIterator<String, ?> iterator =
            new ClosestFirstIterator<String, DirectedEdge<String>>(graph, "1", 301);

        while (iterator.hasNext()) {
            m_result.append(iterator.next());

            if (iterator.hasNext()) {
                m_result.append(',');
            }
        }

        assertEquals("1,2,3,5,6,7", m_result.toString());
    }

    // NOTE:  the edge weights make the result deterministic
    String getExpectedStr1()
    {
        return "1,2,3,5,6,7,9,4,8";
    }

    String getExpectedStr2()
    {
        return getExpectedStr1() + ",orphan";
    }

    AbstractGraphIterator<String, DirectedEdge<String>> createIterator(DirectedGraph<String, DirectedEdge<String>> g, String vertex)
    {
        AbstractGraphIterator<String, DirectedEdge<String>> i = new ClosestFirstIterator<String, DirectedEdge<String>>(g, vertex);
        i.setCrossComponentTraversal(true);

        return i;
    }
}
