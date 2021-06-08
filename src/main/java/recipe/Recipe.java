package recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import javafx.util.Pair;

/**
 * Recipe class is used to hold a single recipe with its required ingredients details
 * It has name, ingredients associated with it.
 */
public class Recipe {

    /**
     * Variable to hold name of the recipe
     */
    private final String name;

    /**
     * List to hold required ingredients
     */
    private final List<RecipeIngredient> ingredients;

    /**
     * Construct a new Recipe with a given name and ingredients
     * @param name recipe's name
     * @param ingredients required ingredients
     */
    public Recipe(String name, List<Pair<String, Integer>> ingredients) {
        this.name = name;
        this.ingredients = new ArrayList<>();
        for (Pair<String, Integer> ingredient : ingredients) {
            this.ingredients.add(new RecipeIngredient(ingredient.getKey(), ingredient.getValue()));
        }
    }

    /**
     * Function to fetch all the ingredients of the recipe
     * @return list of ingredients
     */
    public List<RecipeIngredient> getIngredients() {
        return this.ingredients;
    }

    /**
     * Function to get name of recipe
     * @return name of recipe
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Returns a string representation of this class
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Recipe.class.getSimpleName() + "[", "]")
            .add("name='" + name + "'")
            .add("ingredients=" + ingredients)
            .toString();
    }

}
