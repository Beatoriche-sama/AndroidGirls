package game_object.task.task;


public abstract class Task {
    private boolean isActive;
    private final boolean isRepeating;
    private double workDone;
    private final int workMax;

    protected Task(boolean isRepeating, int workMax) {
        this.isRepeating = isRepeating;
        this.workMax = workMax;
        this.isActive = false;
    }

    public void execute(int work) {
        if (isMaxed()) {
            onDone();
        }
        else {
            workDone = Math.min(workDone + work, workMax);
        }
    }

    public void onDone() {
        if (isRepeating) workDone = 0;
        else setActive(false);
    }

    public abstract String getName();

    private boolean isMaxed() {
        return workDone == workMax;
    }


    public void setActive(boolean active) {
        isActive = active;
    }


    public int getProgress() {
        return (int) workDone;
    }


    public boolean isDone() {
        return !isRepeating && isMaxed();
    }


    public boolean isActive() {
        return isActive;
    }

    public int getMaxProgress() {
        return workMax;
    }
}
