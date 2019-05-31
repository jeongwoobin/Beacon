package com.example.mybeacon2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;


public class MainActivity extends Activity {
    Vibrator vib;

    private BeaconManager beaconManager;
    private BeaconRegion region;

    private TextView tvId, mes;
    private Button gotoinfo;

    int txPower = -59;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        tvId = (TextView)findViewById(R.id.tvId);
        mes = (TextView)findViewById(R.id.mes);

        gotoinfo=(Button)findViewById(R.id.gotoinfo);
        gotoinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId()==R.id.gotoinfo) {
                    Intent intent = new Intent(MainActivity.this, ModuleInfo.class);
                    intent.putExtra("uuid", ""+region.getProximityUUID());
                    intent.putExtra("major", ""+region.getMajor());
                    intent.putExtra("minor", ""+region.getMinor());
                    startActivity(intent);
                }
            }
        });

        beaconManager = new BeaconManager(this);

        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {
                if(!beacons.isEmpty()) {
                    Beacon nearestBeacon = beacons.get(0);
                    Log.d("MyPhone", Integer.toString(nearestBeacon.getRssi()));
                    double dis = calculateAccuracy(txPower, nearestBeacon.getRssi());
                    String sDis = String.format("%.2f", dis);
                    tvId.setText("예측거리 >> " + sDis);

                    if(nearestBeacon.getRssi()>=(-50)) {
                        mes.setText("신호 강함");
                        vib.cancel();
                    }
                    else if(nearestBeacon.getRssi()>=(-70)) {
                        mes.setText("신호 보통");
                        vib.vibrate(new long[]{200, 500, 200, 700}, -1);
                    }
                    else {
                        mes.setText("신호 약함");
                        vib.vibrate(new long[]{200, 1000}, -1);
                    }
                }
            }
        });
        region = new BeaconRegion("ranged region", UUID.fromString("74278bda-b644-4520-8f0c-720eaf059935"), 0xFFE0, 0xFFE1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //블루투스 권한 및 활성화코드
        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    public void onPause() {
        //beaconManager.stopRanging(region);
        super.onPause();
    }

    protected static double calculateAccuracy(int txPower, int rssi) {
        if(rssi == 0) {
            return -1.0;
        }
        double ratio = rssi*1.0/txPower;
        if(ratio < 1.0) {
            return Math.pow(ratio, 10);
        }
        else {
            double accuracy = (0.89976)*Math.pow(ratio, 7.7095) + 0.111;
            return accuracy;
        }
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        vib.cancel();
    }
}
