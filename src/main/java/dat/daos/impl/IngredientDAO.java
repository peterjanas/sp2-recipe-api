package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.IngredientDTO;
import dat.dtos.RecipeDTO;
import dat.entities.Ingredient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class IngredientDAO implements IDAO<IngredientDTO, Integer>
{
    private static IngredientDAO instance;
    private static EntityManagerFactory emf;

    public static IngredientDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new IngredientDAO();
        }
        return instance;
    }

    @Override
    public IngredientDTO read(Integer integer)
    {
        try (EntityManager em = emf.createEntityManager()) {
            Ingredient ingredient = em.find(Ingredient.class, integer);
            return new IngredientDTO(ingredient);
        }
    }

    @Override
    public List<IngredientDTO> readAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            TypedQuery<IngredientDTO> query = em.createQuery("SELECT new dat.dtos.IngredientDTO(i) FROM Ingredient i", IngredientDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public IngredientDTO create(IngredientDTO ingredientDTO)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Ingredient ingredient = new Ingredient(ingredientDTO);
            em.persist(ingredient);
            em.getTransaction().commit();
            return new IngredientDTO(ingredient);
        }
    }

    @Override
    public IngredientDTO update(Integer integer, IngredientDTO ingredientDTO)
    {
        return null;
    }

    @Override
    public void delete(Integer integer)
    {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Ingredient ingredient = em.find(Ingredient.class, integer);
            if (ingredient != null) {
                em.remove(ingredient);
            }
            em.getTransaction().commit();
        }

    }

    @Override
    public boolean validatePrimaryKey(Integer integer)
    {
        try (EntityManager em = emf.createEntityManager()) {
            Ingredient ingredient = em.find(Ingredient.class, integer);
            return ingredient != null;
        }
    }
}