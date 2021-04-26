package com.edencoding;

import com.edencoding.controllers.FrameRateCalculator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    int count = 0;
    StringBuilder results = new StringBuilder();

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/PaneTest.fxml"));
        Parent content = loader.load();
        Scene scene = new Scene(content);
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("img/EdenCodingIcon.png")));
        stage.setScene(scene);
        stage.show();

        FrameRateCalculator controller = loader.getController();
        controller.frameRateProperty().addListener((observable, oldValue, newValue) -> {
            stage.setTitle("FPS: " + newValue);
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
