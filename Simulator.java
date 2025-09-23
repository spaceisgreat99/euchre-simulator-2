import java.util.*;

public class Simulator {

    private final static boolean VERBOSE = false;

    // main public method which simulates multiple games and averages the number of points scored by the caller's team
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

    // simulates a single game, based on a GameSetup, and returns the score for the caller (negative indicates score for the non-callers)
    private static int simulateGame(GameSetup game) {
        int[] tricksWon = new int[4];
        int leader = 1; // Player 2, index 1
        List<Card> playedCards = new ArrayList<>();

        for (int trick = 0; trick < 5; trick++) {
            List<Card> trickCards = new ArrayList<>();

            Suit leadSuit = null;

            for (int i = 0; i < 4; i++) {
                int player = (leader + i) % 4;
                List<Card> hand = game.hands.get(player);

                Card chosen = selectCard(
                    hand,
                    game.trump,
                    leadSuit,
                    trickCards,
                    playedCards
                );
                if(VERBOSE) {
                    System.out.println("Card played: " + chosen.toString());
                }

                hand.remove(chosen);
                trickCards.add(chosen);
                playedCards.add(chosen);

                if (i == 0) {
                    leadSuit = chosen.effectiveSuit(game.trump);
                }
            }

            int winnerIndex = determineTrickWinner(trickCards, leadSuit, game.trump);
            int winningPlayer = (leader + winnerIndex) % 4;
            if (VERBOSE) {
                System.out.println("Winner: " + trickCards.get(winnerIndex) + "\n--------------");
            }

            tricksWon[winningPlayer]++;

            leader = winningPlayer;
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

    private static int determineTrickWinner(List<Card> trickCards, Suit leadSuit, Suit trump) {
        int winner = 0;
        for (int i = 1; i < 4; i++) {
            if (Card.beats(trickCards.get(i), trickCards.get(winner), leadSuit, trump)) {
                winner = i;
            }
        }
        return winner;
    }    

    // Handles the actual selecting of the best card to play
    private static Card selectCard(
        List<Card> hand,
        Suit trump,
        Suit leadSuit,
        List<Card> trickCards,
        List<Card> playedCards
    ) {
        if (hand.size() == 1) {
            return hand.get(0);
        }

        Random rand = new Random();

        int position = trickCards.size();

        // Leading
        if (position == 0) {
            // Non-trump Ace
            for (Card c : hand) {
                if (c.getValue() == Value.ACE && c.effectiveSuit(trump) != trump) {
                    return c;
                }
            }
            // Non-trump Singleton
            for (Card c : hand) {
                Suit suit = c.effectiveSuit(trump);
                if (suit != trump && countSuit(hand, suit, trump) == 1) {
                    return c;
                }
            }
            // If hand is entirely trump, lead the lowest one unless you have the highest trump
            long trumpCount = hand.stream().filter(c -> c.effectiveSuit(trump) == trump).count();
            if (trumpCount == hand.size()) {
                if (hasHighestUnplayedTrump(hand, playedCards, trump)) {
                    return highestTrump(hand, trump);
                }
                return lowestTrump(hand, trump);
            }
            // If none of these apply, lead a random card
            return hand.get(rand.nextInt(hand.size()));
            // Technically there is more logic that can be done here, such as leading the suit your partner sloughed, but this will usually be fine
        }

        // Second player
        if (position == 1) {
            // If we must follow suit, take the trick if possible
            if (hasSuit(hand, leadSuit, trump)) {
                Card highest = highestOfSuit(hand, leadSuit, trump);
                if (Card.beats(highest, trickCards.get(0), leadSuit, trump)) {
                    return highest;
                }
                return lowestOfSuit(hand, leadSuit, trump);
            } else if (hasSuit(hand, trump, trump)) {
                return lowestTrump(hand, trump); // Take with trump
            } else {
                // Attempt to short suit
                for (Card c : hand) {
                    Suit s = c.effectiveSuit(trump);
                    if (countSuit(hand, s, trump) == 1 && c.getValue() != Value.ACE) {
                        return c;
                    }
                }
                return worstCard(hand, leadSuit, trump);
            }
        }

        // Third player
        if (position == 2) {
            Card leader = trickCards.get(0);
            Card second = trickCards.get(1);
            Card currentBest = (Card.beats(second, leader, leadSuit, trump)) ? second : leader;

            if (hasSuit(hand, leadSuit, trump)) {
                Card highest = highestOfSuit(hand, leadSuit, trump);
                // Special case: if our partner led the king, do not ace over it unless we have to
                if (leader.getValue() == Value.KING && leadSuit != trump) {
                    Card ace = hand.stream()
                                .filter(c -> c.effectiveSuit(trump) == leadSuit && c.getValue() == Value.ACE)
                                .findFirst()
                                .orElse(null);
                    if (ace != null) {
                        return lowestOfSuit(hand, leadSuit, trump);
                    }
                }
                // Otherwise try to take the trick
                if (Card.beats(highest, currentBest, leadSuit, trump)) {
                    return highest;
                }
                return lowestOfSuit(hand, leadSuit, trump);
            } else if (hasSuit(hand, trump, trump)) {
                // In the first two hands, do not trump our partner's ace
                if (leader.getValue() == Value.ACE && leader.effectiveSuit(trump) != trump
                    && second.effectiveSuit(trump) != trump
                    && (playedCards.size() / 4) < 2) {
                    return worstCardOrLowestTrump(hand, leadSuit, trump);
                }
                // Trump the trick if we can take it
                Card highestTrump = highestTrump(hand, trump);
                if (Card.beats(highestTrump, currentBest, leadSuit, trump)) {
                    return highestTrump;
                }
                return worstCardOrLowestTrump(hand, leadSuit, trump);
            } else {
                // Attempt to short suit
                for (Card c : hand) {
                    Suit s = c.effectiveSuit(trump);
                    if (s != trump && countSuit(hand, s, trump) == 1 && c.getValue() != Value.ACE) {
                        return c;
                    }
                }
                return worstCard(hand, leadSuit, trump);
            }
        }

        // Fourth player
        if (position == 3) {
            Card leader = trickCards.get(0);
            Card second = trickCards.get(1);
            Card third = trickCards.get(2);
            Card currentBest = leader;
            if (Card.beats(second, currentBest, leadSuit, trump)) currentBest = second;
            if (Card.beats(third, currentBest, leadSuit, trump)) currentBest = third;

            if (hasSuit(hand, leadSuit, trump)) {
                // If our partner has the trick, play low
                if (currentBest == second) {
                    return lowestOfSuit(hand, leadSuit, trump);
                } else {
                    // Try to take the trick with the lowest winning card
                    Card lowestWinner = lowestWinningCard(hand, leadSuit, trump, currentBest);
                    if (lowestWinner != null){
                        return lowestWinner;
                    }
                    return lowestOfSuit(hand, leadSuit, trump);
                }
            } else if (hasSuit(hand, trump, trump)) {
                // Try to take the trick with trump
                Card lowestWinningTrump = lowestWinningTrump(hand, trump, currentBest, leadSuit);
                if (lowestWinningTrump != null) {
                    return lowestWinningTrump;
                }
                return worstCardOrLowestTrump(hand, leadSuit, trump);
            } else {
                // Attempt to short suit
                for (Card c : hand) {
                    Suit s = c.effectiveSuit(trump);
                    if (s != trump && countSuit(hand, s, trump) == 1 && c.getValue() != Value.ACE) {
                        return c;
                    }
                }
                return worstCard(hand, leadSuit, trump);
            }
        }

        // Fallback - should never be needed
        return hand.get(0);
    }


    private static boolean hasSuit(List<Card> hand, Suit suit, Suit trump) {
        return hand.stream().anyMatch(c -> c.effectiveSuit(trump) == suit);
    }

    private static Card highestOfSuit(List<Card> hand, Suit suit, Suit trump) {
        if (suit == trump) {
            return highestTrump(hand, trump);
        }
        return hand.stream()
                .filter(c -> c.effectiveSuit(trump) == suit)
                .max((a, b) -> a.normalStrength() - b.normalStrength())
                .orElse(null);
    }

    private static Card lowestOfSuit(List<Card> hand, Suit suit, Suit trump) {
        if (suit == trump) {
            return lowestTrump(hand, trump);
        }
        return hand.stream()
                .filter(c -> c.effectiveSuit(trump) == suit)
                .min((a, b) -> a.normalStrength() - b.normalStrength())
                .orElse(null);
    }

    private static Card lowestTrump(List<Card> hand, Suit trump) {
        return hand.stream()
                .filter(c -> c.effectiveSuit(trump) == trump)
                .min((a, b) -> a.trumpStrength(trump) - b.trumpStrength(trump))
                .orElse(null);
    }

    private static Card highestTrump(List<Card> hand, Suit trump) {
        return hand.stream()
                .filter(c -> c.effectiveSuit(trump) == trump)
                .max((a, b) -> a.trumpStrength(trump) - b.trumpStrength(trump))
                .orElse(null);
    }

    private static int countSuit(List<Card> hand, Suit suit, Suit trump) {
        return (int) hand.stream().filter(c -> c.effectiveSuit(trump) == suit).count();
    }

    // Checks if this hand contains the highest unplayed trump
    private static boolean hasHighestUnplayedTrump(List<Card> hand, List<Card> playedCards, Suit trump) {
        List<Card> trumpsInHand = new ArrayList<>();
        for (Card c : hand) {
            if (c.effectiveSuit(trump) == trump) {
                trumpsInHand.add(c);
            }
        }
        if (trumpsInHand.isEmpty()) return false;

        Card strongest = trumpsInHand.stream().max((a, b) -> a.trumpStrength(trump) - b.trumpStrength(trump)).get();
        int strength = strongest.trumpStrength(trump);

        // Check all higher trump cards to see if they are in playedCards
        for (int s = strength + 1; s <= 200; s++) {
            int check = s;
            boolean seen = playedCards.stream().anyMatch(c -> c.trumpStrength(trump) == check);
            if (!seen) return false;
        }
        return true;
    }

    private static Card lowestWinningCard(List<Card> hand, Suit leadSuit, Suit trump, Card currentBest) {
        return hand.stream()
                .filter(c -> c.effectiveSuit(trump) == leadSuit && Card.beats(c, currentBest, leadSuit, trump))
                .min((a, b) -> a.normalStrength() - b.normalStrength())
                .orElse(null);
    }

    private static Card lowestWinningTrump(List<Card> hand, Suit trump, Card currentBest, Suit leadSuit) {
        return hand.stream()
                .filter(c -> c.effectiveSuit(trump) == trump && Card.beats(c, currentBest, leadSuit, trump))
                .min((a, b) -> a.trumpStrength(trump) - b.trumpStrength(trump))
                .orElse(null);
    }

    // Worst card in a given trick, for sloughing purposes
    private static Card worstCard(List<Card> hand, Suit leadSuit, Suit trump) {
        return hand.stream()
            .filter(c -> c.effectiveSuit(trump) != trump && c.effectiveSuit(trump) != leadSuit)
            .min((a, b) -> a.normalStrength() - b.normalStrength())
            .orElse(null);
    }

    private static Card worstCardOrLowestTrump(List<Card> hand, Suit leadSuit, Suit trump) {
        Card wc = worstCard(hand, leadSuit, trump);
        if (wc == null) {
            wc = lowestTrump(hand, trump);
        }
        return wc;
    }

    public static List<Card> replaceCard(Card card, List<Card> hand, Suit trump) {
        // Attempt to short suit
        for (Card c : hand) {
            Suit s = c.effectiveSuit(trump);
            if (s != trump && countSuit(hand, s, trump) == 1 && c.getValue() != Value.ACE) {
                hand.remove(c);
                hand.add(card);
                return hand;
            }
        }
        // Otherwise drop worst card
        hand.remove(worstCardOrLowestTrump(hand, trump, trump));
        hand.add(card);
        return hand;
    }

}
