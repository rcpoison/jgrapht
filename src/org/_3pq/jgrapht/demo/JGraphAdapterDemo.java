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
/* ----------------------
 * JGraphAdapterDemo.java
 * ----------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * 03-Aug-2003 : Initial revision (BN);
 *
 */
package org._3pq.jgrapht.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Rectangle;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JApplet;
import javax.swing.JFrame;

import com.jgraph.JGraph;
import com.jgraph.graph.DefaultGraphCell;
import com.jgraph.graph.GraphConstants;

import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.GraphFactory;
import org._3pq.jgrapht.ListenableGraph;
import org._3pq.jgrapht.jgraph.JGraphModelAdapter;

/**
 * A demo applet that shows how to use JGraph to visualize JGraphT graphs.
 *
 * @author Barak Naveh
 *
 * @since Aug 3, 2003
 */
public class JGraphAdapterDemo extends JApplet {
    private static final Color     BG_COLOR = Color.decode( "#FAFBFF" );
    private static final Dimension SIZE = new Dimension( 530, 320 );

    //
    private JGraphModelAdapter m_jGraphModel;
    private ListenableGraph    m_graph;

    /**
     * Constructor for VisualizationDemo.
     *
     * @throws HeadlessException
     */
    public JGraphAdapterDemo(  ) throws HeadlessException {
        super(  );

        GraphFactory gf = GraphFactory.getFactory(  );
        m_graph           = gf.createListenableGraph( gf.createDirectedGraph(  ) );
        m_jGraphModel     = new JGraphModelAdapter( m_graph );

        JGraph jgraph     = new JGraph( m_jGraphModel );

        jgraph.setBackground( BG_COLOR );
        jgraph.setPreferredSize( SIZE );
        getContentPane(  ).add( jgraph );
    }

    /**
     * @see java.applet.Applet#init().
     */
    public void init(  ) {
        resize( SIZE );

        // add some sample data
        Graph g = m_graph;
        g.addVertex( "v1" );
        g.addVertex( "v2" );
        g.addVertex( "v3" );
        g.addVertex( "v4" );

        g.addEdge( "v1", "v2" );
        g.addEdge( "v2", "v3" );
        g.addEdge( "v3", "v1" );
        g.addEdge( "v4", "v3" );

        // position the vertices
        positionVertexAt( "v1", 130, 40 );
        positionVertexAt( "v2", 60, 200 );
        positionVertexAt( "v3", 310, 230 );
        positionVertexAt( "v4", 380, 70 );
    }


    /**
     * An alternative starting point for the demo (other than the Applet
     * starting point).
     *
     * @param args ignored.
     */
    public static void main( String[] args ) {
        JGraphAdapterDemo applet = new JGraphAdapterDemo(  );
        applet.init(  );

        JFrame frame = new JFrame(  );
        frame.getContentPane(  ).add( applet );
        frame.setTitle( "JGraphT Adapter to JGraph Demo" );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.pack(  );
        frame.show(  );
    }


    private void positionVertexAt( Object vertex, int x, int y ) {
        DefaultGraphCell cell = m_jGraphModel.getVertexCell( vertex );
        Map              attr = cell.getAttributes(  );
        Rectangle        b    = GraphConstants.getBounds( attr );

        GraphConstants.setBounds( attr, new Rectangle( x, y, b.width, b.height ) );

        Map cellAttr = new HashMap(  );
        cellAttr.put( cell, attr );
        m_jGraphModel.edit( cellAttr );
    }
}
