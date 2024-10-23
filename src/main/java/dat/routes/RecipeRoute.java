package dat.routes;

import dat.controllers.impl.RecipeController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class RecipeRoute
{
    private final RecipeController recipeController = new RecipeController();

    protected EndpointGroup getRoutes()
    {

        return () ->
        {
                get("/", recipeController::readAll);
                get("/{id}", recipeController::read);
                get("/name/{name}", recipeController::readByName);
                get("/servings/{servings}", recipeController::readByServings);

                post("/", recipeController::create);
                put("/{id}", recipeController::update);
                delete("/{id}", recipeController::delete);
            };
        }
}
