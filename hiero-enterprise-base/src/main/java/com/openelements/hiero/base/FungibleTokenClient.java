package com.openelements.hiero.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.data.Account;

import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with a Hiero network. This interface provides methods for interacting with Hedera Fungible
 * Tokens, like creating and deleting Token. An implementation of this interface is using an internal account to
 * interact with a Hiero network. That account is the account that is used to pay for the transactions that are sent to
 * the Hedera network and called 'operator account'.
 */
public interface FungibleTokenClient {

    /**
     * Create a new token. The operator account is used as suppler account and as treasury account for the token.
     *
     * @param name   the name of the token
     * @param symbol the symbol of the token
     * @return the ID of the new token
     * @throws HieroException if the token could not be created
     */
    @NonNull
    TokenId createToken(@NonNull String name, @NonNull String symbol) throws HieroException;

    /**
     * Create a new token. The operator account is used treasury account for the token.
     *
     * @param name      the name of the token
     * @param symbol    the symbol of the token
     * @param supplyKey the private key of the supplier account
     * @return the ID of the new token
     * @throws HieroException if the token could not be created
     */
    @NonNull
    TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull PrivateKey supplyKey)
            throws HieroException;


    /**
     * Create a new token. The operator account is used as treasury account for the token.
     *
     * @param name      the name of the token
     * @param symbol    the symbol of the token
     * @param supplyKey the private key of the supplier account
     * @return the ID of the new token
     * @throws HieroException if the token could not be created
     */
    @NonNull
    default TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull String supplyKey)
            throws HieroException {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(symbol, "symbol must not be null");
        Objects.requireNonNull(supplyKey, "supplyKey must not be null");
        return createToken(name, symbol, PrivateKey.fromString(supplyKey));
    }


    /**
     * Create a new token.
     *
     * @param name              the name of the token
     * @param symbol            the symbol of the token
     * @param treasuryAccountId the ID of the treasury account
     * @param treasuryKey       the private key of the treasury account
     * @return the ID of the new token
     * @throws HieroException if the token could not be created
     */
    @NonNull
    TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull AccountId treasuryAccountId,
            @NonNull PrivateKey treasuryKey) throws HieroException;


    /**
     * Create a  new token.
     *
     * @param name              the name of the token
     * @param symbol            the symbol of the token
     * @param treasuryAccountId the ID of the treasury account
     * @param treasuryKey       the private key of the treasury account
     * @return the ID of the new token
     * @throws HieroException if the token could not be created
     */
    @NonNull
    default TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull String treasuryAccountId,
            @NonNull String treasuryKey) throws HieroException {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(symbol, "symbol must not be null");
        Objects.requireNonNull(treasuryAccountId, "treasuryAccountId must not be null");
        Objects.requireNonNull(treasuryKey, "treasuryKey must not be null");
        return createToken(name, symbol, AccountId.fromString(treasuryAccountId),
                PrivateKey.fromString(treasuryKey));
    }

    /**
     * Create a new token.
     *
     * @param name            the name of the token
     * @param symbol          the symbol of the token
     * @param treasuryAccount the treasury account
     * @return the ID of the new token
     * @throws HieroException if the token could not be created
     */
    @NonNull
    default TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull Account treasuryAccount)
            throws HieroException {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(symbol, "symbol must not be null");
        Objects.requireNonNull(treasuryAccount, "treasuryAccount must not be null");
        return createToken(name, symbol, treasuryAccount.accountId(), treasuryAccount.privateKey());
    }

    /**
     * Create a new token.
     *
     * @param name              the name of the token
     * @param symbol            the symbol of the token
     * @param treasuryAccountId the ID of the treasury account
     * @param treasuryKey       the private key of the treasury account
     * @param supplyKey         the private key of the supplier account
     * @return the ID of the new token
     * @throws HieroException if the token could not be created
     */
    @NonNull
    TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull AccountId treasuryAccountId,
            @NonNull PrivateKey treasuryKey, @NonNull PrivateKey supplyKey) throws HieroException;

    /**
     * Create a new token.
     *
     * @param name              the name of the token
     * @param symbol            the symbol of the token
     * @param treasuryAccountId the ID of the treasury account
     * @param treasuryKey       the private key of the treasury account
     * @param supplyKey         the private key of the supplier account
     * @return the ID of the new token
     * @throws HieroException if the token could not be created
     */
    @NonNull
    default TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull String treasuryAccountId,
            @NonNull String treasuryKey, @NonNull String supplyKey) throws HieroException {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(symbol, "symbol must not be null");
        Objects.requireNonNull(treasuryAccountId, "treasuryAccountId must not be null");
        Objects.requireNonNull(treasuryKey, "treasuryKey must not be null");
        Objects.requireNonNull(supplyKey, "supplyKey must not be null");
        return createToken(name, symbol, AccountId.fromString(treasuryAccountId), PrivateKey.fromString(treasuryKey),
                PrivateKey.fromString(supplyKey));
    }

    /**
     * Create a new token.
     *
     * @param name            the name of the token
     * @param symbol          the symbol of the token
     * @param supplyKey       the private key of the supplier account
     * @param treasuryAccount the treasury account
     * @return the ID of the new token
     * @throws HieroException if the token could not be created
     */
    @NonNull
    default TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull Account treasuryAccount,
            @NonNull PrivateKey supplyKey) throws HieroException {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(symbol, "symbol must not be null");
        Objects.requireNonNull(treasuryAccount, "treasuryAccount must not be null");
        Objects.requireNonNull(supplyKey, "supplyKey must not be null");
        return createToken(name, symbol, treasuryAccount.accountId(), treasuryAccount.privateKey(), supplyKey);
    }

    /**
     * Create a new token.
     *
     * @param name            the name of the token
     * @param symbol          the symbol of the token
     * @param supplyKey       the private key of the supplier account
     * @param treasuryAccount the treasury account
     * @return the ID of the new token
     * @throws HieroException if the token could not be created
     */
    @NonNull
    default TokenId createToken(@NonNull String name, @NonNull String symbol, @NonNull Account treasuryAccount,
            @NonNull String supplyKey) throws HieroException {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(symbol, "symbol must not be null");
        Objects.requireNonNull(treasuryAccount, "treasuryAccount must not be null");
        Objects.requireNonNull(supplyKey, "supplyKey must not be null");
        return createToken(name, symbol, treasuryAccount.accountId(), treasuryAccount.privateKey(),
                PrivateKey.fromString(supplyKey));
    }

    /**
     * Associate an account with token.
     *
     * @param tokenId    the ID of the token
     * @param accountId  the ID of the account
     * @param accountKey the private key of the account
     * @throws HieroException if the account could not be associated with the token
     */
    void associateToken(@NonNull TokenId tokenId, @NonNull AccountId accountId, @NonNull PrivateKey accountKey)
            throws HieroException;

    /**
     * Associate an account with token.
     *
     * @param tokenId    the ID of the token
     * @param accountId  the ID of the account
     * @param accountKey the private key of the account
     * @throws HieroException if the account could not be associated with the token
     */
    default void associateToken(@NonNull TokenId tokenId, @NonNull String accountId, @NonNull String accountKey)
            throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(accountKey, "accountKey must not be null");
        associateToken(tokenId, AccountId.fromString(accountId), PrivateKey.fromString(accountKey));
    }

    /**
     * Associate an account with token.
     *
     * @param tokenId the ID of the token
     * @param account the account
     * @throws HieroException if the account could not be associated with the token
     */
    default void associateToken(@NonNull TokenId tokenId, @NonNull Account account) throws HieroException {
        Objects.requireNonNull(account, "account must not be null");
        associateToken(tokenId, account.accountId(), account.privateKey());
    }

    /**
     * Associate an account with token.
     *
     * @param tokenIds list of the ID of the token
     * @param accountId the accountId
     * @param  accountKey the account privateKey
     * @throws HieroException if the account could not be associated with the token
     */
    void associateToken(@NonNull List<TokenId> tokenIds, @NonNull AccountId accountId, @NonNull PrivateKey accountKey)
            throws HieroException;

    /**
     * Associate an account with token.
     *
     * @param tokenIds list of the ID of the token
     * @param account the account
     * @throws HieroException if the account could not be associated with the token
     */
    default void associateToken(@NonNull List<TokenId> tokenIds, @NonNull Account account) throws HieroException {
        Objects.requireNonNull(account, "accountId must not be null");
        associateToken(tokenIds, account.accountId(), account.privateKey());
    };

    /**
     * Dissociate an account with token.
     *
     * @param tokenId the ID of the token
     * @param accountId the accountId
     * @param accountKey the account privateKey
     * @throws HieroException if the account could not be associated with the token
     */
    void dissociateToken(@NonNull TokenId tokenId, @NonNull AccountId accountId, @NonNull PrivateKey accountKey)
            throws HieroException;

    /**
     * Dissociate an account with token.
     *
     * @param tokenId the ID of the token
     * @param accountId the accountId
     * @param accountKey the account privateKey
     * @throws HieroException if the account could not be associated with the token
     */
    default void dissociateToken(@NonNull String tokenId, @NonNull String accountId, @NonNull String accountKey)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(accountKey, "accountKey must not be null");
        dissociateToken(TokenId.fromString(tokenId), AccountId.fromString(accountId), PrivateKey.fromString(accountKey));
    };

    /**
     * Dissociate an account with token.
     *
     * @param tokenId the ID of the token
     * @param account the account
     * @throws HieroException if the account could not be associated with the token
     */
    default void dissociateToken(@NonNull TokenId tokenId, @NonNull Account account) throws HieroException {
        Objects.requireNonNull(account, "accountId must not be null");
        dissociateToken(tokenId, account.accountId(), account.privateKey());
    };

    /**
     * Dissociate an account with token.
     *
     * @param tokenIds list of the ID of the token
     * @param accountId the accountId
     * @param  accountKey the account privateKey
     * @throws HieroException if the account could not be associated with the token
     */
    void dissociateToken(@NonNull List<TokenId> tokenIds, @NonNull AccountId accountId, @NonNull PrivateKey accountKey)
            throws HieroException;

    /**
     * Dissociate an account with token.
     *
     * @param tokenIds list of the ID of the token
     * @param account the account
     * @throws HieroException if the account could not be associated with the token
     */
    default void dissociateToken(@NonNull List<TokenId> tokenIds, @NonNull Account account) throws HieroException {
        Objects.requireNonNull(account, "accountId must not be null");
        dissociateToken(tokenIds, account.accountId(), account.privateKey());
    };

    /**
     * Mint a Token.
     *
     * @param tokenId the ID of the token
     * @param amount  amount to mint to the Treasury Account
     * @return totalSupply for the token
     * @throws HieroException if the token could not be minted
     */
    long mintToken(@NonNull TokenId tokenId, long amount) throws HieroException;

    /**
     * Mint a Token.
     *
     * @param tokenId the ID of the token
     * @param amount  amount to mint to the Treasury Account
     * @return totalSupply for the token
     * @throws HieroException if the token could not be minted
     */
    default long mintToken(@NonNull String tokenId, long amount) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return mintToken(TokenId.fromString(tokenId), amount);
    }

    /**
     * Mint a Token.
     *
     * @param tokenId   the ID of the token
     * @param amount    amount to mint to the Treasury Account
     * @param supplyKey the private key of the supply account
     * @return totalSupply for the token
     * @throws HieroException if the token could not be minted
     */
    long mintToken(@NonNull TokenId tokenId, @NonNull PrivateKey supplyKey, long amount) throws HieroException;

    /**
     * Mint a Token.
     *
     * @param tokenId   the ID of the token
     * @param amount    amount to mint to the Treasury Account
     * @param supplyKey the private key of the supply account
     * @return totalSupply for the token
     * @throws HieroException if the token could not be minted
     */
    default long mintToken(@NonNull String tokenId, @NonNull String supplyKey, long amount)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(supplyKey, "supplyKey must not be null");
        return mintToken(TokenId.fromString(tokenId), PrivateKey.fromString(supplyKey), amount);
    }

    /**
     * Burn a Token.
     *
     * @param tokenId the ID of the token
     * @param amount  amount to burn
     * @return totalSupply for the token
     * @throws HieroException if the token could not be burned
     */
    long burnToken(@NonNull TokenId tokenId, long amount) throws HieroException;

    /**
     * Burn a Token.
     *
     * @param tokenId   the ID of the token
     * @param amount    amount to burn
     * @param supplyKey the private key of the supply account
     * @return totalSupply for the token
     * @throws HieroException if the token could not be burned
     */
    long burnToken(@NonNull TokenId tokenId, long amount, @NonNull PrivateKey supplyKey) throws HieroException;

    /**
     * Burn a Token.
     *
     * @param tokenId   the ID of the token
     * @param amount    amount to burn
     * @param supplyKey the private key of the supply account
     * @return totalSupply for the token
     * @throws HieroException if the token could not be burned
     */
    default long burnToken(@NonNull TokenId tokenId, long amount, @NonNull String supplyKey) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(supplyKey, "supplyKey must not be null");
        return burnToken(tokenId, amount, PrivateKey.fromString(supplyKey));
    }

    /**
     * Transfer a Token to another account.
     *
     * @param tokenId     the ID of the token
     * @param amount      the value of token to transfer
     * @param toAccountId the ID of the account that should receive the token
     * @throws HieroException if the token could not be transferred
     */
    void transferToken(@NonNull TokenId tokenId, @NonNull AccountId toAccountId, long amount) throws HieroException;

    /**
     * Transfer a Token to another account.
     *
     * @param tokenId     the ID of the token
     * @param amount      the value of token to transfer
     * @param toAccountId the ID of the account that should receive the token
     * @throws HieroException if the token could not be transferred
     */
    default void transferToken(@NonNull TokenId tokenId, @NonNull String toAccountId, long amount)
            throws HieroException {
        transferToken(tokenId, AccountId.fromString(toAccountId), amount);
    }


    /**
     * Transfer a Token to another account.
     *
     * @param tokenId        the ID of the token
     * @param amount         the value of token to transfer
     * @param fromAccountId  the ID of the account that holds the token
     * @param fromAccountKey the private key of the account that holds the token
     * @param toAccountId    the ID of the account that should receive the token
     * @throws HieroException if the token could not be transferred
     */
    void transferToken(@NonNull TokenId tokenId, @NonNull AccountId fromAccountId, @NonNull PrivateKey fromAccountKey,
            @NonNull AccountId toAccountId, long amount) throws HieroException;

    /**
     * Transfer a Token to another account.
     *
     * @param tokenId     the ID of the token
     * @param amount      the value of token to transfer
     * @param fromAccount the account that holds the token
     * @param toAccountId the ID of the account that should receive the token
     * @throws HieroException if the token could not be transferred
     */
    default void transferToken(@NonNull TokenId tokenId, @NonNull Account fromAccount, @NonNull AccountId toAccountId,
            long amount)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(fromAccount, "fromAccount must not be null");
        Objects.requireNonNull(toAccountId, "toAccountId must not be null");
        transferToken(tokenId, fromAccount.accountId(), fromAccount.privateKey(), toAccountId, amount);
    }

    /**
     * Transfer a Token to another account.
     *
     * @param tokenId        the ID of the token
     * @param amount         the value of token to transfer
     * @param fromAccountId  the ID of the account that holds the token
     * @param fromAccountKey the private key of the account that holds the token
     * @param toAccountId    the ID of the account that should receive the token
     * @throws HieroException if the token could not be transferred
     */
    default void transferToken(@NonNull TokenId tokenId, @NonNull String fromAccountId, @NonNull String fromAccountKey,
            @NonNull String toAccountId, long amount) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(fromAccountId, "fromAccountId must not be null");
        Objects.requireNonNull(fromAccountKey, "fromAccountKey must not be null");
        Objects.requireNonNull(toAccountId, "toAccountId must not be null");
        transferToken(tokenId, AccountId.fromString(fromAccountId), PrivateKey.fromString(fromAccountKey),
                AccountId.fromString(toAccountId), amount);
    }
}
