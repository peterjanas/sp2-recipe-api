package dat.routes;


import dat.controllers.impl.IngredientController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class IngredientRoute {

    private final IngredientController ingredientController = new IngredientController();

    protected EndpointGroup getRoutes()
    {
        return () ->
        {
            get("/", ingredientController::readAll, Role.ANYONE);
            get("/{id}", ingredientController::read, Role.ANYONE);
            get("/findrecipefromingredient/{ingredientName}", ingredientController::getRecipesByIngredientName, Role.ANYONE);
            post("/", ingredientController::create, Role.ADMIN);
            put("/{id}", ingredientController::update, Role.ADMIN);
            delete("/{id}", ingredientController::delete, Role.ADMIN);
        };
    }
}
