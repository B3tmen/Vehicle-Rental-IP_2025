package org.unibl.etf.promotionsapp.service;

import com.google.gson.Gson;
import lombok.Setter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpService {
    private final Gson gson = new Gson();
    @Setter
    private String authToken;
    private Map<String, HttpURLConnection> connectionPool = new HashMap<>();

    public <T> T post(String url, Object requestBody, Class<T> responseType) throws URISyntaxException, IOException {
        HttpURLConnection connection = prepareConnection(url, "POST");

        try (OutputStream os = connection.getOutputStream()) {
            String jsonBody = gson.toJson(requestBody);
            os.write(jsonBody.getBytes(StandardCharsets.UTF_8));
        }

        return handleResponse(connection, responseType);
    }

    public <T> T get(String url, Class<T> responseType) throws URISyntaxException, IOException {
        HttpURLConnection connection = prepareConnection(url, "GET");
        return handleResponse(connection, responseType);
    }

    private HttpURLConnection prepareConnection(String urlString, String method) throws URISyntaxException, IOException {
        URL url = new URI(urlString).toURL();
        HttpURLConnection connection;

        // Try to reuse connection if available
        if (connectionPool.containsKey(urlString)) {
            connection = connectionPool.get(urlString);
            connection.disconnect(); // Disconnect previous instance
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }

        connection.setRequestMethod(method);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        if (authToken != null && !authToken.isEmpty()) {
            connection.setRequestProperty("Authorization", "Bearer " + authToken);
        }

        if ("POST".equals(method) || "PUT".equals(method)) {
            connection.setDoOutput(true);
        }

        // Store connection in pool
        connectionPool.put(urlString, connection);

        return connection;
    }

    private <T> T handleResponse(HttpURLConnection connection, Class<T> responseType) throws IOException {
        int responseCode = connection.getResponseCode();

        try (InputStream inputStream = connection.getErrorStream() != null
                ? connection.getErrorStream()
                : connection.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String response = reader.lines().collect(Collectors.joining());

            if (responseCode >= HttpURLConnection.HTTP_OK && responseCode < HttpURLConnection.HTTP_MULT_CHOICE) {
                return gson.fromJson(response, responseType);
            } else {
                // Include the response body in the exception
                throw new IOException("HTTP error " + responseCode + ": " + response);
            }
        } finally {
            connection.disconnect(); // Always disconnect
        }
    }

    public void closeEverything() {
        for (HttpURLConnection conn : connectionPool.values()) {
            conn.disconnect();
        }
        connectionPool.clear();
    }

    public void clearAuthToken() {
        this.authToken = null;
    }
}
