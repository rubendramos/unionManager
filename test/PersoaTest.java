import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;
import java.text.DateFormat;


public class PersoaTest extends UnitTest {

	 @Before
	    public void setup() {
	        Fixtures.deleteAllModels();
                Fixtures.loadModels("data.yml");
	    }
	
	
    @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }
    
    @Test
    public void createAndRetrievePersoa() {
        // Create a new Persoa and save it
        
       Persoa p=Persoa.find("byDni", "11111111H").first();
        
       //Persoa bob = Persoa.find("byDni", "76574900B").first();
       ComunidadeAutonoma ca=ComunidadeAutonoma.all().first();
       Provincia pr=Provincia.all().first();
        
        //String a=ca.descricion;
       Sexo s=Sexo.all().first();
       Enderezo enderezo=Enderezo.all().first();
        
       Persoa p2=new Persoa(p.nome,p.apelido1,p.apelido2,p.dni,p.email,s,p.dataNacemento,enderezo,null).save();
        
       

        // Retrieve the Persoa with bob Persoaname
        

        // Test 
        assertNotNull(p);
        assertEquals("diaz ramos, ruben", p2.nomeCompleto);
    }

}
