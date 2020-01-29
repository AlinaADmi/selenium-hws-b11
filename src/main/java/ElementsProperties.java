import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class ElementsProperties {
    private static WebDriver driver;
    private static WebDriverWait wait;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void checkSorting() throws InterruptedException {
        loginAsAdmin();
        //checkCountriesSorting();
        checkGeoZonesSorting();
    }

    private void checkGeoZonesSorting() {
        ArrayList<String> linksList = new ArrayList<>();

        openPage(
                "http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones",
                "//a[@class='button']"
        );

        List<WebElement> elements = driver.findElements(
                By.xpath("//a[contains(@href, 'geo_zone_id') and not(@title='Edit')]")
        );

        for (WebElement element : elements) {
            linksList.add(element.getAttribute("href"));
        }

        for (int i = 0; i < linksList.size(); i++) {
            ArrayList<String> nativeZonesList = new ArrayList<>();

            String href = linksList.get(i);
            openPage(href, "//button[@name = 'save']");
            List<WebElement> zones = driver.findElements(
                    By.xpath("//*[contains(@name, 'zone_code')]/*[@selected = 'selected']")
            );
            for (WebElement zone : zones) {
                nativeZonesList.add(zone.getText());
            }

            ArrayList<String> sortedZonesList = new ArrayList<>(nativeZonesList);
            Collections.sort(sortedZonesList);
            Assert.assertEquals(sortedZonesList, nativeZonesList);
        }
    }

    private void checkCountriesSorting() {
        openPage(
                "http://localhost/litecart/admin/?app=countries&doc=countries",
                "//a[@class='button']"
        );

        ArrayList<String> nativeList = new ArrayList<>();

        List<WebElement> elements = driver.findElements(
                By.xpath("//a[contains(@href, 'country_code') and not(@title='Edit')]")
        );
        for (WebElement element : elements) {
            nativeList.add(element.getText());
        }
        ArrayList<String> sortedList = new ArrayList<>(nativeList);

        Collections.sort(sortedList);
        Assert.assertEquals(sortedList, nativeList);
    }

    private void closePage() {
        driver.close();
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
