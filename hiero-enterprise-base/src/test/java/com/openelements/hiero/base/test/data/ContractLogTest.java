package com.openelements.hiero.base.test.data;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.data.ContractEventInstance;
import com.openelements.hiero.base.data.ContractLog;
import com.openelements.hiero.smartcontract.abi.model.AbiEvent;
import com.openelements.hiero.smartcontract.abi.model.AbiParameter;
import com.openelements.hiero.smartcontract.abi.model.AbiParameterType;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ContractLogTest {

    @Test
    void test() {
        //given
        // from https://testnet.mirrornode.hedera.com/api/v1/contracts/0.0.5615061/results/logs?limit=10&order=asc
        final String address = "0x000000000000000000000000000000000055add5";
        final String bloom = "0x00000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000400000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080000000000000000000000000000000000000000000";
        final ContractId contractId = ContractId.fromString("0.0.5615061");
        final String data = "0x0000000000000000000000000000000000000000000000000000000000000001";
        final long index = 1;
        final List<String> topics = List.of("0x271219bdbb9b91472a5df68ef7a9d3f8de02f3c27b93a35306f888acf081ea60");
        final String block_hash = "0x3c33e36ebaa60192ff6c714ec15adb3829e8dd24e0f28b607fa88f6052600fb07ab29aeeb828e3faffd85b90960cc679";
        final long blockNumber = 16076698;
        final ContractId rootContractId = ContractId.fromString("0.0.5615061");
        final String timestamp = "1740183080.776076000";
        final String transactionHash = "0x254fa8781dc4babe1df5925bc2e3e8aa50d1b57d019189116f93fbdbb5497812";
        final Long transactionIndex = 6L;
        final ContractLog contractLog = new ContractLog(address, bloom, contractId, data, index, topics, block_hash,
                blockNumber, rootContractId, timestamp, transactionHash, transactionIndex);
        final List<AbiParameter> inputs = List.of(
                new AbiParameter("count", AbiParameterType.UINT, List.of(), false)
        );
        final AbiEvent event = new AbiEvent("MissingVerificationCountUpdated", inputs, false);

        //then
        Assertions.assertTrue(contractLog.isEventOfType(event));
    }

    @Test
    void test2() {
        //given
        // from https://testnet.mirrornode.hedera.com/api/v1/contracts/0.0.5615061/results/logs?limit=10&order=asc
        final String address = "0x000000000000000000000000000000000055add5";
        final String bloom = "0x00000000000000000000000000000000200000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000400000000000000000000000000000000000020000000000000000000000000000000000000000000000000000000000000001000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000080000000000000000000000000000000000000000000";
        final ContractId contractId = ContractId.fromString("0.0.5615061");
        final String data = "0x0000000000000000000000000000000000000000000000000000000000000001";
        final long index = 1;
        final List<String> topics = List.of("0x271219bdbb9b91472a5df68ef7a9d3f8de02f3c27b93a35306f888acf081ea60");
        final String block_hash = "0x3c33e36ebaa60192ff6c714ec15adb3829e8dd24e0f28b607fa88f6052600fb07ab29aeeb828e3faffd85b90960cc679";
        final long blockNumber = 16076698;
        final ContractId rootContractId = ContractId.fromString("0.0.5615061");
        final String timestamp = "1740183080.776076000";
        final String transactionHash = "0x254fa8781dc4babe1df5925bc2e3e8aa50d1b57d019189116f93fbdbb5497812";
        final Long transactionIndex = 6L;
        final ContractLog contractLog = new ContractLog(address, bloom, contractId, data, index, topics, block_hash,
                blockNumber, rootContractId, timestamp, transactionHash, transactionIndex);
        final List<AbiParameter> inputs = List.of(
                new AbiParameter("count", AbiParameterType.UINT, List.of(), false)
        );
        final AbiEvent event = new AbiEvent("MissingVerificationCountUpdated", inputs, false);

        //when
        final ContractEventInstance eventInstance = contractLog.asEventInstance(event);

        //then
        Assertions.assertNotNull(eventInstance);
        Assertions.assertEquals(contractId, eventInstance.contractId());
        Assertions.assertEquals("MissingVerificationCountUpdated", eventInstance.eventName());
        Assertions.assertEquals(1, eventInstance.parameters().size());
        Assertions.assertEquals("count", eventInstance.parameters().get(0).name());
        Assertions.assertEquals(AbiParameterType.UINT, eventInstance.parameters().get(0).type());
        Assertions.assertArrayEquals(
                new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                        0x00, 0x01},
                eventInstance.parameters().get(0).value());
    }

}
