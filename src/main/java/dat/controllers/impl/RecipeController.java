package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.daos.impl.RecipeDAO;
import dat.dtos.RecipeDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class RecipeController implements IController<RecipeDTO, Integer>
{
    private final RecipeDAO dao;

    public RecipeController()
    {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = RecipeDAO.getInstance(emf);
    }

    @Override
    public void read(Context ctx)
    {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // DTO
        RecipeDTO recipeDTO = dao.read(id);
        // response
        ctx.res().setStatus(200);
        ctx.json(recipeDTO, RecipeDTO.class);

    }

    @Override
    public void readAll(Context ctx)
    {
        // List of DTOS
        List<RecipeDTO> recipeDTOS = dao.readAll();
        // response
        ctx.res().setStatus(200);
        ctx.json(recipeDTOS, RecipeDTO.class);

    }

    @Override
    public void create(Context ctx)
    {
        // request
        RecipeDTO jsonRequest = ctx.bodyAsClass(RecipeDTO.class);
        // DTO
        RecipeDTO recipeDTO = dao.create(jsonRequest);
        // response
        ctx.res().setStatus(201);
        ctx.json(recipeDTO, RecipeDTO.class);

    }

    @Override
    public void update(Context ctx)
    {
        // request
        int id = ctx.pathParamAsClass("id", Integer.class).check(this::validatePrimaryKey, "Not a valid id").get();
        // dto
        RecipeDTO recipeDTO = dao.update(id, validateEntity(ctx));
        // response
        ctx.res().setStatus(200);
        ctx.json(recipeDTO, RecipeDTO.class);

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
    public RecipeDTO validateEntity(Context ctx)
    {
        /*
        return ctx.bodyValidator(HotelDTO.class)
                .check(h -> h.getHotelAddress() != null && !h.getHotelAddress().isEmpty(), "Hotel address must be set")
                .check(h -> h.getHotelName() != null && !h.getHotelName().isEmpty(), "Hotel name must be set")
                .check(h -> h.getHotelType() != null, "Hotel type must be set")
                .get();
    */
        return null;
    }

}
