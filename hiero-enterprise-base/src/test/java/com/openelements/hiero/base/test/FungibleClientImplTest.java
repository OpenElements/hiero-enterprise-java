package com.openelements.hiero.base.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenType;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.implementation.FungibleTokenClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.TokenAssociateRequest;
import com.openelements.hiero.base.protocol.data.TokenAssociateResult;
import com.openelements.hiero.base.protocol.data.TokenCreateRequest;
import com.openelements.hiero.base.protocol.data.TokenCreateResult;
import com.openelements.hiero.base.protocol.data.TokenDissociateRequest;
import com.openelements.hiero.base.protocol.data.TokenDissociateResult;
import com.openelements.hiero.base.protocol.data.TokenMintRequest;
import com.openelements.hiero.base.protocol.data.TokenMintResult;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class FungibleClientImplTest {
  ProtocolLayerClient protocolLayerClient;
  Account operationalAccount;
  FungibleTokenClientImpl fungibleClientImpl;

  ArgumentCaptor<TokenCreateRequest> tokenCreateCaptor =
      ArgumentCaptor.forClass(TokenCreateRequest.class);
  ArgumentCaptor<TokenAssociateRequest> tokenAssociateCaptor =
      ArgumentCaptor.forClass(TokenAssociateRequest.class);
  ArgumentCaptor<TokenDissociateRequest> tokenDissociateCaptor =
      ArgumentCaptor.forClass(TokenDissociateRequest.class);
  ArgumentCaptor<TokenMintRequest> tokenMintCaptor =
      ArgumentCaptor.forClass(TokenMintRequest.class);

  @BeforeEach
  public void setup() {
    protocolLayerClient = Mockito.mock(ProtocolLayerClient.class);
    operationalAccount = Mockito.mock(Account.class);
    fungibleClientImpl = new FungibleTokenClientImpl(protocolLayerClient, operationalAccount);
  }

  @Test
  void testCreateToken() throws HieroException {
    final TokenCreateResult tokenCreateResult = Mockito.mock(TokenCreateResult.class);
    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey privateKey = PrivateKey.generateECDSA();
    final TokenId tokenId = TokenId.fromString("0.0.1");

    final String name = "TOKEN";
    final String symbol = "FT";

    when(operationalAccount.accountId()).thenReturn(accountId);
    when(operationalAccount.privateKey()).thenReturn(privateKey);
    when(protocolLayerClient.executeTokenCreateTransaction(any(TokenCreateRequest.class)))
        .thenReturn(tokenCreateResult);
    when(tokenCreateResult.tokenId()).thenReturn(tokenId);

    final TokenId result = fungibleClientImpl.createToken(name, symbol);

    verify(operationalAccount, times(1)).accountId();
    verify(operationalAccount, times(2)).privateKey(); // for supply & treasuryKey
    verify(protocolLayerClient, times(1))
        .executeTokenCreateTransaction(tokenCreateCaptor.capture());
    verify(tokenCreateResult, times(1)).tokenId();

    final TokenCreateRequest capture = tokenCreateCaptor.getValue();
    Assertions.assertEquals(tokenId, result);
    Assertions.assertEquals(accountId, capture.treasuryAccountId());
    Assertions.assertEquals(privateKey, capture.treasuryKey());
    Assertions.assertEquals(privateKey, capture.supplyKey());
    Assertions.assertEquals(name, capture.name());
    Assertions.assertEquals(symbol, capture.symbol());
    Assertions.assertEquals(TokenType.FUNGIBLE_COMMON, capture.tokenType());
  }

  @Test
  void testCreateTokenWithSupplyKey() throws HieroException {
    final TokenCreateResult tokenCreateResult = Mockito.mock(TokenCreateResult.class);
    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey privateKey = PrivateKey.generateECDSA();
    final PrivateKey supplyKey = PrivateKey.generateECDSA();
    final TokenId tokenId = TokenId.fromString("0.0.1");

    final String name = "TOKEN";
    final String symbol = "FT";

    when(operationalAccount.accountId()).thenReturn(accountId);
    when(operationalAccount.privateKey()).thenReturn(privateKey);
    when(protocolLayerClient.executeTokenCreateTransaction(any(TokenCreateRequest.class)))
        .thenReturn(tokenCreateResult);
    when(tokenCreateResult.tokenId()).thenReturn(tokenId);

    final TokenId result = fungibleClientImpl.createToken(name, symbol, supplyKey);

    verify(operationalAccount, times(1)).accountId();
    verify(operationalAccount, times(1)).privateKey();
    verify(protocolLayerClient, times(1))
        .executeTokenCreateTransaction(tokenCreateCaptor.capture());
    verify(tokenCreateResult, times(1)).tokenId();

    final TokenCreateRequest capture = tokenCreateCaptor.getValue();
    Assertions.assertEquals(tokenId, result);
    Assertions.assertEquals(accountId, capture.treasuryAccountId());
    Assertions.assertEquals(privateKey, capture.treasuryKey());
    Assertions.assertEquals(supplyKey, capture.supplyKey());
    Assertions.assertEquals(name, capture.name());
    Assertions.assertEquals(symbol, capture.symbol());
    Assertions.assertEquals(TokenType.FUNGIBLE_COMMON, capture.tokenType());
  }

  @Test
  void testCreateTokenWithAccountIdAndTreasuryKey() throws HieroException {
    final TokenCreateResult tokenCreateResult = Mockito.mock(TokenCreateResult.class);
    final AccountId treasuryAccountId = AccountId.fromString("1.2.3");
    final PrivateKey treasuryKey = PrivateKey.generateECDSA();
    final PrivateKey supplyKey = PrivateKey.generateECDSA();
    final TokenId tokenId = TokenId.fromString("0.0.1");

    final String name = "TOKEN";
    final String symbol = "FT";

    when(operationalAccount.privateKey()).thenReturn(supplyKey);
    when(protocolLayerClient.executeTokenCreateTransaction(any(TokenCreateRequest.class)))
        .thenReturn(tokenCreateResult);
    when(tokenCreateResult.tokenId()).thenReturn(tokenId);

    final TokenId result =
        fungibleClientImpl.createToken(name, symbol, treasuryAccountId, treasuryKey);

    verify(operationalAccount, times(1)).privateKey();
    verify(protocolLayerClient, times(1))
        .executeTokenCreateTransaction(tokenCreateCaptor.capture());
    verify(tokenCreateResult, times(1)).tokenId();

    final TokenCreateRequest capture = tokenCreateCaptor.getValue();
    Assertions.assertEquals(tokenId, result);
    Assertions.assertEquals(treasuryAccountId, capture.treasuryAccountId());
    Assertions.assertEquals(treasuryKey, capture.treasuryKey());
    Assertions.assertEquals(supplyKey, capture.supplyKey());
    Assertions.assertEquals(name, capture.name());
    Assertions.assertEquals(symbol, capture.symbol());
    Assertions.assertEquals(TokenType.FUNGIBLE_COMMON, capture.tokenType());
  }

  @Test
  void testCreateTokenWithAccountIdAndTreasuryKeyAndSupplyKey() throws HieroException {
    final TokenCreateResult tokenCreateResult = Mockito.mock(TokenCreateResult.class);
    final AccountId treasuryAccountId = AccountId.fromString("1.2.3");
    final PrivateKey treasuryKey = PrivateKey.generateECDSA();
    final PrivateKey supplyKey = PrivateKey.generateECDSA();
    final TokenId tokenId = TokenId.fromString("0.0.1");

    final String name = "TOKEN";
    final String symbol = "FT";

    when(protocolLayerClient.executeTokenCreateTransaction(any(TokenCreateRequest.class)))
        .thenReturn(tokenCreateResult);
    when(tokenCreateResult.tokenId()).thenReturn(tokenId);

    final TokenId result =
        fungibleClientImpl.createToken(name, symbol, treasuryAccountId, treasuryKey, supplyKey);

    verify(protocolLayerClient, times(1))
        .executeTokenCreateTransaction(tokenCreateCaptor.capture());
    verify(tokenCreateResult, times(1)).tokenId();

    final TokenCreateRequest capture = tokenCreateCaptor.getValue();
    Assertions.assertEquals(tokenId, result);
    Assertions.assertEquals(treasuryAccountId, capture.treasuryAccountId());
    Assertions.assertEquals(treasuryKey, capture.treasuryKey());
    Assertions.assertEquals(supplyKey, capture.supplyKey());
    Assertions.assertEquals(name, capture.name());
    Assertions.assertEquals(symbol, capture.symbol());
    Assertions.assertEquals(TokenType.FUNGIBLE_COMMON, capture.tokenType());
  }

  @Test
  void testCreateTokenThrowExceptionIfSymbolLengthGreaterThanMaxSymbolLen() {
    final String message = "Symbol length must be less than or equal to 100";

    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey privateKey = PrivateKey.generateECDSA();
    final PrivateKey supplyKey = PrivateKey.generateECDSA();

    final String name = "TOKEN";
    final String symbol = new String(new byte[101]);

    when(operationalAccount.accountId()).thenReturn(accountId);
    when(operationalAccount.privateKey()).thenReturn(privateKey);

    final IllegalArgumentException e1 =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> fungibleClientImpl.createToken(name, symbol));
    final IllegalArgumentException e2 =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> fungibleClientImpl.createToken(name, symbol, supplyKey));
    final IllegalArgumentException e3 =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> fungibleClientImpl.createToken(name, symbol, accountId, privateKey));
    final IllegalArgumentException e4 =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> fungibleClientImpl.createToken(name, symbol, accountId, privateKey, supplyKey));

    Assertions.assertEquals(message, e1.getMessage());
    Assertions.assertEquals(message, e2.getMessage());
    Assertions.assertEquals(message, e3.getMessage());
    Assertions.assertEquals(message, e4.getMessage());
  }

  @Test
  void testCreateTokenThrowExceptionForNullParams() {
    final PrivateKey privateKey = PrivateKey.generateECDSA();
    final String name = "TOKEN";
    final String symbol = "FT";

    final NullPointerException e1 =
        Assertions.assertThrows(
            NullPointerException.class, () -> fungibleClientImpl.createToken(null, symbol));
    final NullPointerException e2 =
        Assertions.assertThrows(
            NullPointerException.class, () -> fungibleClientImpl.createToken(name, null));
    final NullPointerException e3 =
        Assertions.assertThrows(
            NullPointerException.class,
            () -> fungibleClientImpl.createToken(name, symbol, (PrivateKey) null));
    final NullPointerException e4 =
        Assertions.assertThrows(
            NullPointerException.class,
            () -> fungibleClientImpl.createToken(name, symbol, (AccountId) null, privateKey));
    Assertions.assertEquals("name must not be null", e1.getMessage());
    Assertions.assertEquals("symbol must not be null", e2.getMessage());
    Assertions.assertEquals("supplyKey must not be null", e3.getMessage());
    Assertions.assertEquals("Treasury account ID cannot be null", e4.getMessage());
  }

  @Test
  void testAssociateToken() throws HieroException {
    final TokenAssociateResult tokenAssociateResult = Mockito.mock(TokenAssociateResult.class);

    final TokenId tokenId = TokenId.fromString("1.2.3");
    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey accountKey = PrivateKey.generateECDSA();

    when(protocolLayerClient.executeTokenAssociateTransaction(any(TokenAssociateRequest.class)))
        .thenReturn(tokenAssociateResult);

    fungibleClientImpl.associateToken(tokenId, accountId, accountKey);

    verify(protocolLayerClient, times(1))
        .executeTokenAssociateTransaction(tokenAssociateCaptor.capture());

    final TokenAssociateRequest request = tokenAssociateCaptor.getValue();
    Assertions.assertEquals(List.of(tokenId), request.tokenIds());
    Assertions.assertEquals(accountId, request.accountId());
    Assertions.assertEquals(accountKey, request.accountPrivateKey());
  }

  @Test
  void testAssociateTokenWithAccount() throws HieroException {
    final TokenAssociateResult tokenAssociateResult = Mockito.mock(TokenAssociateResult.class);
    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey privateKey = PrivateKey.generateECDSA();
    final PublicKey publicKey = privateKey.getPublicKey();

    final TokenId tokenId = TokenId.fromString("1.2.3");
    final Account account = new Account(accountId, publicKey, privateKey);

    when(protocolLayerClient.executeTokenAssociateTransaction(any(TokenAssociateRequest.class)))
        .thenReturn(tokenAssociateResult);

    fungibleClientImpl.associateToken(tokenId, account);

    verify(protocolLayerClient, times(1))
        .executeTokenAssociateTransaction(tokenAssociateCaptor.capture());

    final TokenAssociateRequest request = tokenAssociateCaptor.getValue();
    Assertions.assertEquals(List.of(tokenId), request.tokenIds());
    Assertions.assertEquals(accountId, request.accountId());
    Assertions.assertEquals(privateKey, request.accountPrivateKey());
  }

  @Test
  void testAssociateTokenThrowsExceptionForInvalidId() throws HieroException {
    final TokenId tokenId = TokenId.fromString("1.2.3");
    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey accountKey = PrivateKey.generateECDSA();
    final Account account = new Account(accountId, accountKey.getPublicKey(), accountKey);

    when(protocolLayerClient.executeTokenAssociateTransaction(any(TokenAssociateRequest.class)))
        .thenThrow(
            new HieroException("Failed to execute transaction of type TokenAssociateTransaction"));

    Assertions.assertThrows(
        HieroException.class,
        () -> fungibleClientImpl.associateToken(tokenId, accountId, accountKey));
    Assertions.assertThrows(
        HieroException.class, () -> fungibleClientImpl.associateToken(tokenId, account));
  }

  @Test
  void testAssociateTokenNullParam() {
    Assertions.assertThrows(
        NullPointerException.class,
        () ->
            fungibleClientImpl.associateToken((TokenId) null, (AccountId) null, (PrivateKey) null));
    Assertions.assertThrows(
        NullPointerException.class, () -> fungibleClientImpl.associateToken((TokenId) null, null));
  }

  @Test
  void testAssociateTokenWithMultipleToken() throws HieroException {
    final TokenAssociateResult tokenAssociateResult = Mockito.mock(TokenAssociateResult.class);

    final TokenId tokenId1 = TokenId.fromString("1.2.3");
    final TokenId tokenId2 = TokenId.fromString("1.2.4");

    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey accountKey = PrivateKey.generateECDSA();

    when(protocolLayerClient.executeTokenAssociateTransaction(any(TokenAssociateRequest.class)))
        .thenReturn(tokenAssociateResult);

    fungibleClientImpl.associateToken(List.of(tokenId1, tokenId2), accountId, accountKey);

    verify(protocolLayerClient, times(1))
        .executeTokenAssociateTransaction(tokenAssociateCaptor.capture());

    final TokenAssociateRequest request = tokenAssociateCaptor.getValue();
    Assertions.assertEquals(List.of(tokenId1, tokenId2), request.tokenIds());
    Assertions.assertEquals(accountId, request.accountId());
    Assertions.assertEquals(accountKey, request.accountPrivateKey());
  }

  @Test
  void testAssociateTokenWithMultipleTokenThrowExceptionIfListEmpty() {
    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey accountKey = PrivateKey.generateECDSA();

    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> fungibleClientImpl.associateToken(List.of(), accountId, accountKey));
    Assertions.assertEquals("tokenIds must not be empty", e.getMessage());
  }

  @Test
  void testDissociateToken() throws HieroException {
    final TokenDissociateResult tokenDissociateResult = Mockito.mock(TokenDissociateResult.class);

    final TokenId tokenId = TokenId.fromString("1.2.3");
    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey accountKey = PrivateKey.generateECDSA();

    when(protocolLayerClient.executeTokenDissociateTransaction(any(TokenDissociateRequest.class)))
        .thenReturn(tokenDissociateResult);

    fungibleClientImpl.dissociateToken(tokenId, accountId, accountKey);

    verify(protocolLayerClient, times(1))
        .executeTokenDissociateTransaction(tokenDissociateCaptor.capture());

    final TokenDissociateRequest request = tokenDissociateCaptor.getValue();
    Assertions.assertEquals(List.of(tokenId), request.tokenIds());
    Assertions.assertEquals(accountId, request.accountId());
    Assertions.assertEquals(accountKey, request.accountKey());
  }

  @Test
  void testDissociateTokenThrowsExceptionForInvalidId() throws HieroException {
    final TokenId tokenId = TokenId.fromString("1.2.3");
    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey accountKey = PrivateKey.generateECDSA();
    final Account account = new Account(accountId, accountKey.getPublicKey(), accountKey);

    when(protocolLayerClient.executeTokenDissociateTransaction(any(TokenDissociateRequest.class)))
        .thenThrow(
            new HieroException("Failed to execute transaction of type TokenDissociateTransaction"));

    Assertions.assertThrows(
        HieroException.class,
        () -> fungibleClientImpl.dissociateToken(tokenId, accountId, accountKey));
    Assertions.assertThrows(
        HieroException.class, () -> fungibleClientImpl.dissociateToken(tokenId, account));
  }

  @Test
  void testDissociateTokenNullParam() {
    Assertions.assertThrows(
        NullPointerException.class,
        () -> fungibleClientImpl.dissociateToken((TokenId) null, null, null));
    Assertions.assertThrows(
        NullPointerException.class, () -> fungibleClientImpl.dissociateToken((TokenId) null, null));
  }

  @Test
  void testDissociateTokenWithMultipleToken() throws HieroException {
    final TokenDissociateResult tokenDissociateResult = Mockito.mock(TokenDissociateResult.class);

    final TokenId tokenId1 = TokenId.fromString("1.2.3");
    final TokenId tokenId2 = TokenId.fromString("1.2.4");

    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey accountKey = PrivateKey.generateECDSA();

    when(protocolLayerClient.executeTokenDissociateTransaction(any(TokenDissociateRequest.class)))
        .thenReturn(tokenDissociateResult);

    fungibleClientImpl.dissociateToken(List.of(tokenId1, tokenId2), accountId, accountKey);

    verify(protocolLayerClient, times(1))
        .executeTokenDissociateTransaction(tokenDissociateCaptor.capture());

    final TokenDissociateRequest request = tokenDissociateCaptor.getValue();
    Assertions.assertEquals(List.of(tokenId1, tokenId2), request.tokenIds());
    Assertions.assertEquals(accountId, request.accountId());
    Assertions.assertEquals(accountKey, request.accountKey());
  }

  @Test
  void testDissociateTokenWithMultipleTokenThrowExceptionIfListEmpty() {
    final AccountId accountId = AccountId.fromString("1.2.3");
    final PrivateKey accountKey = PrivateKey.generateECDSA();

    IllegalArgumentException e =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> fungibleClientImpl.dissociateToken(List.of(), accountId, accountKey));
    Assertions.assertEquals("tokenIds must not be empty", e.getMessage());
  }

  @Test
  void testMintToken() throws HieroException {
    final TokenMintResult tokenMintResult = Mockito.mock(TokenMintResult.class);
    final long totalSupply = 20;
    final PrivateKey privateKey = PrivateKey.generateECDSA();

    final TokenId tokenId = TokenId.fromString("0.0.1");
    final long amount = 10;

    when(operationalAccount.privateKey()).thenReturn(privateKey);
    when(protocolLayerClient.executeMintTokenTransaction(any(TokenMintRequest.class)))
        .thenReturn(tokenMintResult);
    when(tokenMintResult.totalSupply()).thenReturn(totalSupply);

    final long result = fungibleClientImpl.mintToken(tokenId, amount);

    verify(operationalAccount, times(1)).privateKey();
    verify(protocolLayerClient, times(1)).executeMintTokenTransaction(tokenMintCaptor.capture());
    verify(tokenMintResult, times(1)).totalSupply();

    final TokenMintRequest capture = tokenMintCaptor.getValue();
    Assertions.assertEquals(tokenId, capture.tokenId());
    Assertions.assertEquals(privateKey, capture.supplyKey());
    Assertions.assertEquals(amount, capture.amount());
    Assertions.assertEquals(totalSupply, result);
  }

  @Test
  void testMintTokenWithSupplyKey() throws HieroException {
    final TokenMintResult tokenMintResult = Mockito.mock(TokenMintResult.class);
    final long totalSupply = 20;

    final PrivateKey supplyKey = PrivateKey.generateECDSA();
    final TokenId tokenId = TokenId.fromString("0.0.1");
    final long amount = 10;

    when(protocolLayerClient.executeMintTokenTransaction(any(TokenMintRequest.class)))
        .thenReturn(tokenMintResult);
    when(tokenMintResult.totalSupply()).thenReturn(totalSupply);

    final long result = fungibleClientImpl.mintToken(tokenId, supplyKey, amount);

    verify(protocolLayerClient, times(1)).executeMintTokenTransaction(tokenMintCaptor.capture());
    verify(tokenMintResult, times(1)).totalSupply();

    final TokenMintRequest capture = tokenMintCaptor.getValue();
    Assertions.assertEquals(tokenId, capture.tokenId());
    Assertions.assertEquals(supplyKey, capture.supplyKey());
    Assertions.assertEquals(amount, capture.amount());
    Assertions.assertEquals(totalSupply, result);
  }

  @Test
  void testMintTokenThrowExceptionIfAmountLessThanEqualToZero() {
    final String message = "amount must be greater than 0";

    final PrivateKey supplyKey = PrivateKey.generateECDSA();
    final TokenId tokenId = TokenId.fromString("0.0.1");
    final long amount = 0;

    when(operationalAccount.privateKey()).thenReturn(supplyKey);

    final IllegalArgumentException e1 =
        Assertions.assertThrows(
            IllegalArgumentException.class, () -> fungibleClientImpl.mintToken(tokenId, amount));
    final IllegalArgumentException e2 =
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> fungibleClientImpl.mintToken(tokenId, supplyKey, amount));

    Assertions.assertEquals(message, e1.getMessage());
    Assertions.assertEquals(message, e2.getMessage());
  }

  @Test
  void testMintTokenThrowExceptionForInvalidTokenId() throws HieroException {
    final PrivateKey supplyKey = PrivateKey.generateECDSA();
    final TokenId tokenId = TokenId.fromString("0.0.1");
    final long amount = 10;

    when(operationalAccount.privateKey()).thenReturn(supplyKey);
    when(protocolLayerClient.executeMintTokenTransaction(any(TokenMintRequest.class)))
        .thenThrow(
            new HieroException("Failed to execute transaction of type TokenMintTransaction"));

    Assertions.assertThrows(
        HieroException.class, () -> fungibleClientImpl.mintToken(tokenId, amount));
    Assertions.assertThrows(
        HieroException.class, () -> fungibleClientImpl.mintToken(tokenId, supplyKey, amount));
  }

  @Test
  void testMintTokenThrowExceptionForNullParams() {
    final PrivateKey supplyKey = PrivateKey.generateECDSA();
    final TokenId tokenId = TokenId.fromString("0.0.1");
    final long amount = 10;

    final NullPointerException e1 =
        Assertions.assertThrows(
            NullPointerException.class, () -> fungibleClientImpl.mintToken((TokenId) null, amount));
    final NullPointerException e2 =
        Assertions.assertThrows(
            NullPointerException.class,
            () -> fungibleClientImpl.mintToken(null, supplyKey, amount));
    final NullPointerException e3 =
        Assertions.assertThrows(
            NullPointerException.class, () -> fungibleClientImpl.mintToken(tokenId, null, amount));

    Assertions.assertEquals("tokenId must not be null", e1.getMessage());
    Assertions.assertEquals("tokenId must not be null", e2.getMessage());
    Assertions.assertEquals("supplyKey must not be null", e3.getMessage());
  }
}
