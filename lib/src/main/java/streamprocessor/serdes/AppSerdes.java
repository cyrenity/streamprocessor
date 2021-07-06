package streamprocessor.serdes;



import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import streamprocessor.VehicleLocation;
import streamprocessor.aggregator.BillingCount;
import streamprocessor.serializers.JsonDeserializer;
import streamprocessor.serializers.JsonSerializer;

import java.util.HashMap;
import java.util.Map;


public class AppSerdes {

    static final class VehicleLocationSerde extends Serdes.WrapperSerde<VehicleLocation> {
        VehicleLocationSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    public static Serde<VehicleLocation> VehicleLocation() {
        VehicleLocationSerde serde = new VehicleLocationSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, VehicleLocation.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }

    static final class BillingCountSerde extends Serdes.WrapperSerde<BillingCount> {
        BillingCountSerde() {
            super(new JsonSerializer<>(), new JsonDeserializer<>());
        }
    }

    public static Serde<BillingCount> BillingCount() {
        BillingCountSerde serde = new BillingCountSerde();

        Map<String, Object> serdeConfigs = new HashMap<>();
        serdeConfigs.put(JsonDeserializer.VALUE_CLASS_NAME_CONFIG, BillingCount.class);
        serde.configure(serdeConfigs, false);

        return serde;
    }

}
