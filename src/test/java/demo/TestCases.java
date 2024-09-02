package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;
    WebDriverWait wait;
    Wrappers wp;

    /*
     * TODO: Write your tests here with testng @Test annotation.
     * Follow `testCase01` `testCase02`... format or what is provided in
     * instructions
     */

    @Test
    public void testCase01() {
        System.out.println("testCase01 started");

        try {
            ArrayList<HashMap<String, String>> tableList = new ArrayList<>();

            
            wp.openUrl("https://www.scrapethissite.com/pages");

            // Locating the required title to be click
            WebElement hockyTeam = wp.findEleByVisi("//a[text()='Hockey Teams: Forms, Searching and Pagination']");

            // Asserting that the located element have required text
            Assert.assertEquals(hockyTeam.getText(), "Hockey Teams: Forms, Searching and Pagination",
                    "Hockey Teams: Forms, Searching and Pagination Not Found");

            wp.click(hockyTeam);

            // Iterating through 4 pages
            for (int i = 1; i <= 4; i++) {
                wait.until(
                        ExpectedConditions.visibilityOfElementLocated(By.xpath("//th[contains(text(), 'Team Name')]")));
                WebElement nextBtn = wp.findEleByVisi("//a[@aria-label='Next']");
                wp.click(nextBtn);
                wait.until(ExpectedConditions.urlContains("" + i));

                // Finding win perentage as parent webelement
                ArrayList<WebElement> parentWin = new ArrayList<>(
                        driver.findElements(By.xpath("//td[@class = 'pct text-success' or @class='pct text-danger']")));

                for (WebElement element : parentWin) {

                    String epochTime = wp.getEpochTime();
                    String winPer = wp.getText(element);
                    float winPerInFloat = wp.stringTOFloat(winPer);

                    if (winPerInFloat < 0.40f) {
                        // Creating new HashMap for each new row
                        HashMap<String, String> singleRowData = new HashMap<>();

                        // Getting team name by the parent win persentage webelement
                        String teamName = wp
                                .getText(element.findElement(By.xpath("./preceding-sibling::td[@class='name']")));
                        String year = wp
                                .getText(element.findElement(By.xpath("./preceding-sibling::td[@class='year']")));

                        singleRowData.put("Epoch Time of Scrap", epochTime);
                        singleRowData.put("Team Name", teamName);
                        singleRowData.put("Year", year);
                        singleRowData.put("Win %", winPer);

                        // adding all the hashmap for each time for each page for all pages
                        tableList.add(singleRowData);
                    }
                }
            }
            // Converting the ArrayList<HashMap<String, String>> into JSON file
            wp.convertToJson(tableList, "hockey-team-data.json");

            System.out.println("testCase01 Passed");
        } catch (Exception e) {
            System.out.println("testCase01 Failed");
            e.printStackTrace();
        }

        System.out.println("testCase01 ended");
    }

    @Test
    public void testCase02() {
        System.out.println("testCase02 started");
        try {

            ArrayList<HashMap<String, String>> topMoviesName = new ArrayList<>();

            wp.openUrl("https://www.scrapethissite.com/pages");

            WebElement OscarTitle = wp.findEleByVisi("//a[contains(text(),'Oscar Winning Films')]");
            wp.click(OscarTitle);

            WebElement chooseTitle = wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath("//h3[contains(text(),'Choose a Year to View Films')]")));

            List<WebElement> allYears = chooseTitle.findElements(By.xpath("./following-sibling::a"));

            // traversing through each year
            for (WebElement yearElement : allYears) {
                String year = yearElement.getText();
                wp.click(yearElement);
                wait.until(ExpectedConditions
                        .visibilityOfElementLocated(By.xpath("//tbody[@id='table-body']/tr[position()<6]")));

                ArrayList<WebElement> parentRows = new ArrayList<>(
                        driver.findElements(By.xpath("//tbody[@id='table-body']/tr[position()<6]")));

                // traversing through 5 rows of each year
                for (WebElement singleRow : parentRows) {
                    HashMap<String, String> eachRowMap = new HashMap<>();

                    String epoch = wp.getEpochTime();
                    String title = singleRow.findElement(By.xpath("./td[1]")).getText();
                    String nominations = singleRow.findElement(By.xpath("./td[2]")).getText();
                    String awards = singleRow.findElement(By.xpath("./td[3]")).getText();
                    boolean isWinner = false;
                    try {
                        singleRow.findElement(By.xpath("./td[4]/i"));
                        isWinner = true;
                    } catch (Exception e) {
                    }

                    // putting all the key-values pair to the HashMap
                    eachRowMap.put("Epoch Time of Scrape", epoch);
                    eachRowMap.put("Year", year);
                    eachRowMap.put("Title", title);
                    eachRowMap.put("Nomination", nominations);
                    eachRowMap.put("Awards", awards);
                    eachRowMap.put("isWinner", (isWinner + ""));

                    // Adding HashMap to the ArrayList of Hashmap
                    topMoviesName.add(eachRowMap);
                }
            }

            wp.convertToJson(topMoviesName, "oscar-winner-data.json");

            System.out.println("testCase 02 Passed");
        } catch (Exception e) {
            System.out.println("testCase 02 Failed");
            e.printStackTrace();
        }

        System.out.println("testCase02 ended");
    }

    /*
     * Do not change the provided methods unless necessary, they will help in
     * automation and assessment
     */
    @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();

        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        wp = new Wrappers(driver, wait);

    }

    @AfterTest
    public void endTest() {
        // driver.close();
        driver.quit();

    }
}