import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 *
 * Milestone #1
 *
 * --Authors--
 * Jonas Hallgrimsson 101223596
 * Jawad Mohammed 101233031
 * Quentin Weir 101234808
 * Omar Hamzat 101244220
 *
 * Version 1.0
 * Oct 22nd 2023
 *
 */

/**
 * The main class for simulating the Uno game.
 */

public class Main {
    private static boolean isClockwise = true;  // Direction of play

    private static int currentPlayerIndex = 0;
    private static List<Player> players;
    private static Main instance;  // Static field to hold the instance


    /**
     * The main method to start the Uno game.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        // Get the number of players from the user
        int numberOfPlayers = askNumberOfPlayers();

        // Create an instance of Main
        instance = new Main();

        // Create players
        players = createPlayers(numberOfPlayers, createDeck());

        for (Player player : players) {
            player.setGameReference(instance);
        }

        // Set the game reference for each player
        instance.simulateGame(players, createDeck());
    }

    /**
     * Asks the user to input the number of players for the Uno game.
     *
     * @return The number of players.
     */
    private static int askNumberOfPlayers() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of players (2-4): ");
        int numberOfPlayers = scanner.nextInt();

        while (numberOfPlayers < 2 || numberOfPlayers > 4) {
            System.out.println("Invalid number of players. Please enter a number between 2 and 4.");
            System.out.print("Enter the number of players (2-4): ");
            numberOfPlayers = scanner.nextInt();
        }

        return numberOfPlayers;
    }


    /**
     * Creates a list of players for the Uno game.
     *
     * @param numberOfPlayers The number of players to create.
     * @param deck             The deck of cards to distribute among players.
     * @return A list of created players.
     */
    static List<Player> createPlayers(int numberOfPlayers, List<Card> deck) {
        List<Player> players = new ArrayList<>();

        for (int i = 1; i <= numberOfPlayers; i++) {
            System.out.print("Enter the name of Player " + i + ": ");
            Scanner scanner = new Scanner(System.in);
            String playerName = scanner.nextLine();
            Player player = new Player(playerName);

            // Draw 7 cards for each player
            for (int j = 0; j < 7; j++) {
                if (!deck.isEmpty()) {
                    Card drawnCard = deck.remove(0);
                    player.addToHand(drawnCard);
                } else {
                    System.out.println("Deck is empty. Cannot draw more cards.");
                    break;
                }
            }

            players.add(player);
        }

        return players;
    }


    /**
     * Simulates the Uno game.
     *
     * @param players The list of players participating in the game.
     * @param deck    The deck of cards for the game.
     */
    private static void simulateGame(List<Player> players, List<Card> deck) {
        int currentPlayerIndex = 0;
        List<Card> discardPile = new ArrayList<>();

        // Initialize the discard pile with the first card from the deck
        discardPile.add(deck.remove(0));

        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);

            // Display the current state of the game
            System.out.println("Top card: " + discardPile.get(discardPile.size() - 1));
            currentPlayer.displayHand();

            // Player takes a turn and plays a card
            Card selectedCard = currentPlayer.takeTurn(deck, discardPile.get(discardPile.size() - 1));

            // Update the discard pile with the selected card
            discardPile.add(selectedCard);

            // Reverse the direction if a REVERSE card is played
            if (selectedCard.getRank() == Card.Rank.REVERSE) {
                isClockwise = !isClockwise;
                Collections.reverse(players);
            }

            // Move to the next player based on the direction of play
            currentPlayerIndex = getNextPlayerIndex(currentPlayerIndex, players.size());
        }
    }

    /**
     * Calculates the index of the next player based on the direction of play.
     *
     * @param currentIndex The index of the current player.
     * @param numPlayers   The total number of players.
     * @return The index of the next player.
     */

    private static int getNextPlayerIndex(int currentIndex, int numPlayers) {
        if (isClockwise) {
            return (currentIndex + 1) % numPlayers;
        } else {
            return (currentIndex - 1 + numPlayers) % numPlayers;
        }
    }

    /**
     * Reverses the direction of play in the Uno game.
     */
    public void reverseDirection() {
        isClockwise = !isClockwise;
        System.out.println("Direction reversed.");
    }


    /**
     * Creates a shuffled deck of Uno cards.
     *
     * @return A list representing the shuffled deck of Uno cards.
     */
    private static List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }

        // Shuffle the deck
        Collections.shuffle(deck);

        return deck;
    }


    /**
     * Moves to the next player in the Uno game.
     */
    public static void moveToNextPlayer() {
        if (isClockwise) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } else {
            currentPlayerIndex = (currentPlayerIndex - 1 + players.size()) % players.size();
        }
    }

    /**
     * Gets the current player in the Uno game.
     *
     * @return The current player.
     */
    public static Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Draws two cards for the next player in the Uno game.
     *
     * @param deck The deck of cards for drawing.
     */
    public void drawTwoForNextPlayer(List<Card> deck) {
        // Move to the next player
        moveToNextPlayer();

        // Draw two cards for the next player
        Player nextPlayer = getCurrentPlayer();
        nextPlayer.drawCardFromDeck(deck);
        nextPlayer.drawCardFromDeck(deck);
    }


}
