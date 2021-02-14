package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;
	public String baseURL;

	public static WebDriver driver;
	public WebDriverWait wait;

	public HomePage homePage;
	public LoginPage loginPage;
	public SignupPage signupPage;

	String firstName = "Bob";
	String lastName = "Lee";
	String userName = "snow";
	String password = "password";

	@BeforeAll
	public static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = "http://localhost:" + port;
		wait = new WebDriverWait(driver, 100);
	}

	@AfterAll
	public static void afterAll() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void testPublicAccess() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/result");
		Assertions.assertEquals("Login", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/signup");
        Assertions.assertEquals("Sign Up", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/login");
        Assertions.assertEquals("Login", driver.getTitle());
	}

	@Test
	@Order(2)
	public void testSignUpLoginLogout() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/signup");

        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup(firstName, lastName, userName, password);
        Assertions.assertEquals("Sign Up", driver.getTitle());

        driver.get("http://localhost:" + this.port + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(userName, password);
        Assertions.assertEquals("Home", driver.getTitle());

		HomePage homePage = new HomePage(driver);
		driver.get("http://localhost:" + this.port + "/logout");
        wait.until(ExpectedConditions.titleContains("Login"));
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertEquals("Login", driver.getTitle());
	}

    @Test
    @Order(3)
	public void testAddEditDeleteNote() throws InterruptedException {
        driver.get("http://localhost:" + this.port + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(userName, password);
        Assertions.assertEquals("Home", driver.getTitle());

        HomePage homePage = new HomePage(driver);
        WebElement tab = driver.findElement(By.id("nav-notes-tab"));
        tab.click();
        homePage.addNote(driver, "note1", "this is note1");
		driver.get("http://localhost:" + this.port + "/home");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
        Note firstNote = homePage.getFirstNote(driver);

        Assertions.assertEquals("note1", firstNote.getNoteTitle());
        Assertions.assertEquals("this is note1", firstNote.getNoteDescription());

		homePage.editNote(driver, "newnote1", "this is newnote1");
		driver.get("http://localhost:" + this.port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		Note updatedFirstNote = homePage.getFirstNote(driver);

		Assertions.assertEquals("newnote1", updatedFirstNote.getNoteTitle());
		Assertions.assertEquals("this is newnote1", updatedFirstNote.getNoteDescription());

		wait.until(ExpectedConditions.elementToBeClickable(By.id("closeNoteModal"))).click();
		homePage.deleteNote(driver);
		driver.get("http://localhost:" + this.port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		assertTrue(driver.findElements(By.id("noteTitle")).isEmpty());
    }
}
