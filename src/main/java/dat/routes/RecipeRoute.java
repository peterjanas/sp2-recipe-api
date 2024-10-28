package dat.routes;

import dat.controllers.impl.RecipeController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class RecipeRoute
{
    private final RecipeController recipeController = new RecipeController();

    protected EndpointGroup getRoutes()
    {

        return () ->
        {
                get("/", recipeController::readAll, Role.ANYONE);
                get("/{id}", recipeController::read, Role.ANYONE);
                get("/name/{name}", recipeController::readByName, Role.ANYONE);
                get("/servings/{servings}", recipeController::readByServings, Role.ANYONE);

                post("/", recipeController::create, Role.ADMIN);
                put("/{id}", recipeController::update, Role.ADMIN);
                delete("/{id}", recipeController::delete, Role.ADMIN);
            };
        }
}
