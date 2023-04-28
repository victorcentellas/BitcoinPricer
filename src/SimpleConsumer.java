
import java.util.Properties;
import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import okhttp3.*;
import org.json.*;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.Timer;
import javax.swing.WindowConstants;

public class SimpleConsumer {

	static Scanner topic = new Scanner(System.in);
	

	public static void main(String[] args) throws Exception {

		System.out.println("Introduzca el nombre del topic:");
		String topicName = topic.next();
		// Kafka consumer configuration settings
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "test");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "1000");
		props.put("session.timeout.ms", "30000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

		// Kafka Consumer subscribes list of topics here.
		consumer.subscribe(Arrays.asList(topicName));

		// print the topic name
		System.out.println("Subscribed to topic " + topicName);

		RealTimeChart chart = new RealTimeChart("Bitcoin Price Evolution");
		final JFrame frame = new JFrame("Bitcoin Price");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(chart, BorderLayout.CENTER);
		frame.pack();
		Timer timer = new Timer(1000, null);
		timer.start();
		frame.setVisible(true);

		while (true) {
			ConsumerRecords<String, String> records = consumer.poll(100);
			for (ConsumerRecord<String, String> record : records) {
				// print the offset,key and value for the consumer records.

				System.out.printf("offset = %d, key = %s, value = %s\n", record.offset(), record.key(), record.value());
				chart.update(Float.parseFloat(record.value()));
			}

		}

	}

}
