package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.RecipeDTO;
import dat.entities.Ingredient;
import dat.entities.Recipe;
import dat.entities.RecipeIngredient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class RecipeDAO implements IDAO<RecipeDTO, Integer>
{
    private static RecipeDAO instance;
    private static EntityManagerFactory emf;

    public static RecipeDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RecipeDAO();
        }
        return instance;
    }

    @Override
    public RecipeDTO read(Integer integer)
    {
        try (EntityManager em = emf.createEntityManager()) {
            Recipe recipe = em.find(Recipe.class, integer);
            return new RecipeDTO(recipe);
        }
    }

    @Override
    public List<RecipeDTO> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
           TypedQuery<RecipeDTO> query = em.createQuery("SELECT new dat.dtos.RecipeDTO(h) FROM Recipe h", RecipeDTO.class);
           return query.getResultList();
        }
    }


    public List<RecipeDTO> readByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<RecipeDTO> query = em.createQuery("SELECT new dat.dtos.RecipeDTO(r) FROM Recipe r WHERE LOWER(r.recipeName) LIKE LOWER(:name)", RecipeDTO.class);
            query.setParameter("name", "%" + name + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<RecipeDTO> readByServings(String servings) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<RecipeDTO> query = em.createQuery("SELECT new dat.dtos.RecipeDTO(r) FROM Recipe r WHERE r.servings = :servings", RecipeDTO.class);
            query.setParameter("servings", servings);
            return query.getResultList();
        } finally {
            em.close();
        }
    }


    @Override
    public RecipeDTO create(RecipeDTO recipeDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Recipe recipe = new Recipe();
            recipe.setRecipeName(recipeDTO.getRecipeName());
            recipe.setInstructions(recipeDTO.getInstructions());
            recipe.setServings(recipeDTO.getServings());

            Set<RecipeIngredient> recipeIngredients = recipeDTO.getRecipeIngredients().stream()
                    .map(dto -> {
                        Ingredient ingredient = findOrCreateIngredient(em, dto.getIngredient().getIngredientName());
                        return new RecipeIngredient(recipe, ingredient, dto.getAmount());
                    })
                    .collect(Collectors.toSet());

            recipe.setIngredients(recipeIngredients);
            em.persist(recipe);
            em.getTransaction().commit();
            return new RecipeDTO(recipe);
        }
    }


    @Override
    public RecipeDTO update(Integer integer, RecipeDTO recipeDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Recipe recipe = em.find(Recipe.class, integer);

            // Update basic recipe fields
            recipe.setRecipeName(recipeDTO.getRecipeName());
            recipe.setInstructions(recipeDTO.getInstructions());
            recipe.setServings(recipeDTO.getServings());

            // Clear the old recipe ingredients
            recipe.getRecipeIngredients().clear();
            em.flush();  // Ensure the clear operation is flushed to the database

            // Add new RecipeIngredients
            Set<RecipeIngredient> newRecipeIngredients = recipeDTO.getRecipeIngredients().stream()
                    .map(dto -> {
                        Ingredient ingredient = findOrCreateIngredient(em, dto.getIngredient().getIngredientName());
                        return new RecipeIngredient(recipe, ingredient, dto.getAmount());
                    })
                    .collect(Collectors.toSet());

            recipe.getRecipeIngredients().addAll(newRecipeIngredients);

            em.getTransaction().commit();
            return new RecipeDTO(recipe);
        }
    }

    private Ingredient findOrCreateIngredient(EntityManager em, String ingredientName) {
        TypedQuery<Ingredient> query = em.createQuery("SELECT i FROM Ingredient i WHERE i.ingredientName = :name", Ingredient.class);
        query.setParameter("name", ingredientName);
        List<Ingredient> ingredients = query.getResultList();
        if (ingredients.isEmpty()) {
            Ingredient newIngredient = new Ingredient(ingredientName);
            em.persist(newIngredient);
            return newIngredient;
        } else {
            return ingredients.get(0);
        }
    }

    @Override
    public void delete(Integer integer) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();

            Recipe recipe = em.find(Recipe.class, integer);
            if (recipe != null) {
                Set<RecipeIngredient> recipeIngredients = recipe.getRecipeIngredients();

                for (RecipeIngredient ri : recipeIngredients) {
                    em.remove(ri);
                }

                em.remove(recipe);
                // This will cascade the removal to RecipeIngredient due to CascadeType.ALL and orphanRemoval = true
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();  // Rollback in case of any errors
            }
            throw e;
        } finally {
            em.close();
        }
    }


    @Override
    public boolean validatePrimaryKey(Integer integer)
    {
        try (EntityManager em = emf.createEntityManager()) {
            Recipe recipe = em.find(Recipe.class, integer);
            return recipe != null;
        }
    }
}
