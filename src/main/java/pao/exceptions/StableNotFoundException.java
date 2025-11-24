package pao.exceptions;

/**
 * Exception thrown when a stable is not found
 */
public class StableNotFoundException extends StableException {
    public StableNotFoundException(String stableName) {
        super(String.format("Stable '%s' not found", stableName));
    }
}
