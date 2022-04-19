package Producers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Function;

public class Producer  implements Serializable {
    private String producerName;
    private final ArrayList<Resource> resources;

    private AndroidUnion androidUnion;
    private Android.StatType statType;
    private ProduceMethod produceMethod;

    private boolean isInvented, isBuilt, isAutoMode;
    private int knowledgeValue;
    private Producer parent;
    private ArrayList<Producer> children;


    public Producer() {
        resources = new ArrayList<>();
        children = new ArrayList<>();
    }

    public boolean isWorkAllowed(){
        boolean isWorkAllowed;
        boolean isWorkersListEmpty = !getAndroids().isEmpty();
        boolean isBuilding = androidUnion instanceof Building;
        if(isBuilding){
            isWorkAllowed = isBuilt;
            if(!isAutoMode) isWorkAllowed = isWorkAllowed && isWorkersListEmpty;
        }else {
            isWorkAllowed = isWorkersListEmpty;
        }
      return isWorkAllowed;
    }
    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void produce(){
        produceMethod.execute(resources, getUnionPower());
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public void setAndroidUnion(AndroidUnion androidUnion) {
        this.androidUnion = androidUnion;
    }

    public AndroidUnion getAndroidUnion() {
        return androidUnion;
    }

    public void setProduceMethod(ProduceMethod produceMethod) {
        this.produceMethod = produceMethod;
    }

    public ProduceMethod getProduceMethod() {
        return produceMethod;
    }

    public Producer getParent() {
        return parent;
    }

    public void setParent(Producer parent) {
        this.parent = parent;
    }

    public Map<Resource, Integer> getCraftMaterials(){
        AndroidUnion union = getAndroidUnion();
        if(union instanceof Building)
            return ((Building) union).getCraftMaterials();
        else return null;
    }

    public Resource getFuel(){
        AndroidUnion union = getAndroidUnion();
        if(union instanceof Building)
            return ((Building) union).getFuel();
        else return null;
    }

    public void setStatType(Android.StatType statType) {
        this.statType = statType;
    }

    public Android.StatType getStatType() {
        return statType;
    }

    public String getProducerName() {
        return producerName;
    }

    protected double getUnionPower() {
        Function<Android, Integer> func = android ->
                android.getStatValue(statType);
        return androidUnion.getPower(func);
    }

    public void setInvented(boolean invented) {
        isInvented = invented;
    }

    public boolean isInvented() {
        return isInvented;
    }

    public void setBuilt(boolean built) {
        isBuilt = built;
    }

    public boolean isBuilt() {
        return isBuilt;
    }

    public void setAutoMode(boolean autoMode) {
        isAutoMode = autoMode;
    }

    public boolean isAutoMode() {
        return isAutoMode;
    }

    public void setKnowledgeValue(int knowledgeValue) {
        this.knowledgeValue = knowledgeValue;
    }

    public int getKnowledgeValue() {
        return knowledgeValue;
    }

    public void hireWorker(Android android) {
        androidUnion.addMember(android);
    }

    public void dismissWorker(Android android) {
        androidUnion.removeMember(android);
    }

    public ArrayList<Android> getAndroids() {
        return androidUnion.getAndroids();
    }

    public ArrayList<Producer> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Producer> children) {
        this.children = children;
    }
}
