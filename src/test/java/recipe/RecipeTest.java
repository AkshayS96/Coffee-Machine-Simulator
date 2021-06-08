package recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.util.Pair;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecipeTest {
    @Test
    public void testInitialisation() {
        List<Pair<String, Integer>> ingredients = new ArrayList<>(Arrays.asList(new Pair<>("ingredient_1", 10), new Pair<>("ingredient_2", 30), new Pair<>("ingredient_3", 20)));
        Recipe recipe = new Recipe("test_recipe", ingredients);

        List<RecipeIngredient> savedIngredients = recipe.getIngredients();
        assertEquals(3, savedIngredients.size());
        assertEquals("test_recipe", recipe.getName());

        assertEquals("Recipe[name='test_recipe', ingredients=[RecipeIngredient[name='ingredient_1', requiredQuantity=10], RecipeIngredient[name='ingredient_2', requiredQuantity=30], RecipeIngredient[name='ingredient_3', requiredQuantity=20]]]", recipe.toString());
    }
}
