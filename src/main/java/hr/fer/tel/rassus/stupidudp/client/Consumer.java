package hr.fer.tel.rassus.stupidudp.client;

import java.time.Duration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.*;

public class Consumer implements Runnable {

	private final List<String> TOPICS = new LinkedList<String>(Arrays.asList("Command", "Register"));
	
	private StartStop s;
	private Collection<CvorStruktura> listaSusjeda;
	private CvorStruktura cvor;

	

	public Consumer(StartStop s, Collection<CvorStruktura> listaSusjeda, CvorStruktura cvor) {
		super();
		this.s = s;
		this.listaSusjeda = listaSusjeda;
		this.cvor = cvor;
	}



	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	
		System.out.println(cvor.id + " " + cvor.address+ " " + cvor.port);
        Properties consumerProperties = new Properties();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        KafkaConsumer<String, String> consumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(consumerProperties);
        consumer.subscribe(TOPICS);

        Properties producerProperties = new Properties();
        producerProperties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        producerProperties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerProperties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(producerProperties);
        
        System.out.println("Waiting for messaged to arrive on topics: " + TOPICS.toString());

        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));

            consumerRecords.records("Register").forEach(record -> {
            	try {
					JSONObject obj = new JSONObject(record.value());
					if(!obj.get("port").equals(cvor.port))
						listaSusjeda.add(new CvorStruktura(obj.get("id").toString(), obj.get("address").toString(), obj.get("port").toString()));
				} catch (JSONException e) {
					e.printStackTrace();
				}
            });
            
            consumerRecords.records("Command").forEach(record -> {
            	if (record.value().equals("stop")) {
            		this.s.stopped = true;
            		return;
            	}
            	if (record.value().equals("start")) {
                    ProducerRecord<String, String> producerRecord =
                            new ProducerRecord<>("Register", (new JSONObject(cvor)).toString());
                    producer.send(producerRecord);
            		this.s.started = true;
            		System.out.println("start");
            	}
            });
            
            consumer.commitAsync();
        }
	}

}
