package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public record StakingRewardTransfer(
        @NonNull AccountId account,
        long amount
) {
    public StakingRewardTransfer {
        Objects.requireNonNull(account, "account cannot be null");
    }
}