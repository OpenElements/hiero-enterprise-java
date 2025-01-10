package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record Balance(@NonNull AccountId accountId, long balance, long decimals) {
    public Balance {
        Objects.requireNonNull(accountId, "accountId must not be null");
    }
}
