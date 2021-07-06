package streamprocessor;

import com.github.javafaker.Faker;
import com.maxmind.geoip2.model.CityResponse;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsUncaughtExceptionHandler;
import org.apache.kafka.streams.kstream.*;
import streamprocessor.aggregator.BillingCount;
import streamprocessor.extractors.GeoIPService;
import streamprocessor.serdes.AppSerdes;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public final class VehicleStatusCountProcessor {
    private final String brokers;

    public VehicleStatusCountProcessor(String brokers) {
        super();
        this.brokers = brokers;
    }
    public final void process() {

        Map<String, Object> serdeProps = new HashMap<>();

        // Stream DSL
        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<Integer, VehicleLocation> initialStream = streamsBuilder.stream("gpslocation", Consumed.with(Serdes.Integer(), AppSerdes.VehicleLocation()));
        Faker faker = new Faker();

        initialStream
                .mapValues(v -> {
                    String country = null;
                    try {
                        CityResponse cityResponse = new GeoIPService().getLocation(v.getIpAddress());
                        country = cityResponse.getCountry().getName();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        country = "N/a";
                    }
                    v.setCountry(country);
                    return v;
                })
                .selectKey((k, v) -> v.getVehicleId())
                .peek((k, v) -> System.out.println(k +" - " +v.getCountry()))
                .groupByKey(Grouped.with(Serdes.Integer(), AppSerdes.VehicleLocation()))
                .windowedBy(TimeWindows.of(Duration.ofMinutes(1)))
                .aggregate(
                        () -> 0L, /* initializer */
                        (aggKey, newValue, aggValue) -> aggValue + 1,
                        Materialized.with(Serdes.Integer(), Serdes.Long())
                )
                .toStream()
                .map((k, v) -> new KeyValue<>(k.key().toString(), new BillingCount(k.window().start(), k.window().end(), v)))
                .peek((k, v) -> System.out.println(k + " -- " + v.toString()))
                .to("new_counts", Produced.with(Serdes.String(), AppSerdes.BillingCount()));

        // Build stream topology and start the stream processing engine!
        Topology topology = streamsBuilder.build();

        Properties props = new Properties();
        props.put("bootstrap.servers", this.brokers);
        props.put("application.id", "streamprocessor");
        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.setUncaughtExceptionHandler((exception -> StreamsUncaughtExceptionHandler.StreamThreadExceptionResponse.REPLACE_THREAD));

        System.out.println(topology.describe());

        streams.start();
    }
    public static void main(String[] args) {
        (new VehicleStatusCountProcessor("localhost:9092")).process();
    }
}