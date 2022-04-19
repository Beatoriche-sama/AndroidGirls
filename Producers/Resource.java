package Producers;

import java.io.Serializable;
import java.util.Map;

public class Resource implements Serializable {
    String resourceName;
    int difficulty;
    double value;
    Map<Resource, Integer> sourcePairs;
    boolean isDiscovered;

    public Resource(String resourceName){
        this.resourceName = resourceName;
    }

    public Resource() {
    }

    public Resource setSource(Map<Resource, Integer> sourcePairs) {
        this.sourcePairs = sourcePairs;
        return this;
    }

    public Map<Resource, Integer> getSourcePairs() {
        return sourcePairs;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public Resource setDifficulty(int difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDiscovered(boolean discovered) {
        isDiscovered = discovered;
    }

    public boolean isDiscovered() {
        return isDiscovered;
    }
}
