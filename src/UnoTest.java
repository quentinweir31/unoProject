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


    /**
     * This method is executed once after all the test methods.
     * It performs any necessary cleanup after all the tests have been executed.
     */

    @AfterAll
    private static void tearDown() {
        System.out.print("All tests are done");
    }

    /**
     * Test case to verify the default constructor of the Heater class.
     * It checks if the default temperature is set to 15 degrees.
     */
    @Test
    public void test_DefaultConstructor() {
    }
    

}
