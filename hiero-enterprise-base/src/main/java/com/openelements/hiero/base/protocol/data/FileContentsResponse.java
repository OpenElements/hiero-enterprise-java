package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.FileId;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record FileContentsResponse(@NonNull FileId fileId, @NonNull byte[] contents) {

    public FileContentsResponse {
        Objects.requireNonNull(fileId, "fileId must not be null");
        Objects.requireNonNull(contents, "contents must not be null");
    }
}
