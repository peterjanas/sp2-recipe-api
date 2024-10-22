package dat.dtos;

import dat.entities.Recipe;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class RecipeDTO
{
    private Integer id;

    public RecipeDTO(Recipe recipe)
    {

    }


    public static List<RecipeDTO> toRecipeDTOList(List<Recipe> recipes)
    {
        return recipes.stream().map(RecipeDTO::new).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof RecipeDTO recipeDTO)) return false;

        return getId().equals(recipeDTO.getId());
    }


    @Override
    public int hashCode()
    {
        return getId().hashCode();
    }
}
