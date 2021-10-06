package com.recode.framework.automation.ui.pageobjects.jenkins;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Logger;
import com.recode.framework.automation.ui.utils.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Arrays;

public class JobInfoPage extends PageClass {

    public JobInfoPage(RemoteWebDriver theWebDriver)
    {
        super(theWebDriver);
    }

    //Locator values here
    private By getDateTimeJobInfo(){ return By.xpath("//table[@class='pane stripped']//tr//div[@class='pane build-details']//a"); }
    private WebElement getDateTimeJobInfo_webElement(){
        return theWebDriver.findElement(By.xpath("//table[@class='pane stripped']//tr//div[@class='pane build-details']//a"));
    }
    private By buildWithParamLink(){ return By.linkText("Build with Parameters"); }
    private By buildWithParamParentLocator(){ return By.xpath("//table[@class='parameters']//tr//td//div[@name='parameter']"); }
    private By buildWithParamChildLocator(){ return By.xpath("//select[@name='value']//option"); }
    private By buildWithParamDropdown(){ return By.xpath("//select[@name='value']");}
    private By clickBuildButton(){ return By.xpath("//table[@class='parameters']//tbody//tr//td//button[@type='button']");}
    private By clickConsoleOutputLink(){ return By.xpath("//div[@id='page-body']//a[@title='Console Output']");}
    private By consoleOutputMsg(){ return By.className("console-output");}



    //Test Methods here

    /**
     * This Method is used to get the date & time for the latest build in Jenkins Application
     */
    public String getLatestBuildInfo() {
        return getText(getDateTimeJobInfo());
    }

    /**
     * This method is to click on build with parameter link in Jenkins Application
     * @throws Exception
     */
    public void clickBuildWithParam() throws Exception {
        clickByJS(buildWithParamLink());
    }

    /**
     * This method is to get the dropdown values from the build with parameter link page in Jenkins Application
     * @return
     */
    public String[] buildWithParamDropdownOptions(){
        String[] dropdownMenus = getDropdownOptionsUsingSelect(buildWithParamDropdown());
        Logger.instance.info("options are : "+ Arrays.toString(dropdownMenus));
        return dropdownMenus;
    }

    /**
     * This method is to verify the dropdown values from the build with parameter link page in Jenkins Application
     * @return
     */
    public boolean verifyBuildWithParamDropdown() {
        String[] dropdownOptions = buildWithParamDropdownOptions();
        boolean isAllElementFound = verifyDropDownList(buildWithParamParentLocator(), buildWithParamChildLocator(), dropdownOptions);
        return isAllElementFound;
    }

    /**
     * This method is to click on build button from the build with parameter link page in Jenkins Application
     * @return
     */
    public void clickOnBuildButton() throws Exception {
        clickByJS(clickBuildButton());
    }

    /**
     * This Method is to click on the latest build in Jenkins Application
     */
    public void clickOnLatestBuildLink() throws Exception {
        Utils.sleep(5);
        clickByJS(getDateTimeJobInfo_webElement());
    }

    /**
     * This Method is to click on the console output in Jenkins Application
     */
    public void clickOnConsoleOutputLink() throws Exception {
        clickByJS(clickConsoleOutputLink());
    }

    /**
     * This Method is to get the message text from console output in Jenkins Application
     */
    public String getConsoleOutputMsg() throws Exception {
        String outputText = getText(consoleOutputMsg());
        return outputText;
    }


}
