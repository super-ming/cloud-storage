package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
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

import java.beans.Visibility;
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

		wait.until(ExpectedConditions.elementToBeClickable(By.id("success-redirect"))).click();
		driver.get("http://localhost:" + this.port + "/home");

        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
        Note firstNote = homePage.getFirstNote(driver);

		String noteTitle = driver.findElements(By.id("noteTitle")).get(0).getText();
		String noteDescription = driver.findElements(By.id("noteDescription")).get(0).getText();

        Assertions.assertEquals("note1", noteTitle);
        Assertions.assertEquals("this is note1", noteDescription);

		homePage.editNote(driver, "newnote1", "this is newnote1");

		driver.get("http://localhost:" + this.port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		Note updatedFirstNote = homePage.getFirstNote(driver);

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("closeNoteModal"))));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("closeNoteModal"))).click();

		String updatedNoteTitle = driver.findElements(By.id("noteTitle")).get(0).getText();
		String updatedNoteDescription = driver.findElements(By.id("noteDescription")).get(0).getText();

		Assertions.assertEquals("newnote1", updatedNoteTitle);
		Assertions.assertEquals("this is newnote1", updatedNoteDescription);


		homePage.deleteNote(driver);
		driver.get("http://localhost:" + this.port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		assertTrue(driver.findElements(By.id("noteTitle")).isEmpty());
    }

	@Test
	@Order(4)
	public void testAddEditDeleteCredential() throws InterruptedException {
		driver.get("http://localhost:" + this.port + "/home");
		HomePage homePage = new HomePage(driver);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("nav-credentials-tab"))));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();

		homePage.addCredential(driver, "cred.com", "test1", "password");

		driver.get("http://localhost:" + this.port + "/home");
        homePage = new HomePage(driver);

		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("nav-credentials-tab"))));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();

        List<WebElement> urlTextList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("credential-url")));
        List<WebElement> userNameTextList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("credential-username")));
        List<WebElement> passwordTextList = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.id("credential-password")));

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

		Assertions.assertEquals("cred.com", urlTextList.get(0).getText());
		Assertions.assertEquals("test1", userNameTextList.get(0).getText());
		Assertions.assertNotEquals("password", passwordTextList.get(0).getText());

		wait.until(ExpectedConditions.elementToBeClickable(driver.findElements(By.id("edit-credential-button")).get(0))).click();

		homePage.editCredential(driver, "url.com", "test2", "secret");
		driver.get("http://localhost:" + this.port + "/home");
        homePage = new HomePage(driver);

        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("nav-credentials-tab"))));
		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();

        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }

		String updatedUrl = driver.findElements(By.id("credential-url")).get(0).getText();
		String updatedUserName = driver.findElements(By.id("credential-username")).get(0).getText();
		String updatedPassword = driver.findElements(By.id("credential-password")).get(0).getText();

		Assertions.assertEquals("url.com", updatedUrl);
		Assertions.assertEquals("test2", updatedUserName);
		Assertions.assertNotEquals("secret", updatedPassword);

		homePage.deleteCredential(driver);
		driver.get("http://localhost:" + this.port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		assertTrue(driver.findElements(By.id("credential-url")).isEmpty());

	}
}
