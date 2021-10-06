package com.recode.framework.automation.ui.pageobjects.jenkins.managejenkins;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ManageJenkinsPage extends PageClass {

    public ManageJenkinsPage(RemoteWebDriver theWebDriver)
    {
        super(theWebDriver);
    }

    //Locator values here
    private By managePageHeader() {
        return By.xpath("//h1[text()='Manage Jenkins']");
    }
    private By systemConfigHeaderSection(){ return By.xpath("//h2[text()='System Configuration']"); }
    private By securitySection(){ return By.xpath("//h2[text()='Security']"); }
    private By manageUserLink(){ return By.xpath("//a[@title='Manage Users']");}
    private By manageCredentialsLink() {
        return By.xpath("//a[@title='Manage Credentials']");
    }
    private By manageNodesAndCloudsLink() { return By.xpath("//a[@title='Manage Nodes and Clouds']");}

    //Test Methods here

    /**
     * This method is to click on the Manage Users option from Jenkins Application
     */
    public void clickManageUsersOption() {
        try {
            scrollToElementByJS(findElement(securitySection()));
            clickByJS(manageUserLink());
        }
        catch (Exception e){
            Logger.instance.error("Manage user option is not clicked"+e);
        }
    }

    /**
     * This Method is used to click on the Manage Jenkins menu in Jenkins Application
     *
     * @throws Exception
     */
    public void clickManageCredentialsOption() throws Exception {
        WebElement manageCredentials = findElement(manageCredentialsLink());
        scrollToElementByJS(manageCredentials);
        clickByJS(manageCredentials);
    }

    /**
     * This Method is used to verify manage page title in Jenkins Application
     */
    public boolean isManagePageTitleExists() {
        pageLoadWait();
        return isElementPresent(managePageHeader());
    }

    /**
     * This Method is used to click on the Manage Nodes and Clouds menu in Jenkins Application
     *
     * @throws Exception
     */
    public void clickManageNodesAndCloudsOption() throws Exception {
        WebElement manageNodesAndClouds = findElement(manageNodesAndCloudsLink());
        scrollToElementByJS(findElement(systemConfigHeaderSection()));
        if(isElementVisible(manageNodesAndClouds)){
            manageNodesAndClouds.click();
            Logger.instance.info("Clicked ManageNodesAndCloudOption Link Successfully.");
        } else {
            Logger.instance.error("ManageNodesAndCloudOption Element is not visible to click");
        }

    }
}
