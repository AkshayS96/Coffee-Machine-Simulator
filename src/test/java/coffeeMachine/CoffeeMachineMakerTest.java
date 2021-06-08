package coffeeMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoffeeMachineMakerTest {
    @Test
    public void testCoffeeMachineCreationSingleThreaded() throws Exception {
        CoffeeMachine coffeeMachine = CoffeeMachineMaker.getCoffeeMachineFromGivenInput("{\n"
            + "  \"machine\": {\n"
            + "    \"outlets\": {\n"
            + "      \"count_n\": 3\n"
            + "    },\n"
            + "    \"total_items_quantity\": {\n"
            + "      \"hot_water\": 500,\n"
            + "      \"hot_milk\": 500,\n"
            + "      \"ginger_syrup\": 100,\n"
            + "      \"sugar_syrup\": 100,\n"
            + "      \"tea_leaves_syrup\": 100\n"
            + "    },\n"
            + "    \"beverages\": {\n"
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
            + "    }\n"
            + "  }\n"
            + "}");
        assertNotNull(coffeeMachine);

        assertEquals(3, coffeeMachine.getTotalOutlets());
        assertEquals(5, coffeeMachine.getTotalIngredients());
        assertEquals(4, coffeeMachine.getTotalRecipes());

        assertEquals("hot_tea is prepared", coffeeMachine.prepareBeverage("hot_tea"));
        assertEquals("black_tea is prepared", coffeeMachine.prepareBeverage("black_tea"));
        assertEquals("green_tea cannot be prepared because green_mixture is not available", coffeeMachine.prepareBeverage("green_tea"));
        assertEquals("hot_coffee cannot be prepared because item sugar_syrup is not sufficient", coffeeMachine.prepareBeverage("hot_coffee"));
    }


    @Test
    public void testCoffeeMachineCreationMultiThreaded() throws Exception {
        CoffeeMachine coffeeMachine = CoffeeMachineMaker.getCoffeeMachineFromGivenInput("{\n"
            + "  \"machine\": {\n"
            + "    \"outlets\": {\n"
            + "      \"count_n\": 10\n"
            + "    },\n"
            + "    \"total_items_quantity\": {\n"
            + "      \"hot_water\": 500000,\n"
            + "      \"hot_milk\": 500000,\n"
            + "      \"ginger_syrup\": 100000,\n"
            + "      \"sugar_syrup\": 100000,\n"
            + "      \"tea_leaves_syrup\": 100000\n"
            + "    },\n"
            + "    \"beverages\": {\n"
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
            + "    }\n"
            + "  }\n"
            + "}");
        assertNotNull(coffeeMachine);

        String[] recipes = {"hot_tea", "hot_coffee", "black_tea", "green_tea"};

        ExecutorService es = Executors.newFixedThreadPool(100); // Lets make 100 thread
        List<Future<String>> futureList = new ArrayList<>();
        for (int i=0;i<10000;i++) { // thousand orders
            int index = i;
            Future<String> result = es.submit(() -> {
                return coffeeMachine.prepareBeverage(recipes[index % recipes.length]);
            });
            futureList.add(result);
        }

        assertEquals(10000, futureList.size());
        for (int i=0;i<=3332;i++) {
            String result = futureList.get(i).get();
            String expected;
            if (i % recipes.length == 3) {
                expected = "green_tea cannot be prepared because green_mixture is not available";
            } else {
                expected = String.format("%s is prepared", recipes[i % recipes.length]);
            }
            assertEquals(expected, result);
        }

        for (int i=3333;i<10000;i++) {
            String result = futureList.get(i).get();
            String recipe = recipes[i % recipes.length];
            assertTrue(result.startsWith(String.format("%s cannot be prepared", recipe)));
        }

        assertEquals(5, coffeeMachine.getRunningLowIngredients().size());
    }
}
