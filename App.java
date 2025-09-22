import java.util.ArrayList;
import java.util.List;



public class App {
    public static void main(String[] args) {
        // Get player input somehow
        // TODO: implement this

        // Define cards and game setup
        // TODO: make this responsive to player input
        List<Card> testHand1 = new ArrayList<>();
        testHand1.add(deck[0][3]);
        testHand1.add(deck[1][1]);
        testHand1.add(deck[3][3]);
        testHand1.add(deck[2][2]);
        testHand1.add(deck[2][0]);
        List<Card> testHand2 = new ArrayList<>();
        testHand2.add(deck[2][1]);
        testHand2.add(deck[3][4]);
        testHand2.add(deck[3][5]);
        testHand2.add(deck[3][0]);
        testHand2.add(deck[1][3]);
        List<Card> testHand3 = new ArrayList<>();
        testHand3.add(deck[3][1]);
        testHand3.add(deck[0][5]);
        testHand3.add(deck[0][2]);
        testHand3.add(deck[2][4]);
        testHand3.add(deck[1][0]);
        List<Card> testHand4 = new ArrayList<>();
        testHand4.add(deck[0][4]);
        testHand4.add(deck[1][2]);
        testHand4.add(deck[2][3]);
        testHand4.add(deck[2][5]);
        testHand4.add(deck[3][2]);

        List<List<Card>> hands = new ArrayList<>();
        hands.add(testHand1);
        hands.add(testHand2);
        hands.add(testHand3);
        hands.add(testHand4);

        Card cardUp = deck[0][0];
        int caller = 0;
        Suit trump = Suit.DIAMONDS;
        GameSetup setup = new GameSetup(hands, cardUp, caller, trump);

        // Run simulations
        double result = Simulator.simulateGames(setup, 10);
        // Report results
        System.out.println("Result: " + result);
    }

    private static final Card[][] deck = {
        {new Card(Suit.CLUBS, Value.ACE), new Card(Suit.CLUBS, Value.KING), new Card(Suit.CLUBS, Value.QUEEN), new Card(Suit.CLUBS, Value.JACK), new Card(Suit.CLUBS, Value.TEN), new Card(Suit.CLUBS, Value.NINE)},
        {new Card(Suit.DIAMONDS, Value.ACE), new Card(Suit.DIAMONDS, Value.KING), new Card(Suit.DIAMONDS, Value.QUEEN), new Card(Suit.DIAMONDS, Value.JACK), new Card(Suit.DIAMONDS, Value.TEN), new Card(Suit.DIAMONDS, Value.NINE)},
        {new Card(Suit.HEARTS, Value.ACE), new Card(Suit.HEARTS, Value.KING), new Card(Suit.HEARTS, Value.QUEEN), new Card(Suit.HEARTS, Value.JACK), new Card(Suit.HEARTS, Value.TEN), new Card(Suit.HEARTS, Value.NINE)},
        {new Card(Suit.SPADES, Value.ACE), new Card(Suit.SPADES, Value.KING), new Card(Suit.SPADES, Value.QUEEN), new Card(Suit.SPADES, Value.JACK), new Card(Suit.SPADES, Value.TEN), new Card(Suit.SPADES, Value.NINE)}
    };
}
