package com.openelements.hiero.smartcontract.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Objects;

public abstract class BasicTypeAdapter<T> extends TypeAdapter<T> {

    private final Gson gson;

    private final TypeAdapter<JsonObject> objectTypeAdapter;

    private final TypeAdapter<JsonArray> arrayTypeAdapter;


    public BasicTypeAdapter(final Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
        objectTypeAdapter = gson.getAdapter(JsonObject.class);
        arrayTypeAdapter = gson.getAdapter(JsonArray.class);
    }

    protected JsonObject readObject(JsonReader in) throws IOException {
        return objectTypeAdapter.read(in);
    }

    protected JsonArray readArray(JsonReader in) throws IOException {
        return arrayTypeAdapter.read(in);
    }


    protected <T> T fromJson(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(json, classOfT);
    }


    protected Gson getGson() {
        return gson;
    }

    protected TypeAdapter<JsonObject> getObjectTypeAdapter() {
        return objectTypeAdapter;
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
