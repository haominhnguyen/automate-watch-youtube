package com.selenium;

import org.openqa.selenium.By;
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
            // BYPASS LOGIN GOOGLE
            browser.goToUrl("https://stackoverflow.com/users/login");
            try {
                wait.until(CustomExpectedConditions.pageLoadComplete());
//                driver.findElement(By.className("search_query")).sendKeys(arr[1]);
                // click login with google
                driver.findElement(By.className("grid--cell s-btn s-btn__icon s-btn__google bar-md ba bc-black-3")).click();
                wait.until(CustomExpectedConditions.pageLoadComplete());
                driver.findElement(By.id("identifierId")).sendKeys("haonm.mmo");
                wait.until(CustomExpectedConditions.pageLoadComplete());
                driver.findElement(By.className("RveJvd snByac")).click();
                wait.until(CustomExpectedConditions.pageLoadComplete());
                driver.findElement(By.name("password")).sendKeys("anhhao1234");
                wait.until(CustomExpectedConditions.pageLoadComplete());
                driver.findElement(By.className("RveJvd snByac")).click();
                driver.findElement(By.className("ytp-time-duration"));
                long time = getDurationVideo(duration);

                Thread.sleep(time);
                browser.shutdown();

//            browser.goToUrl("https://www.youtube.com/");
//            try {
//                wait.until(CustomExpectedConditions.pageLoadComplete());
//                driver.findElement(By.name("search_query")).sendKeys(arr[1]);
//                driver.findElement(By.id("search-icon-legacy")).click();
//                wait.until(CustomExpectedConditions.pageLoadComplete());
////                driver.findElement(By.cssSelector(String.format("a[href=\"%s\"]", link))).click();
//                driver.findElement(By.cssSelector(String.format("a[href=\"/watch?v=%s\"]", arr[1]))).click();
//                wait.until(CustomExpectedConditions.pageLoadComplete());
////                WebElement e = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("ytp-play-button")));
////                System.out.println(e.getAttribute("aria-label"));
////                if (e.getAttribute("aria-label").contains("Play") || e.getAttribute("aria-label").contains("Ph")
////                        || e.getAttribute("aria-label").contains("Lire")) {
////                    e.click();
////                }
//                // find element time duration video! //TODO : handle
//                driver.findElement(By.className("ytp-time-duration"));
//                long time = getDurationVideo(duration);
//
//                Thread.sleep(time);
//                browser.shutdown();

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
