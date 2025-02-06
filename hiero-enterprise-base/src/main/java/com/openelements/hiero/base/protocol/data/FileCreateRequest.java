package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record FileCreateRequest(@NonNull Hbar maxTransactionFee,
                                @NonNull Duration transactionValidDuration,
                                @NonNull byte[] contents,
                                @Nullable Instant expirationTime,
                                @Nullable String fileMemo) implements TransactionRequest {

    private static final String DEFAULT_FILE_MEMO = "";

    public static final int FILE_CREATE_MAX_SIZE = 2048;

    public static final int FILE_MAX_SIZE = 1_024_000;

    public FileCreateRequest {
        Objects.requireNonNull(contents, "File contents are required");
        if (contents.length > FILE_CREATE_MAX_SIZE) {
            throw new IllegalArgumentException("File contents must be less than " + FILE_CREATE_MAX_SIZE + " bytes");
        }
    }

    public static FileCreateRequest of(@NonNull byte[] contents) {
        return new FileCreateRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, contents, null,
                DEFAULT_FILE_MEMO);
    }

    public static FileCreateRequest of(@NonNull byte[] contents, @NonNull Instant expirationTime) {
        return new FileCreateRequest(DEFAULT_MAX_TRANSACTION_FEE, DEFAULT_TRANSACTION_VALID_DURATION, contents,
                expirationTime, DEFAULT_FILE_MEMO);
    }
}
