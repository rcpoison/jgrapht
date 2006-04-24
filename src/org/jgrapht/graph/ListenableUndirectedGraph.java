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
/* ------------------------------
 * ListenableUndirectedGraph.java
 * ------------------------------
 * (C) Copyright 2003, by Barak Naveh and Contributors.
 *
 * Original Author:  Barak Naveh
 * Contributor(s):   Christian Hammer
 *
 * $Id: ListenableUndirectedGraph.java,v 1.4 2005/09/05 04:00:57 perfecthash
 * Exp $
 *
 * Changes
 * -------
 * 05-Aug-2003 : Initial revision (BN);
 * 11-Mar-2004 : Made generic (CH);
 *
 */
package org.jgrapht.graph;

import org.jgrapht.*;


/**
 * An undirected graph which is also {@link org.jgrapht.ListenableGraph}.
 *
 * @see org.jgrapht.graph.DefaultListenableGraph
 */
public class ListenableUndirectedGraph<V, E extends Edge<V>>
    extends DefaultListenableGraph<V, E> implements UndirectedGraph<V, E>
{

    //~ Static fields/initializers --------------------------------------------

    private static final long serialVersionUID = 3256999969193145905L;

    //~ Constructors ----------------------------------------------------------

    /**
     * Creates a new listenable undirected simple graph.
     */
    public ListenableUndirectedGraph()
    {
        this(new SimpleGraph<V,E>());
    }

    /**
     * Creates a new listenable undirected graph.
     *
     * @param base the backing graph.
     */
    public ListenableUndirectedGraph(UndirectedGraph<V, E> base)
    {
        super(base);
    }
}
