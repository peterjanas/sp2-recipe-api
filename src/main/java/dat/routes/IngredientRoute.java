package dat.routes;


import dat.controllers.impl.IngredientController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.post;

public class IngredientRoute {

    private final IngredientController ingredientcontroller = new IngredientController();

    protected EndpointGroup getRoutes()
    {

        return () ->
        {
            get("/findfromingredient/{id}", IngredientController::read);

            post("/", IngredientController::create);
            put("/{id}", IngredientController::update);
            delete("/{id}", IngredientController::delete);
        };
    }
}
