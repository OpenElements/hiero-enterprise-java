package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TokenTransferRequest(@NonNull Hbar maxTransactionFee,
                                   @NonNull Duration transactionValidDuration,
                                   @NonNull TokenId tokenId,
                                   @NonNull List<Long> serials,
                                   @Nullable Long amount,
                                   @NonNull AccountId sender,
                                   @NonNull AccountId receiver,
                                   @NonNull PrivateKey senderKey) implements TransactionRequest {

    public TokenTransferRequest {
        Objects.requireNonNull(maxTransactionFee, "maxTransactionFee must not be null");
        Objects.requireNonNull(transactionValidDuration, "transactionValidDuration must not be null");
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(sender, "sender must not be null");
        Objects.requireNonNull(receiver, "receiver must not be null");
        Objects.requireNonNull(senderKey, "senderKey must not be null");
        Objects.requireNonNull(serials, "serials must not be null");
        if (amount == null && serials.isEmpty()) {
            throw new IllegalArgumentException("either amount or serial must be provided");
        }

        serials.forEach(serial -> {
            if (serial < 0) {
                throw new IllegalArgumentException("serial must be positive");
            }
        });
    }

    public static TokenTransferRequest of(@NonNull final TokenId tokenId, final long serial,
            @NonNull final AccountId sender, @NonNull final AccountId receiver, @NonNull final PrivateKey senderKey) {
        return of(tokenId, List.of(serial), sender, receiver, senderKey);
    }

    public static TokenTransferRequest of(@NonNull final TokenId tokenId, @NonNull final List<Long> serials,
            @NonNull final AccountId sender, @NonNull final AccountId receiver, @NonNull final PrivateKey senderKey) {
        return new TokenTransferRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
                TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION, tokenId, serials, null, sender, receiver,
                senderKey);
    }

    public static TokenTransferRequest of(@NonNull final TokenId tokenId, @NonNull final AccountId sender,
            @NonNull final AccountId receiver, @NonNull final PrivateKey senderKey, @NonNull final Long amount) {
        return new TokenTransferRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
                TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION, tokenId, List.of(), amount, sender, receiver,
                senderKey);
    }
}
