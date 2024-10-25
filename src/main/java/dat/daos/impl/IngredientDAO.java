package dat.daos.impl;

import dat.daos.IDAO;
import dat.dtos.IngredientDTO;
import dat.entities.Ingredient;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
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

    public List<IngredientDTO> getRecipesByIngredientName(String ingredientName) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<IngredientDTO> query = em.createQuery("SELECT new dat.dtos.IngredientDTO(i) FROM Ingredient i WHERE i.ingredientName = :ingredientName", IngredientDTO.class);
            query.setParameter("ingredientName", ingredientName);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    // IngredientDAO.java
    public IngredientDTO create(IngredientDTO ingredientDTO) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            // Ensure id is null before persisting
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredientName(ingredientDTO.getIngredientName());
            em.persist(ingredient);
            transaction.commit();
            return new IngredientDTO(ingredient);
        } finally {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            em.close();
        }
    }

    @Override
    public IngredientDTO update(Integer integer, IngredientDTO ingredientDTO)
    {
        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            Ingredient ingredient = em.find(Ingredient.class, integer);
            ingredient.setIngredientName(ingredientDTO.getIngredientName());
            em.getTransaction().commit();
            return new IngredientDTO(ingredient);
        }
    }

    @Override
    public boolean delete(Integer integer)
    {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Ingredient ingredient = em.find(Ingredient.class, integer);
            if (ingredient != null) {
                em.remove(ingredient);
            }
            em.getTransaction().commit();
        }
        return false;
    }

    public List<IngredientDTO> readByName(String name) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<IngredientDTO> query = em.createQuery("SELECT new dat.dtos.IngredientDTO(i) FROM Ingredient i WHERE LOWER(i.ingredientName) = LOWER(:name)", IngredientDTO.class);
            query.setParameter("name", name);
            return query.getResultList();
        } finally {
            em.close();
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