package streamprocessor.api;

import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

public interface MessageResource {
    @Post("json")
    public Representation post(JsonRepresentation entity);
}