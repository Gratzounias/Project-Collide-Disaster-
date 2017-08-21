package mosquito;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.sql.Timestamp;

import javax.security.auth.callback.Callback;

import usersettings.CommonSettings;

//xrisimopoii8ike kwdikas apo tis diafaneies (lecture 6_)


public class MainSubscriber { //ilopoiisi android subscriber
    public static void subscribe(String clientId, String topic, MqttCallback main) {
        int qos = 2;
        String broker = "tcp://" + CommonSettings.ip + ":" +  (int)CommonSettings.port;
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            sampleClient.setCallback(main);
            sampleClient.connect(connOpts);
            sampleClient.subscribe(topic, qos);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println(" msg" + me.getMessage());
            System.out.println(" loc" + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println(" excep " + me);
            me.printStackTrace();
        }
    }
}

