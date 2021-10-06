package com.recode.framework.automation.ui.vt.functional.jenkins;

import com.recode.framework.automation.ui.pageobjects.jenkins.JobInfoPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.SystemInfoPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.homepage.HomePage;
import com.recode.framework.automation.ui.pageobjects.jenkins.login.LoginPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.sequences.SequenceActions;
import com.recode.framework.automation.ui.utils.ExcelUtils;
import com.recode.framework.automation.ui.utils.Logger;
import com.recode.framework.automation.ui.utils.Utils;
import com.recode.framework.automation.ui.TestClass;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.File;
import java.net.URL;

public class JenkinsInfoTest extends TestClass {

    private static RemoteWebDriver theDriver;
    private static final String testProtocol = "JenkinsInfoTest";
    private static final String testData = Utils.getAutomationProperties().getProperty("JenkinsAccessTest");
    static LoginPage loginPage = null;
    static SequenceActions sequence = null;
    static HomePage homePage = null;
    static SystemInfoPage systemInfoPage = null;
    static JobInfoPage jobInfoPage = null;
    static String appURL = null;

    @BeforeTest
    @Parameters({"browser"})
    public void setupForTest(String browser)  {
        ThreadContext.put("threadName", Logger.logLocation(browser) + "\\" + testProtocol);
        appURL = Utils.getAutomationProperties().getProperty("JenkinsApplication");
    }

    @BeforeMethod
    @Parameters({"browser"})
    public void loginApplication(String browser) throws Exception {
        theDriver = (RemoteWebDriver) TestClass.getInstance().setUpDriver(browser);
        setDriver(theDriver);
        loginPage = new LoginPage(theDriver);
        sequence = new SequenceActions(theDriver);
        homePage = new HomePage(theDriver);
        systemInfoPage = new SystemInfoPage(theDriver);
        jobInfoPage = new JobInfoPage(theDriver);
        loginPage.launchApplication(appURL);
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        sequence.loginJenkins(username, password);
    }

    @Test(priority = 1, description = "Getting Jenkins Version Info")
    public void jenkinsVersionInfo() throws Exception {
        String testID = "987457";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "jenkinsVersionInfo";
        String expectedVersion = ExcelUtils.getTestData(testData, "SystemProperties", testID, "JenkinVersion");
        String actualVersion = homePage.getJenkinsVersion();
        Assert.assertEquals(actualVersion,expectedVersion);
        Logger.instance.info("The version of Jenkins Application is " + homePage.getJenkinsVersion());
        Reporter.log("<br>1. Verified the Jenkins Version</br>");
    }

    @Test(priority = 2, description = "Verifying Jenkins Rest API")
    public void jenkinsRestApi() throws  Exception{
        String testID = "987458";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "jenkinsRestApi";
        homePage.clickRestApiLink();
        Reporter.log("<br>1. Clicked Rest API Link Successfully.</br>");
        String getApiUrl = homePage.getCurrentUrl();
        String verifyApiURL = ExcelUtils.getTestData(testData,"JenkinsInfo", testID, "ApiUrl");
        Assert.assertEquals(getApiUrl, verifyApiURL);
        Reporter.log("<br>2. Verified the API URL in Jenkins</br>");
        homePage.refreshPage();
        homePage.selectBackOnBrowser();
        String getHomeUrl = homePage.getCurrentUrl();
        Assert.assertEquals(appURL, getHomeUrl);
        Reporter.log("<br>3. Verified the Home URL in Jenkins</br>");
    }

    @Test(priority = 3, description = "Handling Window by Switch Method")
    public void jenkinsSwitchWindow() throws Exception {
        String testID = "852369";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "jenkinsSwitchWindow";
        String dashboardTitle = ExcelUtils.getTestData(testData, "LoginDetails", "12345", "DashboardTitle");
        String jenkinsNewWindowTitle = ExcelUtils.getTestData(testData, "LoginDetails", testID, "DashboardTitle");
        homePage.clickJenkinsVersion();
        homePage.switchToWindow(1);
        Reporter.log("<br>1. Switched to New Window.</br>");
        String title = homePage.getPageTitle();
        Assert.assertTrue(title.contains(jenkinsNewWindowTitle));
        Reporter.log("<br>2. Verified the New Window Title.</br>");
        homePage.switchToWindow(0);
        Reporter.log("<br>3. Switched to Parent Window.</br>");
        title = homePage.getPageTitle();
        Assert.assertTrue(title.contains(dashboardTitle));
        Reporter.log("<br>4. Verified HomePage Window Title.</br>");
        homePage.closeAllChildWindows();
        int windows = homePage.getNoOfWindowsOpened();
        Assert.assertEquals(windows, 1, windows+ " is displayed. But expected is 1");
        Reporter.log("<br>5. Verified All Child Windows are Closed</br>");
    }

    @Test(priority = 4,description = "Checking Jenkins System Properties Information")
    public void jenkinsSystemProperties() throws Exception {
        String testID = "987457";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "jenkinsSystemProperties";
        String expectedJavaVersion = ExcelUtils.getTestData(testData, "SystemProperties", testID, "JenkinJavaVersion");
        String expectedJavaHome = ExcelUtils.getTestData(testData, "SystemProperties", testID, "JenkinHome");
        String expectedOSName = ExcelUtils.getTestData(testData, "SystemProperties", testID, "JenkinOSName");
        homePage.clickManageJenkinsLink();
        Reporter.log("<br>1. Clicked ManageJenkins Link Successfully.</br>");
        homePage.clickSystemInfoLink();
        Reporter.log("<br>2. Clicked SystemInfo Link Successfully.</br>");
        boolean scrollCheck = homePage.isVerticalScrollBarPresentInWebPageByJS();
        Assert.assertTrue(scrollCheck,"Scrollbar is not available on the webpage");
        Logger.instance.info(scrollCheck);
        Reporter.log("<br>3. Verified ScrollBar is available.</br>");
        String javaVersion = systemInfoPage.jenkinsJavaVersion();
        Assert.assertEquals(javaVersion,expectedJavaVersion);
        Logger.instance.info(javaVersion);
        Reporter.log("<br>4. Verified the Java Version.</br>");
        String javaHome = systemInfoPage.jenkinsHome();
        Assert.assertEquals(javaHome,expectedJavaHome);
        Logger.instance.info(javaHome);
        Reporter.log("<br>5. Verified the Java Home.</br>");
        String osName = systemInfoPage.jenkinsOSName();
        Assert.assertEquals(osName,expectedOSName);
        Logger.instance.info(osName);
        Reporter.log("<br>6. Verified Jenkins Operating System Name.</br>");
    }

    @Test(priority = 5,description = "Getting Jenkins Latest Build Information")
    public void jenkinsLatestBuildInfo() throws Exception {
        String testID = "987457";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "jenkinsLatestBuildInfo";
        String jobName = ExcelUtils.getTestData(testData, "SystemProperties", testID, "JobName");
        homePage.openJob(jobName);
        Reporter.log("<br>1. Opened " + jobName +" Job</br>");
        String jobInfo = jobInfoPage.getLatestBuildInfo();
        Logger.instance.info("Date & time info for the latest build is retrieved " + jobInfo);
        Reporter.log("<br>2. Retrieved LatestBuild Information " + jobInfo + ".</br>");
    }

    @Test(priority = 6,description = "Getting General Browser Information")
    public void generalInfoTesting() {
        String testID = "987457";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "generalInfoTesting";
        String browserName = homePage.getBrowserName();
        Logger.instance.info("The browser which is automated is: "+browserName);
        Reporter.log("<br>1. Retrieved Browser Name " + browserName + ".</br>");
        String remoteSessionID = homePage.getRemoteWebDriveSessionID();
        Logger.instance.info("The Remote WebDriver Session is: "+remoteSessionID);
        Reporter.log("<br>2. Retrieved Remote SessionID " + remoteSessionID + ".</br>");
    }
    @Test(priority = 7, description = "Getting Date from Jenkins Job")
    public void jenkinsJobDate() throws Exception {
        String testID = "987459";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "jenkinsSuccessfulBuildDate";
        homePage.clickTestBranchJob();
        Reporter.log("<br>1. Clicked Test Branch Job.</br>");
        homePage.clickLastSuccessfulBuild();
        Reporter.log("<br>2. Clicked Last Successful Build</br>");
        String buildTime = homePage.getTimeStampBuild();
        String currentDateTime = Utils.getCurrentdatetime();
        String lastSuccessfulBuildDate = Utils.findDateDifference(currentDateTime,buildTime);
        Logger.instance.info("LastSuccessful BuildDate arrives at: " + lastSuccessfulBuildDate + " Ago");
        Reporter.log("<br>3. Retrieved LastSuccessful BuildDate arrives at: " + lastSuccessfulBuildDate + " Ago.</br>");
    }
    @Test(priority = 8,description= "Capture the JenkinsLogo")
    public void jenkinslogo() throws Exception {
        String testID = "987460";
        String testCaseName = "jenkinsLogo";
        String jenkinsLogo = homePage.captureElement(By.id("jenkins-head-icon"),"JenkinsInfoTest",testCaseName,"Logojenkins");
        Reporter.log("<br>1. Capture the jenkins logo.</br>");
        URL baseImageDir = this.getClass().getClassLoader().getResource("BaseImage");
        File baseImage = new File( baseImageDir.getPath() + File.separator + testProtocol + File.separator + testCaseName+ File.separator + "Base_Logojenkins"+".png");
        Reporter.log("<br>2. Getting the BaseImage Directory.</br>");
        Assert.assertTrue(Utils.compareImages(String.valueOf(baseImage),jenkinsLogo,testCaseName));
        Reporter.log("<br>3. Verified the image of Jenkins Logo.</br>");
    }

    @AfterMethod
    public void tearDown() {
        if (theDriver != null) {
            TestClass.getInstance().removeDriver();
        }
    }
}
