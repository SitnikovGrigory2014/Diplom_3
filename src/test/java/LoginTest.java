import helpers.AuthAPI;
import helpers.DriverRule;
import helpers.EnvConfig;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import pages.*;
import helpers.UserGenerator;

import static org.junit.Assert.assertTrue;
public class LoginTest {
    @Rule
    public DriverRule driverRule = new DriverRule();

    private WebDriver driver;
    private String email;
    private String password;
    private String name;
    private String accessToken;

    @Before
    public void setUp() {
        driver = driverRule.getDriver();
        driver.get(EnvConfig.BASE_URL);

        // Генерация тестовых данных
        email = UserGenerator.generateRandomEmail();
        password = UserGenerator.generateRandomPassword(8);
        name = UserGenerator.generateRandomName();

        // Создание пользователя через API
        AuthAPI.registerUser(email, password, name)
                .then()
                .statusCode(200);

        // Получаем токен для последующего удаления
        accessToken = AuthAPI.getAccessToken(email, password);
    }

    @After
    public void tearDown() {
        AuthAPI.deleteUser(accessToken);
        if (driver != null) {
            driver.quit();
        }
    }

    private void performLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();
    }

    @Test
    @DisplayName("Вход по кнопке 'Войти в аккаунт' на главной")
    public void testLoginFromMainPage() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickLoginButton();
        performLogin();

        assertTrue("Пользователь должен быть авторизован",
                mainPage.isAuthorized());
    }

    @Test
    @DisplayName("Вход через кнопку 'Личный кабинет'")
    public void testLoginFromPersonalAccountButton() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickPersonalAccountButton();
        performLogin();

        assertTrue("Пользователь должен быть авторизован",
                mainPage.isAuthorized());
    }

    @Test
    @DisplayName("Вход через кнопку в форме регистрации")
    public void testLoginFromRegistrationPage() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickLoginButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickRegisterLink();

        RegistrationPage registrationPage = new RegistrationPage(driver);
        registrationPage.clickLoginLink();

        performLogin();

        assertTrue("Пользователь должен быть авторизован",
                new MainPage(driver).isAuthorized());
    }

    @Test
    @DisplayName("Вход через кнопку в форме восстановления пароля")
    public void testLoginFromPasswordRecoveryPage() {
        MainPage mainPage = new MainPage(driver);
        mainPage.clickLoginButton();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.clickForgotPasswordLink();

        ForgotPasswordPage forgotPasswordPage = new ForgotPasswordPage(driver);
        forgotPasswordPage.clickLoginLink();

        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        assertTrue("Пользователь должен быть авторизован",
                new MainPage(driver).isAuthorized());
    }
}
