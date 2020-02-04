import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class lessonWebDriverWait {
    private static WebDriver driver;
    private static WebDriverWait wait;

    static String userMail = "admitrieva@plesk.com";
    static String userPassword = "ifgrfc";
    static int timeout = 3;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, timeout);
    }

    @Test
    public void addToProductToCard() throws InterruptedException {
        loginAsUser();
        addToCart(3);
        removeFromCart();
    }

    private void removeFromCart() throws InterruptedException {
        WebElement element = driver.findElement(By.xpath("//a[@class = 'link']"));
        wait.until(visibilityOf(element));
        element.click();

        while (driver.findElements(By.name("remove_cart_item")).size() > 0) {
            //Click on remove button
            element = driver.findElement(By.name("remove_cart_item"));
            wait.until(visibilityOf(element));
            element.click();

            //wait for table refresh
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            wait.until(ExpectedConditions.stalenessOf(
                    driver.findElement(By.cssSelector("table.dataTable"))));
            driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
        }
    }

    private void addToCart(int num) {
        for (int i = 0; i < num; i++) {
            driver.get("http://localhost/litecart/en/");

            List<WebElement> elements = driver.findElements(By.xpath("//*[@id='box-most-popular']//li[@class = 'product column shadow hover-light']"));
            elements.get(1).click();

            String initialProductsCount = driver.findElement(By.xpath("//span[@class = 'quantity']")).getText();
            System.out.println("Current count of products in cart is " + initialProductsCount);

            //Wait for the button 'Add To Cart' appears and click on it
            WebElement element = driver.findElement(By.name("add_cart_product"));
            wait.until(visibilityOf(element));
            element.click();

            //wait until link with old value disappears
            //wait.until(ExpectedConditions.not(invisibilityOfElementWithText(By.xpath("//span[@class = 'quantity']"), initialProductsCount)));
            wait.until(ExpectedConditions.not(
                    textToBePresentInElement(
                            driver.findElement(By.xpath("//span[@class = 'quantity']")), initialProductsCount
                    )
            ));
        }
    }

    private void loginAsUser() {
        String loginLocator = "//*[@id='navigation']//button[@name='login']";
        String loginButton = "//button[@name='login']";

        driver.get("http://localhost/litecart/en/");
        WebElement element = driver.findElement(By.xpath(loginLocator));
        wait.until(visibilityOf(element));
        element.click();
        element = driver.findElement(By.xpath(loginButton));
        wait.until(visibilityOf(element));

        driver.findElement(By.xpath(loginButton)).click();
        driver.findElement(By.name("email")).sendKeys(userMail);
        driver.findElement(By.name("password")).sendKeys(userPassword);
        driver.findElement(By.xpath(loginLocator)).click();
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
