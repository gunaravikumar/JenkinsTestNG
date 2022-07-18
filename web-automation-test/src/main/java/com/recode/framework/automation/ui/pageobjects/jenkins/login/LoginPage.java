package com.recode.framework.automation.ui.pageobjects.jenkins.login;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class LoginPage extends PageClass {

    public LoginPage(RemoteWebDriver theWebDriver)
    {
        super(theWebDriver);
    }

    //Locator values here

    private By usernameTextField() {
        return By.id("j_username");
    }

    private By passwordTextField() {
        return By.name("j_password");
    }

    private By signInButton() {
        return By.name("Submit");
    }

    //Test Methods here

    /**
     * This Method is used to type in the login ID for Jenkins Application
     * @param username - username needs to be entered
     * @throws Exception
     */
    public void enterLoginID(String username) throws  Exception {
        clickByJS(usernameTextField());
        Utils.sleep(1);
        sendKeys(usernameTextField(), username);
    }

    /**
     * This Method is used to type in the password for Jenkins Application
     * @param password - username needs to be entered
     * @throws Exception
     */
    public void enterPassword(String password) throws  Exception {
        clickByJS(usernameTextField());
        Utils.sleep(1);
        sendKeys(passwordTextField(), password);
    }

    /**
     * This Method is used to click the Sign in button in Jenkins Login page
     * @throws Exception
     */
    public void clickSignInButton() throws Exception {
        click(signInButton());
    }

}
