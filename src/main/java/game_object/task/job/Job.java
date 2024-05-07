package game_object.task.job;

import app.JobManager;
import game_object.GameObject;
import game_object.item.entity.android.Android;
import game_object.item.entity.android.Level;
import game_object.item.entity.android.STAT;
import game_object.task.task.Task;

import java.util.List;

public class Job extends GameObject implements IJob {
    private final Task task;
    private final List<STAT> requiredStats;
    private final int expModification;

    public Job(Task task, List<STAT> requiredStats) {
        this.task = task;
        this.requiredStats = requiredStats;
        this.expModification = task.getMaxProgress() / 2;
    }

    public void accept(Android android) {
        if (task.isDone()) {
            JobManager.getInstance().removeJob(this);
            android.setCurrentJob(null);
        } else if (!task.isActive()) {
            android.setCurrentJob(null);
        }

        List<Level> neededLevels = requiredStats
                .stream().map(android::getProgress).toList();

        int sumSkill = neededLevels.stream()
                .mapToInt(Level::getCurrentLevel).sum();

        task.execute(sumSkill);
        /*neededProgresses.forEach(progress
                -> progress.gainExp(expModification));*/
    }

    public List<STAT> getRequiredStats() {
        return requiredStats;
    }

    @Override
    public String getName() {
        return task.getName();
    }

    public Task getTask() {
        return task;
    }
}
