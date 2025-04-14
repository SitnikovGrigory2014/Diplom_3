import helpers.AuthAPI;
import helpers.DriverRule;
import helpers.EnvConfig;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoginPage;
import pages.MainPage;
import pages.RegistrationPage;
import helpers.UserGenerator;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertTrue;

public class RegistrationTest {
    @Rule
    public DriverRule driverRule = new DriverRule();

    private WebDriverWait wait;


    @Test
    @DisplayName("Успешная регистрация")
    public void testSuccessfulRegistration() {
        // Генерация тестовых данных
        String email = UserGenerator.generateRandomEmail();
        String password = UserGenerator.generateRandomPassword(8);
        String name = UserGenerator.generateRandomName();
        WebDriver driver = driverRule.getDriver();
        var mainPage = new MainPage(driver);
        var loginPage = new LoginPage(driver);
        var registrationPage = new RegistrationPage(driver);

        mainPage.openMainPage();
        mainPage.waitForLoad();
        // Переход на страницу регистрации
        mainPage.clickLoginButton();
        loginPage.clickRegisterLink();

        // Ожидание загрузки формы регистрации
        registrationPage.waitForRegistrationForm();

        // Заполнение формы
        registrationPage.fillRegistrationForm(name, email, password);

        clickWithRetry(() -> registrationPage.clickRegisterButton());

        // Ожидание перехода на страницу входа
        loginPage.waitForLoginForm();


        assertTrue("После регистрации должна отображаться форма входа",
                loginPage.isDisplayed());
    }

    @Test
    @DisplayName("Ошибка при некорректном пароле")
    public void testRegistrationWithShortPassword() {
        // Генерация тестовых данных
        String email = UserGenerator.generateRandomEmail();
        String password = UserGenerator.generateRandomPassword(5); // Слишком короткий пароль
        String name = UserGenerator.generateRandomName();

//        MainPage mainPage = new MainPage(driver);
//        LoginPage loginPage = new LoginPage(driver);
//        RegistrationPage registrationPage = new RegistrationPage(driver);
        WebDriver driver = driverRule.getDriver();
        var mainPage = new MainPage(driver);
        var loginPage = new LoginPage(driver);
        var registrationPage = new RegistrationPage(driver);
        mainPage.openMainPage();
        mainPage.waitForLoad();
        // Переход на страницу регистрации
        mainPage.clickLoginButton();
        loginPage.clickRegisterLink();

        // Ожидание загрузки формы
        registrationPage.waitForRegistrationForm();

        // Заполнение формы
        registrationPage.fillRegistrationForm(name, email, password);

        // Надежный клик по кнопке регистрации
        clickWithRetry(() -> registrationPage.clickRegisterButton());

        // Проверка ошибки
        assertTrue("Должна отображаться ошибка валидации пароля",
                registrationPage.isPasswordErrorDisplayed());
    }

    private void clickWithRetry(Runnable clickAction) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                clickAction.run();
                return;
            } catch (Exception e) {
                System.out.println("Попытка " + (attempts + 1) + " не удалась: " + e.getMessage());
                wait.until(ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath("//div[contains(@class,'Modal_modal_overlay')]")));
                attempts++;
            }
        }
        throw new RuntimeException("Не удалось выполнить действие после 3 попыток");
    }
}
