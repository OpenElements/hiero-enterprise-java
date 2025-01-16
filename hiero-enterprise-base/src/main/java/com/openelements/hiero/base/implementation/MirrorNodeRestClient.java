package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public interface MirrorNodeRestClient<JSON> {

    @NonNull
    default JSON queryNftsByTokenIdAndSerial(@NonNull final TokenId tokenId, @NonNull final long serialNumber)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        if (serialNumber <= 0) {
            throw new IllegalArgumentException("serialNumber must be positive");
        }
        return doGetCall("/api/v1/tokens/" + tokenId + "/nfts/" + serialNumber);
    }

    @NonNull
    default JSON queryTransaction(@NonNull final String transactionId) throws HieroException {
        Objects.requireNonNull(transactionId, "transactionId must not be null");
        return doGetCall("/api/v1/transactions/" + transactionId);
    }

    @NonNull
    default JSON queryAccount(@NonNull AccountId accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return doGetCall("/api/v1/accounts/" + accountId);
    }

    @NonNull
    default JSON queryExchangeRates() throws HieroException {
        return doGetCall("/api/v1/network/exchangerate");
    }

    @NonNull
    default JSON queryNetworkFees() throws HieroException {
        return doGetCall("/api/v1/network/fees");
    }

    @NonNull
    default JSON queryNetworkStake() throws HieroException {
        return doGetCall("/api/v1/network/stake");
    }

    @NonNull
    default JSON queryNetworkSupplies() throws HieroException {
        return doGetCall("/api/v1/network/supply");
    }

    @NonNull
    default  JSON queryTokenById(TokenId tokenId) throws HieroException {
        return doGetCall("/api/v1/tokens/" + tokenId);
    }

    @NonNull
    JSON doGetCall(@NonNull String path) throws HieroException;
}
