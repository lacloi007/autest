package tuanpv.thbs.flow.testcase;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import junit.framework.TestCase;
import tuanpv.thbs.flow.action.GRNew;
import tuanpv.thbs.flow.action.WFAction;
import tuanpv.thbs.web.action.THBSAction;
import tuanpv.thbs.web.action.WebAction;

public class CreateGRByDesc extends TestCase {
	private WebDriver driver;
	private WebAction action;
	private String location;
	private WFAction gr;

	@Before
	public void setUp() throws Exception {
		driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), DesiredCapabilities.chrome());
		Options options = driver.manage();
		options.timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		options.window().setSize(new Dimension(1280, 1500));

		// initialize action
		action = new THBSAction(driver);
		location = (new Date()).getTime() + "";
		gr = new GRNew(driver, action, location);
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
	}

	@Test
	public void test00001() throws Exception {
		Map<String, Object> data = new TreeMap<>();

		data.put("po-document-no", "S1809-0059");

		// create GR
		gr.completed(data);
	}
}