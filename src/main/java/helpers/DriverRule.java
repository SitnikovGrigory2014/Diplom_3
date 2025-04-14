package helpers;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.rules.ExternalResource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;

public class DriverRule extends ExternalResource {
    private WebDriver driver;

    public void initDriver() {
//        if ("firefox".equalsIgnoreCase(System.getProperty("browser"))) {
//            startUpFireFox();
//        } else {
//            startUpChrome();
//        }
        if ("firefox".equalsIgnoreCase(System.getProperty("browser"))) {
            startUpFireFox();
        } else {
            startUpFireFox();
        }
    }

    @Override
    protected void before() {
        initDriver();
    }

    @Override
    protected void after() {
        driver.quit();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void startUpChrome() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(EnvConfig.IMPLICIT_WAIT));
    }

    public void startUpFireFox() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(EnvConfig.IMPLICIT_WAIT));
    }
}
