package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenType;
import com.openelements.hiero.base.AccountClient;
import com.openelements.hiero.base.FungibleTokenClient;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.NftClient;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.data.Balance;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.Token;
import com.openelements.hiero.base.data.TokenInfo;
import com.openelements.hiero.base.mirrornode.TokenRepository;
import com.openelements.hiero.test.HieroTestUtils;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HieroTestConfig.class)
public class TokenRepositoryTest {
  @Autowired private TokenRepository tokenRepository;

  @Autowired private FungibleTokenClient tokenClient;

  @Autowired private AccountClient accountClient;

  @Autowired private NftClient nftClient;

  @Autowired private HieroTestUtils hieroTestUtils;

  @Test
  void testNullParam() {
    Assertions.assertThrows(
        NullPointerException.class, () -> tokenRepository.findByAccount((String) null));
    Assertions.assertThrows(
        NullPointerException.class, () -> tokenRepository.findById((String) null));
    Assertions.assertThrows(
        NullPointerException.class, () -> tokenRepository.getBalances((String) null));
    Assertions.assertThrows(
        NullPointerException.class, () -> tokenRepository.getBalancesForAccount(null, "1.2.3"));
    Assertions.assertThrows(
        NullPointerException.class, () -> tokenRepository.getBalancesForAccount("1.2.3", null));
    Assertions.assertThrows(
        NullPointerException.class,
        () -> tokenRepository.getBalancesForAccount((String) null, null));
  }

  @Test
  void testQueryTokenForAccount() throws HieroException {
    // given
    final String name = "TOKEN";
    final String symbol = "TSY";
    final Account account = accountClient.createAccount();
    final AccountId newOwner = account.accountId();
    final PrivateKey privateKey = account.privateKey();

    final TokenId tokenId = tokenClient.createToken(name, symbol);
    tokenClient.associateToken(tokenId, newOwner, privateKey);
    hieroTestUtils.waitForMirrorNodeRecords();

    // when
    final Page<Token> tokens = tokenRepository.findByAccount(newOwner);

    // then
    Assertions.assertNotNull(tokens);
    Assertions.assertTrue(!tokens.getData().isEmpty());
  }

  @Test
  void testQueryTokenForAccountReturnZeroResult() throws HieroException {
    // given
    final String name = "TOKEN";
    final String symbol = "TSY";
    final Account account = accountClient.createAccount();
    final AccountId newOwner = account.accountId();

    final TokenId tokenId = tokenClient.createToken(name, symbol);
    hieroTestUtils.waitForMirrorNodeRecords();

    // when
    final Page<Token> tokens = tokenRepository.findByAccount(newOwner);

    // then
    Assertions.assertNotNull(tokens);
    Assertions.assertTrue(tokens.getData().isEmpty());
  }

  @Test
  void testQueryTokenById() throws HieroException {
    // given
    final String name = "TOKEN";
    final String symbol = "TSY";
    final TokenId fungiTokenId = tokenClient.createToken(name, symbol);
    final TokenId nftTokenId = nftClient.createNftType(name, symbol);
    hieroTestUtils.waitForMirrorNodeRecords();

    // when
    final Optional<TokenInfo> fungiToken = tokenRepository.findById(fungiTokenId);
    final Optional<TokenInfo> nftToken = tokenRepository.findById(nftTokenId);

    // then
    Assertions.assertTrue(fungiToken.isPresent());
    Assertions.assertEquals(name, fungiToken.get().name());
    Assertions.assertEquals(symbol, fungiToken.get().symbol());
    Assertions.assertEquals(TokenType.FUNGIBLE_COMMON, fungiToken.get().type());

    Assertions.assertTrue(nftToken.isPresent());
    Assertions.assertEquals(name, nftToken.get().name());
    Assertions.assertEquals(symbol, nftToken.get().symbol());
    Assertions.assertEquals(TokenType.NON_FUNGIBLE_UNIQUE, nftToken.get().type());
  }

  @Test
  void testQueryTokenByIdReturnEmptyOptionalForInvalidId() throws HieroException {
    // given
    final TokenId tokenId = TokenId.fromString("1.2.3");
    // when
    final Optional<TokenInfo> token = tokenRepository.findById(tokenId);
    // then
    Assertions.assertTrue(token.isEmpty());
  }

  @Test
  void testGetTokenBalances() throws HieroException {
    // given
    final String name = "TOKEN";
    final String symbol = "TSY";
    final TokenId tokenId = tokenClient.createToken(name, symbol);
    hieroTestUtils.waitForMirrorNodeRecords();

    // when
    final Page<Balance> balances = tokenRepository.getBalances(tokenId);

    // then
    Assertions.assertNotNull(balances.getData());
    Assertions.assertFalse(balances.getData().isEmpty());
  }

  @Test
  void testGetTokenBalancesReturnEmptyResultForInvalidId() throws HieroException {
    // given
    final TokenId tokenId = TokenId.fromString("1.2.3");
    // when
    final Page<Balance> balances = tokenRepository.getBalances(tokenId);
    // then
    Assertions.assertEquals(0, balances.getData().size());
  }

  @Test
  void testGetTokenBalancesForAccount() throws HieroException {
    // given
    final String name = "TOKEN";
    final String symbol = "TSY";
    final Account account = accountClient.createAccount();
    final AccountId newOwner = account.accountId();
    final PrivateKey newPrivateKey = account.privateKey();

    final TokenId tokenId = tokenClient.createToken(name, symbol);
    tokenClient.associateToken(tokenId, newOwner, newPrivateKey);
    hieroTestUtils.waitForMirrorNodeRecords();

    // when
    final Page<Balance> balances = tokenRepository.getBalancesForAccount(tokenId, newOwner);

    // then
    Assertions.assertNotNull(balances.getData());
    Assertions.assertFalse(balances.getData().isEmpty());
  }

  @Test
  void testGetTokenBalancesForAccountReturnZeroResult() throws HieroException {
    // given
    final String name = "TOKEN";
    final String symbol = "TSY";
    final Account account = accountClient.createAccount();
    final AccountId newOwner = account.accountId();

    final TokenId tokenId = tokenClient.createToken(name, symbol);
    hieroTestUtils.waitForMirrorNodeRecords();

    // when
    final Page<Balance> balances = tokenRepository.getBalancesForAccount(tokenId, newOwner);

    // then
    Assertions.assertNotNull(balances.getData());
    Assertions.assertTrue(balances.getData().isEmpty());
  }
}
