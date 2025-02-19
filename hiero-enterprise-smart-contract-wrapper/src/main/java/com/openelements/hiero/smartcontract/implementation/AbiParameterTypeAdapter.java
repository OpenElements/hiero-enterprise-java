package com.openelements.hiero.smartcontract.implementation;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.openelements.hiero.smartcontract.model.AbiParameter;
import com.openelements.hiero.smartcontract.model.AbiParemeterType;
import java.io.IOException;
import java.util.List;

public class AbiParameterTypeAdapter extends BasicTypeAdapter<AbiParameter> {

    public AbiParameterTypeAdapter(Gson gson) {
        super(gson);
    }

    @Override
    public AbiParameter read(JsonReader in) throws IOException {
        return new AbiParameter("FOO", AbiParemeterType.STRING, List.of(), false);
    }
}
