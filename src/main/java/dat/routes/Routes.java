package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes
{

    private final IngredientRoute IngredientRoute = new IngredientRoute();
    private final RecipeRoute RecipeRoute = new RecipeRoute();


    public EndpointGroup getRoutes()
    {
        return () ->
        {
            path("/ingredients", IngredientRoute.getRoutes());
            path("/recipes", RecipeRoute.getRoutes());
        };
    }
}
