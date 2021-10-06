package com.recode.framework.automation.ui.pageobjects.jenkins.users;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Arrays;
import java.util.List;

public class UsersPage extends PageClass {

    public UsersPage(RemoteWebDriver theWebDriver)
    {
        super(theWebDriver);
    }

    //Locator values here

    private By createUserLink(){ return By.xpath("//a[@title='Create User']");}
    private By userNameField(){ return By.xpath("//input[@id='username']");}
    private By passwordField(){ return By.xpath("//input[@name='password1']");}
    private By confirmPasswordField(){ return By.xpath("//input[@name='password2']");}
    private By fullNameField(){ return By.xpath("//input[@name='fullname']");}
    private By emailAddressField(){ return By.xpath("//input[@name='email']");}
    private By createUserButton(){ return By.xpath("//div[@id='main-panel']//button[contains(.,'Create User')]");}
    private By userTableList(){return By.xpath("//table[@id='people']");}
    private By deleteIconClick(){return By.xpath("//tr[contains(.,'jack')]//a[contains(@href,'delete')]");}


    //Test Methods here

    /**
     * This Method is used to click on the Create User option in Jenkins Application
     * @throws Exception
     */
    public void clickCreateUserLink() throws Exception {
        clickByJS(createUserLink());
    }

    /**
     * This method is to enter username from Jenkins Application
     * @throws Exception
     */
    public void enterUsername(String userName) throws Exception {
        sendKeys(userNameField(),userName);
    }

    /**
     * This method is to enter password from Jenkins Application
     * @throws Exception
     */
    public void enterPassword(String password) throws Exception {
        sendKeys(passwordField(),password);
    }
    /**
     * This method is to enter confirm password from Jenkins Application
     * @throws Exception
     */
    public void enterConfirmPassword(String confirmPassword) throws Exception {
        sendKeys(confirmPasswordField(),confirmPassword);
    }
    /**
     * This method is to enter full name from Jenkins Application
     * @throws Exception
     */
    public void enterFullName(String fullName) throws Exception {
        sendKeys(fullNameField(),fullName);
    }
    /**
     * This method is to enter email from Jenkins Application
     * @throws Exception
     */
    public void enterEmail(String emailAddress) throws Exception {
        sendKeys(emailAddressField(),emailAddress);
    }

    /**
     * This method is to click on create user button from Jenkins Application
     * @throws Exception
     */
    public void clickCreateUserButton() throws Exception {
        click(createUserButton());
    }

    /**
     * This method is to get the entire row value of a users list web table
     * @return List<WebElement>
     */
    public List<WebElement> userTableRowList() {
        return getRowListFromTable(userTableList());
    }

    /**
     * This method is to verify whether the user exists in the jenkins application
     * @param username - User name
     * @return boolean
     */
    public boolean isUserExists(String username) {
        boolean isExists = false;
        List<WebElement> userList = userTableRowList();
        for (WebElement element : userList) {
            if (element.getText().trim().contains(username)) {
                Logger.instance.info("The user" + username + "exist in the jenkins application user table");
                isExists = true;
                break;
            }
        }
        return isExists;
    }

    /**
     * This method is to click the newly created user in jenkins application
     */
    public void deleteUserClick() throws Exception {
        click(deleteIconClick());
    }

    /**
     * This method is to fetch the jenkins users count in jenkins application
     */
    public int usersCount() throws Exception {
        List<WebElement> userList = userTableRowList();
        int  users = userList.size();
        return users-1;
    }

    /**
     * This method is to fetch the user names from jenkins user table in jenkins application
     */
    private String[] usersNames() {
        try {
            String[] jenkinUsers = getColumnValues(userTableList(), "User ID");
            Logger.instance.info("The jenkins users are: "+ Arrays.toString(jenkinUsers));
            return jenkinUsers;
        }
        catch (Exception e){
            Logger.instance.error("The user names are not fetched from jenkins user table" +e);
        }
        return null;
    }

    /**
     * This method is to check whether the user exists in jenkins application
     */
    public boolean checkUserExist(String user) {
        String[] jenkinUsersList = usersNames();
        int userIndexPosition = Arrays.binarySearch(jenkinUsersList, user);
        if (userIndexPosition >= 0) {
            return true;
        }
            return false;
    }
}
