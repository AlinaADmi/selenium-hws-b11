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
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class wfd {
    private static WebDriver driver;
    private static WebDriverWait wait;
    static int timeout = 3;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, timeout);
    }

    @Test
    public void checkLinks() throws InterruptedException {
        loginAsAdmin();
        //Open countries page
        openPage(
                "http://localhost/litecart/admin/?app=countries&doc=countries",
                "//a[@class='button']"
        );

        //Click on Add new
        driver.findElement(By.xpath("//a[@class='button']")).click();

        List<WebElement> elements = driver.findElements(By.xpath("//i[@class = 'fa fa-external-link']"));
        String currentWindow = driver.getWindowHandle();
        Set<String> oldWindows = driver.getWindowHandles();
        for (WebElement link : elements) {
            link.click();
            wait.until(ExpectedConditions.numberOfWindowsToBe(oldWindows.size() + 1));
            driver.switchTo().window(getHandleOfNewWindow(oldWindows));
            driver.close();
            driver.switchTo().window(currentWindow);
        }
    }
    private String getHandleOfNewWindow(Set<String> oldWindows) {
        Set<String> handles = driver.getWindowHandles();
        handles.removeAll(oldWindows);
        return handles.isEmpty() ? null : handles.iterator().next();
    }

    private void openPage(String url, String locator) {
        driver.get(url);
        wait.until(presenceOfElementLocated(By.xpath(locator)));
    }


    private void loginAsAdmin() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
