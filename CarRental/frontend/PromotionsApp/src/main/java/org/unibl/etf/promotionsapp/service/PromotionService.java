package org.unibl.etf.promotionsapp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.unibl.etf.promotionsapp.deserializers.SqlDateDeserializer;
import org.unibl.etf.promotionsapp.model.dto.Promotion;
import org.unibl.etf.promotionsapp.util.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PromotionService {

    private Gson gson;
    private String token;

    public PromotionService(String token) {
        gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new SqlDateDeserializer())
                .create();
        this.token = token;
    }

    public List<Promotion> getAll(){
        List<Promotion> promotions = new ArrayList<>();

        try {
            URL url = new URI(Constants.API_PROMOTIONS_URL).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + token);

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                promotions = gson.fromJson(in, new TypeToken<List<Promotion>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return promotions;
    }
}
