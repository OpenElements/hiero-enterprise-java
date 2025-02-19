package com.openelements.hiero.smartcontract.model;

import java.util.List;

public interface AbiEntry {

    AbiEntryType type();

    String name();

    List<AbiParameter> inputs();
}
