import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.*;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class HomeWorks {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void clickItems() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        DesiredCapabilities caps = new DesiredCapabilities();
        loginAsAdmin();
        String mainLocator = "//*[@id='sidebar']//li[@class='selected']/following-sibling::*";
        String headerLocator = "//h1";

        WebElement element = wait.until(presenceOfElementLocated(By.xpath("//*[@class='dataTable']")));

        Assert.assertTrue(
                "There are no menu elements at all! Something went wrong!!",
                areElementsPresent(By.xpath("//*[@id='sidebar']//li"))
        );
//Click first item of menu
        driver.findElement(By.xpath("//*[@id='sidebar']//li[1]")).click();
        Thread.sleep(3000);

//Click the next menu item after already opened
        while (areElementsPresent(By.xpath(mainLocator))) {
            driver.findElement(By.xpath(mainLocator)).click();

//Check header
// it`s better to collect all missing headers in some var and print message with screenshot about all missing headers
            Assert.assertTrue(isElementPresent(By.xpath(headerLocator)));
        }
    }

    private void loginAsAdmin() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    boolean isElementPresent(By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    boolean areElementsPresent(By locator) {
        return driver.findElements(locator).size() > 0;
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
