package com.qa;

import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.screenrecording.CanRecordScreen;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BaseTest {
    protected static AppiumDriver driver;
    protected static Properties props;
    //HashMap to store the strings
    protected static HashMap<String, String> strings = new HashMap<String, String>();
    protected static String dateTime;
    InputStream inputStream;
    //to read the strings.xml file
    InputStream stringsis;
    TestUtils utils;
     static String getPlatformName;

    //constructor
    public BaseTest() {
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }

    @Parameters({"emulator", "platformName", "deviceName"})
    @BeforeTest
    public void beforeTest(String emulator, String platformName, String deviceName) throws Exception {
        utils = new TestUtils();
        dateTime = utils.dateTime();
        getPlatformName = platformName;
        try {
            //strings definition to read from strings.xml file
            String xmlFileName = "strings/strings.xml";
            // to read the strings.xml file
            stringsis = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            strings = utils.parseStringXML(stringsis);

            //properties definition
            props = new Properties();
            String propFileName = "config.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);
            //Appium Driver definition
            DesiredCapabilities caps = new DesiredCapabilities();
            //value from testng.xml and passing the value in the method parameters
            caps.setCapability(MobileCapabilityType.PLATFORM_NAME, platformName);
            URL url;
            switch (platformName) {
                case "Android" -> {
                    caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
                    //this data is coming from the config.properties file
                    caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.getProperty("androidAutomationName"));
                    if (emulator.equalsIgnoreCase("true")) {
                        caps.setCapability(MobileCapabilityType.UDID, props.getProperty("androidEmulator"));
                    } else {
                        caps.setCapability(MobileCapabilityType.UDID, props.getProperty("realDeviceName"));
                    }
                    caps.setCapability("appPackage", props.getProperty("androidAppPackage"));
                    caps.setCapability("appActivity", props.getProperty("androidAppActivity"));
/*
                    //the app path is coming from the config.properties file
                     String appURL = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "app" + File.separator + props.getProperty("androidAppPath");
                    //log the app path
                     System.out.println("AppURLPath ===> " + appURL);
                    //to start from the specific activity
                    caps.setCapability(MobileCapabilityType.APP, appURL);
*/
                    //to run on emulator
                    url = new URL(props.getProperty("appiumURL"));
                    driver = new AndroidDriver(url, caps);
                    System.out.println("Session id = " + driver.getSessionId());
                    String sessionId = driver.getSessionId().toString();
                }
                case "iOS" -> {
                    caps.setCapability(MobileCapabilityType.DEVICE_NAME, deviceName);
                    //this data is coming from the config.properties file
                    caps.setCapability(MobileCapabilityType.AUTOMATION_NAME, props.getProperty("iOSAutomationName"));
                    caps.setCapability(MobileCapabilityType.UDID, "00008030-000D3A1E0A00802E");
                    caps.setCapability("bundleId", props.getProperty("iOSBundleId"));
                    String IOSappURL = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "app" + File.separator + props.getProperty("iOSAppPath");
                    //log the app path
                    System.out.println("AppURLPath ===> " + IOSappURL);
                    //to start from the specific activity
                    caps.setCapability(MobileCapabilityType.APP, IOSappURL);

                    //to run on emulator
                    url = new URL(props.getProperty("appiumURL"));
                    driver = new IOSDriver(url, caps);
                    System.out.println("Session id = " + driver.getSessionId());
                    String sessionId1 = driver.getSessionId().toString();
                }
                default -> throw new Exception("Invalid platform! - " + platformName);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (stringsis != null) {
                stringsis.close();
            }
        }
    }

    InputStream dataIs;
    JSONObject loginUsersData;

    @BeforeClass
    public void beforeClassFunction() throws IOException {

        //to get the data from the json file
        try {
            String dataFileName = "data/loginUsers.json";
            dataIs = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener jsonTokener = new JSONTokener(dataIs);
            loginUsersData = new JSONObject(jsonTokener);
        } catch (Exception e) {
            e.printStackTrace();
            throw e; // to throw the exception to the testng.xml file
        } finally { // to close the stream of the data from the json file
            if (dataIs != null) {
                dataIs.close();
            }
        }

    }

    @BeforeMethod
    public void beforeMethodFunctionBaseClass() { //implement the record screen start
        System.out.println("the Super -beforeMethodFunctionBaseClass- " + "*********** \n");
        ((CanRecordScreen)driver).startRecordingScreen();

    }
    @AfterMethod
    public void afterMethodFunctionBaseClass (ITestResult result){ //implement the record screen stop
        System.out.println("the Super -beforeMethodFunctionBaseClass- " + "*********** \n");

       String media = ((CanRecordScreen)driver).stopRecordingScreen();
       // to chake the status of test case
        if(result.getStatus() == 2) {
            //folder structure
            Map<String , String > Prams = result.getTestContext().getCurrentXmlTest().getAllParameters();
            String dir = "videos" + File.separator + Prams.get("platformName") + "_" + Prams.get("platformVersion") + "_" + Prams.get("deviceName")
                    + "_" + File.separator + dateTime + File.separator + result.getTestClass().getRealClass().getSimpleName();
            File videoDir = new File(dir);
            if(!videoDir.exists()){
                videoDir.mkdirs();
            }
            try {
                FileOutputStream stream = new FileOutputStream(videoDir+File.separator+result.getName() + ".mp4");
                try {
                    //**************not matched the cource code
                    stream.write(Base64.decodeBase64(media));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //wait for visibility
    public void waitForVisibility(WebElement e) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT));
        wait.until(ExpectedConditions.visibilityOf(e));
    }

    /*public void waitForVisibility(WebElement e){
	  Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
	  .withTimeout(Duration.ofSeconds(50))
	  .pollingEvery(Duration.ofSeconds(30))
	  .ignoring(NoSuchElementException.class);
	  wait.until(ExpectedConditions.visibilityOf(e));
	  }*/
    public void click(WebElement element) {
        waitForVisibility(element);
        element.click();
    }

    public void sendKeys(WebElement element, String txt) {
        waitForVisibility(element);
        element.sendKeys(txt);
    }

    public String getAttribute(WebElement element, String txt) {
        waitForVisibility(element);
        return element.getAttribute(txt);
    }

    public void clear(WebElement element) {
        waitForVisibility(element);
        element.clear();
    }

    // this method to close the app after the test case is done
    public void closeApp() {
        switch (getPlatformName) {
            case "Android" -> {
                ((InteractsWithApps) driver).terminateApp(props.getProperty("androidAppPackage"));
            }
            case "iOS" -> {
                System.out.println("<<<<<<<<<< iOS >>>>>>>>>>");
            }

            default -> {
                throw new IllegalStateException("Unexpected value: " + getPlatformName);
            }
        }
    }

    public void launchApp() {
        switch (getPlatformName) {
            case "Android" -> {
                ((InteractsWithApps) driver).activateApp(props.getProperty("androidAppPackage"));
            }
            case "iOS" -> {
                System.out.println("<<<<<<<<<< iOS >>>>>>>>>>");
            }

            default -> {
                throw new IllegalStateException("Unexpected value: " + getPlatformName);
            }
        }

    }

    public WebElement scrollingToItem (){
        return driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
                        + "new UiSelector().description(\"test-Price\"));"));
    }
    public String getDateTime() {
        return dateTime;
    }

    @AfterTest
    public void afterTest() {
        //driver.quit();
    }

    public AppiumDriver getDriver() {
        return driver;
    }
}
