package ExamenFinalMarzo;

import ExamenFinalFebrero.dataProvider;
import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class prueba_mailchimp {

    public SoftAssert softAssert = new SoftAssert();

    //https://login.mailchimp.com/

    WebDriver driver;
    WebDriverWait wait=new WebDriverWait(driver,2);

    @BeforeMethod
    public void beforeEachTest() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.navigate().to("https://login.mailchimp.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler")));
        driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        Thread.sleep(2000);
    }

    @Test(priority=0)
    public void validarTituloTest() {
        Assert.assertEquals(driver.getTitle(), "Login | Mailchimp");
    }

    @Test(priority=1)
    public void iniciarSesionPageTest() {
        List<WebElement> mailchimpTexts = driver.findElements(By.xpath("//*[contains(text(),'Iniciar sesión')]"));
        boolean mailchimpTextExists=false;
        if (!mailchimpTexts.isEmpty()) {
            mailchimpTextExists = true;
        }
        Assert.assertTrue(mailchimpTextExists);

        List<WebElement> mailchimpTexts2 = driver.findElements(By.xpath("//*[contains(text(),'Need a Mailchimp account?')]"));
        boolean mailchimpTextExists2=false;
        if (!mailchimpTexts2.isEmpty()) {
            mailchimpTextExists2 = true;
        }
        Assert.assertTrue(mailchimpTextExists2);
    }

    @Test(priority=2)
    public void loginErrorTest() {
        driver.findElement(By.id("username")).sendKeys("XXXXX@gmail.com");
        driver.findElement(By.xpath("//button[@value='login in']")).click();

        boolean passErrorTextExists=false;
        List<WebElement> passErrorTexts = driver.findElements(By.partialLinkText("Looks like you forgot your password there"));
        if (!passErrorTexts.isEmpty()) {
            passErrorTextExists = true;
        }
        softAssert.assertTrue(passErrorTextExists);

        boolean checkboxChecked=false;
        if (!driver.findElement(By.name("stay-signed-in")).isSelected())
        {
            checkboxChecked=true;
        }
        softAssert.assertTrue(checkboxChecked);
    }

    @Test(priority=3)
    public void fakeEmailTest() throws InterruptedException {

        driver.navigate().to("https://login.mailchimp.com/signup/");

        try {
            wait.until(ExpectedConditions.elementToBeClickable(By.id("onetrust-accept-btn-handler")));
            driver.findElement(By.id("onetrust-accept-btn-handler")).click();
        } catch (ElementNotVisibleException ex){
            System.out.println("No existen cookies para aceptar en esta pantalla.");
        } catch (Exception ex){
            System.out.println("No existe elemento.");
        }
        Thread.sleep(2000);

        Faker faker=new Faker();
        String email= faker.internet().emailAddress();
        driver.findElement(By.id("email")).sendKeys(email);

        Assert.assertTrue(driver.getCurrentUrl().contains("signup"));
    }

    @Test(priority=4, dataProvider="datos", dataProviderClass= dataProvider.class)
    public void dataProviderEmailTest(String email){
        driver.findElement(By.id("username")).sendKeys(email);
        driver.findElement(By.id("password")).sendKeys("holamundo");
        driver.findElement(By.xpath("//button[@value='login in']")).click();

        List<WebElement> errorLoginTexts = driver.findElements(By.xpath("//*[contains(text(),'Can we help you recover your username?')]"));
        boolean errorLoginTextExists=false;
        if (!errorLoginTexts.isEmpty()) {
            errorLoginTextExists = true;
        }
        Assert.assertTrue(errorLoginTextExists);
    }

    @AfterMethod
    public void closeDriver() throws InterruptedException {
        Thread.sleep(4000);
        driver.close();
    }
}
