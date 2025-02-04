package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import java.time.Duration;
import java.util.Objects;
import java.util.Set;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TokenBurnRequest(@NonNull Hbar maxTransactionFee,
                               @NonNull Duration transactionValidDuration,
                               @NonNull TokenId tokenId,
                               @NonNull PrivateKey supplyKey,
                               @Nullable Long amount,
                               @Nullable Set<Long> serials) implements TransactionRequest {

    public TokenBurnRequest {
        Objects.requireNonNull(maxTransactionFee, "Max transaction fee cannot be null");
        Objects.requireNonNull(transactionValidDuration, "Transaction valid duration cannot be null");
        Objects.requireNonNull(tokenId, "Token ID cannot be null");
        Objects.requireNonNull(supplyKey, "Supply key cannot be null");
    }

    public static TokenBurnRequest of(TokenId tokenId, long serial, PrivateKey supplyKey) {
        return of(tokenId, Set.of(serial), supplyKey);
    }

    public static TokenBurnRequest of(TokenId tokenId, Set<Long> serials, PrivateKey supplyKey) {
        return new TokenBurnRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
                TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION, tokenId, supplyKey, null, serials);
    }

    public static TokenBurnRequest of(TokenId tokenId, PrivateKey supplyKey, long amount) {
        return new TokenBurnRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
                TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION, tokenId, supplyKey, amount, null);
    }
}
