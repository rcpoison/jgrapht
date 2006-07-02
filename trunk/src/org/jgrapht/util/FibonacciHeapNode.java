/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (barak_naveh@users.sourceforge.net)
 *
 * (C) Copyright 2003-2006, by Barak Naveh and Contributors.
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
 * $Id$
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
 */
public class FibonacciHeapNode<T>
{
    /**
     * Node data.
     */
    T data;
    
    /**
     * first child node
     */
    FibonacciHeapNode<T> child;

    /**
     * left sibling node
     */
    FibonacciHeapNode<T> left;

    /**
     * parent node
     */
    FibonacciHeapNode<T> parent;

    /**
     * right sibling node
     */
    FibonacciHeapNode<T> right;

    /**
     * true if this node has had a child removed since this node was added
     * to its parent
     */
    boolean mark;

    /**
     * key value for this node
     */
    double key;

    /**
     * number of children of this node (does not count grandchildren)
     */
    int degree;

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
        right = this;
        left = this;
        this.data = data;
        this.key = key;
    }

    /**
     * Obtain the key for this node.
     *
     * @return the key
     */
    public final double getKey()
    {
        return key;
    }

    /**
     * Obtain the data for this node.
     */
    public final T getData()
    {
        return data;
    }

    /**
     * Return the string representation of this object.
     *
     * @return string representing this object
     */
    public String toString()
    {
        if (true) {
            return Double.toString(key);
        } else {
            StringBuffer buf = new StringBuffer();
            buf.append("Node=[parent = ");

            if (parent != null) {
                buf.append(Double.toString(parent.key));
            } else {
                buf.append("---");
            }

            buf.append(", key = ");
            buf.append(Double.toString(key));
            buf.append(", degree = ");
            buf.append(Integer.toString(degree));
            buf.append(", right = ");

            if (right != null) {
                buf.append(Double.toString(right.key));
            } else {
                buf.append("---");
            }

            buf.append(", left = ");

            if (left != null) {
                buf.append(Double.toString(left.key));
            } else {
                buf.append("---");
            }

            buf.append(", child = ");

            if (child != null) {
                buf.append(Double.toString(child.key));
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
