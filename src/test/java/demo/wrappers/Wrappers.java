package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here 
     */
    ChromeDriver driver;
    WebDriverWait wait;
    //Constructor of Wrappers class to initilize driver and wait
    public Wrappers(ChromeDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    //Used to open Url
    public void openUrl(String url) {
        System.out.println("Opening url : " + url);
        driver.get(url);
    }

    //TO click an WebElement
    public void click(WebElement element) {
        element.click();
    }

    //To send text to a WebElement
    public void sendKeys(WebElement element, String s) {
        System.out.println("Sending keys to element: " + s);
        element.clear();
        element.sendKeys(s);
    }

    //Locating webelement by visibility of using xpath
    public WebElement findEleByVisi(String xpath) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(xpath)));
    }

    //Locating webelement by presence of using xpath
    public WebElement findEleByPres(String xpath) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    //Converting String into float
    public float stringTOFloat(String s) {
        return Float.parseFloat(s);
    }

    public String getText(WebElement element) {
        return element.getText();
    }

    //Converting the ArrayList<HashMap<String, String>> into JSON file
    public void convertToJson(ArrayList<HashMap<String, String>> arrList, String fileName) {
        ObjectMapper mapper = new ObjectMapper();

        // Converting map to a JSON payload as string
        try {
            String teamYearWin = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrList);
            System.out.println(teamYearWin);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
 
        String userDir = System.getProperty("user.dir");
 
        //Writing JSON on a file
        try {
            mapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(userDir + "\\src\\test\\resources\\" + fileName), arrList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Returning epoch time as String
    public String getEpochTime() {
        return "" + System.currentTimeMillis();
    }
}
