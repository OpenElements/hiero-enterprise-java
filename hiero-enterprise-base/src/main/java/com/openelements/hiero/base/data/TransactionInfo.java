package com.openelements.hiero.base.data;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import com.openelements.hiero.base.protocol.data.TransactionType;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TransactionInfo(
        @NonNull String transactionId,
        byte[] bytes,
        long chargedTxFee,
        @NonNull Instant consensusTimestamp,
        @Nullable String entityId,
        @NonNull String maxFee,
        byte[] memo,
        @NonNull TransactionType name,
        @NonNull List<NftTransfer> nftTransfers,
        @Nullable String node,
        int nonce,
        @Nullable Instant parentConsensusTimestamp,
        @NonNull String result,
        boolean scheduled,
        @NonNull List<StakingRewardTransfer> stakingRewardTransfers,
        @NonNull List<TokenTransfer> tokenTransfers,
        byte[] transactionHash,
        @NonNull List<Transfer> transfers,
        @NonNull String validDurationSeconds,
        @NonNull Instant validStartTimestamp

) {
    public TransactionInfo {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        Objects.requireNonNull(consensusTimestamp, "consensusTimestamp must not be null");
        Objects.requireNonNull(maxFee, "maxFee must not be null");
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(nftTransfers, "nftTransfers must not be null");
        Objects.requireNonNull(stakingRewardTransfers, "stakingRewardTransfers must not be null");
        Objects.requireNonNull(tokenTransfers, "tokenTransfers must not be null");
        Objects.requireNonNull(transfers, "transfers must not be null");
        Objects.requireNonNull(validDurationSeconds, "validDurationSeconds must not be null");
        Objects.requireNonNull(validStartTimestamp, "validStartTimestamp must not be null");

    }
}
