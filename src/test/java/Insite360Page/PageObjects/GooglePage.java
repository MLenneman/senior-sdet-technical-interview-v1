package Insite360Page.PageObjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class GooglePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(name = "q")
    private WebElement searchBox;

    @FindBy(xpath = "//*[@id=\"rso\"]/div")
    private List<WebElement> searchResults;//#rso > div

    public GooglePage(final WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.wait = new WebDriverWait(driver, 30);
    }

    public void open() {
        this.driver.get("https://www.google.com");
    }

    public void searchFor(String text) throws InterruptedException {
        this.searchBox.sendKeys(text);
        this.searchBox.sendKeys(Keys.RETURN);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("rso")));
    }

    public WebElement getFirstResult(){
        return  getResults().get(0);
    }
    public WebElement getLink(WebElement webElement) {
        WebElement we = webElement.findElement(By.xpath("./div/div/div/div/div/div[1]/a"));
        System.out.println(we.getText());
        return we;
    }
    public List<WebElement> getResults() {
        return this.searchResults;
    }

}
