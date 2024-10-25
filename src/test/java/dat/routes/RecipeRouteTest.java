package dat.routes;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.daos.impl.IngredientDAO;
import dat.daos.impl.RecipeDAO;
import dat.dtos.IngredientDTO;
import dat.dtos.RecipeDTO;
import dat.dtos.RecipeIngredientDTO;
import dat.entities.Ingredient;
import dat.entities.Recipe;
import dat.entities.RecipeIngredient;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class RecipeRouteTest
{
    private static Javalin app;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static String BASE_URL = "http://localhost:7007/api";
    private static RecipeDAO recipeDao = RecipeDAO.getInstance(emf);
    private static IngredientDAO ingredientDAO = IngredientDAO.getInstance(emf);
    private static Populate populator;



    @BeforeAll
    static void init()
    {
        HibernateConfig.setTest(true);
        app = ApplicationConfig.startServer(7007);

    }

    @BeforeEach
    void setUp()
    {
        populator = new Populate(emf);
        populator.populateData();
    }

    @AfterEach
    void tearDown()
    {
        populator.cleanupData();
    }

    @AfterAll
    static void closeDown()
    {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void testReturnAllRecipes()
    {
        given()
                .when()
                .get(BASE_URL + "/recipes")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(Recipe[].class);

        assertThat(recipeDao.readAll().size(), is(2));
    }

    @Test
    void testReturnRecipeWhenIdIsValid()
    {
        // Retrieve the expected recipe from the database
        RecipeDTO expectedRecipe = recipeDao.read(1);  // Get the recipe with ID 1 from the DAO

        // Fetch the recipe from the API using the recipe ID
        Recipe recipeDTO =
                given()
                        .when()
                        .get(BASE_URL + "/recipes/" + expectedRecipe.getId())  // Use the ID from the DAO
                        .then()
                        .log().all()
                        .statusCode(200)  // Ensure the API returns 200 OK
                        .extract()
                        .as(Recipe.class);  // Extract the response as a Recipe object

        // Compare the recipe fetched from the API with the one from the DAO
        assertThat(recipeDTO.getRecipeName(), equalTo(expectedRecipe.getRecipeName()));
        assertThat(recipeDTO.getInstructions(), equalTo(expectedRecipe.getInstructions()));
    }

    @Test
    void testAddNewRecipe()
    {
        RecipeDTO newRecipe = new RecipeDTO("Garlic Super Chicken", "4 servings", "Cook chicken with garlic.");

        RecipeIngredientDTO chickenIngredient = new RecipeIngredientDTO(ingredientDAO.read(1), "250g");
        RecipeIngredientDTO garlicIngredient = new RecipeIngredientDTO(ingredientDAO.read(4), "2 cloves");

        newRecipe.setRecipeIngredients(Set.of(chickenIngredient, garlicIngredient));

        // Send a POST request to add the new recipe
        RecipeDTO createdRecipe =
                given()
                        .contentType("application/json")
                        .body(newRecipe) // Use the DTO object as the request body
                        .when()
                        .post(BASE_URL + "/recipes") // Assuming your endpoint is /recipes
                        .then()
                        .log().all()
                        .statusCode(201)  // Expecting 201 Created
                        .extract()
                        .as(RecipeDTO.class);  // Extract the response as a RecipeDTO


        // Verify that the created recipe matches the input recipe data
        assertThat(createdRecipe.getRecipeName(), equalTo(newRecipe.getRecipeName()));
        assertThat(createdRecipe.getInstructions(), equalTo(newRecipe.getInstructions()));
        assertThat(createdRecipe.getServings(), equalTo(newRecipe.getServings()));
    }


    @Test
    void UpdateRecipe() {
        RecipeDTO chickenRice = recipeDao.read(1);
        assertNotNull(chickenRice, "Recipe should not be null");


        chickenRice.setRecipeName("Chicken and Onion Stir-Fry");
        chickenRice.setServings("2 servings");
        chickenRice.setInstructions("Heat oil in a pan, sauté onions until translucent, add chicken and cook until golden brown, season to taste.");

        IngredientDTO chickenIngredient = ingredientDAO.readByName("Chicken").get(0);
        IngredientDTO riceIngredient = ingredientDAO.readByName("Rice").get(0);
        IngredientDTO onionIngredient = ingredientDAO.readByName("Onion").get(0);
        assertNotNull(chickenIngredient, "Chicken ingredient should not be null");
        assertNotNull(onionIngredient, "Onion ingredient should not be null");


        RecipeIngredientDTO chickenRecipeIngredient = new RecipeIngredientDTO(chickenIngredient, "200g");
        RecipeIngredientDTO riceRecipeIngredient = new RecipeIngredientDTO(riceIngredient, "150g");
        RecipeIngredientDTO onionRecipeIngredient = new RecipeIngredientDTO(onionIngredient, "100g");

        chickenRice.setRecipeIngredients(Set.of(chickenRecipeIngredient, riceRecipeIngredient, onionRecipeIngredient));

        Recipe updatedRecipe =
                given()
                        .contentType("application/json")
                        .body(chickenRice)
                        .when()
                        .put(BASE_URL + "/recipes/1")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(Recipe.class);

        assertThat(updatedRecipe.getRecipeName(), equalTo("Chicken and Onion Stir-Fry"));
        assertThat(updatedRecipe.getServings(), equalTo("2 servings"));
        assertThat(updatedRecipe.getInstructions(), equalTo("Heat oil in a pan, sauté onions until translucent, add chicken and cook until golden brown, season to taste."));
    }

    @Test
    void shouldDeleteRecipeSuccessfully() {

        RecipeDTO chickenRice = recipeDao.read(1);

        given()
                .when()
                .delete(BASE_URL + "/recipes/" + chickenRice.getId())
                .then()
                .log().all()
                .statusCode(204);

        assertThat(recipeDao.readAll().size(), is(1));
    }

}