package Producers;

import java.util.Map;
import java.util.function.Function;

public class Building extends AndroidUnion{
    int buildingPower;
    private Resource fuel;
    private Map<Resource, Integer> craftMaterials;

    public Building(){
    }

    public void setFuel(Resource fuel) {
        this.fuel = fuel;
    }

    public Resource getFuel() {
        return fuel;
    }

    public void setBuildingPower(int buildingPower) {
        this.buildingPower = buildingPower;
    }

    public int getBuildingPower() {
        return buildingPower;
    }

    public void setCraftMaterials(Map<Resource, Integer> craftMaterials) {
        this.craftMaterials = craftMaterials;
    }

    public Map<Resource, Integer> getCraftMaterials() {
        return craftMaterials;
    }

    @Override
    public int getPower(Function<Android, Integer> function){
        return super.getPower(function) + buildingPower;
    }
}
