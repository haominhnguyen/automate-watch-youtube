package com.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PlayVideo extends Thread {

    private String link;
    private String duration;

    public PlayVideo(String link, String duration) {
        super();
        this.link = link;
        this.duration = duration;
    }

    public void run() {
        Browser browser = new Browser();
        browser.launch(10);

        WebDriver driver = browser.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, 30);

        String[] arr = link.split("=");
        if (arr.length == 2) {
            browser.goToUrl("https://www.google.com/");
            try {
                wait.until(CustomExpectedConditions.pageLoadComplete());
                driver.findElement(By.name("q")).sendKeys(arr[1]);
                driver.findElement(By.name("q")).sendKeys(Keys.ENTER);

                wait.until(CustomExpectedConditions.pageLoadComplete());
                driver.findElement(By.cssSelector(String.format("a[href=\"%s\"]", link))).click();

                wait.until(CustomExpectedConditions.pageLoadComplete());

                WebElement e = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ytp-play-button")));
                System.out.println(e.getAttribute("aria-label"));
                if (e.getAttribute("aria-label").contains("Play") || e.getAttribute("aria-label").contains("Ph")
                        || e.getAttribute("aria-label").contains("Lire")) {
                    e.click();
                }

                long time = getDurationVideo(duration);

                Thread.sleep(time);
                browser.shutdown();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private long getDurationVideo(String duration) {
        long time = 0;
        String[] timeSplit = duration.split(":");
        if (timeSplit.length == 2) {
            time = (Integer.parseInt(timeSplit[0].trim()) * 60 + Integer.parseInt(timeSplit[1].trim())) * 1000L;
            System.out.println("total time video: " + time);
        } else if (timeSplit.length == 3) {
            time = (Integer.parseInt(timeSplit[0].trim()) * 360 + Integer.parseInt(timeSplit[1].trim()) * 60
                    + Integer.parseInt(timeSplit[2].trim())) * 1000L;
            System.out.println("total time video: " + time);
        }
        return time;
    }

}
