import java.io.Serializable;

/**
 * Represents a playing card in the Uno game.
 */

public class Card implements Serializable {


    private static Rank color;

    public boolean isSpecial() {
        return true;
    }

    /**
     * Enumeration of card ranks.
     */
    public enum Rank {DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, REVERSE, WILD, FLIP, DRAW2, DRAW4, DRAW5, DRAW_COLOUR, SKIP_EVERYONE}

    /**
     * Enumeration of card suits.
     */
    public enum Suit {RED,YELLOW,BLUE,GREEN,PINK,TEAL,PURPLE,ORANGE}


    /**
     * Gets the Rank enum value corresponding to the given abbreviation character.
     *
     * @param c The abbreviation character.
     * @return The Rank enum value.
     * @throws IllegalArgumentException If the abbreviation is invalid.
     */
    public static Rank getRankFromAbbrev(char c) {
        //using char instead of String to avoid using switch with strings (unavailable on early java)

        // one could use a lookup map inside the enum instead.
        // opting for fewer advanced java concepts instead
        if (Character.isDigit(c)) {
            int irank = Character.digit(c, 10); //convert to int
            if (irank == 0) return Rank.SKIP; //or raise error?
            if (irank == 1) return Rank.REVERSE;
            return Rank.values()[irank-2];
        }

        switch (c) {
            case 'T': return Rank.SKIP;
            case 'J': return Rank.REVERSE;
            case 'Q': return Rank.WILD;
            case 'K': return Rank.DRAW2;
            case 'A': return Rank.DRAW4;
            default: throw new IllegalArgumentException("No such rank!");
        }
    }

    /**
     * Gets the Suit enum value corresponding to the given abbreviation character.
     *
     * @param c The abbreviation character.
     * @return The Suit enum value.
     * @throws IllegalArgumentException If the abbreviation is invalid.
     */
    public static Suit getSuitFromAbbrev(char c) {
        switch (c) {
            case 'C': return Suit.RED;
            case 'D': return Suit.YELLOW;
            case 'H': return Suit.BLUE;
            case 'S': return Suit.GREEN;
            default: throw new IllegalArgumentException("No such suit!");
        }
    }
    private Rank rank;
    private Suit suit;

    private static boolean isFlipped = false;

    public static void flipDeck() {
        isFlipped = !isFlipped;
    }

    public static boolean isFlipped() {
        return isFlipped;
    }


    /**
     * Constructs a Card instance based on the provided abbreviation string.
     *
     * @param s The abbreviation string.
     */
    public Card(String s) {
        this(getRankFromAbbrev(s.charAt(0)), getSuitFromAbbrev(s.charAt(1)));
    }


    private Card.Suit currentColor;

    private Card.Rank currentNumber;
    /**
     * Constructs a Card instance with the specified rank and suit.
     *
     * @param r The rank of the card.
     * @param s The suit of the card.
     */
    public Card(Rank r, Suit s) {
        rank = r;
        suit = s;
    }


    /**
     * Gets the rank of the card.
     *
     * @return The rank enum value.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Gets the suit of the card.
     *
     * @return The suit enum value.
     */
    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit newSuit) {
        this.suit = newSuit;
    }

    public void setRank(Rank newRank) {
        this.rank = newRank;
    }

    public boolean matchesColor(Card card) {
        return card.getSuit() == currentColor;
    }

    public boolean matchesNumber(Card card) {
        return card.getRank() == currentNumber || card.getRank() == Rank.WILD;
    }




    /**
     * Returns a string representation of the card.
     *
     * @return A string representing the rank and suit of the card.
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    public String getImageFilename() {
        return suit.name() + "_" + rank.name() + ".png";
    }

}