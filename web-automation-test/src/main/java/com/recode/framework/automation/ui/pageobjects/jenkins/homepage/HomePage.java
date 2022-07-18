package com.recode.framework.automation.ui.pageobjects.jenkins.homepage;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Logger;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;


public class HomePage extends PageClass {

    public HomePage(RemoteWebDriver theWebDriver)
    {
        super(theWebDriver);
    }

    private static final String homePageTitle = "Dashboard [Jenkins]";

    //Locator values here

    private By logoutButton() { return By.xpath("//a[contains(.,'log out')]"); }
    private By manageJenkinsLink(){ return By.xpath("//a[contains(.,'Create User')]");}
    private By newItemLink(){
        //return By.className("task-link");
        return By.xpath("//a[contains(.,'New Item')]");
    }
    private By createdProject(String projectName) {
        return By.id("job_"+projectName);
    }
    private By jenkinsVersion(){ return By.xpath("//div[contains(@class,'footer')]//a[contains(.,'Jenkins')]");}
    private By restApiLink(){ return By.xpath("//a[@href ='api/']");}
    private By systemInformationLink(){ return By.xpath("//a[contains(@title,'System Information')]//dt[contains(.,'System Information')]");}
    private By statusInfoSection(){ return By.xpath("//h2[text()='Status Information']"); }
    private By jobName(String job){ return By.linkText(job);}
    private By projectStatusTable() { return By.cssSelector("table#projectstatus"); }
    private By configureLink(){
        return By.xpath("//a[contains(.,'Configure')]");
    }
    private By selectTestBranchJob(){ return By.xpath("//a[@href='job/PersonalBuild/' and @class='model-link inside']");}
    private By permanentLinkItem(){ return By.className("permalink-item");}
    private By lastSuccessfulBuildLink(){ return By.xpath("//a[@href='lastSuccessfulBuild/']");}
    private By timeStampBuild(){ return By.xpath("//h1[@class='build-caption page-headline']");}
    private By searchBar(){ return By.id("search-box");}
    private By buildNow(){ return By.xpath("//a[@title='Build Now' and @class='task-link']");}
    private By lastBuildLink(){ return By.xpath("//a[@href='lastBuild/']");}



    //Test Methods here

    /**
     * This Method is used to click on the Logout button in Jenkins Application
     * @throws Exception - Error handling
     */
    public void clickLogout() throws Exception {
        click(logoutButton());
    }

    /**
     * This method is to click on the Manage Jenkins option from Jenkins Application
     * @throws Exception - Error handling
     */
    public void clickManageJenkinsLink() throws Exception {
        clickByJS(manageJenkinsLink());
        Logger.instance.info("Clicked Manage Jenkins Link Successfully.");
    }
    /**
     * This Method is used to click on the New Item link in the Jenkins Homepage
     * @throws Exception - Error handling
     */
    public void clickNewItem() throws Exception {
        clickByJS(newItemLink());
        Logger.instance.info("Clicked New Item Link Successfully.");
    }
    public boolean isJobPresentInJenkinsHomepage(String projectName) {
        try {
            searchAttempts = 1;
            boolean isJobCreated = isElementVisible(createdProject(projectName));
            if (isJobCreated)
                Logger.instance.info(projectName + " - created successfully");
            else
                Logger.instance.error(projectName + " - is not created");

            return isJobCreated;
        }
        finally {
            searchAttempts = 6;
        }
    }

    /**
     * This method is to get the version info about jenkins application
     * @throws Exception - Error handling
     * @return String
     */
    public String getJenkinsVersion() throws Exception {
        return findElement(jenkinsVersion()).getText();
    }

    /**
     * This Method is used to click on the RESTAPI link in the Jenkins Homepage
     * @throws Exception - Error handling
     */
    public void clickRestApiLink() throws Exception {
          click(restApiLink());
    }

    /**
     * This Method is used to click on the Jenkins version displaying in homepage
     * @throws Exception - Error handling
     */
    public void  clickJenkinsVersion() throws Exception {
        click(jenkinsVersion());
    }

    /**
     * This method is to click System information link in jenkins application
     */
    public void clickSystemInfoLink() {
        scrollByVerticalByJS(statusInfoSection());
        clickByJS(systemInformationLink());
    }

    /**
     * This Method is used to open the job by clicking it
     * @param job - Job name
     * @throws Exception - Error handling
     */

   public void openJob(String job) throws Exception {
    clickByJS(jobName(job));
   }

    /**
     * This Method is used to verify home page title in Jenkins Application
     */
    public boolean isHomePageTitleExists() {
        pageLoadWait();
        String getHomePageTitle = getPageTitle().trim();
        if(getHomePageTitle.equalsIgnoreCase(homePageTitle)){
            Logger.instance.info("HomePage is displayed Successfully");
            return true;
        } else {
            Logger.instance.error("Failed to open homepage");
        }
        return false;
    }

    /**
     * This Method is used to click job in jenkins home page
     */
    public void clickJobInJenkinsHomepage(String jobProjectName) {
        clickColumnValueInTable(projectStatusTable(), "Name  ↓", jobProjectName);
    }
    /**
     * This Method is used to click on Configure link
     * @throws Exception
     */
    public void clickConfigure() throws Exception {
        click(configureLink());
    }
    /**
     * This method is to get the last successful build timestamp
     * @throws Exception
     * @return It return the timestamp value
     */
    public String getTimeStampBuild() throws Exception {

        String Build = findElement(timeStampBuild()).getText();
        String timestampBuild = StringUtils.substringBetween(Build, "(", ")");
        return timestampBuild;
    }
    /**
     * This method is to click the created job
     * @throws Exception
     * @return
     */
    public void clickTestBranchJob() throws Exception {
        click(selectTestBranchJob());
    }
    /**
     * This method is to click the last successful build
     * @throws Exception
     * @return
     */
    public void clickLastSuccessfulBuild() throws Exception {
        WebElement lastSuccessfulElement = findElement(permanentLinkItem());
        WebElement permanentLinks = lastSuccessfulElement.findElement(lastSuccessfulBuildLink());
        scrollToElementByJS(permanentLinks);
        clickByJS(permanentLinks);
    }
    /**
     * This Method is used to open the job by clicking it
     * @param jobName - Job name
     * @throws Exception - Error handling
     */

    public void searchJob(String jobName) throws Exception {
        WebElement search = findElement(searchBar());
        scrollToElementByJS(search);
        clearAndSendKeys(searchBar(),jobName);
        keysAction(Keys.ENTER);
    }
    /**
     * This method is to click the last successful build
     * @throws Exception
     * @return
     */
    public void clickLastBuild() throws Exception {
        WebElement permanentLinks = findElement(permanentLinkItem());
        WebElement lastBuildElement = permanentLinks.findElement(lastBuildLink());
        scrollToElementByJS(lastBuildElement);
        clickByJS(lastBuildElement);
    }

}
