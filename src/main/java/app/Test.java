package app;

import game_object.Location;
import game_object.item.ITEM_TYPE;
import game_object.item.Item;
import game_object.item.Recipe;
import game_object.item.entity.Factory;
import game_object.item.entity.android.Android;

import java.util.*;

public class Test {
    private final Data data;
    private Factory chocolateFactory;
    private Item chocolate, chocoBean, coal, electricity;


    protected Test() {
        this.data = Data.getInstance();

        initResources();
        initAndroidTest();
        initChocolateFactoryTest();
        initDesertExpedition();
        initRecipes();
    }

    private void initRecipes() {
        Recipe first = new Recipe(null, chocoBean, 4);
        first.setMiningTime(5);

        Map<Item, Integer> cFIngredients = new HashMap<>();
        cFIngredients.put(chocoBean, 2);
        Recipe second = new Recipe(cFIngredients, chocolateFactory, 4);
        second.setMiningTime(10);
        second.setRequirement(first);

        List<Recipe> recipes = data.getRecipes();
        recipes.add(first);
        recipes.add(second);
    }

    private void initResources() {
        this.electricity = new Item(
                100, "Electricity", ITEM_TYPE.ELECTRICITY);
        electricity.setCurrentAmount(100);

        this.coal = new Item(100, "Coal", ITEM_TYPE.MINERAL);
        coal.setCurrentAmount(90);

        this.chocoBean = new Item(100,
                "Choco beans", ITEM_TYPE.ORGANIC);
        chocoBean.setCurrentAmount(50);

        this.chocolate = new Item(100,
                "Chocolate", ITEM_TYPE.ORGANIC);

        data.getItems().addAll(
                List.of(electricity, coal, chocoBean, chocolate));
    }

    private void initChocolateFactoryTest() {
        Map<Item, Integer> ingredients = new HashMap<>();
        ingredients.put(chocoBean, 2);
        Recipe chocolateRecipe = new Recipe(
                ingredients, chocolate, 5);

        Factory chocolateFactory = new Factory(
                "Chocolate factory", ITEM_TYPE.MINERAL, chocolateRecipe);
        chocolateFactory.setPower(1);
        chocolateFactory.setCurrentAmount(1);
        chocolateFactory.setFuel(coal);
        chocolateFactory.setFuelConsumption(2);

        this.chocolateFactory = chocolateFactory;
        data.getFactories().add(chocolateFactory);
    }

    private void initDesertExpedition() {
        ArrayList<Item> treasures = new ArrayList<>(
                Collections.singletonList(coal));
        Location desert = new Location("Desert", treasures, 4);

        data.getLocations().add(desert);
    }

    private void initAndroidTest() {
        String[] names = new String[]{"Elly", "Vivi", "Rina", "Miffy"};
        List<Android> androids = data.getAndroids();

        Arrays.stream(names).forEach(name -> {
            Android android = new Android(name);
            android.setFuelConsumption(0.2);
            android.setFuel(electricity);
            androids.add(android);
        });
    }
}
