import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static boolean isClockwise = true;  // Direction of play
    private static Main instance;  // Static field to hold the instance

    public static void main(String[] args) {
        // Get the number of players from the user
        int numberOfPlayers = askNumberOfPlayers();

        // Create an instance of Main
        instance = new Main();

        // Create players
        List<Player> players = createPlayers(numberOfPlayers, createDeck());

        for (Player player : players) {
            player.setGameReference(instance);
        }

        // Set the game reference for each player
        instance.simulateGame(players, createDeck());
    }

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

    private static List<Player> createPlayers(int numberOfPlayers, List<Card> deck) {
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

    private static int getNextPlayerIndex(int currentIndex, int numPlayers) {
        if (isClockwise) {
            return (currentIndex + 1) % numPlayers;
        } else {
            return (currentIndex - 1 + numPlayers) % numPlayers;
        }
    }

    public void reverseDirection() {
        isClockwise = !isClockwise;
        System.out.println("Direction reversed.");
    }

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
}
