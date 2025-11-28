package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.NftMetadata;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import com.openelements.hiero.base.mirrornode.NftRepository;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public class NftRepositoryImpl implements NftRepository {

  private final MirrorNodeClient mirrorNodeClient;

  public NftRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
    this.mirrorNodeClient =
        Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
  }

  @NonNull
  @Override
  public Page<Nft> findByOwner(@NonNull final AccountId owner) throws HieroException {
    return mirrorNodeClient.queryNftsByAccount(owner);
  }

  @NonNull
  @Override
  public Page<Nft> findByType(@NonNull final TokenId tokenId) throws HieroException {
    return mirrorNodeClient.queryNftsByTokenId(tokenId);
  }

  @NonNull
  @Override
  public Optional<Nft> findByTypeAndSerial(@NonNull final TokenId tokenId, final long serialNumber)
      throws HieroException {
    return mirrorNodeClient.queryNftsByTokenIdAndSerial(tokenId, serialNumber);
  }

  @NonNull
  @Override
  public Page<Nft> findByOwnerAndType(
      @NonNull final AccountId owner, @NonNull final TokenId tokenId) throws HieroException {
    return mirrorNodeClient.queryNftsByAccountAndTokenId(owner, tokenId);
  }

  @NonNull
  @Override
  public Optional<Nft> findByOwnerAndTypeAndSerial(
      @NonNull final AccountId owner, @NonNull final TokenId tokenId, final long serialNumber)
      throws HieroException {
    return mirrorNodeClient.queryNftsByAccountAndTokenIdAndSerial(owner, tokenId, serialNumber);
  }

  @NonNull
  @Override
  public Optional<NftMetadata> getNftMetadata(TokenId tokenId) throws HieroException {
    return mirrorNodeClient.getNftMetadata(tokenId);
  }

  @NonNull
  @Override
  public Page<NftMetadata> findTypesByOwner(@NonNull AccountId ownerId) throws HieroException {
    return mirrorNodeClient.findNftTypesByOwner(ownerId);
  }

  @Override
  public Page<NftMetadata> findAllTypes() throws HieroException {
    return mirrorNodeClient.findAllNftTypes();
  }
}
