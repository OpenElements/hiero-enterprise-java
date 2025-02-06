package com.openelements.hiero.base.mirrornode;

import com.openelements.hiero.base.HieroException;
import com.openelements.hiero.base.data.ExchangeRates;
import com.openelements.hiero.base.data.NetworkFee;
import com.openelements.hiero.base.data.NetworkStake;
import com.openelements.hiero.base.data.NetworkSupplies;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NonNull;

/**
 * Interface for interacting with a Hiero network. This interface provides methods to get information related to
 * Network.
 */
public interface NetworkRepository {
    /**
     * Return the ExchangeRates for network.
     *
     * @return {@link Optional} containing the ExchangeRates or null
     * @throws HieroException if the search fails
     */
    @NonNull
    Optional<ExchangeRates> exchangeRates() throws HieroException;

    /**
     * Return the List of NetworkFee for network.
     *
     * @return {@link List} containing NetworkFee or empty list
     * @throws HieroException if the search fails
     */
    @NonNull
    List<NetworkFee> fees() throws HieroException;

    /**
     * Return the NetworkStake for network.
     *
     * @return {@link Optional} containing NetworkStake or null
     * @throws HieroException if the search fails
     */
    @NonNull
    Optional<NetworkStake> stake() throws HieroException;

    /**
     * Return the NetworkSupplies for network.
     *
     * @return {@link Optional} containing NetworkSupplies or null
     * @throws HieroException if the search fails
     */
    @NonNull
    Optional<NetworkSupplies> supplies() throws HieroException;
}
