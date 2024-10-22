package dat.config;

import dat.entities.Ingredients;
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
    private static Set<Ingredients> getPastaCarbonaraIngredients() {
        Ingredients ingredients1 = new Ingredients("Pasta", "400g");
        Ingredients ingredients2 = new Ingredients("Bacon", "200g");
        Ingredients ingredients3 = new Ingredients("Egg", "2");
        Ingredients ingredients4 = new Ingredients("Parmesan", "100g");

        return Set.of(ingredients1, ingredients2, ingredients3, ingredients4);
    }

    @NotNull
    private static Set<Ingredients> getChickenCurryIngredients() {
        return Set.of(
                new Ingredients("Chicken", "500g"),
                new Ingredients("Curry Paste", "3 tbsp"),
                new Ingredients("Coconut Milk", "400ml"),
                new Ingredients("Onion", "1"),
                new Ingredients("Garlic", "2 cloves")
        );
    }

    @NotNull
    private static Set<Ingredients> getBeefStroganoffIngredients() {
        return Set.of(
                new Ingredients("Beef", "500g"),
                new Ingredients("Sour Cream", "200g"),
                new Ingredients("Mushrooms", "200g"),
                new Ingredients("Onion", "1"),
                new Ingredients("Butter", "2 tbsp")
        );
    }

    @NotNull
    private static Set<Ingredients> getVegetableStirFryIngredients() {
        return Set.of(
                new Ingredients("Carrots", "2"),
                new Ingredients("Bell Pepper", "1"),
                new Ingredients("Broccoli", "200g"),
                new Ingredients("Soy Sauce", "3 tbsp"),
                new Ingredients("Garlic", "2 cloves")
        );
    }

    @NotNull
    private static Set<Ingredients> getMargheritaPizzaIngredients() {
        return Set.of(
                new Ingredients("Pizza Dough", "1"),
                new Ingredients("Tomato Sauce", "200g"),
                new Ingredients("Mozzarella", "200g"),
                new Ingredients("Basil Leaves", "10"),
                new Ingredients("Olive Oil", "2 tbsp")
        );
    }

    @NotNull
    private static Set<Ingredients> getShrimpTacosIngredients() {
        return Set.of(
                new Ingredients("Shrimp", "300g"),
                new Ingredients("Taco Shells", "6"),
                new Ingredients("Cabbage", "100g"),
                new Ingredients("Avocado", "1"),
                new Ingredients("Lime", "1")
        );
    }

    @NotNull
    private static Set<Ingredients> getFrenchOmeletteIngredients() {
        return Set.of(
                new Ingredients("Eggs", "3"),
                new Ingredients("Butter", "1 tbsp"),
                new Ingredients("Chives", "1 tbsp"),
                new Ingredients("Salt", "to taste"),
                new Ingredients("Pepper", "to taste")
        );
    }

    @NotNull
    private static Set<Ingredients> getLasagnaIngredients() {
        return Set.of(
                new Ingredients("Lasagna Noodles", "12"),
                new Ingredients("Ground Beef", "500g"),
                new Ingredients("Tomato Sauce", "400g"),
                new Ingredients("Ricotta", "250g"),
                new Ingredients("Mozzarella", "200g")
        );
    }

    @NotNull
    private static Set<Ingredients> getCapreseSaladIngredients() {
        return Set.of(
                new Ingredients("Tomatoes", "2"),
                new Ingredients("Mozzarella", "200g"),
                new Ingredients("Basil Leaves", "10"),
                new Ingredients("Olive Oil", "2 tbsp"),
                new Ingredients("Salt", "to taste")
        );
    }

    @NotNull
    private static Set<Ingredients> getBeefBurgersIngredients() {
        return Set.of(
                new Ingredients("Ground Beef", "500g"),
                new Ingredients("Burger Buns", "4"),
                new Ingredients("Cheese Slices", "4"),
                new Ingredients("Lettuce", "4 leaves"),
                new Ingredients("Tomato", "1")
        );
    }
}