package mosquito;

import javafx.scene.control.TextField;
import mysql.InsertToDatabase;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Timestamp;


public class MainSubscriber implements MqttCallback {
    private String id1 = "4fa2064ab69ede57";//id terminal 1
    private String id2 = "a07ea291c136cb8f";//id terminal 2
    private int qos = 2;
    private String broker = "tcp://localhost:1883";
    private String clientId = "JavaSampleSubscriber";

    private TextField prox_thres; // thresholds of javafx
    private TextField accx_thres;
    private TextField accy_thres;
    private TextField accz_thres;

    public void main(TextField prox_thres, TextField accx_thres, TextField accy_thres, TextField accz_thres) {
        this.prox_thres = prox_thres;//ana8esi timwn
        this.accx_thres = accx_thres;
        this.accy_thres = accy_thres;
        this.accz_thres = accz_thres;
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient (broker, clientId , persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            sampleClient.setCallback(this);
            sampleClient.connect (connOpts);
            sampleClient.subscribe(id1,qos);
            sampleClient.subscribe(id2,qos);
            System.out.println("subscription successful");
        } catch(MqttException me) {
            me.printStackTrace();
            System.out.println("subscription failed");
            System.exit(0);
        }
    }

    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost!" + cause);
        System.exit(1);
    }

    public void deliveryComplete (IMqttDeliveryToken token) {

    }


    // Thread gia tin emfanisi tou minimatos tou topic
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        float prox_thres_v, accx_thres_v, accy_thres_v, accz_thres_v;

        try {
            prox_thres_v = Float.parseFloat(prox_thres.getText());
            accx_thres_v = Float.parseFloat(accx_thres.getText());
            accy_thres_v = Float.parseFloat(accy_thres.getText());
            accz_thres_v = Float.parseFloat(accz_thres.getText());
        } catch (Exception e) {
            System.out.println("Thresholds not defined, message ignored");
            return;
        }

        String time = new Timestamp(System.currentTimeMillis()).toString();
        System.out.println("Time:\t" +time +"  Topic:\t" + topic +"  Message:\t" + new String(message.getPayload()));

        String characters = new String(message.getPayload());
        String [] values = characters.split("  ");

        InsertToDatabase itd = new InsertToDatabase();//insert to DB

        try {
            itd.valueOfSource = values[0];
            itd.valueOfProx = Float.parseFloat(values[1]);
            itd.valueOfAccx =  Float.parseFloat(values[2]);
            itd.valueOfAccy =  Float.parseFloat(values[3]);
            itd.valueOfAccz =  Float.parseFloat(values[4]);
            itd.valueOfLat =  Float.parseFloat(values[5]);
            itd.valueOfLn =  Float.parseFloat(values[6]);
            itd.valueOfDate =  Float.parseFloat(values[7]);


            if (itd.valueOfProx < prox_thres_v ||//sin8ikes gia na pragmatopoii8ei to insert
                    itd.valueOfAccx > accx_thres_v ||
                    itd.valueOfAccy > accy_thres_v ||
                    itd.valueOfAccz > accz_thres_v) {
                itd.insert();//insert tou minimstos st DB
            }
        } catch (NumberFormatException nfe) {

        }
    }

}
