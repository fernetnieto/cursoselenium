package ExamenFinal;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class prueba_netflix {

    //https://www.netflix.com/
    WebDriver driver;

    @BeforeTest
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver");
        driver = new ChromeDriver();
        driver.get("https://www.netflix.com/");
    }

    @BeforeMethod
    public void beforeEachTest(){
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void validarTituloTest() {
        Assert.assertEquals(driver.getTitle(), "Netflix Argentina: Ve series online, ve películas online");
    }

    @Test
    public void iniciarSesionPageTest() {

        driver.findElement(By.xpath("//a[@href='/login']")).click();

        Assert.assertNotEquals(driver.getTitle(), "Netflix Argentina: Ve series online, ve películas online");

        boolean h1Exists = false;
        List<WebElement> h1s = driver.findElements(By.tagName("h1"));
        for (WebElement h1 : h1s) {
            if (h1.getText().equals("Inicia sesión")) {
                h1Exists = true;
            }
        }
        Assert.assertTrue(h1Exists);

        boolean facebookTextExists = false;
        List<WebElement> textsInPage = driver.findElements(By.linkText("Iniciar sesión con Facebook"));
        for (WebElement textInPage : textsInPage) {
            if (textInPage.getText().equals("Iniciar sesión con Facebook")) {
                facebookTextExists = true;
            }
        }
        Assert.assertTrue(facebookTextExists);
    }

    @Test
    public void loginToNetflixErrorTest() {

        driver.findElement(By.name("userLoginId")).sendKeys("XXX");
        driver.findElement(By.name("password")).sendKeys("holamundo");
        driver.findElement(By.id("bxid_rememberMe_true")).click();
        driver.findElement(By.xpath("//button[@data-iua='login-submit-button']")).click();

        boolean errorMessageExists = false;
        List<WebElement> errorMessagesInPage = driver.findElements(By.linkText("Contraseña incorrecta."));
        for (WebElement errorMessageInPage : errorMessagesInPage) {
            if (errorMessageInPage.getText().equals("Contraseña incorrecta.")) {
                errorMessageExists = true;
            }
        }
        Assert.assertTrue(errorMessageExists);

        boolean checkboxChecked = false;
        if (driver.findElement(By.id("bxid_rememberMe_true")).isSelected()) {
            checkboxChecked = true;
        }
        Assert.assertTrue(checkboxChecked);
    }

    @Test
    public void fakeEmailTest(){

        driver.navigate().to("https://www.netflix.com/");

        Faker faker=new Faker();
        String email= faker.internet().emailAddress();

        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        String actualURL=driver.getCurrentUrl();
        Assert.assertTrue(driver.getCurrentUrl().contains("signup"));

    }

    @AfterTest
    public void closeDriver() throws InterruptedException {
        Thread.sleep(4000);
        driver.close();
    }
}
