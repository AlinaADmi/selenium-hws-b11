package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class CartPage extends Page {
    @FindBy(xpath = "//a[@class = 'link']")
    WebElement cartLink;

    @FindBy(name = "remove_cart_item")
    WebElement removeLink;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void removeFromCart() {
        wait.until(visibilityOf(cartLink));
        cartLink.click();
        WebElement element;
        while (driver.findElements(By.name("remove_cart_item")).size() > 0) {
            //Click on remove button
            element = driver.findElement(By.name("remove_cart_item"));
            wait.until(visibilityOf(removeLink));
            element.click();

            //wait for table refresh
            switchImplicitlyWaitOff();
            wait.until(ExpectedConditions.stalenessOf(
                    driver.findElement(By.cssSelector("table.dataTable"))));
            switchImplicitlyWaitOn();
        }
    }
}
