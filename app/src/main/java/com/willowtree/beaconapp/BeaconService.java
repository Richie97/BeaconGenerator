package com.willowtree.beaconapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.BeaconTransmitter;

import java.util.Arrays;

public class BeaconService extends Service {
    BeaconTransmitter mBeaconTransmitter;

    public BeaconService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BeaconManager manager = BeaconManager.getInstanceForApplication(this);
        manager.setDebug(BuildConfig.DEBUG);
        mBeaconTransmitter = new BeaconTransmitter(this, new BeaconParser().setBeaconLayout("m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25"));
        // Transmit a beacon with Identifiers 2F234454-CF6D-4A0F-ADF2-F4911BA9FFA6 1 2
        Beacon beacon = new Beacon.Builder()
                .setId1("02FC4494-B0D3-33D6-869A-E9841DA77E39")
                .setId2("1")
                .setId3("2")
                .setManufacturer(0x0000) // Choose a number of 0x00ff or less as some devices cannot detect beacons with a manufacturer code > 0x00ff
                .setTxPower(-59)
                .setDataFields(Arrays.asList(new Long[] {0l}))
                .build();

        mBeaconTransmitter.startAdvertising(beacon);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeaconTransmitter.stopAdvertising();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        mBeaconTransmitter.stopAdvertising();
    }

    public static class ServiceBinder extends Binder {
        private final Service mService;

        public ServiceBinder(Service service) {
            mService = service;
        }

        public Service getService() {
            return mService;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder(this);
    }

}
