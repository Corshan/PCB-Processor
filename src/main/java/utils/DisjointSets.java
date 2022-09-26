package utils;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.Component;
import model.XY;

import java.util.ArrayList;
import java.util.HashMap;

import static utils.UnionFind.*;

public class DisjointSets {

    public static void buildSet(Image image, Component component) {
        findPixels(image, component);
        joinSets((int) image.getWidth(), component);
        buildGroups(image, component);
    }

    private static void findPixels(Image image, Component component) {
        int sets[] = new int[(int) image.getWidth() * (int) image.getHeight()];
        component.setSets(sets);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int index = x + y * (int) image.getWidth();
                Color pixel = image.getPixelReader().getColor(x,y);
                Color componentColor = component.getTargetColor();
                double hue = pixel.getHue();
                double componentHue = componentColor.getHue();

                double saturation = pixel.getSaturation();
                double componentSaturation = componentColor.getSaturation();


                boolean hueRange = false;

                if (componentHue-30 < 0){
                    double range = 30-componentHue;
                    if(hue <= componentHue+30 || hue <= 360 && hue >= 360-range){
                        hueRange = true;
                    }
                }else if (componentHue+30 > 360){
                    double range = componentHue+30 -360;
                    if (hue >= componentHue-30 || hue >= 0 && hue >= 0+range){
                        hueRange = true;
                    }
                }else {
                    hueRange = hue >= componentHue-30 && hue <= componentHue+30;
                }

                boolean saturationRange = saturation >= componentSaturation-0.1 && saturation <= componentSaturation+0.1;
                if (hueRange && saturationRange){
                    sets[index] = index;
                } else {
                    sets[index] = 0;
                }
            }
        }
    }

    private static void joinSets(int width, Component component) {
        int size = component.getSets().length;
        int sets[] = component.getSets();
        for (int index = 0; index < size; index++) {
            if (sets[index] != 0) {
                int indexRight = index + 1;
                int indexBottom = index + width;

                if (indexRight < size && sets[indexRight] != 0) union(sets, indexRight, index);
                if (indexBottom < size && sets[indexBottom] != 0) union(sets, indexBottom, index);
            }
        }


    }

    private static void buildGroups(Image image, Component component) {
        String id = "Group ";
        HashMap<String, ArrayList<XY>> hashMap = component.getHashMap();
        for (int i = 0; i < component.getSetsLength(); i++) {
            if (component.getSets()[i] != 0) {
                int root = UnionFind.find(component.getSets(), i);

                int x = i % (int) image.getWidth();
                int y = i / (int) image.getWidth();
                XY xy = new XY(x, y);
                String groupID = id + root;

                if (hashMap.containsKey(groupID)) {
                    hashMap.get(groupID).add(xy);
                } else {
                    ArrayList<XY> arrayList = new ArrayList<>();
                    arrayList.add(xy);
                    hashMap.put(groupID, arrayList);
                    component.getGroups().add(groupID);
                }
                //System.out.println("Index: " + i + " Root: " + root + " X: " + x + " Y: " + y + " ID: " + groupID + " Array: " + hashMap.get(groupID));
            }
        }
    }

}
