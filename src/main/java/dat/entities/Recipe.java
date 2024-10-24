package dat.entities;

import dat.dtos.IngredientDTO;
import dat.dtos.RecipeDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Recipe
{
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id", nullable = false, unique = true)
    private int id;


    @Column(name = "recipe_name", nullable = false, unique = true)
    private String recipeName;


    @OneToMany(mappedBy = "recipe", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<RecipeIngredient> recipeIngredients;


    private String servings;


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

    public void setIngredients(Set<RecipeIngredient> recipeIngredients)
    {
        this.recipeIngredients = recipeIngredients;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recipe recipe = (Recipe) o;
        return Objects.equals(recipeName, recipe.recipeName);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(recipeName);
    }


}

