package com.qa.listeners;

import com.qa.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class TestListener implements ITestListener {
    // to print the test case name before each test case
    public void  onTestStart (ITestResult result) {

    }
    // on finched test case
    public void onTestFinish (ITestResult result) {

    }
    public void onTestSuccess (ITestResult result) {

    }


    public void onTestFailure (ITestResult result) {
        if(result.getThrowable() != null) {
            StringWriter sw= new StringWriter();
            PrintWriter pw= new PrintWriter(sw);
            result.getThrowable().printStackTrace (pw);
            System.out.println(sw.toString());
        }
        BaseTest baseTest = new BaseTest();
        File file = baseTest.getDriver().getScreenshotAs(OutputType.FILE);

        Map<String,String> prams =new HashMap<>();
        prams = result.getTestContext().getCurrentXmlTest().getAllParameters();

        String imagePathe = "screenshot" + File.separator + prams.get("platformName") + "_" + prams.get("platformVersion") + "_" + prams.get("deviceName")
                + "_" + File.separator +baseTest.getDateTime()+ File.separator + result.getTestClass().getRealClass().getSimpleName()+ File.separator
                + result.getName() + ".png" ;

        String completeImagePath = System.getProperty("user.dir") + File.separator + imagePathe;
        try {
            FileUtils.copyFile(file, new File(imagePathe));
            Reporter.log("This is the sample screenshot");
            Reporter.log("<a href='" + completeImagePath + "'> <img src='" + completeImagePath + "' height='100' width='100'/> <" +
                    "/a>");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
