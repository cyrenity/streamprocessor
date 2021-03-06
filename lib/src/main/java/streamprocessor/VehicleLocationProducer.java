package streamprocessor;
import com.github.javafaker.Faker;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import streamprocessor.serdes.AppSerdes;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public final class VehicleLocationProducer {
    private final Producer<String, VehicleLocation> producer;

    public VehicleLocationProducer(String brokers) {

        Map<String, Object> serdeProps = new HashMap<>();

        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("key.serializer", StringSerializer.class);
        props.put("value.serializer", AppSerdes.VehicleLocation().serializer().getClass());
        producer = new KafkaProducer<>(props);

    }


    public void produce(int ratePerSecond) {
        long waitTimeBetweenIterationsMs = 1000L / (long) ratePerSecond;
        Faker faker = new Faker();
        int numberOfEvents = 100;
        int counter = 0;
        while(counter < numberOfEvents) {
            VehicleLocation fakeVehicleLocation = new VehicleLocation(new Random().nextInt(3) + 100, new Random().nextBoolean(), new Random().nextBoolean(), 35.73,78.29, faker.internet().ipV4Address());
            ProducerRecord<String, VehicleLocation> pr = new ProducerRecord<>("gpslocation", fakeVehicleLocation);
            Future<RecordMetadata> futureResult = producer.send(pr);
            try {
                Thread.sleep(waitTimeBetweenIterationsMs);
                futureResult.get();
            } catch (InterruptedException | ExecutionException e) {
                // deal with the exception
            }
            counter ++;
        }
    }
    public static void main(String[] args) {
        new VehicleLocationProducer("localhost:9092").produce(1);
    }

}

