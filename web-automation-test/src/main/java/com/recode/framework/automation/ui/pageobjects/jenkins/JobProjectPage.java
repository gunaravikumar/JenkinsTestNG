package com.recode.framework.automation.ui.pageobjects.jenkins;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class JobProjectPage extends PageClass {

    public JobProjectPage(RemoteWebDriver theWebDriver) {
        super(theWebDriver);
    }

    // Locator values here
    private By configureLink(){
        return By.xpath("//a[contains(.,'Configure')]");
    }
    private By workSpaceLink() {
        return By.xpath("//a[@title='Workspace']");
    }

    // Test Methods here
    /**
     * This Method is used to verify job project page title in Jenkins Application
     * @param projectName
     */
    public boolean isJobProjectPageTitleExists(String projectName) {
        pageLoadWait();
        String getConfigPageTitle = getPageTitle().trim();
        if (getConfigPageTitle.contains(projectName)) {
            Logger.instance.info("Job Project Page is displayed Successfully");
            return true;
        } else {
            Logger.instance.error("Failed to open Job Project Page");
        }
        return false;
    }

    /**
     * This method is to click on workspace link at Job Project Page in Jenkins Application
     *
     * @throws Exception
     */
    public void clickWorkSpaceLink() throws Exception {
        WebElement workSpace = findElement(workSpaceLink());
        scrollToElementByJS(workSpace);
        clickByJS(workSpaceLink());
    }

    /**
     * This Method is used to click on Configure link
     * @throws Exception
     */
    public void clickConfigure() throws Exception {
        click(configureLink());
    }
}
