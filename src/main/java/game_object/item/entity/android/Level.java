package game_object.item.entity.android;

public class Level {
    private int currentExp;
    private int maxExp;
    private int currentLevel;

    public Level() {
        this.currentLevel = 0;
    }

    public Level(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void gainExp(int modification) {
        currentExp += currentLevel + 100 * modification;
        if (currentExp >= maxExp) levelUp();
    }

    private void levelUp() {
        maxExp = (int) (currentLevel * 100 * 1.25);
        currentLevel += 1;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }
}
