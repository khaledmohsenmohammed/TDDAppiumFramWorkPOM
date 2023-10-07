package com.qa.pages;

import com.qa.BaseTest;
import com.qa.MenuPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class ProductsPage extends MenuPage {
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Cart drop zone\"]/android.view.ViewGroup/android.widget.TextView")
    private WebElement proMessage;
    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Item title\"])[1]")
    private WebElement SLBTitle;

    @AndroidFindBy(xpath = "(//android.widget.TextView[@content-desc=\"test-Price\"])[1]")
    private WebElement SLBPrice;
    @AndroidFindBy(accessibility = "test-BACK TO PRODUCTS")
    private WebElement backButtonToProducts; // back to the product page


    public String getTitle() {
        String proTitle = getAttribute(proMessage, "text");
        System.out.println("the product title is : " + proTitle);
        return proTitle;
    }

    public String getSLBTitle() {
        String slbTitle = getAttribute(SLBTitle, "text");
        System.out.println("the product title is : " + slbTitle);
        return slbTitle;
    }

    public String getSLBPrice() {
        String slbPrice = getAttribute(SLBPrice, "text");
        System.out.println("the product title is : " + slbPrice);
        return slbPrice;
    }

    public ProductDetailPage clickSLBTitle() {
        System.out.println("click on SLB Title");
        click(SLBTitle);
        return new ProductDetailPage();
    }

    //back to the product page
    public ProductsPage clickBack() {
        System.out.println("click on back button");
        click(backButtonToProducts);
        return new ProductsPage();
    }
}
