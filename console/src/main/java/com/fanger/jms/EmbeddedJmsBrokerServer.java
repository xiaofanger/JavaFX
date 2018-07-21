package com.fanger.jms;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;
import org.apache.activemq.store.memory.MemoryPersistenceAdapter;

import java.net.URI;

/**
 * 嵌入式的JMS ActiveMQ服务
 */
public class EmbeddedJmsBrokerServer {

    public static void main(String[] args) {
        final BrokerService broker = new BrokerService();

        try {
            // 配置broker
            broker.setBrokerName("what21.com");
            // 增加连接地址
            TransportConnector connector = new TransportConnector();
            connector.setUri(new URI("tcp://localhost:61616"));
            broker.addConnector(connector);
            // 增加连接地址
            broker.addConnector("tcp://localhost:61617");
            // 是否使用JMX
            broker.setUseJmx(true);
            broker.setDataDirectory("d:/data/activemq-data");
            broker.setUseShutdownHook(true);
            // broker.setPlugins(new BrokerPlugin[]{new JaasAuthenticationPlugin()});
            broker.setPersistenceAdapter(new MemoryPersistenceAdapter());
            broker.start();
            System.out.println("JMS broker started ...");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 关闭
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                broker.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.exit(0);
        }));

    }

}
