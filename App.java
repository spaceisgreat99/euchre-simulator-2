import java.util.ArrayList;
import java.util.List;



public class App {
    public static void main(String[] args) {
        // Get player input somehow
        // TODO: implement this

        // Define cards and game setup
        // TODO: make this responsive to player input
        List<Card> hand = new ArrayList<>();
        hand.add(Card.deck[1][1]);
        hand.add(Card.deck[1][3]);
        hand.add(Card.deck[3][5]);
        hand.add(Card.deck[3][4]);
        hand.add(Card.deck[3][1]);

        Card cardUp = Card.deck[2][0];
        int position = 0; // Position 0 is dealer

        // Run simulations
        double[] result = MultiGame.tryAllTrump(hand, position, cardUp, 1000);
        // Report results
        System.out.println(MultiGame.formatResult(result));
    }
}
