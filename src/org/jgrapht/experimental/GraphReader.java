/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
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
/* -------------------
 * GraphReader.java
 * -------------------
 * (C) Copyright 2003-2008, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id: GraphReader.java 504 2006-07-03 02:37:26Z perfecthash $
 *
 * Changes
 * -------
 * 16-Sep-2003 : Initial revision (BN);
 *
 */
package org.jgrapht.experimental;

import java.io.*;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;


public class GraphReader<V, E>
    implements GraphGenerator<V, E, V>
{
    //~ Instance fields --------------------------------------------------------

    // ~ Static fields/initializers --------------------------------------------

    // ~ Instance fields -------------------------------------------------------

    private final BufferedReader _in;

    // ~ Constructors ----------------------------------------------------------

    //~ Constructors -----------------------------------------------------------

    /**
     * Construct a new GraphReader.
     */
    public GraphReader(String file)
        throws IOException
    {
        _in = new BufferedReader(new FileReader(file));
    }

    //~ Methods ----------------------------------------------------------------

    // ~ Methods ---------------------------------------------------------------

    private List<String> split(String src)
    {
        final List<String> l = new ArrayList<String>();
        final StringTokenizer tok = new StringTokenizer(src);
        while (tok.hasMoreTokens()) {
            l.add(tok.nextToken());
        }
        return l;
    }

    private List<String> skipComments()
    {
        try {
            if (_in.ready()) {
                List<String> cols = split(_in.readLine());
                while (
                    cols.isEmpty()
                    || cols.get(0).equals("c")
                    || cols.get(0).startsWith("%"))
                {
                    cols = split(_in.readLine());
                }
                return cols;
            }
        } catch (IOException e) {
        }
        return null;
    }

    private int readNodeCount()
    {
        List<String> cols = skipComments();
        if (cols.get(0).equals("p")) {
            return Integer.parseInt(cols.get(1));
        }
        return -1;
    }

    /**
     * {@inheritDoc}
     */
    public void generateGraph(
        Graph<V, E> target,
        VertexFactory<V> vertexFactory,
        Map<String, V> resultMap)
    {
        final int size = readNodeCount();
        if (resultMap == null) {
            resultMap = new HashMap<String, V>();
        }

        for (int i = 0; i < size; i++) {
            V newVertex = vertexFactory.createVertex();
            target.addVertex(newVertex);
            resultMap.put(Integer.toString(i + 1), newVertex);
        }
        List<String> cols = skipComments();
        while (cols != null) {
            if (cols.get(0).equals("e")) {
                target.addEdge(
                    resultMap.get(cols.get(1)),
                    resultMap.get(cols.get(2)));
            }
            cols = skipComments();
        }
    }

    public static void main(String [] args)
        throws Exception
    {
        GraphReader<Integer, DefaultEdge> reader =
            new GraphReader<Integer, DefaultEdge>(
                args[0]);
        Graph<Integer, DefaultEdge> g =
            new SimpleGraph<Integer, DefaultEdge>(
                DefaultEdge.class);
        VertexFactory<Integer> vf = new IntVertexFactory();
        reader.generateGraph(g, vf, null);
        System.out.println(g);
    }

    //~ Inner Classes ----------------------------------------------------------

    private static final class IntVertexFactory
        implements VertexFactory<Integer>
    {
        int last = 0;

        public Integer createVertex()
        {
            return last++;
        }
    }
}

// End GraphReader.java
