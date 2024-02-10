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
    public ArtistScrape() {
        this.scrapeBillBoard();
    }
    public void scrapeBillBoard() {
        try {
            // Connect to the website and get the HTML
            Document document = Jsoup.connect("https://www.billboard.com/charts/artist-100/").get();

            // Extract elements you're interested in
            Elements elements = document.select("p"); // Select all <h1> elements

            Text[] artists = new Text[elements.size()];

            // Loop through the elements and create Text objects
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                String text = element.text();
                System.out.println(text);

                // Create a Text object and add it to the array
                artists[i] = new Text(text);
                this.getChildren().add(artists[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
