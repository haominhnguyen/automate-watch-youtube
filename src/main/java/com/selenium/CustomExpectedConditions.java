
package com.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;

/**
 * customize expected condition
 *
 * @version 1.0
 * @see
 * @since 1.0
 *
 */
public class CustomExpectedConditions {

    CustomExpectedConditions() {
    }

    /**
     * wait for page load complete
     *
     * @return
     * @see
     * @since 1.0
     */
    public static Function<WebDriver, Boolean> pageLoadComplete() {
        return driver -> {
            try {
                boolean flagLoadComplete = false;
                flagLoadComplete = ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState")
                        .equals("complete");
                return flagLoadComplete;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        };

    }

    /**
     * Custom expected for waiting child element of parent element visible in the screen
     *
     * @param WebElement parent
     * @param By         childLocator
     * @return Function<WebDriver, WebElement>
     * @see
     * @since 1.0
     */
    public static Function<WebDriver, WebElement> visibileOf(WebElement parent, By childLocator) {
        return driver -> {
            WebElement element = parent.findElement(childLocator);

            if (element.isDisplayed()) {
                return element;
            }

            return null;
        };
    }

}
