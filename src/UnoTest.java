import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

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
    }

    
    /**
     * Test Case to verify if hand actions such as add/ remove and draw card work
     */
    @Test
    public void test_PlayerHandActions() {
        //Testing add to hand, remove from hand/ draw car
        //by creatign a hand, card and deck, and calling the add/remove draw methods
    }


    /**
     * Test Case to verify player can draw a card from the deck
     */
    @Test
    public void test_PlayerDrawfromDeck() {
        //create a deck, a use an empty hand and draw a card into it
    }

    /**
     * Test Case to verify the display Hand method works appropriralety 
     */
    @Test
    public void test_PlayerDisplayHand() {
        //create a player with a hand and display it, assert if the values are correct
    }
    /**
     * Test Case to simulate the player taking a turn 
     */
    @Test
    public void test_PlayerTakeTurn() {
        //simulate play
    }

     /**
     * Test Case to verify if the card constrcutor creates regular cards 
     */
    @Test
    public void test_CardConstructor_card() {
    }
    
     /**
     * Test Case to verify if the card constrcutor creates wild cards 
     */
    @Test
    public void test_CardConstructor_wild() {
    }

     /**
     * Test Case to verify the creation of a deck
     */
    @Test
    public void test_DeckConstructor() {
    }
    
     /**
     * Test Case to verify the DeckShuffle method shuffles the deck
     */
    @Test
    public void test_DeckShuffle() {
    }

    /**
     * Test Case to verify if the printDeck method prints the deck correctly
     */
    @Test
    public void test_DeckPrintDeck() {
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
