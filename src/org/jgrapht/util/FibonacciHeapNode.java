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
 * along with this library; if not, write to the Free Software Foundation,
 * Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307, USA.
 */
/* --------------------------
 * FibonnaciHeapNode.java
 * --------------------------
 * (C) Copyright 1999-2006, by Nathan Fiedler and Contributors.
 *
 * Original Author:  Nathan Fiedler
 * Contributor(s):   John V. Sichi
 *
 * $Id: FibonacciHeap.java 456 2006-04-24 00:32:26Z perfecthash $
 *
 * Changes
 * -------
 * 03-Sept-2003 : Adapted from Nathan Fiedler (JVS);
 *
 *      Name    Date            Description
 *      ----    ----            -----------
 *      nf      08/31/97        Initial version
 *      nf      09/07/97        Removed FibHeapData interface
 *      nf      01/20/01        Added synchronization
 *      nf      01/21/01        Made Node an inner class
 *      nf      01/05/02        Added clear(), renamed empty() to
 *                              isEmpty(), and renamed printHeap()
 *                              to toString()
 *      nf      01/06/02        Removed all synchronization
 *      JVS     06/24/06        Generics
 *
 */
package org.jgrapht.util;

/**
 * Implements a node of the Fibonacci heap. It holds the information
 * necessary for maintaining the structure of the heap. It also holds the
 * reference to the key value (which is used to determine the heap
 * structure).
 *
 * @author Nathan Fiedler
 * @version $Id:$
 */
public class FibonacciHeapNode<T>
{
    /**
     * Node data.
     */
    T m_data;
    
    /**
     * first child node
     */
    FibonacciHeapNode<T> m_child;

    /**
     * left sibling node
     */
    FibonacciHeapNode<T> m_left;

    /**
     * parent node
     */
    FibonacciHeapNode<T> m_parent;

    /**
     * right sibling node
     */
    FibonacciHeapNode<T> m_right;

    /**
     * true if this node has had a child removed since this node was added
     * to its parent
     */
    boolean m_mark;

    /**
     * key value for this node
     */
    double m_key;

    /**
     * number of children of this node (does not count grandchildren)
     */
    int m_degree;

    /**
     * Default constructor.  Initializes the right and left pointers,
     * making this a circular doubly-linked list.
     *
     * @param data data for this node
     *
     * @param key initial key for node
     */
    public FibonacciHeapNode(T data, double key)
    {
        m_right = this;
        m_left = this;
        m_data = data;
        m_key = key;
    }

    /**
     * Obtain the key for this node.
     *
     * @return the key
     */
    public final double getKey()
    {
        return m_key;
    }

    /**
     * Obtain the data for this node.
     */
    public final T getData()
    {
        return m_data;
    }

    /**
     * Return the string representation of this object.
     *
     * @return string representing this object
     */
    public String toString()
    {
        if (true) {
            return Double.toString(m_key);
        } else {
            StringBuffer buf = new StringBuffer();
            buf.append("Node=[parent = ");

            if (m_parent != null) {
                buf.append(Double.toString(m_parent.m_key));
            } else {
                buf.append("---");
            }

            buf.append(", key = ");
            buf.append(Double.toString(m_key));
            buf.append(", degree = ");
            buf.append(Integer.toString(m_degree));
            buf.append(", right = ");

            if (m_right != null) {
                buf.append(Double.toString(m_right.m_key));
            } else {
                buf.append("---");
            }

            buf.append(", left = ");

            if (m_left != null) {
                buf.append(Double.toString(m_left.m_key));
            } else {
                buf.append("---");
            }

            buf.append(", child = ");

            if (m_child != null) {
                buf.append(Double.toString(m_child.m_key));
            } else {
                buf.append("---");
            }

            buf.append(']');

            return buf.toString();
        }
    }

    // toString
}

// End FibonacciHeapNode.java
