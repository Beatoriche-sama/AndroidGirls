package app;

import game_object.Location;
import game_object.item.Item;
import game_object.item.Recipe;
import game_object.item.entity.Factory;
import game_object.item.entity.android.Android;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private static final Data instance = new Data();
    private final List<Recipe> recipes;
    private final List<Item> items;
    private final List<Android> androids;
    private final ArrayList<Factory> factories;
    private final ArrayList<Location> locations;

    private Data() {
        this.recipes = new ArrayList<>();
        this.items = new ArrayList<>();
        this.androids = new ArrayList<>();
        this.factories = new ArrayList<>();
        this.locations = new ArrayList<>();
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public List<Item> getItems() {
        return items;
    }

    public List<Android> getAndroids() {
        return androids;
    }

    public ArrayList<Factory> getFactories() {
        return factories;
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public static Data getInstance() {
        return instance;
    }
}
