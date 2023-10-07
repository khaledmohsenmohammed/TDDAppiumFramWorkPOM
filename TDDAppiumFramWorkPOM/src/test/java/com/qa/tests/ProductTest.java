package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductDetailPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingPage;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ProductTest extends BaseTest {
    LoginPage loginPage;
    ProductsPage productsPage;
    InputStream dataIs;
    JSONObject loginUsersData;
    SettingPage settingPage;

    ProductDetailPage productDetailPage;

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
        System.out.println("the Start Test -- " + m.getName() + "*********** \n");
        loginPage = new LoginPage();
        productsPage = new ProductsPage();
    }

    @Test(priority = 1)
    public void login() {
        loginPage.enterUserName(loginUsersData.getJSONObject("validUser").getString("username"));
        loginPage.enterPass(loginUsersData.getJSONObject("validUser").getString("password"));
        loginPage.clickLogin();
    }

    @Test(priority = 2)
    public void validateProductOnProductsPage() {
        SoftAssert softAssert = new SoftAssert();
        String SLBTitle = productsPage.getSLBTitle();
        String SLBPrice = productsPage.getSLBPrice();
        softAssert.assertEquals(SLBTitle, strings.get("product_page_slb_title"));
        softAssert.assertEquals(SLBPrice, strings.get("product_page_slb_price"));
        softAssert.assertAll();
    }

    @Test(priority = 3)
    public void validateProductOnProductsDetailisPage() {

        SoftAssert softAssert = new SoftAssert();

        productDetailPage = productsPage.clickSLBTitle();
        String SLBTitle = productDetailPage.getProductTitle();
        softAssert.assertEquals(SLBTitle, strings.get("product_product_description_page_slb_description_page_slb_title"));

        String SLBDesc = productDetailPage.getProductDesc();
        softAssert.assertEquals(SLBDesc, strings.get("product_description_page_slb_description"));

        productDetailPage.scrollingToItem();
        String SLBDescPrice = productDetailPage.getProductPrice();
        softAssert.assertEquals(SLBDescPrice, strings.get("product_description_page_slb_price"));

        productsPage.clickBack();
        settingPage = productsPage.clickMenu();
        loginPage = settingPage.clickLogout();

        softAssert.assertAll();
    }
}
