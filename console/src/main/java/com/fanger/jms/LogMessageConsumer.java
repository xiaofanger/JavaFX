package com.fanger.jms;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 队列模式消息消费者
 */
public class LogMessageConsumer {

    private static final String url = "tcp://127.0.0.1:61616";
    private static final String topicName = "topic-test";

    private static Connection connection;

    public static void read(ObservableList<String> items) throws JMSException {

        //1. 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);

        //2. 创建Connection
        connection = connectionFactory.createConnection();

        //3. 启动连接
        connection.start();

        //4. 创建会话
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //5. 创建一个目标
        Destination destination = session.createTopic(topicName);

        //6. 创建一个消费者
        MessageConsumer consumer = session.createConsumer(destination);

        //7. 创建一个监听器

        consumer.setMessageListener(message ->  {
                TextMessage textMessage = (TextMessage) message;
                Platform.runLater(() -> {
                    //更新JavaFX的主线程的代码放在此处
                    try {
                        if (items.size() > 1000) {
                            items.remove(0);
                        }
                        items.add(items.size(), textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                });
        });

    }

    public static void close() {
        //9. 关闭连接
        try {
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
