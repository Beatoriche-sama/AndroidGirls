package app;

import game_object.GameObject;
import game_object.task.job.Job;
import game_object.item.entity.android.Android;
import game_object.item.entity.android.Level;
import game_object.item.entity.android.STAT;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class JobManager {
    private static JobManager instance;
    private final Map<GameObject, Job> jobs;

    private JobManager() {
        instance = this;
        jobs = new HashMap<>();
    }

    public void removeJob(Job job) {
        jobs.entrySet().stream()
                .filter(e -> e.getValue() == job)
                .findFirst().ifPresent(j -> jobs.remove(j.getKey()));
    }

    public Job getRequest(GameObject gameObject,
                          Supplier<Job> jobSupplier) {
        if (jobs.containsKey(gameObject))
            return jobs.get(gameObject);
        else {
            Job job = jobSupplier.get();
            jobs.put(gameObject, job);
            return job;
        }
    }


    public void findJob(Android android) {
        //System.out.println("We're looking for job.");
        List<Job> jobsWithTasks = jobs.values().stream()
                .filter(job-> job.getTask().isActive()).toList();

        if (jobsWithTasks.isEmpty()) return;
        System.out.println("Found available jobs!");

        LinkedHashMap<STAT, Level> rankedMap
                = rankAndroidStats(android.getStats());

        Job matchedJob = jobsWithTasks.stream().sorted((e1, e2) ->
                        getJobCompatibility(e1, rankedMap)
                                - getJobCompatibility(e2, rankedMap))
                .collect(Collectors.toCollection(LinkedList::new))
                .getFirst();

        android.setCurrentJob(matchedJob);
    }

    private int getJobCompatibility(Job job, Map<STAT, Level> map) {
        return job.getRequiredStats().stream()
                .mapToInt(stat -> map.get(stat).getCurrentLevel())
                .sum();
    }

    private LinkedHashMap<STAT, Level> rankAndroidStats(Map<STAT, Level> stats) {
        return stats.entrySet().stream()
                .sorted(Comparator.comparingInt(e -> e.getValue().getCurrentLevel()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (x, y) -> y, LinkedHashMap::new));
    }

    public static JobManager getInstance() {
        return instance == null ?
                new JobManager() : instance;
    }
}
