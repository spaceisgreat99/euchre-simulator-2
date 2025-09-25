
import java.util.List;

public class MultiGame {
    public static double simulateMultipleGames(List<Card> hand, int position, Card cardUp, Suit trump, int n, boolean alone) {
        // Position 0 is dealer
        double sum = 0;
        for(int i = 0; i < n; i++) {
            GameSetup setup = GameSetup.generateGameSetup(hand, position, cardUp, trump, alone);
            sum += Simulator.simulateGames(setup, 1);
        }
        return sum / n;
    }

    public static double[] tryAllTrump(List<Card> hand, int position, Card cardUp, int n) {
        double[] evals = new double[8];
        evals[0] = simulateMultipleGames(hand, position, cardUp, Suit.CLUBS, n, false);
        evals[1] = simulateMultipleGames(hand, position, cardUp, Suit.DIAMONDS, n, false);
        evals[2] = simulateMultipleGames(hand, position, cardUp, Suit.HEARTS, n, false);
        evals[3] = simulateMultipleGames(hand, position, cardUp, Suit.SPADES, n, false);
        evals[4] = simulateMultipleGames(hand, position, cardUp, Suit.CLUBS, n, true);
        evals[5] = simulateMultipleGames(hand, position, cardUp, Suit.DIAMONDS, n, true);
        evals[6] = simulateMultipleGames(hand, position, cardUp, Suit.HEARTS, n, true);
        evals[7] = simulateMultipleGames(hand, position, cardUp, Suit.SPADES, n, true);
        return evals;
    }

    public static String formatResult(double[] result) {
        String s = "Results:\n-> Clubs: " + String.format("%-8s", result [0]) + "      Alone: " + result[4];
        s += "\n-> Diamonds: " + String.format("%-8s", result [1]) + "   Alone: " + result[5];
        s += "\n-> Hearts: " + String.format("%-8s", result [2]) + "     Alone: " + result[6];
        s += "\n-> Spades: " + String.format("%-8s", result [3]) + "     Alone: " + result[7];
        s += "\n";
        return s;
    }
}