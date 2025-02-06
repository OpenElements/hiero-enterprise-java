package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record TokenAssociateRequest(@NonNull Hbar maxTransactionFee,
                                    @NonNull Duration transactionValidDuration,
                                    @NonNull TokenId tokenId,
                                    @NonNull AccountId accountId,
                                    @NonNull PrivateKey accountPrivateKey) implements TransactionRequest {

    public TokenAssociateRequest {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(accountId, "newAccountId must not be null");
        Objects.requireNonNull(accountPrivateKey, "accountPrivateKey must not be null");
    }

    public static TokenAssociateRequest of(@NonNull final TokenId tokenId, @NonNull final AccountId accountId,
            @NonNull final PrivateKey accountPrivateKey) {
        return new TokenAssociateRequest(TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
                TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION, tokenId, accountId, accountPrivateKey);
    }

}
