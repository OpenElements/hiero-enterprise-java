package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TopicId;
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
    default JSON queryTopicById(TopicId topicId) throws HieroException {
        return doGetCall("/api/v1/topics/" + topicId);
    }

    @NonNull
    default JSON queryTopicMessageBySequenceNumber(TopicId topicId, long sequenceNumber) throws HieroException {
        return doGetCall("/api/v1/topics/" + topicId + "/messages/" + sequenceNumber);
    }

    @NonNull
    JSON doGetCall(@NonNull String path) throws HieroException;

    @NonNull
    default JSON queryContracts() throws HieroException {
        return doGetCall("/api/v1/contracts");
    }

    @NonNull
    default JSON queryContractById(@NonNull final ContractId contractId) throws HieroException {
        Objects.requireNonNull(contractId, "contractId must not be null");
        return doGetCall("/api/v1/contracts/" + contractId);
    }

    @NonNull
    default JSON queryContractsByEvmAddress(@NonNull final String evmAddress) throws HieroException {
        Objects.requireNonNull(evmAddress, "evmAddress must not be null");
        return doGetCall("/api/v1/contracts?evm.address=" + evmAddress);
    }

    @NonNull
    default JSON queryContractsByFileId(@NonNull final String fileId) throws HieroException {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return doGetCall("/api/v1/contracts?file.id=" + fileId);
    }

    @NonNull
    default JSON queryContractsByProxyAccountId(@NonNull final String proxyAccountId) throws HieroException {
        Objects.requireNonNull(proxyAccountId, "proxyAccountId must not be null");
        return doGetCall("/api/v1/contracts?proxy.account.id=" + proxyAccountId);
    }
}
