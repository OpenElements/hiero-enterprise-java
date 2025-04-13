package com.openelements.hiero.base.test;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.FileId;
import com.hedera.hashgraph.sdk.Hbar;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.Status;
import com.hedera.hashgraph.sdk.TopicId;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.ContractFunctionResult;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TokenType;
import com.hedera.hashgraph.sdk.proto.ContractFunctionResultOrBuilder;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.data.ContractParam;
import com.openelements.hiero.base.protocol.data.AccountBalanceRequest;
import com.openelements.hiero.base.protocol.data.AccountBalanceResponse;
import com.openelements.hiero.base.protocol.data.AccountCreateRequest;
import com.openelements.hiero.base.protocol.data.AccountCreateResult;
import com.openelements.hiero.base.protocol.data.AccountDeleteRequest;
import com.openelements.hiero.base.protocol.data.AccountDeleteResult;
import com.openelements.hiero.base.protocol.data.ContractCallRequest;
import com.openelements.hiero.base.protocol.data.ContractCallResult;
import com.openelements.hiero.base.protocol.data.ContractCreateRequest;
import com.openelements.hiero.base.protocol.data.ContractCreateResult;
import com.openelements.hiero.base.protocol.data.ContractDeleteRequest;
import com.openelements.hiero.base.protocol.data.ContractDeleteResult;
import com.openelements.hiero.base.protocol.data.FileAppendRequest;
import com.openelements.hiero.base.protocol.data.TokenTransferResult;
import com.openelements.hiero.base.protocol.data.TopicDeleteResult;
import com.openelements.hiero.base.protocol.data.TopicCreateResult;
import com.openelements.hiero.base.protocol.data.TokenMintResult;
import com.openelements.hiero.base.protocol.data.TokenCreateResult;
import com.openelements.hiero.base.protocol.data.TokenBurnResult;
import com.openelements.hiero.base.protocol.data.TokenAssociateResult;
import com.openelements.hiero.base.protocol.data.FileUpdateResult;
import com.openelements.hiero.base.protocol.data.FileInfoResponse;
import com.openelements.hiero.base.protocol.data.FileDeleteResult;
import com.openelements.hiero.base.protocol.data.FileCreateResult;
import com.openelements.hiero.base.protocol.data.FileContentsResponse;
import com.openelements.hiero.base.protocol.data.FileAppendResult;
import com.openelements.hiero.base.protocol.data.FileContentsRequest;
import com.openelements.hiero.base.protocol.data.TokenTransferRequest;
import com.openelements.hiero.base.protocol.data.TokenMintRequest;
import com.openelements.hiero.base.protocol.data.TokenCreateRequest;
import com.openelements.hiero.base.protocol.data.TokenBurnRequest;
import com.openelements.hiero.base.protocol.data.TokenAssociateRequest;
import com.openelements.hiero.base.protocol.data.FileUpdateRequest;
import com.openelements.hiero.base.protocol.data.FileInfoRequest;
import com.openelements.hiero.base.protocol.data.FileDeleteRequest;
import com.openelements.hiero.base.protocol.data.FileCreateRequest;
import com.openelements.hiero.base.protocol.data.TopicSubmitMessageResult;
import com.openelements.hiero.base.protocol.data.TopicSubmitMessageRequest;
import com.openelements.hiero.base.protocol.data.TopicDeleteRequest;
import com.openelements.hiero.base.protocol.data.TopicCreateRequest;
import com.openelements.hiero.base.protocol.data.TopicUpdateRequest;
import com.openelements.hiero.base.protocol.data.TopicUpdateResult;
import com.openelements.hiero.base.protocol.data.TokenDissociateRequest;
import com.openelements.hiero.base.protocol.data.TokenDissociateResult;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ProtocolLayerDataCreationTests {

    @Test
    void testAccountBalanceRequestCreation() {
        //given
        final String accountIdString = "0.0.12345";
        final AccountId accountId = AccountId.fromString(accountIdString);

        //then
        Assertions.assertDoesNotThrow(() -> AccountBalanceRequest.of(accountIdString));
        Assertions.assertDoesNotThrow(() -> AccountBalanceRequest.of(accountId));
        Assertions.assertDoesNotThrow(() -> new AccountBalanceRequest(accountId, null, null));
        Assertions.assertThrows(NullPointerException.class, () -> AccountBalanceRequest.of((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountBalanceRequest(null, null, null));
    }

    @Test
    void testAccountBalanceResponseCreation() {
        //given
        final Hbar balance = Hbar.fromTinybars(1000);

        //then
        Assertions.assertDoesNotThrow(() -> AccountBalanceResponse.of(balance));
        Assertions.assertDoesNotThrow(() -> new AccountBalanceResponse(balance));
        Assertions.assertThrows(NullPointerException.class, () -> AccountBalanceResponse.of(null));
        Assertions.assertThrows(NullPointerException.class, () -> new AccountBalanceResponse(null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> AccountBalanceResponse.of(Hbar.fromTinybars(-1000)));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AccountBalanceResponse(Hbar.fromTinybars(-1000)));
    }

    @Test
    void testAccountCreateRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final Hbar initialBalance = Hbar.fromTinybars(1000);

        //then
        Assertions.assertDoesNotThrow(() -> AccountCreateRequest.of());
        Assertions.assertDoesNotThrow(() -> AccountCreateRequest.of(initialBalance));
        Assertions.assertDoesNotThrow(
                () -> new AccountCreateRequest(Hbar.fromTinybars(1000), transactionValidDuration, initialBalance));
        Assertions.assertThrows(NullPointerException.class, () -> AccountCreateRequest.of(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountCreateRequest(null, transactionValidDuration, initialBalance));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountCreateRequest(maxTransactionFee, null, initialBalance));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountCreateRequest(maxTransactionFee, transactionValidDuration, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> AccountCreateRequest.of(Hbar.fromTinybars(-1000)));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AccountCreateRequest(maxTransactionFee, Duration.ZERO, initialBalance));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AccountCreateRequest(maxTransactionFee, Duration.ofSeconds(-10), initialBalance));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AccountCreateRequest(Hbar.fromTinybars(-1000), transactionValidDuration, initialBalance));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AccountCreateRequest(maxTransactionFee, transactionValidDuration, Hbar.fromTinybars(-1000)));
    }

    @Test
    void testAccountCreateResultCreation() {
        //given
        final AccountId accountId = new AccountId(0, 0, 12345);
        final PrivateKey privateKey = PrivateKey.generateECDSA();
        final Account account = Account.of(accountId, privateKey);
        final TransactionId transactionId = TransactionId.generate(accountId);
        final Status status = Status.SUCCESS;
        final byte[] transactionHash = new byte[]{};
        final Instant consensusTimestamp = Instant.now();
        final Hbar transactionFee = Hbar.fromTinybars(1000);

        //then
        Assertions.assertDoesNotThrow(
                () -> new AccountCreateResult(transactionId, status, transactionHash, consensusTimestamp,
                        transactionFee, account));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountCreateResult(null, status, transactionHash, consensusTimestamp, transactionFee,
                        account));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountCreateResult(transactionId, null, transactionHash, consensusTimestamp, transactionFee,
                        account));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountCreateResult(transactionId, status, null, consensusTimestamp, transactionFee,
                        account));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountCreateResult(transactionId, status, transactionHash, null, transactionFee, account));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountCreateResult(transactionId, status, transactionHash, consensusTimestamp, null,
                        account));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountCreateResult(transactionId, status, transactionHash, consensusTimestamp,
                        transactionFee, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AccountCreateResult(transactionId, status, transactionHash, consensusTimestamp,
                        Hbar.fromTinybars(-1000), account));
    }

    @Test
    void testAccountDeleteRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final Account toDelete = Account.of(new AccountId(0, 0, 12345), PrivateKey.generateECDSA());
        final Account transferFoundsToAccount = Account.of(new AccountId(0, 0, 54321), PrivateKey.generateECDSA());

        //then
        Assertions.assertDoesNotThrow(() -> AccountDeleteRequest.of(toDelete));
        Assertions.assertDoesNotThrow(() -> AccountDeleteRequest.of(toDelete, transferFoundsToAccount));
        Assertions.assertDoesNotThrow(() -> AccountDeleteRequest.of(toDelete, null));
        Assertions.assertDoesNotThrow(
                () -> new AccountDeleteRequest(maxTransactionFee, transactionValidDuration, toDelete,
                        transferFoundsToAccount));
        Assertions.assertDoesNotThrow(
                () -> new AccountDeleteRequest(maxTransactionFee, transactionValidDuration, toDelete, null));
        Assertions.assertThrows(NullPointerException.class, () -> AccountDeleteRequest.of(null));
        Assertions.assertThrows(NullPointerException.class,
                () -> AccountDeleteRequest.of(null, transferFoundsToAccount));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountDeleteRequest(null, transactionValidDuration, toDelete, transferFoundsToAccount));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountDeleteRequest(maxTransactionFee, null, toDelete, transferFoundsToAccount));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountDeleteRequest(maxTransactionFee, transactionValidDuration, null,
                        transferFoundsToAccount));
        Assertions.assertThrows(IllegalArgumentException.class, () -> AccountDeleteRequest.of(toDelete, toDelete));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AccountDeleteRequest(Hbar.from(-1000), transactionValidDuration, toDelete,
                        transferFoundsToAccount));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AccountDeleteRequest(maxTransactionFee, Duration.ZERO, toDelete, transferFoundsToAccount));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AccountDeleteRequest(maxTransactionFee, Duration.ofSeconds(-10), toDelete,
                        transferFoundsToAccount));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new AccountDeleteRequest(maxTransactionFee, transactionValidDuration, toDelete, toDelete));
    }

    @Test
    void testAccountDeleteResultCreation() {
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;
        final byte[] transactionHash = new byte[]{};
        final Instant consensusTimestamp = Instant.now();
        final Hbar transactionFee = Hbar.fromTinybars(1000);

        Assertions.assertDoesNotThrow(
                () -> new AccountDeleteResult(transactionId, status, transactionHash, consensusTimestamp,
                        transactionFee));
        Assertions.assertDoesNotThrow(
                () -> new AccountDeleteResult(transactionId, status, transactionHash, null, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountDeleteResult(null, status, transactionHash, consensusTimestamp, transactionFee));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountDeleteResult(transactionId, null, transactionHash, consensusTimestamp,
                        transactionFee));
        Assertions.assertThrows(NullPointerException.class,
                () -> new AccountDeleteResult(transactionId, status, null, consensusTimestamp, transactionFee));
    }

    @Test
    void testTopicCreateRequestCreation() {
        //given
        final Hbar validMaxTransactionFee = Hbar.fromTinybars(1000);
        final Duration validTransactionDuration = Duration.ofSeconds(120);
        final PrivateKey adminKey = PrivateKey.generateECDSA();

        Assertions.assertDoesNotThrow(() -> new TopicCreateRequest(
                validMaxTransactionFee, validTransactionDuration, adminKey, null, null));
        Assertions.assertThrows(
                NullPointerException.class,
                () -> new TopicCreateRequest(null, null, adminKey, null, null)
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> new TopicCreateRequest(null, validTransactionDuration, null, null, null)
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> new TopicCreateRequest(validMaxTransactionFee, null, null, null, null)
        );
    }

    @Test
    void testTopicUpdateRequestCreation() {
        //given
        final Hbar validMaxTransactionFee = Hbar.fromTinybars(1000);
        final Duration validTransactionDuration = Duration.ofSeconds(120);
        final PrivateKey adminKey = PrivateKey.generateECDSA();
        final TopicId topicId = TopicId.fromString("1.2.3");


        Assertions.assertDoesNotThrow(() -> new TopicUpdateRequest(
                validMaxTransactionFee, validTransactionDuration, topicId, adminKey, null, null, null));
        Assertions.assertThrows(
                NullPointerException.class,
                () -> new TopicUpdateRequest(
                        validMaxTransactionFee, null, null, null, null, null, null)
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> new TopicUpdateRequest(
                        null, validTransactionDuration, null, null, null, null, null)
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> new TopicUpdateRequest(
                        null, null, topicId, null, null, null, null)
        );
        Assertions.assertThrows(
                NullPointerException.class,
                () -> new TopicUpdateRequest(
                        null, null, null, adminKey, null, null, null)
        );
    }

    @Test
    void testContractCallRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final String contractIdString = "0.0.12345";
        final ContractId contractId = ContractId.fromString(contractIdString);
        final String functionName = "functionName";
        final ContractParam<Long> contractParam = ContractParam.int32(1);
        final List<ContractParam<?>> constructorParams = List.of(contractParam);

        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractIdString, functionName));
        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractIdString, functionName, contractParam));
        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractIdString, functionName, constructorParams));
        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractId, functionName));
        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractId, functionName, contractParam));
        Assertions.assertDoesNotThrow(() -> ContractCallRequest.of(contractId, functionName, constructorParams));
        Assertions.assertDoesNotThrow(
                () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, functionName,
                        constructorParams));

        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of((String) null, functionName));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of(contractIdString, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCallRequest.of(contractIdString, functionName, (ContractParam<?>) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCallRequest.of(contractIdString, functionName, (List<ContractParam<?>>) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCallRequest.of((ContractId) null, functionName));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCallRequest.of(contractId, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCallRequest.of(contractId, functionName, (ContractParam<?>) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCallRequest.of(contractId, functionName, (List<ContractParam<?>>) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallRequest(null, transactionValidDuration, contractId, functionName,
                        constructorParams));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallRequest(maxTransactionFee, null, contractId, functionName, constructorParams));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, null, functionName,
                        constructorParams));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, null,
                        constructorParams));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, functionName,
                        null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCallRequest(Hbar.from(-100), transactionValidDuration, contractId, functionName,
                        constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCallRequest(maxTransactionFee, Duration.ZERO, contractId, functionName,
                        constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCallRequest(maxTransactionFee, Duration.ofSeconds(-1), contractId, functionName,
                        constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, "",
                        constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, "   ",
                        constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, " blankPrefix",
                        constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCallRequest(maxTransactionFee, transactionValidDuration, contractId, "blankSuffix ",
                        constructorParams));
    }

    @Test
    void testContractCallResultCreation() throws Exception {
        //given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;
        final byte[] transactionHash = new byte[]{};
        final Instant consensusTimestamp = Instant.now();
        final Hbar transactionFee = Hbar.fromTinybars(1000);
        final com.hedera.hashgraph.sdk.proto.ContractFunctionResult contractFunctionResultProto = com.hedera.hashgraph.sdk.proto.ContractFunctionResult.newBuilder()
                .build();
        final Constructor<ContractFunctionResult> constructor = ContractFunctionResult.class.getDeclaredConstructor(
                ContractFunctionResultOrBuilder.class);
        constructor.setAccessible(true);
        ContractFunctionResult contractFunctionResult = constructor.newInstance(contractFunctionResultProto);

        //then
        Assertions.assertDoesNotThrow(
                () -> new ContractCallResult(transactionId, status, transactionHash, consensusTimestamp, transactionFee,
                        contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallResult(null, status, transactionHash, consensusTimestamp, transactionFee,
                        contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallResult(transactionId, null, transactionHash, consensusTimestamp, transactionFee,
                        contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallResult(transactionId, status, null, consensusTimestamp, transactionFee,
                        contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallResult(transactionId, status, transactionHash, null, transactionFee,
                        contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallResult(transactionId, status, transactionHash, consensusTimestamp, null,
                        contractFunctionResult));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCallResult(transactionId, status, transactionHash, consensusTimestamp, transactionFee,
                        null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCallResult(transactionId, status, transactionHash, consensusTimestamp,
                        Hbar.fromTinybars(-1), contractFunctionResult));
    }

    @Test
    void testContractCreateRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final String fileIdString = "0.0.12345";
        final FileId fileId = FileId.fromString(fileIdString);
        final ContractParam<Long> contractParam = ContractParam.int32(1);
        final List<ContractParam<?>> constructorParams = List.of(contractParam);

        //then
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileIdString));
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileIdString, contractParam));
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileIdString, constructorParams));
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileId));
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileId, contractParam));
        Assertions.assertDoesNotThrow(() -> ContractCreateRequest.of(fileId, constructorParams));
        Assertions.assertDoesNotThrow(
                () -> new ContractCreateRequest(maxTransactionFee, transactionValidDuration, fileId,
                        constructorParams));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of((String) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCreateRequest.of((String) null, contractParam));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCreateRequest.of((String) null, constructorParams));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCreateRequest.of(fileIdString, (ContractParam<?>) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCreateRequest.of(fileIdString, (List<ContractParam<?>>) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractCreateRequest.of((FileId) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCreateRequest.of((FileId) null, contractParam));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCreateRequest.of((FileId) null, constructorParams));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCreateRequest.of(fileId, (ContractParam<?>) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> ContractCreateRequest.of(fileId, (List<ContractParam<?>>) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCreateRequest(null, transactionValidDuration, fileId, constructorParams));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCreateRequest(maxTransactionFee, null, fileId, constructorParams));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCreateRequest(maxTransactionFee, transactionValidDuration, null, constructorParams));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCreateRequest(maxTransactionFee, transactionValidDuration, fileId, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCreateRequest(Hbar.from(-100), transactionValidDuration, fileId, constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCreateRequest(maxTransactionFee, Duration.ZERO, fileId, constructorParams));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractCreateRequest(maxTransactionFee, Duration.ofSeconds(-1), fileId, constructorParams));
    }

    @Test
    void testContractCreateResultCreation() {
        //given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;
        final ContractId contractId = ContractId.fromString("0.0.12345");

        //then
        Assertions.assertDoesNotThrow(() -> new ContractCreateResult(transactionId, status, contractId));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractCreateResult(null, status, contractId));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCreateResult(transactionId, null, contractId));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractCreateResult(transactionId, status, null));
    }

    @Test
    void testContractDeleteRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final String contractIdAsString = "0.0.12345";
        final ContractId contractId = ContractId.fromString(contractIdAsString);
        final ContractId transferFeeToContractId = ContractId.fromString("0.0.54321");
        final AccountId transferFeeToAccountId = new AccountId(0, 0, 54321);

        //then
        Assertions.assertDoesNotThrow(() -> ContractDeleteRequest.of(contractIdAsString));
        Assertions.assertDoesNotThrow(() -> ContractDeleteRequest.of(contractId));
        Assertions.assertDoesNotThrow(
                () -> new ContractDeleteRequest(maxTransactionFee, transactionValidDuration, contractId,
                        transferFeeToContractId, transferFeeToAccountId));
        Assertions.assertDoesNotThrow(
                () -> new ContractDeleteRequest(maxTransactionFee, transactionValidDuration, contractId, null,
                        transferFeeToAccountId));
        Assertions.assertDoesNotThrow(
                () -> new ContractDeleteRequest(maxTransactionFee, transactionValidDuration, contractId,
                        transferFeeToContractId, null));
        Assertions.assertDoesNotThrow(
                () -> new ContractDeleteRequest(maxTransactionFee, transactionValidDuration, contractId, null, null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractDeleteRequest.of((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> ContractDeleteRequest.of((ContractId) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractDeleteRequest(null, transactionValidDuration, contractId, transferFeeToContractId,
                        transferFeeToAccountId));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractDeleteRequest(maxTransactionFee, null, contractId, transferFeeToContractId,
                        transferFeeToAccountId));
        Assertions.assertThrows(NullPointerException.class,
                () -> new ContractDeleteRequest(maxTransactionFee, transactionValidDuration, null,
                        transferFeeToContractId, transferFeeToAccountId));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractDeleteRequest(Hbar.from(-100), transactionValidDuration, contractId,
                        transferFeeToContractId, transferFeeToAccountId));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractDeleteRequest(maxTransactionFee, Duration.ZERO, contractId, transferFeeToContractId,
                        transferFeeToAccountId));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new ContractDeleteRequest(maxTransactionFee, Duration.ofSeconds(-1), contractId,
                        transferFeeToContractId, transferFeeToAccountId));
    }

    @Test
    void testContractDeleteResultCreation() {
        //given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;

        //then
        Assertions.assertDoesNotThrow(() -> new ContractDeleteResult(transactionId, status));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractDeleteResult(null, status));
        Assertions.assertThrows(NullPointerException.class, () -> new ContractDeleteResult(transactionId, null));
    }

    @Test
    void testFileAppendRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final String fileIdString = "0.0.12345";
        final FileId fileId = FileId.fromString(fileIdString);
        final byte[] contents = new byte[]{};
        final byte[] largeContents = IntStream.range(0, 2050).mapToObj(i -> "a").reduce("", (a, b) -> a + b).getBytes();
        final String fileMemo = "fileMemo";
        final String longFileMemo = IntStream.range(0, 102).mapToObj(i -> "a").reduce("", (a, b) -> a + b);

        //then
        Assertions.assertDoesNotThrow(() -> FileAppendRequest.of(fileIdString, contents));
        Assertions.assertDoesNotThrow(() -> FileAppendRequest.of(fileId, contents));
        Assertions.assertDoesNotThrow(
                () -> new FileAppendRequest(maxTransactionFee, transactionValidDuration, fileId, contents, fileMemo));
        Assertions.assertDoesNotThrow(
                () -> new FileAppendRequest(maxTransactionFee, transactionValidDuration, fileId, contents, null));
        Assertions.assertThrows(NullPointerException.class, () -> FileAppendRequest.of((String) null, contents));
        Assertions.assertThrows(NullPointerException.class, () -> FileAppendRequest.of(fileIdString, null));
        Assertions.assertThrows(NullPointerException.class, () -> FileAppendRequest.of((FileId) null, contents));
        Assertions.assertThrows(NullPointerException.class, () -> FileAppendRequest.of(fileId, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new FileAppendRequest(null, transactionValidDuration, fileId, contents, fileMemo));
        Assertions.assertThrows(NullPointerException.class,
                () -> new FileAppendRequest(maxTransactionFee, null, fileId, contents, fileMemo));
        Assertions.assertThrows(NullPointerException.class,
                () -> new FileAppendRequest(maxTransactionFee, transactionValidDuration, null, contents, fileMemo));
        Assertions.assertThrows(NullPointerException.class,
                () -> new FileAppendRequest(maxTransactionFee, transactionValidDuration, fileId, null, fileMemo));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FileAppendRequest(Hbar.fromTinybars(-100), transactionValidDuration, fileId, contents,
                        fileMemo));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FileAppendRequest(maxTransactionFee, Duration.ZERO, fileId, contents, fileMemo));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FileAppendRequest(maxTransactionFee, Duration.ofSeconds(-1), fileId, contents, fileMemo));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FileAppendRequest(maxTransactionFee, transactionValidDuration, fileId, largeContents,
                        fileMemo));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FileAppendRequest(maxTransactionFee, transactionValidDuration, fileId, largeContents,
                        longFileMemo));
    }

    @Test
    public void testTokenTransferResultCreation() {
        //given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;

        //then
        Assertions.assertDoesNotThrow(() -> new TokenTransferResult(transactionId, status));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenTransferResult(null, status));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenTransferResult(transactionId, null));
    }

    @Test
    public void testTokenMintResultCreation() {
        // Given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;
        final List<Long> serials = List.of(1L, 2L, 3L);
        final Long totalSupply = 1L;

        // Then
        Assertions.assertDoesNotThrow(() -> new TokenMintResult(transactionId, status, serials, totalSupply));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenMintResult(null, status, serials, totalSupply));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenMintResult(transactionId, null, serials, totalSupply));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenMintResult(transactionId, status, null, totalSupply));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenMintResult(transactionId, status, serials, null));
    }

    @Test
    public void testTokenCreateResultCreation() {
        //Given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;
        final TokenId tokenId = TokenId.fromString("0.0.12345");

        //Then
        Assertions.assertDoesNotThrow(() -> new TokenCreateResult(transactionId, status, tokenId));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenCreateResult(null, status, tokenId));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenCreateResult(transactionId, null, tokenId));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenCreateResult(transactionId, status, null));
    }

    @Test
    public void testTokenBurnResultCreation() {
        //Given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;
        final Long totalSupply = 1L;

        //Then
        Assertions.assertDoesNotThrow(() -> new TokenBurnResult(transactionId, status, totalSupply));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenBurnResult(null, status, totalSupply));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenBurnResult(transactionId, null, totalSupply));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenBurnResult(transactionId, status, null));
    }

    @Test
    public void testTokenAssociateResultCreation() {
        //Given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;

        //Then
        Assertions.assertDoesNotThrow(() -> new TokenAssociateResult(transactionId, status));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenAssociateResult(null, status));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenAssociateResult(transactionId, null));
    }

    @Test
    public void testTokenDissociateResultCreation() {
        //Given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;

        //Then
        Assertions.assertDoesNotThrow(() -> new TokenDissociateResult(transactionId, status));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenDissociateResult(null, status));
        Assertions.assertThrows(NullPointerException.class, () -> new TokenDissociateResult(transactionId, null));
    }

    @Test
    void testFileUpdateResultCreation() {
        //Given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;

        //Then
        Assertions.assertDoesNotThrow(() -> new FileUpdateResult(transactionId, status));
        Assertions.assertThrows(NullPointerException.class, () -> new FileUpdateResult(null, status));
        Assertions.assertThrows(NullPointerException.class, () -> new FileUpdateResult(transactionId, null));
    }

    @Test
    void testFileInfoResponseCreation() {
        //Given
        final FileId fileId = FileId.fromString("0.0.12345");
        final int size = 100;
        final boolean deleted = false;
        final Instant expirationTime = Instant.now().plus(Duration.ofDays(30));

        //Then
        Assertions.assertDoesNotThrow(() -> new FileInfoResponse(fileId, size, deleted, expirationTime));
    }

    @Test
    void testFileDeleteResultCreation() {
        //Given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;

        //Then
        Assertions.assertDoesNotThrow(() -> new FileDeleteResult(transactionId, status));
        Assertions.assertThrows(NullPointerException.class, () -> new FileDeleteResult(null, status));
        Assertions.assertThrows(NullPointerException.class, () -> new FileDeleteResult(transactionId, null));
    }

    @Test
    void testFileCreateResultCreation() {
        //Given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;
        final FileId fileId = FileId.fromString("0.0.12345");

        //Then
        Assertions.assertDoesNotThrow(() -> new FileCreateResult(transactionId, status, fileId));
        Assertions.assertThrows(NullPointerException.class, () -> new FileCreateResult(null, status, fileId));
        Assertions.assertThrows(NullPointerException.class, () -> new FileCreateResult(transactionId, null, fileId));
        Assertions.assertThrows(NullPointerException.class, () -> new FileCreateResult(transactionId, status, null));
    }

    @Test
    void testFileContentsResponseCreation() {
        //Given
        final FileId fileId = FileId.fromString("0.0.12345");
        final byte[] contents = new byte[]{1, 2, 3};

        //Then
        Assertions.assertDoesNotThrow(() -> new FileContentsResponse(fileId, contents));
        Assertions.assertThrows(NullPointerException.class, () -> new FileContentsResponse(null, contents));
        Assertions.assertThrows(NullPointerException.class, () -> new FileContentsResponse(fileId, null));
    }

    @Test
    void testFileAppendResultCreation() {
        //Given
        final TransactionId transactionId = TransactionId.generate(new AccountId(0, 0, 12345));
        final Status status = Status.SUCCESS;

        //Then
        Assertions.assertDoesNotThrow(() -> new FileAppendResult(transactionId, status));
        Assertions.assertThrows(NullPointerException.class, () -> new FileAppendResult(null, status));
        Assertions.assertThrows(NullPointerException.class, () -> new FileAppendResult(transactionId, null));
    }

    @Test
    void testTokenTransferRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(120);
        final TokenId tokenId = TokenId.fromString("0.0.1234");
        final List<Long> serials = List.of(1L, 2L);
        final AccountId sender = AccountId.fromString("0.0.5678");
        final AccountId receiver = AccountId.fromString("0.0.9876");
        final PrivateKey senderKey = PrivateKey.generateECDSA();
        final List<Long> emptySerials = List.of();
        final List<Long> negativeSerials = List.of(-1L);
        final Long amount = 1l;

        //then
        Assertions.assertDoesNotThrow(
                () -> new TokenTransferRequest(maxTransactionFee, transactionValidDuration, tokenId, emptySerials,
                        amount, sender,
                        receiver, senderKey));
        Assertions.assertDoesNotThrow(
                () -> new TokenTransferRequest(maxTransactionFee, transactionValidDuration, tokenId, serials, null,
                        sender,
                        receiver, senderKey));
        Assertions.assertDoesNotThrow(() -> TokenTransferRequest.of(tokenId, 1L, sender, receiver, senderKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenTransferRequest(null, transactionValidDuration, tokenId, serials, amount, sender,
                        receiver,
                        senderKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenTransferRequest(maxTransactionFee, null, tokenId, serials, null, sender, receiver,
                        senderKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenTransferRequest(maxTransactionFee, transactionValidDuration, null, serials, null, sender,
                        receiver, senderKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenTransferRequest(maxTransactionFee, transactionValidDuration, tokenId, null, amount,
                        sender,
                        receiver, senderKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenTransferRequest(maxTransactionFee, transactionValidDuration, tokenId, serials, null,
                        null,
                        receiver, senderKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenTransferRequest(maxTransactionFee, transactionValidDuration, tokenId, serials, null,
                        sender,
                        null, senderKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenTransferRequest(maxTransactionFee, transactionValidDuration, tokenId, serials, null,
                        sender,
                        receiver, null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new TokenTransferRequest(maxTransactionFee, transactionValidDuration, tokenId, emptySerials, null,
                        sender, receiver, senderKey));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new TokenTransferRequest(maxTransactionFee, transactionValidDuration, tokenId, negativeSerials,
                        null,
                        sender, receiver, senderKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenTransferRequest(maxTransactionFee, transactionValidDuration, tokenId, null, null,
                        sender, receiver, senderKey));
    }

    @Test
    void testTokenMintRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(120);
        final TokenId tokenId = TokenId.fromString("0.0.12345");
        final PrivateKey supplyKey = PrivateKey.generateECDSA();
        final Long amount = 10L;
        final List<byte[]> validMetadata = List.of("valid".getBytes(StandardCharsets.UTF_8));
        final byte[] largeMetadata = new byte[101];

        //then
        Assertions.assertDoesNotThrow(
                () -> new TokenMintRequest(maxTransactionFee, transactionValidDuration, tokenId, supplyKey, amount,
                        validMetadata));
        Assertions.assertDoesNotThrow(
                () -> new TokenMintRequest(maxTransactionFee, transactionValidDuration, tokenId, supplyKey, null,
                        validMetadata));
        Assertions.assertDoesNotThrow(
                () -> new TokenMintRequest(maxTransactionFee, transactionValidDuration, tokenId, supplyKey, amount,
                        List.of()));
        Assertions.assertDoesNotThrow(() -> TokenMintRequest.of(tokenId, supplyKey, "valid metadata"));
        Assertions.assertDoesNotThrow(() -> TokenMintRequest.of(tokenId, supplyKey, amount));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenMintRequest(maxTransactionFee, transactionValidDuration, null, supplyKey, amount,
                        validMetadata));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenMintRequest(maxTransactionFee, transactionValidDuration, tokenId, null, amount,
                        validMetadata));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenMintRequest(maxTransactionFee, transactionValidDuration, tokenId, supplyKey, amount,
                        null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new TokenMintRequest(maxTransactionFee, transactionValidDuration, tokenId, supplyKey, 0L,
                        validMetadata));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new TokenMintRequest(maxTransactionFee, transactionValidDuration, tokenId, supplyKey, null,
                        List.of()));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new TokenMintRequest(maxTransactionFee, transactionValidDuration, tokenId, supplyKey, null,
                        List.of(largeMetadata)));
    }

    @Test
    void testTokenCreateRequestCreation() {
        //Given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(120);
        final String name = "Token Name";
        final String symbol = "TKN";
        final AccountId treasuryAccountId = AccountId.fromString("0.0.12345");
        final PrivateKey treasuryKey = PrivateKey.generateECDSA();
        final PrivateKey supplyKey = PrivateKey.generateECDSA();
        final TokenType tokenType = TokenType.FUNGIBLE_COMMON;

        //Then
        Assertions.assertDoesNotThrow(
                () -> new TokenCreateRequest(maxTransactionFee, transactionValidDuration, name, symbol,
                        treasuryAccountId, treasuryKey, tokenType, supplyKey));
        Assertions.assertDoesNotThrow(() -> TokenCreateRequest.of(name, symbol, treasuryAccountId, treasuryKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenCreateRequest(null, transactionValidDuration, name, symbol, treasuryAccountId,
                        treasuryKey, tokenType, supplyKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenCreateRequest(maxTransactionFee, null, name, symbol, treasuryAccountId, treasuryKey,
                        tokenType, supplyKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenCreateRequest(maxTransactionFee, transactionValidDuration, null, symbol,
                        treasuryAccountId, treasuryKey, tokenType, supplyKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenCreateRequest(maxTransactionFee, transactionValidDuration, name, null, treasuryAccountId,
                        treasuryKey, tokenType, supplyKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenCreateRequest(maxTransactionFee, transactionValidDuration, name, symbol, null,
                        treasuryKey, tokenType, supplyKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenCreateRequest(maxTransactionFee, transactionValidDuration, name, symbol,
                        treasuryAccountId, treasuryKey, null, supplyKey));
    }

    @Test
    void testTokenBurnRequestCreation() {
        //Given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(120);
        final TokenId tokenId = TokenId.fromString("0.0.12345");
        final PrivateKey supplyKey = PrivateKey.generateECDSA();
        final Long amount = 100L;
        final Set<Long> serials = Set.of(1L, 2L, 3L);

        //Then
        Assertions.assertDoesNotThrow(
                () -> new TokenBurnRequest(maxTransactionFee, transactionValidDuration, tokenId, supplyKey, amount,
                        serials));
        Assertions.assertDoesNotThrow(() -> TokenBurnRequest.of(tokenId, 1L, supplyKey));
        Assertions.assertDoesNotThrow(() -> TokenBurnRequest.of(tokenId, serials, supplyKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenBurnRequest(null, transactionValidDuration, tokenId, supplyKey, amount, serials));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenBurnRequest(maxTransactionFee, null, tokenId, supplyKey, amount, serials));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenBurnRequest(maxTransactionFee, transactionValidDuration, null, supplyKey, amount,
                        serials));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenBurnRequest(maxTransactionFee, transactionValidDuration, tokenId, null, amount,
                        serials));
        Assertions.assertDoesNotThrow(
                () -> new TokenBurnRequest(maxTransactionFee, transactionValidDuration, tokenId, supplyKey, null,
                        serials));
        Assertions.assertDoesNotThrow(
                () -> new TokenBurnRequest(maxTransactionFee, transactionValidDuration, tokenId, supplyKey, amount,
                        null));
    }

    @Test
    void testTokenAssociateRequestCreation() {
        //Given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(120);
        final TokenId tokenId = TokenId.fromString("0.0.12345");
        final AccountId accountId = AccountId.fromString("0.0.54321");
        final PrivateKey accountPrivateKey = PrivateKey.generateECDSA();

        //Then
        Assertions.assertDoesNotThrow(
                () -> new TokenAssociateRequest(maxTransactionFee, transactionValidDuration, tokenId, accountId,
                        accountPrivateKey));
        Assertions.assertDoesNotThrow(() -> TokenAssociateRequest.of(tokenId, accountId, accountPrivateKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenAssociateRequest(maxTransactionFee, transactionValidDuration, null, accountId,
                        accountPrivateKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenAssociateRequest(maxTransactionFee, transactionValidDuration, tokenId, null,
                        accountPrivateKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenAssociateRequest(maxTransactionFee, transactionValidDuration, tokenId, accountId, null));
    }

    @Test
    void testTokenDissociateRequestCreation() {
        //Given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(120);
        final TokenId tokenId = TokenId.fromString("0.0.1");
        final List<TokenId> tokenIds = List.of(TokenId.fromString("0.0.12345"));
        final AccountId accountId = AccountId.fromString("0.0.54321");
        final PrivateKey accountPrivateKey = PrivateKey.generateECDSA();

        //Then
        Assertions.assertDoesNotThrow(
                () -> new TokenDissociateRequest(maxTransactionFee, transactionValidDuration, tokenIds, accountId,
                        accountPrivateKey));
        Assertions.assertDoesNotThrow(() -> TokenDissociateRequest.of(tokenIds, accountId, accountPrivateKey));
        Assertions.assertDoesNotThrow(() -> TokenDissociateRequest.of(tokenId, accountId, accountPrivateKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenDissociateRequest(maxTransactionFee, transactionValidDuration, null, accountId,
                        accountPrivateKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenDissociateRequest(maxTransactionFee, transactionValidDuration, tokenIds, null,
                        accountPrivateKey));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TokenDissociateRequest(maxTransactionFee, transactionValidDuration, tokenIds, accountId, null));
    }

    @Test
    void testFileUpdateRequestCreation() {
        //Given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(120);
        final FileId fileId = FileId.fromString("0.0.12345");
        final byte[] contents = new byte[100];
        final Instant expirationTime = Instant.now().plus(Duration.ofDays(1));
        final String fileMemo = "Test file memo";

        //Then
        Assertions.assertDoesNotThrow(
                () -> new FileUpdateRequest(maxTransactionFee, transactionValidDuration, fileId, contents,
                        expirationTime, fileMemo));
        Assertions.assertDoesNotThrow(
                () -> new FileUpdateRequest(maxTransactionFee, transactionValidDuration, fileId, null, expirationTime,
                        fileMemo));
        Assertions.assertDoesNotThrow(
                () -> new FileUpdateRequest(maxTransactionFee, transactionValidDuration, fileId, contents, null,
                        fileMemo));
        Assertions.assertDoesNotThrow(() -> FileUpdateRequest.of(fileId, contents));
        Assertions.assertDoesNotThrow(() -> FileUpdateRequest.of(fileId, expirationTime));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> new FileUpdateRequest(maxTransactionFee, transactionValidDuration, fileId,
                        new byte[FileUpdateRequest.FILE_CREATE_MAX_BYTES + 1], expirationTime, fileMemo));
    }

    @Test
    void testFileInfoRequestCreation() {
        //Given
        final FileId validFileId = FileId.fromString("0.0.12345");
        final Hbar queryPayment = Hbar.fromTinybars(1000);
        final Hbar maxQueryPayment = Hbar.fromTinybars(2000);

        //Then
        Assertions.assertDoesNotThrow(() -> new FileInfoRequest(validFileId, null, null));
        Assertions.assertDoesNotThrow(() -> new FileInfoRequest(validFileId, queryPayment, null));
        Assertions.assertDoesNotThrow(() -> new FileInfoRequest(validFileId, null, maxQueryPayment));
        Assertions.assertDoesNotThrow(() -> new FileInfoRequest(validFileId, queryPayment, maxQueryPayment));
        Assertions.assertDoesNotThrow(() -> FileInfoRequest.of(validFileId));
        Assertions.assertDoesNotThrow(() -> FileInfoRequest.of("0.0.12345"));
        Assertions.assertThrows(NullPointerException.class,
                () -> new FileInfoRequest(null, queryPayment, maxQueryPayment));
        Assertions.assertThrows(NullPointerException.class, () -> FileInfoRequest.of((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> FileInfoRequest.of((FileId) null));
    }

    @Test
    void testFileContentsRequestCreation() {
        //Given
        final FileId validFileId = FileId.fromString("0.0.12345");
        final Hbar queryPayment = Hbar.fromTinybars(1000);
        final Hbar maxQueryPayment = Hbar.fromTinybars(2000);

        //Then
        Assertions.assertDoesNotThrow(() -> new FileContentsRequest(validFileId, null, null));
        Assertions.assertDoesNotThrow(() -> new FileContentsRequest(validFileId, queryPayment, null));
        Assertions.assertDoesNotThrow(() -> new FileContentsRequest(validFileId, null, maxQueryPayment));
        Assertions.assertDoesNotThrow(() -> new FileContentsRequest(validFileId, queryPayment, maxQueryPayment));
        Assertions.assertThrows(NullPointerException.class,
                () -> new FileContentsRequest(null, queryPayment, maxQueryPayment));
        Assertions.assertDoesNotThrow(() -> FileContentsRequest.of(validFileId));
        Assertions.assertDoesNotThrow(() -> FileContentsRequest.of("0.0.12345"));
        Assertions.assertThrows(NullPointerException.class, () -> FileContentsRequest.of((String) null));
        Assertions.assertThrows(NullPointerException.class, () -> FileContentsRequest.of((FileId) null));
    }

    @Test
    void testFileCreateRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final byte[] contents = new byte[]{};
        final Instant expirationTime = Instant.MAX;
        final byte[] largeContents = IntStream.range(0, 2050).mapToObj(i -> "a").reduce("", (a, b) -> a + b).getBytes();
        final String fileMemo = "fileMemo";

        //then
        Assertions.assertDoesNotThrow(() -> FileCreateRequest.of(contents));
        Assertions.assertDoesNotThrow(() -> FileCreateRequest.of(contents, null));
        Assertions.assertDoesNotThrow(
                () -> new FileCreateRequest(maxTransactionFee, transactionValidDuration, contents, expirationTime,
                        fileMemo));
        Assertions.assertDoesNotThrow(
                () -> new FileCreateRequest(maxTransactionFee, transactionValidDuration, contents, null, fileMemo));
        Assertions.assertDoesNotThrow(
                () -> new FileCreateRequest(maxTransactionFee, transactionValidDuration, contents, expirationTime,
                        null));
        Assertions.assertDoesNotThrow(
                () -> new FileCreateRequest(maxTransactionFee, transactionValidDuration, contents, null, null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> FileCreateRequest.of(largeContents));
        Assertions.assertThrows(NullPointerException.class, () -> FileCreateRequest.of(null));
        Assertions.assertThrows(NullPointerException.class, () -> FileCreateRequest.of(null, expirationTime));
        Assertions.assertThrows(NullPointerException.class, () -> FileCreateRequest.of(null, null));
        Assertions.assertThrows(NullPointerException.class, () -> new FileCreateRequest(null, null, null, null, null));
    }

    @Test
    void testFileDeleteRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final String fileIdString = "0.0.12345";
        final FileId fileId = FileId.fromString(fileIdString);

        //then
        Assertions.assertDoesNotThrow(() -> FileDeleteRequest.of(fileId));
        Assertions.assertDoesNotThrow(() -> FileDeleteRequest.of(fileIdString));
        Assertions.assertDoesNotThrow(() -> new FileDeleteRequest(maxTransactionFee, transactionValidDuration, fileId));
        Assertions.assertThrows(NullPointerException.class, () -> FileDeleteRequest.of((FileId) null));
        Assertions.assertThrows(NullPointerException.class, () -> FileDeleteRequest.of((String) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new FileDeleteRequest(maxTransactionFee, transactionValidDuration, null));
        Assertions.assertThrows(NullPointerException.class, () -> new FileDeleteRequest(maxTransactionFee, null, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new FileDeleteRequest(null, transactionValidDuration, null));
        Assertions.assertThrows(NullPointerException.class, () -> new FileDeleteRequest(null, null, null));
    }

    @Test
    void testTopicSubmitMessageResultCreation() {
        //given
        final TransactionId validTransactionId = TransactionId.fromString("0.0.123451@1697590800.123456789");
        final Status validStatus = Status.SUCCESS;

        //then
        Assertions.assertDoesNotThrow(() -> new TopicSubmitMessageResult(validTransactionId, validStatus));
        Assertions.assertThrows(NullPointerException.class, () -> new TopicSubmitMessageResult(null, validStatus));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicSubmitMessageResult(validTransactionId, null));
    }

    @Test
    void testTopicDeleteResultCreation() {
        //given
        final TransactionId validTransactionId = TransactionId.fromString("0.0.123451@1697590800.123456789");
        final Status validStatus = Status.SUCCESS;

        //then
        Assertions.assertDoesNotThrow(() -> new TopicDeleteResult(validTransactionId, validStatus));
        Assertions.assertThrows(NullPointerException.class, () -> new TopicDeleteResult(null, validStatus));
        Assertions.assertThrows(NullPointerException.class, () -> new TopicDeleteResult(validTransactionId, null));
    }

    @Test
    void testTopicSubmitMessageRequestCreation() {
        // Given
        final TopicId validTopicId = TopicId.fromString("0.0.12345");
        final String validMessage = "This is a valid message";
        final byte[] validMessageBytes = validMessage.getBytes(StandardCharsets.UTF_8);
        final byte[] largeMessage = new byte[1025];
        final Hbar validMaxTransactionFee = Hbar.fromTinybars(100_000);
        final Duration validTransactionValidDuration = Duration.ofMinutes(2);

        // Then
        Assertions.assertDoesNotThrow(() -> TopicSubmitMessageRequest.of(validTopicId, validMessage));
        Assertions.assertDoesNotThrow(() -> TopicSubmitMessageRequest.of(validTopicId, validMessageBytes));
        Assertions.assertDoesNotThrow(
                () -> new TopicSubmitMessageRequest(validMaxTransactionFee, validTransactionValidDuration, validTopicId, null,
                        validMessage.getBytes(StandardCharsets.UTF_8)));
        Assertions.assertThrows(NullPointerException.class, () -> TopicSubmitMessageRequest.of(null, validMessage));
        Assertions.assertThrows(NullPointerException.class,
                () -> TopicSubmitMessageRequest.of(validTopicId, (String) null));
        Assertions.assertThrows(NullPointerException.class,
                () -> TopicSubmitMessageRequest.of(validTopicId, (byte[]) null));
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> TopicSubmitMessageRequest.of(validTopicId, largeMessage));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicSubmitMessageRequest(validMaxTransactionFee, validTransactionValidDuration, null,
                       null , validMessage.getBytes(StandardCharsets.UTF_8)));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicSubmitMessageRequest(validMaxTransactionFee, validTransactionValidDuration, validTopicId,
                        null, null));
    }

    @Test
    void testTopicDeleteRequestCreation() {
        //given
        final Hbar maxTransactionFee = Hbar.fromTinybars(1000);
        final Duration transactionValidDuration = Duration.ofSeconds(10);
        final String topicIdString = "0.0.12345";
        final TopicId topicId = TopicId.fromString(topicIdString);
        final PrivateKey adminKey = PrivateKey.generateECDSA();

        //then
        Assertions.assertDoesNotThrow(() -> TopicDeleteRequest.of(adminKey,topicId));
        Assertions.assertDoesNotThrow(() -> new TopicDeleteRequest(maxTransactionFee, transactionValidDuration, adminKey, topicId));

        Assertions.assertThrows(NullPointerException.class, () -> TopicDeleteRequest.of(null, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicDeleteRequest(maxTransactionFee, null, null, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicDeleteRequest(null, transactionValidDuration, null, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicDeleteRequest(null, null, adminKey, null));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicDeleteRequest(null, null, null, topicId));
    }

    @Test
    void testTopicCreateResultCreation() {
        //given
        final TransactionId validTransactionId = TransactionId.fromString("0.0.123451@1697590800.123456789");
        final Status validStatus = Status.SUCCESS;
        final TopicId validTopicId = TopicId.fromString("0.0.12345");

        //then
        Assertions.assertDoesNotThrow(() -> new TopicCreateResult(validTransactionId, validStatus, validTopicId));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicCreateResult(null, validStatus, validTopicId));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicCreateResult(validTransactionId, null, validTopicId));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicCreateResult(validTransactionId, validStatus, null));
    }

    @Test
    void testTopicUpdateResultCreation() {
        //given
        final TransactionId validTransactionId = TransactionId.fromString("0.0.123451@1697590800.123456789");
        final Status validStatus = Status.SUCCESS;

        //then
        Assertions.assertDoesNotThrow(() -> new TopicUpdateResult(validTransactionId, validStatus));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicUpdateResult(null, validStatus));
        Assertions.assertThrows(NullPointerException.class,
                () -> new TopicUpdateResult(validTransactionId, null));
    }
}
