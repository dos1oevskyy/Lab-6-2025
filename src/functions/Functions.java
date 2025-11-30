package functions;

import functions.meta.*;

public class Functions {
    private Functions() {
        throw new AssertionError("Невозможно создать экземпляр класса Functions");
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    public static double integrate(Function function, double a, double b, double step) {
        if (a > b) {
            throw new IllegalArgumentException("Левая граница не может быть больше правой");
        }

        // Проверяем границы области определения
        if (a < function.getLeftDomainBorder() || b > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интегрирования выходит за границы области определения функции");
        }

        if (step <= 0) {
            throw new IllegalArgumentException("Шаг дискретизации должен быть положительным");
        }

        double integral = 0.0;
        double x1 = a;
        double x2;

        // Проходим по всем сегментам
        while (x1 < b) {
            x2 = Math.min(x1 + step, b);
            double y1 = function.getFunctionValue(x1);
            double y2 = function.getFunctionValue(x2);

            // Площадь трапеции
            double segmentArea = (y1 + y2) * (x2 - x1) / 2.0;
            integral += segmentArea;

            x1 = x2;
        }

        return integral;
    }
}