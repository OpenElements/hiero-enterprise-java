package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.IFileReader;
import com.openelements.hiero.base.data.ContractParam;
import com.openelements.hiero.base.implementation.SmartContractClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.ContractCreateRequest;
import com.openelements.hiero.base.protocol.data.ContractCreateResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class SmartContractClientImplTest {

    @Mock
    private FileClient fileClient;

    @Mock
    private ProtocolLayerClient protocolLayerClient;

    @Mock
    private IFileReader fileReader;

    @InjectMocks
    private SmartContractClientImpl smartContractClient;

    private ContractId returnedContractId;

    private ContractId contractId;

    private FileId fileId;

    private ContractParam<?> constructorParams;

    private ContractCreateResult resultMock;

    private Path path;

    private byte[] contents;


    @BeforeEach
    public void init() throws IOException {
        fileClient = Mockito.mock(FileClient.class);
        protocolLayerClient = Mockito.mock(ProtocolLayerClient.class);
        fileReader= Mockito.mock(IFileReader.class);
        smartContractClient = Mockito.spy(new SmartContractClientImpl(protocolLayerClient, fileClient, fileReader));
        contractId = ContractId.fromString("0.0.123");
        fileId = FileId.fromString("0.0.123");
        constructorParams = ContractParam.string("paramValue");
        resultMock = Mockito.mock(ContractCreateResult.class);
        path= Mockito.mock(Path.class);
        contents = "contractBytecode".getBytes();


    }

    @Test
    public void testCreateContract_UsingFileId_WithContractParams() throws HieroException {

        when(resultMock.contractId()).thenReturn(contractId);
        when(protocolLayerClient.executeContractCreateTransaction(any(ContractCreateRequest.class))).thenReturn(resultMock);

        returnedContractId = smartContractClient.createContract(fileId, constructorParams);

        assertEquals(contractId, returnedContractId);
        verify(protocolLayerClient).executeContractCreateTransaction(any());

    }

    @Test
    public void testCreateContract_UsingFileId_WithOutContractParams() throws HieroException {

        when(resultMock.contractId()).thenReturn(contractId);
        when(protocolLayerClient.executeContractCreateTransaction(any(ContractCreateRequest.class))).thenReturn(resultMock);

        returnedContractId = smartContractClient.createContract(fileId);

        assertEquals(contractId, returnedContractId);
        verify(protocolLayerClient).executeContractCreateTransaction(any());
    }

    @Test
    public void testCreateContract_UsingFileId_ThrowsException() throws HieroException {

        when(protocolLayerClient.executeContractCreateTransaction(Mockito.any())).thenThrow(new RuntimeException("Failed"));
        HieroException hieroException = assertThrows(HieroException.class, () -> smartContractClient.createContract(fileId, constructorParams));

        assertTrue(hieroException.getMessage().contains("Failed to create contract with fileId " + fileId));
    }

    @Test
    public void testCreateContract_UsingContent_WithContractParams() throws HieroException {

        when(fileClient.createFile(contents)).thenReturn(fileId);
        when(protocolLayerClient.executeContractCreateTransaction(any(ContractCreateRequest.class))).thenReturn(resultMock);
        when(resultMock.contractId()).thenReturn(contractId);

        returnedContractId = smartContractClient.createContract(contents, constructorParams);

        assertEquals(contractId, returnedContractId);
        verify(fileClient).createFile(contents);
        verify(protocolLayerClient).executeContractCreateTransaction(any());
        verify(fileClient).deleteFile(fileId);

    }

    @Test
    public void testCreateContract_UsingContent_WithOutContractParams() throws HieroException {

        when(fileClient.createFile(contents)).thenReturn(fileId);
        when(protocolLayerClient.executeContractCreateTransaction(any(ContractCreateRequest.class))).thenReturn(resultMock);
        when(resultMock.contractId()).thenReturn(contractId);

        returnedContractId = smartContractClient.createContract(contents);

        assertEquals(contractId, returnedContractId);
        verify(fileClient).createFile(contents);
        verify(protocolLayerClient).executeContractCreateTransaction(any());
        verify(fileClient).deleteFile(fileId);
    }

    @Test
    public void testCreateContract_UsingContent_ThrowsException() throws HieroException {
        when(protocolLayerClient.executeContractCreateTransaction(Mockito.any())).thenThrow(new RuntimeException("Failed"));
        HieroException hieroException = assertThrows(HieroException.class, () -> smartContractClient.createContract(contents, constructorParams));

        assertTrue(hieroException.getMessage().contains("Failed to create contract out of byte array"));

    }

    @Test
    public void testCreateContract_UsingPath_WithContractParams() throws HieroException, IOException {

        when(fileReader.readAllBytes(path)).thenReturn(contents);
        doReturn(contractId).when(smartContractClient).createContract(contents, constructorParams);

        returnedContractId = smartContractClient.createContract(path, constructorParams);

        assertNotNull(returnedContractId);
        assertEquals(contractId, returnedContractId);

        verify(fileReader).readAllBytes(path);
        verify(smartContractClient).createContract(contents, constructorParams);


    }

    @Test
    public void testCreateContract_UsingPath_WithOutContractParams() throws HieroException, IOException {
        when(fileReader.readAllBytes(path)).thenReturn(contents);
        doReturn(contractId).when(smartContractClient).createContract(contents);

        returnedContractId = smartContractClient.createContract(path);

        assertNotNull(returnedContractId);
        assertEquals(contractId, returnedContractId);

        verify(fileReader).readAllBytes(path);
        verify(smartContractClient).createContract(contents);

    }

    @Test
    public void testCreateContract_UsingPath_ThrowsException() throws HieroException {
        when(protocolLayerClient.executeContractCreateTransaction(Mockito.any())).thenThrow(new RuntimeException("Failed"));
        HieroException hieroException = assertThrows(HieroException.class, () -> smartContractClient.createContract(path, constructorParams));

        assertTrue(hieroException.getMessage().contains("Failed to create contract from path " + path));

    }

}