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
            return null;
            //return new RecipeDTO(recipe);
        }
    }

    @Override
    public List<RecipeDTO> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
           // TypedQuery<RecipeDTO> query = em.createQuery("SELECT new dat.dtos.RecipeDTO(h) FROM Recipe h", RecipeDTO.class);
           // return query.getResultList();
            return null;
        }
    }

    @Override
    public RecipeDTO create(RecipeDTO recipeDTO)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            //Recipe recipe = new Recipe(RecipeDTO);
            //em.persist(recipe);
            em.getTransaction().commit();
            //return new RecipeDTO(recipe);
        }
        return null;
    }

    @Override
    public RecipeDTO update(Integer integer, RecipeDTO recipeDTO)
    {
        return null;
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
