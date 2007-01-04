package org.jgrapht.experimental.alg;

import java.util.*;

public interface ExactAlgorithm<ResultType, V>
{
    ResultType getResult(Map<V, Object> optionalData);
}
