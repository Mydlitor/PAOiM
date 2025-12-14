package exceptions;

/**
 * Exception thrown when attempting to add a horse that already exists in stable
 */
public class DuplicateHorseException extends StableException {
    public DuplicateHorseException(String horseName, String stableName) {
        super(String.format("Horse '%s' already exists in stable '%s'", horseName, stableName));
    }
}
