package ingredient;

import org.junit.Test;

import static org.junit.Assert.*;

public class IngredientTest {
    @Test
    public void testIngredientInitialisation() {
        Ingredient ingredient = new Ingredient("test_ingredient", 10, 100);
        assertEquals(10, ingredient.getQuantity());
        assertEquals(100, ingredient.getMaxQuantity());
        assertEquals("test_ingredient", ingredient.getName());
    }

    @Test
    public void testUseIngredient() {
        Ingredient ingredient = new Ingredient("test_ingredient", 100, 100);
        assertEquals(100, ingredient.getQuantity());
        assertEquals(100, ingredient.getMaxQuantity());

        // Use some ingredient this should return true because ingredient have this much quantity
        assertTrue(ingredient.useIngredient(50));

        assertEquals(50, ingredient.getQuantity());
        assertEquals(100, ingredient.getMaxQuantity());

        // This will be false because ingredient now has quantity = 50
        assertFalse(ingredient.useIngredient(60));

        ingredient.refillIngredient(); // Fill with more but it will take only till max quantity
        assertEquals(ingredient.getMaxQuantity(), ingredient.getQuantity());

        assertTrue(ingredient.useIngredient(70));

        assertEquals(30, ingredient.getQuantity());
    }

    @Test
    public void testToString() {
        Ingredient ingredient = new Ingredient("test_ingredient", 100, 1000);
        String expected = "Ingredient[name='test_ingredient', quantity=100, maxQuantity=1000]";
        String actual = ingredient.toString();
        assertEquals(expected, actual);
    }
}
