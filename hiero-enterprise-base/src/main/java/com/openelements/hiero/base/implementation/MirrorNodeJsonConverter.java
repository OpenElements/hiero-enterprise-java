package com.openelements.hiero.base.implementation;

import com.openelements.hiero.base.data.AccountInfo;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import com.openelements.hiero.base.data.Nft;
import com.openelements.hiero.base.data.TransactionInfo;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

public interface MirrorNodeJsonConverter<JSON> {

    @NonNull
    Optional<Nft> toNft(@NonNull JSON json);

    @NonNull
    Optional<NetworkSupplies> toNetworkSupplies(@NonNull JSON json);

    @NonNull
    Optional<NetworkStake> toNetworkStake(@NonNull JSON json);

    @NonNull
    Optional<ExchangeRates> toExchangeRates(@NonNull JSON json);

    @NonNull
    Optional<AccountInfo> toAccountInfo(@NonNull JSON jsonNode);

    @NonNull
    List<NetworkFee> toNetworkFees(@NonNull JSON json);

    @NonNull
    List<TransactionInfo> toTransactionInfos(@NonNull JSON json);

    List<Nft> toNfts(@NonNull JSON json);

}
