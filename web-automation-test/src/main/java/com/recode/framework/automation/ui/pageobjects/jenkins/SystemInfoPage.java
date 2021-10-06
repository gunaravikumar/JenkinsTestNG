package com.recode.framework.automation.ui.pageobjects.jenkins;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SystemInfoPage extends PageClass {

    public SystemInfoPage(RemoteWebDriver theWebDriver)
    {
        super(theWebDriver);
    }

    //Locator values here

    private By jenkinsJava(){ return By.xpath("(//tr[contains(.,'java.version')]//td)[last()]");}
    private By jenkinsHomeInfo(){ return By.xpath("(//tr[contains(.,'JENKINS_HOME')]//td)[last()]"); }
    private By jenkinsOS(){ return By.xpath("(//tr[contains(.,'os.name')]//td)[last()]"); }

    //Test Methods here

    /**
     * This Method is used to get the java version in Jenkins Application
     */
    public String jenkinsJavaVersion() {
        scrollByVerticalByJS(jenkinsJava());
        return getText(jenkinsJava());
    }

    /**
     * This Method is used to get home info in Jenkins Application
     */
    public String jenkinsHome() {
        scrollByVerticalByJS(jenkinsHomeInfo());
        return getText(jenkinsHomeInfo());
    }

    /**
     * This Method is used to get OS Name in Jenkins Application
     * @throws Exception
     */
    public String jenkinsOSName() throws Exception {
        scrollByVerticalByJS(jenkinsOS());
        return findElement(jenkinsOS()).getText();
    }
}
