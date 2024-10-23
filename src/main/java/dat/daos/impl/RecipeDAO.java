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
            TypedQuery<RecipeDTO> query = em.createQuery("SELECT new dat.dtos.RecipeDTO(r) FROM Recipe r WHERE r.recipeName = :name", RecipeDTO.class);
            query.setParameter("name", name);
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
    public RecipeDTO create(RecipeDTO recipeDTO)
    {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Convert RecipeDTO to Recipe entity
            Recipe recipe = new Recipe(recipeDTO);

            // Persist recipe entity
            em.persist(recipe);

            // Persist associated RecipeIngredients
            Set<RecipeIngredient> recipeIngredients = recipeDTO.getRecipeIngredients().stream()
                    .map(dto -> new RecipeIngredient(
                            recipe,
                            em.find(Ingredient.class, dto.getIngredient().getId()), // Fetch existing ingredient by id
                            dto.getAmount()))
                    .collect(Collectors.toSet());

            recipe.setIngredients(recipeIngredients);

            em.getTransaction().commit();
            return new RecipeDTO(recipe);
        }
    }

    @Override
    public RecipeDTO update(Integer id, RecipeDTO recipeDTO)
    {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Recipe recipe = em.find(Recipe.class, id);

            // Update basic recipe fields
            recipe.setRecipeName(recipeDTO.getRecipeName());
            recipe.setInstructions(recipeDTO.getInstructions());
            recipe.setServings(recipeDTO.getServings());

            // Update RecipeIngredients
            Set<RecipeIngredient> newRecipeIngredients = recipeDTO.getRecipeIngredients().stream()
                    .map(dto -> new RecipeIngredient(
                            recipe,
                            em.find(Ingredient.class, dto.getIngredient().getId()), // Fetch existing ingredient by id
                            dto.getAmount()))
                    .collect(Collectors.toSet());

            // Clear the old recipe ingredients and set the new ones
            recipe.getRecipeIngredients().clear();
            recipe.setIngredients(newRecipeIngredients);

            em.getTransaction().commit();
            return new RecipeDTO(recipe);
        }
    }

    @Override
    public void delete(Integer integer)
    {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Recipe recipe = em.find(Recipe.class, integer);
            if (recipe != null) {
                em.remove(recipe);
            }
            em.getTransaction().commit();
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
