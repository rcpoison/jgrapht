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
/* ------------------
 * MatrixExporter.java
 * ------------------
 * (C) Copyright 2005, by Charles Fry
 *
 * Original Author:  Charles Fry
 *
 * $Id$
 *
 * Changes
 * -------
 * 13-Dec-2005 : Initial Version (CF);
 *
 */
package org._3pq.jgrapht.ext;

import java.io.*;

import java.util.*;

import org._3pq.jgrapht.*;


/**
 * Exports a graph to a plain text matrix format, which can be processed
 * by matrix manipulation software, such as
 * <a href="http://www.mathworks.com/products/matlab/">MATLAB</a>.
 *
 * @author Charles Fry
 */
public class MatrixExporter
{

    //~ Instance fields -------------------------------------------------------

    private String m_delimiter = " ";
    private String m_prefix = "";
    private String m_suffix = "";

    //~ Constructors ----------------------------------------------------------

    /**
     * Creates a new MatrixExporter object.
     */
    public MatrixExporter()
    {
    }

    //~ Methods ---------------------------------------------------------------

    private void println(PrintWriter out, String fromName, String toName, String value)
    {
        out.println(m_prefix + fromName + m_suffix + m_delimiter
                + m_prefix + toName + m_suffix + m_delimiter
                + m_prefix + value + m_suffix);
    }

    /**
     * Exports the specified graph into a plain text file format containing
     * a sparse representation of the graph's adjacency matrix. The value
     * stored in each position of the matrix indicates the number of
     * edges between two vertices. With an undirected graph, the adjacency
     * matrix is symetric.
     *
     * @param output the writer to which the graph to be exported.
     * @param g the graph to be exported.
     */
    public void exportAdjacencyMatrix(Writer output, UndirectedGraph  g)
    {
        PrintWriter out = new PrintWriter(output);

        VertexNameProvider nameProvider = new IntegerNameProvider();
        for (Object from : g.vertexSet()) {
            // assign ids in vertex set iteration order
            nameProvider.getVertexName(from);
        }

        for (Object from : g.vertexSet()) {
            // TODO modify GraphHelper to return neighbor sets
            exportAdjacencyMatrixVertex(out, nameProvider, from,
                    GraphHelper.neighborListOf(g, from));
        }

        out.flush();
    }

    /**
     * Exports the specified graph into a plain text file format containing
     * a sparse representation of the graph's adjacency matrix. The value
     * stored in each position of the matrix indicates the number of
     * directed edges going from one vertex to another.
     *
     * @param output the writer to which the graph to be exported.
     * @param g the graph to be exported.
     */
    public void exportAdjacencyMatrix(Writer output, DirectedGraph  g)
    {
        PrintWriter out = new PrintWriter(output);

        VertexNameProvider nameProvider = new IntegerNameProvider();
        for (Object from : g.vertexSet()) {
            // assign ids in vertex set iteration order
            nameProvider.getVertexName(from);
        }

        for (Object from : g.vertexSet()) {
            // TODO modify GraphHelper to return neighbor sets
            exportAdjacencyMatrixVertex(out, nameProvider, from,
                    GraphHelper.successorListOf(g, from));
        }

        out.flush();
    }

    private void exportAdjacencyMatrixVertex(PrintWriter out,
            VertexNameProvider nameProvider, Object from, List neighbors)
    {
        String fromName = nameProvider.getVertexName(from);
        Map<String,Integer> counts = new LinkedHashMap<String,Integer>();
        for (Object to : neighbors) {
            String toName = nameProvider.getVertexName(to);
            Integer count = counts.get(toName);
            if (count == null) {
                count = 0;
            }
            // count loops twice
            count += from.equals(to) ? 2 : 1;
            counts.put(toName, count);
        }
        for (Map.Entry<String,Integer> entry : counts.entrySet()) {
            String toName = entry.getKey();
            Integer count = entry.getValue();
            println(out, fromName,  toName, count.toString());
        }
    }

    /**
     * Exports the specified graph into a plain text file format containing
     * a sparse representation of the graph's Laplacian matrix. Laplacian
     * matrices are only defined for simple graphs, so edge direction,
     * multiple edges, loops, and weights are all ignored when
     * creating the Laplacian matrix. If you're unsure about Laplacian
     * matrices, see:
     * <a href="http://mathworld.wolfram.com/LaplacianMatrix.html">http://mathworld.wolfram.com/LaplacianMatrix.html</a>.
     *
     * @param output the writer to which the graph is to be exported.
     * @param g the graph to be exported.
     */
    public void exportLaplacianMatrix(Writer output, UndirectedGraph g)
    {
        PrintWriter out = new PrintWriter(output);

        VertexNameProvider nameProvider = new IntegerNameProvider();
        for (Object from : g.vertexSet()) {
            // assign ids in vertex set iteration order
            nameProvider.getVertexName(from);
        }

        for (Object from : g.vertexSet()) {
            String fromName = nameProvider.getVertexName(from);
            // TODO modify GraphHelper to return neighbor sets
            List neighbors = GraphHelper.neighborListOf(g, from);
            println(out, fromName, fromName, Integer.toString(neighbors.size()));
            for (Object to : neighbors) {
                String toName = nameProvider.getVertexName(to);
                println(out, fromName, toName, "-1");
            }
        }

        out.flush();
    }

    /**
     * Exports the specified graph into a plain text file format containing
     * a sparse representation of the graph's normalized Laplacian matrix.
     * Laplacian matrices are only defined for simple graphs, so edge
     * direction, multiple edges, loops, and weights are all ignored when
     * creating the Laplacian matrix. If you're unsure about normalized
     * Laplacian matrices, see:
     * <a href="http://mathworld.wolfram.com/LaplacianMatrix.html">http://mathworld.wolfram.com/LaplacianMatrix.html</a>.
     *
     * @param output the writer to which the graph is to be exported.
     * @param g the graph to be exported.
     */
    public void exportNormalizedLaplacianMatrix(Writer output, UndirectedGraph g)
    {
        PrintWriter out = new PrintWriter(output);

        VertexNameProvider nameProvider = new IntegerNameProvider();
        for (Object from : g.vertexSet()) {
            // assign ids in vertex set iteration order
            nameProvider.getVertexName(from);
        }

        for (Object from : g.vertexSet()) {
            String fromName = nameProvider.getVertexName(from);
            // TODO modify GraphHelper to return neighbor sets
            Set neighbors = new LinkedHashSet(GraphHelper.neighborListOf(g, from));
            if (neighbors.isEmpty()) {
                println(out, fromName, fromName, "0");
            }
            else {
                println(out, fromName, fromName, "1");

                for (Object to : neighbors) {
                    String toName = nameProvider.getVertexName(to);
                    double value = -1 / Math.sqrt(g.degreeOf(from) * g.degreeOf(to));
                    println(out, fromName, toName, Double.toString(value));
                }
            }
        }

        out.flush();
    }

    //~ Inner Classes ---------------------------------------------------------

    /**
     * Assigns a unique integer to represent each vertex. Each instance
     * of IntegerNameProvider maintains an internal map between every
     * vertex it has ever seen and the unique integer representing that
     * vertex.
     */
    public class IntegerNameProvider implements VertexNameProvider
    {
        private int nextID = 1;
        private Map<Object,Integer> idMap = new HashMap<Object,Integer>();

        /**
         * Returns the String representation of the unique integer representing
         * a vertex.
         *
         * @param vertex the vertex to be named
         * @return the name of
         * @see GraphListener#edgeAdded(GraphEdgeChangeEvent)
         */
        public String getVertexName(Object vertex)
        {
            Integer id = idMap.get(vertex);
            if (id == null) {
                id = nextID++;
                idMap.put(vertex, id);
            }

            return id.toString();
        }
    }

}
