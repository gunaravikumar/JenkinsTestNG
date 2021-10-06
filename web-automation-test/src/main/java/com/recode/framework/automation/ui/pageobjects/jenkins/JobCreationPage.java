package com.recode.framework.automation.ui.pageobjects.jenkins;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class JobCreationPage extends PageClass {

    public JobCreationPage(RemoteWebDriver theWebDriver)
    {
        super(theWebDriver);
    }

    //Locator values here

    private By folderProjectPageHeader(String folderName) {
        return By.xpath("//h1[contains(text(),'" + folderName + "')]");
    }
    private By generalTab() {
        return By.xpath("//div[text()='General' and contains(@class,'tab config-section')]");
    }
    private By gitBucketTab() {
        return By.xpath("//div[text()='GitBucket' and contains(@class,'tab config-section')]");
    }
    private By postBuildActionsHeader() {
        return By.xpath("//div[text()='Post-build Actions' and contains(@class,'section-header')]");
    }

    private By projectNameTextField() {
        return By.id("name");
    }

    private WebElement projectType(String jobType) throws Exception {
        return findChildElementUsingParentLocator(By.id("items"), By.xpath("//li[contains(.,'"+jobType+"')]/label"), 15);
    }
    private By OkButton() {
        return By.id("ok-button");
    }

    private By jobDescription() {
        return By.name("description");
    }

    private By buildStepDropdown() {
        return By.xpath("//body[@id='jenkins']/descendant::button[contains(.,'Add build step')]");
    }
    private By postBuildActionDropdown() {
        return By.xpath("//body[@id='jenkins']/descendant::button[contains(.,'Add post-build action')]");
    }
    private By dropdownOptions(String option){
        return By.linkText(option);
    }
    private By mavenGoalTextField() {
        return By.xpath("(//input[@name='_.targets'])[last()]");
    }

    private By projectRecipientTextField() {
        return By.name("project_recipient_list");
    }

    private By projectReplyTo() {
        return By.name("project_replyto");
    }

    private By contentTypeDropdown() {
        return By.name("project_content_type");
    }

    private By defaultSubjectField() {
        return By.name("project_default_subject");
    }
    private By defaultContentField() {
        return By.name("project_default_content");
    }
    private By attachmentField() {
        return By.name("project_attachments");
    }

    private By buildLogDropDown() {
        return By.name("project_attach_buildlog");
    }
    private By saveProjectButton() {
        return By.xpath("//button[contains(.,'Save')]");
    }

    private By  postBuildActionDownScrollBar() {
        return By.xpath("//body[@id='jenkins']/descendant::div[contains(@class,'bottomscrollbar')]");
    }

    private By backToDashboardLink() {
        return By.linkText("Back to Dashboard");
    }

    private By copyFromTextField() {
        return By.id("from");
    }

    private By buildEnvironment(String option) {
        return By.xpath("//body[@id='jenkins']/descendant::td[contains(.,'"+option+"')]/input");
    }

    private By deleteJob() {
        return By.linkText("Delete Project");
    }

    private By up() {
        return By.linkText("Up");
    }

    private By InvokeAntTargetTextField() {
        return By.xpath("(//tr[.//text()='Targets'])[last()]//input[@name='_.targets']");
    }

    private By mavenCheckbox() {
        return By.xpath("//input[@name='_.check']");
    }
    private By autoExecServerURLField() {
        return By.xpath("//input[@name='selenium_autoexec.serverUrl']");
    }

    private By frameHTML() {
        return By.xpath("//html/body");
    }

    private By svnRadioButton() {
        return By.id("radio-block-2");
    }
    private By buildExecutorServer() {
        return By.xpath("//div[@id='side-panel']//a[contains(.,'Build Executor')]");
    }
    private By buildServerPageHeader() {
        return By.id("computers");
    }
    private By masterLink() {
        return By.xpath("//a[contains(.,'master')]");
    }
    private By buildHistoryLink() {
        return By.xpath("//a[contains(.,'Build History')]");
    }
    private By buildTimelineTable() {
        return By.xpath("//div[@id='resizeContainer']/div[contains(@class,' timeline-horizontal')]");
    }
    private By jobNameLink(String job) {
        return By.xpath("//a[contains(.,'"+job+"')]");
    }
    private By floatingMenu() {
        return By.id("menuSelector");
    }
    private By emailNotificationsAdvancedSettingButton() {
        return By.xpath("//button[.//text()='Advanced Settings...']");
    }
    private By addButtonUnderTrigger(){
        return By.xpath("(//body[@id='jenkins']/descendant::button[.//text()='Add'])[last()]");
    }
    private By visibleDropdownList()
    {
        return By.xpath("//div[contains(@class,'visible')]//ul//li/a");
    }
    private By developers() {
        return By.xpath("(//div[contains(@class,'dd-handle')])[last()-1]");
    }
    private By culprits() {
        return By.xpath("(//div[contains(@class,'dd-handle')])[last()]");
    }
    private By loadStatisticsLink(){
        return By.xpath("//a[contains(.,'Load Statistics')]");
    }
    //Test Methods here

    /**
     * This Method is used to enter the name of the Job
     * @param jobName - Job name
     * @throws Exception
     */
    public void enterJenkinsJobName(String jobName) throws Exception {
        sendKeys(projectNameTextField(), jobName);
    }

    /**
     * This Method is used to select the specified Job type
     * @param jobType - Any available job type
     * @throws Exception
     */
    public void selectProjectType(String jobType) throws Exception {
        clickByJS(projectType(jobType));
    }

    /**
     * This Method is used to click on the OK button after selecting Job type
     */
    public void clickOKButton() throws Exception {
        click(OkButton());
    }

    /**
     * This Method is used to enter the Job description
     * @param description
     * @throws Exception
     */
    public void enterJobDescription(String description) throws Exception {
        boolean isExists = isAttributeExists(jobDescription(), "rows");
        if (isExists) {
            clearAndSendKeys(jobDescription(), description);
            Logger.instance.info(description + " : Entered in Description TextArea.");
        } else {
            Logger.instance.error(description + " : is not entered in Description TextArea because of attribute is not exists.");
        }
    }

    /**
     * This Method is used to click on the build-step dropdown to open it
     * @throws Exception
     */
    public void selectBuildStepOption(String option) throws Exception {
        boolean isOptionVisible;
                click(buildStepDropdown());
                searchAttempts = 1;
                try {
                    findElement(dropdownOptions(option));
                    isOptionVisible = true;
                }
                catch (Exception e) {
                    isOptionVisible = false;
                }
                finally {
                    searchAttempts = 1;
                }
        //boolean isOptionVisible = isElementVisible(findElement(buildStepOption(option)));
        if (!isOptionVisible) {
            hoverElement(postBuildActionDownScrollBar());
        }
        click(dropdownOptions(option));
        pageLoadWait();
    }

    /**
     * This Method is used to enter the Goal text field
     * @param mavenGoal
     * @throws Exception
     */
    public void enterMavenGoal(String mavenGoal) throws Exception {
        sendKeys(mavenGoalTextField(), mavenGoal);
    }

    /**
     * This Method is used to select a option from Post-build-action dropdown
     * @param option
     * @throws Exception
     */
    public void selectPostBuildAction(String option) throws Exception {
        try {
            click(postBuildActionDropdown());
            searchAttempts = 1;
            boolean isOptionVisible = isElementVisible(findElement(dropdownOptions(option)));
            if (!isOptionVisible) {
                hoverElement(postBuildActionDownScrollBar());
            }
            click(dropdownOptions(option));
            pageLoadWait();
        }
        catch (Exception e)
        {
            hoverElement(postBuildActionDownScrollBar());
            click(dropdownOptions(option));
            pageLoadWait();
        }
        finally {
            searchAttempts = 6;
        }
    }

    /**
     * This Method is used to enter email id in recipient text field
     * @param recipient
     */
    public void enterProjectRecipients(String recipient) {
        clearAndSendKeys(projectRecipientTextField(), recipient);
    }

    /**
     * This Method is used to enter the email id for the reply-to text field
     * @param recipient
     */
    public void enterProjectReplyTo(String recipient) {
        clearAndSendKeys(projectReplyTo(), recipient);
    }

    /**
     * This Method is used to select an option from content type dropdown
     * @param contentType
     */
    public void selectContentType(String contentType) {
        selectFromDropdownByVisibleText(contentTypeDropdown(), contentType);
    }

    /**
     * This Method is used to enter the subject into default subject text field
     * @param subject
     */
    public void enterDefaultSubject(String subject) {
        clearAndSendKeys(defaultSubjectField(), subject);
    }

    /**
     * This Method is used to enter the content text into default content text field
     * @param content
     */
    public void enterDefaultContent(String content) {
        clearAndSendKeys(defaultContentField(), content);
    }

    /**
     * This Method is used to enter the attachment details
     * @param attachment
     */
    public void enterAttachment(String attachment) {
        clearAndSendKeys(attachmentField(), attachment);
    }

    /**
     * This Method is used to select option from Build log dropdown
     * @param buildLog
     */
    public void selectBuildLog(String buildLog) {
        selectFromList(buildLogDropDown(), buildLog, 0);
        //selectFromDropdownByVisibleText(buildLogDropDown(), buildLog);
    }

    /**
     * This Method is used to click on the Save button displaying in the page after creating job with specified details
     * @throws Exception
     */
    public void clickSaveButton() throws Exception {
        scrollToTopOfScreen();
        click(saveProjectButton());
        Logger.instance.info("Clicked Save Button Successfully in Config Page");
        pageLoadWait();
    }

    /**
     * This Method is used to navigate to Dashboard page
     * @throws Exception
     */
    public void clickBackToDashboard() throws Exception {
        click(backToDashboardLink());
        pageLoadWait();
    }

    /**
     * This Method is used to enter the Job name into copy from text field
     * @param text
     * @throws Exception
     */
    public void enterIntoCopyFromTextField(String text) throws Exception {
        sendKeys(copyFromTextField(), text);
    }

    /**
     * This Method is used to get the Ok button state (enabled/disabled)
     * @return
     * @throws Exception
     */
    public boolean checkOKButtonState() throws Exception {
        return checkElementStateUsingDOM(OkButton());
    }

    /**
     * This method is used to select the checkbox under Build Environment
     * @param envOption
     */
    public void selectBuildEnvironment(String envOption) {
        setCheckBox(buildEnvironment(envOption), true);
    }

    public void deSelectBuildEnvironment(String envOption) {
        uncheckCheckBox(buildEnvironment(envOption), true);
    }

    /**
     * This Method is used to delete the Job from Jenkins
     * @throws Exception
     */
    public void clickDeleteJob() throws Exception {
        click(deleteJob());
    }

    /**
     * This Method is used to go back to the
     * @throws Exception
     */
    public void moveBack() throws Exception {
        clickByJS(up());
        pageLoadWait();
    }

    /**
     * This Method is used to verify job project page title in Jenkins Application
     *
     * @param folderName
     */
    public boolean isFolderProjectPageTitleExists(String folderName) {
        pageLoadWait();
        return isElementEnabled(folderProjectPageHeader(folderName));
    }

    /**
     * This Method is used to verify config page title in Jenkins Application
     */
    public boolean isConfigPageTitleExists(String jobName) {
        pageLoadWait();
        String getConfigPageTitle = getPageTitle().trim();
        if (getConfigPageTitle.equalsIgnoreCase(jobName + " Config [Jenkins]")) {
            Logger.instance.info("Config Page is displayed Successfully");
            return true;
        } else {
            Logger.instance.error("Failed to open config");
        }
        return false;
    }


    /**
     * This Method is used to click general tab in configure page
     */
    public void clickGeneralTab() {
        boolean isEnableGeneralTab = isElementEnabled(generalTab());
        if (isEnableGeneralTab) {
            clickByJS(generalTab());
            Logger.instance.info("Clicked General Tab Successfully in Config Page");
        } else {
            Logger.instance.info("General Tab Element is not Enabled in Config Page");
        }
    }

    /**
     * This Method is used to click git bucket tab in configure page
     */
    public void clickGitBucketTab() {
        boolean isEnableGitBucketTab = isElementEnabled(gitBucketTab());
        if (isEnableGitBucketTab) {
            clickByJS(gitBucketTab());
            Logger.instance.info("Clicked GitBucket Tab Successfully in Config Page");
        } else {
            Logger.instance.info("GitBucket Tab Element is not Enabled in Config Page");
        }
    }


    /**
     * This Method is used to verify post build action title in config page
     */
    public boolean isPostBuildActionSectionTitleExists() {
        scrollToBottomOfScreen();
        return isElementPresent(postBuildActionsHeader());
    }

    public void enterAntTarget(String antTargetText) throws Exception {
        sendKeys(InvokeAntTargetTextField(), antTargetText);
    }

    public void selectMavenSnapshotCheck() {
        setCheckBox(mavenCheckbox(), true);
    }
    public void enterExecServerURL(String executionURL) throws Exception {
        sendKeys(autoExecServerURLField(), executionURL);
    }
    public String getBaseURIFromFrame() {
        return getElementsAttribute(frameHTML(), "baseURI");
    }

    public void enableSVNRadioButton() throws Exception {
        scrollToElementByJS(findElement(svnRadioButton()));
        selectRadioButton(svnRadioButton(), true);
    }
    public boolean isSVNRadioButtonSelected() {
       return  isElementSelected(svnRadioButton());
    }

    public void clickBuildExecutorServerLink() throws Exception {
        click(buildExecutorServer());
    }

    public void clickMasterBranchServer() throws Exception {
        WebElement masterLink = findChildElementUsingParentLocator(buildServerPageHeader(), masterLink(), 10);
        clickByJS(masterLink);
    }
    public void clickBuildHistoryLink() throws Exception {
        click(buildHistoryLink());
    }

    public boolean checkBuildTimeIsScrollable(String scrollPos) throws Exception {
        click(buildTimelineTable());
        Thread.sleep(1000);
        if(scrollPos.equals("Horizontal"))
            return isElementHorizontallyScrollable(buildTimelineTable());
        else
            return isElementVerticallyScrollable(buildTimelineTable());
    }

    public void captureTimelineTable(String testProtocol, String testCaseName, String imageName) {
        captureElement(buildTimelineTable(), testProtocol, testCaseName, imageName);
    }

    public void hoverMouseOverJobName(String jobName) {
        hoverAndClickElement(jobNameLink(jobName), floatingMenu());
    }

    public boolean checkDescriptionTextBoxScrollable() {
        return isElementVerticallyScrollable(jobDescription());
    }

    public void expandJobDescriptionField() {
        int attempt = 0;
            while(!isElementVerticallyScrollable(jobDescription()) && attempt <=8 ) {
                keysAction(Keys.ENTER);
                attempt++;
            }
    }

    public void clickEmailNotificationsAdvancedSetting() throws Exception {
        click(emailNotificationsAdvancedSettingButton());
    }

    public void clickAddButtonUnderTriggers() throws Exception {
        click(addButtonUnderTrigger());
    }

    public void selectTriggersList(String option) throws Exception {
        click(addButtonUnderTrigger());
        click(dropdownOptions(option));
        pageLoadWait();
    }

    public String[] getAllTriggersOptions() {
        return getDropdownOptions(addButtonUnderTrigger(), visibleDropdownList());
    }

    public void swapTriggersList() {
        dragAndDrop(developers(), culprits());
    }

    public void clickLoadStatistics() throws Exception {
        click(loadStatisticsLink());
    }

}
