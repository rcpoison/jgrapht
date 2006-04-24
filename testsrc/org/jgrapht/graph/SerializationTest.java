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
/* --------------
 * CloneTest.java
 * --------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 06-Oct-2003 : Initial revision (JVS);
 *
 */
package org.jgrapht.graph;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jgrapht.DirEdge;
import org.jgrapht.EnhancedTestCase;


/**
 * SerializationTest tests serialization and deserialization of JGraphT
 * objects.
 *
 * @author John V. Sichi
 * @version $Id$
 */
public class SerializationTest extends EnhancedTestCase
{

    //~ Instance fields -------------------------------------------------------

    private String m_v1 = "v1";
    private String m_v2 = "v2";
    private String m_v3 = "v3";

    //~ Constructors ----------------------------------------------------------

    /**
     * @see junit.framework.TestCase#TestCase(java.lang.String)
     */
    public SerializationTest(String name)
    {
        super(name);
    }

    //~ Methods ---------------------------------------------------------------

    /**
     * Tests serialization of DirectedMultigraph.
     */
    @SuppressWarnings("unchecked")
    public void testDirectedMultigraph()
        throws Exception
    {
        DirectedMultigraph<String,DirEdge<String>> graph =
            new DirectedMultigraph<String,DirEdge<String>>();
        graph.addVertex(m_v1);
        graph.addVertex(m_v2);
        graph.addVertex(m_v3);
        graph.addEdge(m_v1, m_v2);
        graph.addEdge(m_v2, m_v3);
        graph.addEdge(m_v2, m_v3);

        graph = (DirectedMultigraph<String,DirEdge<String>>) serializeAndDeserialize(graph);
        assertTrue(graph.containsVertex(m_v1));
        assertTrue(graph.containsVertex(m_v2));
        assertTrue(graph.containsVertex(m_v3));
        assertTrue(graph.containsEdge(m_v1, m_v2));
        assertTrue(graph.containsEdge(m_v2, m_v3));
        assertEquals(1, graph.edgesOf(m_v1).size());
        assertEquals(3, graph.edgesOf(m_v2).size());
        assertEquals(2, graph.edgesOf(m_v3).size());
    }

    private Object serializeAndDeserialize(Object obj)
        throws Exception
    {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bout);

        out.writeObject(obj);
        out.flush();

        ByteArrayInputStream bin =
            new ByteArrayInputStream(bout.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bin);

        obj = in.readObject();
        return obj;
    }
}

// End SerializationTest.java
