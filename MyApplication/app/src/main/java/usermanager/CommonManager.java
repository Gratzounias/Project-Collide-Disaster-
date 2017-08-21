package usermanager;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import usersettings.CommonSettings;
import userstate.CommonState;

public class CommonManager {
    public static SensorManager sensorManager;
    public static Sensor acc_sensor;
    public static Sensor prox_sensor;
    public static SensorEventListener sel;

    public static boolean start(SensorEventListener sel) { //Register Listeners
        if (!CommonState.running ) {//an den exei 3ekinisei to programma 3ekinaei kai kanei register tous listeners
            CommonState.running = true;
            sensorManager.registerListener(sel, acc_sensor, CommonSettings.acc_freq);//register listener gia t ka8e sensor
            sensorManager.registerListener(sel, prox_sensor,CommonSettings.prox_freq);
            return true;
        } else {
            return false; // ama idi trexei min kanei register tpt
        }
    }

    public static boolean stop(SensorEventListener sel) { //UnRegister Listeners
        if (CommonState.running ) {//ama trexei kai epile3oume stop
            CommonState.running = false;
            sensorManager.unregisterListener(sel);// kane unregister tous listeners
            return true;
        } else {
            return false;
        }
    }

    public static boolean switchToAutomatic() {//boolean elegxou an einai s automatic
        if (CommonState.isautomatic == false ) {
            CommonState.isautomatic = true;//an oxi alla3e to flag se true
            return true;
        } else {
            return false;
        }
    }

    public static boolean switchToManual() { //boolean elegxou an einai s manual
        if (CommonState.isautomatic == true) {
            CommonState.isautomatic = false;//an oxi alla3e to flag tou automatic se false
            return true;
        } else {
            return false;
        }
    }


    public static boolean switchToOnline() {
        if (CommonState.isonline == false) {
            CommonState.isonline = true;
            return true;
        } else {
            return false;
        }
    }

    public static boolean switchToOffline() {
        if (CommonState.isonline == true) {
            CommonState.isonline = false;
            return true;
        } else {
            return false;
        }
    }
}
