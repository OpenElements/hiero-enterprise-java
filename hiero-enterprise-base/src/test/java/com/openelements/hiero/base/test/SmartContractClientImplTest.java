package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.FileClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.ContractCallResult;
import com.openelements.hiero.base.data.ContractParam;
import com.openelements.hiero.base.implementation.SmartContractClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.ContractCallRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SmartContractClientImplTest {

    private ProtocolLayerClient mockProtocolLayerClient;
    private FileClient mockFileClient;
    private SmartContractClientImpl smartContractClient;

    @BeforeEach
    public void setUp() {
        mockProtocolLayerClient = mock(ProtocolLayerClient.class);
        mockFileClient = mock(FileClient.class);
        smartContractClient = new SmartContractClientImpl(mockProtocolLayerClient, mockFileClient);
    }

    @Test
    void CallContractFunction_success() throws Exception {

        ContractId contractId = ContractId.fromString("0.0.1234");
        String functionName = "getValue";

        ContractFunctionResult mockFunctionResult = mock(ContractFunctionResult.class);
        com.openelements.hiero.base.protocol.data.ContractCallResult mockProtocolResult =
                mock(com.openelements.hiero.base.protocol.data.ContractCallResult.class);
        when(mockProtocolResult.contractFunctionResult()).thenReturn(mockFunctionResult);

        when(mockProtocolLayerClient.executeContractCallTransaction(any(ContractCallRequest.class)))
                .thenReturn(mockProtocolResult);

        com.openelements.hiero.base.data.ContractCallResult result =
                smartContractClient.callContractFunction(contractId, functionName);

        assertNotNull(result);

    }

    @Test
    void CallContractFunction_exceptionThrown() throws Exception {
        ContractId contractId = ContractId.fromString("0.0.1234");

        when(mockProtocolLayerClient.executeContractCallTransaction(any(ContractCallRequest.class)))
                .thenThrow(new RuntimeException("Contract call failed"));

        assertThrows(HieroException.class, () ->
                smartContractClient.callContractFunction(contractId, "getValue"));
    }

    @Test
    void CallContractFunction_failure() throws Exception {
        ContractId contractId = ContractId.fromString("0.0.1234");

        when(mockProtocolLayerClient.executeContractCallTransaction(any(ContractCallRequest.class)))
                .thenThrow(new RuntimeException("Simulated protocol failure"));

        HieroException thrown = assertThrows(HieroException.class, () ->
                smartContractClient.callContractFunction(contractId, "getValue"));

        assertTrue(thrown.getMessage().contains("Failed to call function"));
    }

    @Test
    void callContractFunction_successWithNoParams() throws Exception {
        // Arrange
        ContractId contractId = ContractId.fromString("0.0.1234");
        String functionName = "getValue";

        // Mock the expected result
        ContractFunctionResult mockFunctionResult = mock(ContractFunctionResult.class);

        // Mock the protocol layer to return our mock result
        when(mockProtocolLayerClient.executeContractCallTransaction(any(ContractCallRequest.class)))
                .thenReturn(mockFunctionResult);

        // Act
        ContractCallResult result = smartContractClient.callContractFunction(contractId, functionName);

        // Assert
        assertNotNull(result);

        // Verify the request was formed correctly
        verify(mockProtocolLayerClient).executeContractCallTransaction(argThat(request -> {
            assertEquals(contractId, request.contractId(), "Contract ID mismatch");
            assertEquals(functionName, request.functionName(), "Function name mismatch");

            // Verify no parameters were passed
            if (request.params() != null) {  // Handle both null and empty cases
                assertTrue(request.params().isEmpty(), "Params should be empty");
            }
            return true;
        }));
    }


    @Test
    void callContractFunction_propagatesProtocolExceptions() throws HieroException {
        ContractId contractId = ContractId.fromString("0.0.1234");
        String functionName = "getValue";

        when(mockProtocolLayerClient.executeContractCallTransaction(any(ContractCallRequest.class)))
                .thenThrow(new RuntimeException("Network error"));

        HieroException exception = assertThrows(HieroException.class, () ->
                smartContractClient.callContractFunction(contractId, functionName));

        assertTrue(exception.getMessage().contains("Failed to call function '" + functionName + "'"),
                "Exception message should indicate function call failure");
        assertNotNull(exception.getCause(), "Exception should have a cause");
        assertEquals("Network error", exception.getCause().getMessage(),
                "Should preserve original error message");
    }


    @ParameterizedTest
    @NullSource
    void callContractFunction_wrapsNullPointerExceptions(ContractId nullId) {
        HieroException exception = assertThrows(HieroException.class, () ->
                smartContractClient.callContractFunction(nullId, "test"));

        assertEquals(NullPointerException.class, exception.getCause().getClass());
    }

    @Test
    void callContractFunction_throwsOnNullFunctionName() {
        ContractId contractId = ContractId.fromString("0.0.1234");

        HieroException exception = assertThrows(HieroException.class, () ->
                smartContractClient.callContractFunction(contractId, null));

        assertNotNull(exception.getCause());
        assertEquals(NullPointerException.class, exception.getCause().getClass());
        assertTrue(exception.getMessage().contains("Failed to call function 'null'"));
    }
}

