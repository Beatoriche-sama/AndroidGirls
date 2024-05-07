package game_object;

import game_object.item.Item;

import java.util.List;

public class Location extends GameObject{
    private final List<Item> treasures;
    private final int timeToExplore;

    public Location(String name, List<Item> treasures,
                    int timeToExplore) {
        super(name);
        this.treasures = treasures;
        this.timeToExplore = timeToExplore;
    }

    public int getTimeToExplore() {
        return timeToExplore;
    }

    public List<Item> getTreasures() {
        return treasures;
    }
}
