package functions;

import java.io.*;

public class TabulatedFunctions {
    private TabulatedFunctions() {
        throw new AssertionError("Невозможно создать экземпляр класса TabulatedFunctions");
    }

    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        // Проверка корректности параметров
        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2: " + pointsCount);
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой: " + leftX + " >= " + rightX);
        }

        // Проверка области определения
        double functionLeftBorder = function.getLeftDomainBorder();
        double functionRightBorder = function.getRightDomainBorder();
        if (leftX < functionLeftBorder || rightX > functionRightBorder) {
            throw new IllegalArgumentException("Границы табулирования [" + leftX + ", " + rightX + "] " +
                            "выходят за область определения функции [" + functionLeftBorder + ", " + functionRightBorder + "]"
            );
        }

        // Создание массива значений Y путем вычисления функции в равномерно распределенных точках
        double[] values = new double[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            values[i] = function.getFunctionValue(x);
        }
        return new ArrayTabulatedFunction(leftX, rightX, values);
    }
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) {
        DataOutputStream dataOut = new DataOutputStream(out);
        try {
            int pointsCount = function.getPointsCount();
            dataOut.writeInt(pointsCount);
            for (int i = 0; i < pointsCount; i++) {
                dataOut.writeDouble(function.getPointX(i));
                dataOut.writeDouble(function.getPointY(i));
            }
            dataOut.flush();
        }
        catch (IOException e) {
            throw new RuntimeException("Ошибка при выводе табулированной функции", e);
        }
    }
    public static TabulatedFunction inputTabulatedFunction(InputStream in) {
        DataInputStream dataIn = new DataInputStream(in);
        try {
            int pointsCount = dataIn.readInt();
            if (pointsCount < 2) {
                throw new RuntimeException("Некорректное количество точек: " + pointsCount);
            }
            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                double x = dataIn.readDouble();
                double y = dataIn.readDouble();
                points[i] = new FunctionPoint(x, y);
            }
            return new ArrayTabulatedFunction(points);
        }
        catch (IOException e) {
            throw new RuntimeException("Ошибка при вводе табулированной функции", e);
        }
    }
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) {
        PrintWriter writer = new PrintWriter(new BufferedWriter(out));
        try {
            int pointsCount = function.getPointsCount();
            writer.print(pointsCount);

            for (int i = 0; i < pointsCount; i++) {
                writer.print(" " + function.getPointX(i));
                writer.print(" " + function.getPointY(i));
            }
            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при записи табулированной функции", e);
        }
    }
    public static TabulatedFunction readTabulatedFunction(Reader in) {
        StreamTokenizer tokenizer = new StreamTokenizer(new BufferedReader(in));
        try {
            // Устанавливаем, что числа должны считываться как числа
            tokenizer.resetSyntax();
            tokenizer.wordChars('a', 'z');
            tokenizer.wordChars('A', 'Z');
            tokenizer.wordChars(128 + 32, 255);
            tokenizer.whitespaceChars(' ', ' ');
            tokenizer.whitespaceChars('\n', '\n');
            tokenizer.whitespaceChars('\r', '\r');
            tokenizer.whitespaceChars('\t', '\t');
            tokenizer.parseNumbers();

            if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                throw new RuntimeException("Ожидалось количество точек");
            }
            int pointsCount = (int)tokenizer.nval;
            if (pointsCount < 2) {
                throw new RuntimeException("Некорректное количество точек: " + pointsCount);
            }

            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                    throw new RuntimeException("Ожидалась координата X точки " + i);
                }
                double x = tokenizer.nval;
                if (tokenizer.nextToken() != StreamTokenizer.TT_NUMBER) {
                    throw new RuntimeException("Ожидалась координата Y точки " + i);
                }
                double y = tokenizer.nval;
                points[i] = new FunctionPoint(x, y);
            }
            return new ArrayTabulatedFunction(points);
        }
        catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении табулированной функции", e);
        }
    }
}
