package com.recode.framework.automation.ui;

import com.recode.framework.automation.ui.utils.Utils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.BeforeTest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TestClass {

    protected static String browserName, os, gridHub,defaultPort;

    /**
     * Creating thread local for Web driver and DesiredCapabilities
     */
    public static ThreadLocal<WebDriver> webDriverLocal = new ThreadLocal<>();
    public static ThreadLocal<DesiredCapabilities> capabilitiesLocal = new ThreadLocal<>();
    public static DesiredCapabilities capabilities;
    public static RemoteWebDriver theWebDriver;
    private static final TestClass instance = new TestClass();

    /**
     * This Method is used to get the web driver instance for parallel execution
     * @return - instance
     */
    public static TestClass getInstance(){
        return instance;
    }

    /**
     * This Method is a getter for WebDriver
     * @return - WebDriver
     * Description - Here Getters and setters methods are used for web driver as well as desired capabilities to
     * ensure the code is thread safe for parallel execution.
     */

    public static WebDriver getDriver()
    {
        return webDriverLocal.get();
    }

    /**
     * This Method is a setter for WebDriver
     * Description - Here Getters and setters methods are used for web driver as well as desired capabilities to
     * ensure the code is thread safe for parallel execution.
     */

    public static void setDriver(WebDriver driver) {
        webDriverLocal.set(driver);
    }

    /**
     * This Method is to remove the particular WebDriver
     *
     */
    public static void removeDriver(){
        webDriverLocal.get().quit();
        webDriverLocal.remove();
    }

    /**
     * This Method is a getter for DesiredCapabilities
     * @return - DesiredCapabilities
     * Description - Here Getters and setters methods are used for web driver as well as desired capabilities to
     * ensure the code is thread safe for parallel execution.
     */
    public static DesiredCapabilities getDesiredCapabilities()
    {
        return capabilitiesLocal.get();
    }

    /**
     * This Method is a setter for DesiredCapabilities
     * Description - Here Getters and setters methods are used for web driver as well as desired capabilities to
     * ensure the code is thread safe for parallel execution.
     */
    public static void setDesiredCapabilities(DesiredCapabilities capabilities) {
        capabilitiesLocal.set(capabilities);
    }


    @BeforeTest
    public static void setUp() {
        browserName = Utils.getAutomationProperties().getProperty("BrowserName");
        os = Utils.getAutomationProperties().getProperty("OS");
        gridHub = Utils.getAutomationProperties().getProperty("Hub");
        defaultPort = Utils.getAutomationProperties().getProperty("DefaultPort");
    }

    // Method - setup all the test environments for the execution like OS, Browser and Grid Hub URL
    public static RemoteWebDriver setUpDriver(String browser) throws MalformedURLException
    {
        // Setting the Test environment in Desired capabilities
        if (browser.equalsIgnoreCase("chrome")) {
            capabilities = new DesiredCapabilities();
            setDesiredCapabilities(capabilities);
            getDesiredCapabilities().setBrowserName("chrome");
            getDesiredCapabilities().setAcceptInsecureCerts(true);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--disable-automation");
            options.addArguments("--test-type");
            options.addArguments("--disable-extensions");
            options.addArguments("disable-infobars");
            options.addArguments("--disable-notifications");
            options.addArguments("--disable-popup-blocking");
            //options.addArguments("--headless");
            options.addArguments("start-maximized");
            Map<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("credentials_enable_service", false);
            prefs.put("profile.password_manager_enabled", false);
            options.setExperimentalOption("prefs", prefs);
            options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
            options.setExperimentalOption("useAutomationExtension", false);
            getDesiredCapabilities().setCapability(ChromeOptions.CAPABILITY,options);
            options.merge(getDesiredCapabilities());
        } else if(browser.equalsIgnoreCase("firefox")) {
            capabilities = new DesiredCapabilities();
            setDesiredCapabilities(capabilities);
            getDesiredCapabilities().setBrowserName("firefox");
            getDesiredCapabilities().setCapability("marionette",true);
            FirefoxOptions options = new FirefoxOptions();
            //options.setHeadless(true);
            options.merge(getDesiredCapabilities());

        } else {
            capabilities = new DesiredCapabilities();
            setDesiredCapabilities(capabilities);
            getDesiredCapabilities().setBrowserName("internetExplorer");
            capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, false);
            capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
            capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.merge(getDesiredCapabilities());
        }
        Platform platform = Platform.fromString(os.toUpperCase());
        capabilities.setCapability("platform", platform);
        theWebDriver = new RemoteWebDriver(new URL(gridHub + ":" + defaultPort + "/wd/hub"), getDesiredCapabilities());
        setDriver(theWebDriver);
        theWebDriver.manage().window().maximize();
        return theWebDriver;
    }
}
