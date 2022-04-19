package CreatorEngine;

import Producers.Resource;

import javax.swing.*;
import java.util.ArrayList;

public class LinkedCombobox {
    ArrayList<ComboBoxMember> boxes;

    public LinkedCombobox() {
        boxes = new ArrayList<>();
    }

    protected ComboBoxMember createBox() {
        ComboBoxMember box = new ComboBoxMember();
        box.insertItemAt(null, 0);
        boxes.add(box);
        return box;
    }

    protected void fireAction(ComboBoxMember comboBoxMember, Runnable runnable) {
        comboBoxMember.setStateChangesAllowed(true);
        runnable.run();
        comboBoxMember.setStateChangesAllowed(false);
    }

    static class ComboBoxMember extends JComboBox{
        boolean isStateChangesAllowed;

        ComboBoxMember() {
            isStateChangesAllowed = false;
            setRenderer(new CustomListCellRenderer());
        }

        @Override
        protected void fireActionEvent() {
            if (this.hasFocus() | isStateChangesAllowed) {
                super.fireActionEvent();
            }
        }

        protected void setStateChangesAllowed(boolean isStateChangesAllowed) {
            this.isStateChangesAllowed = isStateChangesAllowed;
        }

    }

    protected void updateData(ArrayList resources) {
        boxes.forEach(box -> {
            DefaultComboBoxModel boxModel =
                    (DefaultComboBoxModel<Resource>) box.getModel();
            boxModel.removeAllElements();
            boxModel.addAll(resources);
        });

    }

    protected void addItem(Object object) {
        boxes.forEach(box -> box.addItem(object));
    }

    protected void removeItem(Object object) {
        boxes.forEach(box -> box.addItem(object));

    }

    protected void updateUIData() {
        boxes.forEach(JComboBox::updateUI);
    }
}
