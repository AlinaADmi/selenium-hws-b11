package app;

import org.junit.After;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CartPage;
import pages.MainPage;
import pages.ProductPage;

public class Application {
    private WebDriver driver;
    private WebDriverWait wait;
    public MainPage mainPage;
    public CartPage cartPage;
    public ProductPage productPage;

    public Application() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
        mainPage = new MainPage(driver);
        cartPage = new CartPage(driver);
        productPage = new ProductPage(driver);
    }

    @After
    public void quit() {
        driver.quit();
    }
}
