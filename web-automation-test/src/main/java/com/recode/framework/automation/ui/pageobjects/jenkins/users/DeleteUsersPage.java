package com.recode.framework.automation.ui.pageobjects.jenkins.users;

import com.recode.framework.automation.ui.pageobjects.PageClass;
import com.recode.framework.automation.ui.utils.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DeleteUsersPage extends PageClass {

    public DeleteUsersPage(RemoteWebDriver theWebDriver)
    {
        super(theWebDriver);
    }

    //Locator values here


    //Test Methods here

    /**
     * This method is to click on the "Yes" confirmation button from delete page of Jenkins Application
     */
    public void clickDeleteConfirmButton() {
        try {
            WebElement element = findChildElementUsingParentLocator(By.name("delete"), By.tagName("button"), 5);
            element.click();
        }
        catch (Exception e){
            Logger.instance.error("Confirmation button for delete is not clicked"+e);
        }
    }

}
