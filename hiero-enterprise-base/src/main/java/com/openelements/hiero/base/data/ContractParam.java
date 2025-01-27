package com.openelements.hiero.base.data;

import com.hedera.hashgraph.sdk.AccountId;
import com.hedera.hashgraph.sdk.ContractId;
import com.openelements.hiero.base.implementation.data.BigIntegerBasedNumericDatatypes;
import com.openelements.hiero.base.implementation.data.BooleanDatatype;
import com.openelements.hiero.base.implementation.data.BytesBasedDatatype;
import com.openelements.hiero.base.implementation.data.LongBasedNumericDatatypes;
import com.openelements.hiero.base.implementation.data.ParamSupplier;
import com.openelements.hiero.base.implementation.data.StringBasedDatatype;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.jspecify.annotations.NonNull;

/**
 * Represents a parameter for a smart contract call (see {@link com.openelements.hiero.base.SmartContractClient}). For
 * all supported types, see the static factory methods. Normally a developer should not need to create instances of this
 * class directly.
 *
 * @param value      the value of the parameter
 * @param nativeType the native type of the parameter
 * @param supplier   the supplier of the parameter
 * @param <T>        the type of the parameter
 */
public record ContractParam<T>(@NonNull T value, @NonNull String nativeType, @NonNull ParamSupplier<T> supplier) {

    public ContractParam {
        Objects.requireNonNull(value, "value must not be null");
        Objects.requireNonNull(nativeType, "nativeType must not be null");
        Objects.requireNonNull(supplier, "supplier must not be null");
        if (!supplier.isValidParam(value)) {
            throw new IllegalArgumentException(
                    "value '" + value + "' is not valid for native type '" + nativeType + "'");
        }
    }

    /**
     * Creates a new contract parameter using the provided value and {@link ParamSupplier}.
     *
     * @param value         the value of the parameter
     * @param paramSupplier the supplier that provides validation and native type information
     * @param <T>           the type of the parameter
     * @return the newly created {@link ContractParam} instance
     */
    private static <T> ContractParam<T> of(T value, ParamSupplier<T> paramSupplier) {
        Objects.requireNonNull(paramSupplier, "paramSupplier must not be null");
        return new ContractParam<>(value, paramSupplier.getNativeType(), paramSupplier);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code string}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<String> string(String value) {
        return of(value, StringBasedDatatype.STRING);
    }

    @NonNull
    public static ContractParam<Bytes> bytes(String value) {
        return bytes(value.getBytes(StandardCharsets.UTF_8));
    }

    @NonNull
    public static ContractParam<Bytes> bytes32(String value) {
        return bytes32(value.getBytes(StandardCharsets.UTF_8));
    }

    @NonNull
    public static ContractParam<Bytes> bytes(byte[] value) {
        return of(new Bytes(value), BytesBasedDatatype.BYTES);
    }

    @NonNull
    public static ContractParam<Bytes> bytes32(byte[] value) {
        return of(new Bytes(value), BytesBasedDatatype.BYTES32);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code address}. The input must be address
     * in solidty format.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<String> addressBySolidty(String value) {
        return of(value, StringBasedDatatype.ADDRESS);
    }

    /**
     * Creates a new contract parameter with the given value and converts the input to a Solidity address format.
     *
     * @param value the value of the parameter as a string
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<String> address(String value) {
        return addressBySolidty(AccountId.fromString(value).toSolidityAddress());
    }

    /**
     * Creates a new contract parameter using the given {@link AccountId} and converts it to a Solidity address format.
     *
     * @param value the value of the parameter as {@link AccountId}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<String> address(@NonNull AccountId value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value.toSolidityAddress(), StringBasedDatatype.ADDRESS);
    }

    /**
     * Creates a new contract parameter using the given {@link ContractId} and converts it to a Solidity address
     * format.
     *
     * @param value the value of the parameter as {@link ContractId}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<String> address(@NonNull ContractId value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value.toSolidityAddress(), StringBasedDatatype.ADDRESS);
    }

    /**
     * Creates a new contract parameter with the given boolean value and native type {@code bool}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Boolean> bool(boolean value) {
        return of(value, BooleanDatatype.BOOL);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int8}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> int8(byte value) {
        return of((long) value, LongBasedNumericDatatypes.INT8);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint8}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> uint8(short value) {
        return of((long) value, LongBasedNumericDatatypes.UINT8);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int16}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> int16(short value) {
        return of((long) value, LongBasedNumericDatatypes.INT16);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint16}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> uint16(int value) {
        return of((long) value, LongBasedNumericDatatypes.UINT16);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int32}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> int32(int value) {
        return of((long) value, LongBasedNumericDatatypes.INT32);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint32}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> uint32(long value) {
        return of(value, LongBasedNumericDatatypes.UINT32);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int40}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> int40(long value) {
        return of(value, LongBasedNumericDatatypes.INT40);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint40}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> uint40(long value) {
        return of(value, LongBasedNumericDatatypes.UINT40);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int48}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> int48(long value) {
        return of(value, LongBasedNumericDatatypes.INT48);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint48}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> uint48(long value) {
        return of(value, LongBasedNumericDatatypes.UINT48);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int56}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> int56(long value) {
        return of(value, LongBasedNumericDatatypes.INT56);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint56}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> uint56(long value) {
        return of(value, LongBasedNumericDatatypes.UINT56);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int64}.
     *
     * @param value the value of the parameter
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<Long> int64(long value) {
        return of(value, LongBasedNumericDatatypes.INT64);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int72}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int72(long value) {
        return int72(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int72}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int72(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT72);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint72}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint72(long value) {
        return uint72(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint72}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint72(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT72);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int80}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int80(long value) {
        return int80(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int80}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int80(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT80);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint80}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint80(long value) {
        return uint80(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint80}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint80(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT80);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int88}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int88(long value) {
        return int88(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int88}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int88(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT88);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint88}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint88(long value) {
        return uint88(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint88}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint88(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT88);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int96}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int96(long value) {
        return int96(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int96}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int96(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT96);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint96}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint96(long value) {
        return uint96(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint96}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint96(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT96);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int104}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int104(long value) {
        return int104(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int104}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int104(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT104);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint104}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint104(long value) {
        return uint104(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint104}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint104(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT104);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int112}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int112(long value) {
        return int112(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int112}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int112(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT112);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint112}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint112(long value) {
        return uint112(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint112}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint112(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT112);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int120}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int120(long value) {
        return int120(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int120}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int120(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT120);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint120}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint120(long value) {
        return uint120(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint120}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint120(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT120);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int128}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int128(long value) {
        return int128(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int128}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int128(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT128);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint128}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint128(long value) {
        return uint128(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint128}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint128(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT128);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int136}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int136(long value) {
        return int136(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int136}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int136(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT136);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint136}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint136(long value) {
        return uint136(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint136}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint136(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT136);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int144}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int144(long value) {
        return int144(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int144}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int144(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT144);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint144}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint144(long value) {
        return uint144(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint144}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint144(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT144);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int152}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int152(long value) {
        return int152(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int152}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int152(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT152);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint152}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint152(long value) {
        return uint152(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint152}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint152(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT152);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int160}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int160(long value) {
        return int160(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int160}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int160(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT160);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint160}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint160(long value) {
        return uint160(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint160}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint160(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT160);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int168}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int168(long value) {
        return int168(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int168}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int168(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT168);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint168}.
     *
     * @param value the value of the parameter as a {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint168(long value) {
        return uint168(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint168}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint168(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT168);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int176}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int176(long value) {
        return int176(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int176}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int176(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT176);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint176}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint176(long value) {
        return uint176(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint176}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint176(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT176);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int184}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int184(long value) {
        return int184(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int184}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int184(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT184);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint184}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint184(long value) {
        return uint184(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint184}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint184(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT184);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int192}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int192(long value) {
        return int192(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int192}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int192(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT192);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint192}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint192(long value) {
        return uint192(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint192}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint192(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT192);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int200}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int200(long value) {
        return int200(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int200}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int200(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT200);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint200}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint200(long value) {
        return uint200(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint200}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint200(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT200);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int208}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int208(long value) {
        return int208(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int208}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int208(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT208);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint208}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint208(long value) {
        return uint208(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint208}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint208(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT208);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int216}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int216(long value) {
        return int216(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int216}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int216(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT216);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint216}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint216(long value) {
        return uint216(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint216}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint216(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT216);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int224}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int224(long value) {
        return int224(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int224}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int224(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT224);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint224}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint224(long value) {
        return uint224(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint224}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint224(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT224);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int232}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int232(long value) {
        return int232(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int232}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int232(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT232);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint232}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint232(long value) {
        return uint232(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint232}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint232(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT232);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int240}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int240(long value) {
        return int240(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int240}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int240(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT240);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint240}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint240(long value) {
        return uint240(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint240}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint240(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT240);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int248}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int248(long value) {
        return int248(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int248}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int248(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.INT248);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint248}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint248(long value) {
        return uint248(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint248}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint248(@NonNull BigInteger value) {
        return of(value, BigIntegerBasedNumericDatatypes.UINT248);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int256}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int256(@NonNull BigInteger value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value, BigIntegerBasedNumericDatatypes.INT256);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code int256}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> int256(long value) {
        return int256(BigInteger.valueOf(value));
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint256}.
     *
     * @param value the value of the parameter as {@link BigInteger}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint256(@NonNull BigInteger value) {
        Objects.requireNonNull(value, "value must not be null");
        return of(value, BigIntegerBasedNumericDatatypes.UINT256);
    }

    /**
     * Creates a new contract parameter with the given value and native type {@code uint256}.
     *
     * @param value the value of the parameter as {@code long}
     * @return the new contract parameter
     */
    @NonNull
    public static ContractParam<BigInteger> uint256(long value) {
        return uint256(BigInteger.valueOf(value));
    }
}
