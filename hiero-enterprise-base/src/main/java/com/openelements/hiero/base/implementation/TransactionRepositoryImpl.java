package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.*;
import com.openelements.hiero.base.mirrornode.TransactionRepository;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;


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


        @Nullable Byte bytes = 0;
        long chargedTxFee = 0;
        String consensusTimeStamp = "";
        String entityId = "";
        String maxFee = "";
        String memoBase64 = "";
        String name = "";
        List<NftTransfers> nftTransfers = List.of();
        String node = "";
        int nonce = 0;
        @Nullable  String parentConsensusTimestamp = "";
        String result = "";
        boolean scheduled =false;
        List<StakingRewardTransfers> stakingRewardTransfers = List.of();
        List<TokenTransfers> tokenTransfers = List.of();
        String transactionHash = "";
        List<Transfers> transfers = List.of();
        String validDurationSeconds = "";
        String validStartTimestamp = "";

        return this.mirrorNodeClient.queryTransaction( bytes,
                                                 chargedTxFee,
                                                 consensusTimeStamp,
                                                 entityId,
                                                 maxFee,
                                                 memoBase64,
                                                 name,
                                                 nftTransfers,
                                                 node,
                                                 nonce,
                                                 parentConsensusTimestamp,
                                                 result,
                                                 scheduled,
                                                 stakingRewardTransfers,
                                                 tokenTransfers,
                                                 transactionHash,
                                                 transactionId,
                                                 transfers,
                                                 validDurationSeconds,
                                                 validStartTimestamp);
    }
}
