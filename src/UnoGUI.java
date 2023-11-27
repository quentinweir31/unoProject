import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.*;
import java.util.List;
import javax.swing.JOptionPane;

public class UnoGUI extends JFrame {
    private List<Player> players;
    private Player currentPlayer;
    private List<Card> deck;
    private List<Card> flippedDeck;
    private List<Card> currentDeck;
    private Card topCard;
    private int playerCount = 0;
    private volatile Boolean numPlayersSelected = false;
    private int currentPlayerIndex = 0;
    private JPanel playerHandPanel;
    private JLabel topCardLabel;
    private JPanel topCardPanel;
    private JLabel currentPlayerLabel;
    private Boolean moveMade = false;
    private JButton nextPlayerButton;
    private JButton drawCardButton;
    private Boolean cardPlayed = false;
    private Boolean draw2Played = false;
    private Boolean draw4Played = false;
    private Boolean skipAll = false;
    private boolean flipMode = false;
    private int numDraws = 0; // this is used for keeping track of the amount of card draws for DRAW2 and DRAW4
    public Boolean reverse = false;
    public Boolean skip = false;

    public UnoGUI() {
        players = new ArrayList<>();
        deck = createDeck();
        flippedDeck = createFlippedDeck();
        currentDeck = new ArrayList<>(deck);
        shuffleDeck(currentDeck);
        currentPlayer = players.isEmpty() ? null : players.get(0);

        setTitle("Uno Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setupPlayerRange();
        setupCardVisibility();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();

        for (Card.Suit suit : Card.Suit.values()) {
            if (suit != Card.Suit.PINK && suit != Card.Suit.TEAL && suit != Card.Suit.PURPLE && suit != Card.Suit.ORANGE) {
                // Include other ranks if needed, excluding DRAW2, DRAW4, and WILD
                for (Card.Rank rank : Arrays.asList(Card.Rank.DEUCE, Card.Rank.THREE, Card.Rank.FOUR, Card.Rank.FIVE,
                        Card.Rank.SIX, Card.Rank.SEVEN, Card.Rank.EIGHT, Card.Rank.NINE, Card.Rank.SKIP,
                        Card.Rank.REVERSE, Card.Rank.DRAW2, Card.Rank.DRAW4, Card.Rank.WILD)) {
                    deck.add(new Card(rank, suit));
                }

                // Add the flip card for each allowed suit
                deck.add(new Card(Card.Rank.FLIP, suit));
            }
        }

        Collections.shuffle(deck);
        // Add more cards or adjust the deck creation logic based on Uno rules

        return deck;
    }

    public void shuffleDeck(List<Card> deck) {
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

    public void initializePlayers(int playerCount) {
        players.clear();

        for (int i = 0; i < playerCount; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
    }
    private List<Card> createFlippedDeck() {
        List<Card> deck = new ArrayList<>();
        for (Card.Suit suit : Card.Suit.values()) {
            if (suit != Card.Suit.YELLOW && suit != Card.Suit.BLUE && suit != Card.Suit.RED && suit != Card.Suit.GREEN) {
                for (Card.Rank rank : Card.Rank.values()) {
                    if(rank != Card.Rank.DRAW2 && rank != Card.Rank.DRAW4 && rank != Card.Rank.SKIP) {
                        deck.add(new Card(rank, suit));
                    }
                }
            }

        }
        return deck;
    }


    private void startGame() {
        nextPlayerButton.setEnabled(false);
        initializePlayers(players.size());

        currentPlayer = players.get(0);
        updateCurrentPlayerLabel();

        shuffleDeck(deck);
        shuffleDeck(flippedDeck);
        currentDeck = deck;
        // Distribute initial cards to players
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {  // Distribute 7 cards to each player
                Card drawnCard = currentDeck.remove(0);
                player.addToHand(drawnCard);
            }
        }
        // Set the top card on the discard pile
        topCard = currentDeck.remove(0);
        updateCardVisibility();
    }

    private void moveToNextPlayer() {
        if (currentPlayer!= null) {
            if(reverse && !skipAll) {
                if(currentPlayerIndex > 0) {
                    currentPlayerIndex--;
                } else {
                    currentPlayerIndex = players.size() - 1;
                }
            } else if(!skipAll) {
                if(currentPlayerIndex < (players.size()-1)) {
                    currentPlayerIndex++;
                } else {
                    currentPlayerIndex = 0;
                }
            }
            if(skip && !skipAll) {
                if(reverse) {
                    if(currentPlayerIndex > 0) {
                        currentPlayerIndex--;
                    } else {
                        currentPlayerIndex = players.size() - 1;
                    }
                } else{
                    if(currentPlayerIndex < (players.size()-1)){
                        currentPlayerIndex++;
                    } else {
                        currentPlayerIndex = 0;
                    }
                }
            }

            skip = false;
            currentPlayer = players.get(currentPlayerIndex);
            updateCurrentPlayerLabel();
            if(!skipAll){
                displayTopCard();
            }
            skipAll = false;
            numDraws = 0;
        }
    }

    public void checkForWinner() {
        for (Player player : players) {
            if (player.getHand().isEmpty()) {
                String winnerMessage = player.getName() + " wins the game!";
                JOptionPane.showMessageDialog(this, winnerMessage);
                System.out.println(player.getName() + " wins the game!");
                restartGame();
            }
        }
    }

    public void restartGame() {
        // Perform any necessary actions to reset the game state
        // For example, reshuffle the deck, reset scores, etc.

        // Clear hands of all players
        for (Player player : players) {
            player.getHand().clear();
        }

        // Reset other game-related variables
        // ...

        // Start a new game or perform any other initialization steps
        startGame();
    }

    private void handleCardButtonClick(Card selectedCard) {
        //wild and wild draw4
        if ((selectedCard.getRank() == Card.Rank.WILD || selectedCard.getRank() == Card.Rank.DRAW4) && !flipMode) {
            if(selectedCard.getRank() == Card.Rank.DRAW4){
                draw4Played = true;
            }
            topCard = selectedCard;
            promptForColorGUI();
        }
        //flipped wild
        if(selectedCard.getRank() == Card.Rank.WILD && flipMode) {
            topCard = selectedCard;
            promptForFlipColorGUI();
        }
        //skip_everyone
        if(selectedCard.getRank() == Card.Rank.SKIP_EVERYONE && currentPlayer.isValidPlay(selectedCard, topCard)) {
            topCard = selectedCard;
            skipAll = true;
        }
        //draw2
        if(selectedCard.getRank() == Card.Rank.DRAW2 && currentPlayer.isValidPlay(selectedCard, topCard)) {
            topCard = selectedCard;
            draw2Played = true;
        }
        //skip
        if(selectedCard.getRank() == Card.Rank.SKIP && currentPlayer.isValidPlay(selectedCard, topCard)) {
            topCard = selectedCard;
            skip = true;
        }
        //reverse
        if(selectedCard.getRank() == Card.Rank.REVERSE && currentPlayer.isValidPlay(selectedCard, topCard)) {
            topCard = selectedCard;
            reverse = !reverse;
        }
        //flip
        if(selectedCard.getRank() == Card.Rank.FLIP && currentPlayer.isValidPlay(selectedCard, topCard)) {
            topCard = selectedCard;
            flipMode = !flipMode;
            if(flipMode) {
                changeToFlip();
            } else {
                changeToNonFlip();
            }
        }
        //numbered
        if (currentPlayer.isValidPlay(selectedCard, topCard)) {
            topCard = selectedCard;
            currentPlayer.removeFromHand(currentPlayer.getHand().indexOf(selectedCard));
            moveMade = true;
            cardPlayed = true;
            drawCardButton.setEnabled(false);
            nextPlayerButton.setEnabled(true);
            displayTopCard();
            displayPlayerHand();

        } else {
            System.out.println("Invalid play. The selected card cannot be played.");
        }
    }
    private void changeToFlip() {
        //iterate through all the players' hands and the deck to flip the cards
        for(Player player : players) {
            for(Card card : player.getHand()) {
                if(card.getSuit() == Card.Suit.RED) {
                    card.setSuit(Card.Suit.TEAL);
                }
                if(card.getSuit() == Card.Suit.BLUE) {
                    card.setSuit(Card.Suit.ORANGE);
                }
                if(card.getSuit() == Card.Suit.GREEN) {
                    card.setSuit(Card.Suit.PURPLE);
                }
                if(card.getSuit() == Card.Suit.YELLOW) {
                    card.setSuit(Card.Suit.PINK);
                }
                if(card.getRank() == Card.Rank.DRAW4) {
                    card.setRank(Card.Rank.DRAW_COLOUR);
                }
                if(card.getRank() == Card.Rank.DRAW2) {
                    card.setRank(Card.Rank.DRAW5);
                }
                if(card.getRank() == Card.Rank.SKIP) {
                    card.setRank(Card.Rank.SKIP_EVERYONE);
                }
            }
        }
        deck = currentDeck;
        currentDeck = flippedDeck;
    }
    private void changeToNonFlip() {
        //iterate through all the players' hands and the deck to flip the cards
        for(Player player : players) {
            for(Card card : player.getHand()) {
                if(card.getSuit() == Card.Suit.TEAL) {
                    card.setSuit(Card.Suit.RED);
                }
                if(card.getSuit() == Card.Suit.ORANGE) {
                    card.setSuit(Card.Suit.BLUE);
                }
                if(card.getSuit() == Card.Suit.PURPLE) {
                    card.setSuit(Card.Suit.GREEN);
                }
                if(card.getSuit() == Card.Suit.PINK) {
                    card.setSuit(Card.Suit.YELLOW);
                }
                if(card.getRank() == Card.Rank.DRAW_COLOUR) {
                    card.setRank(Card.Rank.DRAW4);
                }
                if(card.getRank() == Card.Rank.DRAW5) {
                    card.setRank(Card.Rank.DRAW2);
                }
                if(card.getRank() == Card.Rank.SKIP_EVERYONE) {
                    card.setRank(Card.Rank.SKIP);
                }
            }
        }
        flippedDeck = currentDeck;
        currentDeck = deck;
    }
    private void promptForColorGUI() {
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

        switch (choice) {
            case 0:
                topCard.setSuit(Card.Suit.RED);
                break;
            case 1:
                topCard.setSuit(Card.Suit.YELLOW);
                break;
            case 2:
                topCard.setSuit(Card.Suit.BLUE);
                break;
            case 3:
                topCard.setSuit(Card.Suit.GREEN);
                break;
            default:
                break;
        }
    }
    private void promptForFlipColorGUI() {
        String[] options = {"PINK", "TEAL", "ORANGE", "PURPLE"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose a color for the WILD card:",
                "WILD Card Color",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        switch (choice) {
            case 0:
                topCard.setSuit(Card.Suit.PINK);
                break;
            case 1:
                topCard.setSuit(Card.Suit.TEAL);
                break;
            case 2:
                topCard.setSuit(Card.Suit.ORANGE);
                break;
            case 3:
                topCard.setSuit(Card.Suit.PURPLE);
                break;
            default:
                break;
        }
    }

    private void setupCardVisibility() {
        playerHandPanel = new JPanel();
        topCardLabel = new JLabel();
        topCardPanel = new JPanel();
        currentPlayerLabel = new JLabel();

        add(currentPlayerLabel, BorderLayout.SOUTH);
        add(playerHandPanel, BorderLayout.CENTER);
        add(topCardLabel, BorderLayout.EAST);


        currentPlayerLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    public void updateCardVisibility() {
        if (!players.isEmpty() && currentPlayer != null) {
            displayPlayerHand();
            displayTopCard();
        }
    }

    public void displayPlayerHand() {
        playerHandPanel.removeAll();
        playerHandPanel.setLayout(new GridLayout(3, 1));
        playerHandPanel.add(new JPanel());
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        playerHandPanel.add(centerPanel);

        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        playerHandPanel.add(scrollPane);

        for (Card card : currentPlayer.getHand()) {
            JButton cardButton = new JButton("Card Image");

            String imageName = card.getImageFilename();
            URL imageURL = getClass().getClassLoader().getResource("resources/" + imageName);

            if (imageURL != null) {
                ImageIcon cardImage = new ImageIcon(imageURL);
                Image img = cardImage.getImage();
                Image resizedImg = img.getScaledInstance(100,150, Image.SCALE_SMOOTH);
                cardImage = new ImageIcon(resizedImg);
                cardButton.setIcon(cardImage);
            } else {
                // Handle case where image is not found
                cardButton.setText("Image Not Found");
            }
            Dimension buttonSize = new Dimension(100,150); // Adjust the size as needed
            if(cardPlayed || draw2Played || draw4Played) {
                cardButton.setEnabled(false);
            }
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

    public void displayTopCard() {
        if (topCard != null) {
            topCardPanel.removeAll();
            topCardLabel.setText("Top Card: " + topCard.toString());


            topCardPanel.setLayout(new BorderLayout());

            // Create a JPanel for the top card image
            JPanel topImagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton topCardButton = new JButton();

            // Get the filename of the image for the top card
            String topCardImageName = topCard.getImageFilename();
            URL topCardImageURL = getClass().getClassLoader().getResource("resources/" + topCardImageName);

            if (topCardImageURL != null) {
                ImageIcon topCardImage = new ImageIcon(topCardImageURL);

                // Set a fixed size or a proportion of the label's size
                int buttonWidth = 100;  // Adjust as needed
                int buttonHeight = 150; // Adjust as needed

                // Resize the ImageIcon to fit the button size
                Image img = topCardImage.getImage();
                Image resizedImg = img.getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH);
                topCardImage = new ImageIcon(resizedImg);

                topCardButton.setIcon(topCardImage);
            } else {
                // Handle case where image is not found
                topCardButton.setText("Image Not Found");
            }

            Dimension buttonSize = new Dimension(100, 150);
            topCardButton.setPreferredSize(buttonSize);
            topImagePanel.add(topCardButton);

            // Create a label for displaying suit and rank
            JLabel suitRankLabel = new JLabel("Top Card: " + topCard.getSuit() + " " + topCard.getRank());
            suitRankLabel.setHorizontalAlignment(JLabel.CENTER); // Center the label text


            // Add components to the topCardPanel
            //topCardPanel.removeAll();
            topCardPanel.setLayout(new BorderLayout());
            topCardPanel.add(topImagePanel, BorderLayout.CENTER);
            topCardPanel.add(suitRankLabel, BorderLayout.SOUTH);

            //add(topCardPanel, BorderLayout.NORTH);
        } else {
            topCardLabel.setText("Top Card: None");
            topCardLabel.setIcon(null);
        }
    }

    public void setupGUIComponents() {
        JButton viewStatusButton = new JButton("View Status");
        drawCardButton = new JButton("Draw Card");
        nextPlayerButton = new JButton("Next Player");
        nextPlayerButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewStatusButton);
        buttonPanel.add(drawCardButton);
        buttonPanel.add(nextPlayerButton);


        add(currentPlayerLabel, BorderLayout.SOUTH);
        add(topCardPanel, BorderLayout.NORTH);
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
                checkForWinner();
                drawCard();
            }
        });


        nextPlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(moveMade && !draw2Played){
                    checkForWinner();
                    drawCardButton.setEnabled(true);
                    moveToNextPlayer();
                    moveMade = false;
                    cardPlayed = false;
                    updateCardVisibility();
                    nextPlayerButton.setEnabled(false);
                } else if(moveMade && !draw4Played){
                    checkForWinner();
                    drawCardButton.setEnabled(true);
                    moveToNextPlayer();
                    moveMade = false;
                    cardPlayed = false;
                    updateCardVisibility();
                    nextPlayerButton.setEnabled(false);
                }
                revalidate();
                repaint();
            }
        });

        updateCurrentPlayerLabel();
    }

    public void drawCard() {
        numDraws += 1;
        if(topCard.getRank() == Card.Rank.DRAW2) {
            if (numDraws == 2) {
                draw2Played = false;
            }
        }
        if(topCard.getRank() == Card.Rank.DRAW4) {
            if (numDraws == 4) {
                draw4Played = false;
            }
        }
        Card drawnCard = currentPlayer.drawCardFromDeck(currentDeck);
        if (drawnCard != null) {
            updateCardVisibility();
            revalidate();
            repaint();
            if (!draw2Played && !draw4Played) {
                drawCardButton.setEnabled(false);
                moveMade = true;
                nextPlayerButton.setEnabled(true);
            }
        }
    }

    public void displayPlayerHands() {
        JTextArea playerHandsTextArea = new JTextArea();
        playerHandsTextArea.setEditable(false);
        playerHandsTextArea.append("Player Hands:\n");

        for (Player player : players) {
            playerHandsTextArea.append(player.getName() + "'s Hand: " + player.getHand() + "\n");
        }

        JOptionPane.showMessageDialog(this, new JScrollPane(playerHandsTextArea),
                "Player Hands", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateCurrentPlayerLabel() {
        if (currentPlayer != null) {
            currentPlayerLabel.setText("Current Player: " + currentPlayer.getName());
        } else {
            currentPlayerLabel.setText("Current Player: None");
        }
    }

    public List<Player> getPlayers() {
        return players;
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
