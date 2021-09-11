package Insite360Page;

import Insite360Page.PageObjects.GooglePage;
import Insite360Page.PageObjects.Insite360Page;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class Insite360PageTestExample {
    private WebDriver driver;
    private GooglePage google;

    @BeforeTest
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        google = new GooglePage(driver);
    }

    @Test
    public void googleTest() throws InterruptedException {
        google.open();
        google.searchFor("insite360");
        Assert.assertFalse(google.getResults().isEmpty());
        WebElement firstLink = google.getLink(google.getFirstResult());
        Assert.assertEquals(firstLink.getAttribute("href"),"https://www.insite360suite.com/");
        firstLink.click();
        Insite360Page insite360Page = new Insite360Page(driver);
        Assert.assertEquals(insite360Page.getFooterMenuWrapperByColumnAndRow(3,3).getText(),"Contact Us");
        insite360Page.getFooterMenuWrapperByPartialText("Careers").click();

    }

    @AfterTest
    public void tearDown() throws InterruptedException {
        driver.quit();
    }
}
