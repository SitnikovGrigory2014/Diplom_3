package pages;

import helpers.EnvConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Заголовок "Вход"
    private final By header = By.xpath("//h2[text()='Вход']");

    // Ссылка "Зарегистрироваться"
    private final By registerLink = By.xpath("//a[text()='Зарегистрироваться']");

    // Поле "Email"
    private final By emailField = By.name("name");

    // Поле "Пароль"
    private final By passwordField = By.name("Пароль");

    // Кнопка "Войти"
    private final By loginButton = By.xpath("//button[text()='Войти']");

    //Кнопка Восстановить пароль
    private final By forgotPasswordLink = By.xpath("//a[text()='Восстановить пароль']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void clickRegisterLink() {
        //wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
        driver.findElement(registerLink).click();
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
    }

    public void clickLoginButton() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
    }

    public boolean isDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(header)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickForgotPasswordLink() {
        wait.until(ExpectedConditions.elementToBeClickable(forgotPasswordLink)).click();
    }

    public void waitForLoginForm() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(header));
    }
}
