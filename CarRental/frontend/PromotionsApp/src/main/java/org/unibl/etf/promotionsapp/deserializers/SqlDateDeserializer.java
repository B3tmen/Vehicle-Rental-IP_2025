package org.unibl.etf.promotionsapp.deserializers;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SqlDateDeserializer implements JsonDeserializer<Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date deserialize(JsonElement json, Type typeOfT,
                            JsonDeserializationContext context) throws JsonParseException {
        try {
            java.util.Date parsedDate = dateFormat.parse(json.getAsString());
            return new Date(parsedDate.getTime());
        } catch (ParseException e) {
            throw new JsonParseException("Could not parse date: " + json.getAsString(), e);
        }
    }
}
