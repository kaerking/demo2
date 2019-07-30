package com.example.demo.rabbitmqssl;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

public class ValidatingCert{
    public static void main(String[] args) throws Exception{
        char[] keyPassphrase = "rabbit".toCharArray();  //证书密码
        KeyStore ks = KeyStore.getInstance("PKCS12");
        ks.load(new FileInputStream("/home/ubuntu/work/rabbitMQ/CMF-AMQP-Configuration/ssl/client/rabbit-client1.keycert.p12"), keyPassphrase);//把client目录keycert.p12拷贝到项目里面
        //ks.load(new FileInputStream("/home/ubuntu/work/rabbitMQ/rabbitdir/icekeystore.jks"),keyPassphrase);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, keyPassphrase);

        char[] trustPassphrase = "rabbit".toCharArray();  //证书密码
        KeyStore tks = KeyStore.getInstance("JKS");
        tks.load(new FileInputStream("/home/ubuntu/work/rabbitMQ/rabbitdir/trustStore.jks"), trustPassphrase);//把/usr/testca/目录的trustStore拷贝到项目里面
        //tks.load(new FileInputStream("/home/ubuntu/work/rabbitMQ/CMF-AMQP-Configuration/ssl/client/rabbit-client1.keycert.p12"), keyPassphrase);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(tks);
        SSLContext c = SSLContext.getInstance("TLSv1.1");
        c.init(null, tmf.getTrustManagers(), null);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");//rabbitmq server
        factory.setPort(5671);
        factory.useSslProtocol(c);
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();
        channel.queueDeclare("rabbitmq-queue", false, true, true, null); //rabbitmq-queue是rabbitmq队列
        channel.basicPublish("", "rabbitmq-queue", null, "Test,Test".getBytes());
        GetResponse chResponse = channel.basicGet("rabbitmq-queue", false);
        if (chResponse == null){
            System.out.println("No message retrieved");
        }else {
            byte[] body = chResponse.getBody();
            System.out.println("Recieved: " + new String(body));
        }
        channel.close();
        conn.close();
    }
}
