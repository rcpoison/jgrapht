package org.jgrapht.experimental.alg;

import java.util.*;

public interface ApproximationAlgorithm<ResultType, V>
{
    ResultType getUpperBound(Map<V, Object> optionalData);
    ResultType getLowerBound(Map<V, Object> optionalData);
    boolean isExact();
}
