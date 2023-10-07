package com.qa.pages;

import com.qa.MenuPage;
import io.appium.java_client.pagefactory.AndroidFindBy;
import org.openqa.selenium.WebElement;

public class ProductDetailPage extends MenuPage {
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[1]")
    private WebElement productTitle;
    @AndroidFindBy(xpath = "//android.view.ViewGroup[@content-desc=\"test-Description\"]/android.widget.TextView[2]")
    private WebElement productDesc;

    @AndroidFindBy(accessibility = "test-BACK TO PRODUCTS")
    private WebElement backButtonToProducts;
    @AndroidFindBy(accessibility = "test-Price")
    private WebElement productPrice;

    public String getProductTitle() {
        String pt = getAttribute(productTitle, "text");
        System.out.println("the product title is : " + pt);
        return pt;
    }

    public String getProductDesc() {
        String pDesc = getAttribute(productDesc, "text");
        System.out.println("the product description is : " + pDesc);
        return pDesc;
    }
    public String getProductPrice() {
        String pPrice = getAttribute(productPrice, "text");
        System.out.println("the product price is : " + pPrice);
        return pPrice;
    }

    public ProductDetailPage scrollToProductPrice() {
        scrollingToItem();
        return this;
    }

    public ProductsPage clickBack() {
        System.out.println("click on back button");
        click(backButtonToProducts);
        return new ProductsPage();
    }

}
