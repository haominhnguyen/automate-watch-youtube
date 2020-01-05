package com.controller;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.selenium.Browser;
import com.selenium.CustomExpectedConditions;

@Controller
public class HomeController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        model.addAttribute("message", "hello");
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/view_video", method = RequestMethod.POST)
    public String viewVideo(HttpServletRequest request) {
        String link = request.getParameter("link");
        String duration = request.getParameter("duration");
        System.out.println(link);
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

                String[] timeSplit = duration.split(":");
                if (timeSplit.length == 2) {
                    long time = (Integer.parseInt(timeSplit[0].trim()) * 60 + Integer.parseInt(timeSplit[1].trim()))
                            * 1000L;
                    System.out.println("total time video: " + time);
                    Thread.sleep(time);
                } else if (timeSplit.length == 3) {
                    long time = (Integer.parseInt(timeSplit[0].trim()) * 360
                            + Integer.parseInt(timeSplit[1].trim()) * 60 + Integer.parseInt(timeSplit[2].trim()))
                            * 1000L;
                    System.out.println("total time video: " + time);
                    Thread.sleep(time);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        // tat browser
        browser.shutdown();
        return "success";
    }

}
