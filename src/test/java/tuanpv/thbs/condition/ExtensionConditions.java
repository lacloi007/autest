package tuanpv.thbs.condition;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * The extension conditions for waiting action
 * @author TuanPV
 */
public class ExtensionConditions {
	private final static Logger log = Logger.getLogger(ExpectedConditions.class.getName());
	private ExtensionConditions() {
	}

	public static ExpectedCondition<WebElement> visibilityOfElementLocated(final By locator) {
		return new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				try {
					return elementIfVisible(findElement(locator, driver));
				} catch (StaleElementReferenceException e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "visibility of element located by " + locator;
			}
		};
	}

	private static WebElement elementIfVisible(WebElement element) {
		return element.isDisplayed() ? element : null;
	}

	private static WebElement findElement(By by, WebDriver driver) {
		try {
			return driver.findElements(by).stream().filter(x -> x.isDisplayed()).findFirst().orElseThrow(() -> new NoSuchElementException("Cannot locate an element using " + by));
		} catch (NoSuchElementException e) {
			throw e;
		} catch (WebDriverException e) {
			log.log(Level.WARNING, String.format("WebDriverException thrown by findElement(%s)", by), e);
			throw e;
		}
	}
}
