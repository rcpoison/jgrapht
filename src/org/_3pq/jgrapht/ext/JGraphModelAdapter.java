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
/* -----------------------
 * JGraphModelAdapter.java
 * -----------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 02-Aug-2003 : Initial revision (BN);
 * 10-Aug-2003 : Adaptation to new event model (BN);
 *
 */
package org._3pq.jgrapht.ext;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.undo.UndoableEdit;

import com.jgraph.graph.ConnectionSet;
import com.jgraph.graph.DefaultEdge;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.DefaultGraphModel;
import com.jgraph.graph.DefaultPort;
import com.jgraph.graph.GraphConstants;
import com.jgraph.graph.ParentMap;

import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.ListenableGraph;
import org._3pq.jgrapht.event.EdgeEvent;
import org._3pq.jgrapht.event.GraphListener;
import org._3pq.jgrapht.event.VertexEvent;

/**
 * An adapter that reflects a JGraphT graph as a JGraph graph. This adapter is
 * useful when using JGraph in order to visualize JGraphT graphs. For more
 * about JGraph see <a
 * href="http://jgraph.sourceforge.net">http://jgraph.sourceforge.net</a>
 * 
 * <p>
 * <b>Note:</b> modifications made to the underlying JGraphT graph are
 * reflected to this JGraph model, but changes made to this JGraph model are
 * <b>not</b> reflected back to the underlying JGraphT graph. To avoid
 * confusion, the methods <code>remove</code>, <code>insert</code> have been
 * disabled, and the method <code>edit</code> has been restricted.
 * </p>
 *
 * @author Barak Naveh
 *
 * @since Aug 2, 2003
 */
public class JGraphModelAdapter extends DefaultGraphModel {
    private GraphListener   m_graphListener;
    private ListenableGraph m_graph;
    private Map             m_defaultEdgeAttributes   = new HashMap(  );
    private Map             m_defaultVertexAttributes = new HashMap(  );
    private Map             m_edgeCells               = new HashMap(  );
    private Map             m_vertexCells             = new HashMap(  );

    /**
     * Constructs a new JGraph model adapter for the specified JGraphT graph.
     *
     * @param g the JGraphT graph for which JGraph model adapter to be created.
     */
    public JGraphModelAdapter( ListenableGraph g ) {
        this( g, null, null );
    }


    /**
     * Constructs a new JGraph model adapter for the specified JGraphT graph.
     *
     * @param g the JGraphT graph for which JGraph model adapter to be created.
     * @param defaultVertexAttributes default map of JGraph attributes to
     *        format vertices.
     * @param defaultEdgeAttributes default map of JGraph attributes to format
     *        edges.
     */
    public JGraphModelAdapter( ListenableGraph g, Map defaultVertexAttributes,
        Map defaultEdgeAttributes ) {
        super(  );

        if( defaultVertexAttributes == null ) {
            m_defaultVertexAttributes = createDefaultVertexAttributes(  );
        }
        else {
            m_defaultVertexAttributes = defaultVertexAttributes;
        }

        if( defaultEdgeAttributes == null ) {
            m_defaultEdgeAttributes = createDefaultEdgeAttributes( g );
        }
        else {
            m_defaultEdgeAttributes = defaultEdgeAttributes;
        }

        m_graph             = g;
        m_graphListener     = new MyGraphListener(  );
        m_graph.addGraphListener( m_graphListener );

        for( Iterator i = g.vertexSet(  ).iterator(  ); i.hasNext(  ); ) {
            addJGraphTVertex( i.next(  ) );
        }

        for( Iterator i = g.edgeSet(  ).iterator(  ); i.hasNext(  ); ) {
            addJGraphTEdge( (Edge) i.next(  ) );
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
    public DefaultEdge getEdgeCell( Edge jGraphTEdge ) {
        return (DefaultEdge) m_edgeCells.get( jGraphTEdge );
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
        return (DefaultGraphCell) m_vertexCells.get( jGraphTVertex );
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
     * com.jgraph.graph.GraphModel#edit} method. All other parameters should
     * be <code>null</code>. This is a degenerated version of the method
     * originally intended in the interface. It is recommended that you don't
     * use it. Use {@link #edit(Map)} instead.
     *
     * @param attributes the attributes to be applied to the model.
     * @param cs must be <code>null</code>.
     * @param pm must be <code>null</code>.
     * @param edits must be <code>null</code>.
     *
     * @throws IllegalArgumentException if any of the must-be-null is not null.
     *
     * @see com.jgraph.graph.GraphModel#edit
     */
    public void edit( Map attributes, ConnectionSet cs, ParentMap pm,
        UndoableEdit[] edits ) {
        if( cs != null || pm != null || edits != null ) {
            throw new IllegalArgumentException( 
                "Only null is permitted for cs, pm, and edits parameters" );
        }

        super.edit( attributes, cs, pm, edits );
    }


    /**
     * Applies the specified attributes to the model, as in {@link
     * com.jgraph.graph.GraphModel#edit} method.
     *
     * @param attributes the attributes to be applied to the model.
     */
    public void edit( Map attributes ) {
        super.edit( attributes, null, null, null );
    }


    /**
     * Unsupported operation.
     *
     * @param theRoots ignored.
     * @param attributes ignored.
     * @param cs ignored.
     * @param pm ignored.
     * @param edits ignored.
     *
     * @throws UnsupportedOperationException always.
     */
    public void insert( Object[] theRoots, Map attributes, ConnectionSet cs,
        ParentMap pm, UndoableEdit[] edits ) {
        throw new UnsupportedOperationException( 
            "Insert only via the JGraphT graph." );
    }


    /**
     * Unsupported operation.
     *
     * @param theRoots ignored.
     *
     * @throws UnsupportedOperationException always.
     */
    public void remove( Object[] theRoots ) {
        throw new UnsupportedOperationException( 
            "Remove only via the JGraphT graph." );
    }


    /**
     * Adds the specified JGraphT edge to be reflected by this graph model.
     *
     * @param e a JGraphT edge to be reflected by this graph model.
     */
    protected void addJGraphTEdge( Edge e ) {
        DefaultEdge edgeCell = new DefaultEdge( e );
        m_edgeCells.put( e, edgeCell );

        ConnectionSet cs = new ConnectionSet(  );
        cs.connect( edgeCell, getVertexPort( e.getSource(  ) ),
            getVertexPort( e.getTarget(  ) ) );

        Object[] cells      = { edgeCell };
        Map      attributes = new HashMap(  );
        attributes.put( edgeCell,
            GraphConstants.cloneMap( m_defaultEdgeAttributes ) );
        super.insert( cells, attributes, cs, null, null );
    }


    /**
     * Adds the specified JGraphT vertex to be reflected by this graph model.
     *
     * @param v a JGraphT vertex to be reflected by this graph model.
     */
    protected void addJGraphTVertex( Object v ) {
        DefaultGraphCell vertexCell = new DefaultGraphCell( v );
        vertexCell.add( new DefaultPort(  ) );

        m_vertexCells.put( v, vertexCell );

        Object[] cells = { vertexCell };

        Map      attributes = new HashMap(  );
        attributes.put( vertexCell,
            GraphConstants.cloneMap( m_defaultVertexAttributes ) );
        super.insert( cells, attributes, null, null, null );
    }


    /**
     * Creates and returns a map of attributes to be used as defaults for edge
     * attributes, depending on the specified graph.
     *
     * @param g the graph for which default edge attributes to be created.
     *
     * @return a map of attributes to be used as default for edge attributes.
     */
    protected Map createDefaultEdgeAttributes( Graph g ) {
        Map map = new HashMap(  );

        if( g instanceof DirectedGraph ) {
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
     * Removes the specified JGraphT edge from being reflected by this graph
     * model.
     *
     * @param e a JGraphT edge to be removed from being reflected by this graph
     *        model.
     */
    protected void removeJGraphTEdge( Edge e ) {
        DefaultEdge edgeCell = (DefaultEdge) m_edgeCells.remove( e );

        Object[]    cells = { edgeCell };
        super.remove( cells );
    }


    /**
     * Removes the specified JGraphT vertex from being reflected by this graph
     * model.
     *
     * @param v a JGraphT vertex to be removed from being reflected by this
     *        graph model.
     */
    protected void removeJGraphTVertex( Object v ) {
        DefaultGraphCell vertexCell =
            (DefaultGraphCell) m_vertexCells.remove( v );

        Object[]         cells = { vertexCell, vertexCell.getChildAt( 0 ) };
        super.remove( cells );
    }

    /**
     * A listener on the underlying JGraphT graph. This listener is used to
     * keep the JGraph model in sync.
     *
     * @author Barak Naveh
     *
     * @since Aug 2, 2003
     */
    private class MyGraphListener implements GraphListener {
        /**
         * @see GraphListener#edgeAdded(EdgeEvent)
         */
        public void edgeAdded( EdgeEvent e ) {
            addJGraphTEdge( e.getEdge(  ) );
        }


        /**
         * @see GraphListener#edgeRemoved(EdgeEvent)
         */
        public void edgeRemoved( EdgeEvent e ) {
            removeJGraphTEdge( e.getEdge(  ) );
        }


        /**
         * @see VertexSetListener#vertexAdded(VertexEvent)
         */
        public void vertexAdded( VertexEvent e ) {
            addJGraphTVertex( e.getVertex(  ) );
        }


        /**
         * @see VertexSetListener#vertexRemoved(VertexEvent)
         */
        public void vertexRemoved( VertexEvent e ) {
            removeJGraphTVertex( e.getVertex(  ) );
        }
    }
}
