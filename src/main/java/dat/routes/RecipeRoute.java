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
                get("/", recipeController::readAll, Role.USER);
                get("/{id}", recipeController::read, Role.USER);
                get("/name/{name}", recipeController::readByName, Role.USER);
                get("/servings/{servings}", recipeController::readByServings, Role.USER);

                post("/", recipeController::create, Role.ADMIN);
                put("/{id}", recipeController::update, Role.ADMIN);
                delete("/{id}", recipeController::delete, Role.ADMIN);
            };
        }
}
