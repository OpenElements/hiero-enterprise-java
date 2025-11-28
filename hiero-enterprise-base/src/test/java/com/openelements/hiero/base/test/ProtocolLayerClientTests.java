package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.Client;
import com.openelements.hiero.base.HieroContext;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.implementation.ProtocolLayerClientImpl;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProtocolLayerClientTests {

  @Test
  void testNullConstructorParam() {
    Assertions.assertThrows(NullPointerException.class, () -> new ProtocolLayerClientImpl(null));
  }

  @Test
  void testNullParams() {
    // given
    final HieroContext context =
        new HieroContext() {
          @Override
          public @NonNull Account getOperatorAccount() {
            return null;
          }

          @Override
          public @NonNull Client getClient() {
            return null;
          }
        };
    final ProtocolLayerClient client = new ProtocolLayerClientImpl(context);

    // then
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeAccountBalanceQuery(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeFileContentsQuery(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeFileAppendRequestTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeFileDeleteTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeFileCreateTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeFileUpdateRequestTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeContractCreateTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeContractCallTransaction(null));
    Assertions.assertThrows(NullPointerException.class, () -> client.executeFileInfoQuery(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeAccountDeleteTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeContractDeleteTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeAccountCreateTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeTokenCreateTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeTokenAssociateTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeTokenDissociateTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeTopicCreateTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeTopicDeleteTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeTopicMessageSubmitTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeTokenCreateTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeBurnTokenTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeMintTokenTransaction(null));
    Assertions.assertThrows(
        NullPointerException.class, () -> client.executeTransferTransaction(null));
  }
}
