package controller;

import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import model.Component;
import model.XY;
import utils.DisjointSets;

import java.util.*;

public class ProcessorApi {

    public static Component component;
    private List<Component> components;

    public ProcessorApi() {
        components = new ArrayList<>();
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void buildDisjointSet(Image image, Component component) {
        DisjointSets.buildSet(image, component);
    }

    public Image buildBlackAndWhite(Image image, Component component, int noiseControl, boolean randomColor) {
        WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        updateSetColor(noiseControl, randomColor, writableImage, component);
        return writableImage;
    }

    public Image buildWhiteImage(Image image) {
        WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                writableImage.getPixelWriter().setColor(x, y, Color.WHITE);
            }
        }

        return writableImage;
    }

    public Image deleteComponent(Image image) {
        WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getHeight(), (int) image.getHeight());

        for (String groupID : component.getGroups()) {
            for (XY xy : component.getHashMap().get(groupID)) {
                writableImage.getPixelWriter().setColor(xy.getX(), xy.getY(), Color.WHITE);
            }
        }
        components.remove(component);
        return writableImage;
    }

    public Image updatePartImage(Image image, int noiseControl) {
        WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        for (String groupID : component.getGroups()) {
            if (component.getHashMap().get(groupID).size() > noiseControl)
                for (XY xy : component.getHashMap().get(groupID)) {
                    writableImage.getPixelWriter().setColor(xy.getX(), xy.getY(), component.getColor());
                }
        }
        return writableImage;
    }

    public Image updateNoise(Image image, int noiseControl, boolean randomColor) {
        Image whiteImage = buildWhiteImage(image);
        WritableImage writableImage = new WritableImage(whiteImage.getPixelReader(), (int) whiteImage.getWidth(), (int) whiteImage.getHeight());

        for (Component component : components) {
            updateSetColor(noiseControl, randomColor, writableImage, component);
        }

        return writableImage;
    }

    private void updateSetColor(int noiseControl, boolean randomColor, WritableImage writableImage, Component component) {
        for (String groupID : component.getGroups()) {
            if (component.getHashMap().get(groupID).size() > noiseControl) {
                Color color = (randomColor) ? new Color(Math.random(), Math.random(), Math.random(), 1) : component.getColor();
                for (XY xy : component.getHashMap().get(groupID)) {
                    writableImage.getPixelWriter().setColor(xy.getX(), xy.getY(), color);
                }
            }
        }
    }

    public void buildRectangles(Component component, Image image) {
        for (String groupID : component.getGroups()) {
            ArrayList<XY> cords = component.getHashMap().get(groupID);
            if (cords.size() > 100) {
                int xMin = cords.get(0).getX();
                int xMax = cords.get(0).getX();

                int yMin = cords.get(0).getY();
                int yMax = cords.get(0).getY();

                for (XY xy : cords) {
                    if (xy.getX() < xMin) xMin = xy.getX();
                    if (xy.getX() > xMax) xMax = xy.getX();

                    if (xy.getY() < yMin) yMin = xy.getY();
                    if (xy.getY() > yMax) yMax = xy.getY();
                }
                int xCord = (int) ((xMin / image.getWidth()) * 300);
                int yCord = (int) ((yMin / image.getWidth()) * 300);

                int width = (int) ((xMax / image.getWidth()) * 300) - xCord;
                int height = (int) ((yMax / image.getHeight() * 300) - yCord);
                Paint paint = component.getBorder();
                Rectangle rect = new Rectangle(xCord, yCord, width, height);
                rect.setStroke(paint);
                rect.setFill(null);
                component.getRectangles().put(groupID, rect);
                component.getRectangleArrayList().add(rect);
            }
        }
        component.sortRectangles();
        for (int i = 0; i < component.getRectangleArrayList().size(); i++) {
            Rectangle rect = component.getRectangleArrayList().get(i);
            Tooltip tip = new Tooltip();
            int num = i + 1;
            double size = rect.getWidth() * rect.getHeight();
            tip.setText("Component Type: " + component.getName() + "\nComponent Number: " + num + "\nEstimated Pixels: " + size);
            Tooltip.install(rect, tip);
            System.out.println(tip.getText());
        }
    }

    public LinkedList<Rectangle> buildRectangles() {
        LinkedList<Rectangle> rectangles = new LinkedList<>();
        for (Component component : components) {
            rectangles.addAll(component.getRectangleArrayList());
        }
        return rectangles;
    }

    public void clearComponentList() {
        components.clear();
    }

    public Component getComponent(int index) {
        return components.get(index);
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }
}
