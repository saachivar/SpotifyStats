package com.example.demo1;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.control.TextArea;


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

    private TextField insertBox;
    private Text warning;
    private Text artistText1;
    private Text artistText2;

    private TextArea topTracksTextArea;


    @Override
    public void start(Stage stage) throws IOException, InterruptedException {

        String accessToken = getAccessToken();
        System.out.println("Access token: " + accessToken);
        topTracksTextArea = new TextArea();

        // Creating an HBox as the root node
        HBox root = new HBox();
        root.setBackground(new Background(new BackgroundFill(Color.MEDIUMSEAGREEN, new CornerRadii(10) ,Insets.EMPTY)));

        root.setStyle("-fx-border-color:BLACK; -fx-border-width: 15px;");

        // Creating two VBox nodes
        VBox leftPane = new VBox();

        //LeftVBox elements
        Text insertPrompt = new Text("Insert an artist:");
        Font font = Font.font(insertPrompt.getFont().getFamily(), 20);
        insertPrompt.setStyle("-fx-font: italic 20px \"System\";");
        insertPrompt.setFont(font);
        insertBox = new TextField();
        Text choosePrompt = new Text("... OR choose from the top Billboard 100");
        Font font2 = Font.font(choosePrompt.getFont().getFamily(), 14);
        choosePrompt.setFont(font2);
        StackPane textPane = new StackPane(choosePrompt);
        choosePrompt.setFill(Color.BLANCHEDALMOND);
        leftPane.setSpacing(10);
        choosePrompt.setFont(font2);
        ArtistScrape artistScrape = new ArtistScrape(this);
        Text websiteText = new Text("Text from website");
        Button shuffleButton = new Button("Shuffle Artists");
        Button dataButton = new Button("Get Data");
        HBox buttonHBox = new HBox();
        VBox rightPane = new VBox();
        HBox artistInfo1 = new HBox();
        ImageView artistImageView = new ImageView();
        Text mainArtistText = new Text("text");
        HBox artistInfo2 = new HBox();
        artistText1 = new Text("text");
        artistText2 = new Text("text");
        warning = new Text("");


        // Setting VBox to take half of the available space
        leftPane.setPrefWidth(400);
        leftPane.setPadding(new Insets(25));
        rightPane.setPrefWidth(350);
        rightPane.setPadding(new Insets(25));

        dataButton.setOnAction(event -> {
            String artistName = insertBox.getText();
            if (!artistName.isEmpty()) { // Check if the artist name is provided
                try {
                    String artistDetails = getArtistDetails (artistName);
                    System.out.println(artistDetails);
                  /*  String topTracks = fetchTopTracks(artistDetails);
                    // Update UI elements with artist details and top tracks
                    artistText1.setText("Artist Details: " + artistDetails);
                    artistText2.setText("Top Tracks: " + topTracks); */
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                warning.setText("Insert an artist!");
            }
        });

        shuffleButton.setOnAction(event -> {
            artistScrape.shuffleArtists();
        });





        buttonHBox.getChildren().addAll(shuffleButton, dataButton);
        artistInfo1.getChildren().addAll(artistImageView, mainArtistText);
        artistInfo2.getChildren().addAll(artistText1, artistText2);
        // Adding some content to the VBox nodes (for demonstration purposes)
        leftPane.getChildren().addAll(insertPrompt, insertBox, choosePrompt,artistScrape, buttonHBox, warning);

        rightPane.getChildren().addAll(artistInfo1, artistInfo2);

        // Adding the VBox nodes as children to the HBox
        root.getChildren().addAll(leftPane, rightPane);
        // Embed the Swing panel in a JavaFX SwingNode



        Scene scene = new Scene(root, 800, 600);


        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }



    private String getArtistDetails(String artistName) throws IOException, InterruptedException {
        String query = artistName.replace(" ", "%20"); // Encode spaces in the artist name
        String apiUrl = API_URL + "?q=" + query + "&type=artist";



        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + "BQB_3hGLVjpwBsF5Rpqs5Y3wvssw3V4p1LkZLcRs-wq685KzlDs1Xl6R_4-rEHgETnOzK_h2aL9o1mQ3kpg_BSBTVw5NsqAUymLlUhkSKnmMvvDorIY") // Use "Bearer" prefix for the token
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode == 200) {
            // Handle successful response
            String responseBody = response.body();
            // Process the JSON response here
            System.out.println("Artist Details: " + responseBody);
            //artistText1.setText("Artist Details:" + responseBody);

            // Parse the JSON response to get the artist ID
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonObject artistObject = jsonResponse.getAsJsonObject("artists")
                    .getAsJsonArray("items")
                    .get(0)
                    .getAsJsonObject();

            // Extract required artist details
            String artistName1 = artistObject.get("name").getAsString();
            String genre = extractGenre(artistObject);
            int popularity = artistObject.get("popularity").getAsInt();

            // Format the artist details
            String artistDetails = "Name: " + artistName1 + "\n" +
                    "Genre: " + genre + "\n" +
                    "Popularity: " + popularity;

            // Set the formatted artist details to artistText1
            artistText1.setText(artistDetails);

            // Parse the JSON response to get the artist ID
            String artistId = artistObject.get("id").getAsString();

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
                .header("Authorization", "Bearer " + "BQB_3hGLVjpwBsF5Rpqs5Y3wvssw3V4p1LkZLcRs-wq685KzlDs1Xl6R_4-rEHgETnOzK_h2aL9o1mQ3kpg_BSBTVw5NsqAUymLlUhkSKnmMvvDorIY") // Use "Bearer" prefix for the token
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode == 200) {
            // Handle successful response
            String responseBody = response.body();
            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            JsonArray tracks = jsonResponse.getAsJsonArray("tracks");
            StringBuilder trackNames = new StringBuilder();
            for (JsonElement track : tracks) {
                JsonObject trackObject = track.getAsJsonObject();
                String trackName = trackObject.get("name").getAsString();
                trackNames.append(trackName).append("\n");
            }
            String topTracks = trackNames.toString();
            artistText2.setText("Top Tracks: " + topTracks);
            // Process the JSON response here
            //System.out.println("Top Tracks: " + responseBody);
          //  artistText1.setText("Artist Details: " + artistDetails);
            //artistText2.setText("Top Tracks: " + responseBody);

        } else {
            // Handle unsuccessful response
            throw new IOException("Failed to retrieve top tracks from Spotify API. Response code: " + statusCode);
        }
        return apiUrl;
    }

    public void setTextFieldText(String text) {
        insertBox.setText(text);
    }

    private String extractGenre(JsonObject artistObject) {
        // Extract genre from the artistObject and return it
        // For example:
        JsonArray genresArray = artistObject.getAsJsonArray("genres");
        if (genresArray.size() > 0) {
            return genresArray.get(0).getAsString(); // Assuming you want to get the first genre
        } else {
            return "Unknown";
        }
    }



    public static void main(String[] args) {
        System.out.println("sduhksjdhff");
        System.out.println("sdukhksjdhf");
        launch();
    }
}