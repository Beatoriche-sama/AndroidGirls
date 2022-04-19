package GUI;

import Producers.Android;
import Producers.Producer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class ShowListener {
    private Producer currentVisibleCommunity;
    private ArrayList<Android> currentVisibleList;
    private JButton nextButton, previousButton, lastClickedShowButton;
    private final JLabel selectedListLabel;
    private final int maximumElementsPage;
    private int startIndex;
    private final JList<Android> jlist;
    private final DefaultListModel<Android> jlistModel;


    ShowListener(int maximumElementsPage,
                 JList<Android> jList,
                 JLabel selectedListLabel) {
        this.maximumElementsPage = maximumElementsPage;
        this.jlist = jList;
        this.jlistModel = (DefaultListModel<Android>) jList.getModel();
        this.selectedListLabel = selectedListLabel;
        this.startIndex = 0;
    }

    private void initJList(int begin, int end) {
        if (!jlistModel.isEmpty()) jlistModel.removeAllElements();

        if (!currentVisibleList.isEmpty()) {
            IntStream.rangeClosed(begin, end).forEach(index ->
                    jlistModel.addElement(currentVisibleList.get(index)));
        }
        startIndex = begin;
        if (previousButton != null) previousButton.setEnabled(begin != 0);
        if (nextButton != null) nextButton.setEnabled(begin + jlistModel.size() < currentVisibleList.size());
    }

    public void showJList(JButton clickedButton,
                          Producer communityToShow,
                          String selectedItem) {
        clickedButton.setEnabled(false);
        if (lastClickedShowButton != null)
            lastClickedShowButton.setEnabled(true);
        lastClickedShowButton = clickedButton;

        selectedListLabel.setText(selectedItem);
        currentVisibleCommunity = communityToShow;
        currentVisibleList = communityToShow.getAndroids();

        int end = Math.min(maximumElementsPage - 1, currentVisibleList.size() - 1);
        initJList(0, end);
    }

    public void installNextButtonListener(JButton nextButton) {
        nextButton.addActionListener(e -> {
            int newStartIndex = startIndex + maximumElementsPage;
            int newLastIndex = Math.min(currentVisibleList.size() - 1,
                    newStartIndex + maximumElementsPage - 1);
            initJList(newStartIndex, newLastIndex);
        });
        this.nextButton = nextButton;
    }

    public void installPreviousButtonListener(JButton previousButton) {
        previousButton.addActionListener(e -> {
            int newStartIndex = startIndex - maximumElementsPage;
            int newLastIndex = startIndex - 1;
            initJList(newStartIndex, newLastIndex);
        });
        this.previousButton = previousButton;
    }

    public void reInitJList(ArrayList<Android> hireList,
                            ArrayList<Android> dismissList) {

        if (currentVisibleList != dismissList
                && currentVisibleList != hireList) return;

        if (startIndex >= currentVisibleList.size()) {
            startIndex = currentVisibleList.size() < maximumElementsPage ?
                    0 : startIndex - maximumElementsPage;
        }

        initJList(startIndex, Math.min(currentVisibleList.size() - 1,
                startIndex + maximumElementsPage - 1));
    }

    public JList<Android> getJlist() {
        return jlist;
    }

    public ArrayList<Android> getSelectedAndroidsList() {
        return currentVisibleList;
    }

    public Producer getCurrentVisibleCommunity() {
        return currentVisibleCommunity;
    }
}
