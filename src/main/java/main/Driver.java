package main;

import controller.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Driver extends Application {

    public static Stage primaryStage;
    public static Stage componentStage;
    public static MainMenuController mainMenuController;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(Driver.class.getResource("mainMenuV2.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        mainMenuController = fxmlLoader.getController();
        Image icon = new Image(getClass().getResourceAsStream("/images/icon.png"));
        stage.getIcons().add(icon);
        stage.setTitle("PCB Processor");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
