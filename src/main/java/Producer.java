
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

public class Producer {
    public static void main(String[] args) {
        thread(new HelloWorldProducer(), false);
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class HelloWorldProducer implements Runnable {
        public void run() {
            try {
                // Create connection factory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");

                //Create a connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                // Create a session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create a destination (Queue or  Topic)
                Destination destination = session.createQueue("message.queue");

                // Create a message producer from the session to the Topic or Queue
                MessageProducer producer= session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

                // Create messages
                String text = "Hello World! from " + Thread.currentThread().getName() + " : " + this.hashCode();
                TextMessage message = session.createTextMessage(text);

                // Tell the producer to send message
                System.out.println("Sent messahe " + message.hashCode() + " : " + Thread.currentThread().getName());
                producer.send(message);

                // Clean
                session.close();
                connection.close();

            } catch (Exception e) {
                System.out.println("Exception " + e);
                e.printStackTrace();
            }
        }
    }
}
