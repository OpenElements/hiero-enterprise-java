package com.openelements.hiero.smartcontract.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.openelements.hiero.smartcontract.AbiParser;
import com.openelements.hiero.smartcontract.model.AbiModel;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public class GsonAbiParser implements AbiParser {

    private final Gson gson;

    public GsonAbiParser() {
        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(new AbiTypeAdapterFactory())
                .create();

    }

    public @NonNull AbiModel parse(@NonNull URL url) throws IOException, URISyntaxException {
        Objects.requireNonNull(url, "url");
        return parse(Path.of(url.toURI()));
    }

    public @NonNull AbiModel parse(@NonNull Path abiPath) throws IOException {
        Objects.requireNonNull(abiPath, "abiPath");
        return parse(Files.readString(abiPath));
    }


    public @NonNull AbiModel parse(@NonNull String abi) {
        Objects.requireNonNull(abi, "abi");
        return parse(new StringReader(abi));
    }

    public @NonNull AbiModel parse(@NonNull Reader abiReader) {
        Objects.requireNonNull(abiReader, "abiReader");
        return gson.fromJson(abiReader, AbiModel.class);
    }
}
