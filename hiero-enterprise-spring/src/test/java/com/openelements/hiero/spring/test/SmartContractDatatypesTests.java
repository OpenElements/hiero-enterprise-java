package com.openelements.hiero.spring.test;

import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.SmartContractClient;
import com.openelements.hiero.base.data.Account;
import com.openelements.hiero.base.data.ContractCallResult;
import com.openelements.hiero.base.data.ContractParam;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = HieroTestConfig.class)
public class SmartContractDatatypesTests {

  @Autowired private SmartContractClient smartContractClient;

  private Account adminAccount;

  private static ContractId contractId;

  private synchronized ContractId getOrCreateContractId() {
    if (contractId == null) {
      try {
        final Path path =
            Path.of(ContractServiceTest.class.getResource("/datatypes.bin").getPath());
        contractId = smartContractClient.createContract(path);
      } catch (Exception e) {
        throw new RuntimeException("Can not create contract", e);
      }
    }
    return contractId;
  }

  @Test
  public void testString() throws Exception {
    // given
    final ContractId contract = getOrCreateContractId();
    final String expected = "Hello, World!";

    // when
    final ContractCallResult result =
        smartContractClient.callContractFunction(
            contract, "checkString", ContractParam.string(expected));

    // then
    Assertions.assertEquals(expected, result.getString(0));
  }

  @Test
  @Disabled("Looks like the SDK has a bug regarding address type.")
  public void testAddress() throws Exception {
    // given
    final ContractId contract = getOrCreateContractId();

    // when
    final ContractCallResult result =
        smartContractClient.callContractFunction(
            contract, "checkString", ContractParam.address(adminAccount.accountId()));

    // then
    Assertions.assertEquals(adminAccount.accountId(), result.getAddress(0));
  }

  @ParameterizedTest
  @MethodSource("provideIntArguments")
  public <T> void testLongTypes(
      String functionName,
      long value,
      ContractParam inputParam,
      Function<ContractCallResult, Long> resultExtractor)
      throws Exception {
    // given
    final ContractId contract = getOrCreateContractId();

    // when
    final ContractCallResult result =
        smartContractClient.callContractFunction(contract, functionName, inputParam);

    // then
    Assertions.assertEquals(value, resultExtractor.apply(result));
  }

  @ParameterizedTest
  @MethodSource("provideBigIntegerArguments")
  public <T> void testBigIntegerTypes(
      String functionName,
      BigInteger value,
      ContractParam inputParam,
      Function<ContractCallResult, BigInteger> resultExtractor)
      throws Exception {
    // given
    final ContractId contract = getOrCreateContractId();

    // when
    final ContractCallResult result =
        smartContractClient.callContractFunction(contract, functionName, inputParam);

    // then
    Assertions.assertEquals(value, resultExtractor.apply(result));
  }

  static Stream<Arguments> provideIntArguments() {
    return Stream.of(
        Arguments.of(
            "checkInt8",
            7,
            ContractParam.int8((byte) 7),
            (Function<ContractCallResult, Long>) r -> (long) r.getInt8(0)),
        Arguments.of(
            "checkUint8",
            7,
            ContractParam.uint8((byte) 7),
            (Function<ContractCallResult, Long>) r -> (long) r.getUint8(0)),
        Arguments.of(
            "checkInt32",
            123456789,
            ContractParam.int32(123456789),
            (Function<ContractCallResult, Long>) r -> (long) r.getInt32(0)),
        Arguments.of(
            "checkUint32",
            123456789L,
            ContractParam.uint32(123456789L),
            (Function<ContractCallResult, Long>) r -> (long) r.getUint32(0)),
        Arguments.of(
            "checkInt64",
            1234567890123456789L,
            ContractParam.int64(1234567890123456789L),
            (Function<ContractCallResult, Long>) r -> (long) r.getInt64(0)));
  }

  static Stream<Arguments> provideBigIntegerArguments() {
    return Stream.of(
        Arguments.of(
            "checkInt256",
            new BigInteger(
                "1234567890123456789012345678901234567890123456789012345678901234567890"),
            ContractParam.int256(
                new BigInteger(
                    "1234567890123456789012345678901234567890123456789012345678901234567890")),
            (Function<ContractCallResult, BigInteger>) r -> r.getInt256(0)),
        Arguments.of(
            "checkUint256",
            new BigInteger(
                "1234567890123456789012345678901234567890123456789012345678901234567890"),
            ContractParam.uint256(
                new BigInteger(
                    "1234567890123456789012345678901234567890123456789012345678901234567890")),
            (Function<ContractCallResult, BigInteger>) r -> r.getUint256(0)));
  }
}
