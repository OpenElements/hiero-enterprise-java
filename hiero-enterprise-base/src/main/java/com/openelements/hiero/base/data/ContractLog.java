package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.solidity.SolidityTools;
import com.openelements.hiero.smartcontract.abi.model.AbiEvent;
import java.util.List;
import java.util.Objects;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Represents a log entry for a contract.
 *
 * @param address          The hex encoded EVM address of the contract. example:
 *                         {@code 0xddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef} - pattern:
 *                         {@code ^0x[0-9A-Fa-f]{40}$}
 * @param bloom            The hex encoded bloom filter of the contract log. example:
 *                         {@code 0x549358c4c2e573e02410ef7b5a5ffa5f36dd7398}
 * @param contractId       Network entity ID in the format of shard.realm.num example: {@code 0.0.1234}
 * @param data             The hex encoded data of the contract log - example:
 *                         {@code 0x00000000000000000000000000000000000000000000000000000000000000fa}
 * @param index            The index of the contract log in the chain of logs for an execution
 * @param topics           A list of hex encoded topics associated with this log event. example:
 *                         {@code List [ "0xf4757a49b326036464bec6fe419a4ae38c8a02ce3e68bf0809674f6aab8ad300" ]}
 * @param block_hash       The hex encoded block (record file chain) hash. example:
 *                         {@code
 *                         0x553f9311833391c0a3b2f9ed64540a89f2190a511986cd94889f1c0cf7fa63e898b1c6730f14a61755d1fb4ca05fb073}
 * @param blockNumber      The block height calculated as the number of record files starting from zero since network
 *                         start.
 * @param rootContractId   The executed contract that created this contract log. pattern:
 *                         {@code ^\d{1,10}\.\d{1,10}\.\d{1,10}$}. example: {@code 0.0.1234}
 * @param timestamp        A Unix timestamp in seconds.nanoseconds format. pattern: {@code ^\d{1,10}(\.\d{1,9})?$}.
 *                         example: {@code 1586567700.453054000}
 * @param transactionHash  A hex encoded transaction hash. example:
 *                         {@code 0x397022d1e5baeb89d0ab66e6bf602640610e6fb7e55d78638db861e2c6339aa9}
 * @param transactionIndex The position of the transaction in the block
 */
public record ContractLog(@NonNull String address, @Nullable String bloom, @Nullable ContractId contractId,
                          @Nullable String data, long index,
                          @NonNull List<String> topics, @NonNull String block_hash, long blockNumber,
                          @Nullable ContractId rootContractId, @NonNull String timestamp,
                          @NonNull String transactionHash,
                          @Nullable Long transactionIndex) {

    public ContractLog {
        Objects.requireNonNull(address, "address must not be null");
        Objects.requireNonNull(topics, "topics must not be null");
        Objects.requireNonNull(block_hash, "block_hash must not be null");
        Objects.requireNonNull(timestamp, "timestamp must not be null");
        Objects.requireNonNull(transactionHash, "transactionHash must not be null");
    }

    public boolean isEventOfType(final @NonNull AbiEvent event) {
        Objects.requireNonNull(event, "event");
        if (event.anonymous()) {
            throw new IllegalStateException("Cannot check anonymous event");
        }
        if (topics.isEmpty()) {
            return false;
        }
        final String eventHashAsHex = event.createEventSignatureHashAsHex();
        return topics.get(0).equals(eventHashAsHex);
    }

    public ContractEventInstance asEventInstance(final @NonNull AbiEvent event) {
        Objects.requireNonNull(event, "event must not be null");
        if (!isEventOfType(event)) {
            throw new IllegalArgumentException("Event does not match log");
        }
        return SolidityTools.asEventInstance(this, event);
    }
}
