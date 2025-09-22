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
        if (getValue() == Value.JACK) {
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

    @Override
    public String toString() {
        return getValue() + " of " + getSuit();
    }
}