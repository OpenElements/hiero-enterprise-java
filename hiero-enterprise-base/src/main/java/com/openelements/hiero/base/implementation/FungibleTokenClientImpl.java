package com.openelements.hiero.base.implementation;

import com.openelements.hiero.base.FungibleTokenClient;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenType;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.TokenAssociateRequest;
import com.openelements.hiero.base.protocol.data.TokenDissociateRequest;
import com.openelements.hiero.base.protocol.data.TokenBurnRequest;
import com.openelements.hiero.base.protocol.data.TokenBurnResult;
import com.openelements.hiero.base.protocol.data.TokenCreateRequest;
import com.openelements.hiero.base.protocol.data.TokenCreateResult;
import com.openelements.hiero.base.protocol.data.TokenMintRequest;
import com.openelements.hiero.base.protocol.data.TokenMintResult;
import com.openelements.hiero.base.protocol.data.TokenTransferRequest;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Objects;

public class FungibleTokenClientImpl implements FungibleTokenClient {
    private final ProtocolLayerClient client;

    private final Account operationalAccount;

    public FungibleTokenClientImpl(@NonNull final ProtocolLayerClient client,
            @NonNull final Account operationalAccount) {
        this.client = Objects.requireNonNull(client, "client must not be null");
        this.operationalAccount = Objects.requireNonNull(operationalAccount, "operationalAccount must not be null");
    }

    @Override
    public TokenId createToken(@NonNull String name, @NonNull String symbol)
            throws HieroException {
        return createToken(name, symbol, operationalAccount);
    }

    @Override
    public TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull PrivateKey supplyKey)
            throws HieroException {
        return createToken(name, symbol, operationalAccount, supplyKey);
    }

    @Override
    public TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull AccountId treasuryAccountId,
            @NonNull PrivateKey treasuryKey) throws HieroException {
        return createToken(name, symbol, treasuryAccountId, treasuryKey, operationalAccount.privateKey());
    }

    @Override
    public TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull AccountId treasuryAccountId,
            @NonNull PrivateKey treasuryKey, @NonNull PrivateKey supplyKey) throws HieroException {
        final TokenCreateRequest request = TokenCreateRequest.of(name, symbol, treasuryAccountId, treasuryKey,
                TokenType.FUNGIBLE_COMMON, supplyKey);
        final TokenCreateResult result = client.executeTokenCreateTransaction(request);
        return result.tokenId();
    }

    @Override
    public void associateToken(@NonNull TokenId tokenId, @NonNull AccountId accountId, @NonNull PrivateKey accountKey)
            throws HieroException {
        final TokenAssociateRequest request = TokenAssociateRequest.of(tokenId, accountId, accountKey);
        client.executeTokenAssociateTransaction(request);
    }

    @Override
    public void associateToken(@NonNull List<TokenId> tokenIds, @NonNull AccountId accountId, @NonNull PrivateKey accountKey) throws HieroException {
        Objects.requireNonNull(tokenIds, "tokenIds must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(accountKey, "accountKey must not be null");
        if (tokenIds.isEmpty()) {
            throw new IllegalArgumentException("tokenIds must not be empty");
        }
        final TokenAssociateRequest request = TokenAssociateRequest.of(tokenIds, accountId, accountKey);
        client.executeTokenAssociateTransaction(request);
    }

    @Override
    public void dissociateToken(@NonNull TokenId tokenId, @NonNull AccountId accountId, @NonNull PrivateKey accountKey) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(accountKey, "accountKey must not be null");
        final TokenDissociateRequest request = TokenDissociateRequest.of(tokenId, accountId, accountKey);
        client.executeTokenDissociateTransaction(request);
    }

    @Override
    public void dissociateToken(@NonNull List<TokenId> tokenIds, @NonNull AccountId accountId, @NonNull PrivateKey accountKey) throws HieroException {
        Objects.requireNonNull(tokenIds, "tokenIds must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(accountKey, "accountKey must not be null");
        if (tokenIds.isEmpty()) {
            throw new IllegalArgumentException("tokenIds must not be empty");
        }
        final TokenDissociateRequest request = TokenDissociateRequest.of(tokenIds, accountId, accountKey);
        client.executeTokenDissociateTransaction(request);
    }

    @Override
    public long mintToken(@NonNull TokenId tokenId, long amount) throws HieroException {
        return mintToken(tokenId, operationalAccount.privateKey(), amount);
    }

    @Override
    public long mintToken(@NonNull TokenId tokenId, @NonNull PrivateKey supplyKey, long amount)
            throws HieroException {
        final TokenMintRequest request = TokenMintRequest.of(tokenId, supplyKey, amount);
        final TokenMintResult result = client.executeMintTokenTransaction(request);
        return result.totalSupply();
    }

    @Override
    public long burnToken(@NonNull TokenId tokenId, long amount) throws HieroException {
        return burnToken(tokenId, amount, operationalAccount.privateKey());
    }

    @Override
    public long burnToken(@NonNull TokenId tokenId, long amount, @NonNull PrivateKey supplyKey) throws HieroException {
        final TokenBurnRequest request = TokenBurnRequest.of(tokenId, supplyKey, amount);
        final TokenBurnResult result = client.executeBurnTokenTransaction(request);
        return result.totalSupply();
    }

    @Override
    public void transferToken(@NonNull TokenId tokenId, @NonNull AccountId toAccountId, long amount)
            throws HieroException {
        transferToken(tokenId, operationalAccount, toAccountId, amount);
    }

    @Override
    public void transferToken(@NonNull TokenId tokenId, @NonNull AccountId fromAccountId,
            @NonNull PrivateKey fromAccountKey, @NonNull AccountId toAccountId, long amount) throws HieroException {
        final TokenTransferRequest request = TokenTransferRequest.of(tokenId, fromAccountId, toAccountId,
                fromAccountKey, amount);
        client.executeTransferTransaction(request);
    }
}