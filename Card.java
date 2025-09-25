
import java.util.ArrayList;
import java.util.List;

public class Card {
    private final Suit suit;
    private final Value value;

    public Card(Suit suit, Value value) {
        this.suit = suit;
        this.value = value;
    }

    public Suit getSuit() {
        return suit;
    }

    public Value getValue() {
        return value;
    }

    public boolean equals(Card other) {
        return (getSuit() == other.getSuit() && getValue() == other.getValue());
    }

    public static final Card[][] deck = {
        {new Card(Suit.CLUBS, Value.ACE), new Card(Suit.CLUBS, Value.KING), new Card(Suit.CLUBS, Value.QUEEN), new Card(Suit.CLUBS, Value.JACK), new Card(Suit.CLUBS, Value.TEN), new Card(Suit.CLUBS, Value.NINE)},
        {new Card(Suit.DIAMONDS, Value.ACE), new Card(Suit.DIAMONDS, Value.KING), new Card(Suit.DIAMONDS, Value.QUEEN), new Card(Suit.DIAMONDS, Value.JACK), new Card(Suit.DIAMONDS, Value.TEN), new Card(Suit.DIAMONDS, Value.NINE)},
        {new Card(Suit.HEARTS, Value.ACE), new Card(Suit.HEARTS, Value.KING), new Card(Suit.HEARTS, Value.QUEEN), new Card(Suit.HEARTS, Value.JACK), new Card(Suit.HEARTS, Value.TEN), new Card(Suit.HEARTS, Value.NINE)},
        {new Card(Suit.SPADES, Value.ACE), new Card(Suit.SPADES, Value.KING), new Card(Suit.SPADES, Value.QUEEN), new Card(Suit.SPADES, Value.JACK), new Card(Suit.SPADES, Value.TEN), new Card(Suit.SPADES, Value.NINE)}
    };

    // Returns true iff A beats B
    public static boolean beats(Card A, Card B, Suit leadSuit, Suit trump) {        
        Suit suitA = A.effectiveSuit(trump);
        Suit suitB = B.effectiveSuit(trump);

        // Both trump
        if (suitA == trump && suitB == trump) {
            return A.trumpStrength(trump) > B.trumpStrength(trump);
        }
        // Trump vs non-trump
        if (suitA == trump && suitB != trump) return true;
        if (suitA != trump && suitB == trump) return false;

        // Both lead suit
        if (suitA == leadSuit && suitB == leadSuit) {
            return A.normalStrength() > B.normalStrength();
        }
        // Lead suit vs non-lead
        if (suitA == leadSuit && suitB != leadSuit) return true;
        if (suitA != leadSuit && suitB == leadSuit) return false;

        return false;
    }

    public int trumpStrength(Suit trump) {
        if (getValue() == Value.JACK && getSuit() == trump) return 200; // right bower
        if (getValue() == Value.JACK && getSuit() == sameColor(trump)) return 199; // left bower
        switch (getValue()) {
            case ACE: return 198;
            case KING: return 197;
            case QUEEN: return 196;
            case TEN: return 195;
            case NINE: return 194;
        }
        return 0;
    }

    public int normalStrength() {
        switch (getValue()) {
            case ACE: return 100;
            case KING: return 99;
            case QUEEN: return 98;
            case JACK: return 97;
            case TEN: return 96;
            case NINE: return 95;
        }
        return 0;
    }

    // Next two methods help handle left bower
    public Suit effectiveSuit(Suit trump) {
        if (trump != null && getValue() == Value.JACK) {
            if (getSuit() == trump) {
                return trump;
            }
            if (getSuit() == sameColor(trump)) {
                return trump;
            }
        }
        return getSuit();
    }
    private static Suit sameColor(Suit trump) {
        switch (trump) {
            case HEARTS: return Suit.DIAMONDS;
            case DIAMONDS: return Suit.HEARTS;
            case CLUBS: return Suit.SPADES;
            case SPADES: return Suit.CLUBS;
        }
        throw new IllegalArgumentException("Invalid suit");
    }

    public static List<Card> parseHand(String c1, String c2, String c3, String c4, String c5) {
        List<Card> result = new ArrayList<>();
        result.add(parseCard(c1));
        result.add(parseCard(c2));
        result.add(parseCard(c3));
        result.add(parseCard(c4));
        result.add(parseCard(c5));
        return result;
    }

    public static Card parseCard(String c) {
        Suit suit = Suit.SPADES;
        Value value = Value.NINE;
        switch (c.charAt(0)) {
            case '9': value = Value.NINE; break;
            case '1': value = Value.TEN; break;
            case 'j': value = Value.JACK; break;
            case 'q': value = Value.QUEEN; break;
            case 'k': value = Value.KING; break;
            case 'a': value = Value.ACE; break;
        }
        switch (c.charAt(c.length() - 1)) {
            case 'c': suit = Suit.CLUBS; break;
            case 'd': suit = Suit.DIAMONDS; break;
            case 'h': suit = Suit.HEARTS; break;
            case 's': suit = Suit.SPADES; break;
        }
        return new Card(suit, value);
    }

    @Override
    public String toString() {
        return getValue() + " of " + getSuit();
    }
}