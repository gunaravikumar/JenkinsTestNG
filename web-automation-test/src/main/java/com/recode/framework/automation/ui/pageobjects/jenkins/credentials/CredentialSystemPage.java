package com.recode.framework.automation.ui.pageobjects.jenkins.credentials;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CredentialSystemPage extends PageClass {
    public CredentialSystemPage(RemoteWebDriver theWebDriver) {
        super(theWebDriver);
    }


    // Locator values here

    private By systemPageHeader() {
        return By.xpath("//h1[contains(text(), 'System')]");
    }

    private By globalCredentialsDomainLink() {
        return By.xpath("//a[contains(text(), 'Global credentials')]");
    }

    // Test Methods here

    /**
     * This Method is used to verify the title of system page in Jenkins Application
     */
    public boolean isSystemPageTitleExists() {
        pageLoadWait();
        return isElementPresent(systemPageHeader());
    }

    /**
     * This Method is used to click global credentials domain link in credential system page
     */
    public void clickGlobalCredentialsDomainLink() {
        clickByJS(globalCredentialsDomainLink());
    }
}
