package UI;

import app.Data;
import app.JobManager;
import game_object.item.Recipe;
import game_object.item.entity.android.STAT;
import game_object.task.job.Job;
import game_object.task.task.Research;
import net.miginfocom.swing.MigLayout;
import UI.updatable_progress.ProgressUI;
import UI.updatable_progress.UpdatablePanelImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class ResearchTree extends UpdatablePanelImpl {
    private final Map<Recipe, Node> nodes;

    protected ResearchTree() {
        super(10);
        nodes = new HashMap<>(50);
        Data.getInstance().getRecipes().forEach(this::addNode);

        setLayout(new MigLayout());
    }

    private Node addNode(Recipe recipe) {
        if (nodes.containsKey(recipe))
            return nodes.get(recipe);

        Node node = new Node(recipe);
        getProgressUIList().add(node);
        nodes.put(recipe, node);

        if (recipe.getRequirement() == null) add(node);
        else {
            Recipe parentRecipe = recipe.getRequirement();
            Node parentNode = addNode(parentRecipe);
            parentNode.repaint(node);
        }

        return node;
    }

    private ArrayList<Node> getNodeChildren(Recipe recipe) {
        return nodes.entrySet().stream()
                .filter(e -> e.getKey().getRequirement() == recipe)
                .map(Map.Entry::getValue)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private class Node extends JPanel implements Updatable {
        private final Recipe recipe;
        private final JPanel innerPanel;
        private final ProgressUI researchProgress;
        private final ArrayList<Node> children;

        private Node(Recipe recipe) {
            this.recipe = recipe;
            this.children = new ArrayList<>(5);

            JLabel objectOfResearch = new JLabel(recipe.getName());
            researchProgress = getProgressUI();

            innerPanel = new JPanel();
            innerPanel.setLayout(new MigLayout("flowy"));
            innerPanel.add(objectOfResearch);
            innerPanel.add(researchProgress);
            innerPanel.setBorder(BorderFactory
                    .createLineBorder(Color.BLACK, 2));

            setLayout(new MigLayout("flowx"));
            add(innerPanel, "wrap");
        }

        private ProgressUI getProgressUI() {
            Supplier<Job> j = () -> new Job(new Research(recipe),
                    Collections.singletonList(STAT.CONSTRUCTION));

            Job job = JobManager.getInstance().getRequest(recipe, j);

            ProgressUI researchProgress
                    = new ProgressUI(job.getTask(), "Research") {
                @Override
                public void onDone() {
                    super.onDone();
                    getNodeChildren(recipe).forEach(Node::setAvailable);
                }
            };
            researchProgress.enableToggleButton(recipe.isReadyToLearn());
            return researchProgress;
        }

        private void setAvailable() {
            researchProgress.enableToggleButton(true);
        }

        private void repaint(Node child) {
            children.add(child);
            add(child);
            repaint();
        }

        private Point calculateBounds(Component relativeTo) {
            return SwingUtilities.convertPoint(
                    innerPanel, innerPanel.getX(), innerPanel.getY(), relativeTo);
        }

        @Override
        protected void paintChildren(Graphics g) {
            super.paintChildren(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.BLACK);

            int parentX = innerPanel.getWidth() / 2;
            int parentY = innerPanel.getY() + innerPanel.getHeight();
            children.forEach(child -> {
                Point childPoint = child.calculateBounds(innerPanel);
                g2.draw(new Line2D.Float(parentX, parentY,
                        (float) child.getWidth() / 2, childPoint.y));
            });
        }

        @Override
        public void onUpdate() {
            researchProgress.onUpdate();
        }
    }

}
