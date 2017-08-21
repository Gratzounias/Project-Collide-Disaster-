package com.example.panos.myapplication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import usermanager.CommonManager;
import usersettings.CommonSettings;

public class SettingsActivity extends AppCompatActivity {
    //dilosi metavliton
    private EditText acc_freq;//sixnotites:
    private EditText prox_freq;
    private EditText prox_thres;//katflia :
    private EditText acc_thres_x;
    private EditText acc_thres_y;
    private EditText acc_thres_z;
    private EditText Port;
    private EditText Ip;



    @Override
    protected void onCreate(Bundle savedInstanceState) {//dimiourgia Settings Activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);//-->layout->XML

        acc_freq = (EditText) findViewById(R.id.acc_freq);// Settings Activity -->XML
        prox_freq = (EditText) findViewById(R.id.prox_freq);
        prox_thres = (EditText) findViewById(R.id.prox_thres);
        acc_thres_x = (EditText) findViewById(R.id.acc_thres_x);
        acc_thres_y = (EditText) findViewById(R.id.acc_thres_y);
        acc_thres_z = (EditText) findViewById(R.id.acc_thres_z);
        Port = (EditText) findViewById(R.id.port);
        Ip= (EditText) findViewById(R.id.ip);

    }

    @Override
    protected void onPause() {//pausi main activity kai load twn settings
       super.onPause();
        //load ka8e epilogis twn settings xwrista , kai apodoxi mono katallilwn timwn
        try {
            CommonSettings.acc_freq = Integer.parseInt(acc_freq.getText().toString());
            acc_freq.setError(null);
        } catch (Exception e) {
            acc_freq.setError("Error!");
            CommonSettings.acc_freq = 0;
        }
        try {
            CommonSettings.prox_freq = Integer.parseInt(prox_freq.getText().toString());
            prox_freq.setError(null);
        } catch (Exception e) {
            prox_freq.setError("Error!");
            CommonSettings.prox_freq = 0;
        }

        try {
            CommonSettings.prox_thres = Double.parseDouble(prox_thres.getText().toString());
            prox_thres.setError(null);
        } catch (Exception e) {
            prox_thres.setError("Error!");
            CommonSettings.prox_thres = 0;
        }
        try {
            CommonSettings.acc_thres_x = Double.parseDouble(acc_thres_x.getText().toString());
            acc_thres_x.setError(null);
        } catch (Exception e) {
            acc_thres_x.setError("Error!");
            CommonSettings.acc_thres_x = 0;
        }

        try {
            CommonSettings.acc_thres_y = Double.parseDouble(acc_thres_y.getText().toString());
            acc_thres_y.setError(null);
        } catch (Exception e) {
            acc_thres_y.setError("Error!");
            CommonSettings.acc_thres_y = 0;
        }
        try {
            CommonSettings.acc_thres_z = Double.parseDouble(acc_thres_z.getText().toString());
            acc_thres_z.setError(null);
        } catch (Exception e) {
            acc_thres_z.setError("Error!");
            CommonSettings.acc_thres_z = 0;
        }
        try {
            CommonSettings.port = Double.parseDouble(Port.getText().toString());
            Port.setError(null);
        } catch (Exception e) {
            Port.setError("Error!");
            CommonSettings.port = 1883;
        }
        try {
            CommonSettings.ip = Ip.getText().toString();
            Ip.setError(null);
        } catch (Exception e) {
            Ip.setError("Error!");
            CommonSettings.ip = "?.?.?.?";
        }




    }

    @Override
    public void onBackPressed() {//save twn allagwn sta settings
        try {
            CommonSettings.acc_freq = Integer.parseInt(acc_freq.getText().toString());
            acc_freq.setError(null);
            CommonSettings.prox_freq = Integer.parseInt(prox_freq.getText().toString());
            prox_freq.setError(null);
            CommonSettings.prox_thres = Double.parseDouble(prox_thres.getText().toString());
            prox_thres.setError(null);
            CommonSettings.acc_thres_x = Double.parseDouble(acc_thres_x.getText().toString());
            acc_thres_x.setError(null);
            CommonSettings.acc_thres_y = Double.parseDouble(acc_thres_y.getText().toString());
            acc_thres_y.setError(null);
            CommonSettings.acc_thres_z = Double.parseDouble(acc_thres_z.getText().toString());
            acc_thres_z.setError(null);
            CommonSettings.port = Double.parseDouble(Port.getText().toString());
            Port.setError(null);
            CommonSettings.ip = Ip.getText().toString();
            Ip.setError(null);
            super.onBackPressed();
        } catch (Exception e) {//apodoxi mono twn katallilwn timwn alliws ERROR

            acc_freq.setError("Error!");
            CommonSettings.acc_freq = 0;

            prox_freq.setError("Error!");
            CommonSettings.prox_freq = 0;

            prox_thres.setError("Error!");
            CommonSettings.prox_thres = 0;

            acc_thres_x.setError("Error!");
            CommonSettings.acc_thres_x = 0;

            acc_thres_y.setError("Error!");
            CommonSettings.acc_thres_y = 0;

            acc_thres_z.setError("Error!");
            CommonSettings.acc_thres_z = 0;

            Port.setError("Error!");
            CommonSettings.port = 1883;

            Ip.setError("Error!");
            CommonSettings.ip = "?.?.?.?";
        }



    }
    @Override
    protected void onResume() {//sinexeia stin main activity
        super.onResume();
        //emfanisi sta settings twn newn timwn:
        acc_freq.setText("" + CommonSettings.acc_freq);
        prox_freq.setText("" + CommonSettings.prox_freq);
        prox_thres.setText("" + CommonSettings.prox_thres);
        acc_thres_x.setText("" + CommonSettings.acc_thres_x);
        acc_thres_y.setText("" + CommonSettings.acc_thres_y);
        acc_thres_z.setText("" + CommonSettings.acc_thres_z);
        Port.setText("" + CommonSettings.port);
        Ip.setText(CommonSettings.ip);

    }
}
