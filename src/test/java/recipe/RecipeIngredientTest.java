package recipe;

import org.junit.Test;

import static org.junit.Assert.*;

public class RecipeIngredientTest {
    @Test
    public void testInitialisation() {
        RecipeIngredient recipeIngredient = new RecipeIngredient("recipe_ingredient_1", 100);
        assertEquals("recipe_ingredient_1", recipeIngredient.getName());
        assertEquals(100, recipeIngredient.getRequiredQuantity());
        assertEquals("RecipeIngredient[name='recipe_ingredient_1', requiredQuantity=100]", recipeIngredient.toString());
    }
}
