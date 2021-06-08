package recipe;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import javafx.util.Pair;

/**
 * Think of this class a storage compartment of all the available recipes in their respective containers
 */
public class RecipeHolder {

    /**
     * Map to store all the recipes in form of (name -> Recipe)
     */
    private final ConcurrentHashMap<String, Recipe> recipes;

    /**
     * Constructs an empty holder
     */
    public RecipeHolder() {
        this.recipes = new ConcurrentHashMap<>();
    }

    /**
     * Constructs a holder with initial recipes
     * @param recipes recipes to add in the holder
     */
    public RecipeHolder(List<Pair<String, List<Pair<String, Integer>>>> recipes) {
        this.recipes = new ConcurrentHashMap<>();
        for (Pair<String, List<Pair<String, Integer>>> recipeObject : recipes) {
            Recipe recipe = new Recipe(recipeObject.getKey(), recipeObject.getValue());
            this.recipes.putIfAbsent(recipe.getName(), recipe); // WIll handle duplicate recipes
        }
    }

    /**
     * Function to add a new recipe to the holder
     * This function only adds a new recipe if it is not present previously
     * @param name name of the recipe to be added
     * @param ingredients ingredients of the recipe to add
     */
    public void addNewRecipe(String name, List<Pair<String, Integer>> ingredients) {
        this.recipes.computeIfAbsent(name, key -> {
            Recipe recipe = new Recipe(name, ingredients);
            return recipe;
        });
    }

    /**
     * Function to get total recipes available in the holder
     * @return total number of recipes present in the map
     */
    public int getTotalRecipes() {
        return this.recipes.size();
    }

    /**
     * Function to get a particular recipe
     * @param name name of the recipe to fetch
     * @return Recipe object from the map
     */
    public Recipe getRecipe(String name) {
        return this.recipes.get(name);
    }

    /**
     * @return Returns a string representation of this class
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", RecipeHolder.class.getSimpleName() + "[", "]")
            .add("recipes=" + recipes)
            .toString();
    }
}
