package com.github.kaklakariada.fritzbox.report.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.github.kaklakariada.fritzbox.report.model.event.*;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class EqualsContractTest {
    @ParameterizedTest
    @ValueSource(classes = { DslSyncFailed.class, DslSyncSuccessful.class, InternetDisconnected.class,
            IPv4InternetConnectionEstablished.class, IPv6InternetConnectionEstablished.class,
            IPv6PrefixAssigned.class,
            WifiDeviceAuthorizationFailed.class, WifiDeviceConnected.class, WifiDeviceDisconnected.class,
            WifiDeviceDisconnectedHard.class, WifiGuestDeviceConnected.class, WifiGuestDeviceDisconnected.class,
            WifiGuestDeviceDisconnectedHard.class, //
            DataConnections.class, AggregatedVolume.class, DataVolume.class })
    void equalsContract(final Class<?> type) {
        EqualsVerifier.forClass(type).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @ParameterizedTest
    @ValueSource(classes = { DslSyncFailed.class, DslSyncSuccessful.class, InternetDisconnected.class,
            IPv4InternetConnectionEstablished.class, IPv6InternetConnectionEstablished.class,
            IPv6PrefixAssigned.class,
            WifiDeviceAuthorizationFailed.class, WifiDeviceConnected.class, WifiDeviceDisconnected.class,
            WifiDeviceDisconnectedHard.class, WifiGuestDeviceConnected.class, WifiGuestDeviceDisconnected.class,
            WifiGuestDeviceDisconnectedHard.class, //
            DataConnections.class, AggregatedVolume.class })
    void toStringContract(final Class<?> type) {
        ToStringVerifier.forClass(type).verify();
    }
}
