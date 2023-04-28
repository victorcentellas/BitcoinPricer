
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;
import java.time.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import okhttp3.*;
import org.json.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class SimpleProducer {

	static Scanner topic = new Scanner(System.in);

	public static void main(String[] args) throws Exception {
		System.out.println("Introduzca el nombre del topic:");
		String topicName = topic.next();
		while (true) {
			String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder().url(url).build();
			Response responses = null;

			try {
				responses = client.newCall(request).execute();
			} catch (IOException e) {
				e.printStackTrace();
			}
			String jsonData = responses.body().string();
			JSONObject BitcoinJsonObject = new JSONObject(jsonData);
			
			JSONObject bpi = BitcoinJsonObject.getJSONObject("bpi");
			JSONObject eur = bpi.getJSONObject("EUR");
			float bitcoinPriceInEur = eur.getFloat("rate_float");
			BigDecimal EurPrice = new BigDecimal(bitcoinPriceInEur);
			EurPrice = EurPrice.setScale(2, RoundingMode.DOWN);
			
			JSONObject time = BitcoinJsonObject.getJSONObject("time");
			String DatePriceinEur = time.getString("updatedISO");


			Properties props = new Properties();

			props.put("bootstrap.servers", "localhost:9092");
			props.put("acks", "all");

			props.put("retries", 0);

			props.put("batch.size", 16384);

			props.put("linger.ms", 1);

		
			props.put("buffer.memory", 33554432);

			props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

			props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

			Producer<String, String> producer = new KafkaProducer<String, String>(props);
			producer.send(new ProducerRecord<String, String>(topicName, DatePriceinEur, EurPrice.toPlainString()));
			System.out.println("Message sent successfully <" + DatePriceinEur + "," + EurPrice.toPlainString() + ">");

			TimeUnit.SECONDS.sleep(1);

		}

	}

}
