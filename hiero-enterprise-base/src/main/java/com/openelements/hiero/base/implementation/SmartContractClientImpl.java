package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.SmartContractClient;
import com.openelements.hiero.base.data.ContractCallResult;
import com.openelements.hiero.base.data.ContractParam;
import com.openelements.hiero.base.protocol.data.ContractCallRequest;
import com.openelements.hiero.base.protocol.data.ContractCreateRequest;
import com.openelements.hiero.base.protocol.data.ContractCreateResult;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmartContractClientImpl implements SmartContractClient {

    private static final Logger log = LoggerFactory.getLogger(SmartContractClientImpl.class);

    private final ProtocolLayerClient protocolLayerClient;

    private final FileClient fileClient;

    public SmartContractClientImpl(@NonNull final ProtocolLayerClient protocolLayerClient, FileClient fileClient) {
        this.protocolLayerClient = Objects.requireNonNull(protocolLayerClient, "protocolLevelClient must not be null");
        this.fileClient = Objects.requireNonNull(fileClient, "fileClient must not be null");
    }

    @NonNull
    @Override
    public ContractId createContract(@NonNull final FileId fileId,
            @Nullable final ContractParam<?>... constructorParams)
            throws HieroException {
        try {
            final ContractCreateRequest request;
            if (constructorParams == null) {
                request = ContractCreateRequest.of(fileId);
            } else {
                request = ContractCreateRequest.of(fileId, Arrays.asList(constructorParams));
            }
            final ContractCreateResult result = protocolLayerClient.executeContractCreateTransaction(request);
            return result.contractId();
        } catch (Exception e) {
            throw new HieroException("Failed to create contract with fileId " + fileId, e);
        }
    }

    @NonNull
    @Override
    public ContractId createContract(@NonNull final byte[] contents,
            @Nullable final ContractParam<?>... constructorParams)
            throws HieroException {
        try {
            final FileId fileId = fileClient.createFile(contents);
            final ContractId contract = createContract(fileId, constructorParams);
            fileClient.deleteFile(fileId);
            return contract;
        } catch (Exception e) {
            throw new HieroException("Failed to create contract out of byte array", e);
        }
    }

    @NonNull
    @Override
    public ContractId createContract(@NonNull final Path pathToBin,
            @Nullable final ContractParam<?>... constructorParams)
            throws HieroException {
        try {
            final byte[] bytes = Files.readAllBytes(pathToBin);
            return createContract(bytes, constructorParams);
        } catch (Exception e) {
            throw new HieroException("Failed to create contract from path " + pathToBin, e);
        }
    }

    @NonNull
    @Override
    public ContractCallResult callContractFunction(@NonNull final ContractId contractId,
            @NonNull final String functionName,
            @Nullable ContractParam<?>... params) throws HieroException {
        try {
            final ContractCallRequest request = ContractCallRequest.of(contractId, functionName, params);
            final ContractFunctionResult result = protocolLayerClient.executeContractCallTransaction(request)
                    .contractFunctionResult();
            return new ContractCallResultImpl(result);
        } catch (Exception e) {
            throw new HieroException(
                    "Failed to call function '" + functionName + "' on contract with id " + contractId, e);
        }
    }
}
