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

    public static GameSetup generateGameSetup(List<Card> hand, int position, Card cardUp, Suit trump, boolean alone) {
        // Position 0 is dealer
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
        for (int i = 0; i < 3; i++) {
            List<Card> newHand = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                newHand.add(deck.remove(rand.nextInt(deck.size())));
            }
            hands.add(newHand);
        }
        hands.add(position % 4, hand);

        // Handle possibility of cardUp being picked up
        if (cardUp.getSuit() == trump) {
            hands.set(0, Simulator.replaceCard(cardUp, hands.get(0), trump));
        }

        // If a player is going alone, convert all of their partner's cards to non trump 9s so they can't take any tricks
        if (alone) {
            List<Card> dummyHand = new ArrayList<>();
            if (trump == Suit.CLUBS) {
                for (int i = 0; i < 5; i++) {
                    dummyHand.add(new Card(Suit.DIAMONDS, Value.NINE));
                }
            }
            else {
                for (int i = 0; i < 5; i++) {
                    dummyHand.add(new Card(Suit.CLUBS, Value.NINE));
                }
            }
            hands.set((position + 2) % 4, dummyHand);
        }

        return new GameSetup(hands, cardUp, position, trump, alone);
    }
}
