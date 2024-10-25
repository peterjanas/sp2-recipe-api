package dat.controllers.impl;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.dtos.RecipeDTO;
import dat.entities.Recipe;
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

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RecipeControllerTest {

    private final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private final static SecurityController securityController = SecurityController.getInstance();
    private final static SecurityDAO securityDAO = new SecurityDAO(emf);
    private static Javalin app;
    private static Recipe[] recipes;
    private static Recipe ChickenRice, GarlicChicken;
    private static UserDTO userDTO, adminDTO;
    private static String userToken, adminToken;
    private static final String BASE_URL = "http://localhost:7007/api";

    @BeforeAll
    void setUpAll() {
        HibernateConfig.setTest(true);

        // Start api
        app = ApplicationConfig.startServer(7007);
    }

    @BeforeEach
    void setUp() {
        // Populate the database with hotels and rooms
        recipes = Populate.populateData(emf);
        ChickenRice = recipes[0];
        GarlicChicken = recipes[1];
        UserDTO[] users = Populate.populateUsers(emf);
        userDTO = users[0];
        adminDTO = users[1];

        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        }
        catch (ValidationException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM RecipeIngredient").executeUpdate();
            em.createQuery("DELETE FROM Recipe").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    void tearDownAll() {
        ApplicationConfig.stopServer(app);
    }


    @Test
    void readAll() {
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
                        .as(new TypeRef<List<RecipeDTO>>() {});

        assertThat(recipeDTO.size(), is(2));
        assertThat(recipeDTO.get(0).getRecipeName(), is("Chicken and Rice"));
        assertThat(recipeDTO.get(1).getRecipeName(), is("Garlic Chicken"));
    }

    @Test
    void readByName() {
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
                        .as(new TypeRef<List<RecipeDTO>>() {});

        assertThat(recipeDTOs.size(), is(1));
        assertThat(recipeDTOs.get(0).getRecipeName(), is(recipeName));
    }

    @Test
    void readByServings() {
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
                        .as(new TypeRef<List<RecipeDTO>>() {});

        assertThat(recipeDTOs.size(), is(1));
        assertThat(recipeDTOs.get(0).getServings(), is(servings));
    }

    @Test
    void create()
    {
    }

    @Test
    void update()
    {
    }

    @Test
    void delete()
    {
    }
}