package dat.routes;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.daos.impl.RecipeDAO;
import dat.entities.Recipe;
import io.javalin.Javalin;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class RecipeRouteTest {
    private static Javalin app;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static String BASE_URL = "http://localhost:7070/api";
    private static RecipeDAO recipeDao = RecipeDAO.getInstance(emf);
    private static Populate populator;


    @BeforeAll
    static void init() {
        HibernateConfig.setTest(true);
        app = ApplicationConfig.startServer(7070);

    }

    @BeforeEach
    void setUp() {
        populator = new Populate(emf);
        populator.populateData();
    }

    @AfterEach
    void tearDown() {
        populator.cleanupData();
    }

    @AfterAll
    static void closeDown() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void shouldReturnAllRecipes() {
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
    void shouldReturnRecipeWhenIdIsValid() {
                given()
                        .when()
                        .get(BASE_URL + "/recipes/" + recipeDao.read(1))
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract()
                        .as(Recipe.class);

        assertThat(recipeDao, equalTo(recipeDao.read(1)));
    }
/*
    @Test
    void shouldReturnNotFoundWhenIdIsInvalid() {
        given()
                .when()
                .get(BASE_URL + "/recipes/999")
                .then()
                .log().all()
                .statusCode(404);
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