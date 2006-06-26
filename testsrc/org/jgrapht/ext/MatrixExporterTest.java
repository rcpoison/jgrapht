/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
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
/* ------------------------------
 * MatrixExporterTest.java
 * ------------------------------
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
 *
 * Original Author:  Charles Fry
 *
 * $Id$
 *
 * Changes
 * -------
 * 12-Dec-2005 : Initial revision (CF);
 *
 */
package org.jgrapht.ext;

import java.io.*;
import java.util.*;

import junit.framework.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;


/**
 * .
 *
 * @author Charles Fry
 */
public class MatrixExporterTest extends TestCase
{

    //~ Static fields/initializers --------------------------------------------

    private static final String V1 = new String("v1");
    private static final String V2 = new String("v2");
    private static final String V3 = new String("v3");
    private static final String V4 = new String("v4");

    private static final String LAPLACIAN =
        "1 1 2\n" +
        "1 2 -1\n" +
        "1 3 -1\n" +
        "2 2 1\n" +
        "2 1 -1\n" +
        "3 3 1\n" +
        "3 1 -1\n";

    private static final String NORMALIZED_LAPLACIAN =
        "1 1 1\n" +
        "1 2 -0.7071067811865475\n" +
        "1 3 -0.7071067811865475\n" +
        "2 2 1\n" +
        "2 1 -0.7071067811865475\n" +
        "3 3 1\n" +
        "3 1 -0.7071067811865475\n";

    private static final String UNDIRECTED_ADJACENCY =
        "1 2 1\n" +
        "1 3 1\n" +
        "1 1 2\n" +
        "2 1 1\n" +
        "3 1 1\n";

    private static final String DIRECTED_ADJACENCY =
        "1 2 1\n" +
        "3 1 2\n";

    private static final MatrixExporter exporter = new MatrixExporter();

    //~ Methods ---------------------------------------------------------------

    public void testLaplacian()
    {
        UndirectedGraph g = new SimpleGraph(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addEdge(V1, V2);
        g.addVertex(V3);
        g.addEdge(V3, V1);

        StringWriter w = new StringWriter();
        exporter.exportLaplacianMatrix(w, g);
        assertEquals(LAPLACIAN, w.toString());

        w = new StringWriter();
        exporter.exportNormalizedLaplacianMatrix(w, g);
        assertEquals(NORMALIZED_LAPLACIAN, w.toString());
    }

    public void testAdjacencyUndirected()
    {
        UndirectedGraph g = new Pseudograph(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addEdge(V1, V2);
        g.addVertex(V3);
        g.addEdge(V3, V1);
        g.addEdge(V1, V1);

        StringWriter w = new StringWriter();
        exporter.exportAdjacencyMatrix(w, g);
        assertEquals(UNDIRECTED_ADJACENCY, w.toString());

    }

    public void testAdjacencyDirected()
    {
        DirectedGraph g = new DirectedMultigraph(DefaultEdge.class);
        g.addVertex(V1);
        g.addVertex(V2);
        g.addEdge(V1, V2);
        g.addVertex(V3);
        g.addEdge(V3, V1);
        g.addEdge(V3, V1);

        Writer w = new StringWriter();
        exporter.exportAdjacencyMatrix(w, g);
        assertEquals(DIRECTED_ADJACENCY, w.toString());
    }

}
