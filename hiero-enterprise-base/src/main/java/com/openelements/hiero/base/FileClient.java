package com.openelements.hiero.base;

import com.hedera.hashgraph.sdk.FileId;
import java.time.Instant;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * A client for interacting with the file service on a Hiero network. An implementation of this interface is using an
 * internal account to interact with a Hiero network. That account is the account that is used to pay for the
 * transactions that are sent to a Hiero network and called 'operator account'.
 */
public interface FileClient {

    /**
     * Create a new file with the given contents.
     *
     * @param contents the contents of the file
     * @return the ID of the new file
     * @throws HieroException if the file could not be created
     */
    @NonNull
    FileId createFile(@NonNull byte[] contents) throws HieroException;

    /**
     * Create a new file with the given contents and expiration time.
     *
     * @param contents       the contents of the file
     * @param expirationTime the expiration time of the file
     * @return the ID of the new file
     * @throws HieroException if the file could not be created
     */
    @NonNull
    FileId createFile(@NonNull byte[] contents, @NonNull Instant expirationTime) throws HieroException;

    /**
     * Create a new file with the given contents.
     *
     * @param fileId the ID of the file to update
     * @return the ID of the new file
     * @throws HieroException if the file could not be created
     */
    @NonNull
    default byte[] readFile(@NonNull String fileId) throws HieroException {
        Objects.requireNonNull(fileId, "fileId must not be null");
        return readFile(FileId.fromString(fileId));
    }

    /**
     * Read the contents of a file.
     *
     * @param fileId the ID of the file to read
     * @return the contents of the file
     * @throws HieroException if the file could not be read
     */
    @NonNull
    byte[] readFile(@NonNull FileId fileId) throws HieroException;

    /**
     * Delete a file.
     *
     * @param fileId the ID of the file to delete
     * @throws HieroException if the file could not be deleted
     */
    default void deleteFile(@NonNull String fileId) throws HieroException {
        deleteFile(FileId.fromString(fileId));
    }

    /**
     * Delete a file.
     *
     * @param fileId the ID of the file to delete
     * @throws HieroException if the file could not be deleted
     */
    void deleteFile(@NonNull FileId fileId) throws HieroException;

    /**
     * Update the contents of a file.
     *
     * @param fileId  the ID of the file to update
     * @param content the new contents of the file
     * @throws HieroException if the file could not be updated
     */
    void updateFile(@NonNull FileId fileId, byte[] content) throws HieroException;

    /**
     * Update the expiration time of a file.
     *
     * @param fileId         the ID of the file to update
     * @param expirationTime the new expiration time of the file
     * @throws HieroException if the file could not be updated
     */
    void updateExpirationTime(@NonNull FileId fileId, @NonNull Instant expirationTime) throws HieroException;

    /**
     * Check if a file is deleted.
     *
     * @param fileId the ID of the file to check
     * @return true if the file is deleted, false otherwise
     * @throws HieroException if the file could not be checked
     */
    boolean isDeleted(@NonNull FileId fileId) throws HieroException;

    /**
     * Get the size of a file.
     *
     * @param fileId the ID of the file to check
     * @return the size of the file
     * @throws HieroException if the file could not be checked
     */
    int getSize(@NonNull FileId fileId) throws HieroException;

    /**
     * Get the expiration time of a file.
     *
     * @param fileId the ID of the file to check
     * @return the expiration time of the file
     * @throws HieroException if the file could not be checked
     */
    @NonNull
    Instant getExpirationTime(@NonNull FileId fileId) throws HieroException;
}
