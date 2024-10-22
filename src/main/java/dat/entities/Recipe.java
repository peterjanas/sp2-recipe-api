package dat.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Recipe
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(mappedBy = "recipes", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Ingredient> ingredients;
    private String servings;

    private String instructions;

    public Recipe(String name, String servings, String instructions)
    {
        this.name = name;
        this.servings = servings;
        this.instructions = instructions;
    }


}

