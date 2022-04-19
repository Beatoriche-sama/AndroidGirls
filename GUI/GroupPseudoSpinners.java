package GUI;

import Producers.Android;

import java.util.ArrayList;

public class GroupPseudoSpinners {
    private final ArrayList<PseudoSpinner> pseudoSpinners;
    private final ArrayList<Android> freeAndroids;

    public GroupPseudoSpinners(ArrayList<Android> freeAndroids) {
        this.pseudoSpinners = new ArrayList<>();
        this.freeAndroids = freeAndroids;
    }

    public void addPseudoSpinner(PseudoSpinner pseudoSpinner) {
        pseudoSpinners.add(pseudoSpinner);
    }

    public int getValidValue(int fieldValue, int oldValue){
        int commonValue = getCommonValue();
        int freeListSize = freeAndroids.size();

        boolean overflow =  commonValue + fieldValue > commonValue + freeListSize;
        if (overflow) fieldValue = oldValue + freeListSize;

        return fieldValue;
    }

    private int getCommonValue() {
        return pseudoSpinners.stream()
                .mapToInt(PseudoSpinner::getCurrentValue)
                .sum();
    }

    public ArrayList<PseudoSpinner> getPseudoSpinners() {
        return pseudoSpinners;
    }
}
