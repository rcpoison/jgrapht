/* ==========================================
 * JGraphT : a free Java graph-theory library
 * ==========================================
 *
 * Project Info:  http://jgrapht.sourceforge.net/
 * Project Creator:  Barak Naveh (http://sourceforge.net/users/barak_naveh)
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
/* -----------------
 * ArrayUtil.java
 * -----------------
 * (C) Copyright 2005-2006, by Assaf Lehr and Contributors.
 *
 * $Id$
 *
 * Original Author:  Assaf Lehr
 * Contributor(s):   -
 *
 * Changes
 * -------
 */
package org.jgrapht.util;

/**
 * Utility class to iterate over arrays and use toString() on their elements.
 * In jdk1.5 , a few very useful methods were added to the system.Arrays class.
 * JGraphT should work on jdk1.4, so we re-implement them here, as utility
 * methods.
 *
 * @author Assaf_Lehr
 */
public class ArrayUtil
{

    //~ Methods ---------------------------------------------------------------

    /**
     * Prints the contents of an array (not the array ref itself).
     *
     * @param array
     */
    public static String toString(int [] array)
    {
        StringBuffer stringBuffer = new StringBuffer("[");
        stringBuffer.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuffer.append(",").append(array[i]);
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /**
     * Print the contents of an array (not the array ref itself).
     *
     * @param array
     */
    public static String toString(Object [] array, ToStringFunctor functor)
    {
        StringBuffer stringBuffer = new StringBuffer("[");
        stringBuffer.append(array[0]);
        for (int i = 0; i < array.length; i++) {
            stringBuffer.append(",").append(functor.toString(array[i]));
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    /**
     * Prints the contents of an array (not the array ref itself).
     *
     * @param array
     */
    public static String toString(Object [] array)
    {
        StringBuffer stringBuffer = new StringBuffer("[");
        stringBuffer.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuffer.append(",").append(array[i]);
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    //~ Inner Interfaces ------------------------------------------------------

    public interface ToStringFunctor
    {
        public String toString(Object arrayElement);
    }
}
