package com.openelements.hiero.smartcontract;

import com.openelements.hiero.smartcontract.model.AbiModel;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Path;
import org.jspecify.annotations.NonNull;

public interface AbiParser {

    @NonNull
    AbiModel parse(@NonNull URL url) throws Exception;

    @NonNull
    AbiModel parse(@NonNull Path abiPath) throws Exception;

    @NonNull
    AbiModel parse(@NonNull String abi) throws Exception;

    @NonNull
    AbiModel parse(@NonNull Reader abiReader) throws Exception;
}
