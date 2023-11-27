import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class UnoTest {
    /**a
     * Default constructor for the UnoTest class.
     */
    public UnoTest() {
    }
    
    /**
     * This method is executed once before all the test methods.
     * It sets up the initial state of the test environment.
     */
    @BeforeAll
    private static void setUp() {
    }

    //Tests pertaining to Player 
    /**
     * Test Case to verify player creation via the consructor 
     */
    @Test
    public void test_PlayerConstructor() {
        Player p1 = new Player("Test_Player");
        ArrayList testHand = new ArrayList<>();
        assertEquals(p1.getName(), "Test_Player");
        assertEquals(p1.getHand(), testHand);
    }

    
    /**
     * Test Case to verify if hand actions such as add/ remove and draw card work
     */

    @Test
    public void test_PlayerHandActions() {
        //Testing add to hand, remove from hand/ draw car
        //by creatign a hand, card and deck, and calling the add/remove draw methods

        Player p1 = new Player("Test_Player");
        
        //Creating a sample hand to test against
        Card sample_card = new Card(Card.Rank.FIVE, Card.Suit.YELLOW);
        ArrayList<Card> testHand = new ArrayList<Card>();
        
        
        //adding a card to both the player's hand and the test, verifying that the card is added 
        testHand.add(sample_card);
        p1.addToHand(sample_card); 
        assertEquals(p1.getHand().get(0), testHand.get(0));

        //removing the added card and verifying that the hands are empty 
        p1.removeFromHand(0);
        testHand.remove(sample_card);
        assertEquals(p1.getHand(), testHand);

    }


    /**
     * Test Case to verify if the system draws a card from the Deck.
     */

    @Test
    public void test_DrawCardFromDeck() {
        Player player = new Player("TestPlayer");
        assertNotNull(player);

        List<Card> deck = new ArrayList<>();
        deck.add(new Card(Card.Rank.DRAW2, Card.Suit.RED));

        Card drawnCard = player.drawCardFromDeck(deck);

        assertNotNull(drawnCard);
        assertEquals(1, player.getHand().size());
        assertEquals(drawnCard, player.getHand().get(0));
    }

    /**
     * Test case checks whether a player's play is considered valid based on the provided top card.
     * The method should return true if the selected card has either the same rank or suit as the top card,
     * and false if otherwise.
     */
    @Test
    void test_IsValidPlay() {
        Player player = new Player("TestPlayer");
        Card topCard = new Card(Card.Rank.DEUCE, Card.Suit.RED);
        Card matchingCard = new Card(Card.Rank.DEUCE, Card.Suit.BLUE);
        Card nonMatchingCard = new Card(Card.Rank.THREE, Card.Suit.GREEN);

        assertTrue(player.isValidPlay(matchingCard, topCard));
        assertFalse(player.isValidPlay(nonMatchingCard, topCard));
    }

    /**
     * Test case verifies whether a card is correctly played from the player's hand based on the provided index.
     * The method should return the card at the specified index, and the card should be removed from the player's hand.
     */
    @Test
    void test_PlayCard() {
        Player player = new Player("test-Player");
        Card card = new Card(Card.Rank.DEUCE, Card.Suit.RED);
        player.addToHand(card);
        Card playedCard = player.playCard(0);
        assertNotNull(playedCard);
        assertEquals(card, playedCard);
    }

    /**
     * Test Case to verify if it's a valid play with a matching rank
     */

    @Test
    public void test_IsValidPlayWithMatchingRank() {
        Player player = new Player("TestPlayer");

        Card selectedCard = new Card(Card.Rank.DRAW2, Card.Suit.RED);
        Card topCard = new Card(Card.Rank.DRAW2, Card.Suit.BLUE);

        assertTrue(player.isValidPlay(selectedCard, topCard));
    }


    /**
     * Test Case to verify if it's a valid play with a matching suit
     */
    @Test
    public void test_IsValidPlayWithMatchingSuit() {
        Player player = new Player("TestPlayer");

        Card selectedCard = new Card(Card.Rank.DRAW2, Card.Suit.RED);
        Card topCard = new Card(Card.Rank.SKIP, Card.Suit.RED);

        assertTrue(player.isValidPlay(selectedCard, topCard));
    }

    /**
     * Test Case to verify if it's a valid play with a mismatched rank and suit
     */

    @Test
    public void test_IsValidPlayWithMismatchedRankAndSuit() {
        Player player = new Player("TestPlayer");

        Card selectedCard = new Card(Card.Rank.DRAW2, Card.Suit.RED);
        Card topCard = new Card(Card.Rank.SKIP, Card.Suit.BLUE);

        assertFalse(player.isValidPlay(selectedCard, topCard));
    }

    /**
     * Test case to verify that the createDeck method in the UnoGUI class generates a
     * non-null deck and checks its size to ensure it conforms to the expected size
     * of a standard Uno deck.

     */
    @Test
    void test_CreateDeck() {
        UnoGUI unoGUI = new UnoGUI();
        assertNotNull(unoGUI.createDeck());
        assertEquals(56, unoGUI.createDeck().size()); // Assuming a standard Uno deck
    }

    /**
     * Test case to ensure that the shuffleDeck method in the UnoGUI class shuffles
     * the provided deck and results in a deck that is not equal to the original
     * un-shuffled deck.
     */
    @Test
    void test_ShuffleDeck() {
        UnoGUI unoGUI = new UnoGUI();
        List<Card> originalDeck = unoGUI.createDeck();
        List<Card> shuffledDeck = unoGUI.createDeck();
        unoGUI.shuffleDeck(shuffledDeck);
        assertNotEquals(originalDeck, shuffledDeck);
    }

    /**
     * Test case Verifies that the correct number of players is created and added to the game.
     */
    @Test
    void test_InitializePlayers() {
        UnoGUI unoGUI = new UnoGUI();
        unoGUI.initializePlayers(2);
        assertEquals(2, unoGUI.getPlayers().size());
    }


    @Test
    void determineMoveTest() {
        // Create an AIPlayer
        AIPlayer aiPlayer = new AIPlayer("AIman");

        // Create a hand for the AIPlayer
        List<Card> hand = new ArrayList<>();
        hand.add(new Card(Card.Rank.FIVE, Card.Suit.RED));
        hand.add(new Card( Card.Rank.FOUR, Card.Suit.BLUE));
        hand.add(new Card(Card.Rank.NINE, Card.Suit.BLUE));
        aiPlayer.setHand(hand);
        // Test when there are no legal moves available
        aiPlayer.setHand(new ArrayList<>());
        assertNull(AIPlayer.determineMove());
    }



/**
 * This method is executed once after all the test methods.
 * It performs any necessary cleanup after all the tests have been executed.
 */

    @AfterAll
    private static void tearDown() {
        System.out.print("All tests are done");
    }

}
