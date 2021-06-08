package coffeeMachine;

import ingredient.IngredientsHolder;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import javafx.util.Pair;
import recipe.Recipe;
import recipe.RecipeHolder;
import recipe.RecipeIngredient;

/**
 * Class to simulate a Coffee machine With following functionality
 * 1. Parallel Execution - N oulets
 * 2. Ability to add new Ingredients
 * 3. Ability to add new Recipes
 * 4. Ability to refill ingredients which are running low on quantity
 */
public class CoffeeMachine {

    /**
     * IngredientsHolder object to store all the available ingredients
     */
    private IngredientsHolder ingredientsHolder; // Holder to hold all the ingredients

    /**
     * RecipeHolder object to store all the available recipes
     */
    private RecipeHolder recipeHolder; // Holder to hold all the recipes

    /**
     * Variable to store total number of outlets
     * CoffeeMachine will be able to execute this many order simultaneously
     */
    private final int totalOutlets;

    /**
     * Variable to store if machine is started or not
     * Until machine is started no operations are allowed
     */
    private boolean machineStarted =  false;

    /**
     * Semaphore to restrict parallel execution to totalOutlets
     */
    private final Semaphore outletCountLock;

    /**
     * Lock to handle threads in all system level functions
     * Only one system level function will be called at a time
     */
    private final ReentrantLock holderLock = new ReentrantLock();

    /**
     * Creates a structure of coffee machine
     * @param numOutlets number of outlets machine will have
     */
    public CoffeeMachine (int numOutlets) {
        this.totalOutlets = numOutlets;
        this.outletCountLock = new Semaphore(numOutlets); // Set default to 1 outlet
    }

    /**
     * Function to check if machine is started or not
     * This function is used to restrict all the other functions to work until machine is initialized
     * @throws Exception when machine is not yet started
     */
    private void checkIfMachineStarted() throws Exception {
        if (!machineStarted) {
            throw new Exception("Machine is not started yet");
        }
    }

    /**
     * Function to initialze and start the machine with given ingredients and recipes
     * @param ingredients ingredients to store in ingredientsHolder
     * @param recipes recipes to hold in recipeHolder
     * @throws Exception if machine is already started
     */
    public void initialize(List<Pair<String, Integer>> ingredients, List<Pair<String, List<Pair<String, Integer>>>> recipes) throws Exception {
        try {
            holderLock.lock();

            if (machineStarted) {
                throw new Exception("Machine already started");
            }
            this.ingredientsHolder = new IngredientsHolder(ingredients);
            this.recipeHolder = new RecipeHolder(recipes);
            this.machineStarted = true;

        } finally {
            holderLock.unlock();
        }
    }

    /**
     * Function to prepare a given beverage
     * This function can be used by totalOutlets number of threads in parallel
     * @param beverage Recipe to prepare
     * @return status of preparation
     * @throws Exception when machine is not yet started
     */
    public String prepareBeverage(String beverage) throws Exception {
        checkIfMachineStarted();

        try {
            this.outletCountLock.acquire(); // It will allow only N outlets to work at the same time
            // First fetch the recipe
            Recipe recipe = this.recipeHolder.getRecipe(beverage); // This is thread safe because we are not allowing changing existing recipes
            if (recipe == null) {
                return String.format("%s is not a valid beverage", beverage);
            }

            for (RecipeIngredient recipeIngredient : recipe.getIngredients()) {
                if (!this.ingredientsHolder.isIngredientPresent(recipeIngredient.getName())) {
                    return String.format("%s cannot be prepared because %s is not available", beverage, recipeIngredient.getName());
                }
                if (!this.ingredientsHolder.useIngredient(recipeIngredient.getName(), recipeIngredient.getRequiredQuantity())) {
                    return String.format("%s cannot be prepared because item %s is not sufficient", beverage, recipeIngredient.getName());
                }
            }

            return String.format("%s is prepared", beverage);
        } finally {
            this.outletCountLock.release(); // release semaphore
        }
    }

    /**
     * Function to add a new ingredient to ingredientHolder
     * @param item name of the ingredient to add
     * @param quantity quantity of ingredient to initialize
     * @throws Exception when machine is not yet started
     */
    public void addNewIngredient(String item, int quantity) throws Exception {
        checkIfMachineStarted();
        try {
            holderLock.lock();
            this.ingredientsHolder.addNewIngredient(item, quantity);
        } finally {
            holderLock.unlock();
        }
    }

    /**
     * Function to add a new recipe to recipeHolder
     * @param name name of the recipe to add
     * @param ingredients ingredients of the recipe to add
     * @throws Exception when machine is not yet started
     */
    public void addRecipe(String name, List<Pair<String, Integer>> ingredients) throws Exception {
        checkIfMachineStarted();
        try {
            holderLock.lock();
            this.recipeHolder.addNewRecipe(name, ingredients);
        } finally {
            holderLock.unlock();
        }
    }

    /**
     * Function to refill all the ingredients to their maxQuantity
     * @throws Exception when machine is not yet started
     */
    public void refillAllIngredients() throws Exception {
        checkIfMachineStarted();
        try {
            holderLock.lock();
            this.ingredientsHolder.refillAllIngredients();
        } finally {
            holderLock.unlock();
        }
    }

    /**
     * Function to refill only running low ingredients
     * @throws Exception when machine is not yet started
     */
    public void refillRunningLowIngredients() throws Exception {
        try {
            holderLock.lock();
            List<String> ingredients = getRunningLowIngredients();
            for (String ingredient : ingredients) {
                this.ingredientsHolder.refillIngredient(ingredient);
            }
        } finally {
            holderLock.unlock();
        }
    }

    /**
     * Function to get a list of all running low ingredients
     * @return A list of all the ingredients who are running low on quantity
     * @throws Exception when machine is not yet started
     */
    public List<String> getRunningLowIngredients() throws Exception {
        checkIfMachineStarted();
        try {
            holderLock.lock();
            return this.ingredientsHolder.getRunningLowIngredients();
        } finally {
            holderLock.unlock();
        }
    }

    /**
     * Function to get total number of outlets in the machine
     * @return number of outlets
     */
    public int getTotalOutlets() {
        return totalOutlets;
    }

    /**
     * Function to get total number of ingredients in ingredientsHolder
     * @return total number of ingredients in ingredientsHolder
     * @throws Exception when machine is not yet started
     */
    public int getTotalIngredients() throws Exception {
        checkIfMachineStarted();
        try {
            holderLock.lock();
            return this.ingredientsHolder.getTotalIngredients();
        } finally {
            holderLock.unlock();
        }
    }

    /**
     * Function to get total number of recipes in recipeHolder
     * @return total number of recipes in recipeHolder
     * @throws Exception when machine is not yet started
     */
    public int getTotalRecipes() throws Exception {
        checkIfMachineStarted();
        try {
            holderLock.lock();
            return this.recipeHolder.getTotalRecipes();
        } finally {
            holderLock.unlock();
        }
    }
}
