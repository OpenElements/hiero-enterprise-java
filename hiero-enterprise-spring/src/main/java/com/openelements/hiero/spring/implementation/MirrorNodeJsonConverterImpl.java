package com.openelements.hiero.spring.implementation;

import com.fasterxml.jackson.databind.JsonNode;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenSupplyType;
import com.hedera.hashgraph.sdk.TokenType;
import com.openelements.hiero.base.data.*;
import com.openelements.hiero.base.implementation.MirrorNodeJsonConverter;
import java.math.BigInteger;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jakarta.json.JsonArray;
import jakarta.json.JsonValue;
import org.jspecify.annotations.NonNull;
import jakarta.json.JsonObject;

public class MirrorNodeJsonConverterImpl implements MirrorNodeJsonConverter<JsonNode> {

    @Override
    public Optional<Nft> toNft(final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }
        try {
            final TokenId parsedTokenId = TokenId.fromString(node.get("token_id").asText());
            final AccountId account = AccountId.fromString(node.get("account_id").asText());
            final long serial = node.get("serial_number").asLong();
            final byte[] metadata = node.get("metadata").binaryValue();
            return Optional.of(new Nft(parsedTokenId, serial, account, metadata));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @Override
    public Optional<NetworkSupplies> toNetworkSupplies(final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }
        try {
            final String releasedSupply = node.get("released_supply").asText();
            final String totalSupply = node.get("total_supply").asText();
            return Optional.of(new NetworkSupplies(releasedSupply, totalSupply));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @Override
    public Optional<NetworkStake> toNetworkStake(final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }
        try {
            final long maxStakeReward = node.get("max_stake_rewarded").asLong();
            final long maxStakeRewardPerHbar = node.get("max_staking_reward_rate_per_hbar").asLong();
            final long maxTotalReward = node.get("max_total_reward").asLong();
            final double nodeRewardFeeFraction = node.get("node_reward_fee_fraction").asDouble();
            final long reservedStakingRewards = node.get("reserved_staking_rewards").asLong();
            final long rewardBalanceThreshold = node.get("reward_balance_threshold").asLong();
            final long stakeTotal = node.get("stake_total").asLong();
            final long stakingPeriodDuration = node.get("staking_period_duration").asLong();
            final long stakingPeriodsStored = node.get("staking_periods_stored").asLong();
            final double stakingRewardFeeFraction = node.get("staking_reward_fee_fraction").asDouble();
            final long stakingRewardRate = node.get("staking_reward_rate").asLong();
            final long stakingStartThreshold = node.get("staking_start_threshold").asLong();
            final long unreservedStakingRewardBalance = node.get("unreserved_staking_reward_balance").asLong();

            return Optional.of(new NetworkStake(
                    maxStakeReward,
                    maxStakeRewardPerHbar,
                    maxTotalReward,
                    nodeRewardFeeFraction,
                    reservedStakingRewards,
                    rewardBalanceThreshold,
                    stakeTotal,
                    stakingPeriodDuration,
                    stakingPeriodsStored,
                    stakingRewardFeeFraction,
                    stakingRewardRate,
                    stakingStartThreshold,
                    unreservedStakingRewardBalance
            ));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @Override
    public Optional<ExchangeRates> toExchangeRates(final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }
        try {
            final int currentCentEquivalent = node.get("current_rate").get("cent_equivalent").asInt();
            final int currentHbarEquivalent = node.get("current_rate").get("hbar_equivalent").asInt();
            final Instant currentExpirationTime = Instant.ofEpochSecond(
                    node.get("current_rate").get("expiration_time").asLong()
            );

            final int nextCentEquivalent = node.get("next_rate").get("cent_equivalent").asInt();
            final int nextHbarEquivalent = node.get("next_rate").get("hbar_equivalent").asInt();
            final Instant nextExpirationTime = Instant.ofEpochSecond(
                    node.get("next_rate").get("expiration_time").asLong()
            );

            return Optional.of(new ExchangeRates(
                    new ExchangeRate(currentCentEquivalent, currentHbarEquivalent, currentExpirationTime),
                    new ExchangeRate(nextCentEquivalent, nextHbarEquivalent, nextExpirationTime)
            ));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @Override
    public Optional<AccountInfo> toAccountInfo(final JsonNode node) {
        try {
            final AccountId accountId = AccountId.fromString(node.get("account").asText());
            final String evmAddress = node.get("evm_address").asText();
            final long ethereumNonce = node.get("ethereum_nonce").asLong();
            final long pendingReward = node.get("pending_reward").asLong();
            final long balance = node.get("balance").get("balance").asLong();
            return Optional.of(new AccountInfo(accountId, evmAddress, balance, ethereumNonce, pendingReward));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @Override
    public List<NetworkFee> toNetworkFees(final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return List.of();
        }

        if (!node.has("fees")) {
            return List.of();
        }

        final JsonNode feesNode = node.get("fees");
        return jsonArrayToStream(feesNode)
                .map(n -> {
                    try {
                        final long gas = n.get("gas").asLong();
                        final String transactionType = n.get("transaction_type").asText();
                        return new NetworkFee(gas, transactionType);
                    } catch (final Exception e) {
                        throw new JsonParseException(n, e);
                    }
                })
                .toList();
    }

    @Override
    public @NonNull List<TransactionInfo> toTransactionInfos(@NonNull JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return List.of();
        }

        if (!node.has("transactions")) {
            return List.of();
        }

        final JsonNode transactionsNode = node.get("transactions");
        return jsonArrayToStream(transactionsNode)
                .map(n -> {
                    try {



                        // Converting NftTransfers Object.
                        JsonNode nftTransfersNode= transactionsNode.get("nft_transfers");
                        List<NftTransfers> nftTransfers= new ArrayList<>();

                        if(nftTransfersNode !=null){
                            for(JsonNode value: nftTransfersNode){

                                final boolean isApproval= value.get("is_approval").asBoolean();
                                final String receiverAccountId= value.get("receiver_account_id").asText();
                                final String senderAccountId= value.get("sender_account_id").asText();
                                final int serialNumber= value.get("serial_number").asInt();
                                final String tokenId= value.get("token_id").asText();

                                nftTransfers.add(new NftTransfers(isApproval, receiverAccountId, senderAccountId,serialNumber, tokenId));
                            }
                        }

                        // Converting StakingRewardTransfers Object.
                        JsonNode stakingRewardTransfersNode = transactionsNode.get("staking_reward_transfers");
                        List<StakingRewardTransfers> stakingRewardTransfers= new ArrayList<>();

                        if(stakingRewardTransfersNode !=null && !stakingRewardTransfersNode.isEmpty()){
                            for(JsonNode value: stakingRewardTransfersNode ){
                                stakingRewardTransfers.add(new StakingRewardTransfers());
                            }
                        }

                        // Converting TokenTransfers Object.
                        JsonNode tokenTransfersNode = transactionsNode.get("token_transfers");
                        List<TokenTransfers> tokenTransfers= new ArrayList<>();

                        if(tokenTransfersNode !=null && !tokenTransfersNode.isEmpty()){
                            for(JsonNode value: tokenTransfersNode ){
                                tokenTransfers.add(new TokenTransfers());
                            }
                        }

                        // Converting Transfers Object.
                        JsonNode transfersNode= transactionsNode.get("transfers");
                        List<Transfers> transfers= new ArrayList<>();

                        if(transfersNode != null){
                            for(JsonNode value: transfersNode){

                                final String account= value.get("account").asText();
                                final long amount= value.get("amount").asLong();
                                final boolean isApproval= value.get("is_approval").asBoolean();

                                transfers.add(new Transfers(account, amount, isApproval));
                            }
                        }

                        final Byte bytes =  (byte) n.get("bytes").asInt();
                        final long chargedTxFee= n.get("charged_tx_fee").asLong();
                        final String consensusTimeStamp = n.get("consensus_timestamp").asText();
                        final String entityId = n.get("entity_id").asText();
                        final String maxFee= n.get("max_fee").asText();
                        final String memoBase64= n.get("memo_base64").asText();
                        final String name= n.get("name").asText();
                        final String nodeResult= n.get("node").asText();
                        final int nonce= n.get("nonce").asInt();
                        final String parentConsensusTimestamp= n.get("parent_consensus_timestamp").asText();
                        final String result= n.get("result").asText();
                        final boolean scheduled= n.get("scheduled").asBoolean();
                        final String transactionHash= n.get("transaction_hash").asText();
                        final String transactionId = n.get("transaction_id").asText();
                        final String validDurationSeconds= n.get("valid_duration_seconds").asText();
                        final String validStartTimestamp= n.get("valid_start_timestamp").asText();

                        return new TransactionInfo(
                                bytes,
                                chargedTxFee,
                                consensusTimeStamp,
                                entityId,
                                maxFee,
                                memoBase64,
                                name,
                                nftTransfers,
                                nodeResult,
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
                                validStartTimestamp );

                    } catch (final Exception e) {
                        throw new JsonParseException(n, e);
                    }
                }).toList();
    }

    @Override
    public List<Nft> toNfts(@NonNull JsonNode node) {
        if (!node.has("nfts")) {
            return List.of();
        }
        final JsonNode nftsNode = node.get("nfts");
        if (!nftsNode.isArray()) {
            throw new IllegalArgumentException("NFTs node is not an array: " + nftsNode);
        }
        Spliterator<JsonNode> spliterator = Spliterators.spliteratorUnknownSize(nftsNode.iterator(),
                Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
                .map(n -> toNft(n))
                .filter(optional -> optional.isPresent())
                .map(optional -> optional.get())
                .toList();
    }

    @Override
    public Optional<TokenInfo> toTokenInfo(JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }

        try {
            final TokenId tokenId = TokenId.fromString(node.get("token_id").asText());
            final TokenType type = TokenType.valueOf(node.get("type").asText());
            final String name = node.get("name").asText();
            final String symbol = node.get("symbol").asText();
            final String memo = node.get("memo").asText();
            final long decimals = node.get("decimals").asLong();
            final byte[] metadata = node.get("metadata").asText().getBytes();
            final Instant createdTimeStamp = Instant.ofEpochSecond(node.get("created_timestamp").asLong());
            final Instant modifiedTimestamp = Instant.ofEpochSecond(node.get("modified_timestamp").asLong());
            final TokenSupplyType supplyType = TokenSupplyType.valueOf(node.get("supply_type").asText());
            final String totalSupply = node.get("total_supply").asText();
            final String initialSupply = node.get("initial_supply").asText();
            final AccountId treasuryAccountId = AccountId.fromString(node.get("treasury_account_id").asText());
            final boolean deleted = node.get("deleted").asBoolean();
            final String maxSupply = node.get("max_supply").asText();

            final Instant expiryTimestamp;
            if (!node.get("expiry_timestamp").isNull()) {
                BigInteger nanoseconds = new BigInteger(node.get("expiry_timestamp").asText());
                BigInteger expirySeconds = nanoseconds.divide(BigInteger.valueOf(1_000_000_000));
                expiryTimestamp = Instant.ofEpochSecond(expirySeconds.longValue());
            } else {
                expiryTimestamp = null;
            }

            final CustomFee customFees = getCustomFee(node.get("custom_fees"));

            return Optional.of(new TokenInfo(
                    tokenId,
                    type,
                    name,
                    symbol,
                    memo,
                    decimals,
                    metadata,
                    createdTimeStamp,
                    modifiedTimestamp,
                    expiryTimestamp,
                    supplyType,
                    initialSupply,
                    totalSupply,
                    maxSupply,
                    treasuryAccountId,
                    deleted,
                    customFees
            ));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    private CustomFee getCustomFee(JsonNode node) {
        List<FractionalFee> fractionalFees = List.of();
        List<FixedFee> fixedFees = List.of();
        List<RoyaltyFee> royaltyFees = List.of();

        if (node.has("fixed_fees")) {
            JsonNode fixedFeeNode = node.get("fixed_fees");
            if (!fixedFeeNode.isArray()) {
                throw new IllegalArgumentException("FixedFees node is not an array: " + fixedFeeNode);
            }
            fixedFees = StreamSupport.stream(Spliterators.spliteratorUnknownSize(fixedFeeNode.iterator(),
                            Spliterator.ORDERED), false)
                    .map(n ->{
                        final long amount = n.get("amount").asLong();
                        final AccountId accountId = n.get("collector_account_id").isNull()?
                                        null : AccountId.fromString(n.get("collector_account_id").asText());
                        final TokenId tokenId = n.get("denominating_token_id").isNull()?
                                        null : TokenId.fromString(n.get("denominating_token_id").asText());
                        return new FixedFee(amount, accountId, tokenId);
                    })
                    .toList();
        }

        if (node.has("fractional_fees")) {
            JsonNode fractionalFeeNode = node.get("fractional_fees");
            if (!fractionalFeeNode.isArray()) {
                throw new IllegalArgumentException("FractionalFee node is not an array: " + fractionalFeeNode);
            }
            fractionalFees = StreamSupport.stream(Spliterators.spliteratorUnknownSize(fractionalFeeNode.iterator(),
                            Spliterator.ORDERED), false)
                    .map(n ->{
                        final long numeratorAmount = n.get("amount").get("numerator").asLong();
                        final long denominatorAmount = n.get("amount").get("denominator").asLong();
                        final AccountId accountId = n.get("collector_account_id").isNull()?
                                null : AccountId.fromString(n.get("collector_account_id").asText());
                        final TokenId tokenId = n.get("denominating_token_id").isNull()?
                                null : TokenId.fromString(n.get("denominating_token_id").asText());
                        return new FractionalFee(numeratorAmount, denominatorAmount, accountId, tokenId);
                    })
                    .toList();
        }

        if (node.has("royalty_fees")) {
            JsonNode royaltyFeeNode = node.get("royalty_fees");
            if (!royaltyFeeNode.isArray()) {
                throw new IllegalArgumentException("RoyaltyFee node is not an array: " + royaltyFeeNode);
            }
            royaltyFees = StreamSupport.stream(Spliterators.spliteratorUnknownSize(royaltyFeeNode.iterator(),
                            Spliterator.ORDERED), false)
                    .map(n ->{
                        final long numeratorAmount = n.get("amount").get("numerator").asLong();
                        final long denominatorAmount = n.get("amount").get("denominator").asLong();
                        final long fallbackFeeAmount = n.get("fallback_fee").get("amount").asLong();
                        final AccountId accountId = n.get("collector_account_id").isNull()?
                                null : AccountId.fromString(n.get("collector_account_id").asText());
                        final TokenId tokenId = n.get("fallback_fee").get("denominating_token_id").isNull()?
                                null : TokenId.fromString(n.get("fallback_fee").get("denominating_token_id").asText());
                        return new RoyaltyFee(numeratorAmount, denominatorAmount, fallbackFeeAmount, accountId, tokenId);
                    })
                    .toList();
        }

        return new CustomFee(fixedFees, fractionalFees, royaltyFees);
    }

    @Override
    public List<Balance> toBalances(JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (!node.has("balances")) {
            return List.of();
        }
        final JsonNode balancesNode = node.get("balances");
        if (!balancesNode.isArray()) {
            throw new IllegalArgumentException("TokenBalances node is not an array: " + balancesNode);
        }
        Spliterator<JsonNode> spliterator = Spliterators.spliteratorUnknownSize(balancesNode.iterator(),
                Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
                .map(n -> toBalance(n))
                .filter(optional -> optional.isPresent())
                .map(optional -> optional.get())
                .toList();
    }

    @Override
    public List<Token> toTokens(JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (!node.has("tokens")) {
            return List.of();
        }
        final JsonNode tokens = node.get("tokens");
        if (!tokens.isArray()) {
            throw new IllegalArgumentException("Tokens node is not an array: " + tokens);
        }
        Spliterator<JsonNode> spliterator = Spliterators.spliteratorUnknownSize(tokens.iterator(),
                Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
                .map(n -> toToken(n))
                .filter(optional -> optional.isPresent())
                .map(optional -> optional.get())
                .toList();
    }

    private Optional<Token> toToken(JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }

        try {
            final byte[] metadata = node.get("metadata").asText().getBytes();
            final String name = node.get("name").asText();
            final String symbol = node.get("symbol").asText();
            final long decimals = node.get("decimals").asLong();
            final TokenType type = TokenType.valueOf(node.get("type").asText());
            final TokenId tokenId = node.get("token_id").isNull()? null : TokenId.fromString(node.get("token_id").asText());

            return Optional.of(new Token(decimals, metadata, name, symbol, tokenId, type));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    private Optional<Balance> toBalance(JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (node.isNull() || node.isEmpty()) {
            return Optional.empty();
        }

        try {
            final AccountId account = AccountId.fromString(node.get("account").asText());
            final long balance = node.get("balance").asLong();
            final long decimals = node.get("decimals").asLong();

            return Optional.of(new Balance(account, balance, decimals));
        } catch (final Exception e) {
            throw new JsonParseException(node, e);
        }
    }

    @NonNull
    private Stream<JsonNode> jsonArrayToStream(@NonNull final JsonNode node) {
        Objects.requireNonNull(node, "jsonNode must not be null");
        if (!node.isArray()) {
            throw new JsonParseException("not an array", node);
        }
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(node.iterator(), Spliterator.ORDERED), false);
    }
}
