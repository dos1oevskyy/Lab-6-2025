package threads;

import functions.Function;

public class Task {
    private Function function;
    private double leftBound;
    private double rightBound;
    private double discretizationStep;
    private int taskCount;
    private double base;

    // Конструктор, геттеры и сеттеры...
    public Task() {}

    public Function getFunction() { return function; }
    public void setFunction(Function function) { this.function = function; }

    public double getLeftBound() { return leftBound; }
    public void setLeftBound(double leftBound) { this.leftBound = leftBound; }

    public double getRightBound() { return rightBound; }
    public void setRightBound(double rightBound) { this.rightBound = rightBound; }

    public double getDiscretizationStep() { return discretizationStep; }
    public void setDiscretizationStep(double discretizationStep) { this.discretizationStep = discretizationStep; }

    public int getTaskCount() { return taskCount; }
    public void setTaskCount(int taskCount) { this.taskCount = taskCount; }

    public double getBase() { return base; }
    public void setBase(double base) { this.base = base; }
}
