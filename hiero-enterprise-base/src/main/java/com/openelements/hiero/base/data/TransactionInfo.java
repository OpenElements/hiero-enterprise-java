package com.openelements.hiero.base.data;

import java.util.List;
import java.util.Objects;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TransactionInfo(@Nullable  Byte bytes,
         long chargedTxFee,
         String consensusTimeStamp,
         String entityId,
         String maxFee,
         String memoBase64,
         String name,
         List<NftTransfers> nftTransfers,
         String node,
         int nonce,
         @Nullable  String parentConsensusTimestamp,
         String result,
         boolean scheduled,
         List<StakingRewardTransfers> stakingRewardTransfers,
         List<TokenTransfers> tokenTransfers,
         String transactionHash,
         @NonNull  String transactionId,
         List<Transfers> transfers,
         String validDurationSeconds,
         String validStartTimestamp
) {

    public TransactionInfo(@NonNull String transactionId){
            this(
                    null,                // bytes
                    0L,                  // chargedTxFee
                    null,                // consensusTimeStamp
                    null,                // entityId
                    null,                // maxFee
                    null,                // memoBase64
                    null,                // name
                    List.of(),           // nftTransfers (empty list)
                    null,                // node
                    0,                   // nonce
                    null,                // parentConsensusTimestamp
                    null,                // result
                    false,               // scheduled
                    List.of(),           // stakingRewardTransfers (empty list)
                    List.of(),           // tokenTransfers (empty list)
                    null,                // transactionHash
                    transactionId,       // transactionId
                    List.of(),           // transfers (empty list)
                    null,                // validDurationSeconds
                    null                 // validStartTimestamp
            );
        Objects.requireNonNull(transactionId, "transactionId must not be null");

    }

        public TransactionInfo (Byte bytes,
                             long chargedTxFee,
                             String consensusTimeStamp,
                             String entityId,
                             String maxFee,
                             String memoBase64,
                             String name,
                             List<NftTransfers> nftTransfers,
                             String node, int nonce,
                             String parentConsensusTimestamp,
                             String result,
                             boolean scheduled,
                             List<StakingRewardTransfers> stakingRewardTransfers,
                             List<TokenTransfers> tokenTransfers,
                             String transactionHash,
                             String transactionId,
                             List<Transfers> transfers,
                             String validDurationSeconds,
                             String validStartTimestamp
        ){

            this.bytes= bytes;
            this.chargedTxFee= chargedTxFee;
            this.consensusTimeStamp=consensusTimeStamp;
            this.entityId=entityId;
            this.maxFee= maxFee;
            this.memoBase64=memoBase64;
            this.name=name;
            this.nftTransfers= nftTransfers;
            this.node= node;
            this.nonce= nonce;
            this.parentConsensusTimestamp= parentConsensusTimestamp;
            this.result= result;
            this.scheduled= scheduled;
            this.stakingRewardTransfers= stakingRewardTransfers;
            this.tokenTransfers= tokenTransfers;
            this.transactionHash= transactionHash;
            this.transactionId= transactionId;
            this.transfers= transfers;
            this.validDurationSeconds= validDurationSeconds;
            this.validStartTimestamp= validStartTimestamp;
        }
    }


