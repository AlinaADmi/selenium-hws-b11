import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.security.SecureRandom;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class ActionsWithElements {
    private static WebDriver driver;
    private static WebDriverWait wait;

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    static String userMail;
    static String userPassword;

    @Before
    public void start() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, 30);
    }

    @Test
    public void registerUser() throws InterruptedException {

        userMail = generateMail(5);
        userPassword = generateRandomString(8);

        driver.get("http://localhost/litecart/");

        //Open registration form
        driver.findElement(By.xpath("//*[@id='navigation']//a[contains(@href, 'create_account')]")).click();
        wait.until(presenceOfElementLocated(By.name("firstname")));

        System.out.println(
                "Try to register user with email " + userMail.toString() +
                        " and password " + userPassword.toString()
        );

        //Fill only required fields
        driver.findElement(By.name("firstname")).sendKeys("Firstname");
        driver.findElement(By.name("lastname")).sendKeys("Lastname");
        driver.findElement(By.name("address1")).sendKeys("Some street 22");
        driver.findElement(By.name("postcode")).sendKeys("35004");
        driver.findElement(By.name("city")).sendKeys("Moody");
        driver.findElement(By.xpath("//span[@class='select2-selection select2-selection--single']")).click();
        driver.findElement(By.xpath("//*[@name = 'country_code']/option[@value='US']")).click();
        wait.until(visibilityOfElementLocated(By.xpath("//select[@name = 'zone_code']")));
        driver.findElement(By.xpath("//select[@name = 'zone_code']")).click();
        driver.findElement(By.xpath("//select[@name = 'zone_code']/option[@value='CA']")).click();
        driver.findElement(By.name("email")).sendKeys(userMail);
        driver.findElement(By.name("phone")).sendKeys("+1205-640-2185");
        driver.findElement(By.name("password")).sendKeys(userPassword);
        driver.findElement(By.name("confirmed_password")).sendKeys(userPassword);

        //Click on 'Create Account'
        driver.findElement(By.name("create_account")).click();

        //Wait until user interface opens
        String logoutLocator = "//*[@id='navigation']//*[contains(@href, 'logout')]";
        wait.until(visibilityOfElementLocated(By.xpath(logoutLocator)));
        System.out.println(
                "User with email " + userMail.toString() +
                        " and password " + userPassword.toString() +
                        " was registered"
        );

        logout();
        loginAsUser();
        logout();
    }

    private void loginAsUser() {
        String loginLocator = "//*[@id='navigation']//button[@name='login']";
        String loginButton = "//button[@name='login']";
        wait.until(presenceOfElementLocated(By.xpath(loginLocator)));
        driver.findElement(By.xpath(loginLocator)).click();
        wait.until(presenceOfElementLocated(By.xpath(loginButton)));
        driver.findElement(By.xpath(loginButton)).click();
        driver.findElement(By.name("email")).sendKeys(userMail);
        driver.findElement(By.name("password")).sendKeys(userPassword);
        driver.findElement(By.xpath(loginLocator)).click();
    }

    private void logout() throws InterruptedException {
        String logoutLocator = "//*[@id='navigation']//*[contains(@href, 'logout')]";
        wait.until(visibilityOfElementLocated(By.xpath(logoutLocator)));
        driver.findElement(By.xpath(logoutLocator)).click();
    }

    private String generateMail(int len) {
        StringBuilder sb = new StringBuilder(len);
        sb.append("mail");
        sb.append(generateRandomString(len));
        sb.append("@mailprovider.tld");

        return sb.toString();
    }

    private String generateRandomString(int len) {
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++)
            sb.append(AB.charAt(rnd.nextInt(AB.length())));

        return sb.toString();
    }

    @After
    public void stop() {
        driver.quit();
        driver = null;
    }
}
