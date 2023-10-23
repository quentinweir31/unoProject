import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class UnoTest {
    /**
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
     * Test Case to verify the display Hand method works appropriralety 
     */
    @Test
    public void test_PlayerDisplayHand() {
        //create a player with a hand and display it, assert if the values are correct
        Player p1 = new Player("Test_Player");
        
        //Creating a sample hand to test against
        Card sample_card = new Card(Card.Rank.FIVE, Card.Suit.YELLOW);
        ArrayList<Card> testHand = new ArrayList<Card>();
        
        
        //adding a card to both the player's hand and the test, verifying that the card is added 
        testHand.add(sample_card);
        p1.addToHand(sample_card); 
        p1.displayHand();
        assertEquals(p1.getHand(), testHand);
    }

     /**
     * Test Case to verify if the card constrcutor creates regular cards 
     */
    @Test
    public void test_CardConstructor_card() {
        Card sample_card = new Card(Card.Rank.THREE, Card.Suit.RED);

        assertEquals(sample_card.getRank(),Card.Rank.THREE);
        assertEquals(sample_card.getSuit(), Card.Suit.RED);

    }
    
     /**
     * Test Case to verify if the card constrcutor creates wild cards 
     */
    @Test
    public void test_CardConstructor_wild() {
        Card sample_card = new Card("QC");

        assertEquals(sample_card.getRank(),Card.Rank.WILD);
        assertEquals(sample_card.getSuit(), Card.Suit.RED);

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
