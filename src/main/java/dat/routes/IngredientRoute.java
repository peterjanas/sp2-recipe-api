package dat.routes;


import dat.controllers.impl.IngredientController;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class IngredientRoute {

    private final IngredientController ingredientController = new IngredientController();

    protected EndpointGroup getRoutes()
    {
        return () ->
        {
            get("/findrecipefromingredient/{id}", ingredientController::getRecipesByIngredientName);
            post("/", ingredientController::create);
            put("/{id}", ingredientController::update);
            delete("/{id}", ingredientController::delete);
        };
    }
}
