package com.openelements.hiero.base.protocol.data;

import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record TokenMintRequest(
    @NonNull Hbar maxTransactionFee,
    @NonNull Duration transactionValidDuration,
    @NonNull TokenId tokenId,
    @NonNull PrivateKey supplyKey,
    @Nullable Long amount,
    @NonNull List<byte[]> metadata)
    implements TransactionRequest {

  static final int MAX_METADATA_SIZE = 100;

  public TokenMintRequest {
    Objects.requireNonNull(tokenId, "tokenId must not be null");
    Objects.requireNonNull(supplyKey, "supplyKey must not be null");
    Objects.requireNonNull(metadata, "metadata must not be null");
    if (amount == null && metadata.isEmpty()) {
      throw new IllegalArgumentException("either amount or metadata must be provided");
    }
    if (amount != null && amount <= 0) {
      throw new IllegalArgumentException("amount must be greater than 0");
    }
    metadata.forEach(
        m -> {
          if (m.length > MAX_METADATA_SIZE) {
            throw new IllegalArgumentException(
                "each metadata entry must be less than " + MAX_METADATA_SIZE + " bytes");
          }
        });
  }

  public static TokenMintRequest of(
      @NonNull final TokenId tokenId,
      @NonNull final PrivateKey supplyKey,
      @NonNull byte[]... metadata) {
    Objects.requireNonNull(metadata, "metadata must not be null");
    return new TokenMintRequest(
        TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
        TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION,
        tokenId,
        supplyKey,
        null,
        List.of(metadata));
  }

  public static TokenMintRequest of(
      @NonNull final TokenId tokenId, @NonNull final PrivateKey supplyKey) {
    return new TokenMintRequest(
        TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
        TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION,
        tokenId,
        supplyKey,
        null,
        List.of());
  }

  public static TokenMintRequest of(
      @NonNull final TokenId tokenId, @NonNull final PrivateKey supplyKey, long amount) {
    return new TokenMintRequest(
        TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
        TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION,
        tokenId,
        supplyKey,
        amount,
        List.of());
  }

  public static TokenMintRequest of(
      @NonNull final TokenId tokenId,
      @NonNull final PrivateKey supplyKey,
      @NonNull final String metadata) {
    Objects.requireNonNull(metadata, "metadata must not be null");
    return new TokenMintRequest(
        TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
        TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION,
        tokenId,
        supplyKey,
        null,
        List.of(metadata.getBytes(StandardCharsets.UTF_8)));
  }

  public static TokenMintRequest of(
      @NonNull final TokenId tokenId,
      @NonNull final PrivateKey supplyKey,
      @NonNull final List<String> metadata) {
    Objects.requireNonNull(metadata, "metadata must not be null");
    final List<byte[]> metadataBytes =
        metadata.stream().map(m -> m.getBytes(StandardCharsets.UTF_8)).toList();
    return new TokenMintRequest(
        TransactionRequest.DEFAULT_MAX_TRANSACTION_FEE,
        TransactionRequest.DEFAULT_TRANSACTION_VALID_DURATION,
        tokenId,
        supplyKey,
        null,
        metadataBytes);
  }
}
