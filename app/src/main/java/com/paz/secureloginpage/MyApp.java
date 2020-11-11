package com.paz.secureloginpage;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

public class MyApp extends Application {
    private static final String TAG = "_MyApp";
    public static String advertId = null;

    @Override
    public void onCreate() {
        super.onCreate();
        DeviceData.init(getApplicationContext());
        getGAID();
    }

    private void getGAID() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    advertId = idInfo.getId();
                    Log.d(TAG, "run: advertId = " + advertId);
                } catch (Exception e) {
                    advertId = "error";
                    e.printStackTrace();
                }
            }
        };


        Thread thread = new Thread(runnable);
        thread.start();

    }
}
