package com.openelements.hiero.base;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hiero.base.data.Account;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with a Hiero network. This interface provides methods for interacting with Hedera accounts,
 * like creating and deleting accounts. An implementation of this interface is using an internal account to interact
 * with a Hiero network. That account is the account that is used to pay for the transactions that are sent to the
 * network and called 'operator account'.
 */
public interface AccountClient {

    /**
     * Creates a new account. The account is created with an initial balance of 0 hbar. The account is created by the
     * operator account.
     *
     * @return the created account
     * @throws HieroException if the account could not be created
     */
    @NonNull
    default Account createAccount() throws HieroException {
        return createAccount(Hbar.ZERO);
    }

    /**
     * Creates a new account with the given initial balance. The account is created by the operator account.
     *
     * @param initialBalance the initial balance of the account
     * @return the created account
     * @throws HieroException if the account could not be created
     */
    @NonNull
    Account createAccount(@NonNull Hbar initialBalance) throws HieroException;

    /**
     * Creates a new account with the given initial balance (in HBAR). The account is created by the operator account.
     *
     * @param initialBalanceInHbar the initial balance of the account in HBAR
     * @return the created account
     * @throws HieroException if the account could not be created
     */
    @NonNull
    default Account createAccount(long initialBalanceInHbar) throws HieroException {
        if (initialBalanceInHbar < 0) {
            throw new IllegalArgumentException("initialBalanceInHbar must be non-negative");
        }
        return createAccount(Hbar.from(initialBalanceInHbar));
    }

    /**
     * Deletes the account with the given ID. All fees of that account are transferred to the operator account.
     *
     * @param account the account to delete
     * @throws HieroException if the account could not be deleted
     */
    void deleteAccount(@NonNull Account account) throws HieroException;

    /**
     * Deletes the account with the given ID. All fees of that account are transferred to the given toAccount.
     *
     * @param account   the account to delete
     * @param toAccount the account to transfer the fees to
     * @throws HieroException if the account could not be deleted
     */
    void deleteAccount(@NonNull Account account, @NonNull Account toAccount) throws HieroException;

    /**
     * Returns the balance of the given account.
     *
     * @param accountId the ID of the account
     * @return the balance of the account
     * @throws HieroException if the balance could not be retrieved
     */
    @NonNull
    Hbar getAccountBalance(@NonNull AccountId accountId) throws HieroException;

    /**
     * Returns the balance of the given account.
     *
     * @param accountId the ID of the account
     * @return the balance of the account
     * @throws HieroException if the balance could not be retrieved
     */
    @NonNull
    default Hbar getAccountBalance(@NonNull String accountId) throws HieroException {
        Objects.requireNonNull(accountId, "newAccountId must not be null");
        return getAccountBalance(AccountId.fromString(accountId));
    }

    /**
     * Returns the balance of the operator account.
     *
     * @return the balance of the operator account
     * @throws HieroException if the balance could not be retrieved
     */
    @NonNull
    Hbar getOperatorAccountBalance() throws HieroException;
}
