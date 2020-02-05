import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class printErrorsFromBrowserLogs {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void checkBrowserLogs() throws InterruptedException {
        //Clear browser logs
        driver.manage().logs().get("browser").getAll();

        loginAsAdmin();
        openPage(
                "http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1",
                "//button[@name='enable']"
        );

        ArrayList<String> hrefs = new ArrayList<>();
        List<WebElement> elements = driver.findElements(
                By.xpath(
                        "//*[@class = 'dataTable']//a[contains(@href, 'product_id') and not(@title='Edit')]"
                )
        );

        for (WebElement element : elements) {
            hrefs.add(element.getAttribute("href"));
        }
        for (int i = 0; i < hrefs.size(); i++) {
            openPage(hrefs.get(i), "//button[@name='save']");
        }

        //Get browser log and print messages if it is not empty
        printBrowserLog();
    }

    private void openPage(String url, String locator) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(locator))));
    }

    private void loginAsAdmin() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
    }

    private void printBrowserLog() {
        List<LogEntry> fullLog = driver.manage().logs().get("browser").getAll();
        if (fullLog.size() != 0) {
            for (LogEntry log : fullLog)
                System.out.println(log);
        }
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
