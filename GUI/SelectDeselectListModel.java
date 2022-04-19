package GUI;

import javax.swing.*;

public class SelectDeselectListModel extends DefaultListSelectionModel {

    @Override
    public void setSelectionInterval(int index0, int index1) {
        if (index0 == index1) {
            if (isSelectedIndex(index0)) {
                removeSelectionInterval(index0, index0);
                return;
            }
        }
        super.setSelectionInterval(index0, index1);
    }

    @Override
    public void addSelectionInterval(int index0, int index1) {
        if (index0 == index1) {
            if (isSelectedIndex(index0)) {
                removeSelectionInterval(index0, index0);
                return;
            }
            super.addSelectionInterval(index0, index1);
        }

    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
        super.removeSelectionInterval(index0, index1);
    }
}
