package coffeeMachine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import org.json.JSONObject;

/**
 * Class to create a coffee machine from given json representation of a machine
 * For eg Given below is the format of coffee machine
 * {
 *   "machine": {
 *     "outlets": {
 *       "count_n": 3 --> number of outlets in the machine
 *     },
 *     "total_items_quantity": { ---> raw ingredients details
 *       "hot_water": 500,
 *       "hot_milk": 500,
 *       "ginger_syrup": 100,
 *       "sugar_syrup": 100,
 *       "tea_leaves_syrup": 100
 *     },
 *     "beverages": { ---------> beverages details
 *       "hot_tea": { ----------> recipe with its required ingredients
 *         "hot_water": 200,
 *         "hot_milk": 100,
 *         "ginger_syrup": 10,
 *         "sugar_syrup": 10,
 *         "tea_leaves_syrup": 30
 *       },
 *       "hot_coffee": {
 *         "hot_water": 100,
 *         "ginger_syrup": 30,
 *         "hot_milk": 400,
 *         "sugar_syrup": 50,
 *         "tea_leaves_syrup": 30
 *       },
 *       "black_tea": {
 *         "hot_water": 300,
 *         "ginger_syrup": 30,
 *         "sugar_syrup": 50,
 *         "tea_leaves_syrup": 30
 *       },
 *       "green_tea": {
 *         "hot_water": 100,
 *         "ginger_syrup": 30,
 *         "sugar_syrup": 50,
 *         "green_mixture": 30
 *       },
 *     }
 *   }
 * }
 */
public class CoffeeMachineMaker {
    private static final String MACHINE = "machine";
    public static final String TOTAL_ITEMS_QUANTITY = "total_items_quantity";
    public static final String OUTLETS = "outlets";
    public static final String BEVERAGES = "beverages";
    public static final String OUTLETS_COUNT = "count_n";

    /**
     * Function to get List of pairs of ingredients with their name and quantity
     * @param rawIngredientsObject json representation of a collection of ingredients
     * @return list of pair of ingredients name and quantity
     */
    private static List<Pair<String, Integer>> getIngredients(JSONObject rawIngredientsObject) {
        List<Pair<String, Integer>> rawIngredientsList = new ArrayList<>();
        for (Map.Entry<String, Object> entry : rawIngredientsObject.toMap().entrySet()) {
            if (entry.getValue() instanceof Integer) {
                rawIngredientsList.add(new Pair<>(entry.getKey(), (int)entry.getValue()));
            }
        }
        return rawIngredientsList;
    }

    /**
     * Function to get list of recipes in form of pair(name, ingredients pairs)
     * @param recipesObject Json representation of a collection of recipes
     * @return list of pair of recipes name and their ingredients
     */
    private static List<Pair<String, List<Pair<String, Integer>>>> getRecipes(JSONObject recipesObject) {
        List<Pair<String, List<Pair<String, Integer>>>> recipes = new ArrayList<>();
        for (Object keyObj : recipesObject.names()) {
            JSONObject recipeIngredients = recipesObject.getJSONObject((String) keyObj);
            if (recipeIngredients instanceof JSONObject) {
                List<Pair<String, Integer>> ingredients = getIngredients(recipeIngredients);
                recipes.add(new Pair<>((String) keyObj, ingredients));
            }
        }
        return recipes;
    }

    /**
     * Create machine from json string representation
     * @param coffeeMachineConfiguration json string representation of machine
     * @return CoffeeMachine object
     * @throws Exception when machine is not valid
     */
    public static CoffeeMachine getCoffeeMachineFromGivenInput(String coffeeMachineConfiguration)
        throws Exception {
        JSONObject machineConfig = new JSONObject(coffeeMachineConfiguration);
        return getCoffeeMachineFromGivenInput(machineConfig);
    }

    /**
     * Create machine from json representation
     * @param machineConfig json representation of machine
     * @return CoffeeMachine object
     * @throws Exception when machine is not valid
     */
    public static CoffeeMachine getCoffeeMachineFromGivenInput(JSONObject machineConfig)
        throws Exception {
        JSONObject machine = machineConfig.getJSONObject(MACHINE);
        if (machine == null) {
            throw new Exception("machine field is mandatory");
        }

        JSONObject outlets = machine.getJSONObject(OUTLETS);
        JSONObject rawIngredients = machine.getJSONObject(TOTAL_ITEMS_QUANTITY);
        JSONObject beverages = machine.getJSONObject(BEVERAGES);

        if (outlets == null || rawIngredients == null || beverages == null) {
            throw new Exception("outlets, total_items_quantity, beverages are all mandatory fields");
        }

        int totalOutlets = outlets.getInt(OUTLETS_COUNT);
        List<Pair<String, Integer>> rawIngredientsList = getIngredients(rawIngredients);
        List<Pair<String, List<Pair<String, Integer>>>> recipes = getRecipes(beverages);

        CoffeeMachine coffeeMachine = new CoffeeMachine(totalOutlets);
        coffeeMachine.initialize(rawIngredientsList, recipes);
        return coffeeMachine;
    }

    // Check this to understand how input will be given to machine maker functions
    public static void main(String[] args) throws Exception {
        String machine = "{\n"
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
            + "}";
        CoffeeMachine coffeeMachine = getCoffeeMachineFromGivenInput(machine);
    }
}
