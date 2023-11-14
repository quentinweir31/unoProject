import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.List;
import javax.swing.JOptionPane;

public class UnoGUI extends JFrame {
    private List<Player> players;
    private Player currentPlayer;
    private List<Card> deck;
    private Card topCard;
    private int playerCount = 0;
    private volatile Boolean numPlayersSelected = false;
    private int currentPlayerIndex = 0;
    private JPanel playerHandPanel;
    private JLabel topCardLabel;
    private JLabel currentPlayerLabel;
    private Boolean moveMade;
    private JButton nextPlayerButton;

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
        Collections.shuffle(deck);
    }

    private void setupPlayerRange() {
        JMenuBar menuBar = new JMenuBar();
        JMenu dropDown = new JMenu("Select number of players");
        JMenuItem twoPlayer = new JMenuItem("2");
        JMenuItem threePlayer = new JMenuItem("3");
        JMenuItem fourPlayer = new JMenuItem("4");
        dropDown.add(twoPlayer);
        dropDown.add(threePlayer);
        dropDown.add(fourPlayer);
        menuBar.add(dropDown);
        twoPlayer.setPreferredSize(menuBar.getPreferredSize());
        threePlayer.setPreferredSize(menuBar.getPreferredSize());
        fourPlayer.setPreferredSize(menuBar.getPreferredSize());

        twoPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedNumber = ((JMenuItem) e.getSource()).getText();
                playerCount = Integer.parseInt(selectedNumber);
                numPlayersSelected = true;
                dropDown.setText(playerCount + " Players");
            }
        });
        threePlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedNumber = ((JMenuItem) e.getSource()).getText();
                playerCount = Integer.parseInt(selectedNumber);
                numPlayersSelected = true;
                dropDown.setText(playerCount + " Players");
            }
        });
        fourPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedNumber = ((JMenuItem) e.getSource()).getText();
                playerCount = Integer.parseInt(selectedNumber);
                numPlayersSelected = true;
                dropDown.setText(playerCount + " Players");
            }
        });

        JButton startButton = new JButton("Start Game");
        JButton closeButton = new JButton("Close");
        JPanel playerRangePanel = new JPanel();
        playerRangePanel.setLayout(new FlowLayout());
        playerRangePanel.add(menuBar);
        playerRangePanel.add(startButton);
        playerRangePanel.add(closeButton);

        add(playerRangePanel, BorderLayout.WEST);
        setSize(400, 150);
        setResizable(false);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (numPlayersSelected) {
                    setupGUIComponents();
                    initializePlayers(playerCount);
                    startGame();

                    menuBar.setEnabled(false);
                    startButton.setEnabled(false);
                    playerRangePanel.setVisible(false);
                    setResizable(true);
                    setSize(1500, 1000);
                }
            }
        });
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
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
        nextPlayerButton.setEnabled(false);
        initializePlayers(players.size());

        currentPlayer = players.get(0);
        updateCardVisibility();
        updateCurrentPlayerLabel();

        shuffleDeck(deck);

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
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            currentPlayer = players.get(currentPlayerIndex);
            updateCurrentPlayerLabel();
        }
    }

    private void handleSpecialCards(Card topCard) {
        if (topCard.getRank() == Card.Rank.WILD) {
            System.out.println("Special card detected: WILD");

            // Prompt the user to choose a color using a GUI dialog
            char chosenColor = promptForColorGUI();

            // Update the suit of the WILD card
            topCard.setSuit(chosenColor);

            System.out.println("Top card after handling WILD: " + topCard);
        } else if (topCard.getRank() == Card.Rank.DRAW2) {
            System.out.println("Special card detected: DRAW2");

            // Simulate the logic for DRAW2 cards (you might want to implement specific rules)
            System.out.println("The next player must draw 2 cards.");

            // Additional logic for handling DRAW2 cards can be added here
        }

        // Add logic to handle other special cards if needed

        // Set the updated top card after handling special cards
        this.topCard = topCard;
    }

    private void handleCardButtonClick(Card selectedCard) {
        if (selectedCard.getRank() == Card.Rank.WILD) {
            // If the selected card is a Wild Card, handle it separately
            handleWildCardFromHand(selectedCard);
        } else if (currentPlayer.isValidPlay(selectedCard, topCard)) {
            // Replace the top card with the selected card
            topCard = selectedCard;

            // Remove the selected card from the player's hand
            currentPlayer.removeFromHand(currentPlayer.getHand().indexOf(selectedCard));

            // Continue with the game logic or any other actions needed
            // ...

            // update the top card
            displayTopCard();

            //set moveMade to true and enable the nextPlayerButton
            moveMade = true;
            nextPlayerButton.setEnabled(true);
        } else {
            // Invalid play, notify the player or take appropriate action
            System.out.println("Invalid play. The selected card cannot be played.");
        }
    }

    private void handleWildCardFromHand(Card wildCard) {
        // Prompt the user to choose a color using a GUI dialog
        char chosenColor = promptForColorGUI();

        // Update the suit of the WILD card
        wildCard.setSuit(chosenColor);
        Card updatedWildCard = new Card(Card.Rank.WILD, Card.getSuitFromAbbrev(chosenColor));

        currentPlayer.updateWildCardInHand(wildCard, updatedWildCard);

        // Continue with the game logic or any other actions needed
        // ...

        // Move to the next player and update card visibility
        //moveToNextPlayer();
        //updateCardVisibility();
        moveMade = true;
        nextPlayerButton.setEnabled(true);
    }
    private char promptForColorGUI() {
        // Implement the GUI prompt for choosing a color
        // You can use a JOptionPane or other GUI components for this
        // For simplicity, let's use a JOptionPane input dialog

        String[] options = {"RED", "YELLOW", "BLUE", "GREEN"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose a color for the WILD card:",
                "WILD Card Color",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        // Convert the choice to the corresponding color abbreviation
        return "CDHS".charAt(choice);
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

        add(currentPlayerLabel, BorderLayout.NORTH);
        add(playerHandPanel, BorderLayout.CENTER);
        add(topCardLabel, BorderLayout.SOUTH);

        currentPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
        topCardLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    private void updateCardVisibility() {
        if (!players.isEmpty() && currentPlayer != null) {
            displayPlayerHand();
            displayTopCard();
        }
    }

    private void displayPlayerHand() {
        playerHandPanel.removeAll();
        playerHandPanel.setLayout(new GridLayout(3, 1));
        playerHandPanel.add(new JPanel());
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playerHandPanel.add(centerPanel);

        for (Card card : currentPlayer.getHand()) {
            JButton cardButton = new JButton(card.toString());
            Dimension buttonSize = new Dimension(150, 250); // Adjust the size as needed
            cardButton.setPreferredSize(buttonSize);
            centerPanel.add(cardButton);

            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleCardButtonClick(card);
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
        nextPlayerButton = new JButton("Next Player");
        nextPlayerButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewStatusButton);
        buttonPanel.add(drawCardButton);
        buttonPanel.add(nextPlayerButton);

        add(currentPlayerLabel, BorderLayout.NORTH);
        add(topCardLabel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.EAST);
        currentPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
        topCardLabel.setHorizontalAlignment(JLabel.CENTER);

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

            }
        });

        nextPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(moveMade){
                    moveToNextPlayer();
                    updateCardVisibility();
                    moveMade = false;
                    nextPlayerButton.setEnabled(false);
                }
            }
        });

        updateCurrentPlayerLabel();
    }

    private void drawCard() {
        Card drawnCard = currentPlayer.drawCardFromDeck(deck);
        if (drawnCard != null) {
            updateCardVisibility();
            moveMade = true;
            nextPlayerButton.setEnabled(true);
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
        if (currentPlayer != null) {
            currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());
        } else {
            currentPlayerLabel.setText("Current Player: None");
        }
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
