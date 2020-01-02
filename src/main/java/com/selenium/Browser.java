package com.selenium;

import java.util.concurrent.TimeUnit;

import javax.security.auth.login.Configuration;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Browser {

    /*
     * public static synchronized Browser getBrowser() { if (instance == null) {
     * instance = new Browser(); windowMap = new HashMap<>(); } return instance; }
     */

    // private static Browser instance = null;

    private WebDriver driver = null;

    public WebDriver getDriver() {
        return this.driver;
    }

    public boolean launch(int loadTimeOut) {
        if (this.driver == null) {
            String driverLocation = null;
            driverLocation = System.getProperty("user.dir") + "//geckodriver.exe";
            /*
             * System.setProperty("webdriver.chrome.driver", driverLocation); this.driver =
             * new ChromeDriver();
             */

            System.setProperty("webdriver.gecko.driver", driverLocation);
            DesiredCapabilities capabilities = new DesiredCapabilities(DesiredCapabilities.firefox().getBrowserName(),
                    "ESR", Platform.ANY);
            capabilities.setCapability("marionette", true);
            this.driver = new FirefoxDriver(capabilities);
            this.driver.manage().timeouts().pageLoadTimeout(loadTimeOut, TimeUnit.SECONDS);
            this.driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
        return true;
    }

    public void goToUrl(String url) {
        this.driver.get(url);
    }

    public void shutdown() {
        this.driver.quit();
    }

    public void closeWindow(String url) {
        for (String window : this.driver.getWindowHandles()) {
            this.driver.switchTo().window(window);
            if (this.driver.getCurrentUrl().equals(url)) {
                this.driver.close();
            }
        }
    }

}
