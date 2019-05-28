package com.example.mybeacon2;

import android.app.Application;
import android.os.SystemClock;
import android.widget.Toast;

import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;

public class MyApplication extends Application {
    private BeaconManager beaconManager;

    @Override
    public void onCreate() {

//        Toast.makeText(MyApplication.this, "MA.onCreate 진입", Toast.LENGTH_SHORT).show();

        super.onCreate();
        beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {

//                Toast.makeText(MyApplication.this, "MA.onServiceReady 진입", Toast.LENGTH_SHORT).show();

                beaconManager.startMonitoring(new BeaconRegion("ranged region", UUID.fromString("74278bda-b644-4520-8f0c-720eaf059935"), 0xFFE0, 0xFFE1));
            }
        });

        beaconManager.setMonitoringListener(new BeaconManager.BeaconMonitoringListener() {
            @Override
            public void onEnteredRegion(BeaconRegion beaconRegion, List<Beacon> beacons) {
                SystemClock.sleep(100);
                Toast.makeText(MyApplication.this, Integer.toString(beacons.get(0).getRssi()), Toast.LENGTH_SHORT).show();
//                getApplicationContext().startActivity(new Intent(getApplicationContext(), PopupActivity.class).putExtra("uuid", String.valueOf(beacons.get(0).getProximityUUID()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)));
            }

            @Override
            public void onExitedRegion(BeaconRegion beaconRegion) {
//                Toast.makeText(MyApplication.this, "연결끊김", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
