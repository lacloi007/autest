package tuanpv.thbs.web.action;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

public interface WebAction {
	public WebDriverWait getWaiting();

	public boolean logIn(String usr, String pwd);

	public boolean logOut();

	public boolean gotoUrl(String url, By byPresent);

	public boolean waitLogo();

	public boolean waitBy(By by);

	public WebElement waitElement(WebElement element);

	public boolean waitForClickable(By by);

	public WebElement findLinkFromWorkflowText(String name);

	public boolean inputAutocompleted(WebElement select, String code);

	public void takeScreenShot(String path, String location, String name);
	
	public void log(String path, String location, StringBuilder sb);
	
	public void scrollTo(By by) throws InterruptedException ;
}
