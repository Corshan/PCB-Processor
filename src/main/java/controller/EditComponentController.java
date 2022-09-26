package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import main.Driver;
import model.Component;

public class EditComponentController {

    public Component component;

    @FXML
    TextField componentName;

    @FXML
    Label RValue, GValue, BValue;

    @FXML
    ColorPicker borderColor, color;

    public void initialize() {
        component = ProcessorApi.component;
        componentName.setText(component.getName());
        RValue.setText(String.valueOf(component.getRValue()));
        GValue.setText(String.valueOf(component.getGValue()));
        BValue.setText(String.valueOf(component.getBValue()));
        borderColor.setValue(component.getBorder());
        color.setValue(component.getColor());
    }

    public void editComponent(){
        component.setName(componentName.getText());
        component.setBorder(borderColor.getValue());
        component.setColor(color.getValue());
        Driver.mainMenuController.refreshComponentList();
        Driver.mainMenuController.updateBlackAndWhiteImage();
        Driver.mainMenuController.updateBorderColors();
    }

    public void deleteComponent(){
        Driver.mainMenuController.deleteComponent();
        Driver.componentStage.close();
    }
}
