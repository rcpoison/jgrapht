package org.jgrapht.graph;

import org.jgrapht.*;
import org.jgrapht.util.*;

import java.io.*;
import java.util.*;

public class GraphUnion<V, E, G extends Graph<V, E>>
        extends AbstractGraph<V, E>
        implements Serializable {

    public static final BinaryOperator<Double> SUM = new BinaryOperator<Double>() {
        public Double operate(Double a, Double b) {
            return a + b;
        }
    };

    public static final BinaryOperator<Double> MAX = new BinaryOperator<Double>() {
        public Double operate(Double a, Double b) {
            return Math.max(a, b);
        }
    };

    public static final BinaryOperator<Double> MIN = new BinaryOperator<Double>() {
        public Double operate(Double a, Double b) {
            return Math.min(a, b);
        }
    };

    public static final BinaryOperator<Double> USE_G1 = new BinaryOperator<Double>() {
        public Double operate(Double a, Double b) {
            return a;
        }
    };

    public static final BinaryOperator<Double> USE_G2 = new BinaryOperator<Double>() {
        public Double operate(Double a, Double b) {
            return b;
        }
    };

    private static final String READ_ONLY = "union of graphs is read-only";

    private G g1;
    private G g2;
    private BinaryOperator<Double> operator;

    public GraphUnion(G g1, G g2, BinaryOperator<Double> operator) {
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
        this.operator = operator;
    }

    public GraphUnion(G g1, G g2) {
        this(g1, g2, SUM);
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
        E res = null;
        if (g1.containsVertex(sourceVertex) && g1.containsVertex(targetVertex)) {
            res = g1.getEdge(sourceVertex, targetVertex);
        }
        if (res == null && g2.containsVertex(sourceVertex) && g2.containsVertex(targetVertex)) {
            res = g2.getEdge(sourceVertex, targetVertex);
        }
        return res;
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
        if (g1.containsEdge(e) && g2.containsEdge(e)) {
            return operator.operate(g1.getEdgeWeight(e), g2.getEdgeWeight(e));
        }
        if (g1.containsEdge(e)) {
            return g1.getEdgeWeight(e);
        }
        if (g2.containsEdge(e)) {
            return g2.getEdgeWeight(e);
        }
        // TODO what should I do then?
        throw new UnsupportedOperationException("not implemented yet");
    }

    public G getG1() {
        return g1;
    }

    public G getG2() {
        return g2;
    }
}
