package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class LoginTests extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
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
        closeApp();
        launchApp();

    }

    @BeforeMethod
    public void beforeMethodFunction(Method m) { // to prent the method from running before each test case
        loginPage = new LoginPage();
        productsPage = new ProductsPage();
        System.out.println("the Start Test -- " + m.getName() + "*********** \n");
    }

    @Test
    public void invalidUserName() {

        loginPage.enterUserName(loginUsersData.getJSONObject("invalidUser").getString("username"));
        loginPage.enterPass(loginUsersData.getJSONObject("invalidUser").getString("password"));
        loginPage.clickLogin();

        //find the disappeared element after the click on the element to make sure that the click is done
        String actualErrorMessage = loginPage.getErrorMessage() + "fdf"; //==> make it file manually to fail the test case
        String expectedErrorMessage = strings.get("error_invalid_username_or_pass");
        System.out.println("the Error message -- " + actualErrorMessage + "\n Expected Error Message -- " + expectedErrorMessage);

        //assertion on the element
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage);
    }

    @Test
    public void invalidPass() {
        loginPage.enterUserName(loginUsersData.getJSONObject("invalidPass").getString("username"));
        loginPage.enterPass(loginUsersData.getJSONObject("invalidPass").getString("password"));
        loginPage.clickLogin();

        //find the disappeared element after the click on the element to make sure that the click is done
        String actualErrorMessage = loginPage.getErrorMessage();
        String expectedErrorMessage = strings.get("error_invalid_username_or_pass");
        System.out.println("the Error message -- " + actualErrorMessage + "\n Expected Error Message -- " + expectedErrorMessage);
        //assertion on the element
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage);
    }

    @Test
    public void emptyUserName() {
        loginPage.enterUserName("");
        loginPage.enterPass(loginUsersData.getJSONObject("validUser").getString("password"));
        loginPage.clickLogin();
        //find the disappeared element after the click on the element to make sure that the click is done
        String actualErrorMessage = loginPage.getErrorMessage();
        String expectedErrorMessage = "Username is required";
        System.out.println("the Error message -- " + actualErrorMessage + "\n Expected Error Message -- " + expectedErrorMessage);
        //assertion on the element
        Assert.assertEquals(actualErrorMessage, expectedErrorMessage);
    }

    //test case to log in successfully
    @Test
    public void successfullyLogin() {
        loginPage.enterUserName(loginUsersData.getJSONObject("validUser").getString("username"));
        loginPage.enterPass(loginUsersData.getJSONObject("validUser").getString("password"));
        loginPage.clickLogin();
        String actualTitle = productsPage.getTitle();
        String expectedTitle = strings.get("product_title");
        System.out.println("the Actual Title -- " + actualTitle + "\n Expected Title Message -- " + expectedTitle);
        //assertion on the element
        Assert.assertEquals(actualTitle, expectedTitle);

    }
    @AfterMethod
    public void afterMethodFunction(Method m) { // to prent the method from running before each test case

    }
    @AfterClass
    public void afterClassFunction() {
        closeApp();
    }
}
