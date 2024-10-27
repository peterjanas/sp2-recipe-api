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

    public void readByName(Context ctx) {
        // request
        String name = ctx.pathParam("name");
        // List of DTOS
        List<RecipeDTO> recipeDTOS = dao.readByName(name);
        // response
        ctx.res().setStatus(200);
        ctx.json(recipeDTOS, RecipeDTO.class);
    }

    public void readByServings(Context ctx) {
        // request
        String servings = ctx.pathParam("servings");
        // List of DTOS
        List<RecipeDTO> recipeDTOS = dao.readByServings(servings);
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
    public RecipeDTO validateEntity(Context ctx) {
        return ctx.bodyValidator(RecipeDTO.class)
                .check(r -> r.getRecipeName() != null && !r.getRecipeName().isEmpty(), "Recipe name must be set")
                .check(r -> r.getServings() != null && !r.getServings().isEmpty(), "Servings must be set")
                .check(r -> r.getInstructions() != null && !r.getInstructions().isEmpty(), "Instructions must be set")
                .get();
    }
}
