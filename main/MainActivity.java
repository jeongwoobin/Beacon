// ** 현재 완성한것
// - Rssi값 받아와서 TextView에 주기마다 띄우기
// - 버튼 구현
//
//
// ** 해야하는것
// - Intent
// - 버튼누르면 튕김
// - layout
// - 실시간인지, 데이터가 쌓여서 천천히나오는건지 알아보기
// - Rssi값으로 거리 구할수있는지

package com.example.mybeacon2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.coresdk.common.requirements.SystemRequirementsChecker;
import com.estimote.coresdk.observation.region.beacon.BeaconRegion;
import com.estimote.coresdk.recognition.packets.Beacon;
import com.estimote.coresdk.service.BeaconManager;

import java.util.List;
import java.util.UUID;


public class MainActivity extends AppCompatActivity{
    private Button gotoinfo;

    private BeaconManager beaconManager;
    private BeaconRegion region;

    private TextView tvId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

//        Toast.makeText(MainActivity.this, "onCreate 진입", Toast.LENGTH_SHORT).show();

        super.onCreate(savedInstanceState);

        gotoinfo=(Button)findViewById(R.id.gotoinfo);
        gotoinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "onClick 진입", Toast.LENGTH_SHORT).show();

                if(v.getId()==R.id.gotoinfo) {
                    Intent intent = new Intent(MainActivity.this, ModuleInfo.class);
                    startActivity(intent);
                }

            }
        });

        tvId = (TextView)findViewById(R.id.tvId);

        beaconManager = new BeaconManager(this);

        beaconManager.setRangingListener(new BeaconManager.BeaconRangingListener() {
            @Override
            public void onBeaconsDiscovered(BeaconRegion beaconRegion, List<Beacon> beacons) {

//                Toast.makeText(MainActivity.this, "onBeaconsDiscovered 진입", Toast.LENGTH_SHORT).show();

                if(!beacons.isEmpty()) {

//                    Toast.makeText(MainActivity.this, "if문 진입", Toast.LENGTH_SHORT).show();

                    Beacon nearestBeacon = beacons.get(0);
                    Log.d("MyPhone", Integer.toString(nearestBeacon.getRssi()));
                    tvId.setText(Integer.toString(nearestBeacon.getRssi()));
                }
            }
        });
        region = new BeaconRegion("ranged region", UUID.fromString("74278bda-b644-4520-8f0c-720eaf059935"), 0xFFE0, 0xFFE1);

    }

    @Override
    protected void onResume() {

//        Toast.makeText(MainActivity.this, "onResume 진입", Toast.LENGTH_SHORT).show();

        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {

//                Toast.makeText(MainActivity.this, "onServiceReady 진입", Toast.LENGTH_SHORT).show();

                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    public void onPause() {

//        Toast.makeText(MainActivity.this, "onPause 진입", Toast.LENGTH_SHORT).show();

        //beaconManager.stopRanging(region);

        super.onPause();
    }
}
