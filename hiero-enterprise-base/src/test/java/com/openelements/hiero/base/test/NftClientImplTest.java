package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.implementation.NftClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.TokenCreateRequest;
import com.openelements.hiero.base.protocol.data.TokenCreateResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

public class NftClientImplTest {
    ProtocolLayerClient protocolLayerClient;
    Account operationalAccount;
    NftClientImpl nftClientImpl;

    ArgumentCaptor<TokenCreateRequest> tokenRequestCaptor = ArgumentCaptor.forClass(TokenCreateRequest.class);

    @BeforeEach
    public void setup() {
        protocolLayerClient = Mockito.mock(ProtocolLayerClient.class);
        operationalAccount = Mockito.mock(Account.class);
        nftClientImpl = new NftClientImpl(protocolLayerClient, operationalAccount);
    }

    @Test
    void testCreateNftWithNameAndSymbol() throws HieroException {
        // mock
        final PrivateKey privateKey = PrivateKey.generateECDSA();
        final AccountId accountId = AccountId.fromString("1.2.3");
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final TokenCreateResult tokenCreateResult = mock(TokenCreateResult.class);

        // given
        final String name = "TOKEN";
        final String symbol = "NFT";

        // when
        when(operationalAccount.privateKey()).thenReturn(privateKey);
        when(operationalAccount.accountId()).thenReturn(accountId);
        when(protocolLayerClient.executeTokenCreateTransaction(any(TokenCreateRequest.class)))
                .thenReturn(tokenCreateResult);
        when(tokenCreateResult.tokenId()).thenReturn(tokenId);

        final TokenId result = nftClientImpl.createNftType(name, symbol);

        // then
        // 1st for treasuryKey and 2nd for supplier Key
        verify(operationalAccount, times(2)).privateKey();
        verify(operationalAccount, times(1)).accountId();
        verify(protocolLayerClient, times(1))
                .executeTokenCreateTransaction(tokenRequestCaptor.capture());

        TokenCreateRequest tokenCreateRequest = tokenRequestCaptor.getValue();

        Assertions.assertEquals(privateKey, tokenCreateRequest.treasuryKey());
        Assertions.assertEquals(privateKey, tokenCreateRequest.supplyKey());
        Assertions.assertEquals(accountId, tokenCreateRequest.treasuryAccountId());
        Assertions.assertEquals(name, tokenCreateRequest.name());
        Assertions.assertEquals(symbol, tokenCreateRequest.symbol());
        Assertions.assertEquals(tokenId, result);
    }

    @Test
    void testCreateNftWithNameSymbolAndSupplier() throws HieroException {
        // mock
        final PrivateKey privateKey = PrivateKey.generateECDSA();
        final AccountId accountId = AccountId.fromString("1.2.3");
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final TokenCreateResult tokenCreateResult = Mockito.mock(TokenCreateResult.class);

        // given
        final String name = "TOKEN";
        final String symbol = "NFT";
        final PrivateKey supplierKey = PrivateKey.generateECDSA();

        // when
        when(operationalAccount.privateKey()).thenReturn(privateKey);
        when(operationalAccount.accountId()).thenReturn(accountId);
        when(protocolLayerClient.executeTokenCreateTransaction(any(TokenCreateRequest.class)))
                .thenReturn(tokenCreateResult);
        when(tokenCreateResult.tokenId()).thenReturn(tokenId);

        final TokenId result = nftClientImpl.createNftType(name, symbol, supplierKey);

        //then
        verify(operationalAccount, times(1)).privateKey();
        verify(operationalAccount, times(1)).accountId();
        verify(protocolLayerClient, times(1))
                .executeTokenCreateTransaction(tokenRequestCaptor.capture());

        TokenCreateRequest tokenCreateRequest = tokenRequestCaptor.getValue();

        Assertions.assertEquals(privateKey, tokenCreateRequest.treasuryKey());
        Assertions.assertEquals(supplierKey, tokenCreateRequest.supplyKey());
        Assertions.assertEquals(accountId, tokenCreateRequest.treasuryAccountId());
        Assertions.assertEquals(name, tokenCreateRequest.name());
        Assertions.assertEquals(symbol, tokenCreateRequest.symbol());

        Assertions.assertEquals(tokenId, result);
    }

    @Test
    void testCreateNftWithNameSymbolTreasuryAccountIdAndKey() throws HieroException {
        // mock
        final PrivateKey privateKey = PrivateKey.generateECDSA();
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final TokenCreateResult tokenCreateResult = Mockito.mock(TokenCreateResult.class);

        // given
        final String name = "TOKEN";
        final String symbol = "NFT";
        final PrivateKey treasuryKey = PrivateKey.generateECDSA();
        final AccountId accountId = AccountId.fromString("1.2.3");

        // when
        when(operationalAccount.privateKey()).thenReturn(privateKey);
        when(protocolLayerClient.executeTokenCreateTransaction(any(TokenCreateRequest.class)))
                .thenReturn(tokenCreateResult);
        when(tokenCreateResult.tokenId()).thenReturn(tokenId);

        final TokenId result = nftClientImpl.createNftType(name, symbol, accountId, treasuryKey);

        // then
        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeTokenCreateTransaction(tokenRequestCaptor.capture());

        TokenCreateRequest tokenCreateRequest = tokenRequestCaptor.getValue();

        Assertions.assertEquals(treasuryKey, tokenCreateRequest.treasuryKey());
        Assertions.assertEquals(privateKey, tokenCreateRequest.supplyKey());
        Assertions.assertEquals(accountId, tokenCreateRequest.treasuryAccountId());
        Assertions.assertEquals(name, tokenCreateRequest.name());
        Assertions.assertEquals(symbol, tokenCreateRequest.symbol());

        Assertions.assertEquals(tokenId, result);
    }


    @Test
    void testCreateNftWithAllParam() throws HieroException {
        // mock
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final TokenCreateResult tokenCreateResult = Mockito.mock(TokenCreateResult.class);

        // given
        final String name = "TOKEN";
        final String symbol = "NFT";
        final PrivateKey supplierKey = PrivateKey.generateECDSA();
        final PrivateKey treasuryKey = PrivateKey.generateECDSA();
        final AccountId accountId = AccountId.fromString("1.2.3");

        // when
        when(protocolLayerClient.executeTokenCreateTransaction(any(TokenCreateRequest.class)))
                .thenReturn(tokenCreateResult);
        when(tokenCreateResult.tokenId()).thenReturn(tokenId);

        final TokenId result = nftClientImpl.createNftType(name, symbol, accountId, treasuryKey, supplierKey);

        // then
        verify(protocolLayerClient, times(1))
                .executeTokenCreateTransaction(tokenRequestCaptor.capture());

        TokenCreateRequest tokenCreateRequest = tokenRequestCaptor.getValue();

        Assertions.assertEquals(treasuryKey, tokenCreateRequest.treasuryKey());
        Assertions.assertEquals(supplierKey, tokenCreateRequest.supplyKey());
        Assertions.assertEquals(accountId, tokenCreateRequest.treasuryAccountId());
        Assertions.assertEquals(name, tokenCreateRequest.name());
        Assertions.assertEquals(symbol, tokenCreateRequest.symbol());

        Assertions.assertEquals(tokenId, result);
    }

    @Test
    void testCreateNftForNullParam() {
        Assertions.assertThrows(
                NullPointerException.class, () -> nftClientImpl.createNftType((String) null, null)
        );
        Assertions.assertThrows(
                NullPointerException.class, () -> nftClientImpl.createNftType(null, null, (PrivateKey) null)
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> nftClientImpl.createNftType(null, null, (AccountId) null, (PrivateKey) null)
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> nftClientImpl.createNftType(null, null, null, null, (PrivateKey) null)
        );
    }
}
