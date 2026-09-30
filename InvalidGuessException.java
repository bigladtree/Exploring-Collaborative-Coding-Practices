/** Thrown when a guess (or secret) is not an odd integer from 1 to 1000. */
public class InvalidGuessException extends IllegalArgumentException {
    public InvalidGuessException(String message) {
        super(message);
    }
}
