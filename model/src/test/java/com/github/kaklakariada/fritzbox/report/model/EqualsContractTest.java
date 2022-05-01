package com.github.kaklakariada.fritzbox.report.model;

import com.github.kaklakariada.fritzbox.report.model.event.DslSyncFailed;
import com.github.kaklakariada.fritzbox.report.model.event.DslSyncSuccessful;
import com.github.kaklakariada.fritzbox.report.model.event.IPv4InternetConnectionEstablished;
import com.github.kaklakariada.fritzbox.report.model.event.IPv6InternetConnectionEstablished;
import com.github.kaklakariada.fritzbox.report.model.event.IPv6PrefixAssigned;
import com.github.kaklakariada.fritzbox.report.model.event.InternetDisconnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceAuthorizationFailed;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceConnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiDeviceDisconnectedHard;
import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceConnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceDisconnected;
import com.github.kaklakariada.fritzbox.report.model.event.WifiGuestDeviceDisconnectedHard;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

class EqualsContractTest {
    @ParameterizedTest
    @ValueSource(classes = { DslSyncFailed.class, DslSyncSuccessful.class, InternetDisconnected.class,
            IPv4InternetConnectionEstablished.class, IPv6InternetConnectionEstablished.class, IPv6PrefixAssigned.class,
            WifiDeviceAuthorizationFailed.class, WifiDeviceConnected.class, WifiDeviceDisconnected.class,
            WifiDeviceDisconnectedHard.class, WifiGuestDeviceConnected.class, WifiGuestDeviceDisconnected.class,
            WifiGuestDeviceDisconnectedHard.class, //
            DataConnections.class, AggregatedVolume.class, DataVolume.class })
    void equalsContract(Class<?> type) {
        EqualsVerifier.forClass(type).suppress(Warning.STRICT_INHERITANCE).verify();
    }
}
