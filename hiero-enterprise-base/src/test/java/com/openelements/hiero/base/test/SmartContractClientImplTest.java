package com.openelements.hiero.base.test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.ContractParam;
import com.openelements.hiero.base.implementation.SmartContractClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.ContractCallRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;


class SmartContractClientImplTest {

    private ProtocolLayerClient protocolLayerClient;
    private FileClient fileClient;
    private SmartContractClientImpl smartContractClient;

    @BeforeEach
    void setUp() {
        protocolLayerClient = mock(ProtocolLayerClient.class);
        fileClient = mock(FileClient.class);
        smartContractClient = new SmartContractClientImpl(protocolLayerClient, fileClient);
    }
    @Test
    void callContractFunction_withNullFunctionName_throwsHieroExceptionWithNpeCause() {
        ContractId contractId = ContractId.fromString("0.0.123");

        HieroException exception = assertThrows(HieroException.class, () -> {
            smartContractClient.callContractFunction(contractId, null);
        });

        assertNotNull(exception.getCause());
        assertEquals(NullPointerException.class, exception.getCause().getClass());
        assertEquals("functionName is required", exception.getCause().getMessage());
        assertTrue(exception.getMessage().contains("Failed to call function 'null'"));
    }

    @Test
    void callContractFunction_withNullContractId_throwsHieroExceptionWithNpeCause() {
        HieroException exception = assertThrows(HieroException.class, () -> {
            smartContractClient.callContractFunction((ContractId) null, "testFunction");
        });

        assertNotNull(exception.getCause());
        assertEquals(NullPointerException.class, exception.getCause().getClass());
        assertTrue(exception.getMessage().contains("Failed to call function 'testFunction'"));
    }

    @Test
    void callContractFunction_whenProtocolLayerFails_throwsHieroException() throws Exception {
        ContractId contractId = ContractId.fromString("0.0.123");
        String functionName = "testFunction";

        when(protocolLayerClient.executeContractCallTransaction(any(ContractCallRequest.class)))
                .thenThrow(new RuntimeException("Network error"));

        HieroException exception = assertThrows(HieroException.class, () -> {
            smartContractClient.callContractFunction(contractId, functionName);
        });

        assertEquals("Failed to call function 'testFunction' on contract with id 0.0.123",
                exception.getMessage());
        assertNotNull(exception.getCause());
    }

    @Test
    void callContractFunction_withEmptyFunctionName_throwsHieroException() {
        ContractId contractId = ContractId.fromString("0.0.123");

        HieroException exception = assertThrows(HieroException.class, () -> {
            smartContractClient.callContractFunction(contractId, "");
        });

        assertTrue(exception.getCause() instanceof IllegalArgumentException);
        assertEquals("functionName must not be blank or contain spaces",
                exception.getCause().getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void callContractFunction_shouldValidateFunctionName(String invalidFunctionName) {
        assertThrows(HieroException.class, () ->
                smartContractClient.callContractFunction(
                        ContractId.fromString("0.0.123"),
                        invalidFunctionName
                ));
    }
    @Test
    void callContractFunction_shouldThrowWhenContractIdIsNull() {
        String functionName = "transfer";
        ContractParam<String> recipient = ContractParam.address("0.0.456");

        HieroException exception = assertThrows(HieroException.class, () ->
                smartContractClient.callContractFunction(
                        (ContractId) null, functionName, recipient
                ));

        assertTrue(exception.getMessage().contains("Failed to call function"));
        assertTrue(exception.getCause() instanceof NullPointerException);
        assertTrue(exception.getCause().getMessage().contains("contractId is required"));
    }

    @Test
    void callContractFunction_shouldThrowWhenProtocolLayerClientReturnsNull() throws Exception {

        ContractId contractId = ContractId.fromString("0.0.123");
        String functionName = "transfer";
        ContractParam<String> recipient = ContractParam.address("0.0.456");

        when(protocolLayerClient.executeContractCallTransaction(any())).thenReturn(null);

        HieroException exception = assertThrows(HieroException.class, () ->
                smartContractClient.callContractFunction(contractId, functionName, recipient));

        assertTrue(exception.getMessage().contains("Failed to call function"));
    }

    @Test
    void createContract_shouldHandleFileReadErrors() throws Exception {
        Path invalidPath = Path.of("/invalid/path");
        HieroException exception = assertThrows(HieroException.class, () ->
                smartContractClient.createContract(invalidPath));
        assertEquals("Failed to create contract from path " + invalidPath,
                exception.getMessage());
        assertTrue(exception.getCause() instanceof java.nio.file.NoSuchFileException);
    }

    @Test
    void callContractFunction_withNullContractId_throwsHieroException() {
        HieroException exception = assertThrows(HieroException.class, () -> {
            smartContractClient.callContractFunction((ContractId) null, "testFunction");
        });

        assertNotNull(exception.getCause());
        assertEquals(NullPointerException.class, exception.getCause().getClass());
        assertTrue(exception.getMessage().contains("Failed to call function 'testFunction'"));
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", "  "})
    void callContractFunction_withInvalidFunctionName_throwsHieroException(String invalidFunctionName) {
        ContractId contractId = ContractId.fromString("0.0.123");

        HieroException exception = assertThrows(HieroException.class, () -> {
            smartContractClient.callContractFunction(contractId, invalidFunctionName);
        });

        assertNotNull(exception.getCause());
        if (invalidFunctionName == null) {
            assertEquals(NullPointerException.class, exception.getCause().getClass());
        } else {
            assertEquals(IllegalArgumentException.class, exception.getCause().getClass());
        }
    }
}
