import java.util.Random;

/** Core game logic. Contains no printing or reading of input. */
public class GuessingGame {
    public static final int MIN_VALUE = 1;
    public static final int MAX_VALUE = 1000;

    private final int secret;
    private int attempts;
    private GameState state;

    /** Starts a game with a random odd secret. */
    public GuessingGame() {
        this(generateSecret(new Random()));
    }

    /** Starts a game with a chosen secret (used by tests). */
    public GuessingGame(int secret) {
        if (!isValidGuess(secret)) {
            throw new InvalidGuessException("Secret must be an odd integer from 1 to 1000.");
        }
        this.secret = secret;
        this.attempts = 0;
        this.state = GameState.READY;
    }

    /** True if value is odd and within [MIN_VALUE, MAX_VALUE]. */
    public static boolean isValidGuess(int value) {
        return value >= MIN_VALUE && value <= MAX_VALUE && value % 2 != 0;
    }

    /** Picks a random odd integer from 1, 3, ..., 999. */
    public static int generateSecret(Random rng) {
        int oddCount = (MAX_VALUE - MIN_VALUE + 1) / 2;   // 500 odd numbers
        return MIN_VALUE + 2 * rng.nextInt(oddCount);
    }

    /**
     * Evaluates a guess. Invalid guesses throw InvalidGuessException and are
     * not counted as attempts.
     */
    public GuessResult guess(int value) {
        if (state == GameState.WON) {
            throw new IllegalStateException("Game is over. Start a new game.");
        }
        if (!isValidGuess(value)) {
            throw new InvalidGuessException(
                "Guess must be an odd integer from " + MIN_VALUE + " to " + MAX_VALUE + ".");
        }
        state = GameState.AWAITING_GUESS;
        attempts++;
        if (value < secret) {
            return GuessResult.TOO_LOW;
        }
        if (value > secret) {
            return GuessResult.TOO_HIGH;
        }
        state = GameState.WON;
        return GuessResult.CORRECT;
    }

    public boolean isWon() {
        return state == GameState.WON;
    }

    public GameState getState() {
        return state;
    }

    public int getAttempts() {
        return attempts;
    }
}
