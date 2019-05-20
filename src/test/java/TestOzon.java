import io.qameta.allure.Step;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestOzon {
    WebDriver driver;
    WebDriverWait wait;

    @Before
    public void setUp(){
        ChromeOptions option = new ChromeOptions();
        option.addArguments("--disable-notifications");
        driver = new ChromeDriver(option);
        wait = new WebDriverWait(driver,10);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(25,TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Step("1.Перейдите на сервис http://www.ozon.ru/")
    public void step1(){
        driver.get("https://www.ozon.ru/");
    }

    @Step("2.Выполните авторизацию на сервисе с ранее созданным логином и паролем")
    public void step2(){
        login();
    }

    @Step("3.Выполните поиск по «iPhone» ")
    public void step3(){
        driver.navigate().refresh();
        driver.findElement(By.cssSelector("[data-test-id = header-search-input]")).click();
        driver.findElement(By.cssSelector("[data-test-id = header-search-input]")).sendKeys("iPhone");
        driver.findElement(By.cssSelector("[data-test-id = header-search-go]")).click();
    }

    @Step("4.Из результатов поиска добавьте в корзину все четные товары")
    public void step4() throws InterruptedException {
        List<WebElement> a = driver.findElements(By.className("tile"));
        int i =1;
        while(i < 2 ) { //i<a.size()
            a.get(i).findElement(By.cssSelector("[data-test-id = tile-buy-button]")).click();
            i = i + 2;
            Thread.sleep(2000);
            JavascriptExecutor jse = (JavascriptExecutor)driver;
            jse.executeScript("window.scrollBy(0,-250)", "");
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("scroll-button"))));
            driver.findElement(By.className("scroll-button")).click();
        }
    }

    @Step("5.Перейдите в корзину, убедитесь, что все добавленные ранее товары находятся в корзине")
    public void step5(){
        driver.navigate().refresh();
        driver.findElement(By.cssSelector("[data-test-id = header-cart]")).click();
        Assert.assertEquals("1" ,driver.findElement(By.className("tab-count")).getText());
    }

    @Step("6.Удалите все товары из корзин")
    public void step6(){
        driver.findElement(By.cssSelector("[data-test-id = cart-delete-selected-btn]")).click();
        driver.findElement(By.cssSelector("[data-test-id = checkcart-confirm-modal-confirm-button]")).click();
    }

    @Step("7.Разлогиньтесь с сервиса")
    public void step7(){
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.className("my-ozon-menu"))).perform();
        driver.findElement(By.cssSelector("[data-test-id = header-my-ozon-logout]")).click();
    }

    @Step("8.Выполните авторизацию на сервисе")
    public void step8(){
        driver.navigate().refresh();
        login();
    }

    @Step("9.Проверьте, что корзина не содержит никаких товаров")
    public void step9(){
        Assert.assertEquals("Корзина пуста", driver.findElement(By.className("cart-title")).getText());
    }

    @Test
    public void myfirsttest() throws InterruptedException {
        step1();
        step2();
        step3();
        step4();
        step5();
        step6();
        step7();
        step8();
        step9();
    }

    @After
    public void tearDown(){
        driver.quit();
    }

    public void login(){
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(By.className("my-ozon-menu"))).perform();
        driver.findElement(By.cssSelector("[data-test-id = header-my-ozon-login]")).click();
        if(driver.findElement(By.className("link-base")).getText().equals("Войти по почте")){
            driver.findElement(By.className("link-base")).click();
        }
        driver.findElement(By.cssSelector("[data-test-id = emailField]")).sendKeys("kamilchik182@mail.ru");
        driver.findElement(By.cssSelector("[type = password]")).sendKeys("Kk7309197");
        driver.findElement(By.cssSelector("[data-test-id = loginFormSubmitButton]")).click();
    }

}