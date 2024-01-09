package hr.fer.tel.rassus.stupidudp.client;

import java.util.Properties;
import java.util.Scanner;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class Producer {

    public static void main(String args[])  {

        Properties producerProperties = new Properties();
        producerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties);
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your message to the Command topic >> ");

        String reading = scanner.nextLine();
        
        while(!reading.equals("exit")) {
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<>("Command", reading);
            producer.send(producerRecord);
            System.out.println("Enter your message to the Command topic or exit >> ");
            reading = scanner.nextLine();
        }
        


        
        
    }
}
