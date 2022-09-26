package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;

public class Component {

    private String name;
    private Color targetColor;
    private Color border;
    private Color color;
    private int[] sets;
    private HashMap<String, ArrayList<XY>> hashMap;
    private HashMap<String, Rectangle> rectangles;
    private ArrayList<Rectangle> rectangleArrayList;
    private ArrayList<String> groups;

    public Component(String name, Color targetColor) {
        this.name = name;
        this.targetColor = targetColor;
        this.border = Color.RED;
        this.color = Color.BLACK;
        this.hashMap = new HashMap<>();
        this.groups = new ArrayList<>();
        this.rectangles = new HashMap<>();
        this.rectangleArrayList = new ArrayList<>();
    }

    public int getRValue(){
        return (int) Math.round(targetColor.getRed()*255);
    }

    public int getGValue(){
        return (int) Math.round(targetColor.getGreen()*255);
    }

    public int getBValue(){
        return (int) Math.round(targetColor.getBlue()*255);
    }

    public int disjointSetSize(String groupID){
            return hashMap.get(groupID).size();
    }

    public void sortRectangles(){
        Comparator<Rectangle> comparator = new Comparator<Rectangle>() {
            @Override
            public int compare(Rectangle o1, Rectangle o2) {
                int rect1 = (int) (o1.getWidth() * o1.getHeight());
                int rect2 = (int) (o2.getHeight() * o2.getHeight());
                return rect1 - rect2;
            }
        };

        rectangleArrayList.sort(comparator);
    }

    public ArrayList<Rectangle> getRectangleArrayList() {
        return rectangleArrayList;
    }

    public void setRectangleArrayList(ArrayList<Rectangle> rectangleArrayList) {
        this.rectangleArrayList = rectangleArrayList;
    }

    public HashMap<String, Rectangle> getRectangles() {
        return rectangles;
    }

    public void setRectangles(HashMap<String, Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    public int getSetsLength(){
        return sets.length;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public HashMap<String, ArrayList<XY>> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, ArrayList<XY>> hashMap) {
        this.hashMap = hashMap;
    }

    public int[] getSets() {
        return sets;
    }

    public void setSets(int[] sets) {
        this.sets = sets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getTargetColor() {
        return targetColor;
    }

    public void setTargetColor(Color targetColor) {
        this.targetColor = targetColor;
    }

    public Color getBorder() {
        return border;
    }

    public void setBorder(Color border) {
        this.border = border;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    @Override
    public String toString() {
        return name;
    }
}
