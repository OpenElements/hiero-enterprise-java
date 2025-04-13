package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record TokenAssociateRequest(@NonNull Hbar maxTransactionFee,
                                    @NonNull Duration transactionValidDuration,
                                    @NonNull List<TokenId> tokenIds,
                                    @NonNull AccountId accountId,
                                    @NonNull PrivateKey accountPrivateKey) implements TransactionRequest {

    public TokenAssociateRequest {
        Objects.requireNonNull(tokenIds, "tokenIds must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(accountPrivateKey, "accountPrivateKey must not be null");
    }

    public static TokenAssociateRequest of(@NonNull final TokenId tokenId, @NonNull final AccountId accountId,
                                           @NonNull final PrivateKey accountPrivateKey) {
        return of(List.of(tokenId), accountId, accountPrivateKey);
    }

    public static TokenAssociateRequest of(@NonNull final List<TokenId> tokenIds, @NonNull final AccountId accountId,
            @NonNull final PrivateKey accountPrivateKey) {
        return new TokenAssociateRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
                TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION, tokenIds, accountId, accountPrivateKey);
    }
}
