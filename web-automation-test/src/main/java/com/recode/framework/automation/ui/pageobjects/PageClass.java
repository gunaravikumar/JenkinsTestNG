package com.recode.framework.automation.ui.pageobjects;

import com.recode.framework.automation.ui.utils.Logger;
import com.recode.framework.automation.ui.utils.Utils;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.cropper.indent.BlurFilter;
import ru.yandex.qatools.ashot.cropper.indent.IndentCropper;

import javax.imageio.ImageIO;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static org.testng.AssertJUnit.fail;

public abstract class PageClass {
    protected WebDriver theWebDriver = null;
    public int searchAttempts = 6;

    public PageClass() {

    }

    public PageClass(WebDriver driver) {
        this.theWebDriver = driver;
    }

    /**
     * This Method is used to find the given Web element in the ui page
     * @param locator - element locator
     * @param timeOutSeconds - duration
     * @return - WebElement
     */
    public WebElement findElement(By locator, long timeOutSeconds) throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        boolean isElementFound = false;
        WebElement element = null; int attempt = 1; long secondsWaited;
        long start = System.currentTimeMillis();
        ExpectedCondition<Boolean> waitTillPageLoad = theWebDriver -> ((JavascriptExecutor) theWebDriver).executeScript("return document.readyState").toString().equals("complete");
        while(attempt <= searchAttempts && !isElementFound) {
            try {
               element =  new WebDriverWait(theWebDriver, timeOutSeconds).pollingEvery(Duration.ofSeconds(1)).
                        until(ExpectedConditions.visibilityOfElementLocated(locator));
               if(element !=null)
               {
                   isElementFound = true;
                   ((JavascriptExecutor)theWebDriver).executeScript("arguments[0].scrollIntoView(true);", element);
               }
            }
            catch (TimeoutException e) {
                Logger.instance.error("Error Occurred in the method: "+name+" in Attempt - "+attempt+" : The locator is unable to find in the given time period("+timeOutSeconds+" seconds), hence timeout error has occurred. Detailed log => " + e.toString());
                timeOutSeconds = timeOutSeconds + 5;
                Logger.instance.info("Increasing the timeout value with 5 seconds");
                try {
                    WebDriverWait wait = new WebDriverWait(theWebDriver, timeOutSeconds);
                    wait.until(waitTillPageLoad);
                }
                catch (Exception ex)
                {
                    Logger.instance.error("Error Occurred in the method: "+name+" -> Getting timeout while waiting for Page Load Request to complete");
                }
            }
            catch (Exception e) {
                Logger.instance.error("Error Occurred in the method: "+name+" in Attempt - "+attempt+" : The locator is unable to find because of => " + e.getMessage());
                try {
                    WebDriverWait wait = new WebDriverWait(theWebDriver, timeOutSeconds);
                    wait.until(ExpectedConditions.presenceOfElementLocated(locator));
                    ((JavascriptExecutor)theWebDriver).executeScript("arguments[0].scrollIntoView(true);", element);
                    element =  new WebDriverWait(theWebDriver, timeOutSeconds).pollingEvery(Duration.ofSeconds(1)).
                            until(ExpectedConditions.visibilityOfElementLocated(locator));
                }
                catch (Exception ex)
                {
                    Logger.instance.error("Error occurred in the method: "+name+" while waiting till element to be present in the UI. Detailed log=> " + ex.getMessage());
                }
            }
            attempt++;
        }
        long stop = System.currentTimeMillis();
        secondsWaited = stop - start;
        if (element == null) {
            //Assert.assertTrue(isElementFound, "element WAS NOT FOUND after waiting for " + timeOutSeconds + " seconds in " + searchAttempts + " attempts");
            throw new Exception("The given Element "+locator+" WAS NOT FOUND after waiting for " + secondsWaited/1000 + " seconds in " + searchAttempts + " attempts");
        }
        return element;
    }

    /**
     * This method is found the bulk web elements in the web page
     * @param locator - element locator
     * @param timeOutInSecs - duration
     * @return - returns the List<WebElement>
     */
    public List<WebElement> findElements(By locator, long timeOutInSecs) throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        List<WebElement> elements = null;
        ExpectedCondition<Boolean> waitTillPageLoad = theWebDriver -> ((JavascriptExecutor) theWebDriver).executeScript("return document.readyState").toString().equals("complete");
            int attempt = 1; long secondsWaited;
        long start = System.currentTimeMillis();
            while(attempt <= searchAttempts) {
                try {
                elements = new WebDriverWait(theWebDriver, timeOutInSecs).pollingEvery(Duration.ofSeconds(1))
                        .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
                }
                catch (TimeoutException e) {
                    Logger.instance.error("Error Occurred in the method: "+name+" in Attempt - "+attempt+" : The locator is unable to find in the given time period("+timeOutInSecs+" seconds), hence timeout error has occurred. Detailed log => " + e.toString());
                    timeOutInSecs = timeOutInSecs + 5;
                    Logger.instance.info("Increasing the timeout value with 5 seconds");
                    try {
                        WebDriverWait wait = new WebDriverWait(theWebDriver, timeOutInSecs);
                        wait.until(waitTillPageLoad);
                    }
                    catch (Exception ex)
                    {
                        Logger.instance.error("Error occurred in the method: "+name+" -> Getting timeout while waiting for Page Load Request to complete");
                    }
                }
                catch (Exception e) {
                    Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
                }
                attempt++;
            }
             long stop = System.currentTimeMillis();
             secondsWaited = stop - start;
            if(elements == null)
            {
                throw new Exception("The given Element "+locator+" WAS NOT FOUND after waiting for " + secondsWaited/1000 + " seconds in " + searchAttempts + " attempts");
            }
    return elements;
    }

    /**
     * This Method is used to find the element using the parent locator value
     * @param parentLocator - Parent element locator
     * @param childLocator - child element locator
     * @return - WebElement
     */
    public WebElement findChildElementUsingParentLocator(By parentLocator, By childLocator, long timeOutInSecs) throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        WebElement element = null;

        int attempt = 1; boolean isElementFound = false;
        while(attempt <= searchAttempts && !isElementFound) {
            try {
                element = new WebDriverWait(theWebDriver, timeOutInSecs).pollingEvery(Duration.ofSeconds(1))
                        .until(ExpectedConditions.presenceOfNestedElementLocatedBy(parentLocator, childLocator));
                if(element !=null)
                {
                    isElementFound = true;
                    ((JavascriptExecutor)theWebDriver).executeScript("arguments[0].scrollIntoView(true);", element);
                }
            } catch (Exception e) {
                Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
            }
            attempt++;
        }
        if(element == null)
            throw new Exception("The given Element "+parentLocator+"/"+childLocator+" WAS NOT FOUND after waiting for " + timeOutInSecs + " seconds in " + searchAttempts + " attempts");

        return element;
    }

    /**
     * This Method is used to check the web element is displayed in the UI or not
     * @param locator - element locator
     * @return - boolean value
     */
    public boolean isElementVisible(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            WebElement element = findElement(locator, 5);
            return element.isDisplayed();
            } catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This Method is used to check the given web element is displayed in the UI or not
     * @param element - web element
     * @return - boolean value
     */
    public boolean isElementVisible(WebElement element) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        ExpectedCondition<Boolean> waitTillPageLoad = theWebDriver -> ((JavascriptExecutor) theWebDriver).executeScript("return document.readyState").toString().equals("complete");
        try {
            WebDriverWait wait = new WebDriverWait(theWebDriver, 30);
            wait.until(waitTillPageLoad);
            return element.isDisplayed();
        } catch (StaleElementReferenceException e) {
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
            WebDriverWait wait = new WebDriverWait(theWebDriver, 30);
            try {
                wait.until(waitTillPageLoad);
                element = new WebDriverWait(theWebDriver, 30).pollingEvery(Duration.ofSeconds(1)).
                        until(ExpectedConditions.visibilityOf(element));
                return element.isDisplayed();
            }
            catch (Exception ex)
            {
                Logger.instance.error("Error occurred in the method: "+name+". Getting timeout while waiting for Page Load Request to complete. Detailed log-> " + e.getMessage());
            }
        }
        catch (Exception e)
        {
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is to wait until the page is loaded
     */
    public void pageLoadWait()
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            WebDriverWait wait = new WebDriverWait(theWebDriver,15);
            ExpectedCondition<Boolean> waitForPageLoad = waitDriver -> ((JavascriptExecutor)theWebDriver).executeScript("return document.readyState").equals("complete");
            wait.until(waitForPageLoad);
        }
        catch (Exception e)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + e.getMessage());
        }
    }

    /**
     * This method is to refresh the page
     *
     */
    public void refreshPage()
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            new WebDriverWait(theWebDriver,15);
            switchToDefault();
            theWebDriver.navigate().refresh();
            pageLoadWait();
        }
        catch(Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method is to clear the text in the given text field
     * @param locator - element locator
     */
    public void clearText(By locator)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        WebDriverWait wait = new WebDriverWait(theWebDriver,15);
        pageLoadWait();
        int attempt = 1; boolean isCleared = false; WebElement textField;
        while(attempt <=5 && !isCleared)
        {
            try
            {
                wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
                textField = findElement(locator);
                if (textField.isEnabled() && textField.isDisplayed())
                {
                    isCleared = true;
                    textField.clear();
                }
            }
            catch (Exception e)
            {
                Logger.instance.error("Error occurred in the method: " + name +" because of " + e.getMessage());
                pageLoadWait();
            }
            attempt++;
        }
    }

    /**
     * This Method is used to find the given web element
     * @return WebElement
     */
    public WebElement findElement(By locator) throws Exception
    {
        return findElement(locator, 10);
    }

    /**
     * This Method holds the instance of Selenium Action class
     * @return Actions
     */
    public Actions getAction() {
        return new Actions(theWebDriver);
    }

    /**
     * This Method is used to hover on the given web element
     * @param locator - element locator
     * @throws Exception - Exception
     */
    public void hoverElement(By locator) throws Exception
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        int attempt = 1;
        WebElement element = findElement(locator, 15);
        boolean hoverToElement = false;
        while(attempt <= 2) {
            try {
                switch (attempt) {
                    case 1:
                        getAction().moveToElement(element).perform();
                        hoverToElement = true;
                        break;
                    case 2:
                        int width = element.getSize().getWidth();
                        getAction().moveToElement(element, (width/2)-2, 0).perform();
                        hoverToElement = true;
                }
                attempt++;
            }
             catch (Exception e) {
                 Logger.instance.error("Error occurred in the Method : "+name + " in the attempt - " +attempt+ " because of " + e.getMessage());
                 ((JavascriptExecutor)theWebDriver).executeScript("arguments[0].scrollIntoView(true);", element);
                attempt++;
            }
            if(hoverToElement)
                break;
        }
    }

    /**
     * This Method is used to mouse hover on the web element and then click on any visible element
     * @param locatorToHover - element on which mouse to hover
     * @param targetLocator - element to click after hovering mouse on a specific web element
     */
    public void hoverAndClickElement(By locatorToHover, By targetLocator)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            hoverElement(locatorToHover);
            click(targetLocator);
        }
        catch (Exception e)
        {
            Logger.instance.error("Error occurred in the method " + name + " because of => " + e.getMessage());
        }
    }

    /**
     * This is method is used to move mouse pointer to a given offset values
     * @param height - height
     * @param width - width
     */
    public void moveMousePointerUsingOffset(int height, int width)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            getAction().moveByOffset(height, width).perform();
        }
        catch (Exception e)
        {
            Logger.instance.error("Error occurred in the method " + name + " because of => " + e.getMessage());
        }
    }

    /**
     * This Method is used to hover the mouse pointer over an web element by using its offset values
     * @param locator - element locator
     */
    public void hoverMouseOverElementUsingOffset(By locator)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            WebElement element = findElement(locator);
            int width = element.getSize().getWidth();
            getAction().moveToElement(element, (width/2)-2, 0).perform();
        }
        catch (Exception e)
        {
            Logger.instance.error("Error occurred in the method " + name + " because of => " + e.getMessage());
        }
    }

    /**
     * This Method is used to enter String value to a text field
     * @param locator - element locator
     * @param textToEnter - text to enter
     */
    public void clearAndSendKeys(By locator, String textToEnter)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            clearText(locator);
            sendKeys(locator, textToEnter);
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
    }

    /**
     * This method is to capture the entire screen using Selenium Screenshot.
     * @param testProtocol - Used to create child folder to keep the capture files.
     * @param testCaseName - Used to create child folder to keep the capture files.
     * @param imgName - Given string is append to the image name.
     */
    public String captureScreen(String testProtocol, String testCaseName, String imgName) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        String imagePath = "";
        try {

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_ss");
            String timeText = date.format(formatter);

            //String screenShotDir = Utils.getAutomationProperties().getProperty("ScreenShotDir");
            String screenShotDir = System.getProperty("user.dir");
            File testcaseDir = new File(screenShotDir + "\\target\\Screenshots\\"+ testProtocol + "\\" + testCaseName);
            if (!testcaseDir.exists()) {
                testcaseDir.mkdirs();
            }
            testcaseDir.setWritable(true, false);
            testcaseDir.setReadable(true, false);
            testcaseDir.setExecutable(true, false);

            TakesScreenshot screenShot = ((TakesScreenshot) theWebDriver);
            File srcFile = screenShot.getScreenshotAs(OutputType.FILE);
            imagePath = testcaseDir+"\\"+timeText+"_"+imgName+".png";
            File destFile = new File(testcaseDir+"\\"+timeText+"_"+imgName+".png");
            FileUtils.copyFile(srcFile, destFile);
            destFile.setWritable(true, false);
            destFile.setReadable(true, false);
        }
        catch (Exception exp){
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
        return imagePath;
    }

    /**
     * This method is to capture the given element using Ashot library.
     * @param locator - Web element to Capture
     * @param testProtocol - Used to create child folder to keep the capture files.
     * @param testCaseName - Used to create child folder to keep the capture files.
     * @param imgName - Given string is append to the image name
     * @return
     */
    public String captureElement(By locator, String testProtocol, String testCaseName, String imgName) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        String imagePath ="";
        try{
            pageLoadWait();

            LocalDateTime date = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_ss");
            String timeText = date.format(formatter);

            String screenShotDir = Utils.getAutomationProperties().getProperty("ScreenShotDir");
            File testcaseDir = new File(System.getProperty("user.dir") + screenShotDir + File.separator + testProtocol + File.separator + testCaseName);
            if (!testcaseDir.exists()) {
                testcaseDir.mkdirs();
            }
            testcaseDir.setWritable(true, false);
            testcaseDir.setReadable(true, false);
            testcaseDir.setExecutable(true, false);

            Screenshot capElement= new AShot().coordsProvider(new WebDriverCoordsProvider()).imageCropper(new IndentCropper().addIndentFilter(new BlurFilter())).takeScreenshot(theWebDriver, findElement(locator, 30));
            imagePath = testcaseDir + File.separator + timeText + "_" + imgName + ".png";
            File destFile = new File(testcaseDir + File.separator + timeText + "_" + imgName + ".png");
            ImageIO.write(capElement.getImage(), "PNG", destFile);
            destFile.setReadable(true, false);
            destFile.setWritable(true, false);
        }
        catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
        return imagePath;
    }

    /**
     * This method is to find the attribute value for the elements using the locator
     * @param locator - element locator
     * @param attribute - attribute name
     * @return String
     */
    public String getElementsAttribute(By locator, String attribute)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            WebElement attrElement = findElement(locator,10);
            if (attrElement.isDisplayed())
                return attrElement.getAttribute(attribute);
        }
        catch (Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
        return null;
    }

    /**
     * This method is to get the text present in the locator area
     * @param locator - element locator
     * @return String
     */
    public String getText(By locator)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            WebElement textElement = findElement(locator,10);
            if (textElement.isDisplayed())
                return textElement.getText();
        }
        catch (Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
        return null;
    }

    /**
     * This method is used for back to the previous page
     *
     */
    public void selectBackOnBrowser()
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            switchToDefault();
            theWebDriver.navigate().back();
            pageLoadWait();
        }
        catch (Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method is scroll the page till the element is found
     *
     */
    public void scrollToElementByJS(WebElement element)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            JavascriptExecutor js = (JavascriptExecutor) theWebDriver;
            js.executeScript("arguments[0].scrollIntoView();", element);
        }
        catch (Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This Method is used to check the web element is present in the DOM
     * @param locator - element locator
     */
    public boolean isElementPresent(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            findElement(locator);
            return true;
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This Method is used to check the checkbox or radiobutton is selected or not
     * @param locator - element locator
     * @return boolean value
     */
    public boolean isElementSelected(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            WebElement element = findElement(locator);
            return element.isSelected();
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This Method is used to check the checkbox or radiobutton is enabled or not
     * @param locator - element locator
     * @return boolean
     */
    public boolean isElementEnabled(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            WebElement element = findElement(locator);
            return element.isEnabled();
        }
        catch (Exception e) {
           Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is used to check vertical scroll bar is present in the webpage
     * @return boolean
     */
    public boolean  isVerticalScrollBarPresentInWebPageByJS() {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            JavascriptExecutor js = (JavascriptExecutor) theWebDriver;
            boolean vertScrollStatus = (Boolean) js.executeScript("return document.documentElement.scrollHeight>document.documentElement.clientHeight;");
            return vertScrollStatus;
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is used to check horizontal scroll bar is present in the webpage
     * @return boolean
     */
    public boolean isHorizontalScrollBarPresentInWebPageByJS() {

        String name = new Object() {}.getClass().getEnclosingMethod().getName();
       try {
            JavascriptExecutor js = (JavascriptExecutor) theWebDriver;
            boolean horScrollStatus = (Boolean) js.executeScript("return document.documentElement.scrollWidth>document.documentElement.clientWidth;");
            return horScrollStatus;
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This Method is used to verify the element is vertically scrollable
     * @param locator - element locator
     * @return boolean
     */
    public boolean isElementVerticallyScrollable(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            int scrollHeight = Integer.parseInt(getElementsAttribute(locator, "scrollHeight"));
            int clientHeight = Integer.parseInt(getElementsAttribute(locator, "clientHeight"));
            return scrollHeight > clientHeight;
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This Method is used to verify the element is horizontally scrollable
     * @param locator - element locator
     * @return boolean
     */
    public boolean isElementHorizontallyScrollable(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            int scrollWidth = Integer.parseInt(getElementsAttribute(locator,"scrollWidth"));
            int clientWidth = Integer.parseInt(getElementsAttribute(locator,"clientWidth"));
            return scrollWidth > clientWidth;
        }
        catch (Exception e) {
           Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is used for drag and drop using source and destination
     *
     */
    public void dragAndDrop(By source, By destination)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            getAction().clickAndHold(findElement(source,15)).pause(1500)
                    .moveToElement(findElement(destination, 15))
                    .release()
                    .perform();
        }
        catch(Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method is used for drag and drop using source, xOffset and yOffset
     * */
    public void dragAndDrop(By source, int xOffset, int yOffset)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            getAction().dragAndDropBy(findElement(source), xOffset, yOffset)
                    .build()
                    .perform();
        }
        catch (Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method will select a specified radio button object
     *
     */
    public void selectRadioButton(By locator, boolean isJSClick)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            WebElement element = findElement(locator, 15);
            if (element != null && element.isDisplayed())
            {
                if(!element.isSelected()){
                    if(isJSClick) {
                        clickByJS(element);
                    }
                    element.click();
                    Utils.sleep(5);
                }
                Logger.instance.info("Radio button selected for the element");
            }
            else
            {
                Logger.instance.error("Element is not displayed in the page");
            }
        }
        catch(Exception exp){
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method will navigate the current browser instance to the specified URL
     *
     */
    public void navigateToURL(String url)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            theWebDriver.navigate().to(url);
            pageLoadWait();
            Logger.instance.info("Navigated to URL " + url + " with Browser " + ((RemoteWebDriver) theWebDriver).getCapabilities().getBrowserName());
        }
        catch (Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method will destroy the existing session cookies and session.
     *
     */
    public void closeCurrentSession()
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            this.clearBrowserCookies();
        }
        catch (Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " +name+" because of " + exp.getMessage());
        }
        finally
        {
            this.closeBrowser();
        }
    }

    /**
     * This method is to get the entire values from the dropdown
     *
     * */
    public String[] getDropdownOptions(By parentLocator, By childLocator)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        String[] dropDownValues = null;
        try {
            WebElement parentElement = theWebDriver.findElement(parentLocator);
            parentElement.click();
            WebDriverWait wait = new WebDriverWait(theWebDriver,10);
            wait.until((ExpectedConditions.presenceOfAllElementsLocatedBy(childLocator)));
            List<WebElement> elementList = parentElement.findElements(childLocator);
            dropDownValues = new String[elementList.size()];
            int i  = 0;
            for (WebElement ele : elementList) {
                dropDownValues[i] = ele.getText();
                Logger.instance.info("Dropdown value : " +  dropDownValues[i]);
                i++;
            }
            //close dropdown
            parentElement.click();
        }
        catch(Exception e)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + e.getMessage());
        }
        return dropDownValues;
    }

    /**
     * This method is to close the browser in the specified web driver instance.
     */
    public void closeBrowser(){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            if (theWebDriver.getWindowHandles() != null) {
                theWebDriver.close();
                Utils.sleepWithReason(10,"Closing the Browser");
                theWebDriver.quit();
                Utils.sleepWithReason(10, "Quit the Driver Instance");
                Logger.instance.info("Browser session is closed.");
            }
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
        theWebDriver = null;
    }

    /**
     * This method is to clear the browser cookies and close the browser session.
     */
    public void clearBrowserCookies(){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            theWebDriver.manage().deleteAllCookies();
            Utils.sleepWithReason(10, "Removing all Browser Cookies");

            if(getBrowserName().equals("internet explorer")) {
                Process process = new ProcessBuilder("rundll32.exe", "InetCpl.cpl,ClearMyTracksByProcess 2").start();
            }
            else if (getBrowserName().equals("chrome")) {
                theWebDriver.navigate().to("chrome://settings/clearBrowserData");
                theWebDriver.switchTo().activeElement();
                Utils.sleep(5);
                findElement(By.xpath("//settings-ui")).sendKeys(Keys.ENTER);
                WebDriverWait wait = new WebDriverWait(theWebDriver, 30);
                wait.until(ExpectedConditions.urlToBe("chrome://settings/"));
            }

            Logger.instance.info("Browser cookies are cleared.");
            closeBrowser();
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
    }

    /**
     * This method is for switch to the default frame.
     */
    public void switchToDefault(){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            theWebDriver.switchTo().defaultContent();
            Logger.instance.info("Switched to the default frame content.");
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
    }

    /**
     * This method is for switch to the mentioned frame.
     * @param frameName - frame name to switch.
     */
    public void switchToFrame(String frameName){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            theWebDriver.switchTo().frame(frameName);
            Logger.instance.info("Switched to the frame: "+frameName);
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
    }

    /**
     * This method is for switch to the mentioned frame.
     * @param frameID - frame ID to switch
     */
    public void switchToFrame(int frameID){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            theWebDriver.switchTo().frame(frameID);
            Logger.instance.info("Switched to the frame ID: "+frameID);
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
    }

    /**
     * This method is for switch to the mentioned frame.
     * @param locator - By locator to switch
     */
    public void switchToFrame(By locator){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            theWebDriver.switchTo().frame(findElement(locator, 30));
            Logger.instance.info("Switched to the given frame.");
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
    }

    /**
     * This method is to switch to the window on the given window title.
     * @param title - Switch window using given title.
     */
    public void switchToWindow(String title){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            String newWindow = null;
            boolean isWindowExist = false;
            String currentWindow = theWebDriver.getWindowHandle();

            Set<String> allWindowHandles = theWebDriver.getWindowHandles();
            for (String window : allWindowHandles) {
                theWebDriver.switchTo().window(window);
                if (theWebDriver.getTitle().equals(title)){
                    isWindowExist = true;
                    newWindow = window;
                    break;
                }
            }
            if (isWindowExist){
                theWebDriver.switchTo().window(newWindow);
                Logger.instance.info(title+" - Switched to the given Window.");
            }
            else {
                Logger.instance.error("Unable to find the mentioned Window.");
                theWebDriver.switchTo().window(currentWindow);
            }
            pageLoadWait();
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
        }
    }

    /**
     * This method is to switch to the window based on the given window index.
     * @param windowIndex - Window Index for switch
     */
    public void switchToWindow(int windowIndex){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            String newWindow = null;
            boolean isWindowExist = false;

            Set<String> allWindowHandles = theWebDriver.getWindowHandles();
            int totalWindows =  allWindowHandles.size();
            for(int wdwCnt=0; wdwCnt<totalWindows; wdwCnt++){
                if(wdwCnt==windowIndex) {
                    isWindowExist = true;
                    newWindow = allWindowHandles.toArray()[wdwCnt].toString();
                }
            }
            if (isWindowExist) {
                theWebDriver.switchTo().window(newWindow);
                Logger.instance.info(windowIndex+" - Switched to the given Window index.");
                pageLoadWait();
            }
            else {
                Logger.instance.error("Unable to find the mentioned Window.");
            }
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
        }
    }

    /**
     * This method is to close all the child windows which opened from the parent window.
     */
    public void closeAllChildWindows(){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            Set<String> allWindowHandles = theWebDriver.getWindowHandles();
            int totalWindows =  allWindowHandles.size();
            Logger.instance.info("Total Child Windows are: "+ (totalWindows-1));
            for(int wdwCnt=0; wdwCnt<totalWindows; wdwCnt++) {
                if (wdwCnt != 0){
                    theWebDriver.switchTo().window(allWindowHandles.toArray()[wdwCnt].toString()).close();
                }
            }
            Logger.instance.info("All Child Windows are closed");
            theWebDriver.switchTo().window(allWindowHandles.toArray()[0].toString());
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
        }
    }

    /**
     * This Method is used to get the number of windows/tabs opened in the browser
     * @return int
     */
    public int getNoOfWindowsOpened(){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            Set<String> allWindowHandles = theWebDriver.getWindowHandles();
            return allWindowHandles.size();
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
        }
        return 0;
    }

    /**
     * This method is to launch the application.
     * @param URL - URL needs to launch.
     */
    public void launchApplication(String URL){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            theWebDriver.get(URL);
            Logger.instance.info("The given URL -" +URL+"- is launched successfully.");
            pageLoadWait();
        }
        catch (Exception e){
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
        }
    }

    /**
     * This method is to verify the values from the dropdown list with the expected array values
     *@param values - list of Values in drop down
     * @return boolean
     */
    public boolean verifyDropDownList(By parentLocator, By childLocator, String[] values)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            if(values.length != 0)
            {
                String[] options = getDropdownOptions(parentLocator, childLocator);
                int counter = 0;
                if(options.length == values.length)
                {
                    //for loop for iterating parameter "String[] values"
                    for (String val : values) {
                        //inner for loop for iterating the values obtained from the method getDropdownOptions(By locator)
                        for (String option : options) {
                            if (val.equals(option)) {
                                counter++;
                                break;
                            }
                        }
                    }
                }
                if(counter==values.length)
                    return true;
            }
        }
        catch (Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
        return false;
    }

    /**
     * This method is to get the entire values from dropdown using the locator of select area
     *
     * */
    public String[] getDropdownOptionsUsingSelect(By locator)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        String[] dropDownValues = null;
        try {
            List<WebElement> elementList = getSelect(locator).getOptions();
            dropDownValues = new String[elementList.size()];
            int i = 0;
            for (WebElement ele : elementList) {
                dropDownValues[i] = ele.getText();
                i++;
            }
        }
        catch (Exception e)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + e.getMessage());
        }
        return dropDownValues;
    }

    /**
     * This Method is used to click on the web element
     * @param locator - element locator
     * @throws Exception - Exception
     */
    public void click(By locator) throws Exception {
        WebElement element = findElement(locator);
        for (int currentIteration = 1; currentIteration <= searchAttempts; currentIteration++) {
            try {
                element.click();
                break;
            }
            catch(ElementClickInterceptedException eci) {
                new WebDriverWait(theWebDriver, 20).pollingEvery(Duration.ofSeconds(1)).
                        until(ExpectedConditions.elementToBeClickable(element));
                continue;
            }
            catch (ElementNotInteractableException e) {
                new WebDriverWait(theWebDriver, 20).pollingEvery(Duration.ofSeconds(1)).
                        until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                break;
            }
            catch (Exception e) {
                Logger.instance.error("Send keys on element failed. Attempt: " + currentIteration + "/" + searchAttempts);
                Logger.instance.error("Caught an exception on performing Click operation on the element: " + element + " because of " + e.getMessage());
                element = findElement(locator);
            }
        }
    }

    /**
     * This Method is used to click on the web element by javascript executor
     * @param locator - element locator
     */
    public void clickByJS(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            WebElement element = findElement(locator);
            JavascriptExecutor js = (JavascriptExecutor) theWebDriver;
            js.executeScript("arguments[0].click();", element);
        } catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This Method is used to click on the web element by javascript executor
     * @param element - web element
     */
    public void clickByJS(WebElement element) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            JavascriptExecutor js = (JavascriptExecutor) theWebDriver;
            js.executeScript("arguments[0].click();", element);
        } catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This Method is used to do double-click on the web element in the UI
     * @param locator - element locator
     */
    public void doubleClick(By locator)  {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            WebElement element = findElement(locator);
            getAction().doubleClick(element).perform();
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + e.getMessage());
        }
    }

    /**
     * This Method is used to Move To an Element and Double click it
     * @param locator - element locator
     */
    public void moveToElementAndDoubleClick(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            hoverElement(locator);
            doubleClick(locator);
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + e.getMessage());
        }
    }

    /**
     * This method is to check whether given attribute value exists or not
     *
     * */
    public boolean isAttributeExists(By locator, String attribute) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            String value = getElementsAttribute(locator, attribute);
            if (value != null)
                return true;
        } catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
        return false;
    }

    /**
     * This method is scroll to top of the screen
     *
     * */
    public void scrollToTopOfScreen() {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            getAction().sendKeys(Keys.HOME).build().perform();
        }
        catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method is scroll the page by Vertical
     *
     */
    public void scrollByVerticalByJS(By locator)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            JavascriptExecutor js = (JavascriptExecutor) theWebDriver;
            js.executeScript("return (arguments[0].scrollWidth>arguments[0].clientHeight)", findElement(locator));
        }
        catch (Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method is scroll the page by Horizontal
     *
     */
    public void scrollByHorizontalByJS(By locator)
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            JavascriptExecutor js = (JavascriptExecutor) theWebDriver;
            js.executeScript("return (arguments[0].scrollWidth>arguments[0].clientWidth)", findElement(locator));
        }
        catch (Exception exp)
        {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method is to select the dropdown options using index value
     * @param locator - element locator
     * @param index - value index
     */
    public void selectFromDropdownByIndex(By locator, int index){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            getSelect(locator).selectByIndex(index);
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: " + name + " because of " + e.getMessage());
        }
    }

    /**
     * This method is to select the dropdown options using visible text value
     * @param locator - element locator
     * @param text - value in dropdown
     */
    public void selectFromDropdownByVisibleText(By locator, String text){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            getSelect(locator).selectByVisibleText(text);
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: " + name + " because of " + e.getMessage());
        }
    }

    /**
     * This Method holds the instance of Select class
     * @return Select
     */
    private Select getSelect(By locator) {
        WebElement selectElement = theWebDriver.findElement(locator);
        Select select = new Select(selectElement);
        Logger.instance.info("The instance for select class is generated");
        return select;
    }

    /**
     * This Method is used to wait for particular and invisibility the element by pollingInterval
     * @param locator - element locator
     * @param timeOutInSecs - duration
     * */
    public void waitAndLoseElement(long timeOutInSecs, By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            boolean elementFound = true;
            int count = 0;
            while (count < 30 && elementFound) {
                count++;
                new WebDriverWait(theWebDriver, timeOutInSecs).pollingEvery(Duration.ofSeconds(2))
                        .until(ExpectedConditions.invisibilityOfElementLocated(locator));
                elementFound = false;
            }
            if (elementFound) {
                fail(locator + " element STILL FOUND (Failing here because this item never disappeared which means next step can't proceed)");
            }
        } catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This Method is used to check whether element is clickable
     * @param locator - element locator
     * @return boolean
     */
    public boolean isElementClickable(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        WebElement element = null;
        try {
            findElement(locator);
            element = new WebDriverWait(theWebDriver, 20).pollingEvery(Duration.ofSeconds(1)).
                    until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        }
        catch (WebDriverException e) {
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
            scrollToElementByJS(element);
            new WebDriverWait(theWebDriver, 20).pollingEvery(Duration.ofSeconds(1)).
                    until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: " +name+" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This Method is used to check the web element is enabled or disabled
     */
    public boolean checkElementStateUsingDOM(By locator) throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        WebElement element =  findElement(locator);
        String elementState = element.getAttribute("disabled");
        try {
            if (elementState.equals("true"))
                return false;
            else if (elementState.equals("false"))
                return true;
        }
        catch (NullPointerException npe) {
            if (Objects.isNull(elementState))
                return true;
        }
        catch (Exception e) {
            Logger.instance.info("Error occurred in the method: " +name+" because of " + e.getMessage());
        }
        return false;
    }

    /**
     * This method is to get the current url on the browser
     * @return String
     */
    public String getCurrentUrl() {
            return theWebDriver.getCurrentUrl();
    }

    /**
     * This method is to get the title of the current page
     * @return String
     */
    public String getPageTitle(){
            return theWebDriver.getTitle();
    }

    /**
     * This Method is used to scroll down the UI page to the  bottom
     */
    public void scrollToBottomOfScreen() {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            ((JavascriptExecutor) theWebDriver)
                    .executeScript("window.scrollTo(0, document.body.scrollHeight)");
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+ name +" because of " + e.getMessage());
        }
    }

    /**
     * This Method is used to get the browser Session ID
     * @return String
     */
    protected String getSessionID() {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        String browser = getBrowserName();
        String sessionID = null;
        try {

            if (browser.equalsIgnoreCase("firefox")) {
                sessionID = ((FirefoxDriver) theWebDriver).getSessionId().toString();

            } else if (browser.equalsIgnoreCase("chrome")) {
                sessionID = ((ChromeDriver) theWebDriver).getSessionId().toString();

            } else if (browser.equalsIgnoreCase("internetExplorer")) {
                sessionID = ((InternetExplorerDriver) theWebDriver).getSessionId().toString();
            }
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
        }
        return sessionID;
    }

    /**
     * This method is used to get the session id from remote web driver session
     * @return String
     */
    public String getRemoteWebDriveSessionID(){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            return ((RemoteWebDriver) theWebDriver).getSessionId().toString();
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
        }
        return null;
    }

    /**
     * This Method is used to enter the string value into the text field
     * @param locator - element locator
     * @param textToEnter - Text
     * @throws Exception - Exception
     */
    public void sendKeys(By locator, String textToEnter) throws Exception
    {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        WebElement element = findElement(locator);
        try {
            element.sendKeys(textToEnter);
            Utils.sleep(5);
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: "+name+" because of " + e.getMessage());
        }
    }

    /**
     * This Method is used to check the existence of Alert box
     * @return boolean
     */
    public boolean isAlertPresent() throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        int attempt = 1;
        while(attempt<=5) {
            try {
                theWebDriver.switchTo().alert();
                return true;
            } catch (NoAlertPresentException ex) {
                Logger.instance.debug("Waiting for a while to get the alert popup...");
                Utils.sleepWithReason(1, "Waiting for the Alert popup");
            } catch (Exception e) {
                Logger.instance.error("Error occurred in the method: " + name + " because of " + e.getMessage());
            }
            attempt++;
        }
        return false;
    }

    /**
     * This Method is used to accept the alert popup
     */
    public void acceptAlert() throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        if(isAlertPresent())
            theWebDriver.switchTo().alert().accept();
        else
            Logger.instance.debug(name+" -Attempt to accept the alert but NO ALERT IS FOUND");
    }

    /**
     * This Method is used to dismiss/cancel the alert popup
     */
    public void dismissAlert() throws Exception {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        if(isAlertPresent())
            theWebDriver.switchTo().alert().dismiss();
        else
            Logger.instance.debug(name+" -Attempt to dismiss the alert but NO ALERT IS FOUND");
    }

    /**
     * This method is to hover over an element and to click it
     * @param locator - element locator
     *
     */
    public void hoverAndClick(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            hoverElement(locator);
            click(locator);
            Logger.instance.info("The mouse hover is hovered and clicked");
        }
        catch(Exception e){
            Logger.instance.error("Error occurred in the method: " + name +" because of " + e.getMessage());
        }
    }

    /**
     * Function:
     *   contextClick()
     * Purpose:
     *   To perform right click on the element
     * Parameters:
     *   locator: location of an element
     **/
    public void contextClick(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            getAction().contextClick(findElement(locator)).build().perform();
        } catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method will use to make Ctrl + Down Arrow
     * */
    public void CtrlKeyDown() {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            getAction().keyDown(Keys.CONTROL).sendKeys(Keys.DOWN).build().perform();
        } catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method will use to make Ctrl + Up Arrow
     * */
    public void CtrlKeyUp() {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            getAction().keyUp(Keys.CONTROL).sendKeys(Keys.UP).build().perform();
        } catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method is used to get the browser name from the browser
     * @return browserName
     * */
    public String getBrowserName() {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        String browserName = null;
        try {
            Capabilities cap = ((RemoteWebDriver) theWebDriver).getCapabilities();
            browserName = cap.getBrowserName().toLowerCase();
            Logger.instance.info("Browser Name is " + browserName);
        } catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
        return browserName;
    }

    /**
     * Description :
     * This function will select specified values from a any list box
     * @param locator - The element for which a value will be selected</param>
     * @param text - The option which will be selected from the drop-down list.</param>
     * @param byValue - When this is given the value will be selected based on the order</param>
     */
    public void selectFromList(By locator, String text, int byValue) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            if (byValue == 0) {
                selectFromDropdownByVisibleText(locator, text);
            } else {
                getSelect(locator).selectByValue(text);
            }
            Logger.instance.info("Option with " + text + " is found and Selected");
        } catch (Exception exp) {
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This Method is used to get the list of rows from the Web table
     * @param locator - element locator
     * @return List<WebElement>
     */
    public List<WebElement> getRowListFromTable(By locator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            return findChildElementsUsingParentLocator(locator, By.tagName("tr"));
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: " + name + " because of " + e.getMessage());
        }
        return null;
    }

    /**
     * This Method is used to find the list of child web elements by using the parent locator value
     * @param parentLocator - Parent element locator
     * @param childLocator - Child element locator
     * @return List<WebElement>
     */
    public List<WebElement> findChildElementsUsingParentLocator(By parentLocator, By childLocator) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            findElement(parentLocator);
            List<WebElement> childElements = new WebDriverWait(theWebDriver, 20).pollingEvery(Duration.ofSeconds(1))
                    .until(ExpectedConditions.presenceOfNestedElementsLocatedBy(parentLocator, childLocator));
            return childElements;
        }
        catch (Exception e) {
            Logger.instance.error("Error occurred in the method: " + name + " because of " + e.getMessage());
        }
        return null;
    }

    /**
     * This Method is used to find the list of column values from the table
     * @param table - table locator
     * @param columnName - name of the column
     * @return String[]
     */
    public String[] getColumnValues(By table, String columnName) {
        String[] columnValues = null;
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            int columnIndex = getTableColumnIndex(table, columnName);

            List<WebElement> columnValuesOBJ = findChildElementsUsingParentLocator(table, By.xpath("//td["+columnIndex+"]"));
            columnValues = new String[columnValuesOBJ.size()];
            for (int col = 0; col < columnValuesOBJ.size(); col++) {
                columnValues[col] = columnValuesOBJ.get(col).getText();
            }

        } catch (Exception e) {
            Logger.instance.error("Error occurred in the method: " + name + " because of " + e.getMessage());
        }
        return columnValues;
    }

    /**
     * This method is to get the table heading column index value
     * @param table - table element locator
     * @param columnName - Column name
     * @return int
     */
    public int getTableColumnIndex(By table, String columnName) {
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        int columnIndex = 0;
        try {
            List<WebElement> columnHeaderOBJ = findChildElementsUsingParentLocator(table, By.tagName("th"));

            for (WebElement column : columnHeaderOBJ) {
                String currentColumnName = column.getText().trim();
                columnIndex++;
                if (currentColumnName.equals(columnName)) {
                    Logger.instance.info(columnName +" Column is exists in Table");
                    break;
                }
            }
        } catch (Exception e) {
        Logger.instance.error("Error occurred in the method: " + name + " because of " + e.getMessage());
    }
        return columnIndex;
    }

    /**
     * This method is to set the checkbox
     * @param locator - element locator
     */
    public void setCheckBox(By locator, boolean isJSClick){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            WebElement element = findElement(locator, 15);
            if (element != null && element.isDisplayed())
            {
                if(!element.isSelected()){
                    if(isJSClick) {
                        clickByJS(locator);
                    }
                    else {
                        element.click();
                    }
                    Logger.instance.info("CheckBox is checked");
                }
                else {
                    Logger.instance.info("CheckBox is already checked");
                }
            }
            else
            {
                Logger.instance.error("Element is not displayed in the page");
            }
        }
        catch(Exception exp){
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method is to uncheck the checkbox
     * @param locator - element locator
     */
    public void uncheckCheckBox(By locator, boolean isJSClick){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try
        {
            WebElement element = findElement(locator, 15);
            if (element != null && element.isDisplayed())
            {
                if(element.isSelected()){
                    if(isJSClick) {
                        clickByJS(locator);
                    }
                    else {
                        element.click();
                    }
                    Logger.instance.info("CheckBox is unchecked");
                }
                else {
                    Logger.instance.info("CheckBox is already unchecked");
                }
            }
            else
            {
                Logger.instance.error("Element is not displayed in the page");
            }
        }
        catch(Exception exp){
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    /**
     * This method is to click the column value in the table.
     * @param table - Table locator
     * @param columnName - Name of the column header
     * @param columnValue - Value of the column
     */
    public void clickColumnValueInTable(By table, String columnName, String columnValue){
        String name = new Object() {}.getClass().getEnclosingMethod().getName();
        try {
            boolean isExists = false;
            int columnIndex = getTableColumnIndex(table, columnName);
            List<WebElement> rowsElement = findChildElementsUsingParentLocator(table, By.xpath("//td["+columnIndex+"]"));
            for (WebElement element : rowsElement) {
                String getRowElement = element.getText();
                if (getRowElement.equalsIgnoreCase(columnValue)) {
                    WebElement columnLink = element.findElement(By.tagName("a"));
                    clickByJS(columnLink);
                    Logger.instance.info(columnValue + " value is clicked successfully.");
                    isExists = true;
                    break;
                }
            }
            if(! isExists) {
                Logger.instance.error("Failed to click " + columnValue + " link in the store table.");
            }
        } catch(Exception exp){
            Logger.instance.error("Error occurred in the method: " + name +" because of " + exp.getMessage());
        }
    }

    public void keysAction(Keys keyToSend) {
        getAction().sendKeys(keyToSend).perform();
    }
}