package com.example.demo1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private static final String API_URL = "https://api.spotify.com/v1/search";
    private static final String ACCESS_TOKEN = SpotifyAuthenticator.getAccessToken();
    @Override
    public void start(Stage stage) throws IOException {


        // Creating an HBox as the root node
        HBox root = new HBox();

        // Creating two VBox nodes
        VBox leftVBox = new VBox();
        VBox rightPane = new VBox();
        HBox artistInfo1 = new HBox();
        ImageView artistImageView = new ImageView();
        Text mainArtistText = new Text("text");
        HBox artistInfo2 = new HBox();
        Text artistText1 = new Text("text");
        Text artistText2 = new Text("text");


        artistInfo1.getChildren().addAll(artistImageView, mainArtistText);
        artistInfo2.getChildren().addAll(artistText1, artistText2);
        // Adding some content to the VBox nodes (for demonstration purposes)
        leftVBox.getChildren().add(new javafx.scene.control.Label("Left VBox"));
        rightPane.getChildren().addAll(artistInfo1, artistInfo2);

        // Adding the VBox nodes as children to the HBox
        root.getChildren().addAll(leftVBox, rightPane);

        Scene scene = new Scene(root, 320, 240);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }






    public static void main(String[] args) {
        System.out.println("ad;fkfk");
        launch();
    }
}