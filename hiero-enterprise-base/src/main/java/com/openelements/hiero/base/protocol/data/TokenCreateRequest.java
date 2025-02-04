package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenType;
import com.openelements.hiero.base.data.Account;
import java.time.Duration;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TokenCreateRequest(@NonNull Hbar maxTransactionFee,
                                 @NonNull Duration transactionValidDuration,
                                 @NonNull String name,
                                 @NonNull String symbol,
                                 @NonNull AccountId treasuryAccountId,
                                 @NonNull PrivateKey treasuryKey,
                                 @NonNull TokenType tokenType,
                                 @Nullable PrivateKey supplyKey) implements TransactionRequest {

    static final int MAX_SYMBOL_LENGTH = 100;

    public TokenCreateRequest {
        Objects.requireNonNull(maxTransactionFee, "Max transaction fee cannot be null");
        Objects.requireNonNull(transactionValidDuration, "Transaction valid duration cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(symbol, "Symbol cannot be null");
        Objects.requireNonNull(treasuryAccountId, "Treasury account ID cannot be null");
        Objects.requireNonNull(tokenType, "Token type cannot be null");
        if (symbol.length() > MAX_SYMBOL_LENGTH) {
            throw new IllegalArgumentException("Symbol length must be less than or equal to " + MAX_SYMBOL_LENGTH);
        }
    }

    public static TokenCreateRequest of(@NonNull final String name, @NonNull final String symbol,
            @NonNull final Account account) {
        return of(name, symbol, account.accountId(), account.privateKey(), TokenType.FUNGIBLE_COMMON,
                account.privateKey());
    }

    public static TokenCreateRequest of(@NonNull final String name, @NonNull final String symbol,
            @NonNull TokenType tokenType,
            @NonNull final Account account) {
        return of(name, symbol, account.accountId(), account.privateKey(), tokenType,
                account.privateKey());
    }

    public static TokenCreateRequest of(@NonNull final String name, @NonNull final String symbol,
            @NonNull final AccountId treasuryAccountId, @NonNull final PrivateKey treasuryKey) {
        return of(name, symbol, treasuryAccountId, treasuryKey, TokenType.FUNGIBLE_COMMON);
    }

    public static TokenCreateRequest of(@NonNull final String name, @NonNull final String symbol,
            @NonNull final AccountId treasuryAccountId, @NonNull final PrivateKey treasuryKey,
            @NonNull final TokenType tokenType) {
        return new TokenCreateRequest(Hbar.from(100), TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION, name,
                symbol, treasuryAccountId, treasuryKey, tokenType, null);
    }

    public static TokenCreateRequest of(@NonNull final String name, @NonNull final String symbol,
            @NonNull final AccountId treasuryAccountId, @NonNull final PrivateKey treasuryKey,
            @NonNull final TokenType tokenType, @NonNull final PrivateKey supplyKey) {
        return new TokenCreateRequest(Hbar.from(100), TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION, name,
                symbol, treasuryAccountId, treasuryKey, tokenType, supplyKey);
    }
}
