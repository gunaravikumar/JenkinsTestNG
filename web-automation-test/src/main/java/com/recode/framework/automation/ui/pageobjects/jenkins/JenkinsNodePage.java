package com.recode.framework.automation.ui.pageobjects.jenkins;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;

public class JenkinsNodePage extends PageClass {

    public JenkinsNodePage(RemoteWebDriver theWebDriver) {
        super(theWebDriver);
    }

    //Locator values here
    private By newNodeLink() {
        return By.xpath("//a[@title='New Node']");
    }

    private By nodeComputerTable() {
        return By.cssSelector("table#computers");
    }

    private By nodeNameTextBox() {
        return By.id("name");
    }

    private By permanentAgentRadioButton() {
        return By.cssSelector("td > input[name='mode']");
    }

    private By okButton() {
        return By.id("ok-button");
    }

    private By nodeDescriptionTextBox() {
        return By.name("_.nodeDescription");
    }

    private By numOfExecutorsTextBox() {
        return By.name("_.numExecutors");
    }

    private By remoteRootDirectoryTextBox() {
        return By.name("_.remoteFS");
    }

    private By labelsTextBox() {
        return By.name("_.labelString");
    }

    private By usageDropDown() {
        return By.name("mode");
    }

    private By launchMethodDropDown() {
        return By.cssSelector("tr:nth-of-type(19) > .setting-main > .dropdownList.setting-input");
    }

    private By disabledWorkingDirCheckBox() {
        return By.name("_.disabled");
    }

    private By customWorkDirTextBox() {
        return By.name("_.workDirPath");
    }

    private By internalDataDirTextBox() {
        return By.name("_.internalDir");
    }

    private By failIfWorkDirCheckBox() {
        return By.name("_.failIfWorkDirIsMissing");
    }

    private By webSocketCheckBox() {
        return By.name("_.webSocket");
    }

    private By availabilityDropDown() {
        return By.cssSelector("tr:nth-of-type(22) > .setting-main > .dropdownList.setting-input");
    }

    private By disableDeferredWipeOutNodePropertyCheckBox() {
        return By.name("hudson-plugins-ws_cleanup-DisableDeferredWipeoutNodeProperty");
    }

    private By environmentVariablesNodePropertyCheckBox() {
        return By.name("hudson-slaves-EnvironmentVariablesNodeProperty");
    }

    private By notifyWhenNodeOnlineStatusChangesCheckBox() {
        return By.name("org-jenkinsci-plugins-mailwatcher-WatcherNodeProperty");
    }

    private By toolLocationNodePropertyCheckBox() {
        return By.name("hudson-tools-ToolLocationNodeProperty");
    }

    private By saveButton() {
        return By.xpath("//button[text()='Save']");
    }

    private By advancedButton() {
        return By.id("yui-gen3-button");
    }

    private By tunnelConnectionTextBox() {
        return By.name("_.tunnel");
    }

    private By jvmOptionsTextBox() {
        return By.name("_.vmargs");
    }

    private By deleteAgentLink(){ return By.xpath("//a[@title='Delete Agent']");}

    private By yesButton() {
        return By.xpath("//button[text()='Yes']");
    }

    //Test Methods here

    /**
     * This Method is used to verify node page title in Jenkins Application
     */
    public boolean isNodePageTitleExists() {
        pageLoadWait();
        String getNodePageTitle = getPageTitle().trim();
        if (getNodePageTitle.contains("Nodes")) {
            Logger.instance.info("Nodes Page is opened Successfully");
            return true;
        } else {
            Logger.instance.error("Failed to Open Nodes Page");
        }
        return false;
    }

    /**
     * This method is to click on the NewNode link in Node Page
     */
    public void clickNewNodeLink() throws Exception {
        hoverAndClick(newNodeLink());
    }

    /**
     * This Method is used to click node in Node page
     */
    public void clickNodeInJenkinsNodePage(String nodeName) {
        clickColumnValueInTable(nodeComputerTable(), "Name  ↓", nodeName);
    }

    /**
     * This Method is used to enter node name in Node page
     * @param nodeName
     */
    public void enterNodeNameInTextBox(String nodeName) throws Exception {
        sendKeys(nodeNameTextBox(), nodeName);
    }

    /**
     * This Method is used to click permanent age radio button in Node page
     */
    public void clickPermanentAgentRadioButton() {
        boolean isClick = isElementSelected(permanentAgentRadioButton());
        if (!isClick) {
            selectRadioButton(permanentAgentRadioButton(), false);
            Logger.instance.info("Clicked PermanentAgent Radio Button Successfully.");
        } else {
            Logger.instance.info("Already Selected PermanentAgent Radio Button.");
        }
    }

    /**
     * This Method is used to click ok button in Node page
     */
    public void clickOKButton() {
        boolean isExists = isElementClickable(okButton());
        if (isExists) {
            clickByJS(okButton());
            Logger.instance.info("Clicked Ok Button after enabling 'OK' button Successfully.");
        } else {
            Logger.instance.error("Unable to click button because 'OK' button is not enabled.");
        }
    }

    /**
     * This Method is used to enter node description in Node creation page
     * @param description
     */
    public void enterNodeDescriptionInTextBox(String description) throws Exception {
        sendKeys(nodeDescriptionTextBox(), description);
    }

    /**
     * This Method is used to enter number of executor in Node creation page
     * @param numOfExecutors
     */
    public void enterNumberOfExecutorInTextBox(String numOfExecutors) throws Exception {
        clearAndSendKeys(numOfExecutorsTextBox(), numOfExecutors);
    }

    /**
     * This Method is used to enter remote root directory in Node creation page
     * @param rootDirectory
     */
    public void enterRemoteRootDirectoryInTextBox(String rootDirectory) throws Exception {
        clearAndSendKeys(remoteRootDirectoryTextBox(), rootDirectory);
    }

    /**
     * This Method is used to enter labels in Node creation page
     * @param labels
     */
    public void enterLabelsInTextBox(String labels) throws Exception {
        clearAndSendKeys(labelsTextBox(), labels);
    }

    /**
     * This Method is used to select 'Use this node as much as possible' usage option in Node creation page
     */
    public void selectUseThisNodeAsMuchAsPossibleInUsageDropDown() throws Exception {
        selectFromDropdownByIndex(usageDropDown(), 0);
        Logger.instance.info("Selected 'Use this node as much as possible' option from Usage DropDown.");
    }

    /**
     * This Method is used to select 'Only build jobs with label expressions matching this node' usage option in Node creation page
     */
    public void selectOnlyBuildJobsInUsageDropDown() throws Exception {
        selectFromDropdownByIndex(usageDropDown(), 1);
        Logger.instance.info("Selected 'Only build jobs with label expressions matching this node' option from Usage DropDown.");
    }

    /**
     * This Method is used to select 'Launch agent by connecting it to the master' option from LaunchMethod DropDown in Node creation page
     */
    public void selectLaunchAgentByConnectingToMaster() throws Exception {
        selectFromDropdownByIndex(launchMethodDropDown(), 0);
        Logger.instance.info("Selected 'Launch agent by connecting it to the master' option from LaunchMethod DropDown.");
    }

    /**
     * This Method is used to select 'Launch agent via execution of command on the master' option from LaunchMethod DropDown in Node creation page
     */
    public void selectLaunchAgentViaExecutionOfCommandToMaster() throws Exception {
        selectFromDropdownByIndex(launchMethodDropDown(), 1);
        Logger.instance.info("Selected 'Launch agent via execution of command on the master' option from LaunchMethod DropDown.");
    }

    /**
     * This Method is used to select 'Launch agents via SSH' option from LaunchMethod DropDown in Node creation page
     */
    public void selectLaunchAgentViaSSHToMaster() throws Exception {
        selectFromDropdownByIndex(launchMethodDropDown(), 2);
        Logger.instance.info("Selected 'Launch agents via SSH' option from LaunchMethod DropDown.");
    }

    /**
     * This Method is used to select 'Let Jenkins control this Windows agent as a Windows service' option from LaunchMethod DropDown in Node creation page
     */
    public void selectJenkinsControlThisWindowAgent() throws Exception {
        selectFromDropdownByIndex(launchMethodDropDown(), 3);
        Logger.instance.info("Selected 'Let Jenkins control this Windows agent as a Windows service' option from LaunchMethod DropDown.");
    }

    /**
     * This Method is used to click Disable Work Dir CheckBox in Node page
     */
    public void clickDisableWorkDirCheckBox() {
        boolean isCheck = isElementSelected(disabledWorkingDirCheckBox());
        if (!isCheck) {
            clickByJS(disabledWorkingDirCheckBox());
            Logger.instance.info("Clicked Disable Work Dir CheckBox Successfully.");
        } else {
            Logger.instance.info("Already Checked Disable Work Dir CheckBox");
        }
    }

    /**
     * This Method is used to enter Custom Work Directory in Node creation page
     * @param path
     */
    public void enterCustomWorkDirPathInTextBox(String path) throws Exception {
        sendKeys(customWorkDirTextBox(), path);
    }

    /**
     * This Method is used to enter Internal Directory in Node creation page
     * @param internalDir
     */
    public void enterInternalDirInTextBox(String internalDir) throws Exception {
        clearAndSendKeys(internalDataDirTextBox(), internalDir);
    }

    /**
     * This Method is used to click FailIfWorkDirIsMissing CheckBox in Node page
     */
    public void clickFailIfWorkDirIsMissingCheckBox() {
        boolean isCheck = isElementSelected(failIfWorkDirCheckBox());
        if (!isCheck) {
            clickByJS(failIfWorkDirCheckBox());
            Logger.instance.info("Clicked FailIfWorkDirIsMissing CheckBox Successfully.");
        } else {
            Logger.instance.info("Already Checked FailIfWorkDirIsMissing CheckBox");
        }
    }

    /**
     * This Method is used to click Use WebSocket CheckBox in Node page
     */
    public void clickUseWebSocketCheckBox() {
        boolean isCheck = isElementSelected(webSocketCheckBox());
        if (!isCheck) {
            clickByJS(webSocketCheckBox());
            Logger.instance.info("Clicked Use WebSocket CheckBox Successfully.");
        } else {
            Logger.instance.info("Already Checked Use WebSocket CheckBox");
        }
    }

    /**
     * This Method is used to click Advanced button in Node page
     */
    public void clickAdvancedButton() {
        clickByJS(advancedButton());
        Logger.instance.info("Clicked Advanced Button Successfully.");
    }

    /**
     * This Method is used to enter Tunnel connection through TextBox in Node creation page
     * @param tunnelConnection
     */
    public void enterTunnelConnectionThroughInTextBox(String tunnelConnection) throws Exception {
        sendKeys(tunnelConnectionTextBox(), tunnelConnection);
    }

    /**
     * This Method is used to enter JVM options in TextBox from Node creation page
     * @param jvmOptions
     */
    public void enterJVMOptionsInTextBox(String jvmOptions) throws Exception {
        sendKeys(jvmOptionsTextBox(), jvmOptions);
    }

    /**
     * This Method is used to select 'Keep this agent online as much as possible' option from availability in Node creation page
     */
    public void selectKeepThisAgentOnlineAsMuchAsPossible() throws Exception {
        selectFromDropdownByIndex(availabilityDropDown(), 0);
        Logger.instance.info("Selected 'Keep this agent online as much as possible' option from availability DropDown.");
    }

    /**
     * This Method is used to select 'Bring this agent online according to a schedule' option from availability in Node creation page
     */
    public void selectBringThisAgentOnlineAccordingToSchedule() throws Exception {
        selectFromDropdownByIndex(availabilityDropDown(), 1);
        Logger.instance.info("Selected 'Bring this agent online according to a schedule' option from availability DropDown.");
    }

    /**
     * This Method is used to select 'Bring this agent online when in demand, and take offline when idle' option from availability in Node creation page
     */
    public void selectBringThisAgentOnlineWhenInDemand() throws Exception {
        selectFromDropdownByIndex(availabilityDropDown(), 2);
        Logger.instance.info("Selected 'Bring this agent online when in demand, and take offline when idle' option from availability DropDown.");
    }

    /**
     * This Method is used to click DisableDeferredWipeOut CheckBox in Node page
     */
    public void clickDisableDeferredWipeOutCheckBox() {
        boolean isCheck = isElementSelected(disableDeferredWipeOutNodePropertyCheckBox());
        if (!isCheck) {
            clickByJS(disableDeferredWipeOutNodePropertyCheckBox());
            Logger.instance.info("Clicked DisableDeferredWipeOut CheckBox Successfully.");
        } else {
            Logger.instance.info("Already Checked DisableDeferredWipeOut CheckBox");
        }
    }

    /**
     * This Method is used to click Environment Variables CheckBox in Node page
     */
    public void clickEnvironmentVariablesCheckBox() {
        boolean isCheck = isElementSelected(environmentVariablesNodePropertyCheckBox());
        if (!isCheck) {
            clickByJS(environmentVariablesNodePropertyCheckBox());
            Logger.instance.info("Clicked Environment Variables CheckBox Successfully.");
        } else {
            Logger.instance.info("Already Checked Environment Variables CheckBox");
        }
    }

    /**
     * This Method is used to click NotifyWhenNodeOnlineStatusChanges CheckBox in Node page
     */
    public void clickNotifyNodeOnlineStatusCheckBox() {
        boolean isCheck = isElementSelected(notifyWhenNodeOnlineStatusChangesCheckBox());
        if (!isCheck) {
            clickByJS(notifyWhenNodeOnlineStatusChangesCheckBox());
            Logger.instance.info("Clicked NotifyWhenNodeOnlineStatusChanges CheckBox Successfully.");
        } else {
            Logger.instance.info("Already Checked NotifyWhenNodeOnlineStatusChanges CheckBox CheckBox");
        }
    }

    /**
     * This Method is used to click Tool Locations CheckBox in Node page
     */
    public void clickToolLocationsCheckBox() {
        boolean isCheck = isElementSelected(toolLocationNodePropertyCheckBox());
        if (!isCheck) {
            clickByJS(toolLocationNodePropertyCheckBox());
            Logger.instance.info("Clicked Tool Locations CheckBox Successfully.");
        } else {
            Logger.instance.info("Checked Already Tool Locations CheckBox.");
        }
    }

    /**
     * This Method is used to click Save button in Node page
     */
    public void clickSaveButton() {
        clickByJS(saveButton());
        Logger.instance.info("Clicked Save Button Successfully.");
    }

    /**
     * This Method is used to click node in Node page
     */
    public boolean isVerifyNodeIsExistsInJenkinsNodePage(String nodeName) {
        String[] columnValues = getColumnValues(nodeComputerTable(), "Name  ↓");
        for (String value : columnValues) {
            if (value.equals(nodeName)) {
                Logger.instance.info(nodeName + " : Node is Exists in JenkinsNode Page Successfully.");
                return true;
            }
        }
        Logger.instance.error(nodeName + " : Node is not Exists in JenkinsNode Page.");
        return false;
    }

    /**
     * This method is to click on Delete Agent link in Node Page
     */
    public void clickDeleteAgentLink() throws Exception {
        hoverAndClick((deleteAgentLink()));
    }

    /**
     * This Method is used to click Save button in Node page
     */
    public void clickYesButton() {
        clickByJS(yesButton());
        Logger.instance.info("Clicked Yes Button Successfully.");
    }
}