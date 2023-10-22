public class Main {
    public static void main(String[] args) {
        // Test the Card class

        // Create cards using shorthand notation
        Card card1 = new Card("TD"); // TEN of DIAMONDS
        Card card2 = new Card("JS"); // JACK of SPADES
        Card card3 = new Card("QC"); // QUEEN of CLUBS

        // Display card information
        System.out.println("Card 1: " + card1.getRank() + " of " + card1.getSuit());
        System.out.println("Card 2: " + card2.getRank() + " of " + card2.getSuit());
        System.out.println("Card 3: " + card3.getRank() + " of " + card3.getSuit());
    }
}