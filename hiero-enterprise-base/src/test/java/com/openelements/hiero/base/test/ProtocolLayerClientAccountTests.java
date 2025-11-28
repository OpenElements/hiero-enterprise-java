package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.Hbar;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.AccountBalanceRequest;
import com.openelements.hiero.base.protocol.data.AccountBalanceResponse;
import com.openelements.hiero.base.protocol.data.AccountCreateRequest;
import com.openelements.hiero.base.protocol.data.AccountCreateResult;
import com.openelements.hiero.base.protocol.data.AccountDeleteRequest;
import com.openelements.hiero.base.test.config.HieroTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ProtocolLayerClientAccountTests {

  private static HieroTestContext hieroTestContext;

  private static ProtocolLayerClient protocolLayerClient;

  @BeforeAll
  static void init() {
    hieroTestContext = new HieroTestContext();
    protocolLayerClient = new ProtocolLayerClientImpl(hieroTestContext);
  }

  @Test
  void testAccountBalanceRequest() throws Exception {
    // given
    final Hbar amount = Hbar.from(1L);
    final AccountCreateRequest accountCreateRequest = AccountCreateRequest.of(amount);
    final AccountCreateResult accountCreateResult =
        protocolLayerClient.executeAccountCreateTransaction(accountCreateRequest);
    final AccountId accountId = accountCreateResult.newAccount().accountId();

    // when
    final AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.of(accountId);
    final AccountBalanceResponse accountBalanceResponse =
        protocolLayerClient.executeAccountBalanceQuery(accountBalanceRequest);

    // then
    Assertions.assertNotNull(accountBalanceResponse);
    Assertions.assertNotNull(accountBalanceResponse.hbars());
    Assertions.assertEquals(amount, accountBalanceResponse.hbars());
  }

  @Test
  void testAccountBalanceRequestForZeroBalance() throws Exception {
    // given
    final AccountCreateRequest accountCreateRequest = AccountCreateRequest.of();
    final AccountCreateResult accountCreateResult =
        protocolLayerClient.executeAccountCreateTransaction(accountCreateRequest);
    final AccountId accountId = accountCreateResult.newAccount().accountId();

    // when
    final AccountBalanceRequest accountBalanceRequest = AccountBalanceRequest.of(accountId);
    final AccountBalanceResponse accountBalanceResponse =
        protocolLayerClient.executeAccountBalanceQuery(accountBalanceRequest);

    // then
    Assertions.assertNotNull(accountBalanceResponse);
    Assertions.assertNotNull(accountBalanceResponse.hbars());
    Assertions.assertEquals(0L, accountBalanceResponse.hbars().toTinybars());
  }

  @Test
  void testAccountBalanceRequestForNotExistingAccount() throws Exception {
    // given
    final AccountCreateRequest accountCreateRequest = AccountCreateRequest.of();
    final AccountCreateResult accountCreateResult =
        protocolLayerClient.executeAccountCreateTransaction(accountCreateRequest);
    final Account account = accountCreateResult.newAccount();
    AccountDeleteRequest accountDeleteRequest = AccountDeleteRequest.of(account);
    protocolLayerClient.executeAccountDeleteTransaction(accountDeleteRequest);

    // when
    final AccountBalanceRequest accountBalanceRequest =
        AccountBalanceRequest.of(account.accountId());

    // then
    Assertions.assertThrows(
        HieroException.class,
        () -> protocolLayerClient.executeAccountBalanceQuery(accountBalanceRequest));
  }
}
