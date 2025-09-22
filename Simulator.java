import java.util.ArrayList;
import java.util.List;

public class Simulator {

    public static double simulateGames(GameSetup game, int n) {
        double sum = 0;
        for (int i = 0; i < n; i++) {
            List<List<Card>> handsDup = new ArrayList<>();
            for (List<Card> hand : game.hands) {
                handsDup.add(new ArrayList<>(hand));
            }

            GameSetup gameDup = new GameSetup(handsDup, game.cardUp, game.caller, game.trump, game.alone);

            sum += simulateGame(gameDup);
        }
        return sum / n;
    }

    public static int simulateGame(GameSetup game) {
        int[] tricksWon = new int[4];
        int leader = 1; // Player 2, index 1
        List<Card> playedCards = new ArrayList<>();

        for (int trick = 0; trick < 5; trick++) {
            List<Card> trickCards = new ArrayList<>();
            int[] trickPlayers = new int[4];

            Suit leadSuit = null;

            for (int i = 0; i < 4; i++) {
                int player = (leader + i) % 4;
                List<Card> hand = game.hands.get(player);

                Card chosen = selectCard(
                    hand,
                    game.trump,
                    leadSuit,
                    trickCards,
                    player,
                    playedCards,
                    game
                );

                hand.remove(chosen);
                trickCards.add(chosen);
                playedCards.add(chosen);
                trickPlayers[i] = player;

                if (i == 0) {
                    leadSuit = effectiveSuit(chosen, game.trump);
                }
            }

            int winnerIndex = determineTrickWinner(trickCards, trickPlayers, leadSuit, game.trump);

            tricksWon[winnerIndex]++;

            leader = winnerIndex;
        }

        // Combine caller and partner's tricks and return result
        int partner = (game.caller + 2) % 4;
        int tricksTaken = tricksWon[game.caller] + tricksWon[partner];
        if (tricksTaken == 5) {
            return 2;
        } else if (tricksTaken == 0) {
            return -4;
        } else if (tricksTaken > 2) {
            return 1;
        } else {
            return -2;
        }
    }

    // To handle left bower
    private static Suit effectiveSuit(Card c, Suit trump) {
        if (c.getValue() == Value.JACK) {
            if (c.getSuit() == trump) {
                return trump;
            }
            if (c.getSuit() == sameColor(trump)) {
                return trump;
            }
        }
        return c.getSuit();
    }

    // To handle left bower
    private static Suit sameColor(Suit trump) {
        switch (trump) {
            case HEARTS: return Suit.DIAMONDS;
            case DIAMONDS: return Suit.HEARTS;
            case CLUBS: return Suit.SPADES;
            case SPADES: return Suit.CLUBS;
        }
        throw new IllegalArgumentException("Invalid suit");
    }

    private static int determineTrickWinner(List<Card> trickCards, int[] trickPlayers, Suit leadSuit, Suit trump) {
        int winner = 0;
        for (int i = 1; i < 4; i++) {
            if (beats(trickCards.get(i), trickCards.get(winner), leadSuit, trump)) {
                winner = i;
            }
        }
        return trickPlayers[winner];
    }

    // Returns true if A beats B, false otherwise
    private static boolean beats(Card A, Card B, Suit leadSuit, Suit trump) {
        Suit suitA = effectiveSuit(A, trump);
        Suit suitB = effectiveSuit(B, trump);

        // Both trump
        if (suitA == trump && suitB == trump) {
            return trumpStrength(A, trump) > trumpStrength(B, trump);
        }
        // Trump vs non-trump
        if (suitA == trump && suitB != trump) return true;
        if (suitA != trump && suitB == trump) return false;

        // Both lead suit
        if (suitA == leadSuit && suitB == leadSuit) {
            return normalStrength(A) > normalStrength(B);
        }
        // Lead suit vs non-lead
        if (suitA == leadSuit && suitB != leadSuit) return true;
        if (suitA != leadSuit && suitB == leadSuit) return false;

        return false;
    }

    private static int trumpStrength(Card c, Suit trump) {
        if (c.getValue() == Value.JACK && c.getSuit() == trump) return 200; // right bower
        if (c.getValue() == Value.JACK && c.getSuit() == sameColor(trump)) return 199; // left bower
        switch (c.getValue()) {
            case ACE: return 198;
            case KING: return 197;
            case QUEEN: return 196;
            case TEN: return 195;
            case NINE: return 194;
        }
        return 0;
    }

    private static int normalStrength(Card c) {
        switch (c.getValue()) {
            case ACE: return 100;
            case KING: return 99;
            case QUEEN: return 98;
            case JACK: return 97;
            case TEN: return 96;
            case NINE: return 95;
        }
        return 0;
    }

    // Handles the actual selecting of the best card to play
    private static Card selectCard(List<Card> hand, Suit trump, Suit leadSuit, List<Card> played, int player, List<Card> playedCards, GameSetup game) {
        // TODO: use actual logic
        return hand.get(0);
    }
}
