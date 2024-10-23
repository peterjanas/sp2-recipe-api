package dat.dtos;

import dat.entities.Ingredient;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Getter
public class IngredientDTO
{
    private Integer id;
    private String ingredientName;

    public IngredientDTO(Integer id, String ingredientName)
    {
        this.id = id;
        this.ingredientName = ingredientName;
    }

    public IngredientDTO(Ingredient ingredient)
    {
        this.id = ingredient.getId();
        this.ingredientName = ingredient.getIngredientName();
    }

    public static List<IngredientDTO> toIngredientDTOList(List<Ingredient> ingredients)
    {
        return List.of(ingredients.stream().map(IngredientDTO::new).toArray(IngredientDTO[]::new));
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IngredientDTO that = (IngredientDTO) o;
        return getId().equals(that.getId()) &&
                getIngredientName().equals(that.getIngredientName());
    }

    @Override
    public int hashCode()
    {
        int result = getId().hashCode();
        result = 31 * result + getIngredientName().hashCode();
        return result;
    }
}
