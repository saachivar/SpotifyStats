package com.example.demo1;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
public class SpotifyAuthenticator {
    private static final String CLIENT_ID = "acf4183c24a3497dbacebe6795f92812";
    private static final String CLIENT_SECRET = "19a947ca40324be7b904e222bddf479e";
    private static final String AUTH_URL = "https://accounts.spotify.com/api/token";

    public static String getAccessToken() throws IOException {
        String authString = CLIENT_ID + ":" + CLIENT_SECRET;
        String encodedAuthString = Base64.getEncoder().encodeToString(authString.getBytes());

        URL url = new URL(AUTH_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Basic " + encodedAuthString);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setDoOutput(true);

        String requestBody = "grant_type=client_credentials";
        connection.getOutputStream().write(requestBody.getBytes());

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();
            in.close();
            return response.split("\"access_token\":\"")[1].split("\"")[0];
        } else {
            throw new IOException("Failed to obtain access token. Response code: " + responseCode);
        }
    }
}
