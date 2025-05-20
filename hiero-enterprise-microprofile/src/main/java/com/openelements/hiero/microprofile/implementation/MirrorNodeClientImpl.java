package com.openelements.hiero.microprofile.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.*;
import com.openelements.hiero.base.implementation.AbstractMirrorNodeClient;
import com.openelements.hiero.base.implementation.MirrorNodeJsonConverter;
import com.openelements.hiero.base.implementation.MirrorNodeRestClient;
import jakarta.json.JsonObject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import javax.swing.text.html.Option;

public class MirrorNodeClientImpl extends AbstractMirrorNodeClient<JsonObject> {

    private final MirrorNodeRestClientImpl restClient;

    private final MirrorNodeJsonConverter<JsonObject> jsonConverter;

    public MirrorNodeClientImpl(MirrorNodeRestClientImpl restClient,
            MirrorNodeJsonConverter<JsonObject> jsonConverter) {
        this.restClient = Objects.requireNonNull(restClient, "restClient must not be null");
        this.jsonConverter = Objects.requireNonNull(jsonConverter, "jsonConverter must not be null");
    }

    @Override
    protected @NonNull MirrorNodeRestClient<JsonObject> getRestClient() {
        return restClient;
    }

    @Override
    protected @NonNull MirrorNodeJsonConverter<JsonObject> getJsonConverter() {
        return jsonConverter;
    }

    @Override
    public @NonNull Page<Nft> queryNftsByAccount(@NonNull AccountId accountId) throws HieroException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Page<Nft> queryNftsByAccountAndTokenId(@NonNull AccountId accountId, @NonNull TokenId tokenId)
            throws HieroException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Page<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) throws HieroException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Page<TransactionInfo> queryTransactionsByAccount(@NonNull AccountId accountId)
            throws HieroException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Optional<TransactionInfo> queryTransaction(@Nullable Byte bytes, long chargedTxFee, String consensusTimeStamp, String entityId, String maxFee, String memoBase64, String name, List<NftTransfers> nftTransfers, String nodeString, int nonce, @Nullable String parentConsensusTimestamp, String result, boolean scheduled, List<StakingRewardTransfers> stakingRewardTransfers, List<TokenTransfers> tokenTransfers, String transactionHash, @NonNull String transactionId, List<Transfers> transfers, String validDurationSeconds, String validStartTimestamp) throws HieroException {

        final String path= "/api/v1/transactions";
        final Function<JsonObject, List<TransactionInfo>> dataExtractionFunction= node -> jsonConverter.toTransactionInfos(node);

        RestBasedPage<TransactionInfo> page = new RestBasedPage<>(
                restClient.getTarget(), dataExtractionFunction, path);

        List<TransactionInfo> results = page.getData();

        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }


    @Override
    public Page<Token> queryTokensForAccount(@NonNull AccountId accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        final String path = "/api/v1/tokens?account.id=" + accountId;
        final Function<JsonObject, List<Token>> dataExtractionFunction = node -> jsonConverter.toTokens(node);
        return new RestBasedPage<>(restClient.getTarget(), dataExtractionFunction, path);
    }

    @Override
    public @NonNull Page<Balance> queryTokenBalances(@NonNull TokenId tokenId) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        final String path = "/api/v1/tokens/" + tokenId + "/balances";
        final Function<JsonObject, List<Balance>> dataExtractionFunction = node -> jsonConverter.toBalances(node);
        return new RestBasedPage<>(restClient.getTarget(), dataExtractionFunction, path);
    }

    @Override
    public @NonNull Page<Balance> queryTokenBalancesForAccount(@NonNull TokenId tokenId, @NonNull AccountId accountId) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        final String path = "/api/v1/tokens/" + tokenId + "/balances?account.id=" + accountId;
        final Function<JsonObject, List<Balance>> dataExtractionFunction = node -> jsonConverter.toBalances(node);
        return new RestBasedPage<>(restClient.getTarget(), dataExtractionFunction, path);
    }


    @Override
    public @NonNull Page<NftMetadata> findNftTypesByOwner(AccountId ownerId) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Page<NftMetadata> findAllNftTypes() {
        throw new RuntimeException("Not implemented");
    }
}
