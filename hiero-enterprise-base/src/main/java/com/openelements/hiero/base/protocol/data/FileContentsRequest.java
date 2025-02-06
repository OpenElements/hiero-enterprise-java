package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record FileContentsRequest(@NonNull FileId fileId, @Nullable Hbar queryPayment,
                                  @Nullable Hbar maxQueryPayment) implements QueryRequest {

    public FileContentsRequest {
        Objects.requireNonNull(fileId, "fileId must not be null");
    }

    @NonNull
    public static FileContentsRequest of(@NonNull String fileId) {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return of(FileId.fromString(fileId));
    }

    @NonNull
    public static FileContentsRequest of(@NonNull FileId fileId) {
        return new FileContentsRequest(fileId, null, null);
    }

    @Override
    public Hbar queryPayment() {
        return queryPayment;
    }

    @Override
    public Hbar maxQueryPayment() {
        return maxQueryPayment;
    }
}
