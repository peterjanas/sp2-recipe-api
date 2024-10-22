package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.IngredientDAO;
import dat.daos.impl.RecipeDAO;
import dat.dtos.IngredientDTO;
import dat.dtos.RecipeDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class IngredientController implements IController<IngredientDTO, Integer>
{
    private final IngredientDAO dao;

    public IngredientController()
    {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = IngredientDAO.getInstance(emf);}
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
        // List of DTOS
        List<IngredientDTO> ingredientDTOS = dao.readAll();
        // response
        ctx.res().setStatus(200);
        ctx.json(ingredientDTOS, RecipeDTO.class);
    }

    @Override
    public void create(Context ctx)
    {
        // request
        IngredientDTO jsonRequest = ctx.bodyAsClass(IngredientDTO.class);
        // DTO
        IngredientDTO ingredientDTO = dao.create(jsonRequest);
        // response
        ctx.res().setStatus(201);
        ctx.json(ingredientDTO, IngredientDTO.class);
    }

    @Override
    public void update(Context ctx)
    {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        IngredientDTO ingredientDTO = dao.update(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(ingredientDTO, IngredientDTO.class);
    }

    @Override
    public void delete(Context ctx)
    {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        dao.delete(id);
        // response
        ctx.res().setStatus(204);
    }

    @Override
    public boolean validatePrimaryKey(Integer integer)
    {
        return dao.validatePrimaryKey(integer);
    }

    @Override
    public IngredientDTO validateEntity(Context ctx)
    {
        return null;
    }
}

