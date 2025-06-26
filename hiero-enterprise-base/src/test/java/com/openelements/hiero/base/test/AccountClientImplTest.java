package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;

import com.openelements.hiero.base.implementation.AccountClientImpl;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.protocol.data.AccountBalanceRequest;
import com.openelements.hiero.base.protocol.data.AccountBalanceResponse;
import com.openelements.hiero.base.protocol.data.AccountCreateRequest;
import com.openelements.hiero.base.protocol.data.AccountCreateResult;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

public class AccountClientImplTest {

    private ProtocolLayerClient mockProtocolLayerClient;
    private AccountClientImpl accountClientImpl;

    @BeforeEach
    public void setUp() {
        mockProtocolLayerClient = mock(ProtocolLayerClient.class);
        accountClientImpl = new AccountClientImpl(mockProtocolLayerClient);
    }

    @Test
    public void testGetAccountBalance_ValidPositiveBalance() throws HieroException {
        AccountId accountId = AccountId.fromString("0.0.12345");
        Hbar expectedBalance = new Hbar(10);

        AccountBalanceResponse mockResponse = mock(AccountBalanceResponse.class);
        when(mockResponse.hbars()).thenReturn(expectedBalance);

        when(mockProtocolLayerClient.executeAccountBalanceQuery(
                ArgumentMatchers.any(AccountBalanceRequest.class)
        )).thenReturn(mockResponse);

        Hbar balance = accountClientImpl.getAccountBalance(accountId);

        assertEquals(expectedBalance, balance);
    }

    @Test
    public void testGetAccountBalance_ZeroBalance() throws HieroException {
        AccountId accountId = AccountId.fromString("0.0.67890");
        Hbar expectedBalance = new Hbar(0);

        AccountBalanceResponse mockResponse = mock(AccountBalanceResponse.class);
        when(mockResponse.hbars()).thenReturn(expectedBalance);

        when(mockProtocolLayerClient.executeAccountBalanceQuery(
                ArgumentMatchers.any(AccountBalanceRequest.class)
        )).thenReturn(mockResponse);

        Hbar balance = accountClientImpl.getAccountBalance(accountId);

        assertEquals(expectedBalance, balance);
    }

    @Test
    public void testGetAccountBalance_InvalidAccount_ThrowsException() throws HieroException {
        AccountId invalidAccountId = AccountId.fromString("0.0.9999999");

        when(mockProtocolLayerClient.executeAccountBalanceQuery(
                ArgumentMatchers.any(AccountBalanceRequest.class)
        )).thenThrow(new HieroException("Invalid account"));

        assertThrows(HieroException.class, () -> {
            accountClientImpl.getAccountBalance(invalidAccountId);
        });
    }

    @Test
    public void testGetAccountBalance_NullThrowsException() {
        assertThrows(NullPointerException.class, () -> {
            accountClientImpl.getAccountBalance((AccountId) null);
        });
    }

    @Test
    public void testGetAccountBalance_ProtocolLayerClientFails() throws HieroException {
        AccountId accountId = AccountId.fromString("0.0.12345");

        when(mockProtocolLayerClient.executeAccountBalanceQuery(
                ArgumentMatchers.any(AccountBalanceRequest.class)
        )).thenThrow(new RuntimeException("Protocol Layer Failure"));

        assertThrows(RuntimeException.class, () -> {
            accountClientImpl.getAccountBalance(accountId);
        });
    }

    @Test
    void testCreateAccount_successful() throws HieroException {
        Hbar initialBalance = Hbar.from(100);

        AccountCreateResult mockResult = mock(AccountCreateResult.class);
        Account mockAccount = mock(Account.class);

        when(mockAccount.accountId()).thenReturn(AccountId.fromString("0.0.12345"));
        when(mockResult.newAccount()).thenReturn(mockAccount);
        when(mockProtocolLayerClient.executeAccountCreateTransaction(any(AccountCreateRequest.class)))
                .thenReturn(mockResult);

        Account result = accountClientImpl.createAccount(initialBalance);

        assertNotNull(result);
        assertEquals(AccountId.fromString("0.0.12345"), result.accountId());
        verify(mockProtocolLayerClient, times(1))
                .executeAccountCreateTransaction(any(AccountCreateRequest.class));
    }

    @Test
    void testCreateAccount_invalidInitialBalance_null() {
        Hbar initialBalance = null;

        assertThrows(NullPointerException.class, () -> accountClientImpl.createAccount(initialBalance));
    }

    @Test
    void testCreateAccount_invalidInitialBalance_negative() {
        Hbar initialBalance = Hbar.from(-100);
        HieroException exception = assertThrows(HieroException.class,
                () -> accountClientImpl.createAccount(initialBalance));

        assertTrue(exception.getMessage().contains("Invalid initial balance"));
    }


    @Test
    void testCreateAccount_hieroExceptionThrown() throws HieroException {
        Hbar initialBalance = Hbar.from(100);

        when(mockProtocolLayerClient.executeAccountCreateTransaction(any(AccountCreateRequest.class)))
                .thenThrow(new HieroException("Transaction failed"));

        Exception exception = assertThrows(HieroException.class, () -> accountClientImpl.createAccount(initialBalance));
        assertEquals("Transaction failed", exception.getMessage());
    }


    @Test
    void DeleteAccount_nullAccount_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> accountClientImpl.deleteAccount(null));
    }


    @Test
    void DeleteAccount_successful() throws HieroException {
        Account mockAccount = mock(Account.class);

        accountClientImpl.deleteAccount(mockAccount);

        verify(mockProtocolLayerClient, times(1))
                .executeAccountDeleteTransaction(any());
    }


    @Test
    void DeleteAccount_withTransfer_nullFromAccount_throwsException() {
        Account toAccount = mock(Account.class);

        assertThrows(NullPointerException.class, () -> {
            accountClientImpl.deleteAccount(null, toAccount);
        });
    }

    @Test
    void deleteAccount_withNullToAccount_shouldNotThrow() {
        AccountId accountId = AccountId.fromString("0.0.123");
        PrivateKey privateKey = PrivateKey.generate();
        Account fromAccount = new Account(accountId, privateKey.getPublicKey(), privateKey);

        assertDoesNotThrow(() -> accountClientImpl.deleteAccount(fromAccount, null),
                "deleteAccount should not throw when toAccount is null (should fallback to operator)");
    }

    @Test
    void deleteAccount_withTransfer_throwsHieroException() throws HieroException {
        PrivateKey fromPrivateKey = PrivateKey.generate();
        PublicKey fromPublicKey = fromPrivateKey.getPublicKey();
        AccountId fromAccountId = AccountId.fromString("0.0.1234");

        PrivateKey toPrivateKey = PrivateKey.generate();
        PublicKey toPublicKey = toPrivateKey.getPublicKey();
        AccountId toAccountId = AccountId.fromString("0.0.5678");

        Account fromAccount = new Account(fromAccountId, fromPublicKey, fromPrivateKey);
        Account toAccount = new Account(toAccountId, toPublicKey, toPrivateKey);

        doThrow(new HieroException("Transfer deletion failed"))
                .when(mockProtocolLayerClient)
                .executeAccountDeleteTransaction(any());

        HieroException exception = assertThrows(HieroException.class, () ->
                accountClientImpl.deleteAccount(fromAccount, toAccount)
        );

        assertEquals("Transfer deletion failed", exception.getMessage());

        verify(mockProtocolLayerClient, times(1)).executeAccountDeleteTransaction(any());
    }
}