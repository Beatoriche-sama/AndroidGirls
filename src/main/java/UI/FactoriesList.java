package UI;

import app.Data;
import app.JobManager;
import game_object.item.Recipe;
import game_object.item.entity.Factory;
import game_object.item.entity.android.STAT;
import game_object.task.job.Job;
import game_object.task.task.Craft;
import net.miginfocom.swing.MigLayout;
import UI.updatable_progress.ProgressUI;
import UI.updatable_progress.UpdatablePanelImpl;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.function.Supplier;

class FactoriesList extends UpdatablePanelImpl {
    private final Data data;

    public FactoriesList() {
        super(50);
        data = Data.getInstance();
        data.getFactories().forEach(this::addFactory);

        setLayout(new MigLayout("flowy, fillx"));
    }

    private void addFactory(Factory factory) {
        FactoryUI factoryUI
                = new FactoryUI(factory);
        getProgressUIList().add(factoryUI);
        add(factoryUI, "growx");
    }

    private class FactoryUI extends JPanel implements Updatable {
        private final Factory factory;
        private final Recipe recipe;
        private ProgressUI progressUI;

        FactoryUI(Factory factory) {
            this.factory = factory;

            JLabel nameLabel = new JLabel(factory.getName());

            Recipe productionR = factory.getCraftTask().getRecipe();
            JLabel spoilsLabel = new JLabel(
                    "Brings: " + productionR.getResult().getName());

            JLabel fuelLabel = new JLabel(
                    "Fuel type: " + factory.getFuelType().name().toLowerCase());
            JLabel powerLabel = new JLabel("Power: " + factory.getPower());

            recipe = data.getRecipes().stream()
                    .filter(r -> r.getResult() == factory)
                    .findFirst().orElse(null);
            boolean isCreated = factory.isMaxed();

            setVisible(isCreated);
            setLayout(new MigLayout("flowy"));
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            add(nameLabel);
            add(spoilsLabel);
            add(fuelLabel);
            add(powerLabel);

            if (!isCreated && recipe != null)
                addConstructionProgress();
            else {
                addProductionProgress();
            }
        }

        private void setProgressUI(ProgressUI progressUI) {
            this.progressUI = progressUI;
            add(progressUI);
        }

        private void addConstructionProgress() {
            Supplier<Job> j = () -> {
                Craft buildFactory = new Craft(recipe, false);
                return new Job(buildFactory,
                        Collections.singletonList(STAT.CONSTRUCTION));
            };

            Job job = JobManager.getInstance().getRequest(factory, j);

            ProgressUI constructionUI
                    = new ProgressUI(job.getTask(), "Build") {
                @Override
                public void onDone() {
                    super.onDone();
                    addProductionProgress();
                }
            };

            setProgressUI(constructionUI);
        }

        private void addProductionProgress() {
            setProgressUI(new ProgressUI(
                    factory.getCraftTask(), "Start"));
        }


        @Override
        public void onUpdate() {
            if (!isVisible() && recipe != null)
                setVisible(recipe.isMaxed());
            progressUI.onUpdate();
        }
    }

}
