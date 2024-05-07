package game_object.item.entity;

import game_object.item.ITEM_TYPE;
import game_object.item.Recipe;
import game_object.task.task.Craft;

public class Factory extends Entity {
    private int power;
    private final Craft craftTask;

    public Factory(String name, ITEM_TYPE fuel, Recipe recipe) {
        super(name, fuel);
        this.craftTask = new Craft(recipe, true);
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getPower() {
        return power;
    }

    public Craft getCraftTask() {
        return craftTask;
    }

    @Override
    protected void perform() {
        //System.out.println("is task active " + craftTask.isActive());
        if (!craftTask.isActive()) return;
        craftTask.execute(power);
        getCurrentFuel().decrease(getFuelConsumption());
    }
}
