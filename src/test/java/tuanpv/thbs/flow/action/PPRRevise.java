package tuanpv.thbs.flow.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import tuanpv.thbs.condition.ExtensionConditions;
import tuanpv.thbs.utils.JSONUtils;
import tuanpv.thbs.web.action.WebAction;

public class PPRRevise implements WFAction {
	private boolean isInitialize = false;
	private WebDriver driver;
	private WebAction action;
	private Map<String, Object> config, role;
	private String location, path;

	public PPRRevise(WebDriver driver, WebAction action, String location) {
		try {
			this.driver = driver;
			this.action = action;
			this.config = JSONUtils.parse("input//pr-config.json");
			this.role = JSONUtils.parse("input//role.json");
			this.path = config.get("evident").toString();
			this.location = location;
			this.isInitialize = true;
		} catch (Exception e) {
			e.printStackTrace();
			this.isInitialize = false;
		}
	}

	private void approve(Map<String, Object> data, String code, String step) throws Exception {
		String fName = "NEW-PR-APR-" + step;

		// login
		if (!action.logIn(role.get(code + ".uid").toString(), role.get(code + ".pwd").toString()))
			return;
		data.put(code + ".uid", role.get(code + ".uid").toString());
		data.put(code + ".pwd", role.get(code + ".pwd").toString());
		action.takeScreenShot(path, location, fName + "-0001.jpg");

		// goto list of processing page
		String description = data.get("ppr-rev-description").toString();
		String PROCESS = "http://172.16.0.35:8080/tbas/im_workflow/user/process/process_list";
		if (!action.gotoUrl(PROCESS, By.id("conditionGreyBox")))
			return;

		// goto search item
		driver.findElement(By.id("conditionGreyBox")).click();
		WebElement gbWindow = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.className("GB_frame")));
		driver.switchTo().frame(gbWindow);
		driver.switchTo().frame(driver.findElement(By.id("GB_frame")));

		// enter search description
		WebElement wfForm = driver.findElement(By.id("imui-tabitem-tab_searchInfo"));
		WebElement wfElem = wfForm.findElement(By.id("listPageCol_MatterName"));
		wfElem.clear();
		wfElem.sendKeys(description);
		action.takeScreenShot(path, location, fName + "-0002.jpg");
		wfForm.findElement(By.id("search")).click();

		// goto search result
		By byLink = By.xpath("//tr[@id='1']/td[1]/a");
		if (!action.waitBy(byLink))
			return;
		action.takeScreenShot(path, location, fName + "-0003.jpg");

		// click to action
		driver.findElement(byLink).click();

		// goto view data for processing
		if (!action.waitLogo())
			return;
		action.takeScreenShot(path, location, fName + "-0004.jpg");

		// get PPR Doc No.
		if (!data.containsKey("ppr-document-no")) {
			WebElement elDocNo = driver.findElement(By.xpath("//input[@type='hidden' and @name='docOwner.docNo']"));
			String grDocNo = elDocNo.getAttribute("value");
			data.put("ppr-document-no", grDocNo);
		}

		// process
		driver.findElement(By.id("btnProcess")).click();
		gbWindow = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.className("GB_frame")));
		if (!gbWindow.isDisplayed())
			return;
		driver.switchTo().frame(gbWindow);
		driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
		driver.switchTo().frame(driver.findElement(By.id("IMW_PROC_MAIN")));
		action.takeScreenShot(path, location, fName + "-0005.jpg");
		driver.findElement(By.xpath("//input[@id='proc_button' and @value='Approve']")).click();
		WebElement btnOk = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'ui-dialog-buttonset')]/button[1]")));
		btnOk.click();

		// goto view data for processing
		if (!action.waitLogo())
			return;
		action.takeScreenShot(path, location, fName + "-0006.jpg");

		// logout from system
		if (!action.logOut())
			return;

		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Const.FORMAT, Const.APPROVE + step, data.get(code + ".uid")));
		action.log(path, location, sb);
	}

	@Override
	public void apply(Map<String, Object> data) throws Exception {
		if (!isInitialize)
			return;

		String code = "ppr-requester";
		String ssid = "REV-PR-APL";
		data.put(code + ".uid", role.get(code + ".uid").toString());
		data.put(code + ".pwd", role.get(code + ".pwd").toString());

		// login
		if (!action.logIn(role.get(code + ".uid").toString(), role.get(code + ".pwd").toString()))
			return;
		action.takeScreenShot(path, location, ssid + "-0001.jpg");

		// goto apply page
		if (!action.gotoUrl(config.get("apply").toString(), By.id(config.get("apply.found.id").toString())))
			return;
		action.takeScreenShot(path, location, ssid + "-0002.jpg");

		// click link apply for PPR
		action.findLinkFromWorkflowText(config.get("ppr-flow-name").toString()).click();

		// goto apply screen
		action.waitLogo();
		action.takeScreenShot(path, location, ssid + "-0003.jpg");

		// set action to REVISE
		action.inputAutocompleted(driver.findElement(By.id("applicationType")), "revise");
		driver.findElement(By.xpath("//input[@name='prHeader.purpose' and @type='text']")).sendKeys("");

		/**
		 * select the PPR for revise
		 */
		action.getWaiting().until(ExpectedConditions.elementToBeClickable(By.id("policy_treegrid_search"))).click();

		// find displayed
		WebElement dlgDisplayed = action.getWaiting().until(ExtensionConditions.visibilityOfElementLocated(By.xpath("//div[@aria-labelledby='ui-dialog-title-prSearchDialog']")));

		// input document no for search
		WebElement elDocNo = dlgDisplayed.findElement(By.id("searchDocNo"));
		elDocNo.clear();
		elDocNo.sendKeys(data.get("ppr-document-no").toString());

		// click button Search
		action.getWaiting().until(ExpectedConditions.elementToBeClickable(dlgDisplayed.findElement(By.id("btnSearch")))).click();
		action.getWaiting().until(ExpectedConditions.elementToBeClickable(dlgDisplayed.findElement(By.xpath("//*[@id='searchPr']/div[5]/div/div")))).click();
		action.takeScreenShot(path, location, ssid + "-0004.jpg");

		// click select item
		driver.findElement(By.xpath("/html/body/div[11]/div[11]/div/button[1]")).click();

		// click apply on screen
		action.getWaiting().until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(@class, 'imui-operation-parts')]/input[@type='button' and @value='Apply']"))).click();

		/**
		 * Working with apply screen of IMART
		 */
		WebElement gbWindow = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.className("GB_frame")));
		driver.switchTo().frame(gbWindow);
		driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
		driver.switchTo().frame(driver.findElement(By.id("IMW_PROC_MAIN")));

		WebElement wfForm = driver.findElement(By.id("allBlock"));
		if (wfForm.isDisplayed()) {
			WebElement wfElem = wfForm.findElement(By.xpath("//input[@type='text' and @name='matterName']"));
			wfElem.clear();
			wfElem.sendKeys("REV | PPR | " + location);
			data.put("ppr-rev-description", "REV | PPR | " + location);
			action.takeScreenShot(path, location, ssid + "-0005.jpg");

			// click apply of work flow
			driver.findElement(By.xpath("//div[contains(@class, 'imui-operation-parts')]/input[@type='button' and @value='Apply']")).click();

			// confirm
			action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'ui-dialog-buttonset')]/button[1]"))).click();
		}

		// wait for finish apply
		action.waitLogo();
		action.takeScreenShot(path, location, ssid + "-0006.jpg");
		action.logOut();

		// log to file
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Const.FORMAT, Const.PPR_REV_DESC, data.get("ppr-rev-description")));
		sb.append(String.format(Const.FORMAT, Const.APPLICANT, data.get("ppr-requester.uid")));
		action.log(path, location, sb);
	}

	@Override
	public void draft(Map<String, Object> data) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void completed(Map<String, Object> data) throws Exception {
		apply(data);
		approve(data, "ppr-reviewer", "01");
		if (!StringUtils.equals(role.get("ppr-reviewer.uid").toString(), role.get("ppr-approver.uid").toString()))
			approve(data, "ppr-approver", "02");
		approve(data, "prototype-reviewer", "03");
		approve(data, "prototype-approver", "04");

		// logging data
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Const.FORMAT, Const.PPR_DOC, data.get("ppr-document-no")));
		action.log(path, location, sb);
	}

	@Override
	public void returnBack(Map<String, Object> data) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void reject(Map<String, Object> data) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel(Map<String, Object> data) throws Exception {
		// TODO Auto-generated method stub

	}
}
