package dat.entities;

import dat.dtos.IngredientDTO;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ingredient")
public class Ingredient
{
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ingredient_id", nullable = false, unique = true)
    private int id;

    @Column(name = "ingredient_name", nullable = false, unique = true)
    private String ingredientName;

    // New relationship to RecipeIngredient
    @OneToMany(mappedBy = "ingredient")
    private Set<RecipeIngredient> recipeIngredients;



    public Ingredient(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Ingredient(IngredientDTO ingredientDTO)
    {
        this.id = ingredientDTO.getId();
        this.ingredientName = ingredientDTO.getIngredientName();
    }

  @Override
  public boolean equals(Object o){
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Ingredient ingredient = (Ingredient) o;
    return Objects.equals(ingredientName, ingredient.ingredientName);
  }

    @Override
    public int hashCode()
    {
        return Objects.hash(ingredientName);
    }
}