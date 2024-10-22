package dat.routes;


import dat.controllers.impl.Ingredientcontroller;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class IngredientRoute {

    private final Ingredientcontroller ingredientcontroller = new Ingredientcontroller();

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
