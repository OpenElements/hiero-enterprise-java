package com.openelements.hiero.smartcontract.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.openelements.hiero.smartcontract.model.AbiEntry;
import com.openelements.hiero.smartcontract.model.AbiModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public class AbiModelTypeAdapter extends TypeAdapter<AbiModel> {

    private final Gson gson;

    private final TypeAdapter<JsonArray> arrayTypeAdapter;

    public AbiModelTypeAdapter(@NonNull Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
        arrayTypeAdapter = gson.getAdapter(JsonArray.class);
    }

    @Override
    public void write(JsonWriter out, AbiModel value) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public AbiModel read(JsonReader in) throws IOException {
        JsonArray array = arrayTypeAdapter.read(in);
        final List<AbiEntry> entries = new ArrayList<>();
        array.forEach(jsonElement -> {
            entries.add(gson.fromJson(jsonElement, AbiEntry.class));
        });
        return new AbiModel(Collections.unmodifiableList(entries));
    }
}
