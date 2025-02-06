package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.protocol.data.AccountBalanceRequest;
import com.openelements.hiero.base.protocol.data.AccountBalanceResponse;
import com.openelements.hiero.base.protocol.data.AccountCreateRequest;
import com.openelements.hiero.base.protocol.data.AccountCreateResult;
import com.openelements.hiero.base.protocol.data.AccountDeleteRequest;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public class AccountClientImpl implements AccountClient {

    private final ProtocolLayerClient client;

    public AccountClientImpl(@NonNull final ProtocolLayerClient client) {
        this.client = Objects.requireNonNull(client, "client must not be null");
    }

    @NonNull
    @Override
    public Account createAccount(@NonNull Hbar initialBalance) throws HieroException {
        if (initialBalance == null) {
            throw new NullPointerException("initialBalance must not be null");
        }

        if (initialBalance.toTinybars() < 0) {
            throw new HieroException("Invalid initial balance: must be non-negative");
        }

        try {
            final AccountCreateRequest request = AccountCreateRequest.of(initialBalance);
            final AccountCreateResult result = client.executeAccountCreateTransaction(request);
            return result.newAccount();
        } catch (IllegalArgumentException e) {
            throw new HieroException("Error while creating Account", e);
        }
    }


    @Override
    public void deleteAccount(@NonNull Account account) throws HieroException {
        final AccountDeleteRequest request = AccountDeleteRequest.of(account);
        client.executeAccountDeleteTransaction(request);
    }

    @Override
    public void deleteAccount(@NonNull Account account, @NonNull Account toAccount) throws HieroException {
        final AccountDeleteRequest request = AccountDeleteRequest.of(account, toAccount);
        client.executeAccountDeleteTransaction(request);
    }

    @NonNull
    @Override
    public Hbar getAccountBalance(@NonNull AccountId account) throws HieroException {
        final AccountBalanceRequest request = AccountBalanceRequest.of(account);
        final AccountBalanceResponse response = client.executeAccountBalanceQuery(request);
        return response.hbars();
    }

    @Override
    public @NonNull Hbar getOperatorAccountBalance() throws HieroException {
        return getAccountBalance(client.getOperatorAccountId());
    }
}
