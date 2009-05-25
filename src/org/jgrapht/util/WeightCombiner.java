package org.jgrapht.util;

/**
 * Binary operator for edge weights. There are some prewritten operators.
 */
public interface WeightCombiner
{
    //~ Instance fields --------------------------------------------------------

    /**
     * Sum of weights.
     */
    public WeightCombiner SUM =
        new WeightCombiner() {
            public double combine(double a, double b)
            {
                return a + b;
            }
        };

    /**
     * Minimum weight.
     */
    public WeightCombiner MIN =
        new WeightCombiner() {
            public double combine(double a, double b)
            {
                return Math.min(a, b);
            }
        };

    /**
     * Maximum weight.
     */
    public WeightCombiner MAX =
        new WeightCombiner() {
            public double combine(double a, double b)
            {
                return Math.max(a, b);
            }
        };

    /**
     * First weight.
     */
    public WeightCombiner FIRST =
        new WeightCombiner() {
            public double combine(double a, double b)
            {
                return a;
            }
        };

    /**
     * Second weight.
     */
    public WeightCombiner SECOND =
        new WeightCombiner() {
            public double combine(double a, double b)
            {
                return b;
            }
        };

    //~ Methods ----------------------------------------------------------------

    /**
     * Combines two weights.
     *
     * @param a first weight
     * @param b second weight
     *
     * @return result of the operator
     */
    double combine(double a, double b);
}

// End WeightCombiner.java
