package com.example.demo1;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static com.example.demo1.SpotifyAuthenticator.getAccessToken;


public class HelloApplication extends Application {
    private static final String API_URL = "https://api.spotify.com/v1/search";
    private static final String ACCESS_TOKEN;

    static {
        try {
            ACCESS_TOKEN = getAccessToken();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        String accessToken = getAccessToken();
        System.out.println("Access token: " + accessToken);
        // Creating an HBox as the root node
        HBox root = new HBox();

        // Creating two VBox nodes
        VBox leftPane = new VBox();

        //LeftVBox elements
        Text insertPrompt = new Text("Insert an artist:");
        Font font = Font.font(insertPrompt.getFont().getFamily(), 20);
        insertPrompt.setFont(font);
        TextField insertBox = new TextField();
        Text choosePrompt = new Text("... OR choose from the top Billboard 100");
        Font font2 = Font.font(choosePrompt.getFont().getFamily(), 20);
        choosePrompt.setFont(font2);
        Text websiteText = new Text("text from website");
        Button dataButton = new Button("Get Data");
        dataButton.setOnAction(event -> {
            try {
                String token = getAccessToken(); // Rename the variable to 'token'
                // Use 'token' as needed
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        });




        VBox rightPane = new VBox();
        HBox artistInfo1 = new HBox();
        ImageView artistImageView = new ImageView();
        Text mainArtistText = new Text("text");
        HBox artistInfo2 = new HBox();
        Text artistText1 = new Text("text");
        Text artistText2 = new Text("text");


        // Setting VBox to take half of the available space
        leftPane.setPrefWidth(400);
        leftPane.setPadding(new Insets(25));
        rightPane.setPrefWidth(400);
        rightPane.setPadding(new Insets(25));



        artistInfo1.getChildren().addAll(artistImageView, mainArtistText);
        artistInfo2.getChildren().addAll(artistText1, artistText2);
        // Adding some content to the VBox nodes (for demonstration purposes)
        leftPane.getChildren().addAll(insertPrompt, insertBox, choosePrompt, websiteText, dataButton);
        rightPane.getChildren().addAll(artistInfo1, artistInfo2);

        // Adding the VBox nodes as children to the HBox
        root.getChildren().addAll(leftPane, rightPane);

        Scene scene = new Scene(root, 800, 600);
        this.scrapeBillBoard();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }


    public void scrapeBillBoard() {
        try {
            // Connect to the website and get the HTML
            Document document = Jsoup.connect("https://www.billboard.com/charts/artist-100/").get();

            // Extract elements you're interested in
            Elements elements = document.select("h1"); // Select all <h1> elements

            // Iterate over the elements and print them
            for (Element element : elements) {
                System.out.println(element.text());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args) {
        System.out.println("ad;fkfkkkhjhji");
        launch();
    }
}