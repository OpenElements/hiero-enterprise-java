package com.openelements.hiero.base.mirrornode;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Contract;
import com.openelements.hiero.base.data.Page;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with smart contracts on a Hiero network. This interface provides
 * methods for searching for contracts.
 */
public interface ContractRepository {

  /**
   * Return all contracts.
   *
   * @return first page of contracts
   * @throws HieroException if the search fails
   */
  @NonNull Page<Contract> findAll() throws HieroException;

  /**
   * Return a contract by its contract ID.
   *
   * @param contractId id of the contract
   * @return {@link Optional} containing the found contract or null
   * @throws HieroException if the search fails
   */
  @NonNull Optional<Contract> findById(@NonNull ContractId contractId) throws HieroException;

  /**
   * Return a contract by its contract ID.
   *
   * @param contractId id of the contract
   * @return {@link Optional} containing the found contract or null
   * @throws HieroException if the search fails
   */
  @NonNull
  default Optional<Contract> findById(@NonNull String contractId) throws HieroException {
    Objects.requireNonNull(contractId, "contractId must not be null");
    return findById(ContractId.fromString(contractId));
  }
}
