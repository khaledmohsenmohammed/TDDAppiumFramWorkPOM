package com.qa.pages;

import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;
import com.qa.BaseTest;

public class LoginPage extends BaseTest {

    //Action code to find the element and send the data
    @AndroidFindBy(accessibility = "test-Username")
    private WebElement userNameInput;
    @AndroidFindBy(accessibility = "test-Password")
    private WebElement passNameInput;
    @AndroidFindBy(accessibility = "test-LOGIN")
    private WebElement loginButton;

    //insert the data in the element
    public LoginPage enterUserName(String userName) {
        System.out.println("the user name is : " + userName);
        clear(userNameInput);
        sendKeys(userNameInput, userName);
        return this;
    }

    public LoginPage enterPass(String pass) {
        System.out.println("the password is : " + pass);
        clear(passNameInput);
        sendKeys(passNameInput, pass);
        return this;
    }

    public ProductsPage clickLogin() {
        System.out.println("click on login button");
        click(loginButton);
        return new ProductsPage();
    }

    public ProductsPage login(String userName, String pass) {
        System.out.println("login with : " + userName + " and " + pass);
        enterUserName(userName);
        enterPass(pass);
        return clickLogin();
    }

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Error message\"]/android.widget.TextView")
    private WebElement errorMessage;

    public String getErrorMessage() {
        String error = getAttribute(errorMessage, "text");
        System.out.println("the error message is : " + error);
        return error;
    }
}
