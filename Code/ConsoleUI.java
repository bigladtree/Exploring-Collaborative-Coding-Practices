import java.io.PrintStream;
import java.util.Scanner;

/** Text interface that drives a GuessingGame. */
public class ConsoleUI {
    private final GuessingGame game;
    private final Scanner in;
    private final PrintStream out;

    public ConsoleUI(GuessingGame game, Scanner in, PrintStream out) {
        this.game = game;
        this.in = in;
        this.out = out;
    }

    /** Converts text to an Integer, or returns null if it is not a whole number. */
    public static Integer parseInput(String text) {
        if (text == null) {
            return null;
        }
        try {
            return Integer.valueOf(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public void run() {
        out.println("I'm thinking of an ODD number between "
                + GuessingGame.MIN_VALUE + " and " + GuessingGame.MAX_VALUE + ".");
        while (!game.isWon() && in.hasNextLine()) {
            out.print("Your guess: ");
            Integer value = parseInput(in.nextLine());
            if (value == null) {
                out.println("Please enter a whole number.");
                continue;
            }
            try {
                GuessResult result = game.guess(value);
                if (result == GuessResult.CORRECT) {
                    out.println("Correct! You got it in " + game.getAttempts() + " attempts.");
                } else {
                    out.println(result == GuessResult.TOO_LOW ? "Too low, try again." : "Too high, try again.");
                }
            } catch (InvalidGuessException e) {
                out.println(e.getMessage());
            }
        }
    }
}
