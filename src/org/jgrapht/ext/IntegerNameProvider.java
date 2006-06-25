/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Lead:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
 *
 * (C) Copyright 2003-2005, by Barak Naveh and Contributors.
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
 * IntegerNameProvider.java
 * ------------------
 * (C) Copyright 2005, by Charles Fry and Contributors.
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
package org.jgrapht.ext;

import java.io.*;

import java.util.*;

import org.jgrapht.*;

/**
 * Assigns a unique integer to represent each vertex. Each instance
 * of IntegerNameProvider maintains an internal map between every
 * vertex it has ever seen and the unique integer representing that
 * vertex. As a result it is probably desirable to have a separate
 * instance for each distinct graph.
 *
 * @author Charles Fry
 */
public class IntegerNameProvider<V> implements VertexNameProvider<V>
{
    private int m_nextID = 1;
    private final Map<V,Integer> m_idMap = new HashMap<V,Integer>();

    /**
     * Clears all cached identifiers, and resets the unique identifier counter.
     */
    public void clear()
    {
        m_nextID = 1;
        m_idMap.clear();
    }

    /**
     * Returns the String representation of the unique integer representing
     * a vertex.
     *
     * @param vertex the vertex to be named
     * @return the name of
     * @see GraphListener#edgeAdded(GraphEdgeChangeEvent)
     */
    public String getVertexName(V vertex)
    {
        Integer id = m_idMap.get(vertex);
        if (id == null) {
            id = m_nextID++;
            m_idMap.put(vertex, id);
        }

        return id.toString();
    }

}
