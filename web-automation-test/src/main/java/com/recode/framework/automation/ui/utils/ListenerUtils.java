package com.recode.framework.automation.ui.utils;

import com.recode.framework.automation.ui.TestClass;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;


public class ListenerUtils extends TestClass implements ITestListener {

    @Override
    public void onFinish(ITestContext Result)
    {
    }

    @Override
    public void onStart(ITestContext Result)
    {
        System.setProperty("org.uncommons.reportng.escape-output", "false");
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult Result)
    {
    }

    // When Test case get failed, this method is called.
    @Override
    public void onTestFailure(ITestResult Result)
    {
        String testProtocol = Result.getTestClass().getName();
        String methodName = Result.getMethod().getMethodName();
        Logger.instance.info(Result.getName() + " test case gets failed");
        String destFile = Utils.captureScreenshot(theWebDriver, testProtocol, methodName);
        String wsFile = destFile.replaceFirst("(.*)/workspace/", "/job/");
        int index = wsFile.indexOf("/", 6);
        StringBuilder wsPath = new StringBuilder(wsFile);
        wsPath.insert(index + 1, "ws/");
        Logger.instance.info("Failed Screenshot Location is  " + wsPath);
        Reporter.log("<br><b>Exception: </b><br>");
        Reporter.log("<br><a href='" + wsPath + "'> <img src='" + wsPath + "' height='100' width='100'/> </a><br>");
    }

    // When Test case get Skipped, this method is called.
    @Override
    public void onTestSkipped(ITestResult Result)
    {
        Logger.instance.info(Result.getName() + " test case skipped");
    }

    // When Test case get Started, this method is called.
    @Override
    public void onTestStart(ITestResult Result)
    {
        Logger.instance.info("*************************************************************");
        Logger.instance.info(Result.getName() + " test case started execution");
        Logger.instance.info("*************************************************************");
    }

    // When Test case get passed, this method is called.
    @Override
    public void onTestSuccess(ITestResult Result)
    {
        Logger.instance.info(Result.getName() + " test case executed successfully");
    }
}
