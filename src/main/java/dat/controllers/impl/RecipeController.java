package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.controllers.IController;
import dat.dtos.RecipeDTO;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;

public class RecipeController implements IController<RecipeDTO, Integer>
{

    @Override
    public void read(Context ctx)
    {

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
