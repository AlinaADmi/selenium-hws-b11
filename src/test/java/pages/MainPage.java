package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MainPage extends Page {

    @FindBy(xpath = "//div[@class = 'content']//div[@class='name']")
    public WebElement menuItem;

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public void chooseFirstItem() {
        open();
        menuItem.click();
    }

    public void open() {
        openPage("http://localhost/litecart/en/", By.xpath("//button[@name = 'login']"));
    }
}
