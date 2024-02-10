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
    public ArtistScrape(HelloApplication main) {
        this.main = main;
        this.scrapeBillBoard();
    }
    public void scrapeBillBoard() {
        try {
            // Connect to the website and get the HTML
            Document document = Jsoup.connect("https://www.billboard.com/charts/artist-100/").get();

            // Extract elements you're interested in
            Elements elements = document.select("h3"); // Select all <h1> elements
            Button[] artistButtons = new Button[25];
            System.out.println(elements.size());

            // Loop through the elements and create Text objects
            for (int i = 4; i < 25; i+=2) {
                // Select the element with class "name" within the row
                Element element = elements.get(i);
              //  Element nameElement = element.selectFirst("h3");
                if (element != null) {
                    String text = element.text();

                    System.out.println(text);
                    artistButtons[i] = new Button();
                    // Create a Text object and add it to the array
                    artistButtons[i].setText(text);
                    // Set the action to be performed when the button is clicked
                    artistButtons[i].setOnAction(e -> {
                        main.setTextFieldText(text);
                        System.out.println(text);
                        // Add your code here to handle the button click event
                        // For example, you can open a new window, update UI, etc.
                    });
                    this.getChildren().add(artistButtons[i]);
                } else {
                    System.out.println("nulll");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
