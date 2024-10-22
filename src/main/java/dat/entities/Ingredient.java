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
    private String IngredientName;

    @Column(name = "ingredient_amount", nullable = false)
    private String amount;

    @ManyToMany
    @JoinTable(
        name = "recipe_ingredients",
        joinColumns = @JoinColumn(name = "ingredient_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private Set<Recipe> recipes;


    public Ingredient(String IngredientName, String amount)
    {
        this.IngredientName = IngredientName;
        this.amount = amount;
    }

    public Ingredient(IngredientDTO ingredientDTO)
    {
        this.id = ingredientDTO.getId();
        this.IngredientName = ingredientDTO.getIngredientName();
        this.amount = ingredientDTO.getAmount();
    }

  @Override
  public boolean equals(Object o){
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Ingredient ingredient = (Ingredient) o;
    return Objects.equals(IngredientName, ingredient.IngredientName);
  }

    @Override
    public int hashCode()
    {
        return Objects.hash(IngredientName);
    }
}