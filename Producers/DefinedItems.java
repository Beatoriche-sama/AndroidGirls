package Producers;

import java.util.ArrayList;
import java.util.Map;

public class DefinedItems implements ProduceMethod{

    public void execute(ArrayList<Resource> resources, double unionPower) {
        resources.forEach(r-> produceResource(r, unionPower));
    }

    private void produceResource(Resource resource, double unionPower){
        if (!isReadyToCraft(resource)) return;
        resource.sourcePairs.forEach((source, valueNeed) -> {
            double previousSourceValue = source.value;
            double currentSourceValue = previousSourceValue -
                    (double) (valueNeed / resource.difficulty);
            source.setValue(currentSourceValue);
        });
        double previousProductValue = resource.value;
        double currentProductValue = previousProductValue + unionPower / resource.difficulty;
        resource.setValue(currentProductValue);
    }

    private static boolean isReadyToCraft(Resource product) {
        Map<Resource, Integer> sources = product.sourcePairs;
        return sources.entrySet().stream().allMatch((entry) -> {
            Resource source = entry.getKey();
            double resValueNow = source.getValue();
            return resValueNow >= entry.getValue();
        });
    }
}
