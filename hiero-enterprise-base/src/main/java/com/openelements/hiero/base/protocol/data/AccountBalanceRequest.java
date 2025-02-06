package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public record AccountBalanceRequest(@NonNull AccountId accountId, Hbar queryPayment, Hbar maxQueryPayment) implements
        QueryRequest {

    public AccountBalanceRequest {
        Objects.requireNonNull(accountId, "newAccountId must not be null");
    }

    @NonNull
    public static AccountBalanceRequest of(@NonNull AccountId accountId) {
        return new AccountBalanceRequest(accountId, null, null);
    }

    @NonNull
    public static AccountBalanceRequest of(@NonNull String accountId) {
        Objects.requireNonNull(accountId, "newAccountId must not be null");
        return of(AccountId.fromString(accountId));
    }

    @Override
    public Hbar queryPayment() {
        return queryPayment;
    }

    @Override
    public Hbar maxQueryPayment() {
        return maxQueryPayment;
    }
}
