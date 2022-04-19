package Main;

public class GameEvent {
    private final boolean condition;
    private final Runnable result;
    private boolean isCompleted;

    public GameEvent(boolean condition, Runnable result) {
        this.condition = condition;
        this.result = result;
    }

    public void checkAndStart() {
        if (!condition) return;
        isCompleted = true;
        result.run();
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
