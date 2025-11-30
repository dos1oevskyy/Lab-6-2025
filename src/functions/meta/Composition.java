package functions.meta;

import functions.Function;

public class Composition implements Function {
    private final Function outer; // внешняя функция
    private final Function inner; // внутренняя функция

    public Composition(Function outer, Function inner) {
        this.outer = outer;
        this.inner = inner;
    }

    @Override
    public double getLeftDomainBorder() {
        return inner.getLeftDomainBorder();
    }
    @Override
    public double getRightDomainBorder() {
        return inner.getRightDomainBorder();
    }
    @Override
    public double getFunctionValue(double x) {
        double left = getLeftDomainBorder();
        double right = getRightDomainBorder();
        if (x < left || x > right) {
            throw new IllegalArgumentException("x = " + x + " находится вне области определения функции [" + left + ", " + right + "]");
        }

        double innerValue = inner.getFunctionValue(x);
        double outerLeft = outer.getLeftDomainBorder();
        double outerRight = outer.getRightDomainBorder();
        if (innerValue < outerLeft || innerValue > outerRight) {
            throw new IllegalArgumentException("Значение " + innerValue + " внутренней функции находится вне области определения внешней функции " +
                    "[" + outerLeft + ", " + outerRight + "]");
        }
        return outer.getFunctionValue(innerValue);
    }
}
// h(x) = f(g(x))