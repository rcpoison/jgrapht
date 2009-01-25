package org.jgrapht.util;

public interface WeightCombiner {

    public WeightCombiner SUM = new WeightCombiner() {
        public double combine(double a, double b) {
            return a + b;
        }
    };

    public WeightCombiner MIN = new WeightCombiner() {
        public double combine(double a, double b) {
            return Math.min(a, b);
        }
    };

    public WeightCombiner MAX = new WeightCombiner() {
        public double combine(double a, double b) {
            return Math.max(a, b);
        }
    };

    public WeightCombiner FIRST = new WeightCombiner() {
        public double combine(double a, double b) {
            return a;
        }
    };

    public WeightCombiner SECOND = new WeightCombiner() {
        public double combine(double a, double b) {
            return b;
        }
    };

    double combine(double a, double b);
}
