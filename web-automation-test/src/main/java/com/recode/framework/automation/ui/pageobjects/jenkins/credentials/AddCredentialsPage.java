package com.recode.framework.automation.ui.pageobjects.jenkins.credentials;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class AddCredentialsPage extends PageClass {
    public AddCredentialsPage(RemoteWebDriver theWebDriver) {
        super(theWebDriver);
    }

    // Locator values here

    private By addCredentialsPageHeader() {
        return By.cssSelector("form[name='newCredentials']");
    }

    private By kindDropDown() {
        return By.cssSelector(".dropdownList.setting-input");
    }

    private By scopeDropDown() {
        return By.name("_.scope");
    }

    private By userNameTextBox() {
        return By.name("_.username");
    }

    private By passwordTextBox() {
        return By.name("_.password");
    }

    private By idTextBox() {
        return By.name("_.id");
    }

    private By descriptionTextBox() {
        return By.name("_.description");
    }

    private By submitButton() {
        return By.xpath("//button[text()='OK']");
    }

    private By privateKeyRadioButton() {
        return By.cssSelector("input[name*='privateKeySource']");
    }

    private By addButton() {
        return By.cssSelector("[value='Add']");
    }

    private By privateKeyTextArea() {
        return By.name("_.privateKey");
    }

    private By passPhraseTextBox() {
        return By.name("_.passphrase");
    }

    // Test Methods here

    public void selectKindDropDown(String option) {
        selectFromDropdownByVisibleText(kindDropDown(), option);
    }

    public void selectScopeDropDown(int scopeIndex) throws Exception {
        List<WebElement> getElements = findElements(scopeDropDown(),15);
        for (WebElement element : getElements) {
            if (element.isDisplayed()) {
                Select scopeDropDown = new Select(element);
                scopeDropDown.selectByIndex(scopeIndex);
            }
        }
    }

    public void enterUserName(String username) throws Exception {
        List<WebElement> getElements = findElements(userNameTextBox(),15);
        for (WebElement element : getElements) {
            if (element.isDisplayed()) {
                element.sendKeys(username);
            }
        }
    }

    public void enterPassword(String password) throws Exception {
        List<WebElement> getElements = findElements(passwordTextBox(), 15);
        for (WebElement element : getElements) {
            if (element.isDisplayed()) {
                element.sendKeys(password);
            }
        }
    }

    public void enterID(String id) throws Exception {
        List<WebElement> getElements = findElements(idTextBox(), 15);
        for (WebElement element : getElements) {
            if (element.isDisplayed()) {
                element.sendKeys(id);
            }
        }
    }

    public void enterDescription(String description) throws Exception {
        List<WebElement> getElements = findElements(descriptionTextBox(),15);
        for (WebElement element : getElements) {
            if (element.isDisplayed()) {
                element.sendKeys(description);
            }
        }
    }

    public void clickSubmitButton() {
        clickByJS(submitButton());
    }

    /**
     * This Method is used to verify the title of credential page in Jenkins Application
     */
    public boolean isNewCredentialPageTitleExists() {
        pageLoadWait();
        return isElementPresent(addCredentialsPageHeader());
    }

    public void selectPrivateKeyRadioButton() {
        selectRadioButton(privateKeyRadioButton(),true);
        boolean isSelected = isElementSelected(privateKeyRadioButton());
        if (isSelected) {
            Logger.instance.info("Selected Private Key Radio Button Successfully");
        } else {
            Logger.instance.error("Not Selected Private Key Radio Button");
        }
    }

    public void clickAddButton() {
        boolean isPresent = isElementPresent(addButton());
        if (isPresent) {
            clickByJS(addButton());
            Logger.instance.info("Clicked 'Add' Button in PrivateKey TextArea");
        } else {
            Logger.instance.error("Add Button is not visible in the PrivateKey TextArea");
        }
    }

    public void enterPrivateKey(String privateKey) throws Exception {
        sendKeys(privateKeyTextArea(), privateKey);
    }

    public void enterPassPhrase(String passPhrase) throws Exception {
        sendKeys(passPhraseTextBox(), passPhrase);
    }

}