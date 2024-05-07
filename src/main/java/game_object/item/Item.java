package game_object.item;

import game_object.GameObject;

public class Item extends GameObject {
    private double currentAmount;
    private final int maxAmount;
    private final ITEM_TYPE resourceType;
    private int miningTime;

    public Item(int maxAmount, String name,
                ITEM_TYPE resourceType) {
        super(name);
        this.maxAmount = maxAmount;
        this.resourceType = resourceType;
    }

    public void increase(double value) {
        double supposedValue = currentAmount + value;
        currentAmount = Math.min(supposedValue, maxAmount);
    }

    public void decrease(double value) {
        double supposedValue = currentAmount - value;
        currentAmount = Math.max(supposedValue, 0);
    }

    public synchronized void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public synchronized double getCurrentAmount() {
        return currentAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public ITEM_TYPE getResourceType() {
        return resourceType;
    }

    public void setMiningTime(int miningTime) {
        this.miningTime = miningTime;
    }

    public int getMiningTime() {
        return miningTime;
    }

    public boolean isMaxed(){
        return currentAmount == maxAmount;
    }
}
