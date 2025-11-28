package com.openelements.hiero.spring.implementation;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.verification.ContractVerificationClient;
import com.openelements.hiero.base.verification.ContractVerificationState;
import java.util.Map;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Utility class to help with contract verification. */
public class ContractVerificationUtility {

  private static final Logger log = LoggerFactory.getLogger(ContractVerificationUtility.class);

  private final ContractVerificationClient verificationClient;

  public ContractVerificationUtility(@NonNull final ContractVerificationClient verificationClient) {
    this.verificationClient =
        Objects.requireNonNull(verificationClient, "verificationClient must not be null");
  }

  /**
   * Check verification of a contract and all its files. If the contract has not been verified yet,
   * it will be verified.
   *
   * @param contractId contract to verify
   * @param contractName contract name
   * @param contractSource contract source code
   * @param contractMetadata contract metadata
   * @throws HieroException if an error happens in communication with a Hiero network
   * @throws IllegalStateException if the contract is not fully verified
   */
  void doFullVerification(
      ContractId contractId, String contractName, String contractSource, String contractMetadata)
      throws HieroException {
    doFullVerification(
        contractId,
        contractName,
        Map.of(contractName + ".sol", contractSource, "metadata.json", contractMetadata));
  }

  /**
   * Check verification of a contract and all its files. If the contract has not been verified yet,
   * it will be verified.
   *
   * @param contractId contract to verify
   * @param contractName contract name
   * @param files map of file names to file contents
   * @throws HieroException if an error happens in communication with a Hiero network
   * @throws IllegalStateException if the contract is not fully verified
   */
  void doFullVerification(ContractId contractId, String contractName, Map<String, String> files)
      throws HieroException {
    final ContractVerificationState state = verificationClient.checkVerification(contractId);
    if (state == ContractVerificationState.FULL) {
      log.debug("Contract {} is already fully verified", contractId);
    } else {
      log.debug("Contract {} is not fully verified, will start verification", contractId);
      final ContractVerificationState newState =
          verificationClient.verify(contractId, contractName, files);
      if (newState == ContractVerificationState.FULL) {
        log.debug("Contract {} is now fully verified", contractId);
      } else {
        throw new IllegalStateException(
            "Contract " + contractId + " is not fully verified, state is " + newState);
      }
    }
    for (Map.Entry<String, String> entry : files.entrySet()) {
      if (!verificationClient.checkVerification(contractId, entry.getKey(), entry.getValue())) {
        throw new IllegalStateException("file " + entry.getKey() + " is invalid");
      }
    }
  }
}
