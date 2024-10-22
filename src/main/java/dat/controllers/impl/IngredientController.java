package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.IngredientDAO;
import dat.daos.impl.RecipeDAO;
import dat.dtos.IngredientDTO;
import dat.dtos.RecipeDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

public class IngredientController implements IController<RecipeDTO, Integer>
{
    private final IngredientDAO ingredientDAO = new IngredientDAO();

    public IngredientController()
    {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.ingredientDAO = IngredientDAO.getInstance(emf);}
    @Override
    public void read(Context ctx)
    {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        IngredientDTO ingredientDTO = dao.read(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(ingredientDTO, IngredientDTO.class);
    }

    @Override
    public void readAll(Context ctx)
    {

    }

    @Override
    public void create(Context ctx)
    {

    }

    @Override
    public void update(Context ctx)
    {

    }

    @Override
    public void delete(Context ctx)
    {

    }

    @Override
    public boolean validatePrimaryKey(Integer integer)
    {
        return false;
    }

    @Override
    public RecipeDTO validateEntity(Context ctx)
    {
        return null;
    }
}

