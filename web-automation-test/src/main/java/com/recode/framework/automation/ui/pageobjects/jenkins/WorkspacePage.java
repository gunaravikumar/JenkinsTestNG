package com.recode.framework.automation.ui.pageobjects.jenkins;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WorkspacePage extends PageClass {

    public WorkspacePage(RemoteWebDriver theWebDriver) {
        super(theWebDriver);
    }

    // Locator values here

    private By workspacePageHeader(String projectName) {
        return By.xpath("//h1[contains(text(), 'Workspace of " + projectName + "')]");
    }

    private By allFilesInZipLink(String projectName) {
        return By.xpath("//a[@href='./*zip*/" + projectName + ".zip']");
    }

    // Test Methods here

    /**
     * This Method is used to verify workspace page title in Jenkins Application
     * @param projectName - project name
     */
    public boolean isWorkspacePageTitleExists(String projectName) {
        pageLoadWait();
        return isElementPresent(workspacePageHeader(projectName));
    }

    /**
     * This method is to click on allFilesInZip link at Job Project Page in Jenkins Application
     */
    public void clickAllFilesInZipLink(String projectName) {
        clickByJS(allFilesInZipLink(projectName));
    }
}
