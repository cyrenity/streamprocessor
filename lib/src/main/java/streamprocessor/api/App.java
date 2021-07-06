package streamprocessor.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.resource.Directory;
import org.restlet.routing.Router;

public class App extends Application {

    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());

        // Serve the files generated by the GWT compilation step.
        router.attachDefault(new Directory(getContext(), "war:///"));
        router.attach("/broadcast", MessageServerResource.class);

        return router;
    }
}