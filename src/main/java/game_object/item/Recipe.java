package game_object.item;

import java.util.Map;

public class Recipe extends Item {
    private final Map<Item, Integer> ingredients;
    private final Item result;
    private final int craftingTime;
    private Recipe requirement;

    public Recipe(Map<Item, Integer> ingredients,
                  Item result, int craftingTime) {
        super(1, result.getName() + " recipe", ITEM_TYPE.BLUEPRINT);
        this.ingredients = ingredients;
        this.result = result;
        this.craftingTime = craftingTime;
    }

    public Map<Item, Integer> getIngredients() {
        return ingredients;
    }

    public Item getResult() {
        return result;
    }

    public int getCraftingTime() {
        return craftingTime;
    }

    public void setRequirement(Recipe requirement) {
        this.requirement = requirement;
    }

    public Recipe getRequirement() {
        return requirement;
    }

    public boolean isReadyToLearn() {
        return !isMaxed()
                && (requirement == null || requirement.isMaxed());
    }
}
