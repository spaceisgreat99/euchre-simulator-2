
import java.util.List;

public class MultiGame {
    public static double simulateMultipleGames(List<Card> hand, int position, Card cardUp, Suit trump, int n) {
        // Position 0 is dealer
        double sum = 0;
        for(int i = 0; i < n; i++) {
            GameSetup setup = GameSetup.generateGameSetup(hand, position, cardUp, trump);
            sum += Simulator.simulateGames(setup, 1);
        }
        return sum / n;
    }

    public static double[] tryAllTrump(List<Card> hand, int position, Card cardUp, int n) {
        double[] evals = new double[4];
        evals[0] = simulateMultipleGames(hand, position, cardUp, Suit.CLUBS, n);
        evals[1] = simulateMultipleGames(hand, position, cardUp, Suit.DIAMONDS, n);
        evals[2] = simulateMultipleGames(hand, position, cardUp, Suit.HEARTS, n);
        evals[3] = simulateMultipleGames(hand, position, cardUp, Suit.SPADES, n);
        return evals;
    }

    public static String formatResult(double[] result) {
        String s = "Results:\n-> Clubs: " + result[0];
        s += "\n-> Diamonds: " + result[1];
        s += "\n-> Hearts: " + result[2];
        s += "\n-> Spades: " + result[3] + "\n";
        return s;
    }
}