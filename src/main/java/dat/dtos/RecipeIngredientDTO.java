package dat.dtos;

import dat.entities.RecipeIngredient;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecipeIngredientDTO {
    private Integer id;
    private IngredientDTO ingredient;
    private String amount;

    public RecipeIngredientDTO(RecipeIngredient recipeIngredient) {
        this.id = recipeIngredient.getId();
        this.ingredient = new IngredientDTO(recipeIngredient.getIngredient());
        this.amount = recipeIngredient.getAmount();
    }

    public RecipeIngredientDTO(IngredientDTO ingredient, String amount) {
        this.ingredient = ingredient;
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecipeIngredientDTO that = (RecipeIngredientDTO) o;
        return id.equals(that.id) &&
                ingredient.equals(that.ingredient) &&
                amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + ingredient.hashCode();
        result = 31 * result + amount.hashCode();
        return result;
    }
}
