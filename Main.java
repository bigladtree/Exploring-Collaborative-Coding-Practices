import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new ConsoleUI(new GuessingGame(), new Scanner(System.in), System.out).run();
    }
}
