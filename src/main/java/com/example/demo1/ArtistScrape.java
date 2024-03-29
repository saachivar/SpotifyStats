package com.example.demo1;


import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;


import javafx.scene.control.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;




import static com.example.demo1.SpotifyAuthenticator.getAccessToken;
public class ArtistScrape extends VBox {
    private HelloApplication main;
    private Button[] artistButtons = new Button[25];
    Elements elements;
    public ArtistScrape(HelloApplication main) {
        this.main = main;
        this.scrapeBillBoard();
    }
    public void scrapeBillBoard() {
        try {
            // Connect to the website and get the HTML
            Document document = Jsoup.connect("https://www.billboard.com/charts/artist-100/").get();

            // Extract elements you're interested in
            elements = document.select("h3"); // Select all <h1> elements


            // Loop through the elements and create Text objects
            for (int i = 4; i < 25; i++) {
                int k = (int) (Math.random() * 21) + 4;

                // Select the element with class "name" within the row
                Element element = elements.get(k);
              //  Element nameElement = element.selectFirst("h3");
                if (element != null && !(element.text().equals("Imprint/Promotion Label:"))) {
                    String text = element.text();


                    artistButtons[i] = new Button();
                    // Create a Text object and add it to the array
                    artistButtons[i].setText(text);
                    artistButtons[i].setFont(Font.font("Spotify Circular", 10));
                    artistButtons[i].setStyle(" -fx-font-style:italic; -fx-font-weight:bold");
                    // Set the action to be performed when the button is clicked
                    artistButtons[i].setOnAction(e -> {
                        main.setTextFieldText(text);

                        // Add your code here to handle the button click event
                        // For example, you can open a new window, update UI, etc.
                    });
                    this.getChildren().add(artistButtons[i]);
                } else {
                    System.out.print("");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int artistShuffle = 25;
    public void shuffleArtists() {
        for (int i = artistShuffle; i < artistShuffle + 25; i++) {


            // Select the element with class "name" within the row
            Element element = elements.get(i);
            if (element != null && !(element.text().equals("Imprint/Promotion Label:" ))) {
                if (artistButtons[i-artistShuffle]!=null) {
                    artistButtons[i - artistShuffle].setText(element.text());
                    artistButtons[i - artistShuffle].setOnAction(e -> {
                        main.setTextFieldText(element.text());
                        // Add your code here to handle the button click event
                        // For example, you can open a new window, update UI, etc.
                    });
                }
            }

        }
        if (artistShuffle > elements.size() - 25) {
            artistShuffle = 0;
        } else {
            artistShuffle += 25;
        }
    }
}
