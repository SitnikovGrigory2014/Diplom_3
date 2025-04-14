import helpers.AuthAPI;
import helpers.DriverRule;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import pages.*;
import helpers.UserGenerator;
import java.time.Duration;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertTrue;

public class LogoutTest {
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
        driver.get("https://stellarburgers.nomoreparties.site");

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
        if (accessToken != null) {
            AuthAPI.deleteUser(accessToken);
        }
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Выход из аккаунта через личный кабинет")
    public void testLogoutFromPersonalAccount() {
        MainPage mainPage = new MainPage(driver);
        LoginPage loginPage = new LoginPage(driver);
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);

        // Авторизация
        mainPage.clickLoginButton();
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        // Ожидание завершения авторизации
        mainPage.waitUntilLoginButtonDisappears();

        // Переход в личный кабинет с ожиданием закрытия модальных окон
        personalAccountPage.waitForModalToClose();
        mainPage.clickPersonalAccountButton();

        personalAccountPage.waitForModalToClose();
        // Выход из аккаунта
        personalAccountPage.clickLogoutButton();

        // Проверка успешного выхода
        assertTrue("Должна отображаться страница входа",
                loginPage.isDisplayed());

    }
}
