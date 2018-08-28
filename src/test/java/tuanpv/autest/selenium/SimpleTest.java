package tuanpv.autest.selenium;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import junit.framework.TestCase;

public class SimpleTest extends TestCase {
	private WebDriver driver;

	@Before
	public void setUp() throws Exception {
		// driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"),
		// DesiredCapabilities.firefox());
		driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
		Options options = driver.manage();
		options.timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		options.window().setSize(new Dimension(1280, 800));
	}

	@Test
	public void testSimple() throws Exception {
		driver.get("http://192.168.100.141:8080/B20180731/login");
		assertEquals("Login", driver.getTitle());

		WebElement element = driver.findElement(By.id("im_user"));
		if (element != null) {
			element.clear();
			element.sendKeys("tenant");
		}

		element = driver.findElement(By.id("im_password"));
		if (element != null) {
			element.clear();
			element.sendKeys("tenant");
		}

		element = driver.findElement(By.className("imui-btn-login"));
		if (element != null) {
			element.click();
		}

		wait(1000);

		assertEquals("Request for Goods/Service Search", driver.getTitle());
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}
}
