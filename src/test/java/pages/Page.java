package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class Page {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private int timeout = 10;

    public Page(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, timeout);
        PageFactory.initElements(driver, this);
    }

    public void switchImplicitlyWaitOn() {
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    public void switchImplicitlyWaitOff() {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
    }

    public void  checkSelector(By element, int index)
    {
        Select menu = new Select(driver.findElement(element));
        menu.selectByIndex(index);
    }

    public void openPage(String url, By element) {
        driver.get(url);
        wait.until(presenceOfElementLocated(element));
    }

    public void clickOnElement(By element){
        driver.findElement(element).click();
    }

    protected boolean isElementPresent(WebDriver driver, By locator) {
        return driver.findElements(locator).size() > 0;
    }

    protected boolean isElementNotPresent(WebDriver driver, By locator) {
        try {
            switchImplicitlyWaitOff();
            return driver.findElements(locator).size() == 0;
        } finally {
            switchImplicitlyWaitOn();
        }
    }
}
