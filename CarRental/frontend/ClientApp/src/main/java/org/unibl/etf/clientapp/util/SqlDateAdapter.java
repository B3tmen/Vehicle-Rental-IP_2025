package org.unibl.etf.clientapp.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SqlDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return Date.valueOf(jsonElement.getAsString()); // Convert String -> java.sql.Date
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Invalid date format: " + jsonElement.getAsString(), e);
        }
    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(formatter.format(date));
    }
}
