package UI.updatable_progress;

import game_object.task.task.Task;
import net.miginfocom.swing.MigLayout;
import UI.Updatable;

import javax.swing.*;


public class ProgressUI extends JPanel implements Updatable {
    private final Task task;
    private final JProgressBar progressBar;
    private final JButton toggleButton;
    private final String onStartText;


    public ProgressUI(Task task, String onStartText) {
        this.task = task;
        this.onStartText = onStartText;

        progressBar = new JProgressBar(0, task.getMaxProgress());

        toggleButton = new JButton();
        toggleButton.addActionListener(e -> onToggle());
        updateButtonText(task.isActive());
        onUpdate();

        setLayout(new MigLayout("flowy"));
        add(toggleButton);
        add(progressBar);
    }

    @Override
    public void onUpdate() {
        progressBar.setValue(task.getProgress());
        if (task.isDone()) onDone();
    }

    public void onDone() {
        toggleButton.setEnabled(false);
    }

    private void updateButtonText(boolean isActive) {
        String text = isActive ? "Stop" : onStartText;
        toggleButton.setText(text);
    }

    private void onToggle() {
        boolean isActive = !task.isActive();
        task.setActive(isActive);
        updateButtonText(isActive);
    }

    public void enableToggleButton(boolean isEnabled) {
        toggleButton.setEnabled(isEnabled);
    }
}
