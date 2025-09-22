import java.util.List;

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
}
