package com.qa;

import com.qa.pages.SettingPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class MenuPage extends BaseTest {

    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Menu\"]")
    private WebElement menuButton;

    public SettingPage clickMenu() {
        System.out.println("Clicking on Menu button");
        click(menuButton);
        return new SettingPage();
    }
}
