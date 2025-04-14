package pages;

import helpers.EnvConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegistrationPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Заголовок "Регистрация"
    private final By header = By.xpath("//h2[text()='Регистрация']");

    // Поле "Имя"
    private final By nameField = By.xpath("//label[text()='Имя']/following-sibling::input");

    // Поле "Email"
    private final By emailField = By.xpath("//label[text()='Email']/following-sibling::input");

    // Поле "Пароль"
    private final By passwordField = By.name("Пароль");

    // Кнопка "Зарегистрироваться"
    private final By registerButton = By.xpath("//button[text()='Зарегистрироваться']");

    // Ошибка "Некорректный пароль"
    private final By passwordError = By.xpath("//p[text()='Некорректный пароль']");

    // Ссылка "Войти"
    private final By loginLink = By.className("Auth_link__1fOlj");

    private final By registrationHeader = By.xpath("//h2[text()='Регистрация']");

    private final By modalOverlay = By.xpath("//div[contains(@class,'Modal_modal_overlay')]");

    public RegistrationPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    public void enterName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void enterEmail(String email) {
        driver.findElement(emailField).sendKeys(email);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickRegisterButton() {
        waitForModalToClose();
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        try {
            button.click();
        } catch (Exception e) {
            waitForModalToClose();
            button.click();
        }
    }

    public void waitForModalToClose() {
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(modalOverlay));
            // Дополнительная пауза после закрытия модального окна
            Thread.sleep(500);
        } catch (Exception e) {
            System.out.println("Модальное окно уже закрыто или отсутствует");
        }
    }

    public boolean isDisplayed() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.IMPLICIT_WAIT))
                .until(ExpectedConditions.visibilityOfElementLocated(header));
        return driver.findElement(header).isDisplayed();
    }

    public boolean isPasswordErrorDisplayed() {
        new WebDriverWait(driver, Duration.ofSeconds(EnvConfig.IMPLICIT_WAIT))
                .until(ExpectedConditions.visibilityOfElementLocated(passwordError));
        return driver.findElement(passwordError).isDisplayed();
    }

    public void clickLoginLink() {
        driver.findElement(loginLink).click();
    }

    public void waitForRegistrationForm() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(registrationHeader));
    }

    public void fillRegistrationForm(String name, String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameField)).sendKeys(name);
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
    }



}
