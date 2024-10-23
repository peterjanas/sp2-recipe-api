package dat.dtos;

import dat.entities.Recipe;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class RecipeDTO
{
    @Setter(lombok.AccessLevel.NONE)
    private Integer id;

    private String recipeName;

    private String servings;

    private String instructions;

    private Set<RecipeIngredientDTO> recipeIngredients = new HashSet<>();


    public RecipeDTO(Recipe recipe)
    {
        this.id = recipe.getId();
        this.recipeName = recipe.getRecipeName();
        this.servings = recipe.getServings();
        this.instructions = recipe.getInstructions();
        if (recipe.getRecipeIngredients() != null)
        {
            recipe.getRecipeIngredients().forEach(ri -> recipeIngredients.add(new RecipeIngredientDTO(ri)));
        }
    }

    public RecipeDTO(String recipeName, String servings, String instructions)
    {
        this.recipeName = recipeName;
        this.servings = servings;
        this.instructions = instructions;
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
