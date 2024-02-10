package com.example.demo1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        // Creating an HBox as the root node
        HBox root = new HBox();

        // Creating two VBox nodes
        VBox leftVBox = new VBox();
        VBox rightVBox = new VBox();

        // Adding some content to the VBox nodes (for demonstration purposes)
        leftVBox.getChildren().add(new javafx.scene.control.Label("Left VBox"));
        rightVBox.getChildren().add(new javafx.scene.control.Label("Right VBox"));

        // Adding the VBox nodes as children to the HBox
        root.getChildren().addAll(leftVBox, rightVBox);

        Scene scene = new Scene(root, 320, 240);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }






    public static void main(String[] args) {
        System.out.println("ad;ffk");
        launch();
    }
}