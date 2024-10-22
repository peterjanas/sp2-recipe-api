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
    private String amount;

    public IngredientDTO(Integer id, String ingredientName, String amount)
    {
        this.id = id;
        this.ingredientName = ingredientName;
        this.amount = amount;
    }

    public IngredientDTO(Ingredient ingredient)
    {
        this.id = ingredient.getId();
        this.ingredientName = ingredient.getIngredientName();
        this.amount = ingredient.getAmount();
    }

    public static List<IngredientDTO> toIngredientDTOList(List<Ingredient> ingredients) {
        return List.of(ingredients.stream().map(IngredientDTO::new).toArray(IngredientDTO[]::new));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        IngredientDTO that = (IngredientDTO) o;
        return getId().equals(that.getId()) &&
                getIngredientName().equals(that.getIngredientName()) &&
                getAmount().equals(that.getAmount());
    }

    @Override
    public int hashCode()
    {
        int result = getId().hashCode();
        result = 31 * result + getIngredientName().hashCode();
        result = 31 * result + getAmount().hashCode();
        result = 31 * result + getAmount().hashCode();
        return result;
    }
}
