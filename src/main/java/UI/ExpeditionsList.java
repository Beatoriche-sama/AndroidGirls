package UI;

import app.Data;
import app.JobManager;
import game_object.Location;
import game_object.item.Item;
import game_object.item.entity.android.STAT;
import game_object.task.job.Job;
import game_object.task.task.Expedition;
import net.miginfocom.swing.MigLayout;
import UI.updatable_progress.ProgressUI;
import UI.updatable_progress.UpdatablePanelImpl;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

class ExpeditionsList extends UpdatablePanelImpl {

    protected ExpeditionsList() {
        super(15);
        Data.getInstance().getLocations().forEach(this::addExpedition);

        setLayout(new MigLayout("flowy"));
    }

    private void addExpedition(Location location) {
        JLabel nameLabel = new JLabel(location.getName());

        JLabel spoilsLabel = new JLabel("Brings: "
                + getSpoilsString(location.getTreasures()));

        Supplier<Job> j = () -> new Job(new Expedition(location),
                Collections.singletonList(STAT.CONSTRUCTION));
        Job job = JobManager.getInstance().getRequest(location, j);

        ProgressUI progressUI = new ProgressUI(
                job.getTask(), "Venture");
        getProgressUIList().add(progressUI);

        JPanel container = new JPanel();
        container.setLayout(new MigLayout("flowy"));
        container.setBorder(BorderFactory
                .createLineBorder(Color.BLACK, 2));
        container.add(nameLabel);
        container.add(spoilsLabel);
        container.add(progressUI);

        add(container);
    }

    private String getSpoilsString(List<Item> spoils) {
        StringBuilder spoilsBuilder = new StringBuilder();
        int lastInd = spoils.size() - 1;

        spoils.forEach(s -> {
            spoilsBuilder.append(s.getName());
            if (spoils.indexOf(s) != lastInd)
                spoilsBuilder.append(", ");
        });

        return spoilsBuilder.toString();
    }

}
