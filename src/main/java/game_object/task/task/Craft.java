package game_object.task.task;

import game_object.item.Recipe;
import game_object.item.Item;

import java.util.Map;

public class Craft extends Task {
    private final Recipe recipe;
    private boolean areStacksFilled;

    public Craft(Recipe recipe, boolean isRepeating) {
        super(isRepeating, recipe.getCraftingTime());
        this.recipe = recipe;
    }

    @Override
    public void execute(int work) {
        if (!areStacksFilled){
            refillStacks();
        }

        else super.execute(work);
    }

    private void refillStacks() {
        Map<Item, Integer> ingredients = recipe.getIngredients();
        areStacksFilled = ingredients.entrySet().stream()
                .allMatch(e -> e.getKey().getCurrentAmount() >= e.getValue());

        if (areStacksFilled)
            ingredients.forEach((resource, amount) -> {
                double currentAmount = resource.getCurrentAmount() - amount;
                resource.setCurrentAmount(currentAmount);
            });
    }

    @Override
    public void onDone() {
        recipe.getResult().increase(1);
        refillStacks();
        super.onDone();
    }

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public String getName() {
        return "Making " + recipe.getResult().getName();
    }
}
