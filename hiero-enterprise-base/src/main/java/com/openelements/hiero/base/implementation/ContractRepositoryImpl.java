package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Contract;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.mirrornode.ContractRepository;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/**
 * Implementation of ContractRepository that uses MirrorNodeClient to query contract data.
 */
public class ContractRepositoryImpl implements ContractRepository {

    private final MirrorNodeClient mirrorNodeClient;

    /**
     * Creates a new ContractRepositoryImpl with the given MirrorNodeClient.
     *
     * @param mirrorNodeClient the mirror node client to use for queries
     */
    public ContractRepositoryImpl(@NonNull final MirrorNodeClient mirrorNodeClient) {
        this.mirrorNodeClient = Objects.requireNonNull(mirrorNodeClient, "mirrorNodeClient must not be null");
    }

    @NonNull
    @Override
    public Page<Contract> findAll() throws HieroException {
        return mirrorNodeClient.queryContracts();
    }

    @NonNull
    @Override
    public Optional<Contract> findById(@NonNull final ContractId contractId) throws HieroException {
        return mirrorNodeClient.queryContractById(contractId);
    }

    @NonNull
    @Override
    public Page<Contract> findByEvmAddress(@NonNull final String evmAddress) throws HieroException {
        return mirrorNodeClient.queryContractsByEvmAddress(evmAddress);
    }

    @NonNull
    @Override
    public Page<Contract> findByFileId(@NonNull final String fileId) throws HieroException {
        return mirrorNodeClient.queryContractsByFileId(fileId);
    }

    @NonNull
    @Override
    public Page<Contract> findByProxyAccountId(@NonNull final String proxyAccountId) throws HieroException {
        return mirrorNodeClient.queryContractsByProxyAccountId(proxyAccountId);
    }
}
