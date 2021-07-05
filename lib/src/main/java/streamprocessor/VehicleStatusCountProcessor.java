package streamprocessor;

import com.github.javafaker.Faker;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.model.CityResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.*;
import streamprocessor.aggregator.BillingCount;
import streamprocessor.extractors.GeoIPService;
import streamprocessor.serde.JsonPOJODeserializer;
import streamprocessor.serde.JsonPOJOSerializer;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

public final class VehicleStatusCountProcessor {
    private final String brokers;

    public VehicleStatusCountProcessor(String brokers) {
        super();
        this.brokers = brokers;
    }
    public final void process() {

        Map<String, Object> serdeProps = new HashMap<>();

        final Serializer<VehicleLocation> vehicleLocationSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", VehicleLocation.class);
        vehicleLocationSerializer.configure(serdeProps, false);

        final Deserializer<VehicleLocation> vehicleLocationDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", VehicleLocation.class);
        vehicleLocationDeserializer.configure(serdeProps, false);



        final Serializer<BillingCount> billingCountSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", BillingCount.class);
        billingCountSerializer.configure(serdeProps, false);

        final Deserializer<BillingCount> billingCountDeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", BillingCount.class);
        billingCountDeserializer.configure(serdeProps, false);




        final Serde<VehicleLocation> vehicleLocationSerde = Serdes.serdeFrom(vehicleLocationSerializer, vehicleLocationDeserializer);
        final Serde<BillingCount> billingCountSerde = Serdes.serdeFrom(billingCountSerializer, billingCountDeserializer);


        // Stream DSL
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<Integer, VehicleLocation> initialStream = streamsBuilder.stream("gpslocation", Consumed.with(Serdes.Integer(), vehicleLocationSerde));
        Faker faker = new Faker();

        initialStream
                .selectKey((k, v) -> v.getVehicleId())
                .peek((k, v) -> System.out.println(k +" - " +v))
                .groupByKey(Grouped.with(Serdes.Integer(), vehicleLocationSerde))
                .windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
                .aggregate(
                        () -> 0L, /* initializer */
                        (aggKey, newValue, aggValue) -> aggValue + 1,
                        Materialized.with(Serdes.Integer(), Serdes.Long())
                )
                .toStream()
                .map((k, v) -> new KeyValue<>(k, new BillingCount(k.window().start(), k.window().end(), v)))
                .peek((k, v) -> System.out.println(k + " -- " + v.toString()));


        // Build stream topology and start the stream processing engine!
        Topology topology = streamsBuilder.build();

        Properties props = new Properties();
        props.put("bootstrap.servers", this.brokers);
        props.put("application.id", "smsgwapp");
        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.setUncaughtExceptionHandler((exception -> StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD));

        System.out.println(topology.describe());

        streams.start();
    }
    public static void main(String[] args) {
        (new VehicleStatusCountProcessor("localhost:9092")).process();
    }
}