package threads;

import functions.basic.Log;
import java.util.Random;

public class Generator extends Thread {
    private final Task task;
    private final Semaphore semaphore;
    private final Random random = new Random();

    public Generator(Task task, Semaphore semaphore) {
        this.task = task;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTaskCount(); i++) {
                // Проверяем, не прервали ли нас
                if (isInterrupted()) {
                    System.out.println("Generator: меня прервали, выхожу!");
                    return;
                }

                // Генерируем параметры
                double base = 1 + random.nextDouble() * 9;
                double leftBound = 1 + random.nextDouble() * 99;
                double rightBound = 100 + random.nextDouble() * 100;
                double step = 0.1 + random.nextDouble() * 0.9;

                Log logFunction = new Log(base);

                // Используем семафор вместо synchronized
                semaphore.beginWrite();
                try {
                    task.setFunction(logFunction);
                    task.setLeftBound(leftBound);
                    task.setRightBound(rightBound);
                    task.setDiscretizationStep(step);
                    task.setBase(base);

                    // Вывод для отладки (можно убрать)
                    System.out.printf("Generator: создал задание %d\n", i + 1);
                } finally {
                    semaphore.endWrite();
                }

                // Небольшая пауза
                Thread.sleep(10);
            }
        } catch (InterruptedException e) {
            System.out.println("Generator: прервали во время сна, выхожу!");
            Thread.currentThread().interrupt();
        }
    }
}
