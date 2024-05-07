package UI;

import app.Data;
import net.miginfocom.swing.MigLayout;
import game_object.item.Item;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class ResourcesList extends JPanel implements Updatable {
    private final Map<Item, JLabel> itemLabels;

    protected ResourcesList() {
        itemLabels = new HashMap<>();
        Data.getInstance().getItems().forEach(this::addItem);

        setLayout(new MigLayout("flowy, center"));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
    }

    private void addItem(Item item) {
        JLabel label = new JLabel(getLabelText(item));
        itemLabels.put(item, label);
        add(label);
    }

    private String getLabelText(Item item){
        return item.getName() + ": "
                + (int) item.getCurrentAmount()
                + "/" + item.getMaxAmount();
    }

    @Override
    public void onUpdate() {
        itemLabels.forEach((resource, label)
                -> label.setText(getLabelText(resource)));
    }

}
