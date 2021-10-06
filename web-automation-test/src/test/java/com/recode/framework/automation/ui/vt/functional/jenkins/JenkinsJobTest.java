package com.recode.framework.automation.ui.vt.functional.jenkins;

import com.recode.framework.automation.ui.pageobjects.jenkins.homepage.HomePage;
import com.recode.framework.automation.ui.pageobjects.jenkins.JobCreationPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.JobInfoPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.JobProjectPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.WorkspacePage;
import com.recode.framework.automation.ui.pageobjects.jenkins.login.LoginPage;
import com.recode.framework.automation.ui.pageobjects.jenkins.sequences.SequenceActions;
import com.recode.framework.automation.ui.utils.ExcelUtils;
import com.recode.framework.automation.ui.utils.Logger;
import com.recode.framework.automation.ui.utils.Utils;
import com.recode.framework.automation.ui.TestClass;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.*;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Arrays;

public class JenkinsJobTest extends TestClass {

    private static RemoteWebDriver theDriver;
    private static final String testProtocol = "JenkinsJobTest";
    private static final String testData = Utils.getAutomationProperties().getProperty("JenkinsAccessTest");
    static LoginPage loginPage = null;
    static SequenceActions sequence = null;
    static HomePage homePage = null;
    static JobCreationPage jobCreationPage = null;
    static JobProjectPage jobProjectPage = null;
    static WorkspacePage workspacePage = null;
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
    public void launchURL(String browser) throws MalformedURLException {
        theDriver = (RemoteWebDriver) TestClass.getInstance().setUpDriver(browser);
        setDriver(theDriver);
        loginPage = new LoginPage(theDriver);
        sequence = new SequenceActions(theDriver);
        homePage = new HomePage(theDriver);
        jobCreationPage = new JobCreationPage(theDriver);
        jobProjectPage = new JobProjectPage(theDriver);
        workspacePage = new WorkspacePage(theDriver);
        jobInfoPage = new JobInfoPage(theDriver);
        loginPage.launchApplication(appURL);
    }

    @Test(priority = 1, description = "Creating a Free Style Job in Jenkins")
    public void createFreeStyleJob() throws Exception {
        String testID = "12345";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "createFreeStyleJob";
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String project_Name = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Project_Name");
        String project_Type = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Project_Type");
        String project_Description = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Project_Description");
        String build_Step_option_1 = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Build_Step_option_1");
        String maven_goal = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Maven_goal");
        String post_build_action_1 = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Post_build_action_1");
        String post_build_action_2 = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Post_build_action_2");
        String project_Recipient = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Project_Receipient");
        String project_reply_to = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Project_reply_to");
        String content_type = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Content_type");
        String default_Subject = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Default_Subject");
        String default_content = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Default_content");
        String attachment = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Attachment");
        String build_log = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Build_log");
        String build_Option = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Build_option");
        String test_folder = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Test_Folder");
        String build_Step_option_4 = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Build_Step_option_4");
        String build_Step_option_2 = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Build_Step_option_2");
        String build_Step_option_3 = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Build_Step_option_3");
        String execution_Server_URL = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Execution_Server_URL");
        String base_URI = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Base_URI");
        Reporter.log("<br>1. Log into the Jenkins Application using the credentials " + username + " / " + password +"<br>");
        sequence.loginJenkins(username, password);
        Reporter.log("<br>2. Open the Test Jobs folder <br>");
        homePage.openJob(test_folder);
        Reporter.log("<br>3. Verify the "+project_Name+" job is present <br>");
        boolean isJobPresent_before = homePage.isJobPresentInJenkinsHomepage(project_Name);
        if(isJobPresent_before)
        {
            Reporter.log("<br>4. Delete the job which is present already <br>");
            jobCreationPage.hoverMouseOverJobName(project_Name);
            Logger.instance.info("Hover mouse on the Job " + project_Name);
            //homePage.openJob(project_Name);
            jobCreationPage.clickDeleteJob();
            homePage.dismissAlert();
            Logger.instance.info("Verify that popup message closed");
            boolean popUp = homePage.isAlertPresent();
            Assert.assertFalse(popUp, "Delete Porject confirmation messsage is not closed after clicking cancel");
            jobCreationPage.hoverMouseOverJobName(project_Name);
            Logger.instance.info("Click Delete Project");
            jobCreationPage.clickDeleteJob();
            homePage.acceptAlert();
            Utils.sleep(5);
            Reporter.log("<br>5. "+project_Name+" job deleted succesfully <br>");
        }

        homePage.clickNewItem();
        Reporter.log("<br>6. Click on 'New Item' to create a new job <br>");
        boolean okButtonDisabled = jobCreationPage.checkOKButtonState();
        Assert.assertFalse(okButtonDisabled, "OK buttotn is enabled before entering the Project name");
        Reporter.log("<br>7. Enter job name as " +project_Name+ " <br>");
        jobCreationPage.enterJenkinsJobName(project_Name);
        jobCreationPage.selectProjectType(project_Type);
        Reporter.log("<br>8. Select Project Type <br>");
        boolean okButtonEnabled = jobCreationPage.checkOKButtonState();
        Assert.assertTrue(okButtonEnabled, "OK buttotn is still disabled after entering the Project name");
        jobCreationPage.clickOKButton();
        Reporter.log("<br>9. Enter Project Description and verify the description box is vertically scrollable <br>");
        jobCreationPage.enterJobDescription(project_Description);
        jobCreationPage.expandJobDescriptionField();
        boolean isDescriptionTextBoxVerticallyScrollable = jobCreationPage.checkDescriptionTextBoxScrollable();
        Assert.assertTrue(isDescriptionTextBoxVerticallyScrollable, "Description textbox is not scrollable in vertical position");
        Reporter.log("<br>10. Select Build Environment options <br>");
        jobCreationPage.selectBuildEnvironment(build_Option);
        Reporter.log("<br>11. Select Build Step actions <br>");
        jobCreationPage.selectBuildStepOption(build_Step_option_4);
        jobCreationPage.enterAntTarget("compile");
        jobCreationPage.selectBuildStepOption(build_Step_option_2);
        jobCreationPage.selectMavenSnapshotCheck();
        jobCreationPage.selectBuildStepOption("Execute shell");
        jobCreationPage.selectBuildStepOption(build_Step_option_1);
        jobCreationPage.selectBuildStepOption(build_Step_option_3);
        jobCreationPage.enterExecServerURL(execution_Server_URL);
        jobCreationPage.enterMavenGoal(maven_goal);

        // radiobutton select
        Reporter.log("<br>12. Enable SVN radiobutton and verify the status <br>");
        jobCreationPage.enableSVNRadioButton();
        boolean isSelected = jobCreationPage.isSVNRadioButtonSelected();
        Assert.assertTrue(isSelected, "Sub Version is not getting selected");

        // frame handling
        Reporter.log("<br>13. Switch to 0th Frame and get Base URI value <br>");
        homePage.switchToFrame(0);
        String baseURI = jobCreationPage.getBaseURIFromFrame();
        System.out.println(baseURI);
        Assert.assertTrue(baseURI.contains(base_URI));
        Reporter.log("<br>14.  Switch to default frame <br>");
        homePage.switchToDefault();
        Reporter.log("<br>15. Select Post Build Action values <br>");
        jobCreationPage.selectPostBuildAction(post_build_action_1);
        jobCreationPage.selectPostBuildAction(post_build_action_2);
        Reporter.log("<br>16. Fill in the Email Notification details <br>");
        jobCreationPage.enterProjectRecipients(project_Recipient);
        jobCreationPage.enterProjectReplyTo(project_reply_to);

        jobCreationPage.selectContentType(content_type);
        jobCreationPage.enterDefaultSubject(default_Subject);
        jobCreationPage.enterDefaultContent(default_content);
        jobCreationPage.enterAttachment(attachment);
        jobCreationPage.selectBuildLog(build_log);
        Reporter.log("<br>17. Save the Project <br>");
        jobCreationPage.clickSaveButton();
        jobCreationPage.moveBack();
        boolean isJobPresent = homePage.isJobPresentInJenkinsHomepage(project_Name);
        Assert.assertTrue(isJobPresent, project_Name + " - is not created successfully");
        jobCreationPage.moveBack();
        Reporter.log("<br>18. Navigate to Build Executor and select Master branch <br>");
        jobCreationPage.clickBuildExecutorServerLink();
        jobCreationPage.clickMasterBranchServer();
        jobCreationPage.clickBuildHistoryLink();
        // check element is scrollable in Horizontal or vertical
        Reporter.log("<br>19. Verify timeline is horizontally scrollable <br>");
        boolean isBuildTimeLineHorizontallyScrollable = jobCreationPage.checkBuildTimeIsScrollable("Horizontal");
        Assert.assertTrue(isBuildTimeLineHorizontallyScrollable, "Timeline is not scrollable in horizontal position");

        //capture timeline table
        jobCreationPage.captureTimelineTable(testProtocol, testCaseName, "Job_Config_details");
        Reporter.log("<br>20. Logout from Jenkins Application <br>");
        homePage.clickLogout();
    }

    @Test(priority = 2, description = "Creating New Job From Existing One by Clone")
    public void createNewJobFromExistingOne() throws Exception {
        String testID = "23456";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "createNewJobFromExistingOne";
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String project_Name_1 = ExcelUtils.getTestData(testData, "Job_Creation", "12345", "Project_Name");
        String project_Name_2 = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Project_Name");
        String test_folder = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Test_Folder");

        sequence.loginJenkins(username, password);
        Reporter.log("<br>1. Logged in to Jenkins Application using the credentials " + username + " / " + password+ "<br>");
        homePage.openJob(test_folder);
        Reporter.log("<br>2. User Navigated into the test folder <br>");
        boolean isJobPresent_before = homePage.isJobPresentInJenkinsHomepage(project_Name_2);
        if(isJobPresent_before)
        {
            Reporter.log("<br>3. Click on the Job " + project_Name_2+ "<br>");
            homePage.openJob(project_Name_2);
            Reporter.log("<br>4. Click Delete Project <br>");
            jobCreationPage.clickDeleteJob();
            Reporter.log("<br>5. Click OK in the popup <br>");
            homePage.acceptAlert();
        }
        homePage.clickNewItem();
        Reporter.log("<br>6. Clicked the New Item link in home page <br>");

        boolean okButtonDisabled = jobCreationPage.checkOKButtonState();
        Assert.assertFalse(okButtonDisabled, "OK buttotn is enabled before entering the Project name");

        jobCreationPage.enterJenkinsJobName(project_Name_2);
        Reporter.log("<br>7. User entered Project name as " + project_Name_2 + "<br>");

        jobCreationPage.enterIntoCopyFromTextField(project_Name_1);
        Reporter.log("<br>8. User entered the project to be copied into the Copy from textfield <br>");

        boolean okButtonEnabled = jobCreationPage.checkOKButtonState();
        Assert.assertTrue(okButtonEnabled, "OK buttotn is still disabled after entering the Project name");
        jobCreationPage.clickOKButton();
        Reporter.log("<br>9. User navigated to configuration page <br>");
        jobCreationPage.clickSaveButton();
        Reporter.log("<br>10. Clicked the Save button <br>");

        jobCreationPage.moveBack();
        Reporter.log("<br>11. Return to dashboard and verify the created project <br>");

        boolean isJobPresent = homePage.isJobPresentInJenkinsHomepage(project_Name_2);
        Assert.assertTrue(isJobPresent, project_Name_2 + " - is not created successfully");

        homePage.clickLogout();
        Reporter.log("<br>12. Navigate to Job configuration Page <br>");
        loginPage.navigateToURL(appURL+"job/"+project_Name_2+"/configure");
        sequence.loginJenkins(username, password);
        Reporter.log("<br>13. Get Current Page URL <br>");
        String currentPageURL = loginPage.getCurrentUrl();
        Assert.assertEquals(currentPageURL, appURL+"job/"+project_Name_2+"/configure");
        Reporter.log("<br>14. Current Page URL is " + currentPageURL + "<br>");
        Reporter.log("<br>15. Capture the the job config page <br>");
        String configPageImage_Path = loginPage.captureScreen(testProtocol, testCaseName, "Job_Config_details");
        Assert.assertTrue(new File(configPageImage_Path).exists(), "Image is not captured successfully");
        Reporter.log("<br>16. Logged out from Jenkins Application <br>");
    }

    @Test(priority = 3, description = "Adding Additional Configuration to Existing Job")
    public void addAdditionalConfigToExistingJob() throws Exception {
        String testID = "ModifyJob-1";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "createNewJobFromExistingOne";

        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String project_Name_1 = ExcelUtils.getTestData(testData, "Job_Creation", "12345", "Project_Name");
        String project_Name_2 = ExcelUtils.getTestData(testData, "Job_Creation", "23456", "Project_Name");
        String test_folder = ExcelUtils.getTestData(testData, "Job_Creation", "23456", "Test_Folder");
        String build_Option = ExcelUtils.getTestData(testData, "Job_Creation", "12345", "Build_option");
        String new_Build_Option = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Build_option");
        String post_Build_Action = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Post_build_action_1");
        String trigger_Option_1 = ExcelUtils.getTestData(testData, "Job_Creation", testID, "Trigger_option_1");

        sequence.loginJenkins(username, password);
        Logger.instance.info("<br>1. Logged in to Jenkins Application using the credentials " + username + " / " + password +".</br>");
        Reporter.log("Logged in to Jenkins Application using the credentials " + username + " / " + password);

        homePage.openJob(test_folder);
        Logger.instance.info("User Navigated into the test folder");
        Reporter.log("<br>2. User Navigated into the test folder.</br>");

        Logger.instance.info("Click on the Job " + project_Name_1);
        homePage.openJob(project_Name_1);
        Reporter.log("<br>3. Clicked the Job" + project_Name_1 + " Successfully.</br>");
        Logger.instance.info("Click on the Configure button for the project " + project_Name_1);
        homePage.clickConfigure();
        Reporter.log("<br>4. Clicked Configure Link Successfully.</br>");

        Logger.instance.info("Uncheck the build environment option");
        jobCreationPage.deSelectBuildEnvironment((build_Option));
        Reporter.log("<br>5. Clicked DeSelect Build Environment Option Successfully.</br>");

        Logger.instance.info("Select new Option from Build Environment");
        jobCreationPage.selectBuildEnvironment(new_Build_Option);
        Reporter.log("<br>5. Selected new Option from Build Environment Successfully.</br>");


        jobCreationPage.selectPostBuildAction(post_Build_Action);
        Logger.instance.info("Click Advanced Settings option under Post Build Action");
        jobCreationPage.clickEmailNotificationsAdvancedSetting();
        Reporter.log("<br>6. Clicked Advanced Settings option under Post Build Action Successfully.</br>");

        jobCreationPage.selectTriggersList(trigger_Option_1);

        String[] allTriggersOptions = jobCreationPage.getAllTriggersOptions();
        Assert.assertTrue(allTriggersOptions.length==8, "Dropdown displays incorrect/additional values " + Arrays.deepToString(allTriggersOptions));
        Logger.instance.info("User navigated to configuration page");
        Reporter.log("<br>7. User Navigated to configuration page Successfully.</br>");

        jobCreationPage.swapTriggersList();
        jobCreationPage.clickSaveButton();
        Logger.instance.info("Clicked the Save button");
        Reporter.log("<br>8. Clicked Save Button Successfully.</br>");
        jobCreationPage.moveBack();

        jobCreationPage.moveBack();
        jobCreationPage.clickBuildExecutorServerLink();
        jobCreationPage.clickMasterBranchServer();
        jobCreationPage. clickLoadStatistics();
        boolean horScrollBar = homePage.isHorizontalScrollBarPresentInWebPageByJS();
        Assert.assertTrue(horScrollBar, "Horizontal scroll bar is not present in the build statistics page");

        homePage.clickLogout();
        Logger.instance.info("Logged out from Jenkins Application");
        Reporter.log("<br>9. Logged out from Jenkins Application Successfully.</br>");
    }

    @Test(priority = 4, description = "Downloading Build file from the job")
    public void downloadJobBuildInJenkins() throws Exception {
        String testID = "Test_234";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        int attempt = 1; int searchAttempts = 60;
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String projectName = ExcelUtils.getTestData(testData, "DownloadJobBuild", testID, "ProjectName");
        String downloadPath = Utils.getAutomationProperties().getProperty("DownloadDir");
        sequence.loginJenkins(username, password);
        Assert.assertTrue(homePage.isHomePageTitleExists(), "HomePage Title doesn't exists");
        Reporter.log("<br>1. Verified the Jenkins Home Page Title after logged into application.<br>");
        homePage.clickJobInJenkinsHomepage(projectName);
        Assert.assertTrue(jobProjectPage.isJobProjectPageTitleExists(projectName), "JobPage Title doesn't exists");
        Reporter.log("<br>2. Verified '" + projectName + "' Page Title after clicked '" + projectName +"' Job.<br>");
        jobProjectPage.clickWorkSpaceLink();
        Assert.assertTrue(workspacePage.isWorkspacePageTitleExists(projectName), "Workspace Title doesn't exists");
        Reporter.log("<br>3. Verified '" + projectName + "' Job Workspace Page Title after clicked Workspace Folder.<br>");
        workspacePage.clickAllFilesInZipLink(projectName);
        Reporter.log("<br>4. Verified Zip file is Downloaded Successfully.<br>");
    }

    @Test(priority = 5, description = "Verifying the dropdowns in Build with parametrized job")
    public void buildWithParamDropdownOptions() throws Exception {
        String testID = "546548";
        Reporter.log("<br><strong>Testcase ID: </strong>"+testID + "<br>");
        String testCaseName = "buildWithParamDropdownOptions";
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String jobName1 = ExcelUtils.getTestData(testData, "BuildWithParameters", testID, "JobName1");
        String jobName2 = ExcelUtils.getTestData(testData, "BuildWithParameters", testID, "JobName2");
        String expectedCount = ExcelUtils.getTestData(testData, "BuildWithParameters", testID, "MenuCount");
        sequence.loginJenkins(username,password);
        homePage.openJob(jobName1);
        homePage.openJob(jobName2);
        Reporter.log("<br>1. Build with parameterized job is opened.<br>");
        jobInfoPage.clickBuildWithParam();
        Thread.sleep(1000);
        String[] menuLength = jobInfoPage.buildWithParamDropdownOptions();
        int actualCount = menuLength.length;
        Assert.assertTrue(expectedCount.contains(String.valueOf(actualCount)));
        Reporter.log("<br>2. The dropdown count values matches with the expected count.<br>");
        boolean verifyOptions = jobInfoPage.verifyBuildWithParamDropdown();
        Assert.assertTrue(verifyOptions,"The verification of dropdown options are failed");
        Reporter.log("<br>3. Verification for the dropdown values are done successfully.<br>");
    }

    @Test(priority = 6, description = "Verifying the status of build in console output")
    public void buildStatusInConsoleOutput() throws Exception {
        String testID = "546548";
        Reporter.log("<br><strong>Testcase ID: </strong>"+testID + "<br>");
        String testCaseName = "buildStatusInConsoleOutput";
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String jobName1 = ExcelUtils.getTestData(testData, "BuildWithParameters", testID, "JobName1");
        String jobName2 = ExcelUtils.getTestData(testData, "BuildWithParameters", testID, "JobName2");
        sequence.loginJenkins(username,password);
        homePage.openJob(jobName1);
        String job1 = homePage.getCurrentUrl();
        Assert.assertTrue(job1.contains("Test%20Jobs"),"Test Job folder is not opened");
        Reporter.log("<br>1. Test Job folder is opened.<br>");
        homePage.openJob(jobName2);
        String job2 = homePage.getCurrentUrl();
        Assert.assertTrue(job2.contains("Fixed_Testing_Job_Parameterized"),"Fixed Testing Job Parameterized folder is not opened");
        Reporter.log("<br>2. Fixed Testing Job Parameterized folder is opened.<br>");
        jobInfoPage.clickBuildWithParam();
        Utils.sleep(10);
        String buildLink = homePage.getCurrentUrl();
        Assert.assertTrue(buildLink.contains("build"),"Build with parameter link is not clicked");
        Reporter.log("<br>3. Build with parameter link is clicked.<br>");
        jobInfoPage.clickOnBuildButton();
        Utils.sleep(15);
        jobInfoPage.clickOnLatestBuildLink();
        jobInfoPage.clickOnConsoleOutputLink();
        Utils.sleep(10);
        String consoleLink = homePage.getCurrentUrl();
        Assert.assertTrue(consoleLink.contains("console"),"Console output link is not clicked");
        Reporter.log("<br>4. Console output link is clicked.<br>");
        String[] text = jobInfoPage.getConsoleOutputMsg().split("\n");
        String result = text[text.length-1];
        Logger.instance.info(result);
        Assert.assertTrue(result.contains("SUCCESS"), "BUILD IS NOT SUCCESS");
        Reporter.log("<br>5. Status of the build is Success.<br>");
    }

    @Test(priority = 7, description = "Checking Functionality of the Job Whether it's working or not")
    public void clickTestJobFolderInJenkins() throws Exception {
        String testID = "Test_235";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        String testFolderName = ExcelUtils.getTestData(testData, "TestJobFolder", testID, "TestFolder");
        String jobName = ExcelUtils.getTestData(testData, "TestJobFolder", testID, "JobName");
        String description = ExcelUtils.getTestData(testData, "TestJobFolder", testID, "Description");
        sequence.loginJenkins(username, password);
        Assert.assertTrue(homePage.isHomePageTitleExists(), "HomePageTitle doesn't exists");
        Reporter.log("<br>1. Verified the Jenkins Home Page Title after logged into application.<br>");
        homePage.clickJobInJenkinsHomepage(testFolderName);
        Assert.assertTrue(jobCreationPage.isFolderProjectPageTitleExists(testFolderName), "FolderProjectPageTitle doesn't exists");
        Reporter.log("<br>2. Verified '" + testFolderName + "' Page Title after clicked '" + testFolderName +"' Folder.<br>");
        homePage.openJob(jobName);
        Assert.assertTrue(jobProjectPage.isJobProjectPageTitleExists(jobName), "JobPageTitle doesn't exists");
        Reporter.log("<br>3. Verified '" + jobName + "' Page Title after clicked '" + jobName +"' Job.<br>");
        jobProjectPage.clickConfigure();
        Assert.assertTrue(jobCreationPage.isConfigPageTitleExists(jobName), "ConfigurePageTitle doesn't exists");
        Reporter.log("<br>4. Verified the Jenkins Configure Page Title after clicked Configure Link.<br>");
        jobCreationPage.clickGeneralTab();
        Reporter.log("<br>5. Clicked General Tab Successfully.<br>");
        jobCreationPage.clickGitBucketTab();
        Reporter.log("<br>6. Clicked Git Tab Successfully.<br>");
        jobCreationPage.clickGeneralTab();
        Reporter.log("<br>7. Again Clicked General Tab Successfully.<br>");
        jobCreationPage.enterJobDescription(description);
        Reporter.log("<br>8. Entered Description as '" + description +"' in Description TextBox Successfully.<br>");
        Assert.assertTrue(jobCreationPage.isPostBuildActionSectionTitleExists(), "PostBuildActionHeaderSection doesn't exists in Config Page.");
        jobCreationPage.clickSaveButton();
        Reporter.log("<br>9. Clicked Save Button Successfully.<br>");
        Logger.instance.info("Configured Successfully");
        Assert.assertTrue(jobProjectPage.isJobProjectPageTitleExists(testFolderName), "JobPageTitle doesn't exists");
        Reporter.log("<br>10. Verified Job is Exists after entering the required details.<br>");
    }
    @Test(priority = 8, description = "Searching Existing Job in Jenkins ")
    public void jenkinsSearchExistingJob() throws Exception {
        String testID = "987460";
        Reporter.log("<br><strong>Testcase ID: </strong>" + testID + "<br>");
        String testCaseName = "existingJob";
        String testJob1 = ExcelUtils.getTestData(testData, "JenkinsInfo", testID, "JobName1");
        String testJob2 = ExcelUtils.getTestData(testData, "JenkinsInfo", testID, "JobName2");
        String username = Utils.getAutomationProperties().getProperty("Username");
        String password = Utils.getAutomationProperties().getProperty("Password");
        sequence.loginJenkins(username, password);
        homePage.searchJob(testJob1);
        String job1 = homePage.getCurrentUrl();
        Assert.assertTrue(job1.contains("Test%20Jobs"),"Test Job folder is not opened");
        Reporter.log("<br>1. Test Job folder is opened.<br>");
        homePage.searchJob(testJob2);
        String job2 = homePage.getCurrentUrl();
        Assert.assertTrue(job2.contains("Fixed_Testing_Job_Parameterized"),"Fixed Testing Job Parameterized folder is not opened");
        Reporter.log("<br>2. Fixed Testing Job Parameterized folder is opened.<br>");
        jobInfoPage.clickBuildWithParam();
        String buildLink = homePage.getCurrentUrl();
        Assert.assertTrue(buildLink.contains("build"),"Build with parameter link is not clicked");
        Reporter.log("<br>3. Build with parameter link is clicked.<br>");
        jobInfoPage.clickOnBuildButton();
        homePage.clickLastBuild();
        jobInfoPage.clickOnConsoleOutputLink();
        Reporter.log("<br>4. Console output link is clicked.<br>");
        String consoleLink = homePage.getCurrentUrl();
        Assert.assertTrue(consoleLink.contains("console"),"Console output link is not clicked");
        String[] text = jobInfoPage.getConsoleOutputMsg().split("\n");
        String result = text[text.length-1];
        Logger.instance.info(result);
        Assert.assertTrue(result.contains("SUCCESS"), "BUILD IS NOT SUCCESS");
        Reporter.log("<br>5. Status of the build is Success.<br>");
    }

    @AfterMethod
    public void tearDown() {
        if (theDriver != null) {
            TestClass.getInstance().removeDriver();
        }
    }
}
