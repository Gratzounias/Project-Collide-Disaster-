package mosquito;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import usersettings.CommonSettings;
import userstate.CommonState;

//xrisimopoii8ike kwdikas apo tis diafaneies (lecture 6_)

public class MainPublisher { //ilopoiisi android Publisher
    public void publish(String clientId, String topic, String content) {
        int qos = 2;
        String broker = "tcp://" + CommonSettings.ip + ":" + (int)CommonSettings.port;
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();

            connOpts.setCleanSession(true);

            sampleClient.connect(connOpts);


            MqttMessage message = new MqttMessage(content.getBytes());

            message.setQos(qos);

            sampleClient.publish(topic, message);

            System.out.println("Message published");

            sampleClient.disconnect();

            System.out.println("Disconnected");

        } catch (MqttException me) {

            System.out.println("reason " + me.getReasonCode());

            System.out.println("msg" + me.getMessage());

            System.out.println("loc " + me.getLocalizedMessage());

            System.out.println("cause " + me.getCause());

            System.out.println("excep" + me);

            me.printStackTrace();
        }
    }

    //apostoli minimatos
    public String formatMessage(String uniqueId, float prox, float accx, float accy, float accz, float lat, float lng, long dt) {
        String s= uniqueId + "  " + prox + "  " + accx + "  " + accy + "  " + accz + "  " + lat + "  " + lng + "  " + dt;
        return s;

    }

}
