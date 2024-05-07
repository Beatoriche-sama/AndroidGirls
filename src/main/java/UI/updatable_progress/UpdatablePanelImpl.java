package UI.updatable_progress;

import UI.Updatable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class UpdatablePanelImpl extends JPanel implements Updatable {
    private final List<Updatable> progressUIList;

    public UpdatablePanelImpl(int maxCapacity) {
        this.progressUIList = new ArrayList<>(maxCapacity);
    }

    @Override
    public void onUpdate() {
        progressUIList.forEach(Updatable::onUpdate);
    }

    public List<Updatable> getProgressUIList() {
        return progressUIList;
    }
}
