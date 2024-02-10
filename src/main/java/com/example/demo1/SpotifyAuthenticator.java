package com.example.demo1;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class SpotifyAuthenticator {
    private static final String CLIENT_ID = "acf4183c24a3497dbacebe6795f92812";
    private static final String CLIENT_SECRET = "19a947ca40324be7b904e222bddf479e";
    private static final String AUTH_URL = "https://accounts.spotify.com/api/token";

    private HttpClient httpClient = HttpClient.newBuilder().build();

    private Gson gson = new Gson();

    public static String getAccessToken() throws IOException, InterruptedException {
        String authString = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes(StandardCharsets.UTF_8));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AUTH_URL))
                .header("Authorization", "Basic " + encodedAuthString)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials"))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        System.out.println("status code is" + statusCode);
        if (statusCode == 200) {
            return response.body(); // to return the json string
            //JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
            //return jsonResponse.get("access_token").getAsString();
        } else {
            throw new IOException("Failed to obtain access token. Response code: " + statusCode);
        }
    }
}