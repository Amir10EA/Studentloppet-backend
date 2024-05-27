package com.pvt152.StudentLoppet;

import org.openqa.selenium.JavascriptExecutor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.pvt152.StudentLoppet.model.MidnattsloppRunner;
import com.pvt152.StudentLoppet.repository.MidnattsloppRunnerRepository;

import java.time.Duration;
import java.util.List;

@Service
public class TableScraper {

    @Autowired
    private MidnattsloppRunnerRepository runnerRepository;

    @Scheduled(cron = "0 0 2 * * ?")
    // @Scheduled(cron = "0 6 14 24 5 ?", zone = "Europe/Stockholm")
    public void scrapeAndExtractRunners() {
        String chromeDriverPath = System.getenv("CHROMEDRIVER_PATH");
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--window-size=1920,1200", "--headless");
        WebDriver driver = new ChromeDriver(options);

        try {
            System.out.println("Navigating to the website...");
            driver.get("https://registration.midnattsloppet.com/Web/StartList.aspx?EventGroupId=96&PerCompetition=1");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
            handleConsent(wait);
            initiateSearch(wait);

            String[] categories = { "3156", "3157", "3158", "3155" };

            for (String category : categories) {
                System.out.println("Scraping category with value: " + category);
                selectCategory(driver, wait, category);
                scrapeAllPages(driver, wait);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            System.out.println("Scraping session completed.");
        }
    }

    private void scrapeAllPages(WebDriver driver, WebDriverWait wait) {
        try {
            int lastPage = findLastPageNumber(driver, wait);
            extractRunners(driver.getPageSource());
            for (int i = 2; i <= 10; i++) {
                navigateToNextPage(driver, wait, i);
            }
            clickThreeDotsAndScrape(driver, wait, "//a[contains(text(), '...')]");
            for (int currentPage = 11; currentPage <= lastPage; currentPage++) {
                if (currentPage % 10 == 1 && currentPage != 11) {
                    clickThreeDotsAndScrape(driver, wait, "//a[contains(text(), '...')][2]");
                }
                navigateToNextPage(driver, wait, currentPage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int findLastPageNumber(WebDriver driver, WebDriverWait wait) {
        try {
            System.out.println("Navigating to the last page...");

            WebElement lastPageButton = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), '>>')]")));
            lastPageButton.click();
            wait.until((WebDriver d) -> ((JavascriptExecutor) d).executeScript("return document.readyState")
                    .equals("complete"));

            WebElement paginationElement = driver.findElement(By.id("ctl00_ContentPlaceHolder1_DataPager1"));
            List<WebElement> pageLinks = paginationElement
                    .findElements(By.xpath("//a[contains(@class, 'pagerDB-next active')]"));

            int lastPageNumber = Integer.parseInt(pageLinks.get(pageLinks.size() - 1).getText());
            System.out.println("Navigating back to the first page...");
            WebElement firstPageButton = wait
                    .until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), '<<')]")));
            firstPageButton.click();
            wait.until((WebDriver d) -> ((JavascriptExecutor) d).executeScript("return document.readyState")
                    .equals("complete"));

            System.out.println("Last page number is: " + lastPageNumber);
            return lastPageNumber;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to determine the last page number.");
            return -1;
        }
    }

    private void navigateToNextPage(WebDriver driver, WebDriverWait wait, int pageNumber) {
        try {
            System.out.println("Navigating to page " + pageNumber + "...");
            WebElement nextPageButton = wait.until(ExpectedConditions
                    .elementToBeClickable(By.xpath("//a[contains(text(), '" + pageNumber + "')]")));
            nextPageButton.click();
            wait.until((WebDriver d) -> ((JavascriptExecutor) d).executeScript("return document.readyState")
                    .equals("complete"));
            extractRunners(driver.getPageSource());
        } catch (Exception e) {
            System.out.println("Failed to navigate to page " + pageNumber);
            e.printStackTrace();
        }
    }

    private void clickThreeDotsAndScrape(WebDriver driver, WebDriverWait wait, String xpath) {
        try {
            System.out.println("Clicking three dots button...");
            WebElement threeDotsButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
            threeDotsButton.click();
            wait.until((WebDriver d) -> ((JavascriptExecutor) d).executeScript("return document.readyState")
                    .equals("complete"));
            extractRunners(driver.getPageSource());
        } catch (Exception e) {
            System.out.println("Failed to click three dots button.");
            e.printStackTrace();
        }
    }

    private void handleConsent(WebDriverWait wait) {
        try {
            System.out.println("Handling consent...");
            WebElement allowAllButton = wait.until(ExpectedConditions
                    .elementToBeClickable(By.id("CybotCookiebotDialogBodyLevelButtonLevelOptinAllowAll")));
            allowAllButton.click();
        } catch (Exception e) {
            System.out.println("Cookie consent not found or not clickable.");
        }
    }

    private void initiateSearch(WebDriverWait wait) {
        try {
            System.out.println("Initiating search...");
            WebElement competitionDropdown = wait
                    .until(ExpectedConditions.elementToBeClickable(By.id("ctl00_ContentPlaceHolder1_ddlCompetition")));
            competitionDropdown.sendKeys("Midnattsloppet Stockholm 2024");
            WebElement searchButton = wait
                    .until(ExpectedConditions.elementToBeClickable(By.id("ctl00_ContentPlaceHolder1_btnSubmit")));
            searchButton.click();
            wait.until((WebDriver d) -> ((JavascriptExecutor) d).executeScript("return document.readyState")
                    .equals("complete"));
        } catch (Exception e) {
            System.out.println("Failed to initiate search.");
            e.printStackTrace();
        }
    }

    private void selectCategory(WebDriver driver, WebDriverWait wait, String categoryValue) {
        try {
            System.out.println("Selecting category with value: " + categoryValue);
            WebElement categoryDropdown = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("ctl00_ContentPlaceHolder1_ddlCompetitionClassId")));
            categoryDropdown.click();
            WebElement option = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//option[@value='" + categoryValue + "']")));
            option.click();
            WebElement searchButton = wait
                    .until(ExpectedConditions.elementToBeClickable(By.id("ctl00_ContentPlaceHolder1_btnSubmit")));
            searchButton.click();
            wait.until((WebDriver d) -> ((JavascriptExecutor) d).executeScript("return document.readyState")
                    .equals("complete"));
        } catch (Exception e) {
            System.out.println("Failed to select category " + categoryValue);
            e.printStackTrace();
        }
    }

    private void extractRunners(String htmlContent) {
        System.out.println("Extracting runners...");
        Document doc = Jsoup.parse(htmlContent);
        Elements rows = doc.select("table tr");
        for (Element row : rows) {
            Elements cells = row.select("td");
            if (cells.size() > 5) {
                String startNumber = cells.get(0).text();
                String name = cells.get(1).text() + " " + cells.get(2).text();
                int yearBorn = 0;
                try {
                    yearBorn = Integer.parseInt(cells.get(3).text());
                } catch (NumberFormatException e) {
                    System.out.println("Skipping row due to invalid year: " + cells.get(3).text());
                    continue;
                }
                String clubOrCityOrCompany = cells.get(4).text();
                String startGroup = cells.get(5).text();
                String time = cells.size() > 6 ? cells.get(6).text() : "";
                String startGroupAndTime = startGroup + " " + time;

                MidnattsloppRunner runner = new MidnattsloppRunner(startNumber, name, yearBorn, clubOrCityOrCompany,
                        startGroupAndTime);
                if (!runnerRepository.existsById(startNumber)) {
                    runnerRepository.save(runner);
                }
            }
        }
    }
}