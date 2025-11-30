package functions.meta;

import functions.Function;

public class Scale implements Function {
    private final Function f;
    private final double scaleX;
    private final double scaleY;

    public Scale(Function f, double scaleX, double scaleY) {
        this.f = f;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public double getLeftDomainBorder() {
        if (scaleX > 0) {
            return f.getLeftDomainBorder() / scaleX;
        }
        else if (scaleX < 0) {
            return f.getRightDomainBorder() / scaleX;
        }
        else {
            throw new IllegalStateException("Коэффициент масштабирования не может быть равен нулю");
        }
    }
    @Override
    public double getRightDomainBorder() {
        if (scaleX > 0) {
            return f.getRightDomainBorder() / scaleX;
        }
        else if (scaleX < 0) {
            return f.getLeftDomainBorder() / scaleX;
        }
        else {
            throw new IllegalStateException("Коэффициент масштабирования не может быть равен нулю");
        }
    }
    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            throw new IllegalArgumentException("x находится вне области определения функции");
        }
        return scaleY * f.getFunctionValue(x * scaleX);
    }
}
// h(x) = k * f(m * x)