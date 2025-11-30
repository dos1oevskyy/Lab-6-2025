package threads;

import functions.basic.Log;
import java.util.Random;

public class SimpleGenerator implements Runnable {
    private final Task task;
    private final Random random = new Random();

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                // Генерируем параметры
                double base = 1 + random.nextDouble() * 9;
                double leftBound = 1 + random.nextDouble() * 99;
                double rightBound = 100 + random.nextDouble() * 100;
                double step = 0.1 + random.nextDouble() * 0.9;

                // Создаем функцию
                Log logFunction = new Log(base);

                // Синхронизируем доступ к объекту Task
                synchronized (task) {
                    // Устанавливаем параметры в задание
                    task.setFunction(logFunction);
                    task.setLeftBound(leftBound);
                    task.setRightBound(rightBound);
                    task.setDiscretizationStep(step);
                    task.setBase(base);
                }

                // Небольшая пауза для наглядности
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println("Генератор был прерван: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}