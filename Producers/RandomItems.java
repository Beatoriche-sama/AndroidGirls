package Producers;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

public class RandomItems implements ProduceMethod {
    Random rand = new Random();

    public void execute(ArrayList<Resource> resources, double unionPower) {
        int resourcesListSize = resources.size();
        int maxValues = rand.nextInt(resourcesListSize + 1);

        IntStream.range(0, maxValues).forEach(element -> {
            Resource resource = resources.get(rand.nextInt(resourcesListSize));
            double newValue = resource.getValue() + (unionPower / resource.getDifficulty());
            resource.setValue(newValue);
        });
    }
}
