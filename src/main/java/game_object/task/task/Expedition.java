package game_object.task.task;

import game_object.Location;

public class Expedition extends Task {

    private final Location location;

    public Expedition(Location location) {
        super(true, location.getTimeToExplore());
        this.location = location;
    }

    @Override
    public void onDone() {
        super.onDone();
        location.getTreasures().forEach(treasure
                -> treasure.increase(1));
    }

    @Override
    public String getName() {
        return "Exploring " + location;
    }
}
