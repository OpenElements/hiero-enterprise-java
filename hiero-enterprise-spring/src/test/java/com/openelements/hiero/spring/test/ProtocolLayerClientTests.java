package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.Status;
import com.openelements.hiero.base.HieroContext;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.data.AccountBalanceRequest;
import com.openelements.hiero.base.protocol.data.AccountBalanceResponse;
import com.openelements.hiero.base.protocol.data.AccountCreateRequest;
import com.openelements.hiero.base.protocol.data.AccountCreateResult;
import com.openelements.hiero.base.protocol.data.AccountDeleteRequest;
import com.openelements.hiero.base.protocol.data.ContractCreateRequest;
import com.openelements.hiero.base.protocol.data.ContractCreateResult;
import com.openelements.hiero.base.protocol.data.FileAppendRequest;
import com.openelements.hiero.base.protocol.data.FileContentsRequest;
import com.openelements.hiero.base.protocol.data.FileContentsResponse;
import com.openelements.hiero.base.protocol.data.FileCreateRequest;
import com.openelements.hiero.base.protocol.data.FileCreateResult;
import com.openelements.hiero.base.protocol.data.FileDeleteRequest;
import com.openelements.hiero.base.protocol.data.FileDeleteResult;
import com.openelements.hiero.base.protocol.data.FileInfoRequest;
import com.openelements.hiero.base.protocol.data.FileInfoResponse;
import com.openelements.hiero.base.protocol.data.FileUpdateRequest;
import com.openelements.hiero.test.HieroTestUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HieroTestConfig.class)
public class ProtocolLayerClientTests {

  @Autowired private ProtocolLayerClient protocolLayerClient;

  @Autowired private HieroContext hieroContext;

  @Autowired private HieroTestUtils hieroTestUtils;

  @Test
  @Disabled
  void testGetBalance() throws Exception {
    // given
    final AccountBalanceRequest accountBalanceRequest =
        AccountBalanceRequest.of(hieroContext.getOperatorAccount().accountId());

    // when
    final AccountBalanceResponse accountBalanceResult =
        protocolLayerClient.executeAccountBalanceQuery(accountBalanceRequest);

    // then
    Assertions.assertNotNull(accountBalanceResult);
    Assertions.assertNotNull(accountBalanceResult.hbars());
    Assertions.assertTrue(accountBalanceResult.hbars().toTinybars() > 0);
    System.out.println("Balance: " + accountBalanceResult.hbars().toString() + " HBARs");
  }

  @Test
  void testCreateFile() throws Exception {
    // given
    final byte[] contents = "Hello, Hedera!".getBytes();
    final FileCreateRequest request = FileCreateRequest.of(contents);

    // when
    final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);

    // then
    Assertions.assertNotNull(result);
    Assertions.assertNotNull(result.transactionId());
    Assertions.assertEquals(Status.SUCCESS, result.status());
    Assertions.assertNotNull(result.fileId());
  }

  @Test
  void testFileContents() throws Exception {
    // given
    final byte[] contents = "Hello, Hedera!".getBytes();
    final FileCreateRequest request = FileCreateRequest.of(contents);
    final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);
    final FileId fileId = result.fileId();
    final FileContentsRequest contentsRequest = FileContentsRequest.of(fileId);

    // when
    final FileContentsResponse fileContentsResponse =
        protocolLayerClient.executeFileContentsQuery(contentsRequest);

    // then
    Assertions.assertNotNull(fileContentsResponse);
    Assertions.assertArrayEquals(contents, fileContentsResponse.contents());
  }

  @Test
  void testFileInfo() throws Exception {
    // given
    final byte[] contents = "Hello, Hedera!".getBytes();
    final FileCreateRequest request = FileCreateRequest.of(contents);
    final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);
    final FileId fileId = result.fileId();
    final FileInfoRequest infoRequest = FileInfoRequest.of(fileId);

    // when
    final FileInfoResponse infoResponse = protocolLayerClient.executeFileInfoQuery(infoRequest);

    // then
    Assertions.assertNotNull(infoResponse);
    Assertions.assertEquals(contents.length, infoResponse.size());
  }

  @Test
  void testFileDelete() throws Exception {
    // given
    final byte[] contents = "Hello, Hedera!".getBytes();
    final FileCreateRequest request = FileCreateRequest.of(contents);
    final FileCreateResult result = protocolLayerClient.executeFileCreateTransaction(request);
    final FileId fileId = result.fileId();
    final FileDeleteRequest deleteRequest = FileDeleteRequest.of(fileId);

    // when
    final FileDeleteResult deleteResponse =
        protocolLayerClient.executeFileDeleteTransaction(deleteRequest);

    // then
    Assertions.assertNotNull(deleteResponse);
    Assertions.assertEquals(Status.SUCCESS, deleteResponse.status());
    Assertions.assertNotNull(deleteResponse.transactionId());
  }

  @Test
  void testContractCreate() throws Exception {
    // given
    final Path path =
        Path.of(ContractServiceTest.class.getResource("/small_contract.bin").getPath());
    final String content = Files.readString(path, StandardCharsets.UTF_8);
    final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
    final FileCreateRequest fileCreateRequest = FileCreateRequest.of(bytes);
    final FileCreateResult result =
        protocolLayerClient.executeFileCreateTransaction(fileCreateRequest);
    final FileId fileId = result.fileId();
    final ContractCreateRequest request = ContractCreateRequest.of(fileId);

    // when
    final ContractCreateResult contractCreateResult =
        protocolLayerClient.executeContractCreateTransaction(request);

    // then
    Assertions.assertNotNull(contractCreateResult);
    Assertions.assertEquals(Status.SUCCESS, contractCreateResult.status());
    Assertions.assertNotNull(contractCreateResult.transactionId());
    Assertions.assertNotNull(contractCreateResult.contractId());
  }

  @Test
  void testTooLargeFile() {
    // given
    final byte[] contents = new byte[FileCreateRequest.FILE_MAX_SIZE + 1];
    final FileId fakeId = FileId.fromString("1.2.3");

    // then
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> protocolLayerClient.executeFileCreateTransaction(FileCreateRequest.of(contents)));
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            protocolLayerClient.executeFileAppendRequestTransaction(
                FileAppendRequest.of(fakeId, contents)));
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            protocolLayerClient.executeFileUpdateRequestTransaction(
                FileUpdateRequest.of(fakeId, contents)));
  }

  @Test
  void testTooLargeFileForOneTransaction() {
    // given
    final byte[] contents = new byte[FileCreateRequest.FILE_CREATE_MAX_SIZE + 1];
    final FileId fakeId = FileId.fromString("1.2.3");

    // then
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> protocolLayerClient.executeFileCreateTransaction(FileCreateRequest.of(contents)));
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            protocolLayerClient.executeFileAppendRequestTransaction(
                FileAppendRequest.of(fakeId, contents)));
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () ->
            protocolLayerClient.executeFileUpdateRequestTransaction(
                FileUpdateRequest.of(fakeId, contents)));
  }

  @Test
  void testDeleteAccount() throws Exception {
    // given
    final AccountCreateRequest accountCreateRequest = AccountCreateRequest.of();
    final AccountCreateResult accountCreateResult =
        protocolLayerClient.executeAccountCreateTransaction(accountCreateRequest);

    hieroTestUtils.waitForMirrorNodeRecords();
    final AccountDeleteRequest request = AccountDeleteRequest.of(accountCreateResult.newAccount());

    // then
    Assertions.assertDoesNotThrow(
        () -> protocolLayerClient.executeAccountDeleteTransaction(request));
  }

  @Test
  void testDeleteAccountWithTransferAccount() throws Exception {
    // given
    final AccountCreateRequest accountCreateRequest = AccountCreateRequest.of(Hbar.from(10));
    final AccountCreateResult accountCreateResult =
        protocolLayerClient.executeAccountCreateTransaction(accountCreateRequest);
    final Account toDeleteAccount = accountCreateResult.newAccount();

    final AccountCreateRequest transferAccountCreateRequest = AccountCreateRequest.of(Hbar.from(1));
    final AccountCreateResult transferAccountCreateResult =
        protocolLayerClient.executeAccountCreateTransaction(transferAccountCreateRequest);
    final Account toTransferAccount = transferAccountCreateResult.newAccount();

    // when
    final AccountDeleteRequest request =
        AccountDeleteRequest.of(toDeleteAccount, toTransferAccount);
    protocolLayerClient.executeAccountDeleteTransaction(request);

    // then
    hieroTestUtils.waitForMirrorNodeRecords();
    final AccountBalanceRequest accountBalanceRequest =
        AccountBalanceRequest.of(toTransferAccount.accountId());
    final AccountBalanceResponse accountBalanceResult =
        protocolLayerClient.executeAccountBalanceQuery(accountBalanceRequest);
    Assertions.assertEquals(Hbar.from(11), accountBalanceResult.hbars());
  }
}
