package dat.routes;


import io.javalin.apibuilder.EndpointGroup;

public class IngredientRoute {

    private final IngredientController ingredientController = new IngredientController();

    protected EndpointGroup getRoutes() {
/*
        return () -> {
            post("/", hotelController::create, Role.USER);
            get("/", hotelController::readAll);
            get("/{id}", hotelController::read);
            put("/{id}", hotelController::update);
            delete("/{id}", hotelController::delete);


        };
    */
            return null;
    }
}
