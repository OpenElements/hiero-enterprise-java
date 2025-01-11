package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenSupplyType;
import com.hedera.hashgraph.sdk.TokenType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.util.Objects;

public record TokenInfo(
        @NonNull TokenId tokenId,
        @NonNull TokenType type,
        @NonNull String name,
        @NonNull String symbol,
        @Nullable String memo,
        long decimals,
        byte[] metadata,
        @NonNull Instant createdTimestamp,
        @NonNull Instant modifiedTimestamp,
        @Nullable Instant expiryTimestamp,
        @NonNull TokenSupplyType supplyType,
        @NonNull String initialSupply,
        @NonNull String totalSupply,
        @NonNull String maxSupply,
        @NonNull AccountId treasuryAccountId,
        boolean deleted,
        @NonNull CustomFee customFees
) {
    public TokenInfo {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(symbol, "symbol must not be null");
        Objects.requireNonNull(metadata, "metadata must not be null");
        Objects.requireNonNull(createdTimestamp, "createdTimestamp must not be null");
        Objects.requireNonNull(modifiedTimestamp, "modifiedTimestamp must not be null");
        Objects.requireNonNull(supplyType, "supplyType must not be null");
        Objects.requireNonNull(initialSupply, "initialSupply must not be null");
        Objects.requireNonNull(totalSupply, "totalSupply must not be null");
        Objects.requireNonNull(maxSupply, "maxSupply must not be null");
        Objects.requireNonNull(treasuryAccountId, "treasuryAccountId must not be null");
        Objects.requireNonNull(customFees, "customFees must not be null");
    }
}