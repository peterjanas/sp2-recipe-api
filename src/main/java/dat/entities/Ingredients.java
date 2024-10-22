package dat.entities;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@NoArgsConstructor
public class Ingredients
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String amount;

    @ManyToMany
    @JoinTable(
        name = "recipe_ingredients",
        joinColumns = @JoinColumn(name = "ingredient_id"),
        inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private Set<Recipe> recipes;

    public Ingredients(String name, String amount)
    {
        this.name = name;
        this.amount = amount;
    }
}