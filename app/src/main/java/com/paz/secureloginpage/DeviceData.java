package com.paz.secureloginpage;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import static java.lang.Math.abs;

public class DeviceData {
    private final String APP_PACKAGE_NAME = "guy4444.shiftmanager";
    //private final String APP_PACKAGE_NAME = "com.facebook.katana";
    private final String TAG = "MyDeviceData";
    private Context context;
    private static DeviceData instance = null;
    private final String NETWORK_WIFI = "WIFI";
    private final String NETWORK_MOBILE = "MOBILE";
    private final String NETWORK_UNKNOWN = "unknown";
    private final String CARRIER_CDMA = "CDMA";
    private final String MODE_NORMAL = "Normal mode";
    private final String MODE_VIBRATE = "Vibrate mode";
    private final String MODE_SILENT = "Silent mode";

    private DeviceData(Context context) {
        this.context = context;
    }

    public static DeviceData getInstance() {
        return instance;
    }

    public static void init(Context context) {

        if (instance == null)
            instance = new DeviceData(context);
    }

    public boolean isGpsEnable() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        boolean enable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Log.d(TAG, "isGpsEnable: gps enable = " + enable);
        return enable;
    }

    public boolean isAirplaneModeOn() {
        boolean enable = Settings.System.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        Log.d(TAG, "isAirplaneModeOn: Airplane Mode enable = " + enable);
        return enable;
    }


    public boolean isDeviceInHebrew() {
        String lang = Resources.getSystem().getConfiguration().locale.getDisplayLanguage();
        Log.d(TAG, "isDeviceInHebrew: lang = " + lang);
        return lang.equals("עברית");

    }

    public boolean isDarkModeOn() {

        int mode = Resources.getSystem().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean enable = (mode == Configuration.UI_MODE_NIGHT_YES);
        Log.d(TAG, "isDarkModeOn: enable = " + enable);
        return enable;
    }

    public boolean isNfcEnable() {

        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        boolean enable = (adapter != null && adapter.isEnabled());
        Log.d(TAG, "isNfcEnable: enable = " + enable);
        return enable;
    }

    public boolean isBTEnable() {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean enable = (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled());
        Log.d(TAG, "isBTEnable: enable = " + enable);
        return enable;
    }


    public boolean isScreenRotationLocked() {

        boolean locked = Settings.System.getInt(context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION,
                0) != 1;

        Log.d(TAG, "isScreenRotationLocked: locked = " + locked);
        return locked;
    }

    public boolean isLandscape() {
        boolean land = (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        Log.d(TAG, "isLandscape: Landscape = " + land);
        return land;
    }

    private String getManufacturerName() {
        String manu = android.os.Build.MANUFACTURER;
        Log.d(TAG, "getManufacturerName: Manufacturer Name = " + manu);
        return manu;

    }

    public boolean isSamsung() {
        boolean b = getManufacturerName().equals("samsung");
        Log.d(TAG, "isSamsung: smasung = " + b);
        return b;
    }

    public void battery() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);


        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

// How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;


    }

    public boolean isInCharging() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);


        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        Log.d(TAG, "isInCharging: " + isCharging);
        return isCharging;

    }

    public boolean isInChargingByAC() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);


        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        Log.d(TAG, "isInChargingWithAC In Charging: " + isCharging);
        if (isCharging) {
            int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
            Log.d(TAG, "isInChargingWithAC: by ac = " + acCharge);
            return acCharge;
        }
        return false;
    }

    public boolean isInChargingByUSB() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);


        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        Log.d(TAG, "isInChargingByUSB In Charging: " + isCharging);
        if (isCharging) {
            int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            Log.d(TAG, "isInChargingByUSB: by usb = " + usbCharge);
            return usbCharge;
        }
        return false;
    }


    private float getBatteryLevel() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        //Determine the current battery level
        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level * 100 / (float) scale;

        Log.d(TAG, "getBatteryLevel: Battery Level = " + batteryPct + "%");
        return batteryPct;
    }

    public boolean isBatteryBetween70TO90() {
        float level = getBatteryLevel();
        boolean b = level >= 70f && level <= 90f;
        Log.d(TAG, "isBatteryBetween70TO90: " + b);
        return b;
    }

    public boolean isSomeAppInstall() {
        try {
            ApplicationInfo info = context.getPackageManager().
                    getApplicationInfo(APP_PACKAGE_NAME, 0);
            Log.d(TAG, "isSomeAppInstall: true");
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "isSomeAppInstall: false");

            return false;
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private String getNetworkTypeL(@NonNull ConnectivityManager cm) {
        Network[] allNetworks = cm.getAllNetworks();
        for (Network network : allNetworks) {
            NetworkInfo networkInfo = cm.getNetworkInfo(network);
            if (isActive(networkInfo)) {
                if (ConnectivityManager.TYPE_WIFI == networkInfo.getType()) {
                    return NETWORK_WIFI;
                }
                if (ConnectivityManager.TYPE_MOBILE == networkInfo.getType()) {
                    return NETWORK_MOBILE;
                }
                return NETWORK_UNKNOWN;
            }
        }
        return NETWORK_UNKNOWN;
    }

    private String getNetworkType() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null == cm) {
            return NETWORK_UNKNOWN;
        }
        return getNetworkTypeL(cm);
    }

    private boolean isActive(NetworkInfo networkInfo) {
        return null != networkInfo && networkInfo.isConnectedOrConnecting();
    }

    public boolean isConnectedToWifi() {
        String net = getNetworkType();
        Log.d(TAG, "isConnectedToWifi: network = " + net);
        return net.equals(NETWORK_WIFI);
    }

    public boolean isOnTable(float x, float y) {
        boolean res = (int) abs(x) == 0 && (int) abs(y) == 0;
        Log.d(TAG, "isOnTable: res = " + res);
        return res;
    }

    private int getBrightness() {
        try {
            int brightness = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
            Log.d(TAG, "getBrightness: brightness = " + brightness);
            return brightness;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            Log.d(TAG, "getBrightness: Exception +\n " + e.getMessage());
            return -1;
        }
    }

    public boolean isMaxBrightness() {
        boolean res = getBrightness() == 255;
        Log.d(TAG, "isMaxBrightness: res = " + res);
        return res;
    }

    private String getDeviceRingerMode() {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.d(TAG, "Silent mode");
                return MODE_SILENT;
            case AudioManager.RINGER_MODE_VIBRATE:
                Log.d(TAG, "Vibrate mode");
                return MODE_VIBRATE;
            case AudioManager.RINGER_MODE_NORMAL:
                Log.d(TAG, "Normal mode");
                return MODE_NORMAL;
            default:
                return "Error";
        }
    }


    public boolean isInVibrateMode() {
        boolean vibrate = getDeviceRingerMode().equals(MODE_VIBRATE);
        Log.d(TAG, "isInVibrateMode: vibrate mode = " + vibrate);
        return vibrate;
    }


}
