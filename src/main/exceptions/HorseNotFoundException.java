package exceptions;

/**
 * Exception thrown when a horse is not found
 */
public class HorseNotFoundException extends StableException {
    public HorseNotFoundException(String horseName) {
        super(String.format("Horse '%s' not found", horseName));
    }
}
