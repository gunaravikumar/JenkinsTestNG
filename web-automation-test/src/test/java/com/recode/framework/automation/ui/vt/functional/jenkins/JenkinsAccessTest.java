package com.recode.framework.automation.ui.vt.functional.jenkins;

import com.recode.framework.automation.ui.pageobjects.jenkins.JenkinsNodePage;
import com.recode.framework.automation.ui.pageobjects.jenkins.credentials.AddCredentialsPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.credentials.CredentialSystemPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.credentials.CredentialsPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.credentials.GlobalCredentialsPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.homepage.HomePage;
import com.recode.framework.automation.ui.pageobjects.jenkins.login.LoginPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.managejenkins.ManageJenkinsPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.sequences.SequenceActions;
import com.recode.framework.automation.ui.TestClass;
import com.recode.framework.automation.ui.pageobjects.jenkins.users.DeleteUsersPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.users.UsersPage;
import com.recode.framework.automation.ui.utils.ExcelUtils;
import com.recode.framework.automation.ui.utils.Logger;
import com.recode.framework.automation.ui.utils.Utils;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;
import java.net.MalformedURLException;

public class JenkinsAccessTest extends TestClass {

    private static RemoteWebDriver theDriver;
    private static final String testProtocol = "JenkinsAccessTest";
    private static final String testData = Utils.getAutomationProperties().getProperty("JenkinsAccessTest");
    static LoginPage loginPage = null;
    static SequenceActions sequence = null;
    static HomePage homePage = null;
    static UsersPage userPage = null;
    static ManageJenkinsPage managePage = null;
    static DeleteUsersPage deleteUsersPage = null;
    static CredentialsPage credentialsPage = null;
    static CredentialSystemPage credentialSystemPage = null;
    static GlobalCredentialsPage globalCredentialsPage = null;
    static AddCredentialsPage addCredentialsPage;
    static JenkinsNodePage jenkinsNodePage = null;
    static String appURL = null;

    @BeforeTest
    @Parameters({"browser"})
    public void setupForTest(String browser)  {
        ThreadContext.put("threadName", Logger.logLocation(browser) + "\\" + testProtocol);
        appURL = Utils.getAutomationProperties().getProperty("JenkinsApplication");
    }

    @BeforeMethod
    @Parameters({"browser"})
    public void launchURL(String browser) throws MalformedURLException {
        theDriver = (RemoteWebDriver) TestClass.getInstance().setUpDriver(browser);
        setDriver(theDriver);
        loginPage = new LoginPage(theDriver);
        sequence = new SequenceActions(theDriver);
        homePage = new HomePage(theDriver);
        userPage = new UsersPage(theDriver);
        managePage = new ManageJenkinsPage(theDriver);
        deleteUsersPage = new DeleteUsersPage(theDriver);
        deleteUsersPage = new DeleteUsersPage(theDriver);
        credentialsPage = new CredentialsPage(theDriver);
        credentialSystemPage = new CredentialSystemPage(theDriver);
        globalCredentialsPage = new GlobalCredentialsPage(theDriver);
        addCredentialsPage = new AddCredentialsPage(theDriver);
        jenkinsNodePage = new JenkinsNodePage(theDriver);
        loginPage.launchApplication(appURL);
    }

    @Test(priority = 1, description = "To check Login functionality.")
    public void jenkinsLoginTest() throws Exception {
        String testID = "12345";
        Reporter.log("<br><strong>Testcase ID: </strong>"+ testID + "<br>");
        String testCaseName = "jenkinsLoginTest";
        //Update the excel sheet input-files/JenkinsTestData.xlsx with your username and password details
        String username = ExcelUtils.getTestData(testData, "LoginDetails", testID, "Username");
        String password = ExcelUtils.getTestData(testData, "LoginDetails", testID, "Password");
        String dashboardTitle = ExcelUtils.getTestData(testData, "LoginDetails", testID, "DashboardTitle");
        sequence.loginJenkins(username, password);
        Reporter.log("<br>1. Logged into Jenkins application with valid Username and Password.<br>");
        String title = homePage.getPageTitle();
        Assert.assertTrue(title.contains(dashboardTitle));
        Reporter.log("<br>2. Verified the Jenkins home page title after logged into application.<br>");
        homePage.clickLogout();
        Reporter.log("<br>3. Logged out from the Jenkins application.<br>");
    }

    @Test(priority = 2, description = "To create user in Jenkins")
    public void createUserInJenkins() throws Exception {
        String testID = "123456";
        Reporter.log("<br><strong>Testcase ID: </strong>"+ testID + "<br>");
        String testCaseName = "createUserInJenkins";
        //Update automation config file with your username and password
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String createUsername = ExcelUtils.getTestData(testData, "CreateUserDetails", testID, "UserName");
        String createPassword = ExcelUtils.getTestData(testData, "CreateUserDetails", testID, "Password");
        String confirmPassword = ExcelUtils.getTestData(testData, "CreateUserDetails", testID, "ConfirmPassword");
        String fullName = ExcelUtils.getTestData(testData, "CreateUserDetails", testID, "FullName");
        String email = ExcelUtils.getTestData(testData, "CreateUserDetails", testID, "EmailAddress");
        sequence.loginJenkins(username, password);
        Assert.assertTrue(homePage.isHomePageTitleExists(), "JenkinsHome Page doesn't exists");
        Reporter.log("<br>1. Verified the Jenkins home page title after logged into application<br>");
        homePage.clickManageJenkinsLink();
        Assert.assertTrue(managePage.isManagePageTitleExists(), "JenkinsManage Page doesn't exists");
        Reporter.log("<br>2. Verified the Jenkins manage page is displayed when clicked on Manage jenkins link <br>");
        managePage.clickManageUsersOption();
        userPage.clickCreateUserLink();
        Utils.sleep(10);
        sequence.createUserInJenkins(createUsername,createPassword,confirmPassword,fullName,email);
        Reporter.log("<br>3. User is created after entering the required details <br>");
        boolean isUserExists = userPage.isUserExists(createUsername);
        if(!isUserExists)
        {
            Logger.instance.error("New user is not listed out in user list table");
        }
    }

    @Test(priority = 3, description = "To Create Global Credential for Git Access in Jenkins")
    public void createCredentialsInJenkins () throws Exception {
        String testID = "Test_123";
        Reporter.log("<br><strong>Testcase ID: </strong>"+ testID + "<br>");
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String storeScope = ExcelUtils.getTestData(testData, "AddCredentials", testID, "StoreScope");
        String kindOption = ExcelUtils.getTestData(testData, "AddCredentials", testID, "KindOption");
        String createUsername = ExcelUtils.getTestData(testData, "AddCredentials", testID, "UserName");
        String createPassword = ExcelUtils.getTestData(testData, "AddCredentials", testID, "Password");
        String id = ExcelUtils.getTestData(testData, "AddCredentials", testID, "ID");
        String description = ExcelUtils.getTestData(testData, "AddCredentials", testID, "Description");
        sequence.loginJenkins(username, password);
        Assert.assertTrue(homePage.isHomePageTitleExists(), "JenkinsHome Page doesn't exists");
        Reporter.log("<br>1. Verified the Jenkins Home Page Title after logged into application.<br>");
        homePage.clickManageJenkinsLink();
        Assert.assertTrue(managePage.isManagePageTitleExists(), "JenkinsManage Page doesn't exists");
        Reporter.log("<br>2. Verified the Jenkins Manage Page Title after clicked Manage Jenkins Link.<br>");
        managePage.clickManageCredentialsOption();
        Assert.assertTrue(credentialsPage.isCredentialsPageTitleExists(), "JenkinsCredential Page doesn't exists");
        Reporter.log("<br>3. Verified the Jenkins Credential Page Title after clicked Manage Credentials Link.<br>");
        credentialsPage.selectStoreColumnValueFromStoresScopedTable(storeScope);
        Assert.assertTrue(credentialSystemPage.isSystemPageTitleExists(), "JenkinsCredential System Page doesn't exists");
        Reporter.log("<br>4. Verified the Jenkins System Page Title after clicked Store Column Value From Stores Scoped Table.<br>");
        credentialSystemPage.clickGlobalCredentialsDomainLink();
        Assert.assertTrue(globalCredentialsPage.isGlobalCredentialsPageTitleExists(), "GlobalCredential Page doesn't exists");
        Reporter.log("<br>5. Verified the Jenkins Global Credential Page Title after clicked Global Credentials Domain Link.<br>");
        globalCredentialsPage.clickAddCredentialLink();
        Assert.assertTrue(addCredentialsPage.isNewCredentialPageTitleExists(), "AddCredentials Page doesn't exists");
        Reporter.log("<br>6. Verified the Jenkins  New Credential Page Title after clicked Add Credentials Link.<br>");
        sequence.addCredentials(kindOption, 0, createUsername, createPassword, id, description);
        Assert.assertTrue(globalCredentialsPage.isGlobalCredentialsPageTitleExists(), "GlobalCredential Page doesn't exists");
        boolean isCredentialExists = globalCredentialsPage.isVerifyCredentialInGlobalCredentialPageTableByID(id);
        Assert.assertTrue(isCredentialExists, "Created NewCredentials is not exists in GlobalCredentials Table.");
        Reporter.log("<br>7. Credentials is created after entering the required details <br>");
    }

    @Test(priority = 4, description = "To Delete Global Credentials for already Created in Jenkins")
    public void deleteCredentialsInJenkins () throws Exception {
        String testID = "Test_123";
        Reporter.log("<br><strong>Testcase ID: </strong>"+ testID + "<br>");
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String storeScope = ExcelUtils.getTestData(testData, "AddCredentials", testID, "StoreScope");
        String id = ExcelUtils.getTestData(testData, "AddCredentials", testID, "ID");
        sequence.loginJenkins(username, password);
        Assert.assertTrue(homePage.isHomePageTitleExists(), "JenkinsHome Page doesn't exists");
        Reporter.log("<br>1. Verified the Jenkins Home Page Title after logged into application.<br>");
        homePage.clickManageJenkinsLink();
        Assert.assertTrue(managePage.isManagePageTitleExists(), "JenkinsManage Page doesn't exists");
        Reporter.log("<br>2. Verified the Jenkins Manage Page Title after clicked Manage Jenkins Link.<br>");
        managePage.clickManageCredentialsOption();
        Assert.assertTrue(credentialsPage.isCredentialsPageTitleExists(), "JenkinsCredential Page doesn't exists");
        Reporter.log("<br>3. Verified the Jenkins Credential Page Title after clicked Manage Credentials Link.<br>");
        credentialsPage.selectStoreColumnValueFromStoresScopedTable(storeScope);
        Assert.assertTrue(credentialSystemPage.isSystemPageTitleExists(), "JenkinsCredential System Page doesn't exists");
        Reporter.log("<br>4. Verified the Jenkins System Page Title after clicked Store Column Value From Stores Scoped Table.<br>");
        credentialSystemPage.clickGlobalCredentialsDomainLink();
        Assert.assertTrue(globalCredentialsPage.isGlobalCredentialsPageTitleExists(), "GlobalCredential Page doesn't exists");
        Reporter.log("<br>5. Verified the Jenkins Global Credential Page Title after clicked Global Credentials Domain Link.<br>");
        boolean isCredentialExists = globalCredentialsPage.isVerifyCredentialInGlobalCredentialPageTableByID(id);
        Assert.assertTrue(isCredentialExists, id + " ID is not exists in Global Credential Table.");
        Reporter.log("<br>6. Verified Credential ID is Exists in Global Credentials Page.<br>");
        sequence.deleteCredentials(id);
        Assert.assertTrue(globalCredentialsPage.isGlobalCredentialsPageTitleExists(), "GlobalCredential Page doesn't exists");
        isCredentialExists = globalCredentialsPage.isVerifyCredentialInGlobalCredentialPageTableByID(id);
        Assert.assertFalse(isCredentialExists, "Credential should not be available after deleted in Global Credentials Table.");
        Reporter.log("<br>7. Credentials is deleted after checking the global credential table.<br>");
    }

    @Test(priority = 5, description = "Deleting created user in jenkins")
    public void deleteUserInJenkins() throws Exception {
        String testID = "987456";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "deleteUserInJenkins";
        //Update automation config file with your username and password
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String jenkinsUser = ExcelUtils.getTestData(testData, "CreateUserDetails", testID, "UserName");
        sequence.loginJenkins(username, password);
        Assert.assertTrue(homePage.isHomePageTitleExists(), "JenkinsHome Page doesn't exists");
        Reporter.log("<br>1. Verified the Jenkins home page title after logged into application<br>");
        homePage.clickManageJenkinsLink();
        Assert.assertTrue(managePage.isManagePageTitleExists(), "JenkinsManage Page doesn't exists");
        Reporter.log("<br>2. Verified the Jenkins manage page is displayed when clicked on Manage jenkins link <br>");
        managePage.clickManageUsersOption();
        boolean isUserExists = userPage.isUserExists(jenkinsUser);
        Assert.assertTrue(isUserExists,"New jenkinsUser is not listed out in jenkinsUser list table");
        Reporter.log("<br>3. User is listed in jenkins application <br>");
        userPage.deleteUserClick();
        deleteUsersPage.clickDeleteConfirmButton();
        isUserExists = userPage.isUserExists(jenkinsUser);
        Assert.assertFalse(isUserExists,"Jenkins User is not deleted from user list");
        Reporter.log("<br>4. User is deleted from jenkins application <br>");
    }

    @Test(priority = 6, description = "The verify the existing users in jenkins")
    public void usersInJenkins() throws Exception {
        String testID = "987457";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "usersInJenkins";
        //Update automation config file with your username and password
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String user1 = ExcelUtils.getTestData(testData, "SystemProperties", testID, "User1");
        String user2= ExcelUtils.getTestData(testData, "SystemProperties", testID, "User2");
        String user3= ExcelUtils.getTestData(testData, "SystemProperties", testID, "User3");
        sequence.loginJenkins(username, password);
        Assert.assertTrue(homePage.isHomePageTitleExists(), "JenkinsHome Page doesn't exists");
        Reporter.log("<br>1. Verified the Jenkins home page title after logged into application<br>");
        homePage.clickManageJenkinsLink();
        Assert.assertTrue(managePage.isManagePageTitleExists(), "JenkinsManage Page doesn't exists");
        Reporter.log("<br>2. Verified the Jenkins manage page is displayed when clicked on Manage jenkins link <br>");
        managePage.clickManageUsersOption();
        int users = userPage.usersCount();
        Logger.instance.info("The jenkins users count are " +users);
        Assert.assertTrue(userPage.checkUserExist(user1), "The given user - "+user1+" is not found");
        Assert.assertTrue(userPage.checkUserExist(user2), "The given user - "+user2+" is not found");
        Assert.assertTrue(userPage.checkUserExist(user3), "The given user - "+user3+" is not found");
        Reporter.log("<br>3. Verified that the users exists in jenkins application <br>");
    }

    @Test(priority = 7, description = "To Create Node Agent for Control and Monitor the Jobs in Jenkins")
    public void createNodeAgentInJenkinsNode() throws Exception {
        String testID = "Test_124";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        //Update automation config file with your username and password
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String nodeName = ExcelUtils.getTestData(testData, "JenkinsNode", testID, "NodeName");
        String description = ExcelUtils.getTestData(testData, "JenkinsNode", testID, "Description");
        String numOfExecutors  = ExcelUtils.getTestData(testData, "JenkinsNode", testID, "NumOfExecutors");
        String rootDirectory = ExcelUtils.getTestData(testData, "JenkinsNode", testID, "RemoteRootDir");
        String labels = ExcelUtils.getTestData(testData, "JenkinsNode", testID, "Labels");
        String path = ExcelUtils.getTestData(testData, "JenkinsNode", testID, "CustomWorkDirPath");
        sequence.loginJenkins(username, password);
        Assert.assertTrue(homePage.isHomePageTitleExists(), "JenkinsHome Page doesn't exists");
        Reporter.log("<br>1. Verified the Jenkins Home Page Title after logged into application.<br>");
        homePage.clickManageJenkinsLink();
        Assert.assertTrue(managePage.isManagePageTitleExists(), "JenkinsManage Page doesn't exists");
        Reporter.log("<br>2. Verified the Jenkins Manage Page Title after clicked Manage Jenkins Link.<br>");
        managePage.clickManageNodesAndCloudsOption();
        Assert.assertTrue(jenkinsNodePage.isNodePageTitleExists(), "JenkinsNodePage doesn't exists");
        Reporter.log("<br>3. Verified the Jenkins Node Page Title after clicked Manage Nodes And Cloud Link.<br>");
        jenkinsNodePage.clickNewNodeLink();
        jenkinsNodePage.enterNodeNameInTextBox(nodeName);
        jenkinsNodePage.clickPermanentAgentRadioButton();
        jenkinsNodePage.clickOKButton();
        jenkinsNodePage.enterNodeDescriptionInTextBox(description);
        jenkinsNodePage.enterNumberOfExecutorInTextBox(numOfExecutors);
        jenkinsNodePage.enterRemoteRootDirectoryInTextBox(rootDirectory);
        jenkinsNodePage.enterLabelsInTextBox(labels);
        jenkinsNodePage.selectUseThisNodeAsMuchAsPossibleInUsageDropDown();
        jenkinsNodePage.selectLaunchAgentByConnectingToMaster();
        jenkinsNodePage.clickDisableWorkDirCheckBox();
        jenkinsNodePage.enterCustomWorkDirPathInTextBox(path);
        jenkinsNodePage.clickFailIfWorkDirIsMissingCheckBox();
        jenkinsNodePage.selectKeepThisAgentOnlineAsMuchAsPossible();
        jenkinsNodePage.clickDisableDeferredWipeOutCheckBox();
        jenkinsNodePage.clickSaveButton();
        Utils.sleepWithReason(5,"Waiting for Creating New Node");
        Assert.assertTrue(jenkinsNodePage.isNodePageTitleExists(), "JenkinsNodePage doesn't exists");
        boolean isNodeExists = jenkinsNodePage.isVerifyNodeIsExistsInJenkinsNodePage(nodeName);
        Assert.assertTrue(isNodeExists, "Created Node is not exists in JenkinsNode Page.");
        Reporter.log("<br>4. Node is created after entering the required details <br>");
    }

    @Test(priority = 8, description = "To Delete Node Agent in Jenkins")
    public void deleteNodeAgentInJenkinsNode() throws Exception {
        String testID = "Test_124";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        //Update automation config file with your username and password
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String nodeName = ExcelUtils.getTestData(testData, "JenkinsNode", testID, "NodeName");
        sequence.loginJenkins(username, password);
        Assert.assertTrue(homePage.isHomePageTitleExists(), "JenkinsHome Page doesn't exists");
        Reporter.log("<br>1. Verified the Jenkins Home Page Title after logged into application.<br>");
        homePage.clickManageJenkinsLink();
        Assert.assertTrue(managePage.isManagePageTitleExists(), "JenkinsManage Page doesn't exists");
        Reporter.log("<br>2. Verified the Jenkins Manage Page Title after clicked Manage Jenkins Link.<br>");
        managePage.clickManageNodesAndCloudsOption();
        Assert.assertTrue(jenkinsNodePage.isNodePageTitleExists(), "JenkinsNodePage doesn't exists");
        Reporter.log("<br>3. Verified the Jenkins Node Page Title after clicked Manage Nodes And Cloud Link.<br>");
        jenkinsNodePage.clickNodeInJenkinsNodePage(nodeName);
        jenkinsNodePage.clickDeleteAgentLink();
        jenkinsNodePage.clickYesButton();
        Utils.sleepWithReason(5, "Waiting for Deleting Node");
        Assert.assertTrue(jenkinsNodePage.isNodePageTitleExists(), "JenkinsNodePage doesn't exists");
        boolean isNodeExists = jenkinsNodePage.isVerifyNodeIsExistsInJenkinsNodePage(nodeName);
        Assert.assertFalse(isNodeExists, nodeName + " : Node is exists after deleting node in JenkinsNode Page.");
        Reporter.log("<br>4. Node is deleted from jenkins application <br>");
    }

    @Test(priority = 9, description = "To Create Credential for Git Access with Type of 'SSH Username with Private Key' in Jenkins.")
    public void addCredentialsTypeSSHUsernameWithPrivateKey () throws Exception {
        String testID = "Test_125";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String storeScope = ExcelUtils.getTestData(testData, "AddCredentials", testID, "StoreScope");
        String kindOption = ExcelUtils.getTestData(testData, "AddCredentials", testID, "KindOption");
        String id = ExcelUtils.getTestData(testData, "AddCredentials", testID, "ID");
        String description = ExcelUtils.getTestData(testData, "AddCredentials", testID, "Description");
        String createUsername = ExcelUtils.getTestData(testData, "AddCredentials", testID, "UserName");
        String createPrivateKey = ExcelUtils.getTestData(testData, "AddCredentials", testID, "PrivateKey");
        String passPhrase = ExcelUtils.getTestData(testData, "AddCredentials", testID, "PassPhrase");
        sequence.loginJenkins(username, password);
        Assert.assertTrue(homePage.isHomePageTitleExists(), "JenkinsHome Page doesn't exists");
        Reporter.log("<br>1. Verified the Jenkins Home Page Title after logged into application.<br>");
        homePage.clickManageJenkinsLink();
        Assert.assertTrue(managePage.isManagePageTitleExists(), "JenkinsManage Page doesn't exists");
        Reporter.log("<br>2. Verified the Jenkins Manage Page Title after clicked Manage Jenkins Link.<br>");
        managePage.clickManageCredentialsOption();
        Assert.assertTrue(credentialsPage.isCredentialsPageTitleExists(), "JenkinsCredential Page doesn't exists");
        Reporter.log("<br>3. Verified the Jenkins Credential Page Title after clicked Manage Credentials Link.<br>");
        credentialsPage.selectStoreColumnValueFromStoresScopedTable(storeScope);
        Assert.assertTrue(credentialSystemPage.isSystemPageTitleExists(), "JenkinsCredential System Page doesn't exists");
        Reporter.log("<br>4. Verified the Jenkins System Page Title after clicked Store Column Value From Stores Scoped Table.<br>");
        credentialSystemPage.clickGlobalCredentialsDomainLink();
        Assert.assertTrue(globalCredentialsPage.isGlobalCredentialsPageTitleExists(), "GlobalCredential Page doesn't exists");
        Reporter.log("<br>5. Verified the Jenkins Global Credential Page Title after clicked Global Credentials Domain Link.<br>");
        globalCredentialsPage.clickAddCredentialLink();
        Assert.assertTrue(addCredentialsPage.isNewCredentialPageTitleExists(), "AddCredentials Page doesn't exists");
        Reporter.log("<br>6. Verified the Jenkins  New Credential Page Title after clicked Add Credentials Link.<br>");
        sequence.addNewCredentialTypeSSHUsernameWithPrivateKey(kindOption,1,id,description,createUsername,createPrivateKey,passPhrase);
        Assert.assertTrue(globalCredentialsPage.isGlobalCredentialsPageTitleExists(), "GlobalCredential Page doesn't exists");
        boolean isCredentialExists = globalCredentialsPage.isVerifyCredentialInGlobalCredentialPageTableByID(id);
        Assert.assertTrue(isCredentialExists, "Created NewCredentials is not exists in GlobalCredentials Table.");
        //Delete Credential
        sequence.deleteCredentials(id);
        Assert.assertTrue(globalCredentialsPage.isGlobalCredentialsPageTitleExists(), "GlobalCredential Page doesn't exists");
        isCredentialExists = globalCredentialsPage.isVerifyCredentialInGlobalCredentialPageTableByID(id);
        Assert.assertFalse(isCredentialExists, "Credential should not be available after deleted in Global Credentials Table.");
        Reporter.log("<br>7. Credentials is created after entering the required details <br>");
    }

    @Test(priority = 10, description = "To close an existing session and open a new session")
    public void openAndCloseSessions() throws Exception {
        String testID = "987457";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "openAndCloseSessions";
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        sequence.loginJenkins(username,password);
        String browserName = homePage.getBrowserName();
        homePage.closeCurrentSession();
        Reporter.log("<br>1. Existing jenkins session is closed<br>");
        launchURL(browserName);
        sequence.loginJenkins(username,password);
        Reporter.log("<br>2. Logged into jenkins application<br>");
        String pipelineURL = ExcelUtils.getTestData(testData, "SystemProperties", testID, "Url");
        homePage.navigateToURL(pipelineURL);
        Utils.sleep(5);
        Reporter.log("<br>3. Navigated to pipeline url in jenkins application<br>");
        String pageTitle = ExcelUtils.getTestData(testData, "SystemProperties", testID, "PageTitle");
        String title = homePage.getPageTitle();
        Assert.assertTrue(title.contains(pageTitle));
        Reporter.log("<br>4. Verified the page title after navigating to the pipeline url<br>");

    }

    @AfterMethod
    public void tearDown() {
        if (theDriver != null) {
            TestClass.getInstance().removeDriver();
        }
    }
}
