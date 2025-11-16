package pl.edu.pao.exceptions;

/**
 * Exception thrown when attempting to add a stable that already exists
 */
public class DuplicateStableException extends StableException {
    public DuplicateStableException(String stableName) {
        super(String.format("Stable '%s' already exists", stableName));
    }
}
