open module com.openelements.hiero.test {
    exports com.openelements.hiero.test;

    provides com.openelements.hiero.base.config.NetworkSettingsProvider with com.openelements.hiero.test.implementation.SoloActionNetworkSettingsProvider;

    requires transitive com.openelements.hiero.base;
    requires org.jspecify;
    requires com.google.auto.service;
    requires org.slf4j;
}