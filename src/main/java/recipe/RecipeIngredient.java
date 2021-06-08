package recipe;

import java.util.StringJoiner;

/**
 * Class to hold recipe's ingredient details
 * This class is different from Ingredients class because all the members of this class are final and we don't want all the functionality of ingredient class
 */
public class RecipeIngredient {

    /**
     * Variable to store name of the ingredient
     */
    private final String name;

    /**
     * Variable to store requiredQuantity of the ingredient
     */
    private final int requiredQuantity;

    /**
     * Constructs a object with given name and required quantity
     * @param name name of the ingredient
     * @param requiredQuantity required quantity of the ingredient
     */
    public RecipeIngredient(String name, int requiredQuantity) {
        this.name = name;
        this.requiredQuantity = requiredQuantity;
    }

    /**
     * Function to get the name of the ingredient
     * @return name of the ingredient
     */
    public String getName() {
        return this.name;
    }

    /**
     * Function to get requiredQuantity of the ingredient
     * @return requiredQuantity of the ingredient
     */
    public int getRequiredQuantity() {
        return this.requiredQuantity;
    }

    /**
     * @return Returns a string representation of this class
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", RecipeIngredient.class.getSimpleName() + "[", "]")
            .add("name='" + name + "'")
            .add("requiredQuantity=" + requiredQuantity)
            .toString();
    }
}
