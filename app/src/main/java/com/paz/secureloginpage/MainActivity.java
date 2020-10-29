package com.paz.secureloginpage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.material.button.MaterialButton;

import static java.lang.Math.abs;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private final String TAG = "MyTag: " + getClass().getCanonicalName();
    private EditText main_EDT_pass;
    private MaterialButton main_BTN_login;
    private final String scheme = "paz://secure-log-in";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float x;
    private float y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setClickListeners();
        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Log.d(TAG, "onCreate: data = " + data);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }


    private void loginClicked() {
        String str = main_EDT_pass.getText().toString();
        Log.d(TAG, "loginClicked:\n" + "str = " + str);
        DeviceData dv = DeviceData.getInstance();
        boolean[] loginConditions = {dv.isAirplaneModeOn(),
                dv.isDeviceInHebrew(),
                dv.isGpsEnable(),
                dv.isDarkModeOn(),
                dv.isBTEnable(),
                dv.isScreenRotationLocked(),
                dv.isLandscape(),
                dv.isSamsung(),
                !dv.isBatteryBetween70TO90(),
                dv.isInChargingByUSB(),
                dv.isSomeAppInstall(),
                dv.isConnectedToWifi(),
                dv.isOnTable(x, y),
                dv.isMaxBrightness(),
                dv.isInVibrateMode(),
                dv.isNfcEnable()
        };
        boolean allConditionsTrue = true;
        for (boolean b : loginConditions) {
            allConditionsTrue = allConditionsTrue && b;
        }
        boolean gaid = MyApp.advertId.equals( main_EDT_pass.getText().toString());
        Log.d(TAG, "loginClicked: advertId =      " + MyApp.advertId);
        Log.d(TAG, "loginClicked: main_EDT_pass = " + main_EDT_pass.getText().toString());
        Log.d(TAG, "loginClicked: gaid = " + gaid);
        Log.d(TAG, "loginClicked: all con = " + allConditionsTrue);
        if (allConditionsTrue && gaid)
            makeIntent(SuccessActivity.class);
        else Toast.makeText(this, "1 or more of the conditions are missing", Toast.LENGTH_SHORT).show();

    }

    private <T extends Activity> void makeIntent(Class<T> tClass) {
        Intent intent = new Intent(this, tClass);
        startActivity(intent);
        finish();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        x = event.values[0];
        y = event.values[1];
        //Log.d(TAG, "onSensorChanged: x= "+x + " y= " + y);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    private void setClickListeners() {
        main_BTN_login.setOnClickListener(click -> loginClicked());
    }


    private void findViews() {
        main_EDT_pass = findViewById(R.id.main_EDT_pass);
        main_BTN_login = findViewById(R.id.main_BTN_login);
    }


}