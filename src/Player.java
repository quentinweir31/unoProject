import java.util.*;


/**
 * Represents a player in the Uno game.
 */
public class Player {
    private String name;
    private List<Card> hand;

    private int score;

    private Main game;


    /**
     * Constructor for creating a player with a given name.
     *
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.game = game;
        this.score = 0;

    }

    /**
     * Gets the name of the player.
     *
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the hand of the player.
     *
     * @return The list of cards in the player's hand.
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to add to the hand.
     */
    public void addToHand(Card card) {
        hand.add(card);
    }

    /**
     * Removes a card from the player's hand at the specified index.
     *
     * @param index The index of the card to remove.
     */
    public void removeFromHand(int index) {
        hand.remove(index);
    }

    // Draw a card from the deck
    public void drawCard(Card card) {
        addToHand(card);
    }

    /**
     * Simulates drawing a card from the deck and adds it to the player's hand.
     *
     * @param deck The deck of cards to draw from.
     * @return The drawn card.
     */
    public Card drawCardFromDeck(List<Card> deck) {
        if (!deck.isEmpty()) {
            Card drawnCard = deck.remove(0);
            System.out.println(name + " draws a card: " + drawnCard);
            addToHand(drawnCard);
            return drawnCard;  // Return the drawn card
        } else {
            System.out.println("Deck is empty. Cannot draw a card.");
            return null;  // or throw an exception, depending on your design
        }
    }

    /**
     * Simulates playing a card from the player's hand.
     *
     * @param index The index of the card to play.
     * @return The played card.
     */
    public Card playCard(int index) {
        return hand.get(index);
    }

    /**
     * Displays the cards in the player's hand.
     */
    public void displayHand() {
        System.out.println(name + "'s Hand:");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ". " + hand.get(i));
        }
    }

    /**
     * Simulates a player's turn in the Uno game.
     *
     * @param deck    The deck of cards.
     * @param topCard The top card on the discard pile.
     * @return The card played or drawn.
     */
    public Card takeTurn(List<Card> deck, Card topCard) {
        System.out.println(name + "'s Turn.");
        System.out.println("Current side: " + topCard.getSuit());
        displayHand();
        System.out.println("Top card: " + topCard);
        System.out.println("Score: " + score);

        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.print("Enter card index to play or 0 to draw a card: ");
            choice = scanner.nextInt();

            if (choice == 0) {
                return drawCardFromDeck(deck);  // Return the drawn card
            } else if (choice > 0 && choice <= hand.size()) {
                Card selectedCard = playCard(choice - 1);

                // Handle the WILD card
                if (selectedCard.getRank() == Card.Rank.WILD || topCard.getRank() == Card.Rank.WILD) {
                    System.out.println(name + " plays WILD!");
                    // Prompt the player to choose a color
                    System.out.print("Choose a color (C/D/H/S) for the WILD card: ");
                    char chosenColor = scanner.next().charAt(0);
                    // Update the suit of the selected card
                    selectedCard = new Card(Card.Rank.WILD, Card.getSuitFromAbbrev(chosenColor));
                }

                // Handle the DRAW2 card
                if (selectedCard.getRank() == Card.Rank.DRAW2 || topCard.getRank() == Card.Rank.DRAW2) {
                    System.out.println(name + " plays DRAW2! The next player must draw 2 cards.");

                    // Draw two cards for the next player
                    game.drawTwoForNextPlayer(deck);

                    return selectedCard;
                }

                if (selectedCard.getRank() == Card.Rank.SKIP || topCard.getRank() == Card.Rank.SKIP) {
                    System.out.println(name + " plays SKIP! Turn is skipped.");
                    return selectedCard;
                }

                if (selectedCard.getRank() == Card.Rank.REVERSE) {
                    game.reverseDirection();  // Reverse the direction if a REVERSE card is played
                }

                // Check if the selected card is a valid play
                if (isValidPlay(selectedCard, topCard)) {
                    System.out.println(name + " plays: " + selectedCard);
                    removeFromHand(choice - 1);

                    if (hand.size() == 1 || hand.isEmpty()) {
                        score++;
                        System.out.println(name + " scores 1 point!");
                    }

                    return selectedCard;  // Return the played card
                } else {
                    System.out.println("Invalid play. The selected card cannot be played. Draw a card.");
                    drawCardFromDeck(deck);
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        } while (true);

        // The loop will always break before reaching this point, but for the sake of completeness
    }


    /**
     * Sets the game reference for the player.
     *
     * @param game The Main instance representing the Uno game.
     */
    public void setGameReference(Main game) {
        this.game = game;
    }
    
    /**
     * Checks if the selected card is a valid play based on the top card.
     *
     * @param selectedCard The card selected to play.
     * @param topCard      The top card on the discard pile.
     * @return True if the play is valid, false otherwise.
     */
    public boolean isValidPlay(Card selectedCard, Card topCard) {
        return selectedCard.getRank() == topCard.getRank() || selectedCard.getSuit() == topCard.getSuit();
    }
    
    /**
     * Updates the wild card after the user has chosen a colour
     * 
     * @param originalCard The card to be updated
     * @parem updatedCard The new card that will replace the original wild card
     */
    public void updateWildCardInHand(Card originalCard, Card updatedCard) {
        //loops through each card using an iterator looking for the originalCard parameter
        //once the original card is found, it is replace by the updatedCard parameter
        Iterator<Card> iter = hand.iterator();
        while (iter.hasNext()) {
            Card card = iter.next();
            if (card == originalCard) {
                iter.remove();
                hand.add(updatedCard);
                return;
            }
        }
    }
}
