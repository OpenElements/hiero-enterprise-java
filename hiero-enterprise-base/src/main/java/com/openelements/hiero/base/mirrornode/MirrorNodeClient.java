package com.openelements.hiero.base.mirrornode;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TopicId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.data.Balance;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.NftMetadata;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.Token;
import com.openelements.hiero.base.data.TokenInfo;
import com.openelements.hiero.base.data.TransactionInfo;
import com.openelements.hiero.base.data.Topic;
import com.openelements.hiero.base.data.TopicMessage;
import com.openelements.hiero.base.data.Result;
import com.openelements.hiero.base.data.BalanceModification;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.openelements.hiero.base.protocol.data.TransactionType;
import org.jspecify.annotations.NonNull;

/**
 * A client for querying the Hiero Mirror Node REST API.
 */
public interface MirrorNodeClient {

    /**
     * Queries the NFTs owned by an account.
     *
     * @param accountId the account ID
     * @return the NFTs owned by the account
     * @throws HieroException if an error occurs
     */
    @NonNull
    Page<Nft> queryNftsByAccount(@NonNull AccountId accountId) throws HieroException;

    /**
     * Queries the NFTs owned by an account.
     *
     * @param accountId the account ID
     * @return the NFTs owned by the account
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Page<Nft> queryNftsByAccount(@NonNull String accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return queryNftsByAccount(AccountId.fromString(accountId));
    }

    /**
     * Queries the NFTs owned by an account for a specific token ID.
     *
     * @param accountId the account ID
     * @param tokenId   the token ID
     * @return the NFTs owned by the account for the token ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    Page<Nft> queryNftsByAccountAndTokenId(@NonNull AccountId accountId, @NonNull TokenId tokenId)
            throws HieroException;

    /**
     * Queries the NFTs owned by an account for a specific token ID.
     *
     * @param accountId the account ID
     * @param tokenId   the token ID
     * @return the NFTs owned by the account for the token ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Page<Nft> queryNftsByAccountAndTokenId(@NonNull String accountId, @NonNull String tokenId)
            throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByAccountAndTokenId(AccountId.fromString(accountId), TokenId.fromString(tokenId));
    }

    /**
     * Queries the NFTs for a specific token ID.
     *
     * @param tokenId the token ID
     * @return the NFTs for the token ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    Page<Nft> queryNftsByTokenId(@NonNull TokenId tokenId) throws HieroException;

    /**
     * Queries the NFTs for a specific token ID.
     *
     * @param tokenId the token ID
     * @return the NFTs for the token ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Page<Nft> queryNftsByTokenId(@NonNull String tokenId) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByTokenId(TokenId.fromString(tokenId));
    }

    /**
     * Queries the NFTs for a specific token ID and serial number.
     *
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs for the token ID and serial number
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull TokenId tokenId, long serialNumber) throws HieroException;

    /**
     * Queries the NFTs for a specific token ID and serial number.
     *
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs for the token ID and serial number
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Optional<Nft> queryNftsByTokenIdAndSerial(@NonNull String tokenId, long serialNumber)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByTokenIdAndSerial(TokenId.fromString(tokenId), serialNumber);
    }

    /**
     * Queries the NFTs owned by an account for a specific token ID and serial number.
     *
     * @param accountId    the account ID
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs owned by the account for the token ID and serial number
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull AccountId accountId, @NonNull TokenId tokenId,
            long serialNumber) throws HieroException {
        Objects.requireNonNull(accountId, "newAccountId must not be null");
        return queryNftsByTokenIdAndSerial(tokenId, serialNumber)
                .filter(nft -> Objects.equals(nft.owner(), accountId));
    }

    /**
     * Queries the NFTs owned by an account for a specific token ID and serial number.
     *
     * @param accountId    the account ID
     * @param tokenId      the token ID
     * @param serialNumber the serial number
     * @return the NFTs owned by the account for the token ID and serial number
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Optional<Nft> queryNftsByAccountAndTokenIdAndSerial(@NonNull String accountId, @NonNull String tokenId,
            long serialNumber) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryNftsByAccountAndTokenIdAndSerial(AccountId.fromString(accountId), TokenId.fromString(tokenId),
                serialNumber);
    }

    /**
     * Queries all transactions for a specific account.
     *
     * @param accountId the account ID to query transactions for
     * @return a page of transaction information
     * @throws HieroException if an error occurs during the query
     */
    @NonNull
    Page<TransactionInfo> queryTransactionsByAccount(@NonNull AccountId accountId) throws HieroException;


    /**
     * Queries all transactions for a specific account and transaction type.
     *
     * @param accountId the account ID to query transactions for
     * @return a page of transaction information
     * @throws HieroException if an error occurs during the query
     */
    @NonNull
    Page<TransactionInfo> queryTransactionsByAccountAndType(@NonNull AccountId accountId, @NonNull TransactionType type)
            throws HieroException;

    /**
     * Queries all transactions for a specific account and transaction type.
     *
     * @param accountId the account ID to query transactions for
     * @param result the result of transaction
     * @return a page of transaction information
     * @throws HieroException if an error occurs during the query
     */
    @NonNull
    Page<TransactionInfo> queryTransactionsByAccountAndResult(@NonNull AccountId accountId, @NonNull Result result)
            throws HieroException;

    /**
     * Find all transactions associated with a specific account and
     * has specific transaction type.
     *
     * @param accountId id of the account
     * @param type type of balance modification of transaction
     * @return page of transactions
     * @throws HieroException if the search fails
     */
    @NonNull
    Page<TransactionInfo> queryTransactionsByAccountAndModification(@NonNull AccountId accountId, @NonNull BalanceModification type)
            throws HieroException;

    /**
     * Queries the transaction information for a specific transaction ID.
     *
     * @param transactionId the transaction ID
     * @return the transaction information for the transaction ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<TransactionInfo> queryTransaction(@NonNull String transactionId) throws HieroException;

    /**
     * Queries the account information for a specific account ID.
     *
     * @param accountId the account ID
     * @return the account information for the account ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<AccountInfo> queryAccount(@NonNull AccountId accountId) throws HieroException;

    /**
     * Queries the account information for a specific account ID.
     *
     * @param accountId the account ID
     * @return the account information for the account ID
     * @throws HieroException if an error occurs
     */
    @NonNull
    default Optional<AccountInfo> queryAccount(@NonNull String accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return queryAccount(AccountId.fromString(accountId));
    }

    /**
     * Queries the ExchangeRates for the network.
     *
     * @return the Optional of ExchangeRates for the Network
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<ExchangeRates> queryExchangeRates() throws HieroException;

    /**
     * Queries the NetworkFee for the network.
     *
     * @return the List of NetworkFee for the Network
     * @throws HieroException if an error occurs
     */
    @NonNull
    List<NetworkFee> queryNetworkFees() throws HieroException;

    /**
     * Queries the NetworkStake for the network.
     *
     * @return the Optional of NetworkStake for the Network
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<NetworkStake> queryNetworkStake() throws HieroException;

    /**
     * Queries the NetworkSupplies for the network.
     *
     * @return the Optional of NetworkSupplies for the Network
     * @throws HieroException if an error occurs
     */
    @NonNull
    Optional<NetworkSupplies> queryNetworkSupplies() throws HieroException;

    /**
     * Return Tokens associated with given accountId.
     *
     * @param accountId id of the account
     * @return Optional of TokenInfo
     * @throws HieroException if the search fails
     */
    @NonNull
    Page<Token> queryTokensForAccount(@NonNull AccountId accountId) throws HieroException;

    /**
     * Return Tokens associated with given accountId.
     *
     * @param accountId id of the account
     * @return Optional of TokenInfo
     * @throws HieroException if the search fails
     */
    @NonNull
    default Page<Token> queryTokensForAccount(@NonNull String accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return queryTokensForAccount(AccountId.fromString(accountId));
    }

    /**
     * Return Token Info for given tokenID.
     *
     * @param tokenId id of the token
     * @return Optional of Token
     * @throws HieroException if the search fails
     */
    @NonNull
    Optional<TokenInfo> queryTokenById(@NonNull TokenId tokenId) throws HieroException;

    /**
     * Return Token Info for given tokenID.
     *
     * @param tokenId id of the token
     * @return Optional of Token
     * @throws HieroException if the search fails
     */
    @NonNull
    default Optional<TokenInfo> queryTokenById(@NonNull String tokenId) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryTokenById(TokenId.fromString(tokenId));
    }

    /**
     * Return Balance Info for given tokenID.
     *
     * @param tokenId id of the token
     * @return Page of Balance
     * @throws HieroException if the search fails
     */
    @NonNull
    Page<Balance> queryTokenBalances(@NonNull TokenId tokenId) throws HieroException;

    /**
     * Return Balance Info for given tokenID.
     *
     * @param tokenId id of the token
     * @return Page of Balance
     * @throws HieroException if the search fails
     */
    @NonNull
    default Page<Balance> queryTokenBalances(@NonNull String tokenId) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return queryTokenBalances(TokenId.fromString(tokenId));
    }

    /**
     * Return Balance Info for given tokenID and accountId.
     *
     * @param tokenId   id of the token
     * @param accountId id of the account
     * @return Page of Balance
     * @throws HieroException if the search fails
     */
    @NonNull
    Page<Balance> queryTokenBalancesForAccount(@NonNull TokenId tokenId, @NonNull AccountId accountId)
            throws HieroException;

    /**
     * Return Balance Info for given tokenID and accountId.
     *
     * @param tokenId   id of the token
     * @param accountId id of the account
     * @return Page of Balance
     * @throws HieroException if the search fails
     */
    @NonNull
    default Page<Balance> queryTokenBalancesForAccount(@NonNull String tokenId, @NonNull String accountId)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        return queryTokenBalancesForAccount(TokenId.fromString(tokenId), AccountId.fromString(accountId));
    }

    /**
     * Return Topic for given topicId.
     *
     * @param topicId id of the topic
     * @return Optional of Topic
     * @throws HieroException if the search fails
     */
    @NonNull
    Optional<Topic> queryTopicById(TopicId topicId) throws HieroException;

    /**
     * Return Topic for given topicId.
     *
     * @param topicId id of the topic
     * @return Optional of Topic
     * @throws HieroException if the search fails
     */
    @NonNull
    default Optional<Topic> queryTopicById(String topicId) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        return queryTopicById(TopicId.fromString(topicId));
    }

    /**
     * Return TopicMessages for given topicId.
     *
     * @param topicId id of the topic
     * @return Page of TopicMessage
     * @throws HieroException if the search fails
     */
    @NonNull
    Page<TopicMessage> queryTopicMessages(TopicId topicId) throws HieroException;

    /**
     * Return TopicMessages for given topicId.
     *
     * @param topicId id of the topic
     * @return Page of TopicMessage
     * @throws HieroException if the search fails
     */
    @NonNull
    default Page<TopicMessage> queryTopicMessages(String topicId) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        return queryTopicMessages(TopicId.fromString(topicId));
    }

    /**
     * Return TopicMessage for given topicId.
     *
     * @param topicId id of the topic
     * @param sequenceNumber sequenceNumber of the message
     * @return Optional of TopicMessage
     * @throws HieroException if the search fails
     */
    @NonNull
    Optional<TopicMessage> queryTopicMessageBySequenceNumber(TopicId topicId, long sequenceNumber) throws HieroException;

    /**
     * Return TopicMessage for given topicId.
     *
     * @param topicId id of the topic
     * @param sequenceNumber sequenceNumber of the message
     * @return Optional of TopicMessage
     * @throws HieroException if the search fails
     */
    @NonNull
    default Optional<TopicMessage> queryTopicMessageBySequenceNumber(String topicId, long sequenceNumber) throws HieroException {
        Objects.requireNonNull(topicId, "topicId must not be null");
        return queryTopicMessageBySequenceNumber(TopicId.fromString(topicId), sequenceNumber);
    }

    @NonNull
    Optional<NftMetadata> getNftMetadata(@NonNull TokenId tokenId) throws HieroException;

    @NonNull
    Page<NftMetadata> findNftTypesByOwner(@NonNull AccountId ownerId);

    @NonNull
    Page<NftMetadata> findAllNftTypes();
}
