package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.RecipeDTO;
import dat.entities.Recipe;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @Override
    public RecipeDTO create(RecipeDTO recipeDTO)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Recipe recipe = new Recipe(recipeDTO);
            em.persist(recipe);
            em.getTransaction().commit();
            return new RecipeDTO(recipe);
        }
    }

    @Override
    public RecipeDTO update(Integer integer, RecipeDTO recipeDTO)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Recipe recipe = em.find(Recipe.class, integer);
            recipe.setRecipeName(recipeDTO.getRecipeName());
            recipe.setInstructions(recipeDTO.getInstructions());
            recipe.setServings(recipeDTO.getServings());
            recipe.setIngredients(recipeDTO.getIngredients());
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
