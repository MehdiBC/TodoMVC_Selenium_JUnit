package com.todomvc;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class AddAndCheckTodosTest {

    private WebDriver driver;

    @BeforeAll
    public static void setupDriverBinary() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void configureDriver(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void BackboneJsTestCase() {
        technologiesTestCase("Backbone.js");
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Backbone.js",
            "AngularJS",
            "KnockoutJS",
            "Dojo",
            "Knockback.js",
            "CanJS",
            "Polymer",
            "React"
    })
    public void technologiesTestCase(String technology) {
        driver.navigate().to("https://todomvc.com");
        selectTechnology(technology);
        String[] todos = new String[]{ "Todo1", "Todo2", "Todo3", "Todo4" };
        for(String todo: todos){
            addTodo(todo);
        }
        checkTodo();
        Assertions.assertTrue(expectNumberOfTodosChecked(todos.length-1));
    }

    private void selectTechnology(String technology) {
        driver.findElement(By.linkText(technology)).click();
    }

    private void addTodo(String todo) {
        WebElement todoInput = driver.findElement(By.cssSelector("input.new-todo"));
        todoInput.sendKeys(todo);
        todoInput.sendKeys(Keys.ENTER);
    }

    private void checkTodo() {
        driver.findElement(By.cssSelector("li:first-child .toggle")).click();
    }

    private boolean expectNumberOfTodosChecked(int expectedItemsLeft) {
        WebElement webElement = driver.findElement(By.xpath("//footer/*/span | //footer/span"));
        String expectedText = (expectedItemsLeft == 1)? expectedItemsLeft + " item left" : expectedItemsLeft + " items left";
        return webElement.getText().equals(expectedText);
    }

    @AfterEach
    public void closeDriverSession() {
        driver.quit();
    }
}
