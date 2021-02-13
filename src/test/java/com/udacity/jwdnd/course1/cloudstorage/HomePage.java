package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import static java.lang.Integer.parseInt;

public class HomePage {
    @FindBy(id="logoutButton")
    private WebElement logoutButton;

    @FindBy(id="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id="nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id="add-note-button")
    private WebElement addNoteButton;

    @FindBy(id="noteTitle")
    private List<WebElement> noteTitleList;

    @FindBy(id="noteDescription")
    private List<WebElement> noteDescriptionList;

    @FindBy(id="note-id")
    private WebElement noteId;

    @FindBy(id="noteTitleInput")
    private WebElement noteTitleInput;

    @FindBy(id="noteDescriptionInput")
    private WebElement noteDescriptionInput;

    @FindBy(id="saveNoteChanges")
    private WebElement noteSubmitButton;

    @FindBy(id="edit-note")
    private List<WebElement> editNoteButton;

    @FindBy(id="delete-note")
    private List<WebElement> deleteNoteButton;

    @FindBy(id="add-credential-button")
    private WebElement addCredentialButton;

    @FindBy(id="edit-credential-button")
    private List<WebElement> editCredentialButton;

    @FindBy(id="delete-credential-button")
    private List<WebElement> deleteCredentialButton;

    @FindBy(id="credential-url")
    private List<WebElement> credentialUrlList;

    @FindBy(id="credential-username")
    private List<WebElement> credentialUsernameList;

    @FindBy(id="credential-password")
    private List<WebElement> credentialPasswordList;

    @FindBy(id="credential-id")
    private WebElement credentialIdInput;

    @FindBy(id="credentialUrl")
    private WebElement credentialUrlInput;

    @FindBy(id="credentialUsername")
    private WebElement credentialUsernameInput;

    @FindBy(id="credentialPassword")
    private WebElement credentialPasswordInput;

    @FindBy(id="saveCredentialChanges")
    private WebElement saveCredentialButton;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public Note getFirstNote(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.visibilityOf(notesTab)).click();
        Note firstNote = new Note(parseInt(noteId.getText()), noteTitleList.get(0).getText(), noteDescriptionList.get(0).getText(), 1);
        return firstNote;
    }

    public void addNote(WebDriver driver, String title, String description, WebElement nav){
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            wait.until(ExpectedConditions.visibilityOf(notesTab)).click();
        } catch (TimeoutException ex) {
            System.out.println("Timeout Exception");
            nav.click();
            wait.until(ExpectedConditions.visibilityOf(notesTab)).click();
        }
        wait.until(ExpectedConditions.visibilityOf(addNoteButton)).click();

        wait.until(ExpectedConditions.visibilityOf(noteTitleInput)).sendKeys(title);
        wait.until(ExpectedConditions.visibilityOf(noteDescriptionInput)).sendKeys(description);

        wait.until(ExpectedConditions.visibilityOf(noteSubmitButton)).click();

        wait.until(ExpectedConditions.visibilityOf(notesTab)).click();
    }

    public void editNote(WebDriver driver, String title, String description, WebElement nav){
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(notesTab)).click();
        wait.until(ExpectedConditions.visibilityOf((editNoteButton.get(0)))).click();

        wait.until(ExpectedConditions.visibilityOf(noteTitleInput));
        noteTitleInput.clear();
        noteTitleInput.sendKeys(title);
        wait.until(ExpectedConditions.visibilityOf(noteDescriptionInput));
        noteTitleInput.clear();
        noteTitleInput.sendKeys(description);

        wait.until(ExpectedConditions.visibilityOf(noteSubmitButton)).click();
        wait.until(ExpectedConditions.visibilityOf(notesTab)).click();
    }

    public void deleteNote(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(notesTab)).click();

        wait.until(ExpectedConditions.visibilityOf(deleteNoteButton.get(0))).click();
    }

    public String encryptValue(String data, String key) {
        byte[] encryptedValue = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedValue = cipher.doFinal(data.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException
                | UnsupportedEncodingException | IllegalBlockSizeException | BadPaddingException e) {
            return e.getMessage();
        }

        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public Credential getCredential(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try {
            Thread.sleep(1000);
        } catch(InterruptedException e){
            e.printStackTrace();
        }
        wait.until(ExpectedConditions.visibilityOf(credentialsTab)).click();
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String newKey = encryptValue(credentialPasswordList.get(0).getText(),encodedKey);
        Credential firstCredential = new Credential(parseInt(credentialIdInput.getText()), credentialUrlList.get(0).getText(),
                credentialUsernameList.get(0).getText(), newKey, credentialPasswordList.get(0).getText(), 1);
        return firstCredential;
    }

    public void addCredential(WebDriver driver, String url, String username, String password, WebElement nav){
        WebDriverWait wait = new WebDriverWait(driver, 10);

        try {
            wait.until(ExpectedConditions.visibilityOf(credentialsTab)).click();
        } catch (TimeoutException ex) {
            System.out.println("Timeout Exception");
            nav.click();
            wait.until(ExpectedConditions.visibilityOf(credentialsTab)).click();
        }
        wait.until(ExpectedConditions.visibilityOf(addCredentialButton)).click();

        wait.until(ExpectedConditions.visibilityOf(credentialUrlInput)).sendKeys(url);
        wait.until(ExpectedConditions.visibilityOf(credentialUsernameInput)).sendKeys(username);
        wait.until(ExpectedConditions.visibilityOf(credentialPasswordInput)).sendKeys(password);

        wait.until(ExpectedConditions.visibilityOf(saveCredentialButton)).click();
        wait.until(ExpectedConditions.visibilityOf(credentialsTab)).click();
    }

    public void editCredential(WebDriver driver, String url, String username, String password, WebElement nav){
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(credentialsTab)).click();
        wait.until(ExpectedConditions.visibilityOf((editCredentialButton.get(0)))).click();

        wait.until(ExpectedConditions.visibilityOf(credentialUrlInput));
        noteTitleInput.clear();
        noteTitleInput.sendKeys(url);
        wait.until(ExpectedConditions.visibilityOf(credentialUsernameInput));
        noteTitleInput.clear();
        noteTitleInput.sendKeys(username);
        wait.until(ExpectedConditions.visibilityOf(credentialPasswordInput));
        noteTitleInput.clear();
        noteTitleInput.sendKeys(password);

        wait.until(ExpectedConditions.visibilityOf(saveCredentialButton)).click();
        wait.until(ExpectedConditions.visibilityOf(credentialsTab)).click();
    }

    public void deleteCredential(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOf(credentialsTab)).click();

        wait.until(ExpectedConditions.visibilityOf(deleteCredentialButton.get(0))).click();
    }

    public void logout() {
        logoutButton.click();
    }
}
