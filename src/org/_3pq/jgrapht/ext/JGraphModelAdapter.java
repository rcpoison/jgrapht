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
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* -----------------------
 * JGraphModelAdapter.java
 * -----------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   Erik Postma
 *
 * $Id$
 *
 * Changes
 * -------
 * 02-Aug-2003 : Initial revision (BN);
 * 10-Aug-2003 : Adaptation to new event model (BN);
 * 06-Nov-2003 : Allowed non-listenable underlying JGraphT graph (BN);
 * 12-Dec-2003 : Added CellFactory support (BN);
 * 27-Jan-2004 : Added support for JGraph->JGraphT change propagation (EP);
 *
 */
package org._3pq.jgrapht.ext;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.ListenableGraph;
import org._3pq.jgrapht.UndirectedGraph;
import org._3pq.jgrapht.edge.DirectedEdge;
import org._3pq.jgrapht.edge.UndirectedEdge;
import org._3pq.jgrapht.event.GraphEdgeChangeEvent;
import org._3pq.jgrapht.event.GraphListener;
import org._3pq.jgrapht.event.GraphVertexChangeEvent;

import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelEvent.GraphModelChange;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.Port;

/**
 * An adapter that reflects a JGraphT graph as a JGraph graph. This adapter is
 * useful when using JGraph in order to visualize JGraphT graphs. For more
 * about JGraph see <a
 * href="http://jgraph.sourceforge.net">http://jgraph.sourceforge.net</a>
 * 
 * <p>
 * Modifications made to the underlying JGraphT graph are reflected to this
 * JGraph model if and only if the underlying JGraphT graph is a {@link
 * org._3pq.jgrapht.ListenableGraph}. If the underlying JGraphT graph is
 * <i>not</i> ListenableGraph, then this JGraph model represent a snapshot if
 * the graph at the time of its creation.
 * </p>
 * 
 * <p>
 * Changes made to this JGraph model are also reflected back to the underlying
 * JGraphT graph. To avoid confusion, variables are prefixed according to the
 * JGraph/JGraphT object(s) they are refering to.
 * </p>
 * 
 * <p>
 * <b>KNOWN BUGS:</b> There is a small issue to be aware of. JGraph allows
 * 'dangling edges' incident with just one vertex; JGraphT doesn't. Such a
 * configuration can arise when adding an edge or removing a vertex. The code
 * handles this by removing the newly-added dangling edge or removing all
 * edges incident with the vertex before actually removing the vertex,
 * respectively. This works very well, only it doesn't play all that nicely
 * with the undo-manager in the JGraph: for the second situation where you
 * remove a vertex incident with some edges, if you undo the removal, the
 * vertex is 'unremoved' but the edges aren't.
 * </p>
 *
 * @author Barak Naveh
 *
 * @since Aug 2, 2003
 */
public class JGraphModelAdapter extends DefaultGraphModel {
    Set                   m_jEdgesBeingAdded        = new HashSet(  );
    Set                   m_jEdgesBeingRemoved      = new HashSet(  );
    Set                   m_jVerticesBeingAdded     = new HashSet(  );
    Set                   m_jVerticesBeingRemoved   = new HashSet(  );
    Set                   m_jtEdgesBeingAdded       = new HashSet(  );
    Set                   m_jtEdgesBeingRemoved     = new HashSet(  );
    Set                   m_jtVerticesBeingAdded    = new HashSet(  );
    Set                   m_jtVerticesBeingRemoved  = new HashSet(  );
    private CellFactory   m_cellFactory;
    private Graph         m_jtGraph;
    private GraphListener m_jtGraphListener;
    private Map           m_cellToEdge              = new HashMap(  );
    private Map           m_cellToVertex            = new HashMap(  );
    private Map           m_defaultEdgeAttributes   = new HashMap(  );
    private Map           m_defaultVertexAttributes = new HashMap(  );
    private Map           m_edgeToCell              = new HashMap(  );
    private Map           m_vertexToCell            = new HashMap(  );

    /**
     * Constructs a new JGraph model adapter for the specified JGraphT graph.
     *
     * @param jGraphTGraph the JGraphT graph for which JGraph model adapter to
     *        be created.
     */
    public JGraphModelAdapter( Graph jGraphTGraph ) {
        this( jGraphTGraph, null, null );
    }


    /**
     * Constructs a new JGraph model adapter for the specified JGraphT graph.
     *
     * @param jGraphTGraph the JGraphT graph for which JGraph model adapter to
     *        be created.
     * @param defaultVertexAttributes a default map of JGraph attributes to
     *        format vertices, or <code>null</code> to use internal defaults.
     * @param defaultEdgeAttributes a default map of JGraph attributes to
     *        format edges, or <code>null</code> to use internal defaults.
     */
    public JGraphModelAdapter( Graph jGraphTGraph, Map defaultVertexAttributes,
        Map defaultEdgeAttributes ) {
        this( jGraphTGraph, defaultVertexAttributes, defaultEdgeAttributes, null );
    }


    /**
     * Constructs a new JGraph model adapter for the specified JGraphT graph.
     *
     * @param jGraphTGraph the JGraphT graph for which JGraph model adapter to
     *        be created.
     * @param defaultVertexAttributes a default map of JGraph attributes to
     *        format vertices, or <code>null</code> to use internal defaults.
     * @param defaultEdgeAttributes a default map of JGraph attributes to
     *        format edges, or <code>null</code> to use internal defaults.
     * @param cellFactory a {@link CellFactory} to be used to create the JGraph
     *        cells, or <code>null</code> to use internal default factory.
     */
    public JGraphModelAdapter( Graph jGraphTGraph, Map defaultVertexAttributes,
        Map defaultEdgeAttributes, CellFactory cellFactory ) {
        super(  );

        m_jtGraph = jGraphTGraph;

        if( defaultVertexAttributes == null ) {
            m_defaultVertexAttributes = createDefaultVertexAttributes(  );
        }
        else {
            m_defaultVertexAttributes = defaultVertexAttributes;
        }

        if( defaultEdgeAttributes == null ) {
            m_defaultEdgeAttributes =
                createDefaultEdgeAttributes( jGraphTGraph );
        }
        else {
            m_defaultEdgeAttributes = defaultEdgeAttributes;
        }

        if( cellFactory == null ) {
            m_cellFactory = new DefaultCellFactory(  );
        }
        else {
            m_cellFactory = cellFactory;
        }

        if( jGraphTGraph instanceof ListenableGraph ) {
            m_jtGraphListener = new JGraphTListener(  );
            ( (ListenableGraph) jGraphTGraph ).addGraphListener( m_jtGraphListener );
        }

        this.addGraphModelListener( new JGraphListener(  ) );

        for( Iterator i = jGraphTGraph.vertexSet(  ).iterator(  );
                i.hasNext(  ); ) {
            addJGraphTVertex( i.next(  ) );
        }

        for( Iterator i = jGraphTGraph.edgeSet(  ).iterator(  ); i.hasNext(  ); ) {
            addJGraphTEdge( (org._3pq.jgrapht.Edge) i.next(  ) );
        }
    }

    /**
     * Returns the JGraph edge cell that corresponds to the specified JGraphT
     * edge. If no corresponding cell found, returns <code>null</code>.
     *
     * @param jGraphTEdge a JGraphT edge of the JGraphT graph.
     *
     * @return the JGraph edge cell that corresponds to the specified JGraphT
     *         edge, or <code>null</code> if no corresponding cell found.
     */
    public DefaultEdge getEdgeCell( org._3pq.jgrapht.Edge jGraphTEdge ) {
        return (DefaultEdge) m_edgeToCell.get( jGraphTEdge );
    }


    /**
     * Returns the JGraph vertex cell that corresponds to the specified JGraphT
     * vertex. If no corresponding cell found, returns <code>null</code>.
     *
     * @param jGraphTVertex a JGraphT vertex of the JGraphT graph.
     *
     * @return the JGraph vertex cell that corresponds to the specified JGraphT
     *         vertex, or <code>null</code> if no corresponding cell found.
     */
    public DefaultGraphCell getVertexCell( Object jGraphTVertex ) {
        return (DefaultGraphCell) m_vertexToCell.get( jGraphTVertex );
    }


    /**
     * Returns the JGraph port cell that corresponds to the specified JGraphT
     * vertex. If no corresponding port found, returns <code>null</code>.
     *
     * @param jGraphTVertex a JGraphT vertex of the JGraphT graph.
     *
     * @return the JGraph port cell that corresponds to the specified JGraphT
     *         vertex, or <code>null</code> if no corresponding cell found.
     */
    public DefaultPort getVertexPort( Object jGraphTVertex ) {
        DefaultGraphCell vertexCell = getVertexCell( jGraphTVertex );

        if( vertexCell == null ) {
            return null;
        }
        else {
            return (DefaultPort) vertexCell.getChildAt( 0 );
        }
    }


    /**
     * Applies the specified attributes to the model, as in {@link
     * org.jgraph.graph.GraphModel#edit} method.
     *
     * @param attributes the attributes to be applied to the model.
     */
    public void edit( Map attributes ) {
        super.edit( attributes, null, null, null );
    }


    /**
     * Adds an edge corresponding to the specified JGraph edge to the
     * underlying JGraphT graph. We try to find out to which vertices the edge
     * is connected; if we find only one or none at all, we remove it again
     * from the JGraph graph, because we cannot add such an edge to the
     * JGraphT graph.
     * 
     * <p>
     * This method is to be called only for edges that have already been added
     * to the JGraph graph.
     * </p>
     *
     * @param jEdge the JGraph Edge to be added.
     *
     * @return true if the edge was successfully added, false otherwise.
     */
    protected boolean addJGraphEdge( org.jgraph.graph.Edge jEdge ) {
        org._3pq.jgrapht.Edge jtEdge;
        Object                jSource = getSourceVertex( this, jEdge );
        Object                jTarget = getTargetVertex( this, jEdge );

        if( !( m_cellToVertex.containsKey( jSource )
                && m_cellToVertex.containsKey( jTarget ) ) ) {
            // This is a 'dangling edge'. We have to remove it in the
            // JGraph. 
            // TODO: Consider alternatives that will allow dangling edges.
            Object[] eArray = { jEdge };
            m_jEdgesBeingRemoved.add( jEdge );
            super.remove( eArray );

            return false;
        }

        Object jtSource = m_cellToVertex.get( jSource );
        Object jtTarget = m_cellToVertex.get( jTarget );

        if( m_jtGraph instanceof UndirectedGraph ) {
            jtEdge = new UndirectedEdge( jtSource, jtTarget );
        }
        else if( m_jtGraph instanceof DirectedGraph ) {
            jtEdge = new DirectedEdge( jtSource, jtTarget );
        }
        else {
            jtEdge =
                new org._3pq.jgrapht.edge.DefaultEdge( jtSource, jtTarget );

            // We use the JGraph DefaultEdge all the time, so we import
            // that and not this version.
        }

        m_jtEdgesBeingAdded.add( jtEdge );

        boolean result = m_jtGraph.addEdge( jtEdge );

        if( result ) {
            m_cellToEdge.put( jEdge, jtEdge );
            m_edgeToCell.put( jtEdge, jEdge );

            return true;
        }
        else {
            // Adding the edge failed. We have to remove it from the
            // JGraph too.
            super.remove( new Object[] { jEdge } );

            return false;
        }
    }


    /**
     * Adds the specified JGraphT edge to be reflected by this graph model. To
     * be called only for edges that already exist in the JGraphT graph.
     *
     * @param jtEdge a JGraphT edge to be reflected by this graph model.
     */
    protected void addJGraphTEdge( org._3pq.jgrapht.Edge jtEdge ) {
        DefaultEdge edgeCell = m_cellFactory.createEdgeCell( jtEdge );
        m_edgeToCell.put( jtEdge, edgeCell );
        m_cellToEdge.put( edgeCell, jtEdge );

        ConnectionSet cs = new ConnectionSet(  );
        cs.connect( edgeCell, getVertexPort( jtEdge.getSource(  ) ),
            getVertexPort( jtEdge.getTarget(  ) ) );

        Object[] cells      = { edgeCell };
        Map      attributes = new HashMap(  );
        attributes.put( edgeCell,
            GraphConstants.cloneMap( m_defaultEdgeAttributes ) );
        m_jEdgesBeingAdded.add( edgeCell );
        super.insert( cells, attributes, cs, null, null );
    }


    /**
     * Adds the specified JGraphT vertex to be reflected by this graph model.
     * To be called only for edges that already exist in the JGraphT graph.
     *
     * @param jtVertex a JGraphT vertex to be reflected by this graph model.
     */
    protected void addJGraphTVertex( Object jtVertex ) {
        DefaultGraphCell vertexCell =
            m_cellFactory.createVertexCell( jtVertex );
        vertexCell.add( new DefaultPort(  ) );

        m_vertexToCell.put( jtVertex, vertexCell );
        m_cellToVertex.put( vertexCell, jtVertex );

        Object[] cells = { vertexCell };

        Map      attributes = new HashMap(  );
        attributes.put( vertexCell,
            GraphConstants.cloneMap( m_defaultVertexAttributes ) );
        m_jVerticesBeingAdded.add( vertexCell );
        super.insert( cells, attributes, null, null, null );
    }


    /**
     * Add a vertex corresponding to this JGraph vertex to the JGraphT graph.
     * In JGraph, two vertices with the same user object are in principle
     * allowed; in JGraphT, this would lead to duplicate vertices, which is
     * not allowed. So if the vertex exists already, we remove it. This method
     * is to be called only for vertices that have already been added to the
     * JGraph graph.
     *
     * @param jVertex the JGraph vertex to be added.
     *
     * @return true if the vertex was successfully added, false otherwise.
     */
    protected boolean addJGraphVertex( GraphCell jVertex ) {
        Object jtVertex;

        if( jVertex instanceof DefaultGraphCell ) {
            jtVertex = ( (DefaultGraphCell) jVertex ).getUserObject(  );
        }
        else {
            jtVertex = jVertex.toString(  );
        }

        if( m_vertexToCell.containsKey( jtVertex ) ) {
            // We have to remove the new vertex, because it would lead to
            // duplicate vertices. We can't use removeJGraphTVertex for
            // that, because it would remove the wrong (existing) vertex.
            Object[] cells = { jVertex };
            super.remove( cells );

            return false;
        }

        m_jtVerticesBeingAdded.add( jtVertex );

        boolean result = m_jtGraph.addVertex( jtVertex );

        m_cellToVertex.put( jVertex, jtVertex );
        m_vertexToCell.put( jtVertex, jVertex );

        return result;
    }


    /**
     * Creates and returns a map of attributes to be used as defaults for edge
     * attributes, depending on the specified graph.
     *
     * @param jGraphTGraph the graph for which default edge attributes to be
     *        created.
     *
     * @return a map of attributes to be used as default for edge attributes.
     */
    protected Map createDefaultEdgeAttributes( Graph jGraphTGraph ) {
        Map map = new HashMap(  );

        if( jGraphTGraph instanceof DirectedGraph ) {
            GraphConstants.setLineEnd( map, GraphConstants.ARROW_TECHNICAL );
            GraphConstants.setEndFill( map, true );
            GraphConstants.setEndSize( map, 10 );
        }

        GraphConstants.setForeground( map, Color.decode( "#25507C" ) );
        GraphConstants.setFont( map,
            GraphConstants.defaultFont.deriveFont( Font.BOLD, 12 ) );
        GraphConstants.setLineColor( map, Color.decode( "#7AA1E6" ) );

        return map;
    }


    /**
     * Creates and returns a map of attributes to be used as defaults for
     * vertex attributes.
     *
     * @return a map of attributes to be used as defaults for vertex
     *         attributes.
     */
    protected Map createDefaultVertexAttributes(  ) {
        Map   map = new HashMap(  );
        Color c = Color.decode( "#FF9900" );

        GraphConstants.setBounds( map, new Rectangle( 50, 50, 90, 30 ) );
        GraphConstants.setBorder( map, BorderFactory.createRaisedBevelBorder(  ) );
        GraphConstants.setBackground( map, c );
        GraphConstants.setForeground( map, Color.white );
        GraphConstants.setFont( map,
            GraphConstants.defaultFont.deriveFont( Font.BOLD, 12 ) );
        GraphConstants.setOpaque( map, true );

        return map;
    }


    /**
     * Remove the edge corresponding to this JGraph edge from the JGraphT
     * graph. To be called only for edges that have already been removed from
     * the JGraph graph.
     *
     * @param jEdge the JGraph Edge to be removed. If it is not in
     *        m_cellsToEdges, it is silently ignored.
     *
     * @return true if the edge could successfully be removed, false otherwise.
     */
    protected boolean removeJGraphEdge( org.jgraph.graph.Edge jEdge ) {
        if( !m_cellToEdge.containsKey( jEdge ) ) {
            return false;
        }

        org._3pq.jgrapht.Edge jtEdge =
            (org._3pq.jgrapht.Edge) m_cellToEdge.get( jEdge );

        m_jtEdgesBeingRemoved.add( jtEdge );

        boolean result = m_jtGraph.removeEdge( jtEdge );

        m_cellToEdge.remove( jEdge );
        m_edgeToCell.remove( jtEdge );

        return result;
    }


    /**
     * Removes the specified JGraphT edge from being reflected by this graph
     * model. To be called only for edges that have already been removed from
     * the JGraphT graph.
     *
     * @param jtEdge a JGraphT edge to be removed from being reflected by this
     *        graph model.
     */
    protected void removeJGraphTEdge( org._3pq.jgrapht.Edge jtEdge ) {
        DefaultEdge edgeCell = (DefaultEdge) m_edgeToCell.remove( jtEdge );
        m_cellToEdge.remove( edgeCell );

        Object[] cells = { edgeCell };
        m_jEdgesBeingRemoved.add( edgeCell );
        super.remove( cells );
    }


    /**
     * Removes the specified JGraphT vertex from being reflected by this graph
     * model. To be called only for vertices that have already been removed
     * from the JGraphT graph.
     *
     * @param jtVertex a JGraphT vertex to be removed from being reflected by
     *        this graph model.
     */
    protected void removeJGraphTVertex( Object jtVertex ) {
        DefaultGraphCell vertexCell =
            (DefaultGraphCell) m_vertexToCell.remove( jtVertex );
        m_cellToVertex.remove( vertexCell );

        Object[] cells = { vertexCell, vertexCell.getChildAt( 0 ) };
        m_jVerticesBeingRemoved.add( vertexCell );
        super.remove( cells );
    }


    /**
     * Remove the vertex corresponding to this JGraph vertex from the JGraphT
     * graph. If any edges are incident with this vertex, we remove them from
     * both graphs first, because otherwise the JGraph graph would leave them
     * intact and the JGraphT graph would throw them out. This method is to be
     * called only for vertices that have already been removed from the JGraph
     * graph.
     *
     * @param jVertex the JGraph vertex to be removed. If it is not in
     *        m_cellsToVertices, it is silently ignored.
     *
     * @return true if the vertex could successfully be removed, false
     *         otherwise.
     */
    protected boolean removeJGraphVertex( GraphCell jVertex ) {
        if( !m_cellToVertex.containsKey( jVertex ) ) {
            return false;
        }

        Object jtVertex      = m_cellToVertex.get( jVertex );
        List   incidentEdges = m_jtGraph.edgesOf( jtVertex );

        if( incidentEdges != null ) {
            // We can't just call removeAllEdges with this list: that
            // would throw a ConcurrentModificationException. So we create
            // a shallow copy.
            Iterator iterator = incidentEdges.iterator(  );
            incidentEdges = new ArrayList( incidentEdges.size(  ) );

            while( iterator.hasNext(  ) ) {
                incidentEdges.add( iterator.next(  ) );
            }

            m_jtGraph.removeAllEdges( incidentEdges );

            // This also triggers removal of the corresponding JGraph
            // edges. 
        }

        m_jtVerticesBeingRemoved.add( jtVertex );

        boolean result = m_jtGraph.removeVertex( jtVertex );

        m_cellToVertex.remove( jVertex );
        m_vertexToCell.remove( jtVertex );

        return result;
    }

    /**
     * Creates the JGraph cells that reflect the respective JGraphT elements.
     *
     * @author Barak Naveh
     *
     * @since Dec 12, 2003
     */
    public interface CellFactory {
        /**
         * Creates an edge cell that contains its respective JGraphT edge.
         *
         * @param jGraphTEdge a JGraphT edge to be contained.
         *
         * @return an edge cell that contains its respective JGraphT edge.
         */
        public DefaultEdge createEdgeCell( org._3pq.jgrapht.Edge jGraphTEdge );


        /**
         * Creates a vertex cell that contains its respective JGraphT vertex.
         *
         * @param jGraphTVertex a JGraphT vertex to be contained.
         *
         * @return a vertex cell that contains its respective JGraphT vertex.
         */
        public DefaultGraphCell createVertexCell( Object jGraphTVertex );
    }

    /**
     * A simple default cell factory.
     *
     * @author Barak Naveh
     *
     * @since Dec 12, 2003
     */
    public class DefaultCellFactory implements CellFactory, Serializable {
        /**
         * @see org._3pq.jgrapht.ext.JGraphModelAdapter.CellFactory#createEdgeCell(Edge)
         */
        public DefaultEdge createEdgeCell( org._3pq.jgrapht.Edge jGraphTEdge ) {
            return new DefaultEdge( jGraphTEdge );
        }


        /**
         * @see org._3pq.jgrapht.ext.JGraphModelAdapter.CellFactory#createVertexCell(Object)
         */
        public DefaultGraphCell createVertexCell( Object jGraphTVertex ) {
            return new DefaultGraphCell( jGraphTVertex );
        }
    }


    /**
     * <p>
     * Inner class listening to the GraphModel. If something is changed in the
     * GraphModel, this Listener gets notified and propagates the change back
     * to the JGraphT graph, if it didn't originate there.
     * </p>
     * 
     * <p>
     * If this change contains changes that would make this an illegal JGraphT
     * graph, like adding an edge that is incident with only one vertex, the
     * illegal parts of the change are undone.
     * </p>
     */
    private class JGraphListener implements GraphModelListener, Serializable {
        /**
         * This method is called for all JGraph changes.
         *
         * @param e
         */
        public void graphChanged( GraphModelEvent e ) {
            // We first remove edges that have to be removed, then we
            // remove vertices, then we add vertices and finally we add
            // edges. Otherwise, things might go wrong: for example, if we
            // would first remove vertices and then edges, removal of the
            // vertices might induce 'automatic' removal of edges. If we
            // later attempt to re-remove these edges, we get confused.
            GraphModelChange change    = e.getChange(  );
            Set              jEdges    = new HashSet(  );
            Set              JVertices = new HashSet(  );

            Object[]         arrayToProcess = change.getRemoved(  );

            if( arrayToProcess != null ) {
                filterEdgesAndVertices( arrayToProcess, jEdges, JVertices );

                for( Iterator i = jEdges.iterator(  ); i.hasNext(  ); ) {
                    org.jgraph.graph.Edge jEdge =
                        (org.jgraph.graph.Edge) i.next(  );

                    if( m_jEdgesBeingRemoved.contains( jEdge ) ) {
                        m_jEdgesBeingRemoved.remove( jEdge );
                    }
                    else {
                        removeJGraphEdge( jEdge );
                    }
                }

                for( Iterator i = JVertices.iterator(  ); i.hasNext(  ); ) {
                    GraphCell jVertex = (GraphCell) i.next(  );

                    if( m_jVerticesBeingRemoved.contains( jVertex ) ) {
                        m_jVerticesBeingRemoved.remove( jVertex );
                    }
                    else {
                        removeJGraphVertex( jVertex );
                    }
                }

                jEdges.clear(  );
                JVertices.clear(  );
            }

            arrayToProcess = change.getInserted(  );

            if( arrayToProcess != null ) {
                filterEdgesAndVertices( arrayToProcess, jEdges, JVertices );

                for( Iterator i = JVertices.iterator(  ); i.hasNext(  ); ) {
                    GraphCell jVertex = (GraphCell) i.next(  );

                    if( m_jVerticesBeingAdded.contains( jVertex ) ) {
                        m_jVerticesBeingAdded.remove( jVertex );
                    }
                    else {
                        addJGraphVertex( jVertex );
                    }
                }

                for( Iterator i = jEdges.iterator(  ); i.hasNext(  ); ) {
                    org.jgraph.graph.Edge jEdge =
                        (org.jgraph.graph.Edge) i.next(  );

                    if( m_jEdgesBeingAdded.contains( jEdge ) ) {
                        m_jEdgesBeingAdded.remove( jEdge );
                    }
                    else {
                        addJGraphEdge( jEdge );
                    }
                }
            }
        }


        /**
         * Filters an array of JGraph GraphCell objects into edges and
         * vertices. Ports and 'groups' are thrown away.
         *
         * @param allCells Array of cells to be filtered. These are just plain
         *        Objects in principle, but if they aren't GraphCells we can't
         *        detect what they are. So we ignore the non-GraphCell
         *        elements.
         * @param jEdges Set to which all edges are added.
         * @param jVertices Set to which all vertices are added.
         */
        private void filterEdgesAndVertices( Object[] allCells, Set jEdges,
            Set jVertices ) {
            for( int i = 0; i < allCells.length; i++ ) {
                Object current = allCells[ i ];

                if( current instanceof org.jgraph.graph.Edge ) {
                    jEdges.add( current );
                }
                else if( !( current instanceof Port ) ) {
                    if( current instanceof DefaultGraphCell ) {
                        DefaultGraphCell graphCell = (DefaultGraphCell) current;

                        if( graphCell.isLeaf(  ) // Note: do not change the order
                                || 
                            // of these conditions; the code uses the short-cutting of ||.
                            ( graphCell.getFirstChild(  ) instanceof Port ) ) {
                            jVertices.add( current );
                        }

                        // If a DefaultGraphCell has a Port as a child, it is a
                    }
                    else if( current instanceof GraphCell ) {
                        // If it is not a DefaultGraphCell, it doesn't have
                        // children.
                        jVertices.add( current );
                    }

                    // Otherwise, this is neither an Edge nor a GraphCell; then we
                    // don't know what to do with it. So ignore.
                }
            }
        }
    }


    /**
     * A listener on the underlying JGraphT graph. This listener is used to
     * keep the JGraph model in sync. Whenever one of the event handlers is
     * called, it first checks whether the change is due to a previous change
     * in the JGraph model. If it is, then no action is taken.
     *
     * @author Barak Naveh
     *
     * @since Aug 2, 2003
     */
    private class JGraphTListener implements GraphListener, Serializable {
        /**
         * @see GraphListener#edgeAdded(GraphEdgeChangeEvent)
         */
        public void edgeAdded( GraphEdgeChangeEvent e ) {
            org._3pq.jgrapht.Edge jtEdge = e.getEdge(  );

            if( m_jtEdgesBeingAdded.contains( jtEdge ) ) {
                m_jtEdgesBeingAdded.remove( jtEdge );
            }
            else {
                addJGraphTEdge( jtEdge );
            }
        }


        /**
         * @see GraphListener#edgeRemoved(GraphEdgeChangeEvent)
         */
        public void edgeRemoved( GraphEdgeChangeEvent e ) {
            org._3pq.jgrapht.Edge jtEdge = e.getEdge(  );

            if( m_jtEdgesBeingRemoved.contains( jtEdge ) ) {
                m_jtEdgesBeingRemoved.remove( jtEdge );
            }
            else {
                removeJGraphTEdge( jtEdge );
            }
        }


        /**
         * @see VertexSetListener#vertexAdded(GraphVertexChangeEvent)
         */
        public void vertexAdded( GraphVertexChangeEvent e ) {
            Object jtVertex = e.getVertex(  );

            if( m_jtVerticesBeingAdded.contains( jtVertex ) ) {
                m_jtVerticesBeingAdded.remove( jtVertex );
            }
            else {
                addJGraphTVertex( jtVertex );
            }
        }


        /**
         * @see VertexSetListener#vertexRemoved(GraphVertexChangeEvent)
         */
        public void vertexRemoved( GraphVertexChangeEvent e ) {
            Object jtVertex = e.getVertex(  );

            if( m_jtVerticesBeingRemoved.contains( jtVertex ) ) {
                m_jtVerticesBeingRemoved.remove( jtVertex );
            }
            else {
                removeJGraphTVertex( jtVertex );
            }
        }
    }
}
