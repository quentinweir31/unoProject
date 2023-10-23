public class Card {
    public enum Rank {DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, SKIP, REVERSE, WILD, DRAW2, DRAW4}
    public enum Suit {RED,YELLOW,BLUE,GREEN}

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

    public static Suit getSuitFromAbbrev(char c) {
        switch (c) {
            case 'C': return Suit.RED;
            case 'D': return Suit.YELLOW;
            case 'H': return Suit.BLUE;
            case 'S': return Suit.GREEN;
            default: throw new IllegalArgumentException("No such suit!");
        }
    }
    private final Rank rank;
    private final Suit suit;

    public Card(String s) {
        this(getRankFromAbbrev(s.charAt(0)), getSuitFromAbbrev(s.charAt(1)));
    }

    public Card(Rank r, Suit s) {
        rank = r;
        suit = s;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

}

