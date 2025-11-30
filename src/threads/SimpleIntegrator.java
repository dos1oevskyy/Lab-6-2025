package threads;

import functions.Function;
import functions.Functions;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                double leftBound, rightBound, step, base;
                Function function;

                // Синхронизируем доступ к объекту Task
                synchronized (task) {
                    // Получаем параметры из задания
                    function = task.getFunction();
                    leftBound = task.getLeftBound();
                    rightBound = task.getRightBound();
                    step = task.getDiscretizationStep();
                    base = task.getBase();
                }

                // Проверяем, что функция не null
                if (function == null) {
                    synchronized (System.out) {
                        System.out.printf("%3d | %9s | %5s | %6s | %5s | %11s\n",
                                i + 1, "ОШИБКА", "-", "-", "-", "NULL");
                    }
                    continue;
                }

                try {
                    // Вычисляем интеграл
                    double integralResult = Functions.integrate(function, leftBound, rightBound, step);

                    // Синхронизируем вывод всей строки
                    synchronized (System.out) {
                        System.out.printf("%3d | %9.3f | %5.1f | %6.1f | %5.3f | %11.6f\n",
                                i + 1, base, leftBound, rightBound, step, integralResult);
                    }

                } catch (IllegalArgumentException e) {
                    synchronized (System.out) {
                        System.out.printf("%3d | %9.3f | %5.1f | %6.1f | %5.3f | %11s\n",
                                i + 1, base, leftBound, rightBound, step, "ОШИБКА");
                    }
                }

                // Небольшая пауза для наглядности
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println("Интегратор был прерван: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}