package tuanpv.thbs.web.action;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class THBS2Action implements WebAction {
	private WebDriver driver;
	private WebDriverWait wait;

	public THBS2Action(WebDriver driver) {
		this.driver = driver;
		if (driver != null) {
			wait = new WebDriverWait(driver, 10);
		}
	}

	@Override
	public boolean logOut() {
		driver.get("http://172.16.0.35:8080/tbas/logout");
		WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("imui-login-logo")));
		return logo.isDisplayed();
	}

	@Override
	public boolean logIn(String usr, String pwd) {
		driver.get("http://172.16.0.35:8080/tbas/login");

		WebElement element = driver.findElement(By.id("im_user"));
		if (element != null) {
			element.clear();
			element.sendKeys(usr);
		}

		element = driver.findElement(By.id("im_password"));
		if (element != null) {
			element.clear();
			element.sendKeys(pwd);
		}

		element = driver.findElement(By.className("imui-btn-login"));
		if (element != null) {
			element.click();
		}

		return waitLogo();
	}

	@Override
	public void takeScreenShot(String path, String location, String name) {
		try {
			path += File.separator + getCurrentLocalDate() + File.separator + location;
			FileUtils.forceMkdir(new File(path));

			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

			path += File.separator + name;
			FileUtils.copyFile(scrFile, new File(path));
			System.out.println("Take screen shot to : " + path);
		} catch (Exception e) {
			System.err.println("Cannot take screen shot to " + path);
		}
	}

	public String getCurrentLocalDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	}

	@Override
	public boolean gotoUrl(String url, By byPresent) {
		driver.get(url);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(byPresent));
		return element.isDisplayed();
	}

	@Override
	public boolean waitLogo() {
		WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("imui-logo")));
		return logo.isDisplayed();
	}

	@Override
	public boolean waitBy(By by) {
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		return element.isDisplayed();
	}

	@Override
	public WebElement findLinkFromWorkflowText(String name) {
		WebElement table = driver.findElement(By.id("flowList"));
		WebElement element = table.findElement(By.xpath("//td[@title='" + name + "' and @role='gridcell']"));
		WebElement tr = element.findElement(By.xpath("./.."));
		WebElement a = tr.findElement(By.xpath("td/a"));
		return a;
	}

	@Override
	public boolean waitForClickable(By by) {
		WebElement element = wait.until(ExpectedConditions.elementToBeClickable(by));
		return element.isEnabled();
	}

	@Override
	public boolean inputAutocompleted(WebElement select, String code) {
		WebElement parent = select.findElement(By.xpath("./.."));
		WebElement span = parent.findElement(By.xpath("span"));
		span.click();

		WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@type='search' and @role='textbox']")));
		input.sendKeys(code);
		input.sendKeys(Keys.ENTER);
		return true;
	}

	@Override
	public WebElement waitElement(WebElement element) {
		WebElement item = wait.until(ExpectedConditions.elementToBeClickable(element));
		return item;
	}

	@Override
	public WebDriverWait getWaiting() {
		// TODO Auto-generated method stub
		return this.wait;
	}

	@Override
	public void scrollTo(By by) throws InterruptedException {
		WebElement element = driver.findElement(by);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
		Thread.sleep(500);
	}

	@Override
	public void log(String path, String location, StringBuilder sb) {
		// TODO Auto-generated method stub
		path += File.separator + getCurrentLocalDate();
		path += File.separator + location + ".txt";
		try {
			FileUtils.writeStringToFile(new File(path), sb.toString(), "UTF-8", true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
