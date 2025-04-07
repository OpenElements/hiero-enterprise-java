package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record Transfer(
        @NonNull AccountId account,
        long amount,
        boolean isApproval
) {
    public Transfer {
        Objects.requireNonNull(account, "account cannot be null");
    }
}
