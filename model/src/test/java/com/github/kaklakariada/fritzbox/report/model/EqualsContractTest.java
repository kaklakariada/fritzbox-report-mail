package com.github.kaklakariada.fritzbox.report.model;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.kaklakariada.fritzbox.report.model.event.*;
import com.jparams.verifier.tostring.ToStringVerifier;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class EqualsContractTest {
    @ParameterizedTest
    @MethodSource("modelClasses")
    void equalsContract(final Class<?> type) {
        EqualsVerifier.forClass(type).suppress(Warning.STRICT_INHERITANCE).verify();
    }

    @ParameterizedTest
    @MethodSource("modelClasses")
    void toStringContract(final Class<?> type) {
        ToStringVerifier.forClass(type).verify();
    }

    static Stream<Arguments> modelClasses() {
        return Stream.of(DslSyncFailed.class, DslSyncSuccessful.class, InternetDisconnected.class,
                IPv4InternetConnectionEstablished.class, IPv6InternetConnectionEstablished.class,
                IPv6PrefixAssigned.class,
                WifiDeviceAuthorizationFailed.class, WifiDeviceConnected.class, WifiDeviceDisconnected.class,
                WifiDeviceDisconnectedHard.class, WifiGuestDeviceConnected.class, WifiGuestDeviceDisconnected.class,
                WifiGuestDeviceDisconnectedHard.class, //
                DataConnections.class, AggregatedVolume.class, DataVolume.class).map(Arguments::of);
    }

}
