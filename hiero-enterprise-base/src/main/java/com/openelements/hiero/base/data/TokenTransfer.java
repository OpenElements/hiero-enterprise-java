package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record TokenTransfer(
        @NonNull TokenId tokenId,
        @NonNull AccountId account,
        long amount,
        boolean isApproval
) {
    public TokenTransfer {
        Objects.requireNonNull(tokenId, "tokenId cannot be null");
        Objects.requireNonNull(account, "account cannot be null");
    }
}
