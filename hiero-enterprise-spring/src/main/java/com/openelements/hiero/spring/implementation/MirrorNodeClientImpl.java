package com.openelements.hiero.spring.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.*;
import com.openelements.hiero.base.implementation.AbstractMirrorNodeClient;
import com.openelements.hiero.base.implementation.MirrorNodeJsonConverter;
import com.openelements.hiero.base.implementation.MirrorNodeRestClient;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.web.client.RestClient;

public class MirrorNodeClientImpl extends AbstractMirrorNodeClient<JsonNode> {

    private final ObjectMapper objectMapper;

    private final RestClient restClient;

    private final MirrorNodeRestClientImpl mirrorNodeRestClient;

    private final MirrorNodeJsonConverter<JsonNode> jsonConverter;

    /**
     * Constructor.
     *
     * @param restClientBuilder the builder for the REST client that must have the base URL set
     */
    public MirrorNodeClientImpl(final RestClient.Builder restClientBuilder) {
        Objects.requireNonNull(restClientBuilder, "restClientBuilder must not be null");
        mirrorNodeRestClient = new MirrorNodeRestClientImpl(restClientBuilder);
        jsonConverter = new MirrorNodeJsonConverterImpl();
        objectMapper = new ObjectMapper();
        restClient = restClientBuilder.build();
    }

    @Override
    protected final MirrorNodeRestClient<JsonNode> getRestClient() {
        return mirrorNodeRestClient;
    }

    @Override
    protected final MirrorNodeJsonConverter<JsonNode> getJsonConverter() {
        return jsonConverter;
    }

    @Override
    public Page<Nft> queryNftsByAccount(@NonNull final AccountId accountId) throws HieroException {
        Objects.requireNonNull(accountId, "newAccountId must not be null");
        final String path = "/api/v1/accounts/" + accountId + "/nfts";
        final Function<JsonNode, List<Nft>> dataExtractionFunction = node -> jsonConverter.toNfts(node);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public Page<Nft> queryNftsByAccountAndTokenId(@NonNull final AccountId accountId, @NonNull final TokenId tokenId) {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        final String path = "/api/v1/tokens/" + tokenId + "/nfts/?account.id=" + accountId;
        final Function<JsonNode, List<Nft>> dataExtractionFunction = node -> jsonConverter.toNfts(node);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public Page<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) {
        final String path = "/api/v1/tokens/" + tokenId + "/nfts";
        final Function<JsonNode, List<Nft>> dataExtractionFunction = node -> jsonConverter.toNfts(node);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public Page<TransactionInfo> queryTransactionsByAccount(@NonNull final AccountId accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        final String path = "/api/v1/transactions?account.id=" + accountId;
        final Function<JsonNode, List<TransactionInfo>> dataExtractionFunction = n -> jsonConverter.toTransactionInfos(
                n);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public @NonNull Optional<TransactionInfo> queryTransaction(@Nullable Byte bytes, long chargedTxFee, String consensusTimeStamp, String entityId, String maxFee, String memoBase64, String name, List<NftTransfers> nftTransfers, String node, int nonce, @Nullable String parentConsensusTimestamp, String result, boolean scheduled, List<StakingRewardTransfers> stakingRewardTransfers, List<TokenTransfers> tokenTransfers, String transactionHash, @NonNull String transactionId, List<Transfers> transfers, String validDurationSeconds, String validStartTimestamp) throws HieroException {

        final JsonNode jsonNode = mirrorNodeRestClient.queryTransaction(transactionId);
        if (jsonNode == null || jsonNode.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(new TransactionInfo(transactionId));
    }

    @Override
    public Optional<TransactionInfo> queryTransaction(String transactionId) throws HieroException {
        return Optional.empty();
    }


    @Override
    public Page<Token> queryTokensForAccount(@NonNull AccountId accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        final String path = "/api/v1/tokens?account.id=" + accountId;
        final Function<JsonNode, List<Token>> dataExtractionFunction = node -> jsonConverter.toTokens(node);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public @NonNull Page<Balance> queryTokenBalances(TokenId tokenId) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        final String path = "/api/v1/tokens/" + tokenId +"/balances";
        final Function<JsonNode, List<Balance>> dataExtractionFunction = node -> jsonConverter.toBalances(node);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public @NonNull Page<Balance> queryTokenBalancesForAccount(@NonNull TokenId tokenId, @NonNull AccountId accountId) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        final String path = "/api/v1/tokens/" + tokenId +"/balances?account.id=" + accountId;
        final Function<JsonNode, List<Balance>> dataExtractionFunction = node -> jsonConverter.toBalances(node);
        return new RestBasedPage<>(objectMapper, restClient.mutate().clone(), path, dataExtractionFunction);
    }

    @Override
    public @NonNull Page<NftMetadata> findNftTypesByOwner(AccountId ownerId) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public @NonNull Page<NftMetadata> findAllNftTypes() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
