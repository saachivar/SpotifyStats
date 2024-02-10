package com.example.demo1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.scene.paint.Color;




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
        root.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        root.setStyle("-fx-border-color: BLACK; -fx-border-width: 15px;");

        // Creating two VBox nodes
        VBox leftPane = new VBox();

        //LeftVBox elements
        Text insertPrompt = new Text("Insert an artist:");
        Font font = Font.font(insertPrompt.getFont().getFamily(), 20);
        insertPrompt.setStyle("-fx-font: italic 20px \"System\";");
        insertPrompt.setFont(font);
        TextField insertBox = new TextField();
        Text choosePrompt = new Text("... OR choose from the top Billboard 100");
        Font font2 = Font.font(choosePrompt.getFont().getFamily(), 13);
        choosePrompt.setFont(font2);
        StackPane textPane = new StackPane(choosePrompt);
        choosePrompt.setFill(Color.BLANCHEDALMOND);
        leftPane.setSpacing(10);
        choosePrompt.setFont(font2);
        Text websiteText = new Text("text from website");
        Button dataButton = new Button("Get Data");
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
        dataButton.setOnAction(event -> {
            try {
                String token = getAccessToken(); // Rename the variable to 'token'
                // Use 'token' as needed
                String artistDetails = getArtistDetails(insertBox.getText());
                mainArtistText.setText(artistDetails);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
        });









        artistInfo1.getChildren().addAll(artistImageView, mainArtistText);
        artistInfo2.getChildren().addAll(artistText1, artistText2);
        // Adding some content to the VBox nodes (for demonstration purposes)
        leftPane.getChildren().addAll(insertPrompt, insertBox, choosePrompt, websiteText, dataButton);
        rightPane.getChildren().addAll(artistInfo1, artistInfo2);

        // Adding the VBox nodes as children to the HBox
        root.getChildren().addAll(leftPane, rightPane);
        // Embed the Swing panel in a JavaFX SwingNode



        Scene scene = new Scene(root, 800, 600);
        this.scrapeBillBoard();

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }



    private String getArtistDetails(String artistName) throws IOException, InterruptedException {
        String query = artistName.replace(" ", "%20"); // Encode spaces in the artist name
        String apiUrl = API_URL + "?q=" + query + "&type=artist";
        String accessToken = SpotifyAuthenticator.getAccessToken();
        //String apiUrl = "https://api.spotify.com/v1/search?q=" + artistName + "&type=artist";

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + "BQCl0XJ2z1zxoTPgKE2CP92LoXXr6uXBBy9XncuIvYkM5LStjBM-S3w23_-02F_OB6RjdffJ6uSh_aF1IVzXKyXAeRvRoOVcZxc1XpSep7zVuQIWtBw")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Parse the JSON response and extract the artist details
            JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            // For example, extract the artist name from the response
            artistName = jsonResponse.getAsJsonObject("artists").getAsJsonArray("items")
                    .get(0).getAsJsonObject().get("name").getAsString();
            return artistName; // Return the artist details
        } else {
            throw new IOException("Failed to retrieve artist details from Spotify API. Response code: " + response.statusCode());
        }
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