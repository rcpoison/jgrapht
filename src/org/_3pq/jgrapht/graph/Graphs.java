/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (barak_naveh@users.sourceforge.net)
 *
 * (C) Copyright 2003, by Barak Naveh and Contributors.
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
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -----------
 * Graphs.java
 * -----------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 31-Jul-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.graph;

import java.util.Set;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.DirectedWeightedGraph;
import org._3pq.jgrapht.EdgeFactory;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.ListenableGraph;
import org._3pq.jgrapht.UndirectedGraph;
import org._3pq.jgrapht.UndirectedWeightedGraph;

/**
 * This utility class is a container of internal graph classes that should not
 * be instantiated directly, but rather via the {@link
 * org._3pq.jgrapht.GraphFactory}. Classes included here do not have any
 * substantial logic -- their implementation is done by their superclass. They
 * were created order to provide type-safety and in order to avoid unchecked
 * users' type-casts.
 * 
 * <p>
 * All included classes are small. They are grouped together in this container
 * to avoid clutter and to avoid unnecessary increase in configuration items.
 * If an inner class requires a more elaborated implementation it should be
 * extracted from this container to be a top-level class.
 * </p>
 *
 * @author Barak Naveh
 *
 * @see org._3pq.jgrapht.GraphFactory
 * @since Jul 31, 2003
 */
public class Graphs {
    private Graphs(  ) {} // ensure non-instantiability.

    /**
     * A directed multigraph. A directed multigraph is a non-simple directed
     * graph in which loops and multiple edges between any two vertices are
     * permitted.
     */
    public static class DirectedMultigraph extends DefaultGraph
        implements DirectedGraph {
        /**
         * @see DefaultGraph
         */
        public DirectedMultigraph( EdgeFactory ef ) {
            super( ef, true, true );
        }
    }


    /**
     * A directed weighted multigraph. A directed weighted multigraph is a
     * non-simple directed graph in which loops and multiple edges between any
     * two vertices are permitted, and edges have weights.
     */
    public static class DirectedWeightedMultigraph extends DirectedMultigraph
        implements DirectedWeightedGraph {
        /**
         * @see DefaultGraph
         */
        public DirectedWeightedMultigraph( EdgeFactory ef ) {
            super( ef );
        }
    }


    /**
     * A directed graph which is also {@link org._3pq.jgrapht.ListenableGraph}.
     */
    public static class ListenableDirectedGraph extends DefaultListenableGraph
        implements DirectedGraph {
        /**
         * @see DefaultListenableGraph#DefaultListenableGraph(Graph)
         */
        public ListenableDirectedGraph( DirectedGraph base ) {
            super( base );
        }
    }


    /**
     * A directed weighted graph which is also {@link
     * org._3pq.jgrapht.ListenableGraph}.
     */
    public static class ListenableDirectedWeightedGraph
        extends ListenableDirectedGraph implements DirectedWeightedGraph {
        /**
         * @see DefaultListenableGraph#DefaultListenableGraph(Graph)
         */
        public ListenableDirectedWeightedGraph( DirectedWeightedGraph base ) {
            super( base );
        }
    }


    /**
     * An undirected graph which is also {@link
     * org._3pq.jgrapht.ListenableGraph}.
     */
    public static class ListenableUndirectedGraph extends DefaultListenableGraph
        implements UndirectedGraph {
        /**
         * @see DefaultListenableGraph#DefaultListenableGraph(Graph)
         */
        public ListenableUndirectedGraph( UndirectedGraph base ) {
            super( base );
        }
    }


    /**
     * An undirected weighted graph which is also {@link
     * org._3pq.jgrapht.ListenableGraph}.
     */
    public static class ListenableUndirectedWeightedGraph
        extends ListenableUndirectedGraph implements UndirectedWeightedGraph {
        /**
         * @see DefaultListenableGraph#DefaultListenableGraph(Graph)
         */
        public ListenableUndirectedWeightedGraph( UndirectedWeightedGraph base ) {
            super( base );
        }
    }


    /**
     * A multigraph. A multigraph is a non-simple undirected graph in which no
     * loops are permitted, but multiple edges between any two vertices are.
     * If you're unsure about multigraphs, see: <a
     * href="http://mathworld.wolfram.com/Multigraph.html">
     * http://mathworld.wolfram.com/Multigraph.html</a>.
     */
    public static class Multigraph extends DefaultGraph
        implements UndirectedGraph {
        /**
         * @see DefaultGraph
         */
        public Multigraph( EdgeFactory ef ) {
            super( ef, true, false );
        }
    }


    /**
     * A directed graph. A directed graph is a non-simple directed graph in
     * which multiple edges between any two vertices are <i>not</i> permitted,
     * but loops are.
     * 
     * <p>
     * prefixed 'Plain' to avoid name collision with the DirectedGraph
     * interface.
     * </p>
     */
    public static class PlainDirectedGraph extends DefaultGraph
        implements DirectedGraph {
        /**
         * @see DefaultGraph
         */
        public PlainDirectedGraph( EdgeFactory ef ) {
            super( ef, false, true );
        }
    }


    /**
     * A directed weighted graph. A directed weighted graph is a non-simple
     * directed graph in which multiple edges between any two vertices are
     * <i>not</i> permitted, but loops are. The graph has weights on its
     * edges.
     * 
     * <p>
     * prefixed 'Plain' to avoid name collision with the DirectedWeightedGraph
     * interface.
     * </p>
     */
    public static class PlainDirectedWeightedGraph extends PlainDirectedGraph
        implements DirectedWeightedGraph {
        /**
         * @see DefaultGraph
         */
        public PlainDirectedWeightedGraph( EdgeFactory ef ) {
            super( ef );
        }
    }


    /**
     * A pseudograph. A pseudograph is a non-simple undirected graph in which
     * both graph loops and multiple edges are permitted. If you're unsure
     * about pseudographs, see: <a
     * href="http://mathworld.wolfram.com/Pseudograph.html">
     * http://mathworld.wolfram.com/Pseudograph.html</a>.
     */
    public static class Pseudograph extends DefaultGraph
        implements UndirectedGraph {
        /**
         * @see DefaultGraph
         */
        public Pseudograph( EdgeFactory ef ) {
            super( ef, true, true );
        }
    }


    /**
     * A simple directed graph. A simple directed graph is a directed graph in
     * which neither multiple edges between any two vertices nor loops are
     * permitted.
     */
    public static class SimpleDirectedGraph extends DefaultGraph
        implements DirectedGraph {
        /**
         * @see DefaultGraph
         */
        public SimpleDirectedGraph( EdgeFactory ef ) {
            super( ef, false, false );
        }
    }


    /**
     * A simple directed weighted graph. A simple directed weighted graph is a
     * simple directed graph for which edges are assigned weights.
     */
    public static class SimpleDirectedWeightedGraph extends SimpleDirectedGraph
        implements DirectedWeightedGraph {
        /**
         * @see DefaultGraph
         */
        public SimpleDirectedWeightedGraph( EdgeFactory ef ) {
            super( ef );
        }
    }


    /**
     * A simple graph. A simple graph is an undirected graph for which at most
     * one edge connects any two vertices, and loops are not permitted.  If
     * you're unsure about simple graphs, see: <a
     * href="http://mathworld.wolfram.com/SimpleGraph.html">
     * http://mathworld.wolfram.com/SimpleGraph.html</a>.
     */
    public static class SimpleGraph extends DefaultGraph
        implements UndirectedGraph {
        /**
         * @see DefaultGraph
         */
        public SimpleGraph( EdgeFactory ef ) {
            super( ef, false, false );
        }
    }


    /**
     * A simple weighted graph. A simple weighted graph is a simple graph  for
     * which edges are assigned weights.
     */
    public static class SimpleWeightedGraph extends SimpleGraph
        implements UndirectedWeightedGraph {
        /**
         * @see DefaultGraph
         */
        public SimpleWeightedGraph( EdgeFactory ef ) {
            super( ef );
        }
    }


    /**
     * A directed graph that is a subgraph on other graph.
     */
    public static class SubgraphDirected extends Subgraph
        implements DirectedGraph {
        /**
         * @see Subgraph#Subgraph(Graph, Set, Set)
         */
        public SubgraphDirected( DirectedGraph base, Set vertexSubset,
            Set edgeSubset ) {
            super( (ListenableGraph) base, vertexSubset, edgeSubset );
        }
    }


    /**
     * A directed weighted graph that is a subgraph on other graph.
     */
    public static class SubgraphDirectedWeighted extends SubgraphDirected
        implements DirectedWeightedGraph {
        /**
         * @see Subgraph#Subgraph(Graph, Set, Set)
         */
        public SubgraphDirectedWeighted( DirectedWeightedGraph base,
            Set vertexSubset, Set edgeSubset ) {
            super( base, vertexSubset, edgeSubset );
        }
    }


    /**
     * An undirected graph that is a subgraph on other graph.
     */
    public static class SubgraphUndirected extends Subgraph
        implements UndirectedGraph {
        /**
         * @see Subgraph#Subgraph(Graph, Set, Set)
         */
        public SubgraphUndirected( UndirectedGraph base, Set vertexSubset,
            Set edgeSubset ) {
            super( (ListenableGraph) base, vertexSubset, edgeSubset );
        }
    }


    /**
     * An undirected weighted graph that is a subgraph on other graph.
     */
    public static class SubgraphUndirectedWeighted extends SubgraphUndirected
        implements UndirectedWeightedGraph {
        /**
         * @see Subgraph#Subgraph(Graph, Set, Set)
         */
        public SubgraphUndirectedWeighted( UndirectedWeightedGraph base,
            Set vertexSubset, Set edgeSubset ) {
            super( base, vertexSubset, edgeSubset );
        }
    }


    /**
     * A directed graph that cannot be modified.
     */
    public static class UnmodifiableDirectedGraph extends UnmodifiableGraph
        implements DirectedGraph {
        /**
         * @see UnmodifiableGraph#UnmodifiableGraph(Graph)
         */
        public UnmodifiableDirectedGraph( Graph g ) {
            super( g );
        }
    }


    /**
     * A directed weighted graph that cannot be modified.
     */
    public static class UnmodifiableDirectedWeightedGraph
        extends UnmodifiableDirectedGraph implements DirectedWeightedGraph {
        /**
         * @see UnmodifiableGraph#UnmodifiableGraph(Graph)
         */
        public UnmodifiableDirectedWeightedGraph( Graph g ) {
            super( g );
        }
    }


    /**
     * An undirected graph that cannot be modified.
     */
    public static class UnmodifiableUndirectedGraph extends UnmodifiableGraph
        implements UndirectedGraph {
        /**
         * @see UnmodifiableGraph#UnmodifiableGraph(Graph)
         */
        public UnmodifiableUndirectedGraph( Graph g ) {
            super( g );
        }
    }


    /**
     * An undirected weighted graph that cannot be modified.
     */
    public static class UnmodifiableUndirectedWeightedGraph
        extends UnmodifiableUndirectedGraph implements UndirectedWeightedGraph {
        /**
         * @see UnmodifiableGraph#UnmodifiableGraph(Graph)
         */
        public UnmodifiableUndirectedWeightedGraph( Graph g ) {
            super( g );
        }
    }


    /**
     * A weighted multigraph. A weighted multigraph is a non-simple undirected
     * graph in which no loops are permitted, but multiple edges between any
     * two vertices are. The edges of a weighted multigraph have weights. If
     * you're unsure about multigraphs, see: <a
     * href="http://mathworld.wolfram.com/Multigraph.html">
     * http://mathworld.wolfram.com/Multigraph.html</a>.
     */
    public static class WeightedMultigraph extends Multigraph
        implements UndirectedWeightedGraph {
        /**
         * @see DefaultGraph
         */
        public WeightedMultigraph( EdgeFactory ef ) {
            super( ef );
        }
    }


    /**
     * A weighted pseudograph. A weighted pseudograph is a non-simple
     * undirected graph in which both graph loops and multiple edges are
     * permitted. The edges of a weighted pseudograph have weights. If you're
     * unsure about pseudographs, see: <a
     * href="http://mathworld.wolfram.com/Pseudograph.html">
     * http://mathworld.wolfram.com/Pseudograph.html</a>.
     */
    public static class WeightedPseudograph extends Pseudograph
        implements UndirectedWeightedGraph {
        /**
         * @see DefaultGraph
         */
        public WeightedPseudograph( EdgeFactory ef ) {
            super( ef );
        }
    }
}
