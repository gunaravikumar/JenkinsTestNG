package com.recode.framework.automation.ui.pageobjects.jenkins.credentials;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CredentialsPage extends PageClass {
    public CredentialsPage(RemoteWebDriver theWebDriver) {
        super(theWebDriver);
    }

    /**
     * Locator values here
     */
    private By credentialsPageHeader() {
        return By.xpath("//h1[contains(text(), 'Credentials')]");
    }

    private By storesScopedTable() {
        return By.xpath("//div[@id='main-panel']/table[3]");
    }

    /*
      Test Methods here
     */

    /**
     * This Method is used to verify the title of new credential page in Jenkins Application
     */
    public boolean isCredentialsPageTitleExists() {
        pageLoadWait();
        return isElementPresent(credentialsPageHeader());
    }

    /**
     * This Method is used to select store column value from stores scoped table in credentials page
     */
    public void selectStoreColumnValueFromStoresScopedTable(String selectStoreValue) {
        clickColumnValueInTable(storesScopedTable(), "Store", selectStoreValue);
    }
}