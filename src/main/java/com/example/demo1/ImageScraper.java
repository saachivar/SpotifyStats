package com.example.demo1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;

public class ImageScraper {
    private String artistName;
    private static String imageUrl;
    public ImageScraper(String artistName) {
        this.artistName = artistName;
        searchAndPrintImageUrls(this.artistName);
    }

    private static void searchAndPrintImageUrls(String query) {
        try {
            // Perform a Google image search
            Document document = Jsoup.connect("https://www.google.com/search?tbm=isch&q=" + query +" artist").get();
            Elements imgElements = document.select("img[data-src]");
            // Check if there are any image results
            if (!imgElements.isEmpty()) {
                // Get the URL of the first image
                Element firstImgElement = imgElements.first();
                imageUrl = firstImgElement.attr("data-src");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
