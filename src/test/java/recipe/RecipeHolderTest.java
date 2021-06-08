package recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class RecipeHolderTest {

    private  List<Pair<String, List<Pair<String, Integer>>>> getTestRecipes() {

        JSONObject recipesTestObject = new JSONObject("{\n"
            + "      \"hot_tea\": {\n"
            + "        \"hot_water\": 200,\n"
            + "        \"hot_milk\": 100,\n"
            + "        \"ginger_syrup\": 10,\n"
            + "        \"sugar_syrup\": 10,\n"
            + "        \"tea_leaves_syrup\": 30\n"
            + "      },\n"
            + "      \"hot_coffee\": {\n"
            + "        \"hot_water\": 100,\n"
            + "        \"ginger_syrup\": 30,\n"
            + "        \"hot_milk\": 400,\n"
            + "        \"sugar_syrup\": 50,\n"
            + "        \"tea_leaves_syrup\": 30\n"
            + "      },\n"
            + "      \"black_tea\": {\n"
            + "        \"hot_water\": 300,\n"
            + "        \"ginger_syrup\": 30,\n"
            + "        \"sugar_syrup\": 50,\n"
            + "        \"tea_leaves_syrup\": 30\n"
            + "      },\n"
            + "      \"green_tea\": {\n"
            + "        \"hot_water\": 100,\n"
            + "        \"ginger_syrup\": 30,\n"
            + "        \"sugar_syrup\": 50,\n"
            + "        \"green_mixture\": 30\n"
            + "      },\n"
            + "    }");

        List<Pair<String, List<Pair<String, Integer>>>> recipes = new ArrayList<>();
        for (Object keyObj : recipesTestObject.names()) {
            JSONObject recipeIngredients = recipesTestObject.getJSONObject((String) keyObj);
            if (recipeIngredients instanceof JSONObject) {
                List<Pair<String, Integer>> rawIngredientsList = new ArrayList<>();
                for (Map.Entry<String, Object> entry : recipeIngredients.toMap().entrySet()) {
                    if (entry.getValue() instanceof Integer) {
                        rawIngredientsList.add(new Pair<>(entry.getKey(), (int)entry.getValue()));
                    }
                }
                recipes.add(new Pair<>((String) keyObj, rawIngredientsList));
            }
        }
        return recipes;
    }

    @Test
    public void testInitialisation() {
        // Initialisation with empty state
        RecipeHolder recipeHolder = new RecipeHolder();
        assertEquals(0, recipeHolder.getTotalRecipes());

        // Initialisation with some initial State
        recipeHolder = new RecipeHolder(getTestRecipes());
        assertEquals(4, recipeHolder.getTotalRecipes());
    }

    @Test
    public void testRecipeHolderFunctions() {
        RecipeHolder recipeHolder = new RecipeHolder(getTestRecipes());
        assertEquals(4, recipeHolder.getTotalRecipes());

        // get a not existing recipe
        Recipe recipe = recipeHolder.getRecipe("test");
        assertNull(recipe);

        recipe = recipeHolder.getRecipe("green_tea");
        assertNotNull(recipe);
        assertEquals(4, recipe.getIngredients().size());
        assertEquals("green_tea", recipe.getName());
    }

    @Test
    public void testToString() {
        RecipeHolder recipeHolder = new RecipeHolder(getTestRecipes());
        assertEquals("RecipeHolder[recipes={hot_coffee=Recipe[name='hot_coffee', ingredients=[RecipeIngredient[name='hot_milk', requiredQuantity=400], RecipeIngredient[name='ginger_syrup', requiredQuantity=30], RecipeIngredient[name='sugar_syrup', requiredQuantity=50], RecipeIngredient[name='tea_leaves_syrup', requiredQuantity=30], RecipeIngredient[name='hot_water', requiredQuantity=100]]], green_tea=Recipe[name='green_tea', ingredients=[RecipeIngredient[name='green_mixture', requiredQuantity=30], RecipeIngredient[name='ginger_syrup', requiredQuantity=30], RecipeIngredient[name='sugar_syrup', requiredQuantity=50], RecipeIngredient[name='hot_water', requiredQuantity=100]]], black_tea=Recipe[name='black_tea', ingredients=[RecipeIngredient[name='ginger_syrup', requiredQuantity=30], RecipeIngredient[name='sugar_syrup', requiredQuantity=50], RecipeIngredient[name='tea_leaves_syrup', requiredQuantity=30], RecipeIngredient[name='hot_water', requiredQuantity=300]]], hot_tea=Recipe[name='hot_tea', ingredients=[RecipeIngredient[name='hot_milk', requiredQuantity=100], RecipeIngredient[name='ginger_syrup', requiredQuantity=10], RecipeIngredient[name='sugar_syrup', requiredQuantity=10], RecipeIngredient[name='tea_leaves_syrup', requiredQuantity=30], RecipeIngredient[name='hot_water', requiredQuantity=200]]]}]", recipeHolder.toString());
    }
}
