package ingredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import javafx.util.Pair;

/**
 * Think of this class a storage compartment of all the available ingredients in their respective containers
 */
public class IngredientsHolder {

    /**
     * Map to hold ingredient name -> ingredient object mapping
     */
    private final ConcurrentHashMap<String, Ingredient> ingredients; // Ingredients storage compartment

    /**
     * Constructs a new empty IngredientsHolder object
     */
    public IngredientsHolder() {
        this.ingredients = new ConcurrentHashMap<>();
    }

    /**
     * Constructs a new IngredientsHolder object with given ingredients
     * @param initialIngredients initial ingredients from which the holder will be populated
     */
    public IngredientsHolder(List<Pair<String, Integer>> initialIngredients) {
        this.ingredients = new ConcurrentHashMap<>();
        for (Pair<String, Integer> ingredient : initialIngredients) {
            this.ingredients.put(ingredient.getKey(), new Ingredient(ingredient.getKey(), ingredient.getValue(), ingredient.getValue())); // Considering that at initialisation we will provider max quantity
        }
    }

    /**
     * Function to add a new Ingredient to the holder
     * This function will only add this ingredient if it doesn't exist beforehand
     * @param name name of the new ingredient
     * @param quantity quantity to initialise ingredient
     */
    public void addNewIngredient(String name, Integer quantity) {
        // Put only if not present
        this.ingredients.putIfAbsent(name, new Ingredient(name, quantity, quantity)); // Considering that at initialisation we will provider max quantity
    }

    /**
     * Function to refill a particular ingredient only if it is present in the holder
     * @param ingredient ingredient name to refill
     */
    public void refillIngredient(String ingredient) {
        this.ingredients.computeIfPresent(ingredient, (key, value) -> {
            value.refillIngredient(); // refill ingredient
            return value;
        });
    }

    /**
     * Function to refill all the available ingredients in the holder
     */
    public void refillAllIngredients() {
        this.ingredients.forEach((key, ingredient) -> {
            ingredient.refillIngredient();
        });
    }

    /**
     * Function to check if a particular ingredient is present in holder or not
     * @param ingredient name of ingredient to find
     * @return True if present else False
     */
    public boolean isIngredientPresent(String ingredient) {
        return this.ingredients.containsKey(ingredient);
    }

    /**
     * Function to use given amount of a particular ingredient
     * @param ingredient ingredient to use
     * @param quantity required Quantity
     * @return True if ingredient is available in sufficient amount else Falses
     */
    public boolean useIngredient(String ingredient, int quantity) {
        if (!this.ingredients.containsKey(ingredient)) return false;
        return this.ingredients.get(ingredient).useIngredient(quantity);
    }

    /**
     * Function get the ingredients that are running low
     * @return list of ingredients whose available quantites are low
     */
    public List<String> getRunningLowIngredients() {
        List<String> runningLow = new ArrayList<>();
        for (Map.Entry<String, Ingredient> ingredientEntry : this.ingredients.entrySet()) {
            Ingredient ingredient = ingredientEntry.getValue();
            if (ingredient.isRunningLow()) {
                runningLow.add(ingredient.getName());
            }
        }
        return runningLow;
    }

    /**
     * Function to get total number of ingredients present in the holder
     * @return number of ingredients present in the map
     */
    public int getTotalIngredients() {
        return this.ingredients.size();
    }

    /**
     * @return Returns a string representation of this class
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", IngredientsHolder.class.getSimpleName() + "[", "]")
            .add("ingredients=" + ingredients)
            .toString();
    }
}
