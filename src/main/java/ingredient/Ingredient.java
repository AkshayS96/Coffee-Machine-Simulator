package ingredient;

import java.util.StringJoiner;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Ingredient is used to hold a single type of raw ingredient for CoffeeMachine
 * It has name, quantity, maxQuantity associated with it from which quantity will change overtime as ingredient is being used
 */
public class Ingredient {

    /**
     * Variable to store name of the ingredient
     */
    private final String name;

    /**
     * Variable to store current quantity of the ingredient
     */
    private int quantity;

    /**
     * Variable to store max allowed quantity of the ingredient
     */
    private final int maxQuantity;

    /**
     * Lock to handle updates from multiple threads
     */
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * Constructs a new Ingredient with given name, initialQuantity and maxQuantity
     * @param name ingredients name.
     * @param initialQuantity initial quantity.
     * @param maxQuantity max allowed quantity.
     */
    public Ingredient(String name, int initialQuantity, int maxQuantity) {
        this.name = name;
        this.quantity = initialQuantity;
        this.maxQuantity = maxQuantity;
    }

    /**
     * Getter for name of ingredient
     * @return name associated with ingredient
     */
    public String getName() {
        return this.name;
    }

    /**
     * Refill current ingredient to its maxQuantity
     */
    public void refillIngredient() {
        try {
            lock.lock();
            this.quantity = this.maxQuantity; // Fill till max quantity
        } finally {
            lock.unlock();;
        }
    }

    /**
     * Get current available quantity of the ingredient
     * @return current available quantity
     */
    public int getQuantity() {
        try {
            lock.lock();
            return this.quantity;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Get max quantity that ingredient can store
     * @return return max quantity
     */
    public int getMaxQuantity() {
        return this.maxQuantity; // It will be same for every thread so no locking in this case
    }

    /**
     * Function to check if the ingredient is running low
     * @return True if current available quantity is less than 50% of the max allowed quantity else False
     */
    public boolean isRunningLow() {
        // Low quantity is considered when quantity is less than 20% of max quantity
        try {
            lock.lock();
            return ((this.quantity * 1.0) / (Math.max(1.0, this.maxQuantity))) <= 0.5;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Function to use ingredient and update its available quantity
     * @param requiredQuantity Quantity that is required
     * @return True if requiredQuantity is available else False
     */
    public boolean useIngredient(int requiredQuantity) {
        try {
            lock.lock();
            if (this.quantity < requiredQuantity) {
                return false;
            }
            this.quantity -= requiredQuantity;
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * @return Returns a string representation of this class
     */
    @Override
    public String toString() {
        return new StringJoiner(", ", Ingredient.class.getSimpleName() + "[", "]")
            .add("name='" + name + "'")
            .add("quantity=" + quantity)
            .add("maxQuantity=" + maxQuantity)
            .toString();
    }
}
