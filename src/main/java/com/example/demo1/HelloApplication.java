package com.example.demo1;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
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
        root.setBackground(new Background(new BackgroundFill(Color.FORESTGREEN, new CornerRadii(10) ,Insets.EMPTY)));

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
        Font font2 = Font.font(choosePrompt.getFont().getFamily(), 14);
        choosePrompt.setFont(font2);
        StackPane textPane = new StackPane(choosePrompt);
        choosePrompt.setFill(Color.BLANCHEDALMOND);
        leftPane.setSpacing(10);
        choosePrompt.setFont(font2);
        ArtistScrape artistScrape = new ArtistScrape();
        Text websiteText = new Text("Text from website");
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
            String artistName = insertBox.getText();
            try {
                String artistDetails = getArtistDetails(artistName);
                String[] artistDetailsArray = artistDetails.split("\n"); // Split artist details into multiple lines
                StringBuilder artistDetailsText = new StringBuilder();
                for (String detail : artistDetailsArray) {
                    artistDetailsText.append(detail).append("\n"); // Update the artistText with artist details
                } artistText1.setText(artistDetailsText.toString());
            }catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });







        artistInfo1.getChildren().addAll(artistImageView, mainArtistText);
        artistInfo2.getChildren().addAll(artistText1, artistText2);
        // Adding some content to the VBox nodes (for demonstration purposes)
        leftPane.getChildren().addAll(insertPrompt, insertBox, choosePrompt, dataButton);
        leftPane.getChildren().add(artistScrape);
        rightPane.getChildren().addAll(artistInfo1, artistInfo2);

        // Adding the VBox nodes as children to the HBox
        root.getChildren().addAll(leftPane, rightPane);
        // Embed the Swing panel in a JavaFX SwingNode



        Scene scene = new Scene(root, 800, 600);


        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public String getArtistDetails(String artistName) throws IOException, InterruptedException {
        String query = artistName.replace(" ", "%20"); // Encode spaces in the artist name
        String apiUrl = API_URL + "?q=" + query + "&type=artist";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + "BQBXGutcYG6AApupkpVMNG2pXWnVOtmdZ63lKkFisRPozT0-lvzbcvaFqenktIQjRmgspD4Y6kA9CrwYd6RtHCE4GUYt2b6eIKB6zEGQ8LuznWpmvTM") // Use "Bearer" prefix for the token
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode == 200) {
            // Handle successful response
            String responseBody = response.body();
            // Process the JSON response here
            System.out.println("Artist Details: " + responseBody);

            // Parse the JSON response to get the artist ID
            JsonObject json = JsonParser.parseString(responseBody).getAsJsonObject();
            String artistId = json.getAsJsonObject("artists")
                    .getAsJsonArray("items")
                    .get(0)
                    .getAsJsonObject()
                    .get("id")
                    .getAsString();

            // Fetch top tracks for the artist
            fetchTopTracks(artistId);
        } else {
            // Handle unsuccessful response
            throw new IOException("Failed to retrieve artist details from Spotify API. Response code: " + statusCode);
        }
        return query;
    }

    private String fetchTopTracks(String artistId) throws IOException, InterruptedException {
        String apiUrl = "https://api.spotify.com/v1/artists/" + artistId + "/top-tracks?country=US";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + "BQCUVeRyK-QqPQ9MfINIqVil_AzpPPfwqXD0ZkbqrYup25MnJmX6ffPPRXoKY1HudhMIBqhp329ggMj8fVPF9SZaq-URhAwF2Yj-YxAjCiE-e6hKD9k") // Use "Bearer" prefix for the token
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode == 200) {
            // Handle successful response
            String responseBody = response.body();
            // Process the JSON response here
            System.out.println("Top Tracks: " + responseBody);
        } else {
            // Handle unsuccessful response
            throw new IOException("Failed to retrieve top tracks from Spotify API. Response code: " + statusCode);
        }
        return apiUrl;
    }

    public static void main(String[] args) {
        System.out.println("sduhksjdhf");
        System.out.println("sduhksjdhf");
        launch();
    }
}