import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> deck;

    public Deck() {
        deck = new ArrayList<Card>();
        createDeck();
    }
    // not yet complete
    private void createDeck() { 
        int numCards = 0; //max is 112
        int numColour = 0; //max is 25
        int numZerosPerColour = 0; //max is 1 per colour
        int numOneToNinePerColour = 0; //max is 2 per colour
        int numDrawPerColour = 0; //max is 2 per colour
        int numReversePerColour = 0; //max is 2 per colour
        int numSkipPerColour = 0; // max is 2 per colour
        int numWild = 0; // max is 4
        int numWildDraw = 0; // max is 4
        String[] allColours = {"Red", "Blue", "Yellow", "Green"};
        String[] allTypes = {"Draw", "Skip", "Reverse", "Wild", "Wild Draw", "Flip"};
        //my plan here is to make a random deck of 112 cards
    }
}// type something here if you can edit this
