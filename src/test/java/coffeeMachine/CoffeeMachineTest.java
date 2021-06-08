package coffeeMachine;

import java.util.ArrayList;
import java.util.Arrays;
import javafx.util.Pair;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoffeeMachineTest {
    @Test
    public void testInitialisation() throws Exception {
        CoffeeMachine coffeeMachine = new CoffeeMachine(1);

        assertEquals(1, coffeeMachine.getTotalOutlets());

        // Machine is not started yet so all the function should throw errors while calling
        Throwable throwable = assertThrows(Exception.class, () -> coffeeMachine.refillRunningLowIngredients());
        assertEquals("Machine is not started yet", throwable.getMessage());

        throwable = assertThrows(Exception.class, () -> coffeeMachine.prepareBeverage("test_bevergae"));
        assertEquals("Machine is not started yet", throwable.getMessage());

        throwable = assertThrows(Exception.class, () -> coffeeMachine.refillAllIngredients());
        assertEquals("Machine is not started yet", throwable.getMessage());

        throwable = assertThrows(Exception.class, () -> coffeeMachine.refillRunningLowIngredients());
        assertEquals("Machine is not started yet", throwable.getMessage());

        throwable = assertThrows(Exception.class, () -> coffeeMachine.addNewIngredient("new ingredient", 100));
        assertEquals("Machine is not started yet", throwable.getMessage());

        throwable = assertThrows(Exception.class, () -> coffeeMachine.addRecipe("mnew recipe", new ArrayList<>()));
        assertEquals("Machine is not started yet", throwable.getMessage());
    }

    private void insertTestIngredientsToCoffeeMachine(CoffeeMachine coffeeMachine) throws Exception {
        // Add some ingredients
        coffeeMachine.addNewIngredient("ingredient_1", 110);
        coffeeMachine.addNewIngredient("ingredient_2", 120);
        coffeeMachine.addNewIngredient("ingredient_3", 130);
    }

    private void insertTestRecipesToCoffeeMachine(CoffeeMachine coffeeMachine) throws Exception {
        // Now add some recipes
        coffeeMachine.addRecipe("recipe_1", new ArrayList<>(Arrays.asList(new Pair<>("ingredient_1", 40), new Pair<>("ingredient_2", 70))));
        coffeeMachine.addRecipe("recipe_2", new ArrayList<>(Arrays.asList(new Pair<>("ingredient_2", 40), new Pair<>("ingredient_3", 70))));
        coffeeMachine.addRecipe("recipe_3", new ArrayList<>(Arrays.asList(new Pair<>("ingredient_3", 10), new Pair<>("ingredient_1", 70))));
    }

    @Test
    public void testCoffeeMachineAfterInitialisation() throws Exception {
        CoffeeMachine coffeeMachine = new CoffeeMachine(1);
        assertEquals(1, coffeeMachine.getTotalOutlets());

        coffeeMachine.initialize(new ArrayList<>(), new ArrayList<>());

        assertEquals(0, coffeeMachine.getTotalIngredients());
        assertEquals(0, coffeeMachine.getTotalRecipes());

        assertEquals("test_beverage_that_does_not_exist is not a valid beverage", coffeeMachine.prepareBeverage("test_beverage_that_does_not_exist"));

        assertEquals(0, coffeeMachine.getRunningLowIngredients().size());

        insertTestIngredientsToCoffeeMachine(coffeeMachine);

        assertEquals(3, coffeeMachine.getTotalIngredients());
        assertEquals(0, coffeeMachine.getTotalRecipes());

        insertTestRecipesToCoffeeMachine(coffeeMachine);

        assertEquals(3, coffeeMachine.getTotalIngredients());
        assertEquals(3, coffeeMachine.getTotalRecipes());

        // Now prepare some beverages
        assertEquals("recipe_1 is prepared", coffeeMachine.prepareBeverage("recipe_1"));
        assertEquals("recipe_2 is prepared", coffeeMachine.prepareBeverage("recipe_2"));
        assertEquals("recipe_3 is prepared", coffeeMachine.prepareBeverage("recipe_3"));
        assertEquals("recipe_2 cannot be prepared because item ingredient_2 is not sufficient", coffeeMachine.prepareBeverage("recipe_2"));
    }

    @Test
    public void testBeveragePrepareAndRefill() throws Exception {
        CoffeeMachine coffeeMachine = new CoffeeMachine(1);
        coffeeMachine.initialize(new ArrayList<>(), new ArrayList<>());
        assertEquals(0, coffeeMachine.getTotalIngredients());
        assertEquals(0, coffeeMachine.getTotalRecipes());

        // Add test ingredients
        insertTestIngredientsToCoffeeMachine(coffeeMachine);

        // Add test recipes
        insertTestRecipesToCoffeeMachine(coffeeMachine);

        assertEquals(3, coffeeMachine.getTotalIngredients());
        assertEquals(3, coffeeMachine.getTotalRecipes());

        // Run once and this time all should be prepared
        assertEquals("recipe_1 is prepared", coffeeMachine.prepareBeverage("recipe_1"));
        assertEquals("recipe_2 is prepared", coffeeMachine.prepareBeverage("recipe_2"));
        assertEquals("recipe_3 is prepared", coffeeMachine.prepareBeverage("recipe_3"));

        assertEquals(3, coffeeMachine.getRunningLowIngredients().size());

        // Run again this time all should give not sufficient value
        assertEquals("recipe_1 cannot be prepared because item ingredient_1 is not sufficient", coffeeMachine.prepareBeverage("recipe_1"));
        assertEquals("recipe_2 cannot be prepared because item ingredient_2 is not sufficient", coffeeMachine.prepareBeverage("recipe_2"));
        assertEquals("recipe_3 cannot be prepared because item ingredient_1 is not sufficient", coffeeMachine.prepareBeverage("recipe_3"));


        // Now refill all values and then again try to make beverage and this time it should work
        coffeeMachine.refillAllIngredients();
        assertEquals("recipe_1 is prepared", coffeeMachine.prepareBeverage("recipe_1"));
        assertEquals("recipe_2 is prepared", coffeeMachine.prepareBeverage("recipe_2"));
        assertEquals("recipe_3 is prepared", coffeeMachine.prepareBeverage("recipe_3"));
    }
}
