package GUI;

import Producers.Android;
import Producers.Producer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class HireListener {
    private final HashMap<Producer, JComponent> communityUIMap;

    HireListener() {
        communityUIMap = new HashMap<>();
    }

    public void setValueAndUpdateUI(Producer hireCommunity,
                                    ShowListener showListener) {
        setValueAndUpdateUI(showListener.getJlist().getSelectedValuesList(),
                hireCommunity, showListener.getCurrentVisibleCommunity(), showListener);
    }

    public void setValueAndUpdateUI(PseudoSpinner pseudoSpinner,
                                    Producer spinnerCommunity,
                                    Producer freeAndroids,
                                    ShowListener showListener) {
        int valueDifference = pseudoSpinner.getPreviousValue() - pseudoSpinner.getCurrentValue();
        Producer hireCommunity, dismissCommunity;
        if (valueDifference < 0) {
            hireCommunity = spinnerCommunity;
            dismissCommunity = freeAndroids;
        } else {
            hireCommunity = freeAndroids;
            dismissCommunity = spinnerCommunity;
        }

        List<Android> selectedGirlsList = new CopyOnWriteArrayList<>
                (dismissCommunity.getAndroids().subList(0, Math.abs(valueDifference)));
        setValueAndUpdateUI(selectedGirlsList, hireCommunity, dismissCommunity, showListener);
    }

    public void setValueAndUpdateUI(List<Android> selectedGirls,
                                    Producer hireCommunity,
                                    Producer dismissCommunity,
                                    ShowListener showListener) {
        ArrayList<Android> hireList = hireCommunity.getAndroids();
        ArrayList<Android> dismissList = dismissCommunity.getAndroids();

        hireList.addAll(selectedGirls);
        dismissList.removeAll(selectedGirls);

        updateUIComponent(communityUIMap.get(hireCommunity), hireList.size());
        updateUIComponent(communityUIMap.get(dismissCommunity), dismissList.size());

        showListener.reInitJList(hireList, dismissList);
    }

    private void updateUIComponent(JComponent component, int value) {
        if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            label.setText(String.valueOf(value));
        } else if (component instanceof PseudoSpinner.UI) {
            PseudoSpinner.UI pseudoSpinnerUI = (PseudoSpinner.UI) component;
            pseudoSpinnerUI.remoteUIUpdate(value);
        }
    }

    public HashMap<Producer, JComponent> getCommunityUIMap() {
        return communityUIMap;
    }
}
