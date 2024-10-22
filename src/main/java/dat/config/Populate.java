package dat.config;

import dat.entities.Ingredient;
import dat.entities.Recipe;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class Populate {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Recipe pastaCarbonara = new Recipe("Pasta Carbonara", "4", "Cook pasta, fry bacon, mix together");
            pastaCarbonara.setIngredients(getPastaCarbonaraIngredients());
            em.persist(pastaCarbonara);

            Recipe chickenCurry = new Recipe("Chicken Curry", "4", "Cook chicken, add curry paste, simmer with coconut milk");
            chickenCurry.setIngredients(getChickenCurryIngredients());
            em.persist(chickenCurry);

            Recipe beefStroganoff = new Recipe("Beef Stroganoff", "4", "Sear beef, sauté onions, add sour cream");
            beefStroganoff.setIngredients(getBeefStroganoffIngredients());
            em.persist(beefStroganoff);

            Recipe vegetableStirFry = new Recipe("Vegetable Stir Fry", "2", "Stir-fry vegetables, add soy sauce");
            vegetableStirFry.setIngredients(getVegetableStirFryIngredients());
            em.persist(vegetableStirFry);

            Recipe margheritaPizza = new Recipe("Margherita Pizza", "2", "Roll dough, spread tomato sauce, add mozzarella");
            margheritaPizza.setIngredients(getMargheritaPizzaIngredients());
            em.persist(margheritaPizza);

            Recipe shrimpTacos = new Recipe("Shrimp Tacos", "3", "Grill shrimp, prepare toppings, assemble tacos");
            shrimpTacos.setIngredients(getShrimpTacosIngredients());
            em.persist(shrimpTacos);

            Recipe frenchOmelette = new Recipe("French Omelette", "1", "Whisk eggs, cook gently, fold and serve");
            frenchOmelette.setIngredients(getFrenchOmeletteIngredients());
            em.persist(frenchOmelette);

            Recipe lasagna = new Recipe("Lasagna", "6", "Layer pasta, meat sauce, and cheese, bake");
            lasagna.setIngredients(getLasagnaIngredients());
            em.persist(lasagna);

            Recipe capreseSalad = new Recipe("Caprese Salad", "2", "Slice tomatoes and mozzarella, drizzle with olive oil and basil");
            capreseSalad.setIngredients(getCapreseSaladIngredients());
            em.persist(capreseSalad);

            Recipe beefBurgers = new Recipe("Beef Burgers", "4", "Grill beef patties, assemble with buns and toppings");
            beefBurgers.setIngredients(getBeefBurgersIngredients());
            em.persist(beefBurgers);

            em.getTransaction().commit();
        }
    }

    @NotNull
    private static Set<Ingredient> getPastaCarbonaraIngredients() {
        Ingredient ingredient1 = new Ingredient("Pasta", "400g");
        Ingredient ingredient2 = new Ingredient("Bacon", "200g");
        Ingredient ingredient3 = new Ingredient("Egg", "2");
        Ingredient ingredient4 = new Ingredient("Parmesan", "100g");

        return Set.of(ingredient1, ingredient2, ingredient3, ingredient4);
    }

    @NotNull
    private static Set<Ingredient> getChickenCurryIngredients() {
        return Set.of(
                new Ingredient("Chicken", "500g"),
                new Ingredient("Curry Paste", "3 tbsp"),
                new Ingredient("Coconut Milk", "400ml"),
                new Ingredient("Onion", "1"),
                new Ingredient("Garlic", "2 cloves")
        );
    }

    @NotNull
    private static Set<Ingredient> getBeefStroganoffIngredients() {
        return Set.of(
                new Ingredient("Beef", "500g"),
                new Ingredient("Sour Cream", "200g"),
                new Ingredient("Mushrooms", "200g"),
                new Ingredient("Onion", "1"),
                new Ingredient("Butter", "2 tbsp")
        );
    }

    @NotNull
    private static Set<Ingredient> getVegetableStirFryIngredients() {
        return Set.of(
                new Ingredient("Carrots", "2"),
                new Ingredient("Bell Pepper", "1"),
                new Ingredient("Broccoli", "200g"),
                new Ingredient("Soy Sauce", "3 tbsp"),
                new Ingredient("Garlic", "2 cloves")
        );
    }

    @NotNull
    private static Set<Ingredient> getMargheritaPizzaIngredients() {
        return Set.of(
                new Ingredient("Pizza Dough", "1"),
                new Ingredient("Tomato Sauce", "200g"),
                new Ingredient("Mozzarella", "200g"),
                new Ingredient("Basil Leaves", "10"),
                new Ingredient("Olive Oil", "2 tbsp")
        );
    }

    @NotNull
    private static Set<Ingredient> getShrimpTacosIngredients() {
        return Set.of(
                new Ingredient("Shrimp", "300g"),
                new Ingredient("Taco Shells", "6"),
                new Ingredient("Cabbage", "100g"),
                new Ingredient("Avocado", "1"),
                new Ingredient("Lime", "1")
        );
    }

    @NotNull
    private static Set<Ingredient> getFrenchOmeletteIngredients() {
        return Set.of(
                new Ingredient("Eggs", "3"),
                new Ingredient("Butter", "1 tbsp"),
                new Ingredient("Chives", "1 tbsp"),
                new Ingredient("Salt", "to taste"),
                new Ingredient("Pepper", "to taste")
        );
    }

    @NotNull
    private static Set<Ingredient> getLasagnaIngredients() {
        return Set.of(
                new Ingredient("Lasagna Noodles", "12"),
                new Ingredient("Ground Beef", "500g"),
                new Ingredient("Tomato Sauce", "400g"),
                new Ingredient("Ricotta", "250g"),
                new Ingredient("Mozzarella", "200g")
        );
    }

    @NotNull
    private static Set<Ingredient> getCapreseSaladIngredients() {
        return Set.of(
                new Ingredient("Tomatoes", "2"),
                new Ingredient("Mozzarella", "200g"),
                new Ingredient("Basil Leaves", "10"),
                new Ingredient("Olive Oil", "2 tbsp"),
                new Ingredient("Salt", "to taste")
        );
    }

    @NotNull
    private static Set<Ingredient> getBeefBurgersIngredients() {
        return Set.of(
                new Ingredient("Ground Beef", "500g"),
                new Ingredient("Burger Buns", "4"),
                new Ingredient("Cheese Slices", "4"),
                new Ingredient("Lettuce", "4 leaves"),
                new Ingredient("Tomato", "1")
        );
    }
}