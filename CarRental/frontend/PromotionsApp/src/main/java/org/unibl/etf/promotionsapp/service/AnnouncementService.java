package org.unibl.etf.promotionsapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import org.unibl.etf.promotionsapp.model.dto.Announcement;
import org.unibl.etf.promotionsapp.model.dto.Promotion;
import org.unibl.etf.promotionsapp.util.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnnouncementService {

    private String token;

    public AnnouncementService(String token) {
        this.token = token;
    }


    public List<Announcement> getAll(){

        List<Announcement> announcements = new ArrayList<>();
        try {
            URL url = new URI(Constants.API_ANNOUNCEMENTS_URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder responseBuilder = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    responseBuilder.append(line);
                }
                in.close();

                ObjectMapper mapper = new ObjectMapper();
                Announcement[] promotionsArray = mapper.readValue(responseBuilder.toString(), Announcement[].class);
                announcements = List.of(promotionsArray);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return announcements;
    }
}
