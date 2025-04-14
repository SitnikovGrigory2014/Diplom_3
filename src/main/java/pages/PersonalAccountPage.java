package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class PersonalAccountPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By profileHeader = By.xpath("//a[text()='Профиль']");
    private final By userName = By.xpath("//input[@name='Name']");
    private final By constructorButton = By.xpath("//p[text()='Конструктор']");
    private final By logo = By.xpath("//div[@class='AppHeader_header__logo__2D0X2']");
    private final By logoutButton = By.xpath("//button[text()='Выход']");
    private final By modalOverlay = By.xpath("//div[contains(@class,'Modal_modal_overlay')]");

    public PersonalAccountPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public boolean isDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(profileHeader))
                    .isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(userName))
                .getAttribute("value");
    }

    public void clickConstructorButton() {
        wait.until(ExpectedConditions.elementToBeClickable(constructorButton)).click();
    }

    public void clickLogo() {
        wait.until(ExpectedConditions.elementToBeClickable(logo)).click();
    }

    public void clickLogoutButton() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
        //driver.findElement(logoutButton).click();
    }

    public void waitForModalToClose() {
        new WebDriverWait(driver, Duration.ofSeconds(30))
                .until(ExpectedConditions.invisibilityOfElementLocated(modalOverlay));
    }
}
