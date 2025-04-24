package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

public record NftTransfer(
        boolean isApproval,
        @Nullable AccountId receiverAccountId,
        @Nullable AccountId senderAccountId,
        long serialNumber,
        @Nullable TokenId tokenId
) {
    public NftTransfer {}
}
