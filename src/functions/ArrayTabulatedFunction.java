package functions;

import java.io.Serializable;

public class ArrayTabulatedFunction implements TabulatedFunction, Serializable {
    // Поля класса
    private int pointsCount;
    private FunctionPoint[] points;

    // Конструкторы
    public ArrayTabulatedFunction(double leftX, double rightX, int pointsCount) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой границы: " + leftX + " >= " + rightX);
        }
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество баллов должно быть не менее 2: " + pointsCount);
        }

        this.pointsCount = pointsCount;
        this.points = new FunctionPoint[pointsCount + 5];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, 0.0);
        }
    }
    public ArrayTabulatedFunction(double leftX, double rightX, double[] values) {
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой границы: " + leftX + " >= " + rightX);
        }
        if (values.length < 2) {
            throw new IllegalArgumentException("Количество баллов должно быть не менее 2: " + values.length);
        }

        this.pointsCount = values.length;
        this.points = new FunctionPoint[pointsCount + 5];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, values[i]);
        }
    }
    public ArrayTabulatedFunction(FunctionPoint[] points) {
        if (points.length < 2) {
            throw new IllegalArgumentException("Требуется как минимум 2 точки");
        }
        for (int i = 1; i < points.length; i++) {
            if (points[i].getX() <= points[i - 1].getX()) {
                throw new IllegalArgumentException("Точки должны быть упорядочены по возрастанию X");
            }
        }

        this.pointsCount = points.length;
        this.points = new FunctionPoint[pointsCount + 5];
        for (int i = 0; i < pointsCount; i++) {
            this.points[i] = new FunctionPoint(points[i]);
        }
    }

    // Реализация методов
    @Override
    public double getLeftDomainBorder() {
        return points[0].getX();
    }
    @Override
    public double getRightDomainBorder() {
        return points[pointsCount - 1].getX();
    }
    @Override
    public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder())
            return Double.NaN;

        final double EPSILON = 1e-10;

        for (int i = 0; i < pointsCount - 1; i++) {
            double x1 = points[i].getX();
            double x2 = points[i + 1].getX();

            if (Math.abs(x - x1) < EPSILON) {
                return points[i].getY();
            }
            if (Math.abs(x - x2) < EPSILON) {
                return points[i + 1].getY();
            }

            if (x >= x1 && x <= x2) {
                double y1 = points[i].getY();
                double y2 = points[i + 1].getY();
                return y1 + ((y2 - y1) * (x - x1)) / (x2 - x1);
            }
        }

        if (Math.abs(x - points[pointsCount - 1].getX()) < EPSILON) {
            return points[pointsCount - 1].getY();
        }

        return Double.NaN;
    }
    @Override
    public int getPointsCount() {
        return pointsCount;
    }
    @Override
    public FunctionPoint getPoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс: " + index + ", Количество: " + pointsCount);
        }
        return new FunctionPoint(points[index]);
    }
    @Override
    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс: " + index + ", Количество: " + pointsCount);
        }

        if (index > 0 && point.getX() <= points[index - 1].getX()) {
            throw new InappropriateFunctionPointException("Точка x=" + point.getX() + " должно быть больше предыдущей точки x=" + points[index - 1].getX());
        }
        if (index < pointsCount - 1 && point.getX() >= points[index + 1].getX()) {
            throw new InappropriateFunctionPointException("Точка x=" + point.getX() + " должно быть меньше следующей точки x=" + points[index + 1].getX());
        }

        points[index] = new FunctionPoint(point);
    }
    @Override
    public double getPointX(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс: " + index + ", Количество: " + pointsCount);
        }
        return points[index].getX();
    }
    @Override
    public void setPointX(int index, double x) throws InappropriateFunctionPointException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс: " + index + ", Количество: " + pointsCount);
        }

        if (index > 0 && x <= points[index - 1].getX()) {
            throw new InappropriateFunctionPointException("Точка x= " + x + " должно быть больше предыдущей точки x= " + points[index - 1].getX());
        }
        if (index < pointsCount - 1 && x >= points[index + 1].getX()) {
            throw new InappropriateFunctionPointException("Точка x= " + x + " должно быть меньше следующей точки x= " + points[index + 1].getX());
        }

        points[index].setX(x);
    }
    @Override
    public double getPointY(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс: " + index + ", Количество: " + pointsCount);
        }
        return points[index].getY();
    }
    @Override
    public void setPointY(int index, double y) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс: " + index + ", Количеств: " + pointsCount);
        }
        points[index].setY(y);
    }
    @Override
    public void deletePoint(int index) {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Индекс: " + index + ", Количество: " + pointsCount);
        }

        if (pointsCount < 3) {
            throw new IllegalStateException("Невозможно удалить точку: требуется минимум 2 точки, текущая: " + pointsCount);
        }

        System.arraycopy(points, index + 1, points, index, pointsCount - index - 1);
        pointsCount--;
    }
    @Override
    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        int insertIndex = 0;
        while (insertIndex < pointsCount && points[insertIndex].getX() < point.getX()) {
            insertIndex++;
        }

        if (insertIndex < pointsCount && Math.abs(points[insertIndex].getX() - point.getX()) < Double.MIN_VALUE) {
            throw new InappropriateFunctionPointException("Точка с х= " + point.getX() + " уже существует по индексу " + insertIndex);
        }

        if (pointsCount >= points.length) {
            FunctionPoint[] newPoints = new FunctionPoint[points.length + 5];
            System.arraycopy(points, 0, newPoints, 0, pointsCount);
            points = newPoints;
        }

        System.arraycopy(points, insertIndex, points, insertIndex + 1, pointsCount - insertIndex);
        points[insertIndex] = new FunctionPoint(point);
        pointsCount++;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < pointsCount; i++) {
            sb.append("(").append(points[i].getX())
                    .append("; ").append(points[i].getY()).append(")");
            if (i < pointsCount - 1) {
                sb.append(", ");
            }
        }
        sb.append("}");
        return sb.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;

        TabulatedFunction other = (TabulatedFunction) o;

        // Быстрая проверка для ArrayTabulatedFunction
        if (o instanceof ArrayTabulatedFunction) {
            ArrayTabulatedFunction otherArray = (ArrayTabulatedFunction) o;

            if (this.pointsCount != otherArray.pointsCount) return false;

            // Прямое сравнение массивов точек
            for (int i = 0; i < pointsCount; i++) {
                if (!this.points[i].equals(otherArray.points[i])) {
                    return false;
                }
            }
            return true;
        }

        // Общий случай для любого TabulatedFunction
        if (this.getPointsCount() != other.getPointsCount()) return false;

        for (int i = 0; i < pointsCount; i++) {
            FunctionPoint thisPoint = this.getPoint(i);
            FunctionPoint otherPoint = other.getPoint(i);

            if (!thisPoint.equals(otherPoint)) {
                return false;
            }
        }
        return true;
    }
    @Override
    public int hashCode() {
        int hash = pointsCount; // Начинаем с количества точек

        for (int i = 0; i < pointsCount; i++) {
            // Комбинируем хэш текущей точки с общим хэшем через XOR
            hash ^= points[i].hashCode();
        }

        return hash;
    }
    @Override
    public Object clone() {
        try {
            ArrayTabulatedFunction cloned = (ArrayTabulatedFunction) super.clone();

            // Глубокое копирование массива точек
            cloned.points = new FunctionPoint[this.points.length];
            for (int i = 0; i < this.pointsCount; i++) {
                cloned.points[i] = (FunctionPoint) this.points[i].clone();
            }

            // pointsCount примитивный тип - копируется по значению
            cloned.pointsCount = this.pointsCount;

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Клонирование не поддерживается", e);
        }
    }
}