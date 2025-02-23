package com.openelements.hiero.base.mirrornode;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.data.ContractLog;
import com.openelements.hiero.base.data.Order;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.smartcontract.abi.model.AbiEvent;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

public interface ContractRepository {

    default Page<ContractLog> findLogsByContract(@NonNull String contractId) {
        Objects.requireNonNull(contractId, "contractId must be provided");
        return findLogsByContract(ContractId.fromString(contractId));
    }

    default Page<ContractLog> findLogsByContract(@NonNull String contractId, @NonNull Order order) {
        Objects.requireNonNull(contractId, "contractId must be provided");
        return findLogsByContract(ContractId.fromString(contractId), order);
    }

    default Page<ContractLog> findLogsByContract(@NonNull String contractId, @NonNull AbiEvent abiEvent) {
        Objects.requireNonNull(contractId, "contractId must be provided");
        return findLogsByContract(ContractId.fromString(contractId), abiEvent);
    }

    default Page<ContractLog> findLogsByContract(@NonNull String contractId, @NonNull AbiEvent abiEvent,
            @NonNull Order order) {
        Objects.requireNonNull(contractId, "contractId must be provided");
        return findLogsByContract(ContractId.fromString(contractId), abiEvent, order);
    }

    default Page<ContractLog> findLogsByContract(@NonNull String contractId, int pageLimit) {
        Objects.requireNonNull(contractId, "contractId must be provided");
        return findLogsByContract(ContractId.fromString(contractId), pageLimit);
    }

    default Page<ContractLog> findLogsByContract(@NonNull String contractId, @NonNull Order order, int pageLimit) {
        Objects.requireNonNull(contractId, "contractId must be provided");
        return findLogsByContract(ContractId.fromString(contractId), order, pageLimit);
    }

    default Page<ContractLog> findLogsByContract(@NonNull String contractId, @NonNull AbiEvent abiEvent,
            int pageLimit) {
        Objects.requireNonNull(contractId, "contractId must be provided");
        return findLogsByContract(ContractId.fromString(contractId), abiEvent, pageLimit);
    }

    default Page<ContractLog> findLogsByContract(@NonNull String contractId, @NonNull AbiEvent abiEvent,
            @NonNull Order order, int pageLimit) {
        Objects.requireNonNull(contractId, "contractId must be provided");
        return findLogsByContract(ContractId.fromString(contractId), abiEvent, order, pageLimit);
    }

    default Page<ContractLog> findLogsByContract(@NonNull ContractId contractId) {
        return findLogsByContract(contractId, Order.DESC);
    }

    default Page<ContractLog> findLogsByContract(@NonNull ContractId contractId, @NonNull AbiEvent abiEvent) {
        return findLogsByContract(contractId, abiEvent, Order.DESC);
    }

    default Page<ContractLog> findLogsByContract(@NonNull ContractId contractId, int pageLimit) {
        return findLogsByContract(contractId, Order.DESC, pageLimit);
    }

    default Page<ContractLog> findLogsByContract(@NonNull ContractId contractId, @NonNull AbiEvent abiEvent,
            int pageLimit) {
        return findLogsByContract(contractId, abiEvent, Order.DESC, pageLimit);
    }

    Page<ContractLog> findLogsByContract(@NonNull ContractId contractId, @NonNull Order order);

    Page<ContractLog> findLogsByContract(@NonNull ContractId contractId, @NonNull AbiEvent abiEvent,
            @NonNull Order order);

    Page<ContractLog> findLogsByContract(@NonNull ContractId contractId, @NonNull Order order, int pageLimit);

    Page<ContractLog> findLogsByContract(@NonNull ContractId contractId, @NonNull AbiEvent abiEvent,
            @NonNull Order order, int pageLimit);
}
