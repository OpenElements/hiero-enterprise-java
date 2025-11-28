package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.FileId;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.FileAppendRequest;
import com.openelements.hiero.base.protocol.data.FileContentsRequest;
import com.openelements.hiero.base.protocol.data.FileContentsResponse;
import com.openelements.hiero.base.protocol.data.FileCreateRequest;
import com.openelements.hiero.base.protocol.data.FileCreateResult;
import com.openelements.hiero.base.protocol.data.FileDeleteRequest;
import com.openelements.hiero.base.protocol.data.FileInfoRequest;
import com.openelements.hiero.base.protocol.data.FileInfoResponse;
import com.openelements.hiero.base.protocol.data.FileUpdateRequest;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileClientImpl implements FileClient {

  private static final Logger log = LoggerFactory.getLogger(FileClientImpl.class);

  private final ProtocolLayerClient protocolLayerClient;

  public FileClientImpl(@NonNull final ProtocolLayerClient protocolLayerClient) {
    this.protocolLayerClient =
        Objects.requireNonNull(protocolLayerClient, "protocolLevelClient must not be null");
  }

  @Override
  public FileId createFile(@NonNull final byte[] contents) throws HieroException {
    return createFileImpl(contents, null);
  }

  @Override
  public FileId createFile(@NonNull final byte[] contents, @NonNull final Instant expirationTime)
      throws HieroException {
    return createFileImpl(contents, expirationTime);
  }

  private FileId createFileImpl(
      @NonNull final byte[] contents, @Nullable final Instant expirationTime)
      throws HieroException {
    Objects.requireNonNull(contents, "contents must not be null");
    if (contents.length > FileCreateRequest.FILE_MAX_SIZE) {
      throw new HieroException(
          "File contents must be less than " + FileCreateRequest.FILE_MAX_SIZE + " bytes");
    }
    if (expirationTime != null && expirationTime.isBefore(Instant.now())) {
      throw new IllegalArgumentException("Expiration time must be in the future");
    }
    if (contents.length <= FileCreateRequest.FILE_CREATE_MAX_SIZE) {
      final FileCreateRequest request;
      if (expirationTime != null) {
        request = FileCreateRequest.of(contents, expirationTime);
      } else {
        request = FileCreateRequest.of(contents);
      }
      final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);
      return result.fileId();
    } else {
      if (log.isDebugEnabled()) {
        final int appendCount =
            Math.floorDiv(contents.length, FileCreateRequest.FILE_CREATE_MAX_SIZE);
        log.debug(
            "Content of size {} is to big for 1 FileCreate transaction. Will append {} FileAppend transactions",
            contents.length,
            appendCount);
      }
      final byte[] start = Arrays.copyOf(contents, FileCreateRequest.FILE_CREATE_MAX_SIZE);
      final FileCreateRequest request = FileCreateRequest.of(start);
      final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);
      final FileId fileId = result.fileId();
      byte[] remaining =
          Arrays.copyOfRange(contents, FileCreateRequest.FILE_CREATE_MAX_SIZE, contents.length);
      while (remaining.length > 0) {
        final int length = Math.min(remaining.length, FileCreateRequest.FILE_CREATE_MAX_SIZE);
        final byte[] next = Arrays.copyOf(remaining, length);
        final FileAppendRequest appendRequest = FileAppendRequest.of(fileId, next);
        protocolLayerClient.executeFileAppendRequestTransaction(appendRequest);
        remaining = Arrays.copyOfRange(remaining, length, remaining.length);
      }
      return fileId;
    }
  }

  @NonNull
  @Override
  public byte[] readFile(@NonNull final FileId fileId) throws HieroException {
    Objects.requireNonNull(fileId, "fileId must not be null");
    try {
      final FileContentsRequest request = FileContentsRequest.of(fileId);
      final FileContentsResponse response = protocolLayerClient.executeFileContentsQuery(request);
      return response.contents();
    } catch (Exception e) {
      throw new HieroException("Failed to read file with fileId " + fileId, e);
    }
  }

  @Override
  public void deleteFile(@NonNull final FileId fileId) throws HieroException {
    Objects.requireNonNull(fileId, "fileId must not be null");
    try {
      final FileDeleteRequest request = FileDeleteRequest.of(fileId);
      protocolLayerClient.executeFileDeleteTransaction(request);
    } catch (Exception e) {
      throw new HieroException("Failed to delete file with fileId " + fileId, e);
    }
  }

  @Override
  public void updateFile(@NonNull final FileId fileId, @NonNull final byte[] content)
      throws HieroException {
    Objects.requireNonNull(fileId, "fileId must not be null");
    Objects.requireNonNull(content, "content must not be null");
    if (content.length > FileCreateRequest.FILE_MAX_SIZE) {
      throw new HieroException(
          "File contents must be less than " + FileCreateRequest.FILE_MAX_SIZE + " bytes");
    }
    if (content.length <= FileCreateRequest.FILE_CREATE_MAX_SIZE) {
      final FileUpdateRequest request = FileUpdateRequest.of(fileId, content);
      protocolLayerClient.executeFileUpdateRequestTransaction(request);
    } else {
      if (log.isDebugEnabled()) {
        final int appendCount =
            Math.floorDiv(content.length, FileCreateRequest.FILE_CREATE_MAX_SIZE);
        log.debug(
            "Content of size {} is to big for 1 FileUpdate transaction. Will append {} FileAppend transactions",
            content.length,
            appendCount);
      }
      byte[] start = Arrays.copyOf(content, FileCreateRequest.FILE_CREATE_MAX_SIZE);
      final FileUpdateRequest request = FileUpdateRequest.of(fileId, start);
      protocolLayerClient.executeFileUpdateRequestTransaction(request);
      byte[] remaining =
          Arrays.copyOfRange(content, FileCreateRequest.FILE_CREATE_MAX_SIZE, content.length);
      while (remaining.length > 0) {
        final int length = Math.min(remaining.length, FileCreateRequest.FILE_CREATE_MAX_SIZE);
        byte[] next = Arrays.copyOf(remaining, length);
        final FileAppendRequest appendRequest = FileAppendRequest.of(fileId, next);
        protocolLayerClient.executeFileAppendRequestTransaction(appendRequest);
        remaining = Arrays.copyOfRange(remaining, length, remaining.length);
      }
    }
  }

  @Override
  public void updateExpirationTime(
      @NonNull final FileId fileId, @NonNull final Instant expirationTime) throws HieroException {
    Objects.requireNonNull(fileId, "fileId must not be null");
    Objects.requireNonNull(expirationTime, "expirationTime must not be null");

    if (expirationTime.isBefore(Instant.now())) {
      throw new IllegalArgumentException("Expiration time must be in the future");
    }
    final FileUpdateRequest request = FileUpdateRequest.of(fileId, expirationTime);
    protocolLayerClient.executeFileUpdateRequestTransaction(request);
  }

  @Override
  public boolean isDeleted(@NonNull final FileId fileId) throws HieroException {
    Objects.requireNonNull(fileId, "fileId must not be null");
    final FileInfoRequest request = FileInfoRequest.of(fileId);
    final FileInfoResponse infoResponse = protocolLayerClient.executeFileInfoQuery(request);
    return infoResponse.deleted();
  }

  @Override
  public int getSize(@NonNull final FileId fileId) throws HieroException {
    Objects.requireNonNull(fileId, "fileId must not be null");
    final FileInfoRequest request = FileInfoRequest.of(fileId);
    final FileInfoResponse infoResponse = protocolLayerClient.executeFileInfoQuery(request);
    return infoResponse.size();
  }

  @Override
  public Instant getExpirationTime(@NonNull final FileId fileId) throws HieroException {
    Objects.requireNonNull(fileId, "fileId must not be null");
    final FileInfoRequest request = FileInfoRequest.of(fileId);
    final FileInfoResponse infoResponse = protocolLayerClient.executeFileInfoQuery(request);
    return infoResponse.expirationTime();
  }
}
