module com.openelements.hiero.smartcontract {
    exports com.openelements.hiero.smartcontract;
    exports com.openelements.hiero.smartcontract.model;
    exports com.openelements.hiero.smartcontract.implementation to com.openelements.hiero.smartcontract.test;

    requires transitive com.openelements.hiero.base;
    requires org.jspecify;
    requires com.google.gson;
}