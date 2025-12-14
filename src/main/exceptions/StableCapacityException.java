package exceptions;

/**
 * Exception thrown when stable capacity is exceeded
 */
public class StableCapacityException extends StableException {
    public StableCapacityException(String stableName, int capacity) {
        super(String.format("Stable '%s' has reached maximum capacity of %d horses", stableName, capacity));
    }
}
