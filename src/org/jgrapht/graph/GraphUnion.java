package org.jgrapht.graph;

import org.jgrapht.*;

import java.io.*;
import java.util.*;

public class GraphUnion<V, E, G extends Graph<V, E>>
        extends AbstractGraph<V, E>
        implements Serializable {

    private static final String READ_ONLY = "union of graphs is read-only";

    private G g1;
    private G g2;

    public GraphUnion(G g1, G g2) {
        if (g1 == null) {
            throw new NullPointerException("g1 is null");
        }
        if (g2 == null) {
            throw new NullPointerException("g2 is null");
        }
        if (g1 == g2) {
            throw new IllegalArgumentException("g1 is equal to g2");
        }
        this.g1 = g1;
        this.g2 = g2;
    }

    public Set<E> getAllEdges(V sourceVertex, V targetVertex) {
        Set<E> res = new HashSet<E>();
        if (g1.containsVertex(sourceVertex) && g1.containsVertex(targetVertex)) {
            res.addAll(g1.getAllEdges(sourceVertex, targetVertex));
        }
        if (g2.containsVertex(sourceVertex) && g2.containsVertex(targetVertex)) {
            res.addAll(g2.getAllEdges(sourceVertex, targetVertex));
        }
        return Collections.unmodifiableSet(res);
    }

    public E getEdge(V sourceVertex, V targetVertex) {
        if (g1.containsEdge(sourceVertex, targetVertex)) {
            return g1.getEdge(sourceVertex, targetVertex);
        }
        if (g2.containsEdge(sourceVertex, targetVertex)) {
            return g2.getEdge(sourceVertex, targetVertex);
        }
        return null;
    }

    public EdgeFactory<V, E> getEdgeFactory() {
        throw new UnsupportedOperationException(READ_ONLY);
    }

    public E addEdge(V sourceVertex, V targetVertex) {
        throw new UnsupportedOperationException(READ_ONLY);
    }

    public boolean addEdge(V sourceVertex, V targetVertex, E e) {
        throw new UnsupportedOperationException(READ_ONLY);
    }

    public boolean addVertex(V v) {
        throw new UnsupportedOperationException(READ_ONLY);
    }

    public boolean containsEdge(E e) {
        return g1.containsEdge(e) || g2.containsEdge(e);
    }

    public boolean containsVertex(V v) {
        return g1.containsVertex(v) || g2.containsVertex(v);
    }

    public Set<E> edgeSet() {
        Set<E> res = new HashSet<E>();
        res.addAll(g1.edgeSet());
        res.addAll(g2.edgeSet());
        return Collections.unmodifiableSet(res);
    }

    public Set<E> edgesOf(V vertex) {
        Set<E> res = new HashSet<E>();
        if (g1.containsVertex(vertex)) {
            res.addAll(g1.edgesOf(vertex));
        }
        if (g2.containsVertex(vertex)) {
            res.addAll(g2.edgesOf(vertex));
        }
        return Collections.unmodifiableSet(res);
    }

    public E removeEdge(V sourceVertex, V targetVertex) {
        throw new UnsupportedOperationException(READ_ONLY);
    }

    public boolean removeEdge(E e) {
        throw new UnsupportedOperationException(READ_ONLY);
    }

    public boolean removeVertex(V v) {
        throw new UnsupportedOperationException(READ_ONLY);
    }

    public Set<V> vertexSet() {
        Set<V> res = new HashSet<V>();
        res.addAll(g1.vertexSet());
        res.addAll(g2.vertexSet());
        return Collections.unmodifiableSet(res);
    }

    public V getEdgeSource(E e) {
        if (g1.containsEdge(e)) {
            return g1.getEdgeSource(e);
        }
        if (g2.containsEdge(e)) {
            return g2.getEdgeSource(e);
        }
        return null;
    }

    public V getEdgeTarget(E e) {
        if (g1.containsEdge(e)) {
            return g1.getEdgeTarget(e);
        }
        if (g2.containsEdge(e)) {
            return g2.getEdgeTarget(e);
        }
        return null;
    }

    public double getEdgeWeight(E e) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public G getG1() {
        return g1;
    }

    public G getG2() {
        return g2;
    }
}
