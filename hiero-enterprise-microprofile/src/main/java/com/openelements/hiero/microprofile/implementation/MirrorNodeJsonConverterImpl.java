package com.openelements.hiero.microprofile.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.data.ExchangeRate;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.TransactionInfo;
import com.openelements.hiero.base.implementation.MirrorNodeJsonConverter;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jakarta.json.JsonValue;
import org.jspecify.annotations.NonNull;

public class MirrorNodeJsonConverterImpl implements MirrorNodeJsonConverter<JsonObject> {

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
                        final String transactionId = n.asJsonObject().getString("transaction_id");
                        return new TransactionInfo(transactionId);
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
}

