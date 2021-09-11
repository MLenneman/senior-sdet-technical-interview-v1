package Insite360Page.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Insite360Page {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(id = "footer-menu-wrapper")
    private WebElement footerMenuWrapper;

    public Insite360Page(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, 30);
        waitForPageToLoad();
    }

    public void waitForPageToLoad() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("footer-menu-wrapper")));
    }

    public WebElement getFooterMenuWrapperByPartialText(String text) {
        return footerMenuWrapper.findElement(By.partialLinkText(text));
    }

    public WebElement getFooterMenuWrapperByColumnAndRow(int column, int row) {
        return  footerMenuWrapper.findElement(By.xpath("./div["+column+"]/ul/li["+row+"]/a"));
    }
}
