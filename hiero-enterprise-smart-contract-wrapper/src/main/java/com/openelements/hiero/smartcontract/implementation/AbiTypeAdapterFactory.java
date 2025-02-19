package com.openelements.hiero.smartcontract.implementation;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.openelements.hiero.smartcontract.model.AbiEntry;
import com.openelements.hiero.smartcontract.model.AbiModel;
import com.openelements.hiero.smartcontract.model.AbiParameter;

public class AbiTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().equals(AbiModel.class)) {
            return (TypeAdapter<T>) new AbiModelTypeAdapter(gson);
        }
        if (type.getRawType().equals(AbiEntry.class)) {
            return (TypeAdapter<T>) new AbiEntryTypeAdapter(gson);
        }
        if (type.getRawType().equals(AbiParameter.class)) {
            return (TypeAdapter<T>) new AbiParameterTypeAdapter(gson);
        }
        return null;
    }
}
