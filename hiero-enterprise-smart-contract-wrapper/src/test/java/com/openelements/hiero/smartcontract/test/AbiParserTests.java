package com.openelements.hiero.smartcontract.test;

import com.openelements.hiero.smartcontract.AbiParser;
import com.openelements.hiero.smartcontract.implementation.GsonAbiParser;
import com.openelements.hiero.smartcontract.model.AbiModel;
import java.net.URL;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AbiParserTests {

    @Test
    protected void test() throws Exception {
        //given
        final AbiParser parser = new GsonAbiParser();
        final URL abiFile = AbiParserTests.class.getClassLoader().getResource("test.json");

        //when
        final AbiModel model = parser.parse(abiFile);

        //then
        Assertions.assertNotNull(model);
    }
}
