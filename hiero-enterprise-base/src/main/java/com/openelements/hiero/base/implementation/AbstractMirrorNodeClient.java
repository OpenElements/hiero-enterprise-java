package com.openelements.hiero.base.implementation;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.hedera.hashgraph.sdk.TokenId;
import com.hedera.hashgraph.sdk.TopicId;
import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.data.Contract;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.NftMetadata;
import com.openelements.hiero.base.data.Page;
import com.openelements.hiero.base.data.TokenInfo;
import com.openelements.hiero.base.data.Topic;
import com.openelements.hiero.base.data.TopicMessage;
import com.openelements.hiero.base.data.TransactionInfo;
import com.openelements.hiero.base.mirrornode.MirrorNodeClient;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public abstract class AbstractMirrorNodeClient<JSON> implements MirrorNodeClient {

  @NonNull
  protected abstract MirrorNodeRestClient<JSON> getRestClient();

  @NonNull
  protected abstract MirrorNodeJsonConverter<JSON> getJsonConverter();

  @Override
  public @NonNull final Optional<Nft> queryNftsByTokenIdAndSerial(
      @NonNull final TokenId tokenId, final long serialNumber) throws HieroException {
    final JSON json = getRestClient().queryNftsByTokenIdAndSerial(tokenId, serialNumber);
    return getJsonConverter().toNft(json);
  }

  @Override
  public @NonNull final Optional<AccountInfo> queryAccount(@NonNull final AccountId accountId)
      throws HieroException {
    Objects.requireNonNull(accountId, "accountId must not be null");
    final JSON json = getRestClient().queryAccount(accountId);
    return getJsonConverter().toAccountInfo(json);
  }

  @Override
  public @NonNull final Optional<ExchangeRates> queryExchangeRates() throws HieroException {
    final JSON json = getRestClient().queryExchangeRates();
    return getJsonConverter().toExchangeRates(json);
  }

  @Override
  public @NonNull final List<NetworkFee> queryNetworkFees() throws HieroException {
    final JSON json = getRestClient().queryNetworkFees();
    return getJsonConverter().toNetworkFees(json);
  }

  @Override
  public @NonNull final Optional<NetworkStake> queryNetworkStake() throws HieroException {
    final JSON json = getRestClient().queryNetworkStake();
    return getJsonConverter().toNetworkStake(json);
  }

  @Override
  public @NonNull final Optional<NetworkSupplies> queryNetworkSupplies() throws HieroException {
    final JSON json = getRestClient().queryNetworkSupplies();
    return getJsonConverter().toNetworkSupplies(json);
  }

  @NonNull
  public final Optional<TokenInfo> queryTokenById(@NonNull TokenId tokenId) throws HieroException {
    final JSON json = getRestClient().queryTokenById(tokenId);
    return getJsonConverter().toTokenInfo(json);
  }

  @Override
  @NonNull
  public final Optional<TransactionInfo> queryTransaction(@NonNull String transactionId)
      throws HieroException {
    final JSON json = getRestClient().queryTransaction(transactionId);
    return getJsonConverter().toTransactionInfo(json);
  }

  @Override
  @NonNull
  public final Optional<Topic> queryTopicById(TopicId topicId) throws HieroException {
    final JSON json = getRestClient().queryTopicById(topicId);
    return getJsonConverter().toTopic(json);
  }

  @Override
  @NonNull
  public final Optional<TopicMessage> queryTopicMessageBySequenceNumber(
      TopicId topicId, long sequenceNumber) throws HieroException {
    final JSON json = getRestClient().queryTopicMessageBySequenceNumber(topicId, sequenceNumber);
    return getJsonConverter().toTopicMessage(json);
  }

  @Override
  public @NonNull Optional<NftMetadata> getNftMetadata(TokenId tokenId) throws HieroException {
    throw new UnsupportedOperationException("Not yet implemented");
  }

  @Override
  public @NonNull Page<Contract> queryContracts() throws HieroException {
    final JSON json = getRestClient().queryContracts();
    return getJsonConverter().toContractPage(json);
  }

  @Override
  public @NonNull Optional<Contract> queryContractById(@NonNull final ContractId contractId)
      throws HieroException {
    Objects.requireNonNull(contractId, "contractId must not be null");
    final JSON json = getRestClient().queryContractById(contractId);
    return getJsonConverter().toContract(json);
  }
}
