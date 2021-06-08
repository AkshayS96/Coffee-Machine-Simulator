###Coffee Machine Simulator

This project simulates the working of a coffee machine with following functionality
1. Ability to add ingredients
2. Ability to add recipes
3. Simultaneous execution of multiple orders
4. Refilling of ingredients


To play with the functionality use `CoffeeMachineMaker.java` to make a coffee machine.
`CoffeeMachineMaker.getCoffeeMachineFromGivenInput` requires either a JSONObject or string representation of json.

Example of string representation is
```json
{
  "machine": {
    "outlets": {
      "count_n": 3
    },
    "total_items_quantity": {
      "hot_water": 500,
      "hot_milk": 500,
      "ginger_syrup": 100,
      "sugar_syrup": 100,
      "tea_leaves_syrup": 100
    },
    "beverages": {
      "hot_tea": {
        "hot_water": 200,
        "hot_milk": 100,
        "ginger_syrup": 10,
        "sugar_syrup": 10,
        "tea_leaves_syrup": 30
      },
      "hot_coffee": {
        "hot_water": 100,
        "ginger_syrup": 30,
        "hot_milk": 400,
        "sugar_syrup": 50,
        "tea_leaves_syrup": 30
      },
      "black_tea": {
        "hot_water": 300,
        "ginger_syrup": 30,
        "sugar_syrup": 50,
        "tea_leaves_syrup": 30
      },
      "green_tea": {
        "hot_water": 100,
        "ginger_syrup": 30,
        "sugar_syrup": 50,
        "green_mixture": 30
      },
    }
  }
}
```
Check main function of `CoffeeMachineMaker.java` to understand more

1. `CoffeeMachine.java` - Code for CoffeeMachine class with required functionality
2. `Ingredient.java` - Code for raw ingredients
3. `IngredientsHolder.java` - Code for the holder of ingredients
4. `Recipe.java` - Code for recipe
5. `RecipeHolder.java` - Code for the holder of recipes
6. `RecipeIngredient.java` - Code for recipe ingredients
7. `CoffeeMachineMaker` - Helper class to create the coffee machine from json input