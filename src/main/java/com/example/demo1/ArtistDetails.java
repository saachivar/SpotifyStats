package com.example.demo1;

public class ArtistDetails {
    private String artistName;
    private String genre;
    private String imageUrl;

    public ArtistDetails(String artistName, String genre, String imageUrl) {
        this.artistName = artistName;
        this.genre = genre;
        this.imageUrl = imageUrl;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getGenre() {
        return genre;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
