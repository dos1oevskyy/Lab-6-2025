package functions.meta;

import functions.Function;

public class Power implements Function {
    private final Function base; // функция основания
    private final double exponent; // функция показателя степени

    public Power(Function base, double exponent) {
        this.base = base;
        this.exponent = exponent;
    }

    @Override
    public double getLeftDomainBorder() {
        return base.getLeftDomainBorder();
    }
    @Override
    public double getRightDomainBorder() {
        return base.getRightDomainBorder();
    }
    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new IllegalArgumentException("x находится вне области определения функции");
        }
        return Math.pow(base.getFunctionValue(x), exponent);
    }
}
// h(x) = [f(x)]^g(x)