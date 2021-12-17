package com.todomvc;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class AddAndCheckActionsTest {
    WebDriver driver;
    JavascriptExecutor js;

    @BeforeAll
    public static void initialize() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void prepareDriver(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().timeouts().setScriptTimeout(Duration.ofMinutes(2));

        js = (JavascriptExecutor) driver;
    }

    @Test
    public void todoTestCase() throws InterruptedException {
        driver.get("https://todomvc.com");
        choosePlatform("Backbone.js");
        addTodo("Read about selenium");
        addTodo("Write code");
        removeTodo();
        Thread.sleep(1000);

        assertLeft(2);

    }
    @ParameterizedTest
    @ValueSource(strings = {
            "Backbone.js",
            "AngularJS",
            "Dojo",
            "React"
    })
    public void todosTestCase(String platform) {
        driver.get("https://todomvc.com");
        choosePlatform(platform);
        addTodo("Read about selenium");
        addTodo("Write code");
        removeTodo();

        assertLeft(2);

    }

    private void choosePlatform(String platform) {
        WebElement element = driver.findElement(By.linkText(platform));
        element.click();
    }
    private void addTodo(String todo) {
        WebElement element = driver.findElement(By.className("new-todo"));
        element.sendKeys(todo);
        element.sendKeys(Keys.RETURN);
    }
    private void removeTodo() {
        WebElement element = driver.findElement(By.cssSelector("li:nth-child(2) .toggle"));
        element.click();
    }
    private void assertLeft(int expectedLeft) {
        WebElement element = driver.findElement(By.xpath("//footer/*/span | //footer/span"));
        if(expectedLeft == 1 ) {
            String expectedTest = String.format("$d item left", expectedLeft);
            validateInnerText(element,expectedTest);
        } else {
            String expectedTest = String.format("$d items left", expectedLeft);
            validateInnerText(element,expectedTest);
        }
    }

    private void validateInnerText(WebElement element, String expectedTest) {
        ExpectedConditions.textToBePresentInElement(element, expectedTest);
    }

    @AfterEach
    public void quitDriver() throws InterruptedException {
        driver.quit();
    }
}
