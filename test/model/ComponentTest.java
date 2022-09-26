package model;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.DisjointSets;
import utils.UnionFind;
import utils.Utilities;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {

    Component component;
    Image image;

    @BeforeEach
    void setUp() {
        component = new Component("Resistor", Color.BLACK);
        image = new Image(getClass().getResourceAsStream("/images/testImage1.png"));
    }

    @AfterEach
    void tearDown() {
        component = null;
        image = null;
    }

    @Test
    void buildSet() {
        DisjointSets.buildSet(image, component);
        for (int y = 0; y < image.getHeight(); ++y){
            for (int x = 0; x < image.getWidth(); ++x){
                System.out.print("[" + component.getSets()[x + y*(int)image.getWidth()] + "]");
            }
            System.out.println();
        }
        System.out.println();
    }
}