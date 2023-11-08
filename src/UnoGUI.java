import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class UnoGUI extends JFrame {
    private List<Player> players;
    private Player currentPlayer;
    private List<Card> deck;
    private Card topCard;

    private JPanel playerHandPanel;
    private JLabel topCardLabel;
    private JLabel currentPlayerLabel;

    public UnoGUI() {
        players = new ArrayList<>();
        deck = createDeck();
        shuffleDeck(deck);
        currentPlayer = players.isEmpty() ? null : players.get(0);

        setTitle("Uno Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupPlayerRange();
        setupCardVisibility();
        setupGUIComponents();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();

        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }

        // Add more cards or adjust the deck creation logic based on Uno rules

        return deck;
    }

    private void shuffleDeck(List<Card> deck) {
        // Implement the logic to shuffle the deck
        // ...
    }

    private void setupPlayerRange() {
        JTextField playerCountField = new JTextField();
        JButton startButton = new JButton("Start Game");

        JPanel playerRangePanel = new JPanel();
        playerRangePanel.setLayout(new GridLayout(3, 1));
        playerRangePanel.add(new JLabel("Enter the number of players:"));
        playerRangePanel.add(playerCountField);
        playerRangePanel.add(startButton);

        add(playerRangePanel, BorderLayout.NORTH);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int playerCount = Integer.parseInt(playerCountField.getText());
                initializePlayers(playerCount);
                startGame();

                playerCountField.setEnabled(false);
                startButton.setEnabled(false);
                playerRangePanel.setVisible(false);
            }
        });
    }

    private void initializePlayers(int playerCount) {
        players.clear();

        for (int i = 0; i < playerCount; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
    }

    private void startGame() {
        initializePlayers(players.size());

        currentPlayer = players.get(0);
        updateCardVisibility();
        updateCurrentPlayerLabel();

        // Distribute initial cards to players
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {  // Distribute 7 cards to each player
                Card drawnCard = deck.remove(0);
                player.addToHand(drawnCard);
            }
        }

        // Set the top card on the discard pile
        topCard = deck.remove(0);

        // Handle special cards at the beginning (if needed)
        handleSpecialCards(topCard);

        // Start the first player's turn
        playTurn();
    }

    private void playTurn() {
        // Implement the logic for a player's turn
        // ...

        // After a turn is completed, move to the next player
        moveToNextPlayer();

        // Update card visibility for the new current player
        updateCardVisibility();
    }

    private void moveToNextPlayer() {
        if (currentPlayer!= null) {
            int currentIndex = players.indexOf(currentPlayer);
            int nextIndex = (currentIndex + 1) % players.size();
            currentPlayer = players.get(nextIndex);
            updateCurrentPlayerLabel();
        }
    }

    private void handleSpecialCards(Card topCard) {
        // Implement the logic to handle special cards at the beginning of the game
        // For example, handle WILD cards or DRAW2 cards
        // ...
    }

    // Example of a method to handle playing a card
    private void playCard(Card selectedCard) {
        // Implement the logic to handle playing a card
        // ...

        // After playing the card, check if the player has won, or continue to the next player
        // ...

        // Start the next player's turn
        playTurn();
    }

    private void setupCardVisibility() {
        playerHandPanel = new JPanel();
        topCardLabel = new JLabel();
        currentPlayerLabel = new JLabel();

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(2, 1));
        southPanel.add(topCardLabel);
        southPanel.add(currentPlayerLabel);

        add(playerHandPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void updateCardVisibility() {
        if (!players.isEmpty() && currentPlayer != null) {
            displayPlayerHand();
            displayTopCard();
        }
    }

    private void displayPlayerHand() {
        playerHandPanel.removeAll();
        playerHandPanel.setLayout(new FlowLayout());

        for (Card card : currentPlayer.getHand()) {
            JButton cardButton = new JButton(card.toString());
            playerHandPanel.add(cardButton);

            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Implement the logic to handle card button click
                    // ...
                }
            });
        }

        revalidate();
        repaint();
    }

    private void displayTopCard() {
        if (topCard != null) {
            topCardLabel.setText("Top Card: " + topCard.toString());
        } else {
            topCardLabel.setText("Top Card: None");
        }
    }

    private void setupGUIComponents() {
        JButton viewStatusButton = new JButton("View Status");
        JButton drawCardButton = new JButton("Draw Card");
        JButton nextPlayerButton = new JButton("Next Player");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewStatusButton);
        buttonPanel.add(drawCardButton);
        buttonPanel.add(nextPlayerButton);

        JPanel southPanel = new JPanel();
        southPanel.add(currentPlayerLabel);
        southPanel.add(topCardLabel);
        add(southPanel, BorderLayout.SOUTH);


        add(buttonPanel, BorderLayout.EAST);

        viewStatusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayPlayerHands();
            }
        });

        drawCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawCard();
                // Implement the logic to handle draw card button click
                // ...
            }
        });

        nextPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveToNextPlayer();
                updateCardVisibility();
                // Implement the logic to handle next player button click
                // ...
            }
        });

        updateCurrentPlayerLabel();

    }

    private void drawCard() {
        Card drawnCard = currentPlayer.drawCardFromDeck(deck);
        if (drawnCard != null) {
            updateCardVisibility();
        }
    }

    private void displayPlayerHands() {
        JTextArea playerHandsTextArea = new JTextArea();
        playerHandsTextArea.setEditable(false);
        playerHandsTextArea.append("Player Hands:\n");

        for (Player player : players) {
            playerHandsTextArea.append(player.getName() + "'s Hand: " + player.getHand() + "\n");
        }

        JOptionPane.showMessageDialog(this, new JScrollPane(playerHandsTextArea),
                "Player Hands", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateCurrentPlayerLabel() {
        currentPlayerLabel.setText("Current Player: ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new UnoGUI();
            }
        });
    }
}
