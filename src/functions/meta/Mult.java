package functions.meta;

import functions.Function;

public class Mult implements Function {
    private final Function f1;
    private final Function f2;

    public Mult(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    @Override
    public double getLeftDomainBorder() {
        return Math.max(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
    }
    @Override
    public double getRightDomainBorder() {
        return Math.min(f1.getRightDomainBorder(), f2.getRightDomainBorder());
    }
    @Override
    public double getFunctionValue(double x) {
        double left = getLeftDomainBorder();
        double right = getRightDomainBorder();

        if (left > right) {
            throw new IllegalArgumentException("Области определения функций не пересекаются");
        }
        if (x < left || x > right) {
            throw new IllegalArgumentException("x находится вне области определения функции");
        }
        return f1.getFunctionValue(x) * f2.getFunctionValue(x);
    }
}
// h(x) = f(x) * g(x)