package com.openelements.hiero.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.data.Account;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.openelements.hiero.base.data.Token;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with a Hiero network. This interface provides methods for interacting with Hedera NFTs,
 * like creating and deleting NFTs. An implementation of this interface is using an internal account to interact with a
 * Hiero network. That account is the account that is used to pay for the transactions that are sent to the Hedera
 * network and called 'operator account'.
 */
public interface NftClient {

    /**
     * Create a new NFT type. That type is 'owned' by the operator account. The operator account is used as suppler
     * account and as treasury account for the NFT type.
     *
     * @param name   the name of the NFT type
     * @param symbol the symbol of the NFT type
     * @return the ID of the new NFT type
     * @throws HieroException if the NFT type could not be created
     */
    @NonNull
    TokenId createNftType(@NonNull String name, @NonNull String symbol) throws HieroException;

    /**
     * Create a new NFT type. The operator account is used as treasury account for the NFT type.
     *
     * @param name        the name of the NFT type
     * @param symbol      the symbol of the NFT type
     * @param supplierKey the private key of the supplier account
     * @return the ID of the new NFT type
     * @throws HieroException if the NFT type could not be created
     */
    @NonNull
    TokenId createNftType(@NonNull String name, @NonNull String symbol, @NonNull PrivateKey supplierKey)
            throws HieroException;

    /**
     * Create a new NFT type. The operator account is used as treasury account for the NFT type.
     *
     * @param name        the name of the NFT type
     * @param symbol      the symbol of the NFT type
     * @param supplierKey the private key of the supplier account
     * @return the ID of the new NFT type
     * @throws HieroException if the NFT type could not be created
     */
    @NonNull
    default TokenId createNftType(@NonNull String name, @NonNull String symbol, @NonNull String supplierKey)
            throws HieroException {
        Objects.requireNonNull(supplierKey, "supplierKey must not be null");
        return createNftType(name, symbol, PrivateKey.fromString(supplierKey));
    }

    /**
     * Create a new NFT type. The operator account is used as supplier account for the NFT type.
     *
     * @param name              the name of the NFT type
     * @param symbol            the symbol of the NFT type
     * @param treasuryAccountId the ID of the treasury account
     * @param treasuryKey       the private key of the treasury account
     * @return the ID of the new NFT type
     * @throws HieroException if the NFT type could not be created
     */
    @NonNull
    TokenId createNftType(@NonNull String name, @NonNull String symbol, @NonNull AccountId treasuryAccountId,
            @NonNull PrivateKey treasuryKey) throws HieroException;

    /**
     * Create a new NFT type. The operator account is used as supplier account for the NFT type.
     *
     * @param name              the name of the NFT type
     * @param symbol            the symbol of the NFT type
     * @param treasuryAccountId the ID of the treasury account
     * @param treasuryKey       the private key of the treasury account
     * @return the ID of the new NFT type
     * @throws HieroException if the NFT type could not be created
     */
    @NonNull
    default TokenId createNftType(@NonNull String name, @NonNull String symbol, @NonNull String treasuryAccountId,
            @NonNull String treasuryKey) throws HieroException {
        Objects.requireNonNull(treasuryAccountId, "treasuryAccountId must not be null");
        Objects.requireNonNull(treasuryKey, "treasuryKey must not be null");
        return createNftType(name, symbol, AccountId.fromString(treasuryAccountId), PrivateKey.fromString(treasuryKey));
    }

    /**
     * Create a new NFT type. The operator account is used as supplier account for the NFT type.
     *
     * @param name            the name of the NFT type
     * @param symbol          the symbol of the NFT type
     * @param treasuryAccount the treasury account
     * @return the ID of the new NFT type
     * @throws HieroException if the NFT type could not be created
     */
    @NonNull
    default TokenId createNftType(@NonNull String name, @NonNull String symbol, @NonNull Account treasuryAccount)
            throws HieroException {
        Objects.requireNonNull(treasuryAccount, "treasuryAccount must not be null");
        return createNftType(name, symbol, treasuryAccount.accountId(), treasuryAccount.privateKey());
    }


    /**
     * Create a new NFT type.
     *
     * @param name              the name of the NFT type
     * @param symbol            the symbol of the NFT type
     * @param treasuryAccountId the ID of the treasury account
     * @param treasuryKey       the private key of the treasury account
     * @param supplierKey       the private key of the supplier account
     * @return the ID of the new NFT type
     * @throws HieroException if the NFT type could not be created
     */
    @NonNull
    TokenId createNftType(@NonNull String name, @NonNull String symbol, @NonNull AccountId treasuryAccountId,
            @NonNull PrivateKey treasuryKey, @NonNull PrivateKey supplierKey) throws HieroException;

    /**
     * Create a new NFT type.
     *
     * @param name              the name of the NFT type
     * @param symbol            the symbol of the NFT type
     * @param treasuryAccountId the ID of the treasury account
     * @param treasuryKey       the private key of the treasury account
     * @param supplierKey       the private key of the supplier account
     * @return the ID of the new NFT type
     * @throws HieroException if the NFT type could not be created
     */
    @NonNull
    default TokenId createNftType(@NonNull String name, @NonNull String symbol, @NonNull String treasuryAccountId,
            @NonNull String treasuryKey, @NonNull String supplierKey) throws HieroException {
        Objects.requireNonNull(treasuryAccountId, "treasuryAccountId must not be null");
        Objects.requireNonNull(treasuryKey, "treasuryKey must not be null");
        Objects.requireNonNull(supplierKey, "supplierKey must not be null");
        return createNftType(name, symbol, AccountId.fromString(treasuryAccountId), PrivateKey.fromString(treasuryKey),
                PrivateKey.fromString(supplierKey));
    }

    /**
     * Create a new NFT type.
     *
     * @param name            the name of the NFT type
     * @param symbol          the symbol of the NFT type
     * @param treasuryAccount the treasury account
     * @param supplierKey     the private key of the supplier account
     * @return the ID of the new NFT type
     * @throws HieroException if the NFT type could not be created
     */
    @NonNull
    default TokenId createNftType(@NonNull String name, @NonNull String symbol, @NonNull Account treasuryAccount,
            @NonNull PrivateKey supplierKey) throws HieroException {
        Objects.requireNonNull(treasuryAccount, "treasuryAccount must not be null");
        return createNftType(name, symbol, treasuryAccount.accountId(), treasuryAccount.privateKey(), supplierKey);
    }


    /**
     * Associate an account with an NFT type. If an account is associated with an NFT type, the account can hold NFTs of
     * that type. Otherwise, the account cannot hold NFTs of that type and tranfer NFTs of that type will fail.
     *
     * @param tokenId    the ID of the NFT type
     * @param accountId  the ID of the account
     * @param accountKey the private key of the account
     * @throws HieroException if the account could not be associated with the NFT type
     */
    void associateNft(@NonNull TokenId tokenId, @NonNull AccountId accountId, @NonNull PrivateKey accountKey)
            throws HieroException;

    /**
     * Associate an account with an NFT type. If an account is associated with an NFT type, the account can hold NFTs of
     * that type. Otherwise, the account cannot hold NFTs of that type and tranfer NFTs of that type will fail.
     *
     * @param tokenId    the ID of the NFT type
     * @param accountId  the ID of the account
     * @param accountKey the private key of the account
     * @throws HieroException if the account could not be associated with the NFT type
     */
    default void associateNft(@NonNull String tokenId, @NonNull String accountId, @NonNull String accountKey)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(accountKey, "accountKey must not be null");
        associateNft(TokenId.fromString(tokenId), AccountId.fromString(accountId), PrivateKey.fromString(accountKey));
    }

    /**
     * Associate an account with an NFT type. If an account is associated with an NFT type, the account can hold NFTs of
     * that type. Otherwise, the account cannot hold NFTs of that type and tranfer NFTs of that type will fail.
     *
     * @param tokenId the ID of the NFT type
     * @param account the  account
     * @throws HieroException if the account could not be associated with the NFT type
     */
    default void associateNft(@NonNull TokenId tokenId, @NonNull Account account) throws HieroException {
        Objects.requireNonNull(account, "account must not be null");
        associateNft(tokenId, account.accountId(), account.privateKey());
    }

    /**
     * Dissociate an account with an NFT type.
     *
     * @param tokenId the ID of the NFT type
     * @param accountId the  accountId
     * @param accountKey the account privateKey
     * @throws HieroException if the account could not be associated with the NFT type
     */
    void dissociateNft(@NonNull TokenId tokenId, @NonNull AccountId accountId, @NonNull PrivateKey accountKey)
            throws HieroException;

    /**
     * Dissociate an account with an NFT type.
     *
     * @param tokenId the ID of the NFT type
     * @param accountId the  accountId
     * @param accountKey the account privateKey
     * @throws HieroException if the account could not be associated with the NFT type
     */
    default void dissociateNft(@NonNull String tokenId, @NonNull String accountId, @NonNull String accountKey)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(accountId, "accountId must not be null");
        Objects.requireNonNull(accountKey, "accountKey must not be null");
        dissociateNft(TokenId.fromString(tokenId), AccountId.fromString(accountId), PrivateKey.fromString(accountKey));
    };

    /**
     * Dissociate an account with an NFT type.
     *
     * @param tokenId the ID of the NFT type
     * @param account the  account
     * @throws HieroException if the account could not be associated with the NFT type
     */
    default void dissociateNft(@NonNull TokenId tokenId, @NonNull Account account) throws HieroException {
        Objects.requireNonNull(account, "accountId must not be null");
        dissociateNft(tokenId, account.accountId(), account.privateKey());
    };

    /**
     * Dissociate an account with an NFT type.
     *
     * @param tokenIds the List of ID for NFT type
     * @param accountId the  accountId
     * @param accountKey the account privateKey
     * @throws HieroException if the account could not be associated with the NFT type
     */
    void dissociateNft(@NonNull List<TokenId> tokenIds, @NonNull AccountId accountId, @NonNull PrivateKey accountKey)
            throws HieroException;

    /**
     * Dissociate an account with an NFT type.
     *
     * @param tokenIds the List of ID for NFT type
     * @param account the  account
     * @throws HieroException if the account could not be associated with the NFT type
     */
    default void dissociateNft(@NonNull List<TokenId> tokenIds, @NonNull Account account) throws HieroException {
        Objects.requireNonNull(account, "accountId must not be null");
        dissociateNft(tokenIds, account.accountId(), account.privateKey());
    };

    /**
     * Mint a new NFT of the given type. The NFT is minted by the operator account. The operator account is used as
     * supply account for the NFT.
     *
     * @param tokenId  the ID of the NFT type
     * @param metadata the metadata of the NFT
     * @return the serial number of the new NFT
     * @throws HieroException if the NFT could not be minted
     */
    long mintNft(@NonNull TokenId tokenId, @NonNull byte[] metadata) throws HieroException;

    /**
     * Mint a new NFT of the given type. The NFT is minted by the operator account. The operator account is used as
     * supply account for the NFT.
     *
     * @param tokenId  the ID of the NFT type
     * @param metadata the metadata of the NFT
     * @return the serial number of the new NFT
     * @throws HieroException if the NFT could not be minted
     */
    default long mintNft(@NonNull String tokenId, @NonNull byte[] metadata) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return mintNft(TokenId.fromString(tokenId), metadata);
    }

    /**
     * Mint a new NFT of the given type.
     *
     * @param tokenId   the ID of the NFT type
     * @param metadata  the metadata of the NFT
     * @param supplyKey the private key of the supply account
     * @return the serial number of the new NFT
     * @throws HieroException if the NFT could not be minted
     */
    long mintNft(@NonNull TokenId tokenId, @NonNull PrivateKey supplyKey, @NonNull byte[] metadata)
            throws HieroException;

    /**
     * Mint a new NFT of the given type.
     *
     * @param tokenId   the ID of the NFT type
     * @param metadata  the metadata of the NFT
     * @param supplyKey the private key of the supply account
     * @return the serial number of the new NFT
     * @throws HieroException if the NFT could not be minted
     */
    default long mintNft(@NonNull String tokenId, @NonNull String supplyKey, @NonNull byte[] metadata)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(supplyKey, "supplyKey must not be null");
        return mintNft(TokenId.fromString(tokenId), PrivateKey.fromString(supplyKey), metadata);
    }

    /**
     * Mint new NFTs of the given type. The NFTs are minted by the operator account. The operator account is used as
     * supply account for the NFTs.
     *
     * @param tokenId  the ID of the NFT type
     * @param metadata the metadata of the NFTs
     * @return the serial numbers of the new NFTs
     * @throws HieroException if the NFTs could not be minted
     */
    @NonNull
    List<Long> mintNfts(@NonNull TokenId tokenId, @NonNull byte[]... metadata) throws HieroException;

    /**
     * Mint new NFTs of the given type. The NFTs are minted by the operator account. The operator account is used as
     * supply account for the NFTs.
     *
     * @param tokenId  the ID of the NFT type
     * @param metadata the metadata of the NFTs
     * @return the serial numbers of the new NFTs
     * @throws HieroException if the NFTs could not be minted
     */
    @NonNull
    default List<Long> mintNfts(@NonNull String tokenId, @NonNull byte[]... metadata) throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        return mintNfts(TokenId.fromString(tokenId), metadata);
    }

    /**
     * Mint new NFTs of the given type.
     *
     * @param tokenId   the ID of the NFT type
     * @param metadata  the metadata of the NFTs
     * @param supplyKey the private key of the supply account
     * @return the serial numbers of the new NFTs
     * @throws HieroException if the NFTs could not be minted
     */
    @NonNull
    List<Long> mintNfts(@NonNull TokenId tokenId, @NonNull PrivateKey supplyKey, @NonNull byte[]... metadata)
            throws HieroException;

    /**
     * Mint new NFTs of the given type.
     *
     * @param tokenId   the ID of the NFT type
     * @param metadata  the metadata of the NFTs
     * @param supplyKey the private key of the supply account
     * @return the serial numbers of the new NFTs
     * @throws HieroException if the NFTs could not be minted
     */
    @NonNull
    default List<Long> mintNfts(@NonNull String tokenId, @NonNull String supplyKey, @NonNull byte[]... metadata)
            throws HieroException {
        Objects.requireNonNull(tokenId, "tokenId must not be null");
        Objects.requireNonNull(supplyKey, "supplyKey must not be null");
        return mintNfts(TokenId.fromString(tokenId), PrivateKey.fromString(supplyKey), metadata);
    }

    /**
     * Burn an NFT.
     *
     * @param tokenId      the ID of the NFT type
     * @param serialNumber the serial number of the NFT
     * @throws HieroException if the NFT could not be burned
     */
    default void burnNft(@NonNull TokenId tokenId, long serialNumber) throws HieroException {
        burnNfts(tokenId, Set.of(serialNumber));
    }

    /**
     * Burn an NFT.
     *
     * @param tokenId      the ID of the NFT type
     * @param serialNumber the serial number of the NFT
     * @param supplyKey    the private key of the supply account
     * @throws HieroException if the NFT could not be burned
     */
    default void burnNft(@NonNull TokenId tokenId, long serialNumber, @NonNull PrivateKey supplyKey)
            throws HieroException {
        burnNfts(tokenId, Set.of(serialNumber), supplyKey);
    }

    /**
     * Burn NFTs.
     *
     * @param tokenId       the ID of the NFT type
     * @param serialNumbers the serial numbers of the NFTs
     * @throws HieroException if the NFTs could not be burned
     */
    void burnNfts(@NonNull TokenId tokenId, @NonNull Set<Long> serialNumbers) throws HieroException;

    /**
     * Burn NFTs.
     *
     * @param tokenId       the ID of the NFT type
     * @param serialNumbers the serial numbers of the NFTs
     * @param supplyKey     the private key of the supply account
     * @throws HieroException
     */
    void burnNfts(@NonNull TokenId tokenId, @NonNull Set<Long> serialNumbers, @NonNull PrivateKey supplyKey)
            throws HieroException;

    /**
     * Transfer an NFT to another account.
     *
     * @param tokenId        the ID of the NFT type
     * @param serialNumber   the serial number of the NFT
     * @param fromAccountId  the ID of the account that holds the NFT
     * @param fromAccountKey the private key of the account that holds the NFT
     * @param toAccountId    the ID of the account that should receive the NFT
     * @throws HieroException if the NFT could not be transferred
     */
    void transferNft(@NonNull TokenId tokenId, long serialNumber, @NonNull AccountId fromAccountId,
            @NonNull PrivateKey fromAccountKey, @NonNull AccountId toAccountId) throws HieroException;

    /**
     * Transfer an NFT to another account.
     *
     * @param tokenId      the ID of the NFT type
     * @param serialNumber the serial number of the NFT
     * @param fromAccount  the account that holds the NFT
     * @param toAccountId  the ID of the account that should receive the NFT
     * @throws HieroException if the NFT could not be transferred
     */
    default void transferNft(@NonNull TokenId tokenId, long serialNumber, @NonNull Account fromAccount,
            @NonNull AccountId toAccountId) throws HieroException {
        Objects.requireNonNull(fromAccount, "fromAccount must not be null");
        transferNft(tokenId, serialNumber, fromAccount.accountId(), fromAccount.privateKey(), toAccountId);
    }

    /**
     * Transfer NFTs to another account.
     *
     * @param tokenId        the ID of the NFT type
     * @param serialNumbers  the serial numbers of the NFTs
     * @param fromAccountId  the ID of the account that holds the NFTs
     * @param fromAccountKey the private key of the account that holds the NFTs
     * @param toAccountId    the ID of the account that should receive the NFTs
     * @throws HieroException if the NFTs could not be transferred
     */
    void transferNfts(@NonNull TokenId tokenId, @NonNull List<Long> serialNumbers, @NonNull AccountId fromAccountId,
            @NonNull PrivateKey fromAccountKey, @NonNull AccountId toAccountId) throws HieroException;

    /**
     * Transfer NFTs to another account.
     *
     * @param tokenId       the ID of the NFT type
     * @param serialNumbers the serial numbers of the NFTs
     * @param fromAccount   the account that holds the NFTs
     * @param toAccountId   the ID of the account that should receive the NFTs
     * @throws HieroException if the NFTs could not be transferred
     */
    default void transferNfts(@NonNull TokenId tokenId, @NonNull List<Long> serialNumbers, @NonNull Account fromAccount,
            @NonNull AccountId toAccountId) throws HieroException {
        Objects.requireNonNull(fromAccount, "fromAccount must not be null");
        transferNfts(tokenId, serialNumbers, fromAccount.accountId(), fromAccount.privateKey(), toAccountId);
    }
}
