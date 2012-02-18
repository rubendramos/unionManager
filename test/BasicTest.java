import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;
import java.text.DateFormat;


public class BasicTest extends UnitTest {

	 @Before
	    public void setup() {
	        Fixtures.deleteAll();
	    }
	
	
    @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }
    
    @Test
    public void createAndRetrievePersoa() {
        // Create a new Persoa and save it
        new Persoa("ruben","diaz","ramos","76574900B","rubendramos@gmail.com",new Sexo("H"),
        		new Date("29/04/1974"),new Enderezo("sdffsa","rwer","423","234","323","ewe")).save();

        // Retrieve the Persoa with bob Persoaname
        Persoa bob = Persoa.find("byDni", "76574900B").first();

        // Test 
        assertNotNull(bob);
        assertEquals("diaz ramos, ruben", bob.nomeCompleto);
    }

}
