package com.openelements.hiero.smartcontract.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import com.openelements.hiero.smartcontract.model.AbiEntry;
import com.openelements.hiero.smartcontract.model.AbiModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jspecify.annotations.NonNull;

public class AbiModelTypeAdapter extends BasicTypeAdapter<AbiModel> {
    
    public AbiModelTypeAdapter(@NonNull Gson gson) {
        super(gson);
    }

    @Override
    public AbiModel read(JsonReader in) throws IOException {
        JsonArray array = readArray(in);
        final List<AbiEntry> entries = new ArrayList<>();
        array.forEach(jsonElement -> {
            entries.add(fromJson(jsonElement, AbiEntry.class));
        });
        return new AbiModel(Collections.unmodifiableList(entries));
    }
}
