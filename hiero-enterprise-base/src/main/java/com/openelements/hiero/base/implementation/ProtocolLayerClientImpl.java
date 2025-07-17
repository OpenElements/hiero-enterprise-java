package com.openelements.hiero.base.implementation;

import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.AccountBalance;
import com.hedera.hashgraph.sdk.AccountBalanceQuery;
import com.hedera.hashgraph.sdk.AccountCreateTransaction;
import com.hedera.hashgraph.sdk.AccountDeleteTransaction;
import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractCreateTransaction;
import com.hedera.hashgraph.sdk.ContractDeleteTransaction;
import com.hedera.hashgraph.sdk.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.ContractFunctionParameters;
import com.hedera.hashgraph.sdk.FileAppendTransaction;
import com.hedera.hashgraph.sdk.FileContentsQuery;
import com.hedera.hashgraph.sdk.FileCreateTransaction;
import com.hedera.hashgraph.sdk.FileDeleteTransaction;
import com.hedera.hashgraph.sdk.FileInfo;
import com.hedera.hashgraph.sdk.FileInfoQuery;
import com.hedera.hashgraph.sdk.FileUpdateTransaction;
import com.hedera.hashgraph.sdk.NftId;
import com.hedera.hashgraph.sdk.PrivateKey;
import com.hedera.hashgraph.sdk.PublicKey;
import com.hedera.hashgraph.sdk.Query;
import com.hedera.hashgraph.sdk.SubscriptionHandle;
import com.hedera.hashgraph.sdk.TokenAssociateTransaction;
import com.hedera.hashgraph.sdk.TokenBurnTransaction;
import com.hedera.hashgraph.sdk.TokenCreateTransaction;
import com.hedera.hashgraph.sdk.TokenDissociateTransaction;
import com.hedera.hashgraph.sdk.TokenMintTransaction;
import com.hedera.hashgraph.sdk.TopicCreateTransaction;
import com.hedera.hashgraph.sdk.TopicDeleteTransaction;
import com.hedera.hashgraph.sdk.TopicMessageQuery;
import com.hedera.hashgraph.sdk.TopicMessageSubmitTransaction;
import com.hedera.hashgraph.sdk.TopicUpdateTransaction;
import com.hedera.hashgraph.sdk.Transaction;
import com.hedera.hashgraph.sdk.TransactionReceipt;
import com.hedera.hashgraph.sdk.TransactionRecord;
import com.hedera.hashgraph.sdk.TransactionResponse;
import com.hedera.hashgraph.sdk.TransferTransaction;
import com.openelements.hiero.base.HieroContext;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.data.ContractParam;
import com.openelements.hiero.base.interceptors.ReceiveRecordInterceptor;
import com.openelements.hiero.base.interceptors.ReceiveRecordInterceptor.ReceiveRecordHandler;
import com.openelements.hiero.base.protocol.ProtocolLayerClient;
import com.openelements.hiero.base.protocol.TransactionListener;
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
import com.openelements.hiero.base.protocol.data.FileAppendResult;
import com.openelements.hiero.base.protocol.data.FileContentsRequest;
import com.openelements.hiero.base.protocol.data.FileContentsResponse;
import com.openelements.hiero.base.protocol.data.FileCreateRequest;
import com.openelements.hiero.base.protocol.data.FileCreateResult;
import com.openelements.hiero.base.protocol.data.FileDeleteRequest;
import com.openelements.hiero.base.protocol.data.FileDeleteResult;
import com.openelements.hiero.base.protocol.data.FileInfoRequest;
import com.openelements.hiero.base.protocol.data.FileInfoResponse;
import com.openelements.hiero.base.protocol.data.FileUpdateRequest;
import com.openelements.hiero.base.protocol.data.FileUpdateResult;
import com.openelements.hiero.base.protocol.data.TokenAssociateRequest;
import com.openelements.hiero.base.protocol.data.TokenAssociateResult;
import com.openelements.hiero.base.protocol.data.TokenBurnRequest;
import com.openelements.hiero.base.protocol.data.TokenBurnResult;
import com.openelements.hiero.base.protocol.data.TokenCreateRequest;
import com.openelements.hiero.base.protocol.data.TokenCreateResult;
import com.openelements.hiero.base.protocol.data.TokenDissociateRequest;
import com.openelements.hiero.base.protocol.data.TokenDissociateResult;
import com.openelements.hiero.base.protocol.data.TokenMintRequest;
import com.openelements.hiero.base.protocol.data.TokenMintResult;
import com.openelements.hiero.base.protocol.data.TokenTransferRequest;
import com.openelements.hiero.base.protocol.data.TokenTransferResult;
import com.openelements.hiero.base.protocol.data.TopicCreateRequest;
import com.openelements.hiero.base.protocol.data.TopicCreateResult;
import com.openelements.hiero.base.protocol.data.TopicDeleteRequest;
import com.openelements.hiero.base.protocol.data.TopicDeleteResult;
import com.openelements.hiero.base.protocol.data.TopicMessageRequest;
import com.openelements.hiero.base.protocol.data.TopicMessageResult;
import com.openelements.hiero.base.protocol.data.TopicSubmitMessageRequest;
import com.openelements.hiero.base.protocol.data.TopicSubmitMessageResult;
import com.openelements.hiero.base.protocol.data.TopicUpdateRequest;
import com.openelements.hiero.base.protocol.data.TopicUpdateResult;
import com.openelements.hiero.base.protocol.data.TransactionType;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtocolLayerClientImpl implements ProtocolLayerClient {

    private static final Logger log = LoggerFactory.getLogger(ProtocolLayerClientImpl.class);

    public static final int DEFAULT_GAS = 9_000_000;

    private final List<TransactionListener> listeners;

    private final HieroContext hieroContext;

    private final AtomicReference<ReceiveRecordInterceptor> recordInterceptor = new AtomicReference<>(
            ReceiveRecordInterceptor.DEFAULT_INTERCEPTOR);

    public ProtocolLayerClientImpl(@NonNull final HieroContext hieroContext) {
        this.hieroContext = Objects.requireNonNull(hieroContext, "hieroContext must not be null");
        listeners = new CopyOnWriteArrayList<>();
    }

    public void setRecordInterceptor(
            @NonNull final ReceiveRecordInterceptor recordInterceptor) {
        Objects.requireNonNull(recordInterceptor, "recordInterceptor must not be null");
        this.recordInterceptor.set(recordInterceptor);
    }

    @Override
    public AccountBalanceResponse executeAccountBalanceQuery(@NonNull final AccountBalanceRequest request)
            throws HieroException {
        final AccountBalanceQuery query = new AccountBalanceQuery().setAccountId(request.accountId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final AccountBalance balance = executeQueryAndWait(query);
        return new AccountBalanceResponse(balance.hbars);
    }

    @Override
    public FileContentsResponse executeFileContentsQuery(@NonNull final FileContentsRequest request)
            throws HieroException {
        final FileContentsQuery query = new FileContentsQuery().setFileId(request.fileId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final ByteString byteString = executeQueryAndWait(query);
        final byte[] bytes = byteString.toByteArray();
        return new FileContentsResponse(request.fileId(), bytes);
    }

    @Override
    public FileInfoResponse executeFileInfoQuery(@NonNull final FileInfoRequest request) throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        final FileInfoQuery query = new FileInfoQuery().setFileId(request.fileId())
                .setQueryPayment(request.queryPayment())
                .setMaxQueryPayment(request.maxQueryPayment());
        final FileInfo fileInfo = executeQueryAndWait(query);
        if (fileInfo.size > Integer.MAX_VALUE) {
            throw new HieroException("File size is too large to be represented as an integer");
        }
        return new FileInfoResponse(request.fileId(), (int) fileInfo.size, fileInfo.isDeleted, fileInfo.expirationTime);
    }

    @Override
    public FileCreateResult executeFileCreateTransaction(@NonNull final FileCreateRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(request.contents(), "content must not be null");
        if (request.contents().length > FileCreateRequest.FILE_CREATE_MAX_SIZE) {
            throw new HieroException(
                    "File contents of 1 transaction must be less than " + FileCreateRequest.FILE_CREATE_MAX_SIZE
                            + " bytes. Use FileAppend for larger files.");
        }
        final FileCreateTransaction transaction = new FileCreateTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setContents(request.contents())
                .setTransactionMemo(request.fileMemo())
                .setKeys(Objects.requireNonNull(hieroContext.getOperatorAccount().publicKey()));
        if (request.expirationTime() != null) {
            transaction.setExpirationTime(request.expirationTime());
        }

        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileCreateResult(receipt.transactionId, receipt.status, receipt.fileId);
    }

    @Override
    public FileUpdateResult executeFileUpdateRequestTransaction(@NonNull final FileUpdateRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        if (request.contents() != null && request.contents().length > FileCreateRequest.FILE_CREATE_MAX_SIZE) {
            throw new HieroException(
                    "File contents of 1 transaction must be less than " + FileCreateRequest.FILE_CREATE_MAX_SIZE
                            + " bytes. Use FileAppend for larger files.");
        }
        final FileUpdateTransaction transaction = new FileUpdateTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setFileId(request.fileId())
                .setTransactionMemo(request.fileMemo());
        if (request.contents() != null) {
            transaction.setContents(request.contents());
        }
        if (request.expirationTime() != null) {
            transaction.setExpirationTime(request.expirationTime());
        }
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileUpdateResult(receipt.transactionId, receipt.status);
    }

    @Override
    public FileAppendResult executeFileAppendRequestTransaction(@NonNull final FileAppendRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(request.contents(), "content must not be null");
        if (request.contents().length > FileCreateRequest.FILE_CREATE_MAX_SIZE) {
            throw new HieroException(
                    "File contents of 1 transaction must be less than " + FileCreateRequest.FILE_CREATE_MAX_SIZE
                            + " bytes. Use multiple FileAppend for larger files.");
        }
        final FileAppendTransaction transaction = new FileAppendTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setFileId(request.fileId())
                .setContents(request.contents())
                .setTransactionMemo(request.fileMemo());

        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileAppendResult(receipt.transactionId, receipt.status);
    }

    @Override
    public FileDeleteResult executeFileDeleteTransaction(@NonNull final FileDeleteRequest request)
            throws HieroException {
        final FileDeleteTransaction transaction = new FileDeleteTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setFileId(request.fileId());
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new FileDeleteResult(receipt.transactionId, receipt.status);
    }

    @Override
    public ContractCreateResult executeContractCreateTransaction(@NonNull final ContractCreateRequest request)
            throws HieroException {
        final ContractFunctionParameters constructorParams = createParameters(request.constructorParams());
        final ContractCreateTransaction transaction = new ContractCreateTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setBytecodeFileId(request.fileId())
                .setGas(DEFAULT_GAS)
                .setConstructorParameters(constructorParams);
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new ContractCreateResult(receipt.transactionId, receipt.status, receipt.contractId);
    }

    @Override
    public ContractDeleteResult executeContractDeleteTransaction(@NonNull final ContractDeleteRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        final ContractDeleteTransaction transaction = new ContractDeleteTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setContractId(request.contractId());
        if (request.transferFeeToContractId() != null) {
            transaction.setTransferContractId(request.transferFeeToContractId());
        }
        if (request.transferFeeToAccountId() != null) {
            transaction.setTransferAccountId(request.transferFeeToAccountId());
        }
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        return new ContractDeleteResult(receipt.transactionId, receipt.status);
    }

    @Override
    @NonNull
    public ContractCallResult executeContractCallTransaction(@NonNull final ContractCallRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        final ContractFunctionParameters functionParams = createParameters(request.constructorParams());
        final ContractExecuteTransaction transaction = new ContractExecuteTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setContractId(request.contractId())
                .setFunction(request.functionName(), functionParams)
                .setGas(DEFAULT_GAS);
        final TransactionRecord record = executeTransactionAndWaitOnRecord(transaction);
        return new ContractCallResult(record.transactionId, record.receipt.status, record.transactionHash.toByteArray(),
                record.consensusTimestamp, record.transactionFee, record.contractFunctionResult);
    }

    @Override
    @NonNull
    public AccountCreateResult executeAccountCreateTransaction(@NonNull final AccountCreateRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        final PrivateKey privateKey = PrivateKey.generateED25519();
        final PublicKey publicKey = privateKey.getPublicKey();
        final AccountCreateTransaction transaction = new AccountCreateTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setKey(publicKey)
                .setInitialBalance(request.initialBalance());
        final TransactionRecord record = executeTransactionAndWaitOnRecord(transaction);
        final Account newAccount = Account.of(record.receipt.accountId, publicKey, privateKey);
        return new AccountCreateResult(record.transactionId, record.receipt.status,
                record.transactionHash.toByteArray(), record.consensusTimestamp, record.transactionFee, newAccount);
    }

    @Override
    @NonNull
    public AccountDeleteResult executeAccountDeleteTransaction(@NonNull final AccountDeleteRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        final AccountDeleteTransaction transaction = new AccountDeleteTransaction()
                .setMaxTransactionFee(request.maxTransactionFee())
                .setTransactionValidDuration(request.transactionValidDuration())
                .setAccountId(request.toDelete().accountId());
        if (request.transferFoundsToAccount() != null) {
            transaction.setTransferAccountId(request.transferFoundsToAccount().accountId());
            sign(transaction, request.toDelete().privateKey(), request.transferFoundsToAccount().privateKey());
        } else {
            transaction.setTransferAccountId(hieroContext.getOperatorAccount().accountId());
            sign(transaction, request.toDelete().privateKey(), hieroContext.getOperatorAccount().privateKey());
        }
        final TransactionRecord record = executeTransactionAndWaitOnRecord(transaction);
        return new AccountDeleteResult(record.transactionId, record.receipt.status,
                record.transactionHash.toByteArray(), record.consensusTimestamp, record.transactionFee);
    }

    public TopicCreateResult executeTopicCreateTransaction(@NonNull final TopicCreateRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(request.maxTransactionFee(), "maxTransactionFee must not be null");
        Objects.requireNonNull(request.transactionValidDuration(), "transactionValidDuration must not be null");
        try {
            final TopicCreateTransaction transaction = new TopicCreateTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setAdminKey(request.adminKey());
            if (request.memo() != null) {
                transaction.setTopicMemo(request.memo());
            }
            if (request.submitKey() != null) {
                transaction.setSubmitKey(request.submitKey());
            }
            sign(transaction, request.adminKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TopicCreateResult(receipt.transactionId, receipt.status, receipt.topicId);
        } catch (final Exception e) {
            throw new HieroException("Failed to execute create topic transaction", e);
        }
    }

    @Override
    public @NonNull TopicUpdateResult executeTopicUpdateTransaction(@NonNull TopicUpdateRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        Objects.requireNonNull(request.maxTransactionFee(), "maxTransactionFee must not be null");
        Objects.requireNonNull(request.transactionValidDuration(), "transactionValidDuration must not be null");
        try {
            final TopicUpdateTransaction transaction = new TopicUpdateTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTopicId(request.topicId());
            if (request.memo() != null) {
                transaction.setTopicMemo(request.memo());
            }
            if (request.submitKey() != null) {
                transaction.setSubmitKey(request.submitKey());
            }
            if (request.updatedAdminKey() != null) {
                transaction.setAdminKey(request.updatedAdminKey());
            }
            if (request.updatedAdminKey() != null) {
                sign(transaction, request.adminKey(), request.updatedAdminKey());
            } else {
                sign(transaction, request.adminKey());
            }
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TopicUpdateResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HieroException("Failed to execute update topic transaction", e);
        }
    }

    public TopicDeleteResult executeTopicDeleteTransaction(@NonNull final TopicDeleteRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TopicDeleteTransaction transaction = new TopicDeleteTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTopicId(request.topicId());
            sign(transaction, request.adminKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TopicDeleteResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HieroException("Failed to execute delete topic transaction", e);
        }
    }

    public TopicSubmitMessageResult executeTopicMessageSubmitTransaction(
            @NonNull final TopicSubmitMessageRequest request) throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TopicMessageSubmitTransaction transaction = new TopicMessageSubmitTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTopicId(request.topicId())
                    .setMessage(request.message());
            if (request.submitKey() != null) {
                sign(transaction, request.submitKey());
            }
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TopicSubmitMessageResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HieroException("Failed to execute submit message transaction", e);
        }
    }

    @Override
    public TopicMessageResult executeTopicMessageQuery(TopicMessageRequest request) throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TopicMessageQuery query = new TopicMessageQuery()
                    .setTopicId(request.topicId());
            if (request.startTime() != null) {
                query.setStartTime(request.startTime());
            }
            if (request.endTime() != null) {
                query.setEndTime(request.endTime());
            }
            if (request.limit() >= 0) {
                query.setLimit(request.limit());
            }
            final SubscriptionHandle subscribe = query.subscribe(hieroContext.getClient(), request.subscription());
            return new TopicMessageResult();
        } catch (final Exception e) {
            throw new HieroException("Failed to execute query message transaction", e);
        }
    }

    public TokenCreateResult executeTokenCreateTransaction(@NonNull final TokenCreateRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TokenCreateTransaction transaction = new TokenCreateTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTokenName(request.name())
                    .setTokenSymbol(request.symbol())
                    .setTreasuryAccountId(request.treasuryAccountId())
                    .setTokenType(request.tokenType())
                    .setSupplyKey(request.supplyKey());
            sign(transaction, request.treasuryKey(), request.supplyKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenCreateResult(receipt.transactionId, receipt.status, receipt.tokenId);
        } catch (final Exception e) {
            throw new HieroException("Failed to execute create token transaction", e);
        }
    }

    public TokenAssociateResult executeTokenAssociateTransaction(@NonNull final TokenAssociateRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TokenAssociateTransaction transaction = new TokenAssociateTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTokenIds(request.tokenIds())
                    .setAccountId(request.accountId());
            sign(transaction, request.accountPrivateKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenAssociateResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HieroException("Failed to execute associate token transaction", e);
        }
    }

    @Override
    public @NonNull TokenDissociateResult executeTokenDissociateTransaction(@NonNull TokenDissociateRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TokenDissociateTransaction transaction = new TokenDissociateTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setAccountId(request.accountId())
                    .setTokenIds(request.tokenIds());
            sign(transaction, request.accountKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenDissociateResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HieroException("Failed to execute dissociate token transaction", e);
        }
    }

    public TokenBurnResult executeBurnTokenTransaction(@NonNull final TokenBurnRequest request) throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TokenBurnTransaction transaction = new TokenBurnTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTokenId(request.tokenId());
            if (request.amount() != null) {
                transaction.setAmount(request.amount());
            } else if (request.serials() != null) {
                transaction.setSerials(List.copyOf(request.serials()));
            } else {
                throw new IllegalArgumentException("either amount or serial must be provided");
            }

            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenBurnResult(receipt.transactionId, receipt.status, receipt.totalSupply);
        } catch (final Exception e) {
            throw new HieroException("Failed to execute burn token transaction", e);
        }
    }

    public TokenMintResult executeMintTokenTransaction(@NonNull final TokenMintRequest request) throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TokenMintTransaction transaction = new TokenMintTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration())
                    .setTokenId(request.tokenId());
            if (request.amount() != null) {
                transaction.setAmount(request.amount());
            } else if (request.metadata() != null) {
                transaction.setMetadata(request.metadata());
            } else {
                throw new IllegalArgumentException("either amount or metadata must be provided");
            }
            sign(transaction, request.supplyKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenMintResult(receipt.transactionId, receipt.status, receipt.serials, receipt.totalSupply);
        } catch (final Exception e) {
            throw new HieroException("Failed to execute mint token transaction", e);
        }
    }

    public TokenTransferResult executeTransferTransaction(@NonNull final TokenTransferRequest request)
            throws HieroException {
        Objects.requireNonNull(request, "request must not be null");
        try {
            final TransferTransaction transaction = new TransferTransaction()
                    .setMaxTransactionFee(request.maxTransactionFee())
                    .setTransactionValidDuration(request.transactionValidDuration());
            if (!request.serials().isEmpty()) {
                request.serials().forEach(
                        serial -> transaction.addNftTransfer(new NftId(request.tokenId(), serial), request.sender(),
                                request.receiver()));
            } else if (request.amount() != null) {
                transaction.addTokenTransfer(request.tokenId(), request.sender(), request.amount() * -1);
                transaction.addTokenTransfer(request.tokenId(), request.receiver(), request.amount());
            } else {
                throw new IllegalArgumentException("either amount or serial must be provided");
            }
            sign(transaction, request.senderKey());
            final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
            return new TokenTransferResult(receipt.transactionId, receipt.status);
        } catch (final Exception e) {
            throw new HieroException("Failed to execute transfer nft transaction", e);
        }
    }

    @NonNull
    private <T extends Transaction<T>> Transaction<T> sign(Transaction<T> transaction, final PrivateKey... keys) {
        if (keys != null) {
            transaction.freezeWith(hieroContext.getClient());
            for (PrivateKey key : keys) {
                transaction.sign(key);
            }
        }
        return transaction;
    }

    @NonNull
    private ContractFunctionParameters createParameters(@NonNull final List<ContractParam<?>> params) {
        Objects.requireNonNull(params, "params must not be null");
        final ContractFunctionParameters constructorParams = new ContractFunctionParameters();
        final Consumer<ContractParam> consumer = param -> param.supplier()
                .addParamToFunctionParameters(param.value(), constructorParams);
        params.forEach(consumer);
        return constructorParams;
    }

    @NonNull
    private <T extends Transaction<T>> TransactionReceipt executeTransactionAndWaitOnReceipt(
            @NonNull final T transaction) throws HieroException {
        Objects.requireNonNull(transaction, "transaction must not be null");
        try {
            log.debug("Sending transaction of type {}", transaction.getClass().getSimpleName());
            final TransactionResponse response = transaction.execute(hieroContext.getClient());
            listeners.forEach(listener -> {
                try {
                    listener.transactionSubmitted(TransactionType.ACCOUNT_CREATE, response.transactionId);
                } catch (Exception e) {
                    log.error("Failed to notify listener", e);
                }
            });
            try {
                log.debug("Waiting for receipt of transaction '{}' of type {}", response.transactionId,
                        transaction.getClass().getSimpleName());
                final TransactionReceipt receipt = response.getReceipt(hieroContext.getClient());
                listeners.forEach(listener -> {
                    try {
                        listener.transactionHandled(TransactionType.ACCOUNT_CREATE, response.transactionId,
                                receipt.status);
                    } catch (Exception e) {
                        log.error("Failed to notify listener", e);
                    }
                });
                return receipt;
            } catch (Exception e) {
                throw new HieroException(
                        "Failed to receive receipt of transaction '" + response.transactionId + "' of type "
                                + transaction.getClass(), e);
            }
        } catch (final Exception e) {
            throw new HieroException("Failed to execute transaction of type " + transaction.getClass().getSimpleName(),
                    e);
        }
    }

    @NonNull
    private <T extends Transaction<T>> TransactionRecord executeTransactionAndWaitOnRecord(@NonNull final T transaction)
            throws HieroException {
        final TransactionReceipt receipt = executeTransactionAndWaitOnReceipt(transaction);
        try {
            log.debug("Waiting for record of transaction '{}' of type {}", receipt.transactionId,
                    transaction.getClass().getSimpleName());

            final ReceiveRecordHandler data = new ReceiveRecordHandler(transaction, receipt,
                    r -> r.transactionId.getRecord(hieroContext.getClient()));
            return recordInterceptor.get().getRecordFor(data);
        } catch (final Exception e) {
            throw new HieroException("Failed to receive record of transaction '" + receipt.transactionId + "' of type "
                    + transaction.getClass(), e);
        }
    }

    @NonNull
    private <R, Q extends Query<R, Q>> R executeQueryAndWait(@NonNull final Q query) throws HieroException {
        Objects.requireNonNull(query, "query must not be null");
        try {
            log.debug("Sending query of type {}", query.getClass().getSimpleName());
            return query.execute(hieroContext.getClient());
        } catch (Exception e) {
            throw new HieroException("Failed to execute query", e);
        }
    }

    @NonNull
    @Override
    public Runnable addTransactionListener(@NonNull TransactionListener listener) {
        listeners.add(listener);
        return () -> listeners.remove(listener);
    }

    @Override
    public AccountId getOperatorAccountId() {
        return hieroContext.getOperatorAccount().accountId();
    }
}
