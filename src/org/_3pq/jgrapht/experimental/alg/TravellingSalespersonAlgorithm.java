/*
    Copyright (C) 2003 Michael Behrisch

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org._3pq.jgrapht.experimental.alg;

import org._3pq.jgrapht.WeightedGraph;

/**
 * Interface for algorithms for the Travelling Salesperson problem
 * of finding a shortest tour (cycle) in a graph which contains all vertices
 * exactly once.
 *
 * @author Michael Behrisch
 */
public interface TravellingSalespersonAlgorithm {

    /**
    * Abstract method to be implemented by subclasses.
    */
    public abstract WeightedGraph tspTour() throws Exception;
}
