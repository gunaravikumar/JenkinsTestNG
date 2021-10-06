package com.recode.framework.automation.ui.pageobjects.jenkins.sequences;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.pageobjects.jenkins.credentials.AddCredentialsPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.credentials.GlobalCredentialsPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.homepage.HomePage;
import com.recode.framework.automation.ui.pageobjects.jenkins.login.LoginPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.users.UsersPage;
import com.recode.framework.automation.ui.utils.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SequenceActions extends PageClass {

    LoginPage loginPage;
    HomePage homePage;
    UsersPage userPage;
    AddCredentialsPage addCredentialsPage;
    GlobalCredentialsPage globalCredentialsPage;

    public SequenceActions(RemoteWebDriver theWebDriver) {
        this.theWebDriver = theWebDriver;
        loginPage = new LoginPage(theWebDriver);
        homePage = new HomePage(theWebDriver);
        userPage = new UsersPage(theWebDriver);
        addCredentialsPage = new AddCredentialsPage(theWebDriver);
        globalCredentialsPage = new GlobalCredentialsPage(theWebDriver);
    }

    /**
     * This Method is used to login Jenkins using valid credentials
     *
     * @param username - username to be entered
     * @param password - password to be entered
     * @throws Exception
     */
    public void loginJenkins(String username, String password) throws Exception {
        loginPage.enterLoginID(username);
        loginPage.enterPassword(password);
        loginPage.clickSignInButton();
        loginPage.pageLoadWait();
    }

    /**
     * This method is to pass the values to create an user in jenkins
     * @param userName
     * @param password
     * @param confirmPassword
     * @param fullName
     * @param emailAddress
     * @throws Exception
     */
    public void createUserInJenkins(String userName, String password, String confirmPassword, String fullName, String emailAddress) throws Exception {
        userPage.enterUsername(userName);
        userPage.enterPassword(password);
        userPage.enterConfirmPassword(confirmPassword);
        userPage.enterFullName(fullName);
        userPage.enterEmail(emailAddress);
        userPage.clickCreateUserButton();
    }

    /**
     * This Method is used to create a new credentials for user.
     * @param option - kind to be selected
     * @param scopeIndex - scopeIndex to be selected
     * @param userName - username to be entered
     * @param password - password to be entered
     * @param id - id to be entered
     * @param description - description to be entered
     * @throws Exception
     */
    public void addCredentials(String option, int scopeIndex, String userName, String password, String id, String description) throws Exception {
        try {
            addCredentialsPage.selectKindDropDown(option);
            addCredentialsPage.selectScopeDropDown(scopeIndex);
            addCredentialsPage.enterUserName(userName);
            addCredentialsPage.enterPassword(password);
            addCredentialsPage.enterID(id);
            addCredentialsPage.enterDescription(description);
            addCredentialsPage.clickSubmitButton();
        } catch (Exception exp) {
            Logger.instance.error("Error while executing 'addCredentials' method : " + exp);
        }
    }

    /**
     * This Method is used to delete a created credentials for user.
     * @param id - id to be entered
     * @throws Exception
     */
    public void deleteCredentials(String id) throws Exception {
        try {
            globalCredentialsPage.clickGlobalCredentialByID(id);
            pageLoadWait();
            globalCredentialsPage.deleteCredentialLink();
            pageLoadWait();
            globalCredentialsPage.clickYesButton();
            pageLoadWait();
        } catch (Exception exp) {
            Logger.instance.error("Error while executing 'deleteCredentials' method : " + exp);
        }
    }

    /**
     * This Method is used to create a new credentials for user type SSH Username With Private Key.
     * @param option - kind to be selected
     * @param scopeIndex - scopeIndex to be selected
     * @param id - id to be entered
     * @param description - description to be entered
     * @param userName - username to be entered
     * @param privateKey - privateKey to be entered
     * @param passPhrase - passPhrase to be entered
     * @throws Exception
     */
    public void addNewCredentialTypeSSHUsernameWithPrivateKey(String option, int scopeIndex, String id, String description, String userName, String privateKey, String passPhrase) {
        try {
            addCredentialsPage.selectKindDropDown(option);
            addCredentialsPage.selectScopeDropDown(scopeIndex);
            addCredentialsPage.enterID(id);
            addCredentialsPage.enterDescription(description);
            addCredentialsPage.enterUserName(userName);
            addCredentialsPage.selectPrivateKeyRadioButton();
            addCredentialsPage.clickAddButton();
            addCredentialsPage.enterPrivateKey(privateKey);
            addCredentialsPage.enterPassPhrase(passPhrase);
            addCredentialsPage.clickSubmitButton();
        } catch (Exception exp) {
            Logger.instance.error("Error while executing 'addNewCredentialTypeSSHUsernameWithPrivateKey' method : " + exp);
        }
    }
}
