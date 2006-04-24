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
/* --------------------------
 * AsUndirectedGraphTest.java
 * --------------------------
 * (C) Copyright 2003, by John V. Sichi and Contributors.
 *
 * Original Author:  John V. Sichi
 * Contributor(s):   -
 *
 * $Id$
 *
 * Changes
 * -------
 * 14-Aug-2003 : Initial revision (JVS);
 *
 */
package org._3pq.jgrapht.graph;

import org._3pq.jgrapht.DirEdge;
import org._3pq.jgrapht.DirectedGraph;
import org._3pq.jgrapht.Edge;
import org._3pq.jgrapht.EnhancedTestCase;
import org._3pq.jgrapht.Graph;
import org._3pq.jgrapht.edge.DefaultEdge;
import org._3pq.jgrapht.edge.DirectedEdge;

/**
 * A unit test for the AsDirectedGraph view.
 *
 * @author John V. Sichi
 */
public class GenericGraphsTest extends EnhancedTestCase
{

    //~ Instance fields -------------------------------------------------------

    Graph<Object,? extends Edge<Object>> objectGraph;
    Graph<FooVertex,FooEdge<FooVertex>> fooFooGraph;
    Graph<BarVertex,BarEdge<BarVertex>> barBarGraph;
    Graph<FooVertex,BarEdge<FooVertex>> fooBarGraph;

    //~ Constructors ----------------------------------------------------------

    /**
     * @see junit.framework.TestCase#TestCase(java.lang.String)
     */
    public GenericGraphsTest(String name)
    {
        super(name);
    }

    // ~ Methods ---------------------------------------------------------------

    public void testLegalInsertStringGraph() {
        try {
            String v1 = "Vertex1";
            Object v2 = "Vertex2";
            objectGraph.addVertex(v1);
            objectGraph.addVertex(v2);
            objectGraph.addEdge(v1, v2);
        } catch (Throwable e) {
            assertFalse();
        }
        assertTrue();
    }

    public void testLegalInsertFooGraph() {
        try {
            FooVertex v1 = new FooVertex();
            FooVertex v2 = new FooVertex();
            BarVertex vb1 = new BarVertex();
            BarVertex vb2 = new BarVertex();
            fooFooGraph.addVertex(v1);
            fooFooGraph.addVertex(v2);
            fooFooGraph.addVertex(vb1);
            fooFooGraph.addVertex(vb2);
            fooFooGraph.addEdge(v1, v2);
            fooFooGraph.addEdge(vb1, vb2);
            fooFooGraph.addEdge(v1, vb2);
            fooFooGraph.addEdge( new BarEdge<FooVertex>(v1,v2) );
            fooFooGraph.addEdge( new BarEdge<FooVertex>(v1,vb2) );
            //FIXME hb 051124: Basically, I think this should work, but it doesn't :-(
            //FIXME hb 051124: fooFooGraph.addEdge( new BarEdge<BarVertex>(vb1, vb2) );
        } catch (Throwable e) {
            assertFalse();
        }
        assertTrue();
    }

    public void testLegalInsertBarGraph() {
        try {
            BarVertex v1 = new BarVertex();
            BarVertex v2 = new BarVertex();
            barBarGraph.addVertex(v1);
            barBarGraph.addVertex(v2);
            barBarGraph.addEdge(v1, v2);
        } catch (Throwable e) {
            assertFalse();
        }
        assertTrue();
    }

    public void testLegalInsertFooBarGraph() {
        try {
            FooVertex v1 = new FooVertex();
            FooVertex v2 = new FooVertex();
            BarVertex vb1 = new BarVertex();
            BarVertex vb2 = new BarVertex();
            fooFooGraph.addVertex(v1);
            fooFooGraph.addVertex(v2);
            fooFooGraph.addVertex(vb1);
            fooFooGraph.addVertex(vb2);
            fooFooGraph.addEdge(v1, v2);
            fooFooGraph.addEdge(vb1, vb2);
            fooFooGraph.addEdge(v1, vb2);
        } catch (Throwable e) {
            assertFalse();
        }
        assertTrue();
    }

    public void testAlissaHacker() {
        
        DirectedGraph<String,CustomEdge> g = 
            new DefaultDirectedGraph<String,CustomEdge>();
        g.addVertex("a");
        g.addVertex("b");
        g.addEdge("a", "b");
        DirectedEdge custom = g.getEdge("a", "b");
                String s = custom.toString();
                assertEquals("(a,b)",s);
    }

    class CustomEdge extends DirectedEdge<String> {
        private static final long serialVersionUID = 1L;
        public CustomEdge(CustomEdge edge) {
            super(edge);
        }

        public CustomEdge(String sourceVertex, String targetVertex) {
            super(sourceVertex, targetVertex);
        }


    }
    /**
     * .
     */
    protected void setUp()
    {
        objectGraph = new DefaultDirectedGraph<Object,DirEdge<Object>>();
        fooFooGraph = new SimpleGraph<FooVertex,FooEdge<FooVertex>>();
        barBarGraph = new SimpleGraph<BarVertex,BarEdge<BarVertex>>();
   }
    
    
    private class FooEdge<V> extends DefaultEdge<V>
    {
        private static final long serialVersionUID = 1L;
        public FooEdge(V sourceVertex, V targetVertex) {
            super(sourceVertex, targetVertex);
        }
    }
    
    private class FooVertex {
        String str;
        public FooVertex(){
            super();
            str="empty foo";
        }
        
        public FooVertex( String s ) {
            str = s;
        }
    } 
    
    private class BarEdge<V> extends FooEdge<V> {
        private static final long serialVersionUID = 1L;
        public BarEdge(V sourceVertex, V targetVertex) {
            super(sourceVertex, targetVertex);
        }
    }
    
    private class BarVertex extends FooVertex {
        public BarVertex() {
            super("empty bar");
        }

        public BarVertex(String s) {
            super(s);
        }
        
    }
}
