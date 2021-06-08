package ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
import org.junit.Test;

import static org.junit.Assert.*;

public class IngredientsHolderTest {
    @Test
    public void testInitialisation() {
        IngredientsHolder ingredientsHolder = new IngredientsHolder();
        assertFalse(ingredientsHolder.isIngredientPresent("test"));
        assertEquals(0, ingredientsHolder.getTotalIngredients());

        List<Pair<String, Integer>> initialIngredients = new ArrayList<>(Arrays.asList(new Pair<>("ingredient_1", 100), new Pair<>("ingredient_2", 100), new Pair<>("ingredient_3", 50)));
        ingredientsHolder = new IngredientsHolder(initialIngredients);

        assertEquals(3, ingredientsHolder.getTotalIngredients());
        assertTrue(ingredientsHolder.isIngredientPresent("ingredient_1"));
        assertTrue(ingredientsHolder.isIngredientPresent("ingredient_2"));
        assertTrue(ingredientsHolder.isIngredientPresent("ingredient_3"));
        assertFalse(ingredientsHolder.isIngredientPresent("ingredient_4"));
        assertEquals(0, ingredientsHolder.getRunningLowIngredients().size());
    }

    @Test
    public void testIngredientUsage() {
        List<Pair<String, Integer>> initialIngredients = new ArrayList<>(Arrays.asList(new Pair<>("ingredient_1", 100), new Pair<>("ingredient_2", 100), new Pair<>("ingredient_3", 50)));
        IngredientsHolder ingredientsHolder = new IngredientsHolder(initialIngredients);

        assertEquals(3, ingredientsHolder.getTotalIngredients());
        assertEquals(0, ingredientsHolder.getRunningLowIngredients().size());

        // Use some not existing ingredients
        assertFalse(ingredientsHolder.useIngredient("non_existing", 10));

        // Use some ingredient
        assertTrue(ingredientsHolder.useIngredient("ingredient_1", 60));
        assertTrue(ingredientsHolder.useIngredient("ingredient_2", 60));

        // Check low running ingredients
        assertEquals(2, ingredientsHolder.getRunningLowIngredients().size());

        // get quantity more than available
        assertFalse(ingredientsHolder.useIngredient("ingredient_1", 50));

        // Get Quantity less than available
        assertTrue(ingredientsHolder.useIngredient("ingredient_2", 40));

        // Refill one ingredient
        ingredientsHolder.refillIngredient("ingredient_1");

        // Low running ingredients will decrease after refilling one of the ingredient
        assertEquals(1, ingredientsHolder.getRunningLowIngredients().size());

        // Refilling all
        ingredientsHolder.refillAllIngredients();

        assertEquals(0, ingredientsHolder.getRunningLowIngredients().size());
    }

    @Test
    public void testNewIngredientInsertion() {
        List<Pair<String, Integer>> initialIngredients = new ArrayList<>(Arrays.asList(new Pair<>("ingredient_1", 100), new Pair<>("ingredient_2", 100), new Pair<>("ingredient_3", 50)));
        IngredientsHolder ingredientsHolder = new IngredientsHolder(initialIngredients);

        assertEquals(3, ingredientsHolder.getTotalIngredients());
        assertEquals(0, ingredientsHolder.getRunningLowIngredients().size());

        // Add a existing ingredient
        ingredientsHolder.addNewIngredient("ingredient_1", 1000);
        assertEquals(3, ingredientsHolder.getTotalIngredients());

        // Add a new ingredient
        ingredientsHolder.addNewIngredient("ingredient_4", 1000);
        assertEquals(4, ingredientsHolder.getTotalIngredients());
    }

    @Test
    public void testToString() {
        List<Pair<String, Integer>> initialIngredients = new ArrayList<>(Arrays.asList(new Pair<>("ingredient_1", 100), new Pair<>("ingredient_2", 100), new Pair<>("ingredient_3", 50)));
        IngredientsHolder ingredientsHolder = new IngredientsHolder(initialIngredients);

        assertEquals("IngredientsHolder[ingredients={ingredient_3=Ingredient[name='ingredient_3', quantity=50, maxQuantity=50], ingredient_2=Ingredient[name='ingredient_2', quantity=100, maxQuantity=100], ingredient_1=Ingredient[name='ingredient_1', quantity=100, maxQuantity=100]}]", ingredientsHolder.toString());
    }
}
