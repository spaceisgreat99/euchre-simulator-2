import java.util.ArrayList;
import java.util.List;



public class App {
    public static void main(String[] args) {
        // Get player input somehow
        // TODO: implement this

        // Define cards and game setup
        // TODO: make this responsive to player input
        List<Card> hand = new ArrayList<>();
        hand.add(Card.deck[0][3]);
        hand.add(Card.deck[1][1]);
        hand.add(Card.deck[3][3]);
        hand.add(Card.deck[2][2]);
        hand.add(Card.deck[2][0]);

        Card cardUp = Card.deck[0][0];
        int caller = 0;
        Suit trump = Suit.SPADES;
        GameSetup setup = GameSetup.generateGameSetup(hand, cardUp, caller, trump);

        // Run simulations
        double result = Simulator.simulateGames(setup, 1);
        // Report results
        System.out.println("Result: " + result);
    }
}
