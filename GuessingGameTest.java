import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Random;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Unit tests written test-first (TDD). */
class GuessingGameTest {
    private GuessingGame game;

    @BeforeEach
    void setUp() {
        game = new GuessingGame(501);
    }

    // ---- isValidGuess ----
    @Test
    void acceptsOddBoundaries() {
        assertTrue(GuessingGame.isValidGuess(1));
        assertTrue(GuessingGame.isValidGuess(999));
    }

    @Test
    void rejectsEvenNumbers() {
        assertFalse(GuessingGame.isValidGuess(2));
        assertFalse(GuessingGame.isValidGuess(1000));
    }

    @Test
    void rejectsOutOfRange() {
        assertFalse(GuessingGame.isValidGuess(0));
        assertFalse(GuessingGame.isValidGuess(-1));
        assertFalse(GuessingGame.isValidGuess(1001));
        assertFalse(GuessingGame.isValidGuess(1003));
    }

    // ---- generateSecret ----
    @Test
    void generatedSecretIsAlwaysOddAndInRange() {
        Random rng = new Random();
        for (int i = 0; i < 5000; i++) {
            assertTrue(GuessingGame.isValidGuess(GuessingGame.generateSecret(rng)));
        }
    }

    @Test
    void generateSecretCanReachBothEnds() {
        assertEquals(1, GuessingGame.generateSecret(fixedRandom(0)));
        assertEquals(999, GuessingGame.generateSecret(fixedRandom(499)));
    }

    // ---- GuessingGame ----
    @Test
    void newGameStartsReadyWithZeroAttempts() {
        assertEquals(GameState.READY, game.getState());
        assertEquals(0, game.getAttempts());
    }

    @Test
    void randomSecretConstructorWorks() {
        assertEquals(GameState.READY, new GuessingGame().getState());
    }

    @Test
    void rejectsInvalidSecret() {
        assertThrows(InvalidGuessException.class, () -> new GuessingGame(500));
    }

    @Test
    void tooLow() {
        assertEquals(GuessResult.TOO_LOW, game.guess(101));
        assertEquals(GameState.AWAITING_GUESS, game.getState());
    }

    @Test
    void tooHigh() {
        assertEquals(GuessResult.TOO_HIGH, game.guess(999));
    }

    @Test
    void correctGuessWins() {
        assertEquals(GuessResult.CORRECT, game.guess(501));
        assertTrue(game.isWon());
        assertEquals(GameState.WON, game.getState());
    }

    @Test
    void attemptsAreCounted() {
        game.guess(1);
        game.guess(999);
        game.guess(501);
        assertEquals(3, game.getAttempts());
    }

    @Test
    void evenGuessThrowsAndIsNotCounted() {
        assertThrows(InvalidGuessException.class, () -> game.guess(500));
        assertEquals(0, game.getAttempts());
    }

    @Test
    void outOfRangeGuessThrows() {
        assertThrows(InvalidGuessException.class, () -> game.guess(1001));
    }

    @Test
    void guessAfterWinThrows() {
        game.guess(501);
        assertThrows(IllegalStateException.class, () -> game.guess(3));
    }

    // ---- ConsoleUI ----
    @Test
    void parseInput() {
        assertEquals(7, ConsoleUI.parseInput(" 7 "));
        assertNull(ConsoleUI.parseInput("abc"));
        assertNull(ConsoleUI.parseInput(""));
        assertNull(ConsoleUI.parseInput(null));
    }

    @Test
    void fullGameFlow() {
        GuessingGame g = new GuessingGame(51);
        String output = runUi(g, "11\n99\n51\n");
        assertTrue(g.isWon());
        assertEquals(3, g.getAttempts());
        assertTrue(output.contains("Correct"));
    }

    @Test
    void badInputDoesNotCountAsAttempt() {
        GuessingGame g = new GuessingGame(51);
        String output = runUi(g, "hello\n50\n51\n");
        assertEquals(1, g.getAttempts());
        assertTrue(output.contains("whole number"));
        assertTrue(output.contains("odd integer"));
    }

    // ---- helpers ----
    private static String runUi(GuessingGame g, String input) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        new ConsoleUI(g, new Scanner(input), new PrintStream(buffer)).run();
        return buffer.toString();
    }

    private static Random fixedRandom(int value) {
        return new Random() {
            @Override
            public int nextInt(int bound) {
                return value;
            }
        };
    }
}
