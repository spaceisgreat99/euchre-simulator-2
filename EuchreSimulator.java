import java.util.ArrayList;
import java.util.List;



public class EuchreSimulator {
    public static void main(String[] args) {
        // DEFINE CARDS AND GAME SETUP HERE
        List<Card> hand = new ArrayList<>();
        hand.addAll(Card.parseHand("9s", "10h", "ac", "kc", "qd"));

        Card cardUp = Card.parseCard("10d");
        int position = 0; // Position 0 is dealer
        // DEFINE CARDS AND GAME SETUP ABOVE

        // Run simulations
        double[] result = MultiGame.tryAllTrump(hand, position, cardUp, 1000);
        // Report results
        System.out.println(MultiGame.formatResult(result));
    }
}
