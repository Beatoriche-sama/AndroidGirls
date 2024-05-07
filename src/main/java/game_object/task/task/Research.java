package game_object.task.task;

import game_object.item.Recipe;

public class Research extends Task {
    private final Recipe recipe;

    public Research(Recipe recipe) {
        super(false, recipe.getMiningTime());
        this.recipe = recipe;
    }

    @Override
    public void onDone() {
        super.onDone();
        recipe.setCurrentAmount(1);
    }

    @Override
    public String getName() {
        return "Researching " + recipe.getResult().getName();
    }
}
