package com.qa.pages;

import com.qa.BaseTest;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class SettingPage extends BaseTest {
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-LOGOUT\"]")
    private WebElement logoutButton;
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Close\"]/android.widget.ImageView")
    private WebElement closeButton;

    public LoginPage clickLogout() {
        System.out.println("click on logout button");
        click(logoutButton);
        return new LoginPage();
    }

    public ProductsPage clickClose() {
        System.out.println("click on close button to close the setting page");
        click(closeButton);
        return new ProductsPage();
    }
}
