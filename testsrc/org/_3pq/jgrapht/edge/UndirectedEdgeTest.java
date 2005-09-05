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
/* -----------------------
 * UndirectedEdgeTest.java
 * -----------------------
 * (C) Copyright 2003-2004, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 10-Aug-2003 : General edge refactoring (BN);
 *
 */
package org._3pq.jgrapht.edge;

import org._3pq.jgrapht.*;


/**
 * Tests for the {@link UndirectedEdge} class.
 *
 * @author Barak Naveh
 * @since Jul 24, 2003
 */
public class UndirectedEdgeTest extends DefaultEdgeTest
{

    //~ Methods ---------------------------------------------------------------

    /**
     * .
     */
    public void testToString()
    {
        assertTrue("{s1,t1}".equals(m_e1.toString()));
        assertTrue("{s1,t1}".equals(m_e1Clone.toString()));
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp()
        throws Exception
    {
        m_e1 = new UndirectedEdge(m_source1, m_target1);
        m_e3 = new UndirectedEdge(new Object(), new Object());

        m_e1Clone = (Edge) m_e1.clone();
    }
}
