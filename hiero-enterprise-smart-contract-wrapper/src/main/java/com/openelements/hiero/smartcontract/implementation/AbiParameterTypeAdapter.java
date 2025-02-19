package com.openelements.hiero.smartcontract.implementation;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.openelements.hiero.smartcontract.model.AbiParameter;
import com.openelements.hiero.smartcontract.model.AbiParemeterType;
import java.io.IOException;
import java.util.List;

public class AbiParameterTypeAdapter extends TypeAdapter<AbiParameter> {

    private final Gson gson;

    public AbiParameterTypeAdapter(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void write(JsonWriter out, AbiParameter value) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public AbiParameter read(JsonReader in) throws IOException {
        return new AbiParameter("FOO", AbiParemeterType.STRING, List.of(), false);
    }
}
