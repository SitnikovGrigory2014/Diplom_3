import helpers.AuthAPI;
import helpers.DriverRule;
import helpers.EnvConfig;
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

public class NavigationFromPersonalAccountTest {
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
    @DisplayName("Переход из личного кабинета в конструктор по клику на 'Конструктор'")
    public void testNavigateToConstructorViaConstructorButton() {
        MainPage mainPage = new MainPage(driver);
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);
        // Авторизация
        LoginPage loginPage = new LoginPage(driver);

        mainPage.clickLoginButton();
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        mainPage.waitUntilLoginButtonDisappears();

        //mainPage.clickPersonalAccountButton();
        personalAccountPage.waitForModalToClose();

        personalAccountPage.clickConstructorButton();
        assertTrue("Должна отображаться главная страница",
                mainPage.isMainPageLoaded());
    }

    @Test
    @DisplayName("Переход из личного кабинета в конструктор по клику на логотип")
    public void testNavigateToConstructorViaLogo() {
        MainPage mainPage = new MainPage(driver);
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);
        // Авторизация
        LoginPage loginPage = new LoginPage(driver);

        mainPage.clickLoginButton();
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        mainPage.waitUntilLoginButtonDisappears();
        //mainPage.clickPersonalAccountButton();
        personalAccountPage.waitForModalToClose();

        personalAccountPage.clickLogo();
        assertTrue("Должна отображаться главная страница",
                mainPage.isMainPageLoaded());
    }
}
