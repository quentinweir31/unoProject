import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Deck {
    private ArrayList<Card> deck;

    public Deck() {
        deck = new ArrayList<Card>();
        createDeck();
    }

    private void createDeck() {
        String[] allColours = {"Red", "Blue", "Yellow", "Green"};
        //These are all the types of cards except for numbered and wilds, along with the amount per colour
        HashMap<String, Integer> allTypes = new HashMap<>();
        allTypes.put("Draw", 2);
        allTypes.put("Skip", 2);
        allTypes.put("Reverse", 2);
        allTypes.put("Flip", 2);
        // loop through each colour, i is the index of the allColours list
        for (int i = 0; i < 4; i++) {
            // adding numbers 1 through 9 for each colour
            for (int j = 1; j < 10; j++) {
                String value = "" + j;
                Card card = new Card(allColours[i], value);
                deck.add(card);
                deck.add(card); //we have to add two of each number per colour
            }
            //adding draw, skip, reverse, and flip
            for (String type : allTypes.keySet()) {
                int maxOccurence = allTypes.get(type);
                for (int h = 0; h < maxOccurence; h++) {
                    Card card = new Card(allColours[i], type);
                    deck.add(card);
                }
            }
            // adding wild and wild draw
            deck.add(new Card("Wild"));
            deck.add(new Card("Wild", "Wild Draw"));
        }
    }

    public void shuffleDeck() {
        Collections.shuffle(deck);
    }

    public Card drawCard() {
        if (!deck.isEmpty()) {
            return deck.remove(0);
        } else {
            return null;
        }
    }
    public void printDeck() {
        for (Card card : deck) {
            System.out.println(card);
        }
    }
}
