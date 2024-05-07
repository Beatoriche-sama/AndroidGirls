package game_object.item.entity;

import game_object.item.ITEM_TYPE;
import game_object.item.Item;

public abstract class Entity extends Item {
    private final ITEM_TYPE fuelType;
    private Item currentFuel;
    private double fuelConsumption;
   // private boolean isAlive;

    public Entity(String name, ITEM_TYPE fuelType) {
        super(1, name, ITEM_TYPE.METAL);
        this.fuelType = fuelType;
    }

    public void work() {
        boolean isFuelStocked =  currentFuel != null
                && currentFuel.getCurrentAmount() != 0;
        //boolean isAbleToWork = isAlive && isFuelStocked;

        if (!isFuelStocked) return;
        perform();
    }

    public boolean setFuel(Item fuel) {
        if (fuel.getResourceType() != this.fuelType)
            return false;
        else {
            this.currentFuel = fuel;
            return true;
        }
    }

    protected abstract void perform();

    public void setFuelConsumption(double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public double getFuelConsumption() {
        return fuelConsumption;
    }

    public ITEM_TYPE getFuelType() {
        return fuelType;
    }

    public Item getCurrentFuel() {
        return currentFuel;
    }

    /*public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public boolean isAlive() {
        return isAlive;
    }*/
}
