package dat.entities;

import dat.dtos.IngredientDTO;
import dat.dtos.RecipeDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
public class Recipe
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id", nullable = false, unique = true)
    private int id;

    @Column(name = "recipe_name", nullable = false, unique = true)
    @Setter
    private String recipeName;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RecipeIngredient> recipeIngredients;


    @Setter
    private String servings;

    @Setter
    private String instructions;

    public Recipe(String recipeName, String servings, String instructions)
    {
        this.recipeName = recipeName;
        this.servings = servings;
        this.instructions = instructions;
    }
    public Recipe(RecipeDTO recipeDTO)
    {
        this.recipeName = recipeDTO.getRecipeName();
        this.servings = recipeDTO.getServings();
        this.instructions = recipeDTO.getInstructions();
    }

    public void setIngredients(Set<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    /*public void setIngredients(Set<IngredientDTO> ingredientDTOs)
    {
        this.ingredients = ingredientDTOs.stream()
                .map(Ingredient::new)
                .collect(Collectors.toSet());
    }*/

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;
        return Objects.equals(recipeName, recipe.recipeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipeName);
    }


}

