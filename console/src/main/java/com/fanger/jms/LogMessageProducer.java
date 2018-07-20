package com.fanger.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 生产者服务的启动类
 * Created by Administrator on 2017/11/2.
 */
public class LogMessageProducer {

    private static final String url = "tcp://127.0.0.1:61616";
    private static final String topicName = "topic-test";

    private static Connection connection;
    private static Session session;
    private static MessageProducer producer;

    public static void getMessageProducer() {

        //1. 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        try {
            //2. 创建Connection
            connection = connectionFactory.createConnection();
            //3. 启动连接
            connection.start();

            //4. 创建会话
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            //5. 创建一个目标
            Destination destination = session.createTopic(topicName);

            //6. 创建一个生产者
            producer = session.createProducer(destination);
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public static void send(String message) throws JMSException {

        //7. 创建消息
        TextMessage textMessage = session.createTextMessage(message);

        //8. 发布消息
        producer.send(textMessage);

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