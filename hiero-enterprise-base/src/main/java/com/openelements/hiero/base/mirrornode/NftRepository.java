package com.openelements.hiero.base.mirrornode;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.NftMetadata;
import com.openelements.hiero.base.data.Page;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with a Hiero network. This interface provides methods for searching for
 * NFTs.
 */
public interface NftRepository {

  /**
   * Return all NFT types.
   *
   * @return first page of NFT types
   * @throws HieroException if the search fails
   */
  @NonNull Page<NftMetadata> findAllTypes() throws HieroException;

  /**
   * Return all NFT types owned by the given owner.
   *
   * @param ownerId id of the owner account
   * @return first page of NFT types
   * @throws HieroException if the search fails
   */
  @NonNull Page<NftMetadata> findTypesByOwner(@NonNull AccountId ownerId) throws HieroException;

  /**
   * Return all NFT types owned by the given owner.
   *
   * @param ownerId id of the owner account
   * @return first page of NFT types
   * @throws HieroException if the search fails
   */
  @NonNull
  default Page<NftMetadata> findTypesByOwner(@NonNull String ownerId) throws HieroException {
    Objects.requireNonNull(ownerId, "ownerId must not be null");
    return findTypesByOwner(AccountId.fromString(ownerId));
  }

  /**
   * Return all NFTs that are owned by the given owner.
   *
   * @param ownerId id of the owner account
   * @return list of NFTs
   * @throws HieroException if the search fails
   */
  @NonNull Page<Nft> findByOwner(@NonNull AccountId ownerId) throws HieroException;

  /**
   * Return all NFTs that are owned by the given owner.
   *
   * @param ownerId id of the owner account
   * @return list of NFTs
   * @throws HieroException if the search fails
   */
  @NonNull
  default Page<Nft> findByOwner(@NonNull String ownerId) throws HieroException {
    Objects.requireNonNull(ownerId, "ownerId must not be null");
    return findByOwner(AccountId.fromString(ownerId));
  }

  /**
   * Return all NFTs of a given type.
   *
   * @param tokenId id of the token type
   * @return list of NFTs
   * @throws HieroException if the search fails
   */
  @NonNull Page<Nft> findByType(@NonNull TokenId tokenId) throws HieroException;

  /**
   * Return all NFTs of a given type.
   *
   * @param tokenId id of the token type
   * @return list of NFTs
   * @throws HieroException if the search fails
   */
  @NonNull
  default Page<Nft> findByType(@NonNull String tokenId) throws HieroException {
    Objects.requireNonNull(tokenId, "tokenId must not be null");
    return findByType(TokenId.fromString(tokenId));
  }

  /**
   * Return the NFTs of a given type with the given serial.
   *
   * @param tokenId id of the token type
   * @param serialNumber serial of the nft instance
   * @return {@link Optional} containing the found NFT or null
   * @throws HieroException if the search fails
   */
  @NonNull Optional<Nft> findByTypeAndSerial(@NonNull TokenId tokenId, long serialNumber)
      throws HieroException;

  /**
   * Return the NFTs of a given type with the given serial.
   *
   * @param tokenId id of the token type
   * @param serialNumber serial of the nft instance
   * @return {@link Optional} containing the found NFT or null
   * @throws HieroException if the search fails
   */
  @NonNull
  default Optional<Nft> findByTypeAndSerial(@NonNull String tokenId, long serialNumber)
      throws HieroException {
    Objects.requireNonNull(tokenId, "tokenId must not be null");
    return findByTypeAndSerial(TokenId.fromString(tokenId), serialNumber);
  }

  /**
   * Return all NFTs of a given type owned by a specific account.
   *
   * @param ownerId id of the owner
   * @param tokenId id of the token type
   * @return list of NFTs
   * @throws HieroException if the search fails
   */
  @NonNull Page<Nft> findByOwnerAndType(@NonNull AccountId ownerId, @NonNull TokenId tokenId)
      throws HieroException;

  /**
   * Return all NFTs of a given type owned by a specific account.
   *
   * @param ownerId id of the owner
   * @param tokenId id of the token type
   * @return list of NFTs
   * @throws HieroException if the search fails
   */
  @NonNull
  default Page<Nft> findByOwnerAndType(@NonNull String ownerId, @NonNull String tokenId)
      throws HieroException {
    Objects.requireNonNull(ownerId, "ownerId must not be null");
    Objects.requireNonNull(tokenId, "tokenId must not be null");
    return findByOwnerAndType(AccountId.fromString(ownerId), TokenId.fromString(tokenId));
  }

  /**
   * Return the NFT of a given type and serial owned by a specific account.
   *
   * @param owner id of the owner
   * @param tokenId id of the token type
   * @param serialNumber serial of the nft instance
   * @return {@link Optional} containing the found NFT or null
   * @throws HieroException if the search fails
   */
  @NonNull Optional<Nft> findByOwnerAndTypeAndSerial(
      @NonNull AccountId owner, @NonNull TokenId tokenId, long serialNumber) throws HieroException;

  /**
   * Return the NFT of a given type and serial owned by a specific account.
   *
   * @param owner id of the owner
   * @param tokenId id of the token type
   * @param serialNumber serial of the nft instance
   * @return {@link Optional} containing the found NFT or null
   * @throws HieroException if the search fails
   */
  @NonNull
  default Optional<Nft> findByOwnerAndTypeAndSerial(
      @NonNull String owner, @NonNull String tokenId, long serialNumber) throws HieroException {
    Objects.requireNonNull(owner, "owner must not be null");
    Objects.requireNonNull(tokenId, "tokenId must not be null");
    return findByOwnerAndTypeAndSerial(
        AccountId.fromString(owner), TokenId.fromString(tokenId), serialNumber);
  }

  /**
   * Return the NFT metadata of a given tokenId.
   *
   * @param tokenId id of the token type
   * @return {@link Optional} containing the found NFT metadata or null
   * @throws HieroException if the search fails
   */
  @NonNull
  default Optional<NftMetadata> getNftMetadata(@NonNull String tokenId) throws HieroException {
    Objects.requireNonNull(tokenId, "tokenId must not be null");
    return getNftMetadata(TokenId.fromString(tokenId));
  }

  /**
   * Return the NFT metadata of a given tokenId.
   *
   * @param tokenId id of the token type
   * @return {@link Optional} containing the found NFT metadata or null
   * @throws HieroException if the search fails
   */
  @NonNull Optional<NftMetadata> getNftMetadata(@NonNull TokenId tokenId) throws HieroException;

  /**
   * Return the NFT metadata of a given NFT.
   *
   * @param nft NFT instance
   * @return {@link Optional} containing the found NFT metadata or null
   * @throws HieroException if the search fails
   */
  @NonNull
  default Optional<NftMetadata> getNftMetadata(@NonNull Nft nft) throws HieroException {
    Objects.requireNonNull(nft, "nft must not be null");
    return getNftMetadata(nft.tokenId());
  }
}
