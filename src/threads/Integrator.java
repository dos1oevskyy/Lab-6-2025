package threads;

import functions.Function;
import functions.Functions;

public class Integrator extends Thread {
    private final Task task;
    private final Semaphore semaphore;

    public Integrator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                // Проверяем прерывание
                if (isInterrupted()) {
                    System.out.println("Integrator: меня прервали, выхожу!");
                    return;
                }

                double leftBound, rightBound, step, base;
                Function function;

                // Используем семафор вместо synchronized
                semaphore.beginRead();
                try {
                    function = task.getFunction();
                    leftBound = task.getLeftBound();
                    rightBound = task.getRightBound();
                    step = task.getDiscretizationStep();
                    base = task.getBase();
                } finally {
                    semaphore.endRead();
                }

                // Проверяем данные
                if (function == null) {
                    System.out.printf("Integrator: задание %d - нет функции\n", i + 1);
                    continue;
                }

                try {
                    // Вычисляем интеграл
                    double integralResult = Functions.integrate(function, leftBound, rightBound, step);

                    // Выводим результат
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

                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println("Integrator: прервали во время сна, выхожу!");
            Thread.currentThread().interrupt();
        }
    }
}
