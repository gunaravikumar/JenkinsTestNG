package com.recode.framework.automation.ui.pageobjects.jenkins.credentials;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class GlobalCredentialsPage extends PageClass {
    public GlobalCredentialsPage(RemoteWebDriver theWebDriver) {
        super(theWebDriver);
    }

    /**
     * Locator values here
     */
    private By globalCredentialsPageHeader() {
        return By.xpath("//h1[contains(text(), 'Global credentials')]");
    }

    private By addCredentialsLink() {
        return By.xpath("//a[@title='Add Credentials']");
    }

    private By globalCredentialsTable() {
        return By.xpath("//table[@class='sortable pane bigtable']");
    }

    private By deleteCredentialsLink() {
        return By.xpath("//a[@title='Delete']");
    }

    private By yesButton() {
        return By.xpath("//button[text()='Yes']");
    }


     // Test Methods here

    /**
     * This Method is used to verify the title of new credential page in Jenkins Application
     */
    public boolean isGlobalCredentialsPageTitleExists() {
        pageLoadWait();
        return isElementPresent(globalCredentialsPageHeader());
    }

    /**
     * This Method is used to click on the Add Credentials Menu at GlobalCredentialsPage in Jenkins Application
     *
     * @throws Exception - Error handling
     */
    public void clickAddCredentialLink() throws Exception {
        clickByJS(addCredentialsLink());
    }

    /**
     * This Method is used to click on delete credentials menu at GlobalCredentialsPage in Jenkins Application
     *
     * @throws Exception - Error handling
     */
    public void deleteCredentialLink() throws Exception {
        clickByJS(deleteCredentialsLink());
        Logger.instance.info("Clicked Delete Credential Link Successfully.");
    }

    /**
     * This Method is used to click on yes button for delete credential at GlobalCredentialsPage in Jenkins Application
     *
     */
    public void clickYesButton() {
        clickByJS(yesButton());
    }

    /**
     * This Method is used to verify newly created credentials by ID in Global Credentials page
     */
    public boolean isVerifyCredentialInGlobalCredentialPageTableByID(String setID) {
        try {
            String[] columnValues = getColumnValues(globalCredentialsTable(), "ID");
            for (String value : columnValues) {
                if (value.equals(setID)) {
                    Logger.instance.info(setID + " ID is exists in GlobalCredentials Table.");
                    return true;
                }
            }
            Logger.instance.info(setID + ": ID is not exists in GlobalCredentials Table.");
        } catch (Exception exp) {
            Logger.instance.error("Error while executing 'isVerifyCredentialInGlobalCredentialPageTableByID' method : " + exp);
        }
        return false;
    }

    /**
     * This Method is used to click created credentials by ID in Global Credentials page
     */
    public void clickGlobalCredentialByID(String setID){
        clickColumnValueInTable(globalCredentialsTable(), "ID", setID);
    }
}