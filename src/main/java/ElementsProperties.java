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
        checkCountriesSorting();
        //checkGeoZonesSorting();
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

            assertSorting(nativeZonesList);
        }
    }

    private void checkCountriesSorting() {
        openPage(
                "http://localhost/litecart/admin/?app=countries&doc=countries",
                "//a[@class='button']"
        );

        ArrayList<String> nativeList = new ArrayList<>();
        ArrayList<String> hrefs = new ArrayList<>();

        List<WebElement> elements = driver.findElements(
                By.xpath("//a[contains(@href, 'country_code') and not(@title='Edit')]/parent::*")
        );
        for (WebElement element : elements) {
            element.findElement(By.tagName("a")).getText();

            if (Integer.parseInt(
                    element.findElement(By.xpath(".//parent::*/following-sibling::*")).getText()
            ) > 0) {
                hrefs.add(element.findElement(By.tagName("a")).getAttribute("href"));
            }
        }

        assertSorting(nativeList);

        //Check sorting of states for countries with states
        checkCountryStatesSorting(hrefs);
    }

    private void checkCountryStatesSorting(ArrayList<String> hrefs) {
        for (int i = 0; i < hrefs.size(); i++) {
            openPage(hrefs.get(i), "//button[@name='save']");
            List<WebElement> elements = driver.findElements(
                    By.xpath("//table[@id='table-zones']//*[contains(@name, '[name]') and @type = 'hidden']")
            );

            ArrayList<String> statesList = new ArrayList<>();
            for (WebElement element : elements) {
                statesList.add(element.getText());
            }
            assertSorting(statesList);
        }
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

    private void assertSorting(ArrayList<String> nativeList) {
        ArrayList<String> sortedList = new ArrayList<>(nativeList);
        Collections.sort(sortedList);
        Assert.assertEquals(sortedList, nativeList);
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
