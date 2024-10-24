package dat.daos.impl;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.dtos.IngredientDTO;
import dat.dtos.RecipeDTO;
import dat.dtos.RecipeIngredientDTO;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecipeDAOTest
{
    private static RecipeDAO recipeDAO;
    private static IngredientDAO ingredientDAO;
    private static EntityManagerFactory emf;
    private static Populate populate;

    private static List<RecipeDTO> recipes;

    @BeforeAll
    static void setUpAll()
    {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        recipeDAO = RecipeDAO.getInstance(emf);
        ingredientDAO = IngredientDAO.getInstance(emf);
        populate = new Populate(emf);
    }

    @BeforeEach
    void setUp()
    {
        populate.populateData();
    }

    @AfterEach
    void tearDown()
    {
        populate.cleanupData();
    }

    @AfterAll
    static void closeDown()
    {
        if (emf != null)
        {
            emf.close();
        }
    }


    @Test
    void readAll()
    {
        List<RecipeDTO> recipeDTOS = recipeDAO.readAll();
        assertNotNull(recipeDTOS);
        assertEquals(2, recipeDTOS.size());
    }

    @Test
    void create()
    {

        RecipeDTO recipeDTO = new RecipeDTO("Chicken and garlic", "4 servings", "Cook chicken, mix garlic in.");
        RecipeIngredientDTO chickenIngredient = new RecipeIngredientDTO(ingredientDAO.read(1), "250g");
        RecipeIngredientDTO garlicIngredient = new RecipeIngredientDTO(ingredientDAO.read(4), "2 cloves");
        recipeDTO.setRecipeIngredients(Set.of(chickenIngredient, garlicIngredient));
        RecipeDTO recipe = recipeDAO.create(recipeDTO);

        //check recipe
        assertNotNull(recipe);
        assertEquals(recipeDTO.getRecipeName(), recipe.getRecipeName());
        //check ingredient
        assertEquals(2, recipe.getRecipeIngredients().size());
        assertTrue(recipe.getRecipeIngredients().stream().anyMatch(ri -> ri.getIngredient().getIngredientName().equals("Chicken") && ri.getAmount().equals("250g")));
    }

    @Test
    void update()
    {
        RecipeDTO recipeDTO = new RecipeDTO("plain Chicken", "4 servings", "Cook chicken, rice, and mix.");
        RecipeDTO recipe = recipeDAO.create(recipeDTO);
        recipe.setRecipeName("onion Chicken");
        RecipeDTO updatedRecipe = recipeDAO.update(recipe.getId(), recipe);
        assertNotNull(updatedRecipe);
        assertEquals("onion Chicken", updatedRecipe.getRecipeName());

        //note that the recipe ingredients are not updated
    }

    @Test
    void delete()
    {
        RecipeDTO recipetbddeleted = recipeDAO.read(1);
        recipeDAO.delete(recipetbddeleted.getId());
        //recipeDAO.delete(recipeDAO.read(1).getId());
        List<RecipeDTO> recipeDTOS = recipeDAO.readAll();
        assertEquals(1, recipeDTOS.size());
    }

    @Test
    void readByName()
    {
    }

    @Test
    void readByServings()
    {
    }

}