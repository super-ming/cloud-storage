package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {
    @FindBy(css="#success-redirect")
    private WebElement successRedirect;
    @FindBy(css="#error-redirect")
    private WebElement errorRedirect;
    @FindBy(css="#no-file-redirect")
    private WebElement noFileRedirect;
    @FindBy(css="#file-name-redirect")
    private WebElement fileNameRedirect;
    @FindBy(css="#bad-credentials-redirect")
    private WebElement badCredentialsRedirect;


    public ResultPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void redirect(WebElement linkElement) {
        linkElement.click();
    }
}
