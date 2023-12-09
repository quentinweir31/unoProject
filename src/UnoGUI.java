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

    private List<AIPlayer> aiPlayers;
    private List<Card> flippedDeck;

    private List<Card> currentDeck;
    private Card topCard;

    private SaveLoad initialGameState;

    private SaveLoad undo;
    private SaveLoad redo;


    private AIPlayer aiPlayer;
    private int playerCount = 0;

    private int aiPlayerCount = 0;
    private volatile Boolean numPlayersSelected = false;
    private int currentPlayerIndex = 0;
    private JPanel playerHandPanel;
    private JLabel topCardLabel;
    private JPanel topCardPanel;
    private JLabel currentPlayerLabel;
    private Boolean moveMade = false;

    private boolean isHumanTurn;
    private JButton nextPlayerButton;
    private JButton drawCardButton;

    private JButton saveButton;

    private JButton loadButton;

    private JButton restartButton;

    private JButton undoButton;
    private JButton redoButton;
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
        aiPlayer = new AIPlayer("AI Player");
        aiPlayers = new ArrayList<>();
        shuffleDeck(currentDeck);
        playRound();
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
                        Card.Rank.REVERSE, Card.Rank.WILD)) {
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

        // Dropdown for human players
        JMenu humanDropDown = new JMenu("Select number of human players");
        JMenuItem twoHumanPlayer = new JMenuItem("2");
        JMenuItem threeHumanPlayer = new JMenuItem("3");
        JMenuItem fourHumanPlayer = new JMenuItem("4");
        humanDropDown.add(twoHumanPlayer);
        humanDropDown.add(threeHumanPlayer);
        humanDropDown.add(fourHumanPlayer);
        menuBar.add(humanDropDown);
        twoHumanPlayer.setPreferredSize(menuBar.getPreferredSize());
        threeHumanPlayer.setPreferredSize(menuBar.getPreferredSize());
        fourHumanPlayer.setPreferredSize(menuBar.getPreferredSize());




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

        twoHumanPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedNumber = ((JMenuItem) e.getSource()).getText();
                playerCount = Integer.parseInt(selectedNumber);
                numPlayersSelected = true;
                humanDropDown.setText(playerCount + " Players");
            }
        });
        threeHumanPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedNumber = ((JMenuItem) e.getSource()).getText();
                playerCount = Integer.parseInt(selectedNumber);
                numPlayersSelected = true;
                humanDropDown.setText(playerCount + " Players");
            }
        });
        fourHumanPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedNumber = ((JMenuItem) e.getSource()).getText();
                playerCount = Integer.parseInt(selectedNumber);
                numPlayersSelected = true;
                humanDropDown.setText(playerCount + " Players");
            }
        });

        JMenu aiDropDown = new JMenu("Select number of AI players");

        // Dynamically add AI player options
        for (int i = 1; i <= 15; i++) {
            JMenuItem aiPlayerItem = new JMenuItem(Integer.toString(i));
            aiDropDown.add(aiPlayerItem);
            aiPlayerItem.setPreferredSize(menuBar.getPreferredSize());

            aiPlayerItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String selectedNumber = ((JMenuItem) e.getSource()).getText();
                    aiPlayerCount = Integer.parseInt(selectedNumber);
                    numPlayersSelected = true;
                    aiDropDown.setText(aiPlayerCount + " AI Players");
                }
            });
        }

        menuBar.add(aiDropDown);


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
                    initializePlayers(playerCount,aiPlayerCount);
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

    public void initializePlayers(int playerCount, int AiPlayerCount) {
        players.clear();
        aiPlayers.clear();

        for (int i = 0; i < playerCount; i++) {
            players.add(new Player("Player " + (i + 1)));
        }

        for (int i = 0; i < aiPlayerCount; i++) {
            aiPlayers.add(new AIPlayer("AI Player " + (i + 1)));
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
        //undoButton.setEnabled(false);
        //redoButton.setEnabled(false);

        initialGameState = new SaveLoad("previous",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
        restartButton.setEnabled(true);

        nextPlayerButton.setEnabled(false);
        initializePlayers(players.size(),aiPlayers.size());

        currentPlayer = players.get(0);
        isHumanTurn = true;
        updateCurrentPlayerLabel();

        shuffleDeck(deck);
        shuffleDeck(flippedDeck);

        // Distribute initial cards to players
        for (Player player : players) {
            for (int i = 0; i < 7; i++) {  // Distribute 7 cards to each player
                Card drawnCard = currentDeck.remove(0);
                player.addToHand(drawnCard);
            }
        }
        for (AIPlayer aiPlayer : aiPlayers) {
            for (int i = 0; i < 7; i++) {  // Distribute 7 cards to each AI player
                if (!deck.isEmpty()) {
                    Card drawnCard = deck.remove(0);
                    aiPlayer.addToHand(drawnCard);
                } else {
                    // Handle the case when the deck is empty
                    System.out.println("Deck is empty. Cannot distribute more cards.");
                    break; // Exit the loop if the deck is empty
                }
            }
        }

        // Set the top card on the discard pile
        topCard = currentDeck.remove(0);
        updateCardVisibility();

        if (currentPlayer instanceof AIPlayer) {
            playAITurn((AIPlayer) currentPlayer);
            updateCurrentPlayerLabel();
            displayPlayerHand();
            updateCardVisibility();
            displayTopCard();
        }
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


            isHumanTurn = currentPlayer != null;
        }

    }


    public void checkForWinner() {
        for (Player player : players) {
            if (player.getHand().isEmpty()) {
                String winnerMessage = player.getName() + " wins the game!";
                //loop through each player and add up the scores
                for(Player gamer : players) {
                    Integer score = 0;
                    for(Card card : gamer.getHand()) {
                        if(card.getRank() == Card.Rank.DEUCE) {
                            score += 2;
                        }
                        else if(card.getRank() == Card.Rank.THREE) {
                            score += 3;
                        }
                        else if(card.getRank() == Card.Rank.FOUR) {
                            score += 4;
                        }
                        else if(card.getRank() == Card.Rank.FIVE) {
                            score += 5;
                        }
                        else if(card.getRank() == Card.Rank.SIX) {
                            score += 6;
                        }
                        else if(card.getRank() == Card.Rank.SEVEN) {
                            score += 7;
                        }
                        else if(card.getRank() == Card.Rank.EIGHT) {
                            score += 8;
                        }
                        else if(card.getRank() == Card.Rank.NINE) {
                            score += 9;
                        }
                        else if(card.getRank() == Card.Rank.DRAW2) {
                            score += 10;
                        }
                        else if(card.getRank() == Card.Rank.DRAW5 || card.getRank() == Card.Rank.REVERSE || card.getRank() == Card.Rank.SKIP || card.getRank() == Card.Rank.FLIP) {
                            score += 20;
                        }
                        else if(card.getRank() == Card.Rank.SKIP_EVERYONE) {
                            score += 30;
                        }
                        else if(card.getRank() == Card.Rank.WILD) {
                            score += 40;
                        }
                        else if(card.getRank() == Card.Rank.DRAW4) {
                            score += 50;
                        }
                        else if(card.getRank() == Card.Rank.DRAW_COLOUR) {
                            score += 60;
                        }
                    }
                    winnerMessage += "\n" + gamer.getName() + " has score " + score.toString();
                }
                Object[] options = {"OK", "Restart Game"};
                int result = JOptionPane.showOptionDialog(
                        this,
                        winnerMessage,
                        "Game Over",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (result == 0) {
                    System.exit(0);
                } else if (result == 1) {
                    restartGame();
                }
            }
        }
    }

    public void restartGame() {

        for (Player player : players) {
            player.getHand().clear();
        }
        moveMade = false;
        cardPlayed = false;
        draw2Played = false;
        draw4Played = false;
        skipAll = false;
        flipMode = false;
        numDraws = 0;
        reverse = false;
        skip = false;
        drawCardButton.setEnabled(true);
        currentPlayerIndex = 0;
        flippedDeck = createDeck();
        deck = createDeck();

        startGame();
    }

    private void handleCardButtonClick(Card selectedCard) {
        //wild and wild draw4


        if ((selectedCard.getRank() == Card.Rank.WILD || selectedCard.getRank() == Card.Rank.DRAW4) && !flipMode) {

            undo = new SaveLoad("previous",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
            undoButton.setEnabled(true);
            //System.out.println("Undo Triggered");
            if(selectedCard.getRank() == Card.Rank.DRAW4){
                draw4Played = true;
            }
            topCard = selectedCard;
            promptForColorGUI();

        }
        //flipped wild
        if(selectedCard.getRank() == Card.Rank.WILD && flipMode) {

            undo = new SaveLoad("previous",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
            undoButton.setEnabled(true);
            //System.out.println("Undo Triggered");
            topCard = selectedCard;
            promptForFlipColorGUI();

        }
        //skip_everyone
        if(selectedCard.getRank() == Card.Rank.SKIP_EVERYONE && currentPlayer.isValidPlay(selectedCard, topCard)) {

            undo = new SaveLoad("previous",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
            undoButton.setEnabled(true);
            //System.out.println("Undo Triggered");
            topCard = selectedCard;
            skipAll = true;

        }
        //draw2
        if(selectedCard.getRank() == Card.Rank.DRAW2 && currentPlayer.isValidPlay(selectedCard, topCard)) {

            undo = new SaveLoad("previous",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
            undoButton.setEnabled(true);
            // System.out.println("Undo Triggered");
            topCard = selectedCard;
            draw2Played = true;

        }
        //skip
        if(selectedCard.getRank() == Card.Rank.SKIP && currentPlayer.isValidPlay(selectedCard, topCard)) {


            undo = new SaveLoad("previous",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
           undoButton.setEnabled(true);
            //System.out.println("Undo Triggered");
            topCard = selectedCard;
            skip = true;

        }
        //reverse
        if(selectedCard.getRank() == Card.Rank.REVERSE && currentPlayer.isValidPlay(selectedCard, topCard)) {

            undo = new SaveLoad("previous",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
            undoButton.setEnabled(true);
            //System.out.println("Undo Triggered");
            topCard = selectedCard;
            reverse = !reverse;


        }
        //flip
        if(selectedCard.getRank() == Card.Rank.FLIP && currentPlayer.isValidPlay(selectedCard, topCard)) {

            undo = new SaveLoad("previous",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
            undoButton.setEnabled(true);
            // System.out.println("Undo Triggered");
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

            undo = new SaveLoad("previous",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
            undoButton.setEnabled(true);
            //System.out.println("Undo Triggered");
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

    public void updatePlayersHands() {
        for (Player player : players) {
            List<Card> updatedHand = new ArrayList<>();

            for (Card playerCard : player.getHand()) {
                // Find the corresponding card in the current deck
                Card matchingCard = findMatchingCard(playerCard);

                if (matchingCard != null) {
                    updatedHand.add(matchingCard);
                } else {
                    // Handle the case where the card is not found
                    // You might want to keep it as is or replace it with a default card
                    updatedHand.add(playerCard);
                }
            }

            // Update the player's hand
            player.setHand(updatedHand);
        }
    }

    private Card findMatchingCard(Card playerCard) {
        // Iterate through the current deck to find a card with the same rank and suit
        for (Card deckCard : currentDeck) {
            if (deckCard.getRank() == playerCard.getRank() && deckCard.getSuit() == playerCard.getSuit()) {
                return deckCard;
            }
        }
        return null; // Card not found in the current deck
    }

    private void toggleDeck() {
        if (currentDeck == deck) {
            currentDeck = new ArrayList<>(flippedDeck);
        } else {
            currentDeck = new ArrayList<>(deck);
        }

        updatePlayersHands();
        displayPlayerHand();
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
                    undoButton.setEnabled(false);
                    redoButton.setEnabled(false);

                }

            });
            undoButton.setEnabled(true);
            redoButton.setEnabled(true);
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
        saveButton = new JButton("Save Game");
        loadButton = new JButton("Load Game");
        undoButton = new JButton(("Undo"));
        redoButton = new JButton(("Redo"));
        restartButton = new JButton("Restart");
        nextPlayerButton.setEnabled(false);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(viewStatusButton);
        buttonPanel.add(drawCardButton);
        buttonPanel.add(nextPlayerButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);
        buttonPanel.add(restartButton);


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
                if (moveMade && !draw2Played) {
                    if (currentPlayerIndex < players.size() - 1) {
                        moveToNextPlayer();
                        moveMade = false;
                        cardPlayed = false;
                    } else {
                        playAITurn(aiPlayer);
                        updateCurrentPlayerLabel();
                    }

                    displayPlayerHand();
                    updateCardVisibility();
                    displayTopCard();
                    nextPlayerButton.setEnabled(false);
                }

                if (moveMade && !draw4Played) {
                    if (currentPlayerIndex < players.size() - 1) {
                        moveToNextPlayer();
                        moveMade = false;
                        cardPlayed = false;
                    } else {
                        playAITurn(aiPlayer);
                        updateCurrentPlayerLabel();
                    }

                    displayPlayerHand();
                    updateCardVisibility();
                    nextPlayerButton.setEnabled(false);
                }
                drawCardButton.setEnabled(true);
                revalidate();
                repaint();
            }
        });

        updateCurrentPlayerLabel();


        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = getData("Please Enter a filename to save this game as");
                SaveLoad save = new SaveLoad(fileName,players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
                save.save(fileName);
            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // String fileName = getData("Please Enter a filename retrive game frome");
                // SaveLoad oldGame = new SaveLoad(fileName);
                // oldGame.load(fileName);

                // players = oldGame.getPlayers();
                // currentPlayer = oldGame.getCurrentPlayer();
                // deck = oldGame.getDeck();
                // aiPlayers = oldGame.getAiPlayers();
                // flippedDeck =oldGame.getFlippedDeck();
                // currentDeck = oldGame.getCurrentDeck();
                // topCard = oldGame.getTopCard();
                // aiPlayer = oldGame.getAiPlayer();
                // playerCount = oldGame.getPlayerCount();
                // aiPlayerCount = oldGame.getAiPlayerCount();
                // numPlayersSelected = oldGame.getNumPlayersSelected();
                // currentPlayerIndex = oldGame.getCurrentPlayerIndex();

                // updateCurrentPlayerLabel();
                // displayPlayerHand();
                // updateCardVisibility();
                // displayTopCard();
                restartGame();

            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                System.out.println("Restarting!");


                players = initialGameState.getPlayers();
                currentPlayer = initialGameState.getCurrentPlayer();

                deck = initialGameState.getDeck();
                aiPlayers = initialGameState.getAiPlayers();
                flippedDeck = initialGameState.getFlippedDeck();
                currentDeck = initialGameState.getCurrentDeck();
                topCard = initialGameState.getTopCard();
                aiPlayer = initialGameState.getAiPlayer();
                playerCount = initialGameState.getPlayerCount();
                aiPlayerCount = initialGameState.getAiPlayerCount();
                numPlayersSelected = initialGameState.getNumPlayersSelected();
                currentPlayerIndex = initialGameState.getCurrentPlayerIndex();

               restartButton.setEnabled(false);

                updateCurrentPlayerLabel();
                displayPlayerHand();
                updateCardVisibility();
                displayTopCard();

            }
        });

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                redo = new SaveLoad("forthcoming",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
                System.out.println("Redo Triggered");


                players = undo.getPlayers();
                currentPlayer = undo.getCurrentPlayer();
                currentPlayer.addToHand(topCard);
                deck = undo.getDeck();
                aiPlayers = undo.getAiPlayers();
                flippedDeck = undo.getFlippedDeck();
                currentDeck = undo.getCurrentDeck();
                topCard = undo.getTopCard();
                aiPlayer = undo.getAiPlayer();
                playerCount = undo.getPlayerCount();
                aiPlayerCount = undo.getAiPlayerCount();
                numPlayersSelected = undo.getNumPlayersSelected();
                currentPlayerIndex = undo.getCurrentPlayerIndex();

                undoButton.setEnabled(false);
                //redoButton.setEnabled(true);
                updateCurrentPlayerLabel();
                displayPlayerHand();
                updateCardVisibility();
                displayTopCard();

            }
        });

        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                undo = new SaveLoad("previous",players, currentPlayer, deck, aiPlayers, flippedDeck, currentDeck, topCard, aiPlayer, playerCount, aiPlayerCount, numPlayersSelected, currentPlayerIndex);
                System.out.println("UndoTriggered");

                players = redo.getPlayers();
                currentPlayer = redo.getCurrentPlayer();
                currentPlayer.addToHand(topCard);
                deck = redo.getDeck();
                aiPlayers = redo.getAiPlayers();
                flippedDeck = redo.getFlippedDeck();
                currentDeck = redo.getCurrentDeck();
                topCard = redo.getTopCard();
                aiPlayer = redo.getAiPlayer();
                playerCount = redo.getPlayerCount();
                aiPlayerCount = redo.getAiPlayerCount();
                numPlayersSelected = redo.getNumPlayersSelected();
                currentPlayerIndex = redo.getCurrentPlayerIndex();

                //undoButton.setEnabled(true);
                //redoButton.setEnabled(false);
                updateCurrentPlayerLabel();
                displayPlayerHand();
                updateCardVisibility();
                displayTopCard();


            }
        });
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
        Card drawnCard = currentPlayer.drawCardFromDeck(deck);
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
            String playerType;


            if (isHumanTurn) {
                playerType = "Human";
                currentPlayerLabel.setText("Current Player: " + currentPlayer.getName() +  " (" + playerType + ")");
            }
            else{
                playerType = "AI";
                currentPlayerLabel.setText("Current Player: " + currentPlayer.getName() +  "(" + playerType + ")");


            }
            currentPlayerLabel.setText("Current Player: " + " " + playerType + " "+ currentPlayerIndex);
        } else {
            currentPlayerLabel.setText("Current Player: None");
        }
    }

    /**
     * Simulates a round of turns in the Uno game.
     */
    public void playRound() {
        while (!isRoundOver()) {
            // Iterate through all players
            for (Player currentPlayer : players) {
                // Check if the player is human or AI
                if (currentPlayer.isHuman(false)) {
                    // Human player's turn
                    currentPlayer.takeTurn(deck, topCard);
                } else {
                    // AI player's turn
                    playAITurn(aiPlayer);
                }

                // Check if the round is over
                if (isRoundOver()) {
                    break;
                }
            }
        }
    }



    /**
     * Simulates an AI player's turn in the Uno game.
     *
     * @param aiPlayer The AI player.
     */
    private void playAITurn(AIPlayer aiPlayer) {
        isHumanTurn = false;
        System.out.println(aiPlayer.getName() + "'s Turn.");
        System.out.println("Current side: " + topCard.getSuit());

        aiPlayer.displayHand();
        System.out.println("Top card: " + topCard);
        System.out.println("Score: " + aiPlayer.getPoints());

        // You can implement the AI logic here to decide which card to play or to draw
        Card selectedCard = aiPlayer.chooseCardToPlay(topCard);

        if (selectedCard != null) {
            // AI player plays a card
            String message = aiPlayer.getName() + " plays: " + selectedCard;
            JOptionPane.showMessageDialog(this, message);
            aiPlayer.removeFromHand(aiPlayer.getHand().indexOf(selectedCard));

            if (aiPlayer.getHand().size() == 1 || aiPlayer.getHand().isEmpty()) {
                aiPlayer.addPoint();
                String message2 = aiPlayer.getName() + " scores 1 point!";
                JOptionPane.showMessageDialog(this, message2);
            }

            // Handle special card effects if needed
            moveMade = true;
        } else {
            // AI player draws a card
            Card drawnCard = aiPlayer.drawCardFromDeck(deck);
            if (drawnCard != null) {
                String message = aiPlayer.getName() + " draws a card: " + drawnCard;
                JOptionPane.showMessageDialog(this, message);

                // Handle special card effects if needed

                moveMade = true;
            }
        }

        // Enable the nextPlayerButton after the AI player's turn
        nextPlayerButton.setEnabled(true);
        moveToNextPlayer();
    }

    /**
     * Checks if the round is over by verifying if all players have taken their turn.
     *
     * @return True if the round is over, false otherwise.
     */
    public boolean isRoundOver() {
        for (Player player : players) {
            if (!player.hasTakenTurn()) {
                return false;
            }
        }
        return true;
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

    private String getData(String prompt){
        String getword = "Medal";

        JTextField workspace = new JTextField("");
        workspace.setEditable(true);

        JOptionPane.showMessageDialog(this, workspace,
                prompt, JOptionPane.INFORMATION_MESSAGE);

        getword = workspace.getText();
        return getword.toLowerCase();
    }
}
