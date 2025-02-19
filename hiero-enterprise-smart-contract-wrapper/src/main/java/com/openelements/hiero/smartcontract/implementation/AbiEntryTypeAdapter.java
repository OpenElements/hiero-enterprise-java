package com.openelements.hiero.smartcontract.implementation;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.openelements.hiero.smartcontract.model.AbiEntry;
import com.openelements.hiero.smartcontract.model.AbiEntryType;
import com.openelements.hiero.smartcontract.model.AbiFunction;
import com.openelements.hiero.smartcontract.model.AbiParameter;
import com.openelements.hiero.smartcontract.model.StateMutability;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AbiEntryTypeAdapter extends TypeAdapter<AbiEntry> {

    private final Gson gson;

    private final TypeAdapter<JsonElement> elementAdapter;

    public AbiEntryTypeAdapter(Gson gson) {
        this.gson = Objects.requireNonNull(gson, "gson");
        elementAdapter = gson.getAdapter(JsonElement.class);
    }

    @Override
    public void write(JsonWriter out, AbiEntry value) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbiEntry read(JsonReader in) throws IOException {
        final JsonElement read = elementAdapter.read(in);
        if (read.isJsonObject()) {
            JsonObject abiEntryObject = read.getAsJsonObject();
            final AbiEntryType type = getStringValue(abiEntryObject, "type")
                    .map(AbiEntryType::of)
                    .orElseThrow(() -> new IllegalArgumentException("AbiEntry must have a type"));
            if (type == AbiEntryType.CONSTRUCTOR) {
                final List<AbiParameter> inputs = new ArrayList<>();
                getArray(abiEntryObject, "inputs").forEach(jsonElement -> {
                    inputs.add(gson.fromJson(jsonElement, AbiParameter.class));
                });
                final List<AbiParameter> outputs = new ArrayList<>();
                getArray(abiEntryObject, "outputs").forEach(jsonElement -> {
                    outputs.add(gson.fromJson(jsonElement, AbiParameter.class));
                });
                final StateMutability stateMutability = getStringValue(abiEntryObject, "stateMutability")
                        .map(StateMutability::of)
                        .orElseThrow(() -> new IllegalArgumentException("AbiEntry must have a stateMutability"));
                return new AbiFunction(type, null, Collections.unmodifiableList(inputs),
                        Collections.unmodifiableList(outputs), stateMutability);
            }
            if (type.isCompatibleWithFunction()) {
                final String name = getStringValue(abiEntryObject, "name")
                        .orElseThrow(() -> new IllegalArgumentException("AbiEntry must have a name"));
                final List<AbiParameter> inputs = new ArrayList<>();
                getArray(abiEntryObject, "inputs").forEach(jsonElement -> {
                    inputs.add(gson.fromJson(jsonElement, AbiParameter.class));
                });
                final List<AbiParameter> outputs = new ArrayList<>();
                getArray(abiEntryObject, "outputs").forEach(jsonElement -> {
                    outputs.add(gson.fromJson(jsonElement, AbiParameter.class));
                });
                final StateMutability stateMutability = getStringValue(abiEntryObject, "stateMutability")
                        .map(StateMutability::of)
                        .orElseThrow(() -> new IllegalArgumentException("AbiEntry must have a stateMutability"));
                return new AbiFunction(type, name, Collections.unmodifiableList(inputs),
                        Collections.unmodifiableList(outputs), stateMutability);
            }
        }
        throw new IllegalArgumentException("AbiEntry must be a JsonObject");
    }

    private JsonArray getArray(JsonObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            final JsonElement jsonElement = jsonObject.get(key);
            if (jsonElement.isJsonArray()) {
                return jsonElement.getAsJsonArray();
            }
            final JsonArray array = new JsonArray();
            array.add(jsonElement);
        }
        return new JsonArray();
    }

    private Optional<String> getStringValue(JsonObject jsonObject, String key) {
        if (jsonObject.has(key)) {
            final JsonElement jsonElement = jsonObject.get(key);
            if (jsonElement.isJsonNull()) {
                return Optional.empty();
            }
            return Optional.of(jsonObject.get(key).getAsString());
        }
        return Optional.empty();
    }
}
