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

public class PersonalAccountTest {
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

        // Авторизация пользователя
        LoginPage loginPage = new LoginPage(driver);
        MainPage mainPage = new MainPage(driver);

        mainPage.clickLoginButton();
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        // Ожидаем завершения авторизации
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.invisibilityOfElementLocated(
                        By.xpath("//button[text()='Войти в аккаунт']")));
    }

    @After
    public void tearDown() {
        // Удаление тестового пользователя
        AuthAPI.deleteUser(accessToken);
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Переход в личный кабинет после авторизации")
    public void testPersonalAccountNavigationAfterLogin() {
        MainPage mainPage = new MainPage(driver);
        PersonalAccountPage personalAccountPage = new PersonalAccountPage(driver);

        // Клик по кнопке "Личный кабинет"
        mainPage.clickPersonalAccountButton();

        // Проверка перехода в личный кабинет
        assertTrue("Должна отображаться страница личного кабинета",
                personalAccountPage.isDisplayed());

        // Дополнительная проверка - имя пользователя должно совпадать
        assertTrue("Имя пользователя должно соответствовать тестовым данным",
                personalAccountPage.getUserName().equals(name));
    }
}
