import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Player {
    private String name;
    private List<Card> hand;

    private int score;

    private Main game;



    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.game = game;
        this.score = 0;

    }


    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addToHand(Card card) {
        hand.add(card);
    }

    public void removeFromHand(int index) {
        hand.remove(index);
    }

    // Draw a card from the deck
    public void drawCard(Card card) {
        addToHand(card);
    }

    // Simulate drawing a card from the deck
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

    // Simulate playing a card
    public Card playCard(int index) {
        return hand.get(index);
    }

    // Display the player's hand
    public void displayHand() {
        System.out.println(name + "'s Hand:");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println((i + 1) + ". " + hand.get(i));
        }
    }

    // Simulate a player's turn
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


    public void setGameReference(Main game) {
        this.game = game;
    }
    // Check if the selected card is a valid play
    private boolean isValidPlay(Card selectedCard, Card topCard) {
        return selectedCard.getRank() == topCard.getRank() || selectedCard.getSuit() == topCard.getSuit();
