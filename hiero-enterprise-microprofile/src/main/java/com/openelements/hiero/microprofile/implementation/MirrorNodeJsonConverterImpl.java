package com.openelements.hiero.microprofile.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenSupplyType;
import com.hedera.hashgraph.sdk.TokenType;
import com.openelements.hiero.base.data.*;
import com.openelements.hiero.base.implementation.MirrorNodeJsonConverter;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.time.Instant;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jakarta.json.JsonValue;
import org.jspecify.annotations.NonNull;

public class MirrorNodeJsonConverterImpl implements MirrorNodeJsonConverter<JsonObject> {

    private boolean scheduled;

    @Override
    public @NonNull Optional<Nft> toNft(@NonNull JsonObject jsonObject) {
        try {
            final TokenId parsedTokenId = TokenId.fromString(jsonObject.getString("token_id"));
            final AccountId account = AccountId.fromString(jsonObject.getString("account_id"));
            final long serial = jsonObject.getJsonNumber("serial_number").longValue();
            final byte[] metadata = jsonObject.getString("metadata").getBytes();
            return Optional.of(new Nft(parsedTokenId, serial, account, metadata));
        } catch (final Exception e) {
            throw new IllegalStateException("Can not parse JSON: " + jsonObject, e);
        }
    }

    @Override
    public @NonNull Optional<NetworkSupplies> toNetworkSupplies(@NonNull JsonObject jsonObject) {
        try {
            final String releasedSupply = jsonObject.getString("released_supply");
            final String totalSupply = jsonObject.getString("total_supply");
            return Optional.of(new NetworkSupplies(releasedSupply, totalSupply));
        } catch (final Exception e) {
            throw new IllegalStateException("Can not parse JSON: " + jsonObject, e);
        }
    }

    @Override
    public @NonNull Optional<NetworkStake> toNetworkStake(@NonNull JsonObject jsonObject) {
        try {
            final long maxStakeReward = jsonObject.getJsonNumber("max_stake_rewarded").longValue();
            final long maxStakeRewardPerHbar = jsonObject.getJsonNumber("max_staking_reward_rate_per_hbar").longValue();
            final long maxTotalReward = jsonObject.getJsonNumber("max_total_reward").longValue();
            final double nodeRewardFeeFraction = jsonObject.getJsonNumber("node_reward_fee_fraction").doubleValue();
            final long reservedStakingRewards = jsonObject.getJsonNumber("reserved_staking_rewards").longValue();
            final long rewardBalanceThreshold = jsonObject.getJsonNumber("reward_balance_threshold").longValue();
            final long stakeTotal = jsonObject.getJsonNumber("stake_total").longValue();
            final long stakingPeriodDuration = jsonObject.getJsonNumber("staking_period_duration").longValue();
            final long stakingPeriodsStored = jsonObject.getJsonNumber("staking_periods_stored").longValue();
            final double stakingRewardFeeFraction = jsonObject.getJsonNumber("staking_reward_fee_fraction").doubleValue();
            final long stakingRewardRate = jsonObject.getJsonNumber("staking_reward_rate").longValue();
            final long stakingStartThreshold = jsonObject.getJsonNumber("staking_start_threshold").longValue();
            final long unreservedStakingRewardBalance = jsonObject.getJsonNumber("unreserved_staking_reward_balance").longValue();

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
            throw new IllegalStateException("Can not parse JSON: " + jsonObject, e);
        }
    }

    @Override
    public @NonNull Optional<ExchangeRates> toExchangeRates(@NonNull JsonObject jsonObject) {
        try {
            final int currentCentEquivalent = jsonObject.getJsonObject("current_rate").getJsonNumber("cent_equivalent").intValue();
            final int currentHbarEquivalent = jsonObject.getJsonObject("current_rate").getJsonNumber("hbar_equivalent").intValue();
            final Instant currentExpirationTime = Instant.ofEpochSecond(
                    jsonObject.getJsonObject("current_rate").getJsonNumber("expiration_time").longValue()
            );

            final int nextCentEquivalent = jsonObject.getJsonObject("next_rate").getJsonNumber("cent_equivalent").intValue();
            final int nextHbarEquivalent = jsonObject.getJsonObject("next_rate").getJsonNumber("hbar_equivalent").intValue();
            final Instant nextExpirationTime = Instant.ofEpochSecond(
                    jsonObject.getJsonObject("next_rate").getJsonNumber("expiration_time").longValue()
            );

            return Optional.of(new ExchangeRates(
                    new ExchangeRate(currentCentEquivalent, currentHbarEquivalent, currentExpirationTime),
                    new ExchangeRate(nextCentEquivalent, nextHbarEquivalent, nextExpirationTime)
            ));
        } catch (final Exception e) {
            throw new IllegalStateException("Can not parse JSON: " + jsonObject, e);
        }
    }

    @Override
    public @NonNull Optional<AccountInfo> toAccountInfo(@NonNull JsonObject node) {
        try {
            final AccountId accountId = AccountId.fromString(node.getString("account"));
            final String evmAddress = node.getString("evm_address");
            final long ethereumNonce = node.getJsonNumber("ethereum_nonce").longValue();
            final long pendingReward = node.getJsonNumber("pending_reward").longValue();
            final long balance = node.getJsonObject("balance").getJsonNumber("balance").longValue();
            return Optional.of(new AccountInfo(accountId, evmAddress, balance, ethereumNonce, pendingReward));
        } catch (final Exception e) {
            throw new IllegalStateException("Can not parse JSON: " + node, e);
        }
    }

    @Override
    public @NonNull List<NetworkFee> toNetworkFees(@NonNull JsonObject jsonObject) {

        if (!jsonObject.containsKey("nfts")) {
            return List.of();
        }

        final JsonArray feesNode = jsonObject.getJsonArray("fees");
        return jsonArrayToStream(feesNode)
                .map(n -> {
                    try {
                        final long gas = n.asJsonObject().getJsonNumber("gas").longValue();
                        final String transactionType = n.asJsonObject().getString("transaction_type");
                        return new NetworkFee(gas, transactionType);
                    } catch (final Exception e) {
                        throw new IllegalStateException("Can not parse JSON: " + n, e);
                    }
                })
                .toList();
    }

    @Override
    public @NonNull List<TransactionInfo> toTransactionInfos(@NonNull JsonObject jsonObject) {
        if (!jsonObject.containsKey("transactions")) {
            return List.of();
        }


        final JsonArray transactionsNode = jsonObject.getJsonArray("transactions");




        return jsonArrayToStream(transactionsNode)
                .map(n -> {
                    try {
                        JsonObject transactionsObject= n.asJsonObject();

                        // Converting NftTransfers Object.
                        JsonArray nftTransfersNode= n.asJsonObject().getJsonArray("nft_transfers");
                        List<NftTransfers> nftTransfers= new ArrayList<>();

                         if(nftTransfersNode !=null){
                            for(JsonValue value: nftTransfersNode){
                                JsonObject nftTransfersObject = value.asJsonObject();


                                final boolean isApproval= nftTransfersObject.getBoolean("is_approval");
                                final String receiverAccountId= nftTransfersObject.getString("receiver_account_id");
                                final String senderAccountId= nftTransfersObject.getString("sender_account_id", null);
                                final int serialNumber= nftTransfersObject.getInt("serial_number");
                                final String tokenId= nftTransfersObject.getString("token_id");

                                nftTransfers.add(new NftTransfers(isApproval, receiverAccountId, senderAccountId,serialNumber, tokenId));
                            }
                         }

                        // Converting StakingRewardTransfers Object.
                         JsonArray stakingRewardTransfersNode = n.asJsonObject().getJsonArray("staking_reward_transfers");
                          List<StakingRewardTransfers> stakingRewardTransfers= new ArrayList<>();

                          if(stakingRewardTransfersNode !=null && !stakingRewardTransfersNode.isEmpty()){
                         for(JsonValue value: stakingRewardTransfersNode ){
                                stakingRewardTransfers.add(new StakingRewardTransfers());
                            }
                        }

                        // Converting TokenTransfers Object.
                         JsonArray tokenTransfersNode = n.asJsonObject().getJsonArray("token_transfers");
                         List<TokenTransfers> tokenTransfers= new ArrayList<>();

                         if(tokenTransfersNode !=null && !tokenTransfersNode.isEmpty()){
                         for(JsonValue value: tokenTransfersNode ){
                             tokenTransfers.add(new TokenTransfers());
                            }
                         }

                        // Converting Transfers Object.
                         JsonArray transfersNode= n.asJsonObject().getJsonArray("transfers");
                         List<Transfers> transfers= new ArrayList<>();

                         if(transfersNode != null){
                             for(JsonValue value: transfersNode){
                                 JsonObject transfersNodeObject = value.asJsonObject();

                                 final String account= transfersNodeObject.getString("account");
                                 final long amount= transfersNodeObject.getJsonNumber("amount").longValue();
                                 final boolean isApproval= transfersNodeObject.containsKey("is_approval") ? transfersNodeObject.getBoolean("is_approval"): false;

                                 transfers.add(new Transfers(account, amount, isApproval));
                             }
                         }

                        final Byte bytes = transactionsObject.containsKey("bytes") ? (byte) transactionsObject.getInt("bytes") : null;
                        final long chargedTxFee= transactionsObject.getJsonNumber("charged_tx_fee").longValue();
                        final String consensusTimeStamp = transactionsObject.getString("consensus_timestamp");
                        final String entityId = transactionsObject.getString("entity_id");
                        final String maxFee= transactionsObject.getString("max_fee");
                        final String memoBase64= transactionsObject.getString("memo_base64");
                        final String name= transactionsObject.getString("name");
                        final String node= transactionsObject.getString("node");
                        final int nonce= transactionsObject.getInt("nonce");
                        final String parentConsensusTimestamp= transactionsObject.getString("parent_consensus_timestamp", null);
                        final String result= transactionsObject.getString("result");
                        final boolean scheduled= transactionsObject.getBoolean("scheduled");
                        final String transactionHash= transactionsObject.getString("transaction_hash");
                        final String transactionId = transactionsObject.getString("transaction_id");
                        final String validDurationSeconds= transactionsObject.getString("valid_duration_seconds");
                        final String validStartTimestamp= transactionsObject.getString("valid_start_timestamp");

                        return new TransactionInfo(
                                bytes,
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
                                validStartTimestamp );
                    } catch (final Exception e) {
                        throw new IllegalStateException("Can not parse JSON: " + n, e);
                    }
                })
                .toList();
    }

    @Override
    public List<Nft> toNfts(@NonNull JsonObject jsonObject) {
        if (!jsonObject.containsKey("transactions")) {
            return List.of();
        }

        final JsonArray nftsArray = jsonObject.getJsonArray("nfts");
        if (nftsArray.isEmpty()) {
            throw new IllegalArgumentException("NFTs jsonObject is not an array: " + nftsArray);
        }
        Spliterator<JsonValue> spliterator = Spliterators.spliteratorUnknownSize(nftsArray.iterator(),
                Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
                .map(n -> toNft(n.asJsonObject()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @NonNull
    private Stream<JsonValue> jsonArrayToStream(@NonNull final JsonArray jsonObject) {
        if (jsonObject.isEmpty()) {
            throw new IllegalStateException("not an array");
        }
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(jsonObject.iterator(), Spliterator.ORDERED), false);
    }

    @Override
    public List<Token> toTokens(JsonObject jsonObject) {
        Objects.requireNonNull(jsonObject, "jsonObject must not be null");
        if (!jsonObject.containsKey("tokens")) {
            return List.of();
        }
        final JsonArray tokens = jsonObject.getJsonArray("tokens");
        if (tokens == null) {
            throw new IllegalArgumentException("Tokens node is not an array");
        }
        Spliterator<JsonValue> spliterator = Spliterators.spliteratorUnknownSize(tokens.iterator(),
                Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
                .map(n -> toToken(n.asJsonObject()))
                .filter(optional -> optional.isPresent())
                .map(optional -> optional.get())
                .toList();
    }

    private Optional<Token> toToken(JsonObject jsonObject) {
        Objects.requireNonNull(jsonObject, "jsonObject must not be null");
        if (jsonObject.isEmpty()) {
            return Optional.empty();
        }

        try {
            final byte[] metadata = jsonObject.getString("metadata").getBytes();
            final String name = jsonObject.getString("name");
            final String symbol = jsonObject.getString("symbol");
            final long decimals = jsonObject.getJsonNumber("decimals").longValue();
            final TokenType type = TokenType.valueOf(jsonObject.getString("type"));
            final TokenId tokenId = jsonObject.isNull("token_id")? null : TokenId.fromString(jsonObject.getString("token_id"));

            return Optional.of(new Token(decimals, metadata, name, symbol, tokenId, type));
        } catch (final Exception e) {
            throw new IllegalStateException("Can not parse JSON: " + jsonObject, e);
        }
    }

    @Override
    public Optional<TokenInfo> toTokenInfo(JsonObject jsonObject) {
        Objects.requireNonNull(jsonObject, "jsonObject must not be null");
        if (jsonObject.isEmpty()) {
            return Optional.empty();
        }

        try {
            final TokenId tokenId = TokenId.fromString(jsonObject.getString("token_id"));
            final TokenType type = TokenType.valueOf(jsonObject.getString("type"));
            final String name = jsonObject.getString("name");
            final String symbol = jsonObject.getString("symbol");
            final String memo = jsonObject.getString("memo");
            final long decimals = Long.parseLong(jsonObject.getString("decimals"));
            final byte[] metadata = jsonObject.getString("metadata").getBytes();
            final Instant createdTimeStamp = Instant.ofEpochSecond((long) Double.parseDouble(jsonObject.getString("created_timestamp")));
            final Instant modifiedTimestamp = Instant.ofEpochSecond((long) Double.parseDouble(jsonObject.getString("modified_timestamp")));
            final TokenSupplyType supplyType = TokenSupplyType.valueOf(jsonObject.getString("supply_type"));
            final String totalSupply = jsonObject.getString("total_supply");
            final String initialSupply = jsonObject.getString("initial_supply");
            final AccountId treasuryAccountId = AccountId.fromString(jsonObject.getString("treasury_account_id"));
            final boolean deleted = jsonObject.getBoolean("deleted");
            final String maxSupply = jsonObject.getString("max_supply");

            final Instant expiryTimestamp;
            if (!jsonObject.isNull("expiry_timestamp")) {
                BigInteger nanoseconds = new BigInteger(String.valueOf(jsonObject.getJsonNumber("expiry_timestamp")));
                BigInteger expirySeconds = nanoseconds.divide(BigInteger.valueOf(1_000_000_000));
                expiryTimestamp = Instant.ofEpochSecond(expirySeconds.longValue());
            } else {
                expiryTimestamp = null;
            }

            final CustomFee customFees = getCustomFee(jsonObject.get("custom_fees").asJsonObject());

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
            throw new IllegalStateException("Can not parse JSON: " + jsonObject, e);
        }
    }

    private CustomFee getCustomFee(JsonObject object) {
        List<FractionalFee> fractionalFees = List.of();
        List<FixedFee> fixedFees = List.of();
        List<RoyaltyFee> royaltyFees = List.of();

        if (object.containsKey("fixed_fees")) {
            JsonArray fixedFeeArray = object.get("fixed_fees").asJsonArray();
            if (fixedFeeArray == null) {
                throw new IllegalArgumentException("FixedFeesArray is not an array: " + fixedFeeArray);
            }
            fixedFees = StreamSupport.stream(Spliterators.spliteratorUnknownSize(fixedFeeArray.iterator(),
                            Spliterator.ORDERED), false)
                    .map(n -> {
                        JsonObject obj = n.asJsonObject();
                        final long amount = obj.getJsonNumber("amount").longValue();
                        final AccountId accountId = obj.get("collector_account_id").asJsonObject() == null?
                                null : AccountId.fromString(obj.getString("collector_account_id"));
                        final TokenId tokenId = obj.get("denominating_token_id").asJsonObject() == null?
                                null : TokenId.fromString(obj.getString("denominating_token_id"));
                        return new FixedFee(amount, accountId, tokenId);
                    })
                    .toList();
        }

        if (object.containsKey("fractional_fees")) {
            JsonArray fractionalFeeArray = object.get("fractional_fees").asJsonArray();
            if (fractionalFeeArray == null) {
                throw new IllegalArgumentException("FractionalFeeArray is not an array: " + fractionalFeeArray);
            }
            fractionalFees = StreamSupport.stream(Spliterators.spliteratorUnknownSize(fractionalFeeArray.iterator(),
                            Spliterator.ORDERED), false)
                    .map(n ->{
                        JsonObject obj = n.asJsonObject();
                        final long numeratorAmount = obj.get("amount").asJsonObject().getJsonNumber("numerator").longValue();
                        final long denominatorAmount = obj.get("amount").asJsonObject().getJsonNumber("denominator").longValue();
                        final AccountId accountId = obj.get("collector_account_id").asJsonObject() == null?
                                null : AccountId.fromString(obj.getString("collector_account_id"));
                        final TokenId tokenId = obj.get("denominating_token_id").asJsonObject() == null?
                                null : TokenId.fromString(obj.getString("denominating_token_id"));
                        return new FractionalFee(numeratorAmount, denominatorAmount, accountId, tokenId);
                    })
                    .toList();
        }

        if (object.containsKey("royalty_fees")) {
            JsonArray royaltyFeeArray = object.get("royalty_fees").asJsonArray();
            if (royaltyFeeArray == null) {
                throw new IllegalArgumentException("RoyaltyFeeArray is not an array: " + royaltyFeeArray);
            }
            royaltyFees = StreamSupport.stream(Spliterators.spliteratorUnknownSize(royaltyFeeArray.iterator(),
                            Spliterator.ORDERED), false)
                    .map(n ->{
                        JsonObject obj = n.asJsonObject();
                        final long numeratorAmount = obj.get("amount").asJsonObject().getJsonNumber("numerator").longValue();
                        final long denominatorAmount = obj.get("amount").asJsonObject().getJsonNumber("denominator").longValue();
                        final long fallbackFeeAmount = obj.get("fallback_fee").asJsonObject().getJsonNumber("amount").longValue();
                        final AccountId accountId = obj.get("collector_account_id").asJsonObject() == null?
                                null : AccountId.fromString(obj.getString("collector_account_id"));
                        final TokenId tokenId = obj.get("fallback_fee").asJsonObject().get("denominating_token_id").asJsonObject() == null?
                                null : TokenId.fromString(obj.get("fallback_fee").asJsonObject().getString("denominating_token_id"));
                        return new RoyaltyFee(numeratorAmount, denominatorAmount, fallbackFeeAmount, accountId, tokenId);
                    })
                    .toList();
        }

        return new CustomFee(fixedFees, fractionalFees, royaltyFees);
    }

    @Override
    public List<Balance> toBalances(JsonObject jsonObject) {
        Objects.requireNonNull(jsonObject, "jsonObject must not be null");
        if (!jsonObject.containsKey("balances")) {
            return List.of();
        }
        final JsonArray balancesArray = jsonObject.getJsonArray("balances");
        if (balancesArray == null) {
            throw new IllegalArgumentException("TokenBalances array is not an array: " + balancesArray);
        }

        Spliterator<JsonValue> spliterator = Spliterators.spliteratorUnknownSize(balancesArray.iterator(),
                Spliterator.ORDERED);
        return StreamSupport.stream(spliterator, false)
                .map(n -> toBalance(n.asJsonObject()))
                .filter(optional -> optional.isPresent())
                .map(optional -> optional.get())
                .toList();
    }

    private Optional<Balance> toBalance(JsonObject jsonObject) {
        Objects.requireNonNull(jsonObject, "jsonObject must not be null");
        if (jsonObject.isEmpty()) {
            return Optional.empty();
        }

        try {
            final AccountId account = AccountId.fromString(jsonObject.getString("account"));
            final long balance = jsonObject.getJsonNumber("balance").longValue();
            final long decimals = jsonObject.getJsonNumber("decimals").longValue();

            return Optional.of(new Balance(account, balance, decimals));
        } catch (final Exception e) {
            throw new IllegalStateException("Can not parse JSON: " + jsonObject, e);
        }
    }
}

