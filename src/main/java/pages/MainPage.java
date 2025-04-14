package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import helpers.EnvConfig;

import java.time.Duration;

public class MainPage {
    private WebDriver driver;
    private final WebDriverWait wait;

    //Кнопка "Войти в аккаунт"
    private final By loginButton = By.xpath("//button[text()='Войти в аккаунт']");

    //Кнопка "Личный Кабинет"
    private final By personalAccountButton = By.xpath("//a[@href='/account']");

    // Заголовок "Соберите бургер"
    private final By header = By.xpath("//h1[text()='Соберите бургер']");


    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.IMPLICIT_WAIT));
    }

    public void clickLoginButton() {
        //wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        driver.findElement(loginButton).click();
    }

    public void clickPersonalAccountButton() {
        wait.until(ExpectedConditions.elementToBeClickable(personalAccountButton)).click();
    }

    public void waitUntilLoginButtonDisappears() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(loginButton));
    }

    public void waitForLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.IMPLICIT_WAIT))
                .until(ExpectedConditions.visibilityOfElementLocated(header));
    }


    public boolean isAuthorized() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.IMPLICIT_WAIT))
                    .until(ExpectedConditions.invisibilityOfElementLocated(loginButton));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isMainPageLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(header))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void openMainPage() {
        driver.get(EnvConfig.BASE_URL);
    }
}
