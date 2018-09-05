package tuanpv.thbs.flow.action;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import tuanpv.thbs.utils.JSONUtils;
import tuanpv.thbs.web.action.WebAction;

public class GRNew implements WFAction {
	private boolean isInitialize = false;
	private WebDriver driver;
	private WebAction action;
	private Map<String, Object> config, role, content;
	private String location, path;

	public GRNew(WebDriver driver, WebAction action, String location) {
		try {
			this.driver = driver;
			this.action = action;
			this.config = JSONUtils.parse("input//pr-config.json");
			this.role = JSONUtils.parse("input//role.json");
			this.content = JSONUtils.parse("input//pr-data.json");
			this.path = config.get("evident").toString();
			this.location = location;
			this.isInitialize = true;
		} catch (Exception e) {
			e.printStackTrace();
			this.isInitialize = false;
		}
	}

	private void approve(Map<String, Object> data, String code, String step) throws Exception {
		String fName = "NEW-GR-APR-" + step;

		// login
		if (!action.logIn(role.get(code + ".uid").toString(), role.get(code + ".pwd").toString()))
			return;
		data.put(code + ".uid", role.get(code + ".uid").toString());
		data.put(code + ".pwd", role.get(code + ".pwd").toString());
		action.takeScreenShot(path, location, fName + "-0001.jpg");

		// goto list of processing page
		String description = data.get("gr-description").toString();
		String PROCESS = "http://172.16.0.35:8080/tbas/im_workflow/user/process/process_list";
		if (!action.gotoUrl(PROCESS, By.id("conditionGreyBox")))
			return;

		// goto search item
		driver.findElement(By.id("conditionGreyBox")).click();
		WebElement gbWindow = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.className("GB_frame")));
		if (!gbWindow.isDisplayed())
			return;
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
		// driver.switchTo().defaultContent();
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

		// get GR Doc No.
		if (!data.containsKey("gr-document-no")) {
			WebElement elDocNo = driver.findElement(By.xpath("//input[@type='hidden' and @name='docOwner.docNo']"));
			String grDocNo = elDocNo.getAttribute("value");
			data.put("gr-document-no", grDocNo);
		}

		// process Approve
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

		// wait for finish approve
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

		// login
		String code = "gr-applicant";
		String log = "NEW-PR-APL-";
		if (!action.logIn(role.get(code + ".uid").toString(), role.get(code + ".pwd").toString()))
			return;
		data.put(code + ".uid", role.get(code + ".uid").toString());
		data.put(code + ".pwd", role.get(code + ".pwd").toString());
		action.takeScreenShot(path, location, log + "0001.jpg");

		// goto apply page
		if (!action.gotoUrl(config.get("apply").toString(), By.id(config.get("apply.found.id").toString())))
			return;
		action.takeScreenShot(path, location, log + "0002.jpg");

		// click link apply for PPR
		action.findLinkFromWorkflowText(config.get("gr-flow-name").toString()).click();

		// goto apply screen
		if (!action.waitLogo())
			return;
		action.takeScreenShot(path, location, log + "0003.jpg");

		// click to Search button
		driver.findElement(By.id("policy_treegrid_search")).click();
		if (!action.waitBy(By.id("searchPoDocNo")))
			return;

		// input document no
		WebElement element = driver.findElement(By.id("searchPoDocNo"));
		element.clear();
		element.sendKeys(data.get("po-document-no").toString());

		// search PO
		By bySearch = By.id("btnSearch");
		driver.findElement(bySearch).click();
		if (!action.waitBy(By.xpath("//*[@id='poSearchGrid']/div[5]/div/div[1]")))
			return;

		// click select first item
		driver.findElement(By.xpath("//*[@id='poSearchGrid']/div[5]/div/div[1]")).click();
		driver.findElement(By.xpath("/html/body/div[10]/div[11]/div/button[1]")).click();
		if (!action.waitLogo())
			return;

		// input top
		driver.findElement(By.id("receivedDate")).sendKeys(content.get("received-date").toString());
		driver.findElement(By.xpath("//input[@name='grHeader.supplierInvoiceNo' and @type='text']")).sendKeys("SIV" + location);
		driver.findElement(By.xpath("//input[@name='grHeader.supplierDoNo' and @type='text']")).sendKeys("SDO" + location);
		driver.findElement(By.xpath("//textarea[@name='grHeader.remark']")).sendKeys(content.get("remark").toString());

		// take screen shot
		action.takeScreenShot(path, location, log + "0004.jpg");
		action.scrollTo(By.id("imui-footer"));
		action.takeScreenShot(path, location, log + "0005.jpg");

		// apply
		driver.findElement(By.xpath("//div[contains(@class, 'imui-operation-parts')]/input[@type='button' and @value='Apply']")).click();
		WebElement gbWindow = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.className("GB_frame")));
		if (!gbWindow.isDisplayed())
			return;
		driver.switchTo().frame(gbWindow);
		driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
		driver.switchTo().frame(driver.findElement(By.id("IMW_PROC_MAIN")));
		WebElement wfForm = driver.findElement(By.id("allBlock"));
		if (!wfForm.isDisplayed())
			return;

		// input description for searching
		WebElement wfElem = wfForm.findElement(By.xpath("//input[@type='text' and @name='matterName']"));
		wfElem.clear();
		wfElem.sendKeys("NEW | GR | " + location);
		data.put("gr-description", "NEW | GR | " + location);
		action.takeScreenShot(path, location, log + "0006.jpg");
		driver.findElement(By.xpath("//div[contains(@class, 'imui-operation-parts')]/input[@type='button' and @value='Apply']")).click();

		// confirm applying
		WebElement btnOk = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'ui-dialog-buttonset')]/button[1]")));
		btnOk.click();

		if (!action.waitLogo())
			return;
		action.takeScreenShot(path, location, log + "0007.jpg");

		// logout from system
		if (!action.logOut())
			return;

		// log to file
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(String.format(Const.FORMAT, Const.GR_DESC, data.get("gr-description")));
		sb.append(String.format(Const.FORMAT, Const.APPLICANT, data.get(code + ".uid")));
		action.log(path, location, sb);
	}

	@Override
	public void draft(Map<String, Object> data) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void completed(Map<String, Object> data) throws Exception {
		apply(data);
		approve(data, "gr-reviewer", "01");
		approve(data, "ppr-requester", "02");

		// logging
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Const.FORMAT, Const.GR_DOC, data.get("gr-document-no")));
		sb.append("\n");
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
