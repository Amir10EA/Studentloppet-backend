package com.pvt152.StudentLoppet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CountdownExtractor {

    public int extractCountdownDays() {
        String chromeDriverPath = System.getenv("CHROMEDRIVER_PATH");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--window-size=1920,1200", "--headless");
        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("https://midnattsloppet.com/");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

            // Wait for the countdown element to be visible and contain text
            WebElement countdownElement = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.cssSelector(".countdown__unit.js-countdown-days")));
            wait.until(ExpectedConditions
                    .not(ExpectedConditions.textToBe(By.cssSelector(".countdown__unit.js-countdown-days"), "")));

            // Extract the text from the countdown element
            String countdownText = countdownElement.getText();
            System.out.println("Countdown Text: " + countdownText);

            try {
                return Integer.parseInt(countdownText);
            } catch (NumberFormatException e) {
                System.out.println("Failed to parse countdown text to integer: " + countdownText);
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Indicate an error
        } finally {
            driver.quit();
        }
    }
}