package com.openelements.hiero.microprofile.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenSupplyType;
import com.hedera.hashgraph.sdk.TokenType;
import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.TransactionInfo;
import com.openelements.hiero.base.data.Token;
import com.openelements.hiero.base.data.TokenInfo;
import com.openelements.hiero.base.data.Balance;
import com.openelements.hiero.base.data.CustomFee;
import com.openelements.hiero.base.data.FixedFee;
import com.openelements.hiero.base.data.FractionalFee;
import com.openelements.hiero.base.data.RoyaltyFee;
import com.openelements.hiero.base.implementation.MirrorNodeJsonConverter;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonValue;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import org.jspecify.annotations.NonNull;

public class MirrorNodeJsonConverterImpl implements MirrorNodeJsonConverter<JsonObject> {

    @Override
    public @NonNull Optional<Nft> toNft(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Optional<NetworkSupplies> toNetworkSupplies(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Optional<NetworkStake> toNetworkStake(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull Optional<ExchangeRates> toExchangeRates(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
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
        throw new RuntimeException("Not implemented");
    }

    @Override
    public @NonNull List<TransactionInfo> toTransactionInfos(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public List<Nft> toNfts(@NonNull JsonObject jsonObject) {
        throw new RuntimeException("Not implemented");
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
        List<FractionalFee> fractionalFees = null;
        List<FixedFee> fixedFees = null;
        List<RoyaltyFee> royaltyFees = null;

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
