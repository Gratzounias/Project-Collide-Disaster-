package com.example.panos.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;


//import java.io.IOException;
//import java.util.UUID;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.Timestamp;
import java.util.Date;
import java.util.ResourceBundle;

import mosquito.MainPublisher;
import mosquito.MainSubscriber;
import usermanager.CommonManager;
import usermanager.GPSTracker;
import usersettings.CommonSettings;
import userstate.CommonState;

public class MainActivity extends AppCompatActivity implements SensorEventListener, Runnable, MqttCallback {
    private TextView acceleration ;  // dilosi metavliton
    private TextView proximitor;
    private Toast prox_toast;
    private Toast acc_toast;
    private Toast both_toast;

    private Toast remote_both;
    private Toast remote_id;


    private SoundPool mySound;
    int     have_somethingID;
    private String uniqueID;
    private Toast IDtoast;
    private Thread backgroundThread = null;
    private SoundPool mySound1;
    int errorID;
    private SoundPool mySound2;
    int confirmedID;


    private MainSubscriber subscriber = new MainSubscriber();

    //   --- sample buffer ---
    private float prox = 0;
    private float accx = 0;
    private float accy = 0;
    private float accz = 0;

    Button btnShowLocation;

    GPSTracker gpsTracker = null;

    @Override
    public void onCreate(Bundle savedInstanceState) { //dimiourgia kiriou activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommonManager.sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE); //dimiourgia sensorManager

        CommonManager.prox_sensor = CommonManager.sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY); //orismos aisthitirow
        CommonManager.acc_sensor = CommonManager.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(CommonManager.prox_sensor == null) //elegxos an den iparxei o ais8itiras  , dinatotita epektasis
                {Toast.makeText(this, "Proximiter doesn't exist", Toast.LENGTH_LONG).show();}


        if(CommonManager.acc_sensor == null) //elegxos an den iparxei o ais8itiras  , dinatotita epektasis
                {Toast.makeText(this, "Accelerometer doesn't exist", Toast.LENGTH_LONG).show();}


        acceleration=(TextView)findViewById(R.id.acceleration); // --> activity main XML
        proximitor=(TextView)findViewById(R.id.proximitor);     // --> activity main XML

        mySound = new SoundPool(1, AudioManager.STREAM_MUSIC,0); // dimiourgia antikeimenou SounPool
        have_somethingID = mySound.load(this,R.raw.have_something,1);// fortosi arxeiou anaparagogis
        mySound1 = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
        errorID  = mySound1.load(this,R.raw.error,1);
        mySound2 = new SoundPool(1,AudioManager.STREAM_MUSIC,0);
        confirmedID  = mySound2.load(this,R.raw.confirmed,1);


        prox_toast = Toast.makeText(getApplicationContext(), "A L A R M  PROXIMITY", Toast.LENGTH_LONG); //orismos twn toast pou emfanizontai
        acc_toast = Toast.makeText(getApplicationContext(), "A L A R M   ACCELEROMETER", Toast.LENGTH_LONG);//orismos twn toast pou emfanizontai
        both_toast = Toast.makeText(getApplicationContext(), "A L A R M   BOTH SENSORS ON ", Toast.LENGTH_LONG);//orismos twn toast pou emfanizontai

        remote_both = Toast.makeText(getApplicationContext(), "A L A R M   REMOTE BOTH MOBILES ON ", Toast.LENGTH_LONG);//orismos twn toast pou emfanizontai
        remote_id = Toast.makeText(getApplicationContext(), "A L A R M   MY SENSORS ON ", Toast.LENGTH_LONG);//orismos twn toast pou emfanizontai



        uniqueID = Secure.getString(getApplicationContext().getContentResolver(),Secure.ANDROID_ID);//dimiourgia monadikou id mesw tou device id
        IDtoast=Toast.makeText(getApplicationContext(),uniqueID,Toast.LENGTH_LONG);        // toast an unique ID



        backgroundThread = new Thread(this);
        backgroundThread.start();

        gpsTracker = new GPSTracker(MainActivity.this);



    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {// dimiourgia main menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);


        return true;                               //dimiourgi8ike ,ola kala
    }

    @Override
    public void onBackPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//dimiourgia Dialogue Box
                switch (which) { //periptwseis
                    case DialogInterface.BUTTON_POSITIVE:
                        if (CommonManager.stop(MainActivity.this)) {
                            Toast.makeText(MainActivity.this, "Stopped!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "No need to stop, not running", Toast.LENGTH_LONG).show();//just debugging
                        }
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE://akirwnei to dialogue box
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this); //ti minimata emfanizei to dialogue box
        builder.setMessage("Do you want to exit?")
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {// epiloges tou menu
        switch (item.getItemId()) { //ti epilegei
            case R.id.menuItemExit:
                if (CommonManager.stop(this)) { //epilogi exit
                    Toast.makeText(this, "Stopped!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "No need to stop, not running", Toast.LENGTH_LONG).show();//just debugging
                }
                this.finish();
                return true;
            case R.id.menuItemStart://epilogi start
                if (CommonManager.start(this)) {
                    Toast.makeText(this, "Started!", Toast.LENGTH_LONG).show();
                    Toast.makeText(this,"Unique ID is : ",Toast.LENGTH_SHORT).show();//emfanisi me toast message tou monadikou ID pou dimiourgi8ike
                    IDtoast.show();
                }
                return true;
            case R.id.menuItemStop://epilogi stop
                if (CommonManager.stop(this)) {
                    Toast.makeText(this, "Stopped!", Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.menuItemSettings://epilogi settings
                Intent intent = new Intent(this, SettingsActivity.class);//dimiourgia intent , gia to setting activity --> Setting activity
                startActivity(intent);//start to setting activity
                return true;
            case R.id.menuSwitchToAutomatic://epilogi settings
                if (CommonManager.switchToAutomatic()) {

                }
                return true;
            case R.id.menuSwitchToManual://epilogi settings
                if (CommonManager.switchToManual()) {

                }
                return true;
            case R.id.menuSwitchToOnline://epilogi settings
                if (CommonManager.switchToOnline()) {

                }
                return true;
            case R.id.menuSwitchToOffline://epilogi settings
                if (CommonManager.switchToOffline()) {

                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) { //orismos tis on sensor changed ,gia otan allazoyn times oi sensors
        boolean alarm_prox = false; // arxikopoiisi alarms
        boolean alarm_acc = false;


        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {//ama o sensor einai accelerometer kane :
            float value_x = event.values[0]; //ana8esi ton values[] stis metavlites elegxou
            float value_y = event.values[1]; //ana8esi ton values[] stis metavlites elegxou
            float value_z = event.values[2]; //ana8esi ton values[] stis metavlites elegxou

            accx = value_x;
            accy = value_y;
            accz = value_z;

            //elegxos  analoga me ta katwflia
            if (value_x >= CommonSettings.acc_thres_x) {
                alarm_acc = true;
            }//else{
               // alarm_acc = false;
            //}
            if (value_y >= CommonSettings.acc_thres_y) {
                alarm_acc = true;
            }//else{
              //  alarm_acc = false;
            //}
            if (value_z >= CommonSettings.acc_thres_z) {
                alarm_acc = true;
            }//else{
              //  alarm_acc = false;
            //}

            // ektipwsi twn timwn pou lambanei o ais8itiras :
            acceleration.setText("X: " + event.values[0] + "        m/s^2 " + "\nY: " + event.values[1] + "        m/s^2 " + "\nZ: " + event.values[2] +"        m/s^2 ");
        }
        if (sensor.getType() == Sensor.TYPE_PROXIMITY) { //ama o sensor einai proximity kane :
            float sensorvalue = event.values[0];//ana8esi toy values[] sti metavliti elegxou
            prox = sensorvalue;

            //elegxos analoga me to katofli
            if (sensorvalue <= CommonSettings.prox_thres) {
                alarm_prox = true;
            }
            proximitor.setText("\n " + event.values[0] + " cm ");//ektiposi timis tou sensor
        }

        if (!CommonState.internet || CommonSettings.ip == null || (CommonState.internet && (!CommonState.isautomatic && !CommonState.isonline))) {
            //periptoseis emfanisis TOASTS analoga me ta alarms
            if (alarm_prox && alarm_acc) {
                both_toast.show();//emfanise toast
                prox_toast.cancel();
                acc_toast.cancel();
                mySound1.play(errorID, 1, 1, 1, 0, 1);//pai3e mouiki
            } else if (alarm_prox) {
                prox_toast.show();//emfanise toast
                both_toast.cancel();
                acc_toast.cancel();
                mySound1.play(errorID, 1, 1, 1, 0, 1);//pai3e mouiki
            } else if (alarm_acc) {
                acc_toast.show();//emfanise toast
                both_toast.cancel();
                prox_toast.cancel();
                mySound1.play(errorID, 1, 1, 1, 0, 1);//pai3e mouiki
            }
        } else {
            acc_toast.cancel();//emfanise toast
            both_toast.cancel();
            prox_toast.cancel();
        }
/*
            if (alarm_prox || alarm_acc) {
                if (!CommonState.musicplaying) {
                    //play music
                    mySound.play(have_somethingID, 1, 1, 1, 0, 1);
                    //mySound.stop(have_somethingID);
                    CommonState.musicplaying = true;


                }
            }

            if (!alarm_prox && !alarm_acc) {
                if (CommonState.musicplaying) {
                    // stop music
                    //mp.stop();
                    mySound.stop(have_somethingID);
                    CommonState.musicplaying = false;

                }
            }*/
        }


    // pigi http://stackoverflow.com
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) { //emfanisi epilogon menu
        super.onPrepareOptionsMenu(menu);
        boolean running = CommonState.running;

        MenuItem item_start = menu.getItem(0);
        item_start.setVisible(!running); //an den trexei to start button einai emfanisimo

        MenuItem item_stop = menu.getItem(1);
        item_stop.setVisible(running); //an den trexei to stop button den einai emfanisimo

        MenuItem item_settings = menu.getItem(2);
        item_settings.setVisible(!running);//an den trexei to settings button einai emfanisimo

        // automatic - manual
        MenuItem item_sm = menu.getItem(3); // switch to automatic
        item_sm.setVisible(!running && CommonState.isautomatic == false);//an den trexei kai einai manual emfanisi tou switch t automatic

        MenuItem item_sa = menu.getItem(4); // switch to manual
        item_sa.setVisible(!running && CommonState.isautomatic == true);//an den trexei to settings kai einai automatic switch to manual




        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean internet = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        // manual: online - offline
        MenuItem item_son = menu.getItem(5); // switch to online
        item_son.setVisible(!running && CommonState.isautomatic == false && internet && !CommonState.isonline);
        //switch to online ean den trexe kai den einai sto automatic kai iparxei internet kai den einai idi online
        MenuItem item_soff = menu.getItem(6); // switch to offline
        item_soff.setVisible(!running && CommonState.isautomatic == false && CommonState.isonline);//an den trexei to settings button einai emfanisimo
        //switch to offline ean den trexe kai den einai sto automatic kai einai switched online
        return true;
    }


    @Override
    protected void onDestroy() { //ginetai destroy twn antikeimenwn otan termatizei i efarmogi
        super.onDestroy();

        acc_toast.cancel();
        both_toast.cancel();
        prox_toast.cancel();
        mySound.release();
        mySound1.release();
        mySound2.release();

        backgroundThread.interrupt();
    }

    @Override
    public void run() {
        MainPublisher publisher = new MainPublisher(); //dimiourgia publisher-subscriber
        MainSubscriber subscriber = new MainSubscriber();

        try {
            while (!Thread.currentThread().isInterrupted()) {//oso den trexei to thread
                if (CommonState.running == true) {



                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    CommonState.internet = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

                    if (CommonState.internet && gpsTracker != null && CommonSettings.ip != null &&//eam iparxei internet kai gps kai egkiri ip kai ( einai automatic 'i
                            (CommonState.isautomatic || //'i einai manual kai einai einai online, tote:
                            (!CommonState.isautomatic && CommonState.isonline == true))) {
                        subscriber.subscribe(uniqueID + "subscriber", "apantisi", this); //apantisi subscribe

                        float lat = (float) gpsTracker.getLatitude();
                        float lng = (float) gpsTracker.getLongitude();

                        if (lat > 0 && lng > 0) { //publish ta ta data apo to termatiko
                            String msg = publisher.formatMessage(uniqueID, prox, accx, accy, accz, lat, lng, new Date().getTime());
                            publisher.publish(uniqueID + "publisher", uniqueID, msg);
                        }
                    }
                }

                Thread.sleep(500); //sleep gia 0,5 sec
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String s, MqttMessage message) throws Exception {
        String apantisi = new String(message.getPayload());

        if (apantisi.equals(uniqueID)) { // mono t ena termatiko prokeiai na sigkroustei :
            remote_id.show();// remote toast
            remote_both.cancel();
            mySound2.play(confirmedID, 1, 1, 1, 0, 1);//pai3e mousiki gia sigkrousi ens termatikou , diaforetiki apo tin offline
        }
        if (apantisi.equals("TERM1ANDTERM2")) {//an prokeitai g epibebaiwmeni sigkrousi kai twn 2 termatikwn
            ////////////////////////
            remote_id.cancel();
            remote_both.show();//remote toast kai g ta duo termatika
            mySound.play(have_somethingID, 1, 1, 1, 0, 1);//pai3e mouiki gia epibebaiwmeni sigkrousi , diafortiki apo tis alles
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}

