# Euchre Simulator 0.2

Currently there is no UI, but the variables to change are clearly marked in EuchreSimulator.java.

To use, edit EuchreSimulator.java with your desired situation (entering the cards in your hand, the card up in the middle, and what position you are relative to the dealer -- dealer is 0, 1-3 are in the order of play).

Then compile with `javac EuchreSimulator.java` and run with `java EuchreSimulator` (or use your favorite IDE or other method of running Java files), and the output will show the expected number of points your team will get for each possible trump.

Note that for the case where trump is the same as the card up in the middle, it will assume the dealer picks up this card.

## Additional Info:

Gameplay follows a rule-based implementation which I made based on rules I use while playing. I believe it almost always plays the correct card, but it certainly is not a perfect simulator. One way the program mitigates this is by simulating with different random hands each time.

## Future Plans:

- Add calculations for going alone
- Add a 'no trump' option
- Improve rule-based gameplay implementation
