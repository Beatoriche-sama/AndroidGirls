package Main;

import Producers.Android;
import Producers.Producer;
import Producers.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public class DataStorage implements Serializable {
    protected Resource knowledge, energy;
    ArrayList<Resource> resources;
    Producer free;
    ArrayList<Producer> producers;

    public DataStorage() {

    }

    public void spendKnowledge(int amount) {
        knowledge.setValue(knowledge.getValue() - amount);
    }

    public double getKnowledgeAmount(){
        return knowledge.getValue();
    }

    public ArrayList<Producer> getSortedProducers(Android.StatType statType) {
        ArrayList<Producer> sortedProducers = new ArrayList<>();
        producers.stream()
                .filter(producer -> producer.getStatType() == statType)
                .forEach(sortedProducers::add);
        return sortedProducers;
    }

    public void setEnergy(Resource energy) {
        this.energy = energy;
    }

    public Resource getEnergy() {
        return energy;
    }

    public void setKnowledge(Resource knowledge) {
        this.knowledge = knowledge;
    }

    public Resource getKnowledge() {
        return knowledge;
    }

    public void setFree(Producer free) {
        this.free = free;
    }

    public Producer getFree() {
        return free;
    }

    public ArrayList<Producer> getProducers() {
        return producers;
    }

    public ArrayList<Resource> getResources() {
        return resources;
    }
}
