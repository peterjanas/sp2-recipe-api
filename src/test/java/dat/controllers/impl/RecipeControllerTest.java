package dat.controllers.impl;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.impl.IngredientDAO;
import dat.daos.impl.RecipeDAO;
import dat.dtos.IngredientDTO;
import dat.dtos.RecipeDTO;
import dat.dtos.RecipeIngredientDTO;
import dat.entities.Ingredient;
import dat.dtos.RecipeIngredientDTO;
import dat.entities.Recipe;
import dat.entities.RecipeIngredient;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import io.restassured.common.mapper.TypeRef;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.Matchers.not;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecipeControllerTest
{


    private final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private final static SecurityController securityController = SecurityController.getInstance();
    private final static SecurityDAO securityDAO = new SecurityDAO(emf);
    private static RecipeDAO recipeDao = RecipeDAO.getInstance(emf);
    private static IngredientDAO ingredientDAO = IngredientDAO.getInstance(emf);
    private static Javalin app;
    private static Recipe[] recipes;
    private static Recipe ChickenRice, GarlicChicken;
    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;
    private static final String BASE_URL = "http://localhost:7007/api";

    @BeforeAll
    void setUpAll()
    {
        HibernateConfig.setTest(true);

        // Start api
        app = ApplicationConfig.startServer(7007);
    }

    @BeforeEach
    void setUp()
    {
        // Populate the database with hotels and rooms
        recipes = Populate.populateData(emf);
        ChickenRice = recipes[0];
        GarlicChicken = recipes[1];
        UserDTO[] users = Populate.populateUsers(emf);
        userDTO = users[0];
        adminDTO = users[1];

        try
        {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        } catch (ValidationException e)
        {
            throw new RuntimeException(e);
        }

    }

    @AfterEach
    void tearDown()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Role").executeUpdate();
            em.createQuery("DELETE FROM RecipeIngredient").executeUpdate();
            em.createQuery("DELETE FROM Recipe").executeUpdate();
            em.createQuery("DELETE FROM Ingredient").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @AfterAll
    void tearDownAll()
    {
        ApplicationConfig.stopServer(app);
    }


    @Test
    void readAll()
    {
        List<RecipeDTO> recipeDTO =
                given()
                        .when()
                        .header("Authorization", userToken)
                        .get(BASE_URL + "/recipes")
                        .then()
                        .statusCode(200)
                        .body("size()", is(2))
                        .log().all()
                        .extract()
                        .as(new TypeRef<List<RecipeDTO>>()
                        {
                        });

        assertThat(recipeDTO.size(), is(2));
        assertThat(recipeDTO.get(0).getRecipeName(), is("Chicken and Rice"));
        assertThat(recipeDTO.get(1).getRecipeName(), is("Garlic Chicken"));
    }

    @Test
    void readByName()
    {
        String recipeName = "Garlic Chicken";
        List<RecipeDTO> recipeDTOs =
                given()
                        .when()
                        .header("Authorization", userToken)
                        .get(BASE_URL + "/recipes/name/" + recipeName)
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(new TypeRef<List<RecipeDTO>>()
                        {
                        });

        assertThat(recipeDTOs.size(), is(1));
        assertThat(recipeDTOs.get(0).getRecipeName(), is(recipeName));
    }

    @Test
    void readByServings()
    {
        String servings = "2 servings";
        List<RecipeDTO> recipeDTOs =
                given()
                        .when()
                        .header("Authorization", userToken)
                        .get(BASE_URL + "/recipes/servings/" + servings)
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(new TypeRef<List<RecipeDTO>>()
                        {
                        });

        assertThat(recipeDTOs.size(), is(1));
        assertThat(recipeDTOs.get(0).getServings(), is(servings));
    }

    @Test
    void create()
    {
        RecipeDTO newRecipe = new RecipeDTO("Garlic Super Chicken", "4 servings", "Cook chicken with garlic.");

        List<IngredientDTO> ingredients = ingredientDAO.readAll();
        RecipeIngredientDTO chickenIngredient = new RecipeIngredientDTO(ingredients.get(0), "250g");
        RecipeIngredientDTO garlicIngredient = new RecipeIngredientDTO(ingredients.get(3), "2 cloves");

        newRecipe.setRecipeIngredients(Set.of(chickenIngredient, garlicIngredient));

        RecipeDTO createdRecipe =
                given()
                        .contentType("application/json")
                        .header("Authorization", adminToken)
                        .body(newRecipe)
                        .when()
                        .post(BASE_URL + "/recipes")
                        .then()
                        .log().all()
                        .statusCode(201)
                        .extract()
                        .as(RecipeDTO.class);


        assertThat(createdRecipe.getRecipeName(), is(newRecipe.getRecipeName()));
        assertThat(createdRecipe.getServings(), is(newRecipe.getServings()));
        assertThat(createdRecipe.getInstructions(), is(newRecipe.getInstructions()));

    }

    @Test
    void update()
    {
        RecipeDTO chickenRice = recipeDao.readByName("Chicken and rice").get(0);
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
                        .header("Authorization", adminToken)
                        .contentType("application/json")
                        .body(chickenRice)
                        .when()
                        .put(BASE_URL + "/recipes/" + chickenRice.getId())
                        .then()
                        .statusCode(200)
                        .log().all()
                        .extract()
                        .as(Recipe.class);

        // Assert that the update was successful
        assertThat(updatedRecipe.getRecipeName(), equalTo("Chicken and Onion Stir-Fry"));
        assertThat(updatedRecipe.getServings(), equalTo("2 servings"));
        assertThat(updatedRecipe.getInstructions(), equalTo("Heat oil in a pan, sauté onions until translucent, add chicken and cook until golden brown, season to taste."));

        // Verify that the RecipeIngredients match by checking ingredient names and amounts
        List<String> ingredientNames = updatedRecipe.getRecipeIngredients().stream()
                .map(ri -> ri.getIngredient().getIngredientName())
                .toList();
        List<String> ingredientAmounts = updatedRecipe.getRecipeIngredients().stream()
                .map(RecipeIngredient::getAmount)
                .toList();

        // Expected names and amounts
        assertThat(ingredientNames, containsInAnyOrder("Chicken", "Rice", "Onion"));
        assertThat(ingredientAmounts, containsInAnyOrder("200g", "150g", "100g"));
    }


    @Test
    void delete()
    {
        RecipeDTO chickenRice = recipeDao.readByName("Chicken and Rice").get(0);

        given()
                .header("Authorization", adminToken)
                .when()
                .delete(BASE_URL + "/recipes/" + chickenRice.getId())
                .then()
                .log().all()
                .statusCode(204);

        List<RecipeDTO> remainingRecipes = recipeDao.readAll();
        assertThat(remainingRecipes.size(), is(1));
        assertThat(remainingRecipes.get(0).getRecipeName(), not(chickenRice.getRecipeName()));
    }
}