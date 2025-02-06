package com.openelements.hiero.base.mirrornode;

import com.hedera.hashgraph.sdk.AccountId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.AccountInfo;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with a Hiero network. This interface provides methods for searching for Accounts.
 */
public interface AccountRepository {
    /**
     * Return the AccountInfo of a given accountId.
     *
     * @param accountId id of the account
     * @return {@link Optional} containing the found AccountInfo or null
     * @throws HieroException if the search fails
     */
    @NonNull
    Optional<AccountInfo> findById(@NonNull AccountId accountId) throws HieroException;

    /**
     * Return the AccountInfo of a given accountId.
     *
     * @param accountId id of the account
     * @return {@link Optional} containing the found AccountInfo or null
     * @throws HieroException if the search fails
     */
    @NonNull
    default Optional<AccountInfo> findById(@NonNull String accountId) throws HieroException {
        Objects.requireNonNull(accountId, "accountId must not be null");
        return findById(AccountId.fromString(accountId));
    }
}
