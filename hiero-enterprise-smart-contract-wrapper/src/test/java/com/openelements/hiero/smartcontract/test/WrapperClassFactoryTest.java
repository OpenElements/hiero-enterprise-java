package com.openelements.hiero.smartcontract.test;

import com.openelements.hiero.smartcontract.WrapperClassFactory;
import com.openelements.hiero.smartcontract.model.AbiModel;
import java.net.URL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class WrapperClassFactoryTest {

    @Test
    protected void test() throws Exception {
        //given
        final WrapperClassFactory factory = new WrapperClassFactory();
        final URL abiFile = WrapperClassFactoryTest.class.getClassLoader().getResource("test.json");

        //when
        final AbiModel parse = factory.parse(abiFile);

        //then
        Assertions.assertNotNull(parse);

    }
}
