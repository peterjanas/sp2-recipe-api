package dat.daos.impl;

import dat.config.HibernateConfig;
import dat.config.Populate;
import dat.dtos.IngredientDTO;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IngredientDAOTest
{
    private static RecipeDAO recipeDAO;
    private static IngredientDAO ingredientDAO;
    private static EntityManagerFactory emf;
    private static Populate populate;

    @BeforeEach
    void setUpAll()
    {
        HibernateConfig.setTest(true);
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        recipeDAO = RecipeDAO.getInstance(emf);
        ingredientDAO = IngredientDAO.getInstance(emf);
        populate = new Populate(emf);
    }

    @BeforeEach
    void setup()
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
    void read()
    {
        IngredientDTO ingredientDTO = ingredientDAO.read(1);
        assertNotNull(ingredientDTO);
        assertEquals("Chicken", ingredientDTO.getIngredientName());
    }

    @Test
    void readAll()
    {
        List<IngredientDTO> ingredientDTOS = ingredientDAO.readAll();
        assertNotNull(ingredientDTOS);
        assertEquals(2, ingredientDTOS.size());
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