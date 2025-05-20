package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.*;
import com.openelements.hiero.base.mirrornode.TransactionRepository;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;

import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;



public class TransactionRepositoryImpl implements TransactionRepository {
    private final MirrorNodeClient mirrorNodeClient;

    public TransactionRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
        this.mirrorNodeClient = Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
    }

    @NonNull
    @Override
    public Page<TransactionInfo> findByAccount(@NonNull final AccountId accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return this.mirrorNodeClient.queryTransactionsByAccount(accountId);
    }

    @NonNull
    @Override
    public Optional<TransactionInfo> findById(@NonNull final String transactionId) throws HieroException {
        Objects.requireNonNull(transactionId, "transactionId must not be null");

        //query the mirrorNode using the transactionId.


        //Check whether the response is null or empty.


        // return the TransactionInfo object wrapped in an Optional

        return Optional.empty();
    }





}
