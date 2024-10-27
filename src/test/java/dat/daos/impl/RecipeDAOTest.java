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
        populate = new Populate();
    }

    @BeforeEach
    void setUp()
    {
        populate.populateData(emf);
    }

    @AfterEach
    void tearDown()
    {
        populate.cleanupData(emf);
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
        // Dynamically retrieve ingredients by their names using readByName
        IngredientDTO chickenIngredient = ingredientDAO.readByName("Chicken").get(0);
        IngredientDTO garlicIngredient = ingredientDAO.readByName("Garlic").get(0);

        RecipeDTO recipeDTO = new RecipeDTO("Chicken and garlic", "4 servings", "Cook chicken, mix garlic in.");
        RecipeIngredientDTO chickenRecipeIngredient = new RecipeIngredientDTO(chickenIngredient, "250g");
        RecipeIngredientDTO garlicRecipeIngredient = new RecipeIngredientDTO(garlicIngredient, "2 cloves");
        recipeDTO.setRecipeIngredients(Set.of(chickenRecipeIngredient, garlicRecipeIngredient));

        RecipeDTO recipe = recipeDAO.create(recipeDTO);

        // Check the recipe
        assertNotNull(recipe);
        assertEquals(recipeDTO.getRecipeName(), recipe.getRecipeName());
        // Check the ingredients
        assertEquals(2, recipe.getRecipeIngredients().size());
        assertTrue(recipe.getRecipeIngredients().stream()
                .anyMatch(ri -> ri.getIngredient().getIngredientName().equals("Chicken") && ri.getAmount().equals("250g")));
        assertTrue(recipe.getRecipeIngredients().stream()
                .anyMatch(ri -> ri.getIngredient().getIngredientName().equals("Garlic") && ri.getAmount().equals("2 cloves")));
    }


    @Test
    void update()
    {
        // Dynamically retrieve the recipe by name using readByName
        RecipeDTO recipe = recipeDAO.readByName("Chicken and Rice").get(0);  // Adjust to match the exact name

        // Modify the recipe's name and ingredients
        recipe.setRecipeName("Onion Chicken");

        // Dynamically retrieve ingredients for the update
        IngredientDTO chickenIngredient = ingredientDAO.readByName("Chicken").get(0);
        IngredientDTO onionIngredient = ingredientDAO.readByName("Onion").get(0);

        RecipeIngredientDTO updatedChickenIngredient = new RecipeIngredientDTO(chickenIngredient, "250g");
        RecipeIngredientDTO updatedOnionIngredient = new RecipeIngredientDTO(onionIngredient, "1 large");
        recipe.setRecipeIngredients(Set.of(updatedChickenIngredient, updatedOnionIngredient));  // Update ingredients

        // Perform the update
        RecipeDTO updatedRecipe = recipeDAO.update(recipe.getId(), recipe);

        // Verify the updated recipe is not null
        assertNotNull(updatedRecipe);
        // Verify the recipe name is updated
        assertEquals("Onion Chicken", updatedRecipe.getRecipeName());
        // Verify the recipe ingredients are updated
        assertEquals(2, updatedRecipe.getRecipeIngredients().size());
        assertTrue(updatedRecipe.getRecipeIngredients().stream()
                .anyMatch(ri -> ri.getIngredient().getIngredientName().equals("Chicken") && ri.getAmount().equals("250g")));
        assertTrue(updatedRecipe.getRecipeIngredients().stream()
                .anyMatch(ri -> ri.getIngredient().getIngredientName().equals("Onion") && ri.getAmount().equals("1 large")));
    }



    @Test
    void delete()
    {
        List<RecipeDTO> recipeDTOS = recipeDAO.readAll();
        RecipeDTO recipetbddeleted = recipeDTOS.get(1);
        recipeDAO.delete(recipetbddeleted.getId());
        List<RecipeDTO> recipeDTOSCheck = recipeDAO.readAll();
        assertEquals(1, recipeDTOSCheck.size());
    }



    @Test
    void readByName()
    {
        // The Populate class has already added the following recipes:
        // 1. "Chicken and Rice" (with Chicken, Rice, and Onion)
        // 2. "Garlic Chicken" (with Chicken and Garlic)

        // Test search by name, using "Chicken" (should return both "Chicken and Rice" and "Garlic Chicken")
        List<RecipeDTO> chickenRecipes = recipeDAO.readByName("Chicken");
        assertNotNull(chickenRecipes);
        assertEquals(2, chickenRecipes.size());  // We expect 2 matches ("Chicken and Rice" and "Garlic Chicken")

        // Check that the results contain the expected recipes
        assertTrue(chickenRecipes.stream().anyMatch(r -> r.getRecipeName().equals("Chicken and Rice")));
        assertTrue(chickenRecipes.stream().anyMatch(r -> r.getRecipeName().equals("Garlic Chicken")));

        // Test search for "Garlic" (should return "Garlic Chicken" only)
        List<RecipeDTO> garlicRecipes = recipeDAO.readByName("Garlic");
        assertNotNull(garlicRecipes);
        assertEquals(1, garlicRecipes.size());  // We expect 1 match (Garlic Chicken)
        assertEquals("Garlic Chicken", garlicRecipes.get(0).getRecipeName());

        // Test search for a non-existent name (should return an empty list)
        List<RecipeDTO> noMatchRecipes = recipeDAO.readByName("Beef");
        assertNotNull(noMatchRecipes);
        assertTrue(noMatchRecipes.isEmpty());  // No recipes with "Beef" in the name
    }


    @Test
    void readByServings()
    {
        // The Populate class has already added the following recipes:
        // 1. "Chicken and Rice" with "4 servings"
        // 2. "Garlic Chicken" with "2 servings"

        // Test search by servings, using "4 servings" (should return only "Chicken and Rice")
        List<RecipeDTO> fourServingRecipes = recipeDAO.readByServings("4 servings");
        assertNotNull(fourServingRecipes);
        assertEquals(1, fourServingRecipes.size());  // We expect 1 match ("Chicken and Rice")
        assertEquals("Chicken and Rice", fourServingRecipes.get(0).getRecipeName());

        // Test search by servings, using "2 servings" (should return only "Garlic Chicken")
        List<RecipeDTO> twoServingRecipes = recipeDAO.readByServings("2 servings");
        assertNotNull(twoServingRecipes);
        assertEquals(1, twoServingRecipes.size());  // We expect 1 match ("Garlic Chicken")
        assertEquals("Garlic Chicken", twoServingRecipes.get(0).getRecipeName());

        // Test search for servings that do not exist (should return an empty list)
        List<RecipeDTO> noMatchRecipes = recipeDAO.readByServings("6 servings");
        assertNotNull(noMatchRecipes);
        assertTrue(noMatchRecipes.isEmpty());  // No recipes with "6 servings" in the data
    }


}