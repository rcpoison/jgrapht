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
/* -----------------
 * GraphFactory.java
 * -----------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 24-Jul-2003 : Initial revision (BN);
 * 26-Jul-2003 : Added support for user-definable GraphFactory (BN);
 * 27-Jul-2003 : Added accessors for edge-factory factory (BN);
 *
 */
package org._3pq.jgrapht;

import java.util.Set;

import org._3pq.jgrapht.edge.EdgeFactoryFactory;
import org._3pq.jgrapht.graph.DefaultGraph;
import org._3pq.jgrapht.graph.DefaultListenableGraph;
import org._3pq.jgrapht.graph.Subgraph;
import org._3pq.jgrapht.graph.UnmodifiableGraph;

/**
 * A factory of graphs of various kinds and types. Some of the graphs are
 * "wrappers" that are backed by other graph. Note that this "other graph"
 * must be specified as a non-null parameter. If a null parameter is passed a
 * <code>NullPointerException</code> is thrown. If a wrong kind of graph is
 * specified an <code>IllegalArgumentException</code> is thrown.
 * 
 * <p>
 * Please note that the naming of graphs is rather confusing due to
 * inconsistency between directed and undirected graphs. For example, directed
 * multigraph allows loops while (undirected) multigraph does not. The
 * documentation of each  method below tell you the details of the type of
 * graph to be created.
 * </p>
 *
 * @author Barak Naveh
 *
 * @since Jul 19, 2003
 */
public abstract class GraphFactory {
    private static final String BASE_GRAPH_MUST_BE_LISTENABLE =
        "base graph must be listenable";
    private static GraphFactory s_factory = null;
    private EdgeFactoryFactory  m_eff     = new EdgeFactoryFactory(  );

    /**
     * Returns the factory that produces the edge-factories for all the graphs
     * created by this factory.
     *
     * @return the factory that produces the edge-factories.
     */
    public EdgeFactoryFactory getEdgeFactoryFactory(  ) {
        return m_eff;
    }


    /**
     * Sets the factory that produces the edge-factories for all the graphs
     * created by this factory.
     *
     * @param eff factory that produces the edge-factories to be set.
     */
    public void setEff( EdgeFactoryFactory eff ) {
        m_eff = eff;
    }


    /**
     * Installs the specified graph factory as the globally used graph factory
     * of the JGraphT library.
     *
     * @param factory the new graph factory to be installed.
     */
    public static void setFactory( GraphFactory factory ) {
        s_factory = factory;
    }


    /**
     * Returns the graph factory that is globally used by the JGraphT library.
     *
     * @return the graph factory used by the JGraphT library.
     */
    public static GraphFactory getFactory(  ) {
        if( s_factory == null ) {
            s_factory = new GraphFactory(  ) {}
            ;
        }

        return s_factory;
    }


    /**
     * Creates a new directed graph. A directed graph is a non-simple directed
     * graph in which multiple edges between any two vertices are <i>not</i>
     * permitted, but loops are.
     *
     * @return a new directed graph.
     */
    public DirectedGraph createDirectedGraph(  ) {
        EdgeFactory ef = m_eff.createDirectedEdgeFactory(  );

        return new PlainDirectedGraph( ef );
    }


    /**
     * Creates a new directed multigraph. A directed multigraph is a non-simple
     * directed graph in which loops and multiple edges between any two
     * vertices are permitted.
     *
     * @return a new directed multigraph.
     */
    public DirectedGraph createDirectedMultigraph(  ) {
        EdgeFactory ef = m_eff.createDirectedEdgeFactory(  );

        return new DirectedMultigraph( ef );
    }


    /**
     * Creates a new directed weighted graph. A directed weighted graph is a
     * non-simple directed graph in which multiple edges between any two
     * vertices are <i>not</i> permitted, but loops are. The graph has weights
     * on its edges.
     *
     * @return a new directed weighted graph.
     */
    public DirectedWeightedGraph createDirectedWeightedGraph(  ) {
        EdgeFactory ef = m_eff.createDirectedWeightedEdgeFactory(  );

        return new PlainDirectedWeightedGraph( ef );
    }


    /**
     * Creates a new directed weighted multigraph. A directed weighted
     * multigraph is a non-simple directed graph in which loops and multiple
     * edges between any two vertices are permitted, and edges have weights.
     *
     * @return a new directed weighted multigraph.
     */
    public DirectedWeightedGraph createDirectedWeightedMultigraph(  ) {
        EdgeFactory ef = m_eff.createDirectedWeightedEdgeFactory(  );

        return new DirectedWeightedMultigraph( ef );
    }


    /**
     * Creates a new graph backed by the specified graph, which can be listened
     * by <code>GraphListener</code>s and by <code>VertexSetListener</code>s.
     * Operations on the returned graph "pass through" to the specified graph.
     * Any modification made to any of the two graphs is reflected by the
     * other.
     * 
     * <p>
     * The returned graph does <i>not</i> pass the hashCode and equals
     * operations through to the backing graph, but relies on
     * <tt>Object</tt>'s <tt>equals</tt> and <tt>hashCode</tt> methods.
     * </p>
     *
     * @param g the graph to listen on.
     *
     * @return a new graph backed by <code>g</code> that reflects any
     *         modification made to <code>g</code>, but is also listenable.
     *
     * @see GraphListener
     * @see VertexSetListener
     */
    public ListenableGraph createListenableGraph( Graph g ) {
        return new DefaultListenableGraph( g );
    }


    /**
     * @see #createListenableGraph(Graph)
     */
    public ListenableGraph createListenableGraph( DirectedGraph g ) {
        return new ListenableDirectedGraph( g );
    }


    /**
     * @see #createListenableGraph(Graph)
     */
    public ListenableGraph createListenableGraph( DirectedWeightedGraph g ) {
        return new ListenableDirectedWeightedGraph( g );
    }


    /**
     * @see #createListenableGraph(Graph)
     */
    public ListenableGraph createListenableGraph( UndirectedGraph g ) {
        return new ListenableUndirectedGraph( g );
    }


    /**
     * @see #createListenableGraph(Graph)
     */
    public ListenableGraph createListenableGraph( UndirectedWeightedGraph g ) {
        return new ListenableUndirectedWeightedGraph( g );
    }


    /**
     * Creates a new multigraph. A multigraph is a non-simple undirected graph
     * in which no loops are permitted, but multiple edges between any two
     * vertices are. If you're unsure about multigraphs, see: <a
     * href="http://mathworld.wolfram.com/Multigraph.html">
     * http://mathworld.wolfram.com/Multigraph.html</a>.
     *
     * @return a new multigraph.
     */
    public UndirectedGraph createMultigraph(  ) {
        EdgeFactory ef = m_eff.createUndirectedEdgeFactory(  );

        return new Multigraph( ef );
    }


    /**
     * Creates a new pseudograph. A pseudograph is a non-simple undirected
     * graph in which both graph loops and multiple edges are permitted. If
     * you're unsure about pseudographs, see: <a
     * href="http://mathworld.wolfram.com/Pseudograph.html">
     * http://mathworld.wolfram.com/Pseudograph.html</a>.
     *
     * @return a new pseudograph.
     */
    public UndirectedGraph createPseudograph(  ) {
        EdgeFactory ef = m_eff.createUndirectedEdgeFactory(  );

        return new Pseudograph( ef );
    }


    /**
     * Creates a new simple directed graph. A simple directed graph is a
     * directed graph in which neither multiple edges between any two vertices
     * nor loops are permitted.
     *
     * @return a new simple directed graph.
     */
    public DirectedGraph createSimpleDirectedGraph(  ) {
        EdgeFactory ef = m_eff.createDirectedEdgeFactory(  );

        return new SimpleDirectedGraph( ef );
    }


    /**
     * Creates a new simple directed weighted graph. A simple directed weighted
     * graph is a simple directed graph for which edges are assigned weights.
     *
     * @return a new simple directed weighted graph.
     *
     * @see #createSimpleDirectedGraph()
     * @see WeightedElement
     */
    public DirectedWeightedGraph createSimpleDirectedWeightedGraph(  ) {
        EdgeFactory ef = m_eff.createDirectedEdgeFactory(  );

        return new SimpleDirectedWeightedGraph( ef );
    }


    /**
     * Creates a new simple graph. A simple graph is an undirected graph for
     * which at most one edge connects any two vertices, and loops are not
     * permitted.  If you're unsure about simple graphs, see: <a
     * href="http://mathworld.wolfram.com/SimpleGraph.html">
     * http://mathworld.wolfram.com/SimpleGraph.html</a>.
     *
     * @return a new simple graph.
     */
    public UndirectedGraph createSimpleGraph(  ) {
        EdgeFactory ef = m_eff.createUndirectedEdgeFactory(  );

        return new SimpleGraph( ef );
    }


    /**
     * Creates a new simple weighted graph. A simple weighted graph is a simple
     * graph  for which edges are assigned weights.
     *
     * @return a new simple weighted graph.
     *
     * @see #createSimpleGraph()
     */
    public UndirectedWeightedGraph createSimpleWeightedGraph(  ) {
        EdgeFactory ef = m_eff.createUndirectedWeightedEdgeFactory(  );

        return new SimpleWeightedGraph( ef );
    }


    /**
     * Creates a subgraph on the specified graph. A subgraph is a graph that
     * has a subset of vertices and subset of edges with respect to some base
     * graph.  More formally, A subgraph G(V,E) that is based on a graph
     * Gb(Vb,Eb) satisfies the following <b><i>subgraph property</i></b>: V is
     * a subset of Vb and E is a subset of Eb. Other than this property, a
     * subgraph is a graph with any respect and fully complies with the
     * <code>Graph</code> interface. This method uses the specified graph as a
     * base graph and creates a subgraph using the specified subsets of
     * vertices and edges.
     * 
     * <p>
     * The created subgraph is a "live-window" on the the base graph. If edges
     * or vertices are removed from the base graph, they are automatically
     * removed from the subgraph. Subgraph listeners are informed on such
     * removals if they also result in a cascade removal from the subgraph. If
     * edges or vertices are added to the base graph, the subgraph remains
     * unaffected.
     * </p>
     * 
     * <p>
     * Modifications to Subgraph are allowed as long as the subgraph property
     * is maintained. Addition of vertices or edges are allowed as long as
     * they also exist in the base graph. Removal of vertices or edges is
     * always allowed. The base graph is <i>never</i> affected by any
     * modification applied to the subgraph.
     * </p>
     *
     * @param base the graph on which the subgraph is based.
     * @param vertexSubset vertices to include in the subgraph. If
     *        <code>null</code> then all vertices are included.
     * @param edgeSubset edges to in include in the subgraph. If
     *        <code>null</code> then all the edges whose vertices found in the
     *        graph are included.
     *
     * @return a subgraph on the specified graph.
     *
     * @see Subgraph
     */
    public Graph createSubgraph( ListenableGraph base, Set vertexSubset,
        Set edgeSubset ) {
        return new Subgraph( base, vertexSubset, edgeSubset );
    }


    /**
     * @see #createSubgraph(Graph, Set, Set)
     */
    public DirectedGraph createSubgraph( DirectedGraph base, Set vertexSubset,
        Set edgeSubset ) {
        if( base instanceof ListenableGraph ) {
            return new SubgraphDirected( base, vertexSubset, edgeSubset );
        }
        else {
            throw new IllegalArgumentException( BASE_GRAPH_MUST_BE_LISTENABLE );
        }
    }


    /**
     * @see #createSubgraph(Graph, Set, Set)
     */
    public DirectedWeightedGraph createSubgraph( DirectedWeightedGraph base,
        Set vertexSubset, Set edgeSubset ) {
        if( base instanceof ListenableGraph ) {
            return new SubgraphDirectedWeighted( base, vertexSubset, edgeSubset );
        }
        else {
            throw new IllegalArgumentException( BASE_GRAPH_MUST_BE_LISTENABLE );
        }
    }


    /**
     * @see #createSubgraph(Graph, Set, Set)
     */
    public UndirectedGraph createSubgraph( UndirectedGraph base,
        Set vertexSubset, Set edgeSubset ) {
        if( base instanceof ListenableGraph ) {
            return new SubgraphUndirected( base, vertexSubset, edgeSubset );
        }
        else {
            throw new IllegalArgumentException( BASE_GRAPH_MUST_BE_LISTENABLE );
        }
    }


    /**
     * @see #createSubgraph(Graph, Set, Set)
     */
    public UndirectedWeightedGraph createSubgraph( 
        UndirectedWeightedGraph base, Set vertexSubset, Set edgeSubset ) {
        if( base instanceof ListenableGraph ) {
            return new SubgraphUndirectedWeighted( base, vertexSubset,
                edgeSubset );
        }
        else {
            throw new IllegalArgumentException( BASE_GRAPH_MUST_BE_LISTENABLE );
        }
    }


    /**
     * Returns an unmodifiable view of the specified graph.  This method allows
     * modules to provide users with "read-only" access to internal graphs.
     * Query operations on the returned graph "read through" to the specified
     * graph, and attempts to modify the returned graph result in an
     * <code>UnsupportedOperationException</code>.
     * 
     * <p>
     * The returned graph does <i>not</i> pass the hashCode and equals
     * operations through to the backing graph, but relies on
     * <tt>Object</tt>'s <tt>equals</tt> and <tt>hashCode</tt> methods.  The
     * returned graph will be serializable if the specified graph is
     * serializable.
     * </p>
     *
     * @param g the graph for which an unmodifiable view is to be returned.
     *
     * @return an unmodifiable view of the specified graph.
     */
    public Graph createUnmodifiableGraph( Graph g ) {
        return new UnmodifiableGraph( g );
    }


    /**
     * @see #createUnmodifiableGraph(Graph)
     */
    public DirectedGraph createUnmodifiableGraph( DirectedGraph g ) {
        return new UnmodifiableDirectedGraph( g );
    }


    /**
     * @see #createUnmodifiableGraph(Graph)
     */
    public DirectedWeightedGraph createUnmodifiableGraph( 
        DirectedWeightedGraph g ) {
        return new UnmodifiableDirectedWeightedGraph( g );
    }


    /**
     * @see #createUnmodifiableGraph(Graph)
     */
    public UndirectedGraph createUnmodifiableGraph( UndirectedGraph g ) {
        return new UnmodifiableUndirectedGraph( g );
    }


    /**
     * @see #createUnmodifiableGraph(Graph)
     */
    public UndirectedWeightedGraph createUnmodifiableGraph( 
        UndirectedWeightedGraph g ) {
        return new UnmodifiableUndirectedWeightedGraph( g );
    }


    /**
     * Creates a new weighted multigraph. A weighted multigraph is a non-simple
     * undirected graph in which no loops are permitted, but multiple edges
     * between any two vertices are. The edges of a weighted multigraph have
     * weights. If you're unsure about multigraphs, see: <a
     * href="http://mathworld.wolfram.com/Multigraph.html">
     * http://mathworld.wolfram.com/Multigraph.html</a>.
     *
     * @return a new weighted multigraph.
     */
    public UndirectedWeightedGraph createWeightedMultigraph(  ) {
        EdgeFactory ef = m_eff.createUndirectedWeightedEdgeFactory(  );

        return new WeightedMultigraph( ef );
    }


    /**
     * Creates a new weighted pseudograph. A weighted pseudograph is a
     * non-simple undirected graph in which both graph loops and multiple
     * edges are permitted. The edges of a weighted pseudograph have weights.
     * If you're unsure about pseudographs, see: <a
     * href="http://mathworld.wolfram.com/Pseudograph.html">
     * http://mathworld.wolfram.com/Pseudograph.html</a>.
     *
     * @return a new weighted pseudograph.
     */
    public UndirectedWeightedGraph createWeightedPseudograph(  ) {
        EdgeFactory ef = m_eff.createUndirectedWeightedEdgeFactory(  );

        return new WeightedPseudograph( ef );
    }
}
