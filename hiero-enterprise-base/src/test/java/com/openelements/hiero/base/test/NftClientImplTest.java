package com.openelements.hiero.base.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.implementation.NftClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.TokenAssociateRequest;
import com.openelements.hiero.base.protocol.data.TokenAssociateResult;
import com.openelements.hiero.base.protocol.data.TokenBurnRequest;
import com.openelements.hiero.base.protocol.data.TokenBurnResult;
import com.openelements.hiero.base.protocol.data.TokenCreateRequest;
import com.openelements.hiero.base.protocol.data.TokenCreateResult;
import com.openelements.hiero.base.protocol.data.TokenTransferRequest;
import com.openelements.hiero.base.protocol.data.TokenTransferResult;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class NftClientImplTest {
    ProtocolLayerClient protocolLayerClient;
    Account operationalAccount;
    NftClientImpl nftClientImpl;

    ArgumentCaptor<TokenCreateRequest> tokenRequestCaptor = ArgumentCaptor.forClass(TokenCreateRequest.class);
    ArgumentCaptor<TokenTransferRequest> tokenTransferCaptor = ArgumentCaptor.forClass(TokenTransferRequest.class);
    ArgumentCaptor<TokenBurnRequest> tokenBurnCaptor = ArgumentCaptor.forClass(TokenBurnRequest.class);
    ArgumentCaptor<TokenAssociateRequest> tokenAssociateCaptor = ArgumentCaptor.forClass(TokenAssociateRequest.class);

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

    @Test
    void testTransferNft() throws HieroException {
        // mock
        final TokenTransferResult tokenTransferResult = Mockito.mock(TokenTransferResult.class);

        // given
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final long serialNumber = 1L;
        final AccountId fromAccount = AccountId.fromString("1.2.3");
        final AccountId toAccount = AccountId.fromString("4.5.6");
        final PrivateKey fromAccountKey = PrivateKey.generateECDSA();

        // when
        when(protocolLayerClient.executeTransferTransaction(any(TokenTransferRequest.class)))
                .thenReturn(tokenTransferResult);
        nftClientImpl.transferNft(tokenId, serialNumber, fromAccount, fromAccountKey, toAccount);

        // then
        verify(protocolLayerClient, times(1))
                .executeTransferTransaction(tokenTransferCaptor.capture());

        final TokenTransferRequest request = tokenTransferCaptor.getValue();
        Assertions.assertEquals(tokenId, request.tokenId());
        Assertions.assertEquals(List.of(serialNumber), request.serials());
        Assertions.assertEquals(fromAccount, request.sender());
        Assertions.assertEquals(toAccount, request.receiver());
        Assertions.assertEquals(fromAccountKey, request.senderKey());
    }

    @Test
    void testTransferNfts() throws HieroException {
        // mock
        final TokenTransferResult tokenTransferResult = Mockito.mock(TokenTransferResult.class);

        // given
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final List<Long> serialNumbers = List.of(1L, 2L);
        final AccountId fromAccount = AccountId.fromString("1.2.3");
        final AccountId toAccount = AccountId.fromString("4.5.6");
        final PrivateKey fromAccountKey = PrivateKey.generateECDSA();

        // when
        when(protocolLayerClient.executeTransferTransaction(any(TokenTransferRequest.class)))
                .thenReturn(tokenTransferResult);
        nftClientImpl.transferNfts(tokenId, serialNumbers, fromAccount, fromAccountKey, toAccount);

        // then
        verify(protocolLayerClient, times(1))
                .executeTransferTransaction(tokenTransferCaptor.capture());

        final TokenTransferRequest request = tokenTransferCaptor.getValue();
        Assertions.assertEquals(tokenId, request.tokenId());
        Assertions.assertEquals(serialNumbers, request.serials());
        Assertions.assertEquals(fromAccount, request.sender());
        Assertions.assertEquals(toAccount, request.receiver());
        Assertions.assertEquals(fromAccountKey, request.senderKey());
    }

    @Test
    void testTransferNftThrowsExceptionForInvalidTokenId() throws HieroException {
        //given
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final AccountId fromAccount = AccountId.fromString("1.2.3");
        final AccountId toAccount = AccountId.fromString("4.5.6");
        final PrivateKey fromAccountKey = PrivateKey.generateECDSA();
        final long serial = 1L;

        //when
        when(protocolLayerClient.executeTransferTransaction(any(TokenTransferRequest.class)))
                .thenThrow(new HieroException("Failed to execute transaction of type TokenTransferTransaction"));

        //then
        Assertions.assertThrows(HieroException.class, () -> nftClientImpl.transferNft(tokenId, serial,
                fromAccount, fromAccountKey, toAccount));
    }

    @Test
    void testTransferNftThrowsExceptionForInvalidSerial() {
        final String e1Message = "serial must be positive";
        final String e2Message = "either amount or serial must be provided";

        //given
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final AccountId fromAccount = AccountId.fromString("1.2.3");
        final AccountId toAccount = AccountId.fromString("4.5.6");
        final PrivateKey fromAccountKey = PrivateKey.generateECDSA();
        final long serial = -1L;

        IllegalArgumentException e1 = Assertions.assertThrows(IllegalArgumentException.class,
                () -> nftClientImpl.transferNft(tokenId, serial, fromAccount, fromAccountKey, toAccount));
        Assertions.assertEquals(e1Message, e1.getMessage());

        IllegalArgumentException e2 = Assertions.assertThrows(IllegalArgumentException.class,
                () -> nftClientImpl.transferNfts(tokenId, List.of(), fromAccount, fromAccountKey, toAccount));
        Assertions.assertEquals(e2Message, e2.getMessage());
    }

    @Test
    void testTransferNftNullParams() {
        Assertions.assertThrows(NullPointerException.class,
                () -> nftClientImpl.transferNft(null, 1L, null,
                        null, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> nftClientImpl.transferNfts(null, null, null,
                        null, null));
    }

    @Test
    void testBurnNft() throws HieroException {
        final PrivateKey privateKey = PrivateKey.generateECDSA();
        final TokenBurnResult tokenBurnRequest = Mockito.mock(TokenBurnResult.class);

        final TokenId tokenId = TokenId.fromString("1.2.3");
        final long serials  = 1L;

        when(operationalAccount.privateKey()).thenReturn(privateKey);
        when(protocolLayerClient.executeBurnTokenTransaction(any(TokenBurnRequest.class)))
                .thenReturn(tokenBurnRequest);

        nftClientImpl.burnNft(tokenId, serials);

        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeBurnTokenTransaction(tokenBurnCaptor.capture());

        final TokenBurnRequest request = tokenBurnCaptor.getValue();
        Assertions.assertEquals(tokenId, request.tokenId());
        Assertions.assertEquals(Set.of(serials), request.serials());
        Assertions.assertEquals(privateKey, request.supplyKey());
    }

    @Test
    void testBurnNftWithSupplyKey() throws HieroException {
        final TokenBurnResult tokenBurnRequest = Mockito.mock(TokenBurnResult.class);

        final TokenId tokenId = TokenId.fromString("1.2.3");
        final long serials  = 1L;
        final PrivateKey privateKey = PrivateKey.generateECDSA();

        when(protocolLayerClient.executeBurnTokenTransaction(any(TokenBurnRequest.class)))
                .thenReturn(tokenBurnRequest);

        nftClientImpl.burnNft(tokenId, serials, privateKey);

        verify(protocolLayerClient, times(1))
                .executeBurnTokenTransaction(tokenBurnCaptor.capture());

        final TokenBurnRequest request = tokenBurnCaptor.getValue();
        Assertions.assertEquals(tokenId, request.tokenId());
        Assertions.assertEquals(Set.of(serials), request.serials());
        Assertions.assertEquals(privateKey, request.supplyKey());
    }

    @Test
    void testBurnNfts() throws HieroException {
        final PrivateKey privateKey = PrivateKey.generateECDSA();
        final TokenBurnResult tokenBurnRequest = Mockito.mock(TokenBurnResult.class);

        final TokenId tokenId = TokenId.fromString("1.2.3");
        final Set<Long> serials  = Set.of(1L);

        when(operationalAccount.privateKey()).thenReturn(privateKey);
        when(protocolLayerClient.executeBurnTokenTransaction(any(TokenBurnRequest.class)))
                .thenReturn(tokenBurnRequest);

        nftClientImpl.burnNfts(tokenId, serials);

        verify(operationalAccount, times(1)).privateKey();
        verify(protocolLayerClient, times(1))
                .executeBurnTokenTransaction(tokenBurnCaptor.capture());

        final TokenBurnRequest request = tokenBurnCaptor.getValue();
        Assertions.assertEquals(tokenId, request.tokenId());
        Assertions.assertEquals(serials, request.serials());
        Assertions.assertEquals(privateKey, request.supplyKey());
    }

    @Test
    void testBurnNftsWithSupplyKey() throws HieroException {
        final TokenBurnResult tokenBurnRequest = Mockito.mock(TokenBurnResult.class);

        final TokenId tokenId = TokenId.fromString("1.2.3");
        final Set<Long> serials  = Set.of(1L);
        final PrivateKey privateKey = PrivateKey.generateECDSA();

        when(protocolLayerClient.executeBurnTokenTransaction(any(TokenBurnRequest.class)))
                .thenReturn(tokenBurnRequest);

        nftClientImpl.burnNfts(tokenId, serials, privateKey);

        verify(protocolLayerClient, times(1))
                .executeBurnTokenTransaction(tokenBurnCaptor.capture());

        final TokenBurnRequest request = tokenBurnCaptor.getValue();
        Assertions.assertEquals(tokenId, request.tokenId());
        Assertions.assertEquals(serials, request.serials());
        Assertions.assertEquals(privateKey, request.supplyKey());
    }

    @Test
    void testBurnNftThrowsExceptionForInvalidTokenId() throws HieroException {
        final PrivateKey privateKey = PrivateKey.generateECDSA();
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final long serials  = 1L;

        when(operationalAccount.privateKey()).thenReturn(privateKey);
        when(protocolLayerClient.executeBurnTokenTransaction(any(TokenBurnRequest.class)))
                .thenThrow(new HieroException("Failed to execute transaction of type TokenBurnTransaction"));

        Assertions.assertThrows(HieroException.class , () -> nftClientImpl.burnNft(tokenId, serials));
        Assertions.assertThrows(HieroException.class , () -> nftClientImpl.burnNft(tokenId, serials, privateKey));
        Assertions.assertThrows(HieroException.class , () -> nftClientImpl.burnNfts(tokenId, Set.of(serials)));
        Assertions.assertThrows(HieroException.class , () -> nftClientImpl.burnNfts(tokenId, Set.of(serials), privateKey));
    }

    @Test
    void testBurnNftNullParam() {
        Assertions.assertThrows(NullPointerException.class,
                () -> nftClientImpl.burnNft(null, 0));

        Assertions.assertThrows(NullPointerException.class,
                () -> nftClientImpl.burnNft(null, 0, null));

        Assertions.assertThrows(NullPointerException.class,
                () -> nftClientImpl.burnNfts(null, null));

        Assertions.assertThrows(NullPointerException.class,
                () -> nftClientImpl.burnNfts(null, null, null));
    }

    @Test
    void testAssociateNft() throws HieroException {
        final TokenAssociateResult tokenAssociateResult = Mockito.mock(TokenAssociateResult.class);

        final TokenId tokenId = TokenId.fromString("1.2.3");
        final AccountId accountId = AccountId.fromString("1.2.3");
        final PrivateKey accountKey = PrivateKey.generateECDSA();

        when(protocolLayerClient.executeTokenAssociateTransaction(any(TokenAssociateRequest.class)))
                .thenReturn(tokenAssociateResult);

        nftClientImpl.associateNft(tokenId, accountId, accountKey);

        verify(protocolLayerClient, times(1))
                .executeTokenAssociateTransaction(tokenAssociateCaptor.capture());

        final TokenAssociateRequest request = tokenAssociateCaptor.getValue();
        Assertions.assertEquals(tokenId, request.tokenId());
        Assertions.assertEquals(accountId, request.accountId());
        Assertions.assertEquals(accountKey, request.accountPrivateKey());
    }

    @Test
    void testAssociateNftWithAccount() throws HieroException {
        final TokenAssociateResult tokenAssociateResult = Mockito.mock(TokenAssociateResult.class);
        final AccountId accountId = AccountId.fromString("1.2.3");
        final PrivateKey privateKey = PrivateKey.generateECDSA();
        final PublicKey publicKey = privateKey.getPublicKey();

        final TokenId tokenId = TokenId.fromString("1.2.3");
        final Account account = new Account(accountId, publicKey, privateKey);

        when(protocolLayerClient.executeTokenAssociateTransaction(any(TokenAssociateRequest.class)))
                .thenReturn(tokenAssociateResult);

        nftClientImpl.associateNft(tokenId, account);

        verify(protocolLayerClient, times(1))
                .executeTokenAssociateTransaction(tokenAssociateCaptor.capture());

        final TokenAssociateRequest request = tokenAssociateCaptor.getValue();
        Assertions.assertEquals(tokenId, request.tokenId());
        Assertions.assertEquals(accountId, request.accountId());
        Assertions.assertEquals(privateKey, request.accountPrivateKey());
    }

    @Test
    void testAssociateNftThrowsExceptionForInvalidId() throws HieroException {
        final TokenId tokenId = TokenId.fromString("1.2.3");
        final AccountId accountId = AccountId.fromString("1.2.3");
        final PrivateKey accountKey = PrivateKey.generateECDSA();
        final Account account = new Account(accountId, accountKey.getPublicKey(), accountKey);

        when(protocolLayerClient.executeTokenAssociateTransaction(any(TokenAssociateRequest.class)))
                .thenThrow(new HieroException("Failed to execute transaction of type TokenAssociateTransaction"));

        Assertions.assertThrows(HieroException.class, () -> nftClientImpl.associateNft(tokenId, accountId, accountKey));
        Assertions.assertThrows(HieroException.class, () -> nftClientImpl.associateNft(tokenId, account));
    }

    @Test
    void testAssociateNftNullParam() {
        Assertions.assertThrows(NullPointerException.class,
                () -> nftClientImpl.associateNft((TokenId) null, (AccountId) null, (PrivateKey) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> nftClientImpl.associateNft(null, null));
    }
}
