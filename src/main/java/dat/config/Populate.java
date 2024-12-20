package dat.config;

import dat.entities.Ingredient;
import dat.entities.Recipe;
import dat.entities.RecipeIngredient;
import dat.security.entities.Role;
import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class Populate
{

    public static void populateData(EntityManagerFactory emf)
    {
        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            // Persist Ingredients first
            Ingredient chicken = new Ingredient("Chicken");
            Ingredient rice = new Ingredient("Rice");
            Ingredient onion = new Ingredient("Onion");
            Ingredient garlic = new Ingredient("Garlic");

            em.persist(chicken);
            em.persist(rice);
            em.persist(onion);
            em.persist(garlic);

            // Create Recipes
            Recipe chickenRice = new Recipe("Chicken and Rice", "4 servings", "Cook chicken, rice, and mix.");
            Recipe garlicChicken = new Recipe("Garlic Chicken", "2 servings", "Cook chicken with garlic.");

            // Set ingredients for each recipe
            Set<RecipeIngredient> chickenRiceIngredients = getChickenRiceIngredients(chickenRice, chicken, rice, onion);
            Set<RecipeIngredient> garlicChickenIngredients = getGarlicChickenIngredients(garlicChicken, chicken, garlic);

            chickenRice.setIngredients(chickenRiceIngredients);
            garlicChicken.setIngredients(garlicChickenIngredients);

            // Persist Recipes
            em.persist(chickenRice);
            em.persist(garlicChicken);

            em.getTransaction().commit();
        }
    }

    @NotNull
    private static Set<RecipeIngredient> getChickenRiceIngredients(Recipe recipe, Ingredient chicken, Ingredient rice, Ingredient onion)
    {
        // Create RecipeIngredients for Chicken and Rice Recipe
        RecipeIngredient chickenIngredient = new RecipeIngredient(recipe, chicken, "200g");
        RecipeIngredient riceIngredient = new RecipeIngredient(recipe, rice, "150g");
        RecipeIngredient onionIngredient = new RecipeIngredient(recipe, onion, "1 large");

        return Set.of(chickenIngredient, riceIngredient, onionIngredient);
    }

    @NotNull
    private static Set<RecipeIngredient> getGarlicChickenIngredients(Recipe recipe, Ingredient chicken, Ingredient garlic)
    {
        // Create RecipeIngredients for Garlic Chicken Recipe
        RecipeIngredient chickenIngredient = new RecipeIngredient(recipe, chicken, "300g");
        RecipeIngredient garlicIngredient = new RecipeIngredient(recipe, garlic, "3 cloves");

        return Set.of(chickenIngredient, garlicIngredient);
    }

    public static void cleanupData(EntityManagerFactory emf)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();

            // Remove all RecipeIngredient entities
            em.createQuery("DELETE FROM RecipeIngredient").executeUpdate();

            // Remove all Recipe entities
            em.createQuery("DELETE FROM Recipe").executeUpdate();

            // Remove all Ingredient entities
            em.createQuery("DELETE FROM Ingredient").executeUpdate();

            em.getTransaction().commit();
        }
    }
    public static UserDTO[] populateUsers(EntityManagerFactory emf)
    {
        User user, admin;
        Role userRole, adminRole;

        user = new User("usertest", "user123");
        admin = new User("admintest", "admin123");
        userRole = new Role("USER");
        adminRole = new Role("ADMIN");
        user.addRole(userRole);
        admin.addRole(adminRole);

        try (var em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        }
        UserDTO userDTO = new UserDTO(user.getUsername(), "user123");
        UserDTO adminDTO = new UserDTO(admin.getUsername(), "admin123");
        return new UserDTO[]{userDTO, adminDTO};
    }
}
