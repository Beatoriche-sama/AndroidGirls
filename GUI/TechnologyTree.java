package GUI;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class TechnologyTree extends JPanel {
    private final String constrants;

    public TechnologyTree() {
        this.constrants = "top, center, growx";
        setLayout(new MigLayout("fill"));
    }

    public static class GraphicElement extends JPanel {
        private final ArrayList<GraphicElement> children;
        private JButton producerButton;

        private GraphicElement(String name) {
            this.producerButton = new JButton("Invent " + name);
            this.children = new ArrayList<>();

            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    if (children.isEmpty()) return;
                    paintChildrenBranch();
                }
            });

            setLayout(new MigLayout("fill"));
            add(producerButton, "center, top, wrap, gapy 20");
        }

        public JButton getInventButton() {
            return producerButton;
        }

        public ArrayList<GraphicElement> getChildren() {
            return children;
        }

        private void paintChildrenBranch() {
            //parent button coordinates
            Point buttonPoint = producerButton.getLocation();
            int buttonXCenter = buttonPoint.x + producerButton.getWidth() / 2;
            int buttonMinY = buttonPoint.y + producerButton.getHeight();
            Point parentUpPoint = new Point(buttonXCenter, buttonMinY);

            //first child button coordinates
            GraphicElement firstChild = children.get(0);
            Point firstChildLocation = firstChild.producerButton.getLocation();
            Point convertedFirstChildPoint = SwingUtilities
                    .convertPoint(firstChild, firstChildLocation, this);

            int verticalMainEndingY;

            if (children.size() == 1) {
                verticalMainEndingY = convertedFirstChildPoint.y;
            } else {
                int gap = (producerButton.getY() + producerButton.getHeight()) - convertedFirstChildPoint.y;
                verticalMainEndingY = parentUpPoint.y - gap / 2;

                drawMainHorizontalLine(verticalMainEndingY, convertedFirstChildPoint.x,
                        firstChild.producerButton.getWidth());

                drawChildrenVerticalLines(verticalMainEndingY, gap);
            }
            //draw vertical main line
            drawLine(parentUpPoint, new Point(parentUpPoint.x, verticalMainEndingY));
        }

        private void drawMainHorizontalLine(int verticalMainEndingY,
                                            int convertedFirstChildX,
                                            int firstChildWidth) {
            //end x coordinate of horizontal main line
            GraphicElement lastChild = children.get(children.size() - 1);
            Point lastChildLocation = lastChild.producerButton.getLocation();
            Point convertedLastChildPoint = SwingUtilities
                    .convertPoint(lastChild, lastChildLocation, this);

            //centers of first and last children elements
            int lastChildXCenter = convertedLastChildPoint.x + lastChild.producerButton.getWidth() / 2;
            int firstChildXCenter = convertedFirstChildX + firstChildWidth / 2;

            //draw horizontal main line
            Point horizontalPointLeft = new Point(firstChildXCenter, verticalMainEndingY);
            Point horizontalPointRight = new Point(lastChildXCenter, verticalMainEndingY);
            drawLine(horizontalPointLeft, horizontalPointRight);
        }

        private void drawChildrenVerticalLines(int verticalMainEndingY, int parentChildGap) {
            children.forEach(child -> {
                Point childLocation = child.producerButton.getLocation();
                Point convertedChildPoint = SwingUtilities
                        .convertPoint(child, childLocation, this);
                int childXCenter = convertedChildPoint.x + child.producerButton.getWidth() / 2;

                Point childVerticalPointUp = new Point(childXCenter, verticalMainEndingY);
                Point childVerticalPointDown = new Point(childXCenter,
                        verticalMainEndingY - parentChildGap / 2);
                drawLine(childVerticalPointUp, childVerticalPointDown);
            });

        }

        private void drawLine(Point begin, Point end) {
            Graphics2D g2 = (Graphics2D) getGraphics();
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(3));
            g2.draw(new Line2D.Float(begin.x, begin.y, end.x, end.y));
        }
    }

    /*public void addChild(GraphicElement parentGraphicElement,
                         GraphicElement childGraphicElement) {
        ArrayList<GraphicElement> childrenList = parentGraphicElement.children;
        if (!childrenList.contains(childGraphicElement)) {
            childrenList.add(childGraphicElement);
            parentGraphicElement.add(childGraphicElement, constrants);
        }
    }

    public GraphicElement createBasicGraphicElement(String name) {
        GraphicElement basicGraphicElement = createGraphicElement(name);
        add(basicGraphicElement, constrants);
        return basicGraphicElement;
    }*/

    public void addElementToTree(GraphicElement parentGraphicElement,
                                 GraphicElement childGraphicElement) {
        if (parentGraphicElement == null)
            add(childGraphicElement, constrants);
        else {
            parentGraphicElement.children.add(childGraphicElement);
            parentGraphicElement.add(childGraphicElement, constrants);
        }
    }

    public GraphicElement createGraphicElement(String name) {
        return new GraphicElement(name);
    }
}
