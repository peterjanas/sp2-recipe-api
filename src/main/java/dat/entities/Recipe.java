package dat.entities;

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

    @Setter
    @ManyToMany(mappedBy = "recipes", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ingredient> ingredients;

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
        this.id = recipeDTO.getId();
        this.recipeName = recipeDTO.getRecipeName();
        this.servings = recipeDTO.getServings();
        this.instructions = recipeDTO.getInstructions();
        if (recipeDTO.getIngredients() != null) {
            this.ingredients = recipeDTO.getIngredients().stream()
                    .map(Ingredient::new)
                    .collect(Collectors.toSet());
        }
    }
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;
        return Objects.equals(recipeName, recipe.recipeName);
    }


}

