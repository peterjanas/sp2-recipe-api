package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

   // private final HotelRoute hotelRoute = new HotelRoute();


    public EndpointGroup getRoutes() {
        return () -> {
           // path("/hotels", hotelRoute.getRoutes());
        };
    }
}
