package com.androiddevice.devicemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    String currentBatteryHealth = "Battery Health";
    int deviceHealth;
    IntentFilter intentfilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://java2blog.com/check-battery-health-in-android-programmatically/
        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        MainActivity.this.registerReceiver(broadcastreceiver,intentfilter);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv6 = findViewById(R.id.tv6);

        //https://developer.android.com/training/monitoring-device-state/battery-monitoring#java
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);
        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float) scale;

        batteryLevel();

        tv1.setText("Charging status : " + isCharging);
        tv2.setText("USB Charge : " + usbCharge);
        tv3.setText("AC Charge : " + acCharge);
        tv4.setText("Battery Percentage : " + batteryPct);
        tv5.setText("Battery Health : " + batteryPct);
    }

    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            deviceHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_COLD) {

                tv5.setText(currentBatteryHealth + " = Cold");
            }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_DEAD) {

                tv5.setText(currentBatteryHealth + " = Dead");
            }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_GOOD) {

                tv5.setText(currentBatteryHealth + " = Good");
            }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_OVERHEAT) {

                tv5.setText(currentBatteryHealth + " = OverHeat");
            }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {

                tv5.setText(currentBatteryHealth + " = Over voltage");
            }

            if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNKNOWN) {

                tv5.setText(currentBatteryHealth + " = Unknown");
            }
            if (deviceHealth == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {

                tv5.setText(currentBatteryHealth + " = Unspecified Failure");
            }
        }
    };

    private void batteryLevel() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int rawlevel = intent.getIntExtra("level", -1);
                int scale = intent.getIntExtra("scale", -1);
                int level = -1;
                if (rawlevel >= 0 && scale > 0) {
                    level = (rawlevel * 100) / scale;
                }
                tv6.setText("Battery Level Remaining: " + level + "%");
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }
}