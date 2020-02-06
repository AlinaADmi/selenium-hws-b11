package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class ProductPage extends Page {
    @FindBy(xpath = "//span[@class = 'quantity']")
    private WebElement productsCount;

    @FindBy(name = "add_cart_product")
    private WebElement addToCartButton;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void addToCart() {
        String initialProductsCount = productsCount.getText();
        System.out.println("Current count of products in cart is " + initialProductsCount);

        //Wait for the button 'Add To Cart' appears and click on it
        wait.until(visibilityOf(addToCartButton));
        addToCartButton.click();

        //wait until link with old value disappears
        wait.until(ExpectedConditions.not(
                textToBePresentInElement(
                        driver.findElement(By.xpath("//span[@class = 'quantity']")), initialProductsCount
                )
        ));
    }
}
