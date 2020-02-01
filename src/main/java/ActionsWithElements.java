import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
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

    @Test
    public void addProduct() throws InterruptedException {
        String path = "src/test/resources";

        File file = new File(path);
        String imagePath = file.getAbsolutePath() + "\\image.png";

        loginAsAdmin();
        //Open catalog
        WebElement element = wait.until(visibilityOfElementLocated(By.xpath("//a[contains(@href, 'catalog')]")));
        element.click();

        //Click on Add product tab
        element = wait.until((WebDriver d) -> d.findElement(By.xpath("//a[contains(@href, 'edit_product') and @class = 'button']")));
        element.click();

        wait.until((WebDriver d) -> d.findElement(By.name("code")));

        String codeValue = generateRandomString(4);
        driver.findElement(By.xpath("//input[@type = 'radio' and @value = 1]")).click();
        driver.findElement(By.name("name[en]")).click();
        driver.findElement(By.name("name[en]")).sendKeys(codeValue);
        driver.findElement(By.name("code")).click();
        driver.findElement(By.name("code")).sendKeys(codeValue);
        driver.findElement(By.xpath("//input[@type = 'file']")).sendKeys(imagePath);
        driver.findElement(By.xpath("//a[text() = 'Information']")).click();

        element = wait.until((WebDriver d) -> d.findElement(By.name("manufacturer_id")));
        element.click();

        //Select first manufacture from the list of manufactures
        Select manufacturer = new Select(driver.findElement(By.name("manufacturer_id")));
        manufacturer.selectByIndex(1);

        //Select first supplier from the list of Suppliers
        Select supplier = new Select(driver.findElement(By.name("supplier_id")));
        supplier.selectByIndex(2);

        //Fill fields with descriptions
        String lazyField = generateRandomString(10);
        driver.findElement(By.xpath("//input[contains(@name, 'short_description')]")).sendKeys(lazyField);
        driver.findElement(By.xpath("//*[@class ='trumbowyg-editor']")).sendKeys(lazyField);
        driver.findElement(By.xpath("//input[contains(@name, 'head_title')]")).sendKeys(lazyField);
        driver.findElement(By.xpath("//input[contains(@name, 'meta_description')]")).sendKeys(lazyField);

        driver.findElement(By.xpath("//a[@href='#tab-prices']")).click();
        wait.until(visibilityOfElementLocated(By.name("save")));
        driver.findElement(By.name("purchase_price")).sendKeys("5");

        //Purchase
        driver.findElement(By.name("purchase_price_currency_code")).click();
        driver.findElement(By.xpath("//option[@value='USD']")).click();

        //Price and Tax
        driver.findElement(By.name("prices[USD]")).sendKeys("10");
        driver.findElement(By.name("prices[EUR]")).sendKeys("10");

        //Click on Save button
        driver.findElement(By.name("save")).click();
    }

    private void loginAsAdmin() {
        driver.get("http://localhost/litecart/admin/");
        driver.findElement(By.name("username")).sendKeys("admin");
        driver.findElement(By.name("password")).sendKeys("admin");
        driver.findElement(By.name("login")).click();
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
