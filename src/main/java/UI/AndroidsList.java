package UI;

import app.Data;
import game_object.task.job.Job;
import net.miginfocom.swing.MigLayout;
import game_object.item.entity.android.Android;
import game_object.item.entity.android.Level;
import game_object.item.entity.android.STAT;

import javax.swing.*;
import java.awt.*;
import java.util.*;

class AndroidsList extends JPanel implements Updatable {
    private final LinkedList<AndroidUI> allAndroidsUIList;
    private final LinkedList<AndroidUI> shownList;
    private final int maxShownAndroids;

    private final JButton nextButton, prevButton;

    protected AndroidsList() {
        this.allAndroidsUIList = new LinkedList<>();
        this.shownList = new LinkedList<>();
        this.maxShownAndroids = 3;

        Data.getInstance().getAndroids().forEach(android
                -> allAndroidsUIList.add(new AndroidUI(android)));

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> getNext());

        prevButton = new JButton("Previous");
        prevButton.addActionListener(e -> getPrevious());

        setLayout(new MigLayout("flowy, fillx"));
        add(prevButton, "split 2, flowx, center");
        add(nextButton, "flowy");

        showList(0, maxShownAndroids);
    }

    private void showList(int fromInd, int toInd) {
        shownList.forEach(this::remove);
        shownList.clear();

        int first = Math.max(fromInd, 0);
        int allAndroidsSize = allAndroidsUIList.size();
        int last = Math.min(toInd, allAndroidsSize);

        shownList.addAll(allAndroidsUIList.subList(first, last));
        shownList.forEach(ui -> this.add(ui, "growx"));
        this.repaint();

        nextButton.setEnabled(last != allAndroidsSize);
        prevButton.setEnabled(first != 0);
    }

    private void getNext() {
        int shownListLastIndex = allAndroidsUIList
                .indexOf(shownList.getLast());
        showList(shownListLastIndex + 1, shownListLastIndex + maxShownAndroids);
    }

    private void getPrevious() {
        int shownListFirstIndex = allAndroidsUIList
                .indexOf(shownList.getFirst());
        showList(shownListFirstIndex - maxShownAndroids, shownListFirstIndex);
    }

    @Override
    public void onUpdate() {
        shownList.forEach(androidUI -> {
            androidUI.updateJobStatus();
            androidUI.updateStats();
        });
    }

    private static class AndroidUI extends JPanel {
        private final Android android;
        private final JLabel statsField;
        private final JLabel currentJobLabel;

        private AndroidUI(Android android) {
            this.android = android;

            JLabel nameLabel = new JLabel(android.getName());
            currentJobLabel = new JLabel();
            statsField = new JLabel();
            statsField.setBorder(BorderFactory
                    .createLineBorder(Color.BLACK, 1));

            updateJobStatus();
            updateStats();

            setLayout(new MigLayout("flowy"));
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            add(nameLabel);
            add(currentJobLabel);
            add(statsField);
        }

        private void updateJobStatus() {
            Job job = android.getCurrentJob();
            String jobName = job == null ? "Unemployed" : job.getName();
            currentJobLabel.setText(jobName);
        }

        private void updateStats() {
            statsField.setText(getStatsString(android.getStats()));
        }

        private String getStatsString(Map<STAT, Level> stats) {
            StringBuilder stringBuilder = new StringBuilder("<html>Stats:<br/>");

            stats.forEach((stat, level)
                    -> stringBuilder.append(stat.toString().toLowerCase())
                    .append(": ").append(level.getCurrentLevel())
                    .append("<br/>"));
            return stringBuilder.append("</html>").toString();
        }
    }

}
