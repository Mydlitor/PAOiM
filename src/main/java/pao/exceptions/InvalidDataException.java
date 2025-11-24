package pao.exceptions;

/**
 * Exception thrown when invalid data is provided
 */
public class InvalidDataException extends StableException {
    public InvalidDataException(String message) {
        super(message);
    }
}
