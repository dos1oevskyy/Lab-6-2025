import functions.Functions;
import functions.basic.Log;
import threads.*;

import java.util.Random;

public class Main {
    private static final Random random = new Random();

    public static void main(String[] args) {
        System.out.println("=== ЛАБОРАТОРНАЯ РАБОТА: ИНТЕГРИРОВАНИЕ ФУНКЦИЙ ===");
        System.out.println();

        // Тестируем последовательную версию
        System.out.println("=== ПОСЛЕДОВАТЕЛЬНАЯ ВЕРСИЯ ===");
        nonThread();

        System.out.println("\n\n=== МНОГОПОТОЧНАЯ ВЕРСИЯ ===");
        // Тестируем многопоточную версию
        simpleThreads();

        System.out.println("\n\n=== СЛОЖНАЯ МНОГОПОТОЧНАЯ ВЕРСИЯ (С СЕМАФОРОМ) ===");
        complicatedThreads();
    }

    public static void nonThread() {
        Task task = new Task();
        int taskCount = 100;
        task.setTaskCount(taskCount);

        System.out.println("Выполнение " + taskCount + " заданий:");
        System.out.println("================================================================");
        System.out.println("№   | Основание | От    | До     | Шаг   | Результат");
        System.out.println("----|-----------|-------|--------|-------|------------");

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < taskCount; i++) {
            try {
                double base = 1 + random.nextDouble() * 9;
                double leftBound = 1 + random.nextDouble() * 99;
                double rightBound = 100 + random.nextDouble() * 100;
                double step = 0.1 + random.nextDouble() * 0.9;

                Log logFunction = new Log(base);
                double integralResult = Functions.integrate(logFunction, leftBound, rightBound, step);

                System.out.printf("%3d | %9.3f | %5.1f | %6.1f | %5.3f | %11.6f\n",
                        i + 1, base, leftBound, rightBound, step, integralResult);

            } catch (IllegalArgumentException e) {
                System.out.printf("%3d | %9s | %5s | %6s | %5s | %11s\n",
                        i + 1, "ОШИБКА", "-", "-", "-", e.getMessage().substring(0, Math.min(10, e.getMessage().length())));
                i--;
            }
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("================================================================");
        System.out.println("Все задания завершены!");
        System.out.printf("Время выполнения: %d мс\n", duration);
    }

    public static void simpleThreads() {
        // Создаем объект задания
        Task task = new Task();

        // Устанавливаем количество выполняемых заданий
        int taskCount = 100;
        task.setTaskCount(taskCount);

        System.out.println("Многопоточное выполнение " + taskCount + " заданий:");
        System.out.println("================================================================");
        System.out.println("№   | Основание | От    | До     | Шаг   | Результат");
        System.out.println("----|-----------|-------|--------|-------|------------");

        long startTime = System.currentTimeMillis();

        // Создаем потоки
        Thread generatorThread = new Thread(new SimpleGenerator(task));
        Thread integratorThread = new Thread(new SimpleIntegrator(task));

        // Вариант 1: Генератор имеет высший приоритет
        generatorThread.setPriority(Thread.MAX_PRIORITY);
        integratorThread.setPriority(Thread.MIN_PRIORITY);

        // Вариант 2: Интегратор имеет высший приоритет
        // integratorThread.setPriority(Thread.MAX_PRIORITY);
        // generatorThread.setPriority(Thread.MIN_PRIORITY);

        // Вариант 3: Одинаковые приоритеты
        // generatorThread.setPriority(Thread.NORM_PRIORITY);
        // integratorThread.setPriority(Thread.NORM_PRIORITY);

        System.out.println("Приоритет генератора: " + generatorThread.getPriority());
        System.out.println("Приоритет интегратора: " + integratorThread.getPriority());

        // Запускаем потоки
        generatorThread.start();
        integratorThread.start();

        // Ожидаем завершения потоков
        try {
            generatorThread.join();
            integratorThread.join();
        } catch (InterruptedException e) {
            System.out.println("Основной поток был прерван: " + e.getMessage());
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("================================================================");
        System.out.println("Все многопоточные задания выполнены!");
        System.out.printf("Время выполнения: %d мс\n", duration);
    }

    public static void complicatedThreads() {
        // Создаем объект задания и семафор
        Task task = new Task();
        Semaphore semaphore = new Semaphore();

        int taskCount = 100;
        task.setTaskCount(taskCount);

        System.out.println("СЛОЖНАЯ многопоточная версия с семафором:");
        System.out.println("================================================================");
        System.out.println("№   | Основание | От    | До     | Шаг   | Результат");
        System.out.println("----|-----------|-------|--------|-------|------------");

        // Создаем потоки-наследники
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);

        // ЭКСПЕРИМЕНТЫ С ПРИОРИТЕТАМИ
        // Вариант 1: Генератор имеет высший приоритет
        // generator.setPriority(Thread.MAX_PRIORITY);
        // integrator.setPriority(Thread.MIN_PRIORITY);

        // Вариант 2: Интегратор имеет высший приоритет
        // integrator.setPriority(Thread.MAX_PRIORITY);
        // generator.setPriority(Thread.MIN_PRIORITY);

        // Вариант 3: Одинаковые приоритеты
        generator.setPriority(Thread.NORM_PRIORITY);
        integrator.setPriority(Thread.NORM_PRIORITY);

        System.out.println("Приоритет генератора: " + generator.getPriority());
        System.out.println("Приоритет интегратора: " + integrator.getPriority());

        // Запускаем потоки
        generator.start();
        integrator.start();

        // ПРЕРЫВАНИЕ ПОСЛЕ 50 мс
        try {
            Thread.sleep(50); // Ждем 50 мс
            System.out.println("\nОСНОВНОЙ ПОТОК: ПРЕРЫВАЮ РАБОТУ ПОТОКОВ!");
            generator.interrupt();
            integrator.interrupt();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        //
        try {
            generator.join();
            integrator.join();
        } catch (InterruptedException e) {
            System.out.println("Основной поток прерван!");
            Thread.currentThread().interrupt();
        }

        System.out.println("================================================================");
        System.out.println("Сложная многопоточная версия завершена (возможно, не все задания)");
    }
}