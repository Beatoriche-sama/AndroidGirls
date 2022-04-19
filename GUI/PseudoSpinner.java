package GUI;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.text.NumberFormat;

public class PseudoSpinner extends JComponent {
    private final GroupPseudoSpinners groupPseudoSpinners;
    private int currentValue, previousValue;
    private final UI ui;

    public PseudoSpinner(GroupPseudoSpinners groupPseudoSpinners, int currentValue) {
        this.groupPseudoSpinners = groupPseudoSpinners;
        groupPseudoSpinners.addPseudoSpinner(this);
        this.currentValue = currentValue;
        this.previousValue = currentValue;
        ui = new UI();
        ui.internalUIUpdate();

        addPropertyChangeListener(e -> update(e.getNewValue(), e.getOldValue()));
    }

    private void update(Object newValue, Object oldValue) {
        this.previousValue = (Integer) oldValue;
        this.currentValue = (Integer) newValue;
        ui.internalUIUpdate();
    }

    public UI getUi() {
        return ui;
    }

    public int getPreviousValue() {
        return previousValue;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    class UI extends JPanel {
        private final JButton nextStepButton, previousStepButton;
        private final JFormattedTextField field;

        private UI() {
            setLayout(new MigLayout("flowx"));
            previousStepButton = createPreviousStepButton();
            nextStepButton = createNextStepButton();
            field = createEditor();
            add(previousStepButton);
            add(field);
            add(nextStepButton);
        }

        private void internalUIUpdate() {
            field.setValue(currentValue);
            previousStepButton.setEnabled(currentValue != 0);
        }

        public void remoteUIUpdate(int newValue) {
            previousValue = currentValue;
            currentValue = newValue;
            internalUIUpdate();
        }

        private JFormattedTextField createEditor() {
            NumberFormat format = NumberFormat.getInstance();
            NumberFormatter formatter = new NumberFormatter(format);
            formatter.setValueClass(Integer.class);
            formatter.setAllowsInvalid(false);

            JFormattedTextField field = new JFormattedTextField(formatter);
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setColumns(4);
            field.addActionListener(e -> {
                int fieldValue = (Integer) field.getValue();
                if (fieldValue == currentValue) return;
                fieldValue = groupPseudoSpinners.getValidValue(fieldValue, currentValue);
                PseudoSpinner.this.firePropertyChange("textField", currentValue, fieldValue);
            });
            return field;
        }

        private JButton createNextStepButton() {
            JButton nextStepButton = new JButton("+");
            nextStepButton.addActionListener(e ->
                    PseudoSpinner.this.firePropertyChange("nextButton",
                    currentValue, currentValue + 1));
            return nextStepButton;
        }

        private JButton createPreviousStepButton() {
            JButton previousStepButton = new JButton("-");
            previousStepButton.addActionListener(e ->
                    PseudoSpinner.this.firePropertyChange("previousButton",
                            currentValue, currentValue - 1));
            return previousStepButton;
        }

        public JButton getNextStepButton() {
            return nextStepButton;
        }
    }
}
