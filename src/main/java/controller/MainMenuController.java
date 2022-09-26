package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import main.Driver;
import model.Component;

import java.io.File;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

public class MainMenuController {

    private File file;
    private ContextMenu contextMenu;
    private ProcessorApi processorApi;
    private Color tempColor;
    private Stage stage;

    @FXML
    ImageView originalImage, blackAndWhite, borderImage;
    @FXML
    ListView<Component> componentList;
    @FXML
    ListView<String> componentGroup;
    @FXML
    Label fileName, resolution, componentNumLabel;
    @FXML
    Slider noiseControl;
    @FXML
    CheckBox randomColors;
    @FXML
    AnchorPane anchorPane;

    public void initialize() {
        contextMenu = new ContextMenu();
        MenuItem menuItem = new MenuItem("Choose Component");
        menuItem.setOnAction(actionEvent -> addComponent());
        contextMenu.getItems().add(menuItem);
        processorApi = new ProcessorApi();
        stage = new Stage();
        stage.setResizable(false);
    }

    public void chooseImage() {
        try {
            FileChooser fileChooser = new FileChooser();
            file = fileChooser.showOpenDialog(Driver.primaryStage);
            Image image = new Image("file:///" + file.getAbsolutePath());
            fileName.setText(file.getName());
            resolution.setText((int) image.getWidth() + "x" + (int) image.getHeight());
            //imageView.setPreserveRatio(true);
            originalImage.setFitWidth(300);
            originalImage.setFitHeight(300);
            originalImage.setImage(image);
            componentList.getItems().clear();
            processorApi.clearComponentList();
            blackAndWhite.setVisible(false);
            borderImage.setVisible(false);
            blackAndWhite.setImage(processorApi.buildWhiteImage(image));
            borderImage.setImage(new Image("file:///" + file.getAbsolutePath()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chooseComponent(MouseEvent mouseEvent) {
        if (originalImage.getImage() == null) return;
        Image image = originalImage.getImage();
        int x = (int) ((mouseEvent.getX() / 300) * image.getWidth());
        int y = (int) ((mouseEvent.getY() / 300) * image.getHeight());
        tempColor = image.getPixelReader().getColor(x, y);
        //System.out.println(borderImage.getLayoutX() + ", " + borderImage.getLayoutY());
        //System.out.println("R: " + Math.round(tempColor.getRed() * 255) + ", G: " + Math.round(tempColor.getGreen() * 255) + ", B: " + Math.round(tempColor.getBlue() * 255));
    }


    public void contextMenu(ContextMenuEvent contextMenuEvent) {
        if (originalImage.getImage() == null) return;
        contextMenu.show(Driver.primaryStage, Math.round(contextMenuEvent.getScreenX()), Math.round(contextMenuEvent.getScreenY()));
    }

    public void addComponent() {
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.setTitle("Component");
        inputDialog.setHeaderText("Please Enter the Name of the Component");
        Optional<String> result = inputDialog.showAndWait();
        Component component = new Component(result.get(), tempColor);
        processorApi.addComponent(component);
        componentList.getItems().add(component);
        processorApi.buildDisjointSet(originalImage.getImage(), component);
        Image image = processorApi.buildBlackAndWhite(blackAndWhite.getImage(), component, (int) noiseControl.getValue(), randomColors.isSelected());
        blackAndWhite.setImage(image);
        blackAndWhite.setVisible(true);
        processorApi.buildRectangles(component, image);
//        Set<String> groups = component.getRectangles().keySet();
//        for (String groupID : groups) {
//            anchorPane.getChildren().add(component.getRectangles().get(groupID));
//        }
        anchorPane.getChildren().addAll(component.getRectangleArrayList());
        borderImage.setVisible(true);
    }

    public void editComponent(MouseEvent mouseEvent) throws Exception {
        ProcessorApi.component = componentList.getItems().get(componentList.getSelectionModel().getSelectedIndex());
        if (mouseEvent.getClickCount() >= 2) {
            FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("anchor.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 230, 180);
            Driver.componentStage = stage;
            stage.setTitle("Component");
            stage.setScene(scene);
            stage.show();
        }else {
            componentGroup.getItems().clear();
            anchorPane.getChildren().clear();
            Component component = ProcessorApi.component;
            Set<String> groupIDs = component.getRectangles().keySet();
            int num = 1;
//            for (String group : groupIDs){
//                Rectangle rect = component.getRectangles().get(group);
//                componentGroup.getItems().add("Component: " + num + ", Size: " + component.disjointSetSize(group) + " pixels");
//                anchorPane.getChildren().add(numberLabels(num, rect));
//                num++;
//            }
            anchorPane.getChildren().addAll(processorApi.buildRectangles());
            componentNumLabel.setText(String.valueOf(component.getRectangleArrayList().size()));
        }
    }

    public void deleteComponent() {
        Image image = processorApi.deleteComponent(blackAndWhite.getImage());
        blackAndWhite.setImage(image);
        componentList.getItems().remove(ProcessorApi.component);
        anchorPane.getChildren().clear();
        LinkedList<Rectangle> rects = processorApi.buildRectangles();
        for (Rectangle rectangle : rects){
            anchorPane.getChildren().add(rectangle);
        }
        componentGroup.getItems().clear();
        componentNumLabel.setText("");
    }

    public void refreshComponentList() {
        componentList.getItems().clear();
        for (Component component : processorApi.getComponents()) {
            componentList.getItems().add(component);
        }
    }

    public void updateBlackAndWhiteImage() {
        if (!randomColors.isSelected()) {
            Image image = processorApi.updatePartImage(blackAndWhite.getImage(), (int) noiseControl.getValue());
            blackAndWhite.setImage(image);
        }
    }

    public void updateNoise() {
        Image image = processorApi.updateNoise(blackAndWhite.getImage(), (int) noiseControl.getValue(), randomColors.isSelected());
        blackAndWhite.setImage(image);
    }

    public void updateBorderColors(){
        anchorPane.getChildren().clear();
        anchorPane.getChildren().addAll(processorApi.buildRectangles());
    }


//    private Text numberLabels(int string, Rectangle rectangle){
//        Text number = new Text();
//        number.setText(String.valueOf(string));
//        Paint color = Color.ORANGE;
//        number.fillProperty().setValue(color);
//        Font font = new Font(20);
//        number.fontProperty().setValue(font);
//        number.setLayoutX(rectangle.getX());
//        number.setLayoutY(rectangle.getY());
//        return number;
//    }
}
