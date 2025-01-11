package com.openelements.hiero.base.mirrornode;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Balance;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.Token;
import com.openelements.hiero.base.data.TokenInfo;
import org.jspecify.annotations.NonNull;

import java.util.Objects;
import java.util.Optional;

/**
 * Interface for interacting with a Hiero network. This interface provides methods for searching Tokens.
 */
public interface TokenRepository {
    /**
     * Return Tokens associated with given accountId.
     *
     * @param accountId id of the account
     * @return Optional of TokenInfo
     * @throws HieroException if the search fails
     */
    Page<Token> findByAccount(@NonNull AccountId accountId) throws HieroException;

    /**
     * Return Tokens associated with given accountId.
     *
     * @param accountId id of the account
     * @return Optional of TokenInfo
     * @throws HieroException if the search fails
     */
    default Page<Token> findByAccount(@NonNull String accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return findByAccount(AccountId.fromString(accountId));
    }

    /**
     * Return Token Info for given tokenID.
     *
     * @param tokenId id of the token
     * @return Optional of TokenInfo
     * @throws HieroException if the search fails
     */
    Optional<TokenInfo> findById(@NonNull TokenId tokenId) throws HieroException;

    /**
     * Return Token Info for given tokenID.
     *
     * @param tokenId id of the token
     * @return Optional of TokenInfo
     * @throws HieroException if the search fails
     */
    default Optional<TokenInfo> findById(@NonNull String tokenId) throws HieroException{
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return  findById(TokenId.fromString(tokenId));
    }

    /**
     * Return Balance Info for given tokenID.
     *
     * @param tokenId id of the token
     * @return Page of Balance
     * @throws HieroException if the search fails
     */
    Page<Balance> getBalances(@NonNull TokenId tokenId) throws HieroException;

    /**
     * Return Balance Info for given tokenID.
     *
     * @param tokenId id of the token
     * @return Page of Balance
     * @throws HieroException if the search fails
     */
    default Page<Balance> getBalances(@NonNull String tokenId) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return getBalances(TokenId.fromString(tokenId));
    }

    /**
     * Return Balance Info for given tokenID and accountId.
     *
     * @param tokenId id of the token
     * @param accountId id of the account
     * @return Page of Balance
     * @throws HieroException if the search fails
     */
    Page<Balance> getBalancesForAccount(@NonNull TokenId tokenId, @NonNull AccountId accountId) throws HieroException;

    /**
     * Return Balance Info for given tokenID and accountId.
     *
     * @param tokenId id of the token
     * @param accountId id of the account
     * @return Page of Balance
     * @throws HieroException if the search fails
     */
    default Page<Balance> getBalancesForAccount(@NonNull String tokenId, @NonNull String accountId) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        return getBalancesForAccount(TokenId.fromString(tokenId), AccountId.fromString(accountId));
    }
}
