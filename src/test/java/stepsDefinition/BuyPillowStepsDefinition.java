package stepsDefinition;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;

public class BuyPillowStepsDefinition {

    private WebDriver driver;
    private WebElement element, button, iframe, iframe2, popup, tokenForm;
    private WebDriverWait wait;

    private String[] field = {"Name","Email","Phone","City","Address","Postal Code"};
    private String[] fill = {"Enrico","aenrico@xxx.com","+6287700000000","Jakarta","Jl. Kemang No. 20","141414"};
    private String totalOrder;

    @Given("^Open \"([^\"]*)\" browser and start application$")
    public void openBrowserAndStartApplication(String browser) {
        if (browser.equals("chrome")) {
            System.setProperty("webdriver.chrome.driver", "/Users/agungenrico/Documents/practice/midtrans-automation-credits/chromedriver");
            driver = new ChromeDriver();
        } else {
            System.setProperty("webdriver.gecko.driver", "/Users/agungenrico/Documents/practice/midtrans-automation-credits/geckodriver");
            driver = new FirefoxDriver();
        }

        driver.get("https://demo.midtrans.com/");
        driver.manage().window().maximize();

        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(webDriver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete"));
    }

    @When("^I click buy now button$")
    public void iClickBuyNowButton() {
        button = driver.findElement(By.xpath("//*[@class='btn buy']"));
        button.click();
    }

    @And("^I should see Shopping Cart details$")
    public void iShouldSeeShoppingCartDetails() {
        element = driver.findElement(By.xpath("//span[contains(.,'Shopping Cart ')]"));
        Assert.assertTrue("Cart Screen is not displayed!", element.isDisplayed());
    }

    @When("^I input the customer details$")
    public String iInputTheCustomerDetails() {
        for (int i = 0; i < 6; i++) {
            if (i == 4) {
                element = driver.findElement(By.xpath("//td[contains(text(),'" +field[i]+ "')]/following-sibling::td/textarea"));
            } else {
                element = driver.findElement(By.xpath("//td[contains(text(),'" +field[i]+ "')]/following-sibling::td/input"));
            }
            element.clear();
            element.sendKeys(fill[i]);
        }
        totalOrder = element.findElement(By.xpath("//td[@class='amount']")).getText();
        return totalOrder;
    }

    @And("^I press checkout$")
    public void iPressCheckout() {
        button = driver.findElement(By.xpath("//*[@class='cart-checkout']"));
        button.click();
    }


    @Then("^I should see order summary popup$")
    public void iShouldSeeOrderSummaryPopup() {
        iframe = driver.findElement(By.id("snap-midtrans"));
        driver.switchTo().frame(iframe);
        popup = driver.findElement(By.id("app"));

        wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[text()='Order Summary']")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='text-amount-amount']")));

        Assert.assertEquals(popup.findElement(By.xpath("//span[@class='text-amount-amount']")).getText(),totalOrder);
    }

    @When("^I click continue button$")
    public void iClickContinueButton() {
        popup.findElement(By.xpath("//a[@class='button-main-content']")).click();
    }

    @And("^I picked Credit Card as payment method$")
    public void iPickedCreditCardAsPaymentMethod() {
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[text()='Credit/Debit Card']")));
        popup.findElement(By.xpath("//div[text()='Credit/Debit Card']")).click();
    }

    @And("^I input card number$")
    public void iInputCardNumber(DataTable cardNumber)  {
        Map<String, String> number = cardNumber.asMap(String.class, String.class);
        element = popup.findElement(By.xpath("//input[@name='cardnumber']"));
        element.sendKeys(number.get("cardNumber"));
    }

    @And("^I input expiry date and cvv$")
    public void iInputExpiryDateAndCvv() {
        String[] field = {"Expiry date","CVV"};
        String[] fill = {"12/22","123"};
        for (int i = 0; i < 2; i++) {
            element = popup.findElement(By.xpath("//label[text()='" +field[i]+ "']/preceding-sibling::input"));
            element.sendKeys(fill[i]);
        }
    }

    @And("^I am on input token form$")
    public void iAmOnInputTokenForm() {
        wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='page-container scroll']/iframe")));
        iframe2 = driver.findElement(By.xpath("//div[@class='page-container scroll']/iframe"));

        driver.switchTo().frame(iframe2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("acsForm")));
        tokenForm = driver.findElement(By.id("acsForm"));
    }

    @And("^I input BANK OTP$")
    public void iInputBANKOTP() {
        tokenForm.findElement(By.xpath("//input[@type='password']")).sendKeys("112233");
    }

    @And("^I press OK$")
    public void iPressOK() {
        element = tokenForm.findElement(By.xpath("//button[@type='submit']"));
        element.click();
    }

    private void redirectToHomePage() {
        wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Thank you for your purchase.']")));
    }

    @And("^I click on pay now button$")
    public void iClickOnPayNowButton() {
        popup.findElement(By.xpath("//a[@class='button-main-content']")).click();
    }


    @Then("^I should see transaction status$")
    public void iShouldSeeTransaction(DataTable status) {
        Map<String, String> expected = status.asMap(String.class, String.class);
        wait = new WebDriverWait(driver, 10);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("snap-midtrans")));
        iframe = driver.findElement(By.id("snap-midtrans"));
        driver.switchTo().frame(iframe);
        popup = driver.findElement(By.id("app"));

        if (expected.get("status").equals("success")) {
            popup.findElement(By.xpath("//div[text()='Transaction successful']")).isDisplayed();
            redirectToHomePage();
        } else {
            popup.findElement(By.xpath("//span[text()='Transaction failed']")).isDisplayed();
            popup.findElement(By.xpath("//span[text()='Your card got declined by the bank']")).isDisplayed();
        }
    }

    @Then("^I close the browser$")
    public void iCloseTheBrowser() {
        driver.close();
    }

    @Then("^I get the right shipping details$")
    public void iGetTheRightShippingDetails() {
        String[] detailValue = new String[4];
        String[] details = {"Name", "Phone number", "Email", "Address"};
        popup.findElement(By.xpath("//span[text()='shipping details']")).click();
        for (int i = 0; i < 4; i++) {
            String detail = popup.findElement(By.xpath("//div[@class='text-body-title' and text()='" +details[i]+ "']/following-sibling::*[@class='text-body']")).getText();
            detailValue[i] = detail;
        }
        Assert.assertEquals(detailValue[0],fill[0]);
        Assert.assertEquals(detailValue[1],fill[2]);
        Assert.assertEquals(detailValue[2],fill[1]);
        Assert.assertThat(detailValue[3], StringContains.containsString(fill[4]));
    }

    @Then("^I get the right amount to pay$")
    public void iGetTheRightAmoutToPay() {
        String expectedPurchase;
        String promotion;
        WebElement promo = popup.findElement(By.xpath("//label[text()='Potongan 10% - Demo Promo Engine']/span"));
        String actualPurchase = popup.findElement(By.xpath("//span[@class='text-amount-amount']")).getText().replace(",","");
        if (promo.isDisplayed()) {
            promotion = promo.getText().substring(5).replace(",","");
            int promoAmount = Integer.parseInt(promotion);
            int totalPurchase = Integer.parseInt(totalOrder.replace(",","")) - promoAmount;
            expectedPurchase = Integer.toString(totalPurchase);
            Assert.assertEquals(expectedPurchase,actualPurchase);
        } else {
            Assert.assertEquals(totalOrder,actualPurchase);
        }
    }
}
