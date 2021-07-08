package streamprocessor.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.restlet.Component;
import org.restlet.data.Protocol;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ServerResource;
import streamprocessor.Message;
import streamprocessor.MessageResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MessageServerResource extends ServerResource implements MessageResource {

    public static void main(String[] args) throws Exception {
        // Create a new Restlet component and add a HTTP server connector to it
        Component component = new Component();
        component.getServers().add(Protocol.HTTP, 8182);
        // Then attach it to the local host
        component.getDefaultHost().attach("/broadcast", MessageServerResource.class);
        // Now, let's start the component!
        // Note that the HTTP server connector is also automatically started.
        component.start();
    }

    @Override
    public Representation post(JsonRepresentation entity) {
        MessageResponse msgResponse = null;
        try {

            ObjectMapper objectMapper = new ObjectMapper();
            Message req = objectMapper.readValue(entity.getText(), Message.class);

            // Business Logic starts here
            String text = "Message sent successfully to:  " + req.getTo();
            String status = "queued";
            // Business Logic ends here


            msgResponse = new MessageResponse(status, text);
        } catch (UnrecognizedPropertyException e) {
            msgResponse = new MessageResponse("error", "Invalid field: " + e.getPropertyName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Let's prepare the response now
        JsonRepresentation response = null;
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a z");
            response = new JsonRepresentation(
                    new ObjectMapper()
                            .setDateFormat(df)
                            .writeValueAsString(msgResponse)
            );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return response;
    }


}