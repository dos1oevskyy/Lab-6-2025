package functions.basic;

public class Tan extends TrigonometricFunction {
    @Override
    public double getFunctionValue(double x) {
        double cosX = Math.cos(x);
        double epsilon = 1e-14;
        if (Math.abs(cosX) <= epsilon) {
            return Double.NaN;
        }
        return Math.tan(x);
    }
}