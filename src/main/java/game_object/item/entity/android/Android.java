package game_object.item.entity.android;

import app.JobManager;
import game_object.task.job.Job;
import game_object.item.ITEM_TYPE;
import game_object.item.entity.Entity;

import java.util.HashMap;
import java.util.Map;

public class Android extends Entity {
    private final JobManager jobManager;
    private Job currentJob;
    private final Map<STAT, Level> stats;

    public Android(String name) {
        super(name, ITEM_TYPE.ELECTRICITY);
        this.jobManager = JobManager.getInstance();
        this.stats = new HashMap<>();

        for (STAT stat : STAT.values()) {
            stats.put(stat, new Level(1));
        }
    }

    @Override
    protected void perform() {
        double fuelConsumption = getFuelConsumption();
        if (currentJob == null) {
            jobManager.findJob(this);
            fuelConsumption = fuelConsumption / 2;
        } else currentJob.accept(this);


        getCurrentFuel().decrease(fuelConsumption);
    }

    public void setCurrentJob(Job currentJob) {
        this.currentJob = currentJob;
    }

    public Job getCurrentJob() {
        return currentJob;
    }

    public Map<STAT, Level> getStats() {
        return stats;
    }

    public Level getProgress(STAT stat) {
        return stats.get(stat);
    }
}
