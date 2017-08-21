package mosquito;

import com.mysql.cj.mysqlx.protobuf.MysqlxCrud;
import javafx.scene.control.TextField;
import mysql.SearchInDatabase;
import mysql.UpdateDatabase;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

// elegxei gia sigkrouseis vasei twn timwn twn termatikwn
// stelnei t apotelesmata tou an einai epibebaiwmeni sigrousi 'i oxi

public class MainPublisher implements Runnable {
    public static final String TERM1 = "4fa2064ab69ede57";
    public static final String TERM2 = "a07ea291c136cb8f";
    public static final String TERM1ANDTERM2 = "TERM1ANDTERM2";

    private TextField prox_freq;
    private TextField ip;
    private TextField port;

    public void publish(String broker, String content) {
        String topic = "apantisi";
        int qos = 2;
        String clientId = "JavaSamplePublisher";
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();

            connOpts.setCleanSession(true);

            sampleClient.connect(connOpts);

            System.out.println("Publishing message: " + content);

            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);

            sampleClient.disconnect();
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg" + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep" + me);

            me.printStackTrace();
        }
    }

    public void publishConfirmed(String broker) {
        publish(broker, TERM1ANDTERM2);
    }

    public void publishTerm1(String broker) {
        publish(broker,TERM1);
    }

    public void publishTerm2(String broker) {
        publish(broker,TERM2);
    }

    @Override
    public void run() {
        int prox_freq_v;
        String broker;

        try {

            SearchInDatabase searcher = new SearchInDatabase();
            UpdateDatabase updater = new UpdateDatabase();

            while (!Thread.currentThread().isInterrupted()) {//oso ekteeitai to thread

                try {
                    prox_freq_v = Integer.parseInt(prox_freq.getText());
                } catch (NumberFormatException e) {
                    System.out.println("Frequency not defined, loop ignored");
                    prox_freq_v = 1;
                }

                try {
                    broker = "tcp://" + ip.getText().trim() + ":" + port.getText().trim();
                } catch (NumberFormatException e) {
                    System.out.println("Frequency not defined, loop ignored");
                    broker = "tcp://localhost:1883";
                }

                if (prox_freq_v > 0) {
                    Thread.sleep(prox_freq_v*500); //kane sleep gia 500 ms
                    //koimatai gia miso alla deigmatoleiptei-elegxei gia 1 sec -> prox_thres_v


                    long[] ids = searcher.fetchNearRows(prox_freq_v);//an iparxei neo simvan
                    if (ids != null) {
                        updater.update(ids[0]);//grafto st db
                        updater.update(ids[1]);

                        publishConfirmed(broker);//publish
                    } else { // check 1st terminal value
                        long id = searcher.fetchForTerminal1(prox_freq_v);
                        if (id > 0) {
                            System.out.println("line found for term1 ");
                            publishTerm1(broker);
                        }

                        id = searcher.fetchForTerminal2(prox_freq_v); //check 2nd terminal value
                        if (id > 0) {
                            System.out.println("line found for term2 ");
                            publishTerm2(broker);
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            return;
        }
    }

    public void main(TextField prox_freq,TextField ip , TextField port) {
        this.prox_freq = prox_freq;
        this.ip = ip;
        this.port = port;

        Thread t = new Thread(this);
        t.start();
    }
}
