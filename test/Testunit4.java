import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class Testunit4 {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();
	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:9000/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testUnit4() throws Exception {
		driver.get(baseUrl + "/");
		driver.findElement(By.linkText("Entrar")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("aa");
		driver.findElement(By.id("signin")).click();
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.linkText("Afiliación")).click();
		driver.findElement(By.cssSelector("input.add")).click();
		driver.findElement(By.cssSelector("img.new")).click();
		driver.findElement(By.id("addNovopersoa")).click();
		driver.findElement(By.id("object_dni")).clear();
		driver.findElement(By.id("object_dni")).sendKeys("76574900B");
		driver.findElement(By.id("object_nome")).clear();
		driver.findElement(By.id("object_nome")).sendKeys("Rubén");
		driver.findElement(By.id("object_apelido1")).clear();
		driver.findElement(By.id("object_apelido1")).sendKeys("Díaz");
		driver.findElement(By.id("object_apelido2")).clear();
		driver.findElement(By.id("object_apelido2")).sendKeys("Ramos");
		driver.findElement(By.id("object_email")).clear();
		driver.findElement(By.id("object_email")).sendKeys("rubendramos@gmail.com");
		new Select(driver.findElement(By.id("object_sexo"))).selectByVisibleText("Home");
		driver.findElement(By.id("object_dataNacemento")).click();
		driver.findElement(By.linkText("3")).click();
		driver.findElement(By.cssSelector("img.new")).click();
		driver.findElement(By.id("addNovoenderezo")).click();
		driver.findElement(By.cssSelector("img.add")).click();
		driver.findElement(By.id("addlocalidade")).click();
		driver.findElement(By.name("search")).clear();
		driver.findElement(By.name("search")).sendKeys("trazo");
		driver.findElement(By.cssSelector("input[type=\"submit\"]")).click();
		driver.findElement(By.cssSelector("input.engadir")).click();
		driver.findElement(By.id("object_enderezo")).clear();
		driver.findElement(By.id("object_enderezo")).sendKeys("Casa de rubén");
		driver.findElement(By.id("object_concello")).clear();
		driver.findElement(By.id("object_concello")).sendKeys("Trazo");
		driver.findElement(By.name("_saveAndBackForeign")).click();
		driver.findElement(By.name("_saveAndBackForeign")).click();
		new Select(driver.findElement(By.id("object_estadoAfiliado"))).selectByVisibleText("Alta");
		new Select(driver.findElement(By.id("object_ocupacion"))).selectByVisibleText("Cultivo de productos no perennes (OFICIOS VARIOS)");
		driver.findElement(By.id("object_dataAlta")).click();
		new Select(driver.findElement(By.id("object_ramo"))).selectByVisibleText("OFICIOS VARIOS");
		driver.findElement(By.id("object_dataAlta")).click();
		driver.findElement(By.linkText("2")).click();
		driver.findElement(By.id("object_dataBaixa")).click();
		driver.findElement(By.id("object_milita")).click();
		driver.findElement(By.id("object_milita")).click();
		driver.findElement(By.name("_save")).click();
		driver.findElement(By.cssSelector("input.delete")).click();
		// ERROR: Caught exception [ERROR: Unsupported command [getConfirmation]]
		driver.findElement(By.cssSelector("li.inOut > a > b")).click();
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}
