import java.util.*;

public class GameSetup {
    public boolean alone;
    public List<List<Card>> hands;
    public Card cardUp;
    public boolean pickedUp;
    public int caller;
    public Suit trump;

    public GameSetup(List<List<Card>> hands, Card cardUp, int caller, Suit trump, boolean alone) {
        this.hands = hands;
        this.cardUp = cardUp;
        this.caller = caller;
        this.trump = trump;
        this.alone = alone;
        this.pickedUp = cardUp.getSuit() == trump;
    }

    public GameSetup(List<List<Card>> hands, Card cardUp, int caller, Suit trump) {
        this.hands = hands;
        this.cardUp = cardUp;
        this.caller = caller;
        this.trump = trump;
        this.alone = false;
        this.pickedUp = cardUp.getSuit() == trump;
    }

    public static GameSetup generateGameSetup(List<Card> hand, Card cardUp, int caller, Suit trump) {
        Random rand = new Random();
        List<Card> deck = new ArrayList<>();
        for(Card[] cards: Card.deck) {
            for(Card card: cards) {
                if (!hand.contains(card) && !cardUp.equals(card)) {
                    deck.add(card);
                }
            }
        }
        List<List<Card>> hands = new ArrayList<>();
        hands.add(hand);
        for (int i = 0; i < 3; i++) {
            List<Card> newHand = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                newHand.add(deck.remove(rand.nextInt(deck.size())));
            }
            hands.add(newHand);
        }
        return new GameSetup(hands, cardUp, caller, trump);
    }
}
