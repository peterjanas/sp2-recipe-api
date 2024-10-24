package dat.routes;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.daos.impl.RecipeDAO;
import dat.dtos.RecipeDTO;
import dat.entities.Recipe;
import io.javalin.Javalin;
import io.restassured.response.Response;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

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
    void shouldReturnAllRecipes()
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
    void shouldReturnRecipeWhenIdIsValid()
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
    void shouldAddNewRecipeSuccessfully() {
        Recipe r3 = new Recipe("Recipe 3", "test serving", "Description 3");
        Recipe createdRecipe =
                given()
                        .contentType("application/json")
                        .body(r3)
                        .when()
                        .post(BASE_URL + "/recipes")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .as(Recipe.class);

        assertThat(createdRecipe.getRecipeName(), equalTo(r3.getRecipeName()));
        assertThat(createdRecipe.getInstructions(), equalTo(r3.getInstructions()));
    }
/*
    @Test
    void shouldUpdateRecipeSuccessfully() {
        chickenRice.setRecipeName("Chicken Rice Updated");
        Recipe updatedRecipe =
                given()
                        .contentType("application/json")
                        .body(chickenRice)
                        .when()
                        .put(BASE_URL + "/recipes/" + chickenRice.getId())
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(Recipe.class);

        assertThat(updatedRecipe.getRecipeName(), equalTo(chickenRice.getRecipeName()));
        assertThat(updatedRecipe.getInstructions(), equalTo(chickenRice.getInstructions()));
    }

    @Test
    void shouldDeleteRecipeSuccessfully() {
        given()
                .when()
                .delete(BASE_URL + "/recipes/" + chickenRice.getId())
                .then()
                .log().all()
                .statusCode(204);

        Recipe[] recipeDTOS =
                given()
                        .when()
                        .get(BASE_URL + "/recipes")
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(Recipe[].class);

        assertThat(recipeDTOS.length, is(1));
        assertThat(recipeDTOS[0], equalTo(garlicChicken));
    }
 */
    }