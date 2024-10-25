package dat.controllers.impl;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.entities.Recipe;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HotelControllerTest {

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
        app = ApplicationConfig.startServer(7070);
    }

    @BeforeEach
    void setUp() {
        // Populate the database with hotels and rooms
        System.out.println("Populating database with hotels and rooms");
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
    void readAll()
    {
    }

    @Test
    void readByName()
    {
    }

    @Test
    void readByServings()
    {
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