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
/* -------------------------
 * RankingPathElementList.java
 * -------------------------
 * (C) Copyright 2007-2007, by France Telecom
 *
 * Original Author:  Guillaume Boulmier and Contributors.
 * Contributor(s):   John V. Sichi
 *
 * $Id$
 *
 * Changes
 * -------
 * 05-Jun-2007 : Initial revision (GB);
 * 05-Jul-2007 : Added support for generics (JVS);
 *
 */
package org.jgrapht.alg;

import java.util.*;

import org.jgrapht.*;


/**
 * List of simple paths in increasing order of weight.
 *
 * @author Guillaume Boulmier
 * @since July 5, 2007
 */
final class RankingPathElementList<V, E>
    extends AbstractPathElementList<V, E, RankingPathElement<V, E>>
{
    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a list with an empty path. The list size is 1.
     *
     * @param maxSize max number of paths the list is able to store.
     * @param maxSize maximum number of paths the list is able to store.
     */
    RankingPathElementList(
        Graph<V, E> graph,
        int maxSize,
        RankingPathElement<V, E> pathElement)
    {
        super(graph, maxSize, pathElement);
    }

    /**
     * Creates paths obtained by concatenating the specified edge to the
     * specified paths.
     *
     * @param prevPathElementList paths, list of <code>
     * RankingPathElement</code>.
     * @param edge edge reaching the end vertex of the created paths.
     * @param maxSize maximum number of paths the list is able to store.
     */
    RankingPathElementList(
        Graph<V, E> graph,
        int maxSize,
        RankingPathElementList<V, E> elementList,
        E edge)
    {
        super(graph, maxSize, elementList, edge);

        // loop over the path elements in increasing order of weight.
        for (int i = 0; i < elementList.size(); i++) {
            RankingPathElement<V, E> prevPathElement = elementList.get(i);
            if (this.pathElements.size() <= (this.maxSize - 1)) {
                double weight = calculatePathWeight(prevPathElement, edge);
                RankingPathElement<V, E> newPathElement =
                    new RankingPathElement<V, E>(
                        this.graph,
                        prevPathElement,
                        edge,
                        weight);

                // the new path is inserted at the end of the list.
                this.pathElements.add(newPathElement);
            }
        }

        assert (!this.pathElements.isEmpty());
    }

    /**
     * Copy constructor.
     *
     * @param original source to copy from
     */
    protected RankingPathElementList(RankingPathElementList<V, E> original)
    {
        super(original);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Adds paths in the list at vertex y. Candidate paths are obtained by
     * concatenating the specified edge (v->y) to the paths <code>
     * elementList</code> at vertex v.
     *
     * @param elementList list of paths at vertex v.
     * @param edge edge (v->y).
     *
     * @return <code>true</code> if at least one path has been added in the
     * list, <code>false</code> otherwise.
     */
    public boolean addPathElements(
        RankingPathElementList<V, E> elementList,
        E edge)
    {
        assert (this.vertex.equals(
            Graphs.getOppositeVertex(
                this.graph,
                edge,
                elementList.getVertex())));

        boolean pathAdded = false;

        // loop over the paths elements of the list at vertex v.
        for (
            int vIndex = 0, yIndex = 0;
            vIndex < elementList.size();
            vIndex++)
        {
            RankingPathElement<V, E> prevPathElement = elementList.get(vIndex);
            if (isAlreadyImprovedBythisEdge(edge, prevPathElement)
                || containsTargetPreviously(prevPathElement))
            {
                // checks if path is simple.
                continue;
            }
            double weight = calculatePathWeight(prevPathElement, edge);

            // loop over the paths elements of the list at vertex y from yIndex
            // to the end.
            for (; yIndex < size(); yIndex++) {
                RankingPathElement<V, E> yPathElement = get(yIndex);
                RankingPathElement<V, E> newPathElement =
                    new RankingPathElement<V, E>(
                        this.graph,
                        prevPathElement,
                        edge,
                        weight);

                if (weight < yPathElement.getWeight()) {
                    this.pathElements.add(yIndex, newPathElement);
                    if (size() > this.maxSize) {
                        this.pathElements.remove(this.maxSize);
                    }
                    pathAdded = true;
                    break;
                }
                if (weight == yPathElement.getWeight()) {
                    // checks if newPathElement is not already in the list.
                    if (isAlreadyAdded(newPathElement)) {
                        break;
                    }

                    if (size() <= (this.maxSize - 1)) {
                        this.pathElements.add(yIndex + 1, newPathElement);
                        if (size() > this.maxSize) {
                            this.pathElements.remove(this.maxSize);
                        }
                        pathAdded = true;
                        break;
                    }
                }

                if ((weight > yPathElement.getWeight())
                    && (yIndex == (size() - 1)))
                {
                    if (size() <= (this.maxSize - 1)) {
                        this.pathElements.add(newPathElement);
                        pathAdded = true;
                        break;
                    }
                }
            }
        }
        return pathAdded;
    }

    /**
     * @return list of <code>RankingPathElement</code>.
     */
    List<RankingPathElement<V, E>> getPathElements()
    {
        return this.pathElements;
    }

    /**
     * Costs taken into account are the weights stored in <code>Edge</code>
     * objects.
     *
     * @param pathElement
     * @param edge the edge via which the vertex was encountered.
     *
     * @return the cost obtained by concatenation.
     *
     * @see Graph#getEdgeWeight(E)
     */
    private double calculatePathWeight(
        RankingPathElement<V, E> pathElement,
        E edge)
    {
        double pathWeight = this.graph.getEdgeWeight(edge);

        // otherwise it's the start vertex.
        if ((pathElement.getPrevEdge() != null)) {
            pathWeight += pathElement.getWeight();
        }

        return pathWeight;
    }

    /**
     * Ensures that paths of the list are simple.
     *
     * @param pathElement
     *
     * @return <code>true</code> if the vertex specified at constructor is
     * already in the specified path element, <code>false</code> otherwise.
     */
    private boolean containsTargetPreviously(
        RankingPathElement<V, E> pathElement)
    {
        RankingPathElement<V, E> tempPathElement = pathElement;
        while (tempPathElement.getPrevEdge() != null) {
            if (tempPathElement.getVertex() == this.vertex) {
                return true;
            } else {
                tempPathElement = tempPathElement.getPrevPathElement();
            }
        }
        return false;
    }

    private boolean isAlreadyAdded(RankingPathElement<V, E> pathElement)
    {
        for (int i = 0; i <= (size() - 1); i++) {
            RankingPathElement<V, E> yPathElement = get(i);
            RankingPathElement<V, E> pathElementToTest = pathElement;
            if (!isDifferent(yPathElement, pathElementToTest)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAlreadyImprovedBythisEdge(
        E edge,
        RankingPathElement<V, E> prevPathElement)
    {
        RankingPathElement<V, E> pathElementToTest = prevPathElement;
        while (pathElementToTest.getPrevEdge() != null) {
            if (pathElementToTest.getPrevEdge() == edge) {
                return true;
            }
            pathElementToTest = pathElementToTest.getPrevPathElement();
        }
        return false;
    }

    /**
     * @param yPathElement
     * @param pathElementToTest
     *
     * @return <code>false</code> if the two paths are equal, <code>true</code>
     * otherwise.
     */
    private boolean isDifferent(
        RankingPathElement<V, E> yPathElement,
        RankingPathElement<V, E> pathElementToTest)
    {
        while (
            ((yPathElement.getPrevEdge() != null)
                || (pathElementToTest.getPrevEdge() != null)))
        {
            if (yPathElement.getPrevEdge() != pathElementToTest.getPrevEdge()) {
                return true;
            } else {
                yPathElement = yPathElement.getPrevPathElement();
                pathElementToTest = pathElementToTest.getPrevPathElement();
            }
        }
        return false;
    }
}

// End RankingPathElementList.java
