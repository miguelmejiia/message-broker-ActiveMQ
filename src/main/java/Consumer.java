import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Consumer {
    public static void main(String[] args) {
        thread(new HelloWorldConsumer(), false);
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class HelloWorldConsumer implements Runnable {
        public void run() {
            try {
                // Create connection factory
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");

                // Create a connection
                Connection connection = connectionFactory.createConnection();
                connection.start();

                // Create session
                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                // Create destination
                Destination destination = session.createQueue("message.queue");

                // Create message consumer from the sessionj tp the Topic or Queue
                MessageConsumer consumer = session.createConsumer(destination);

                // Wait form a message
                Message message = consumer.receive(1000);

                if(message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    String text = textMessage.getText();
                    System.out.println("Received: " + text);
                } else {
                    System.out.println("Received: " + message);
                }

            } catch (Exception e) {

            }
        }
    }
}
