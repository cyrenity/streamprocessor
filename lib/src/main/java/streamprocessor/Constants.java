package streamprocessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;


public final class Constants {
    private static final ObjectMapper jsonMapper;
    static {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDateFormat(new StdDateFormat());
        jsonMapper = mapper;
    }
    public static ObjectMapper getJsonMapper() {
        return jsonMapper;
    }
}