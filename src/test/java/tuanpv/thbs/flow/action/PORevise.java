package tuanpv.thbs.flow.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import tuanpv.thbs.utils.JSONUtils;
import tuanpv.thbs.web.action.WebAction;

public class PORevise implements WFAction {
	private boolean isInitialize = false;
	private WebDriver driver;
	private WebAction action;
	private Map<String, Object> config, role;
	private String location, path;

	public PORevise(WebDriver driver, WebAction action, String location) {
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
		String fName = "NEW-PO-APR-" + step;

		// login
		if (!action.logIn(role.get(code + ".uid").toString(), role.get(code + ".pwd").toString()))
			return;
		data.put(code + ".uid", role.get(code + ".uid").toString());
		data.put(code + ".pwd", role.get(code + ".pwd").toString());
		action.takeScreenShot(path, location, fName + "-0001.jpg");

		// goto list of processing page
		String description = data.get("po-description").toString();
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

		// get PPR Doc No.
		String poDocNo = "";
		if (StringUtils.equals("02", step)) {
			WebElement elDocNo = driver.findElement(By.xpath("//input[@type='hidden' and @name='docOwner.docNo']"));
			System.out.println("DocNO" + elDocNo.getAttribute("value"));
			poDocNo = elDocNo.getAttribute("value");

			if (StringUtils.isNoneEmpty(poDocNo))
				data.put("po-document-no", poDocNo);
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

		// login
		if (!action.logIn(role.get("po-applicant.uid").toString(), role.get("po-applicant.pwd").toString()))
			return;
		data.put("po-applicant.uid", role.get("po-applicant.uid").toString());
		data.put("po-applicant.pwd", role.get("po-applicant.pwd").toString());
		action.takeScreenShot(path, location, "NEW-PO-APL-0001.jpg");

		// goto apply page
		if (!action.gotoUrl(config.get("apply").toString(), By.id(config.get("apply.found.id").toString())))
			return;
		action.takeScreenShot(path, location, "NEW-PO-APL-0002.jpg");

		// click link apply for PPR
		action.findLinkFromWorkflowText(config.get("po-flow-name").toString()).click();

		// goto apply screen
		if (!action.waitLogo())
			return;
		action.takeScreenShot(path, location, "NEW-PO-APL-0003.jpg");

		// click to Search button
		driver.findElement(By.id("policy_treegrid_search")).click();
		if (!action.waitBy(By.id("prDocNo")))
			return;

		// input document no
		WebElement element = driver.findElement(By.id("prDocNo"));
		element.clear();
		element.sendKeys(data.get("ppr-document-no").toString());

		// search PPR
		By bySearch = By.id("btnSearch");
		driver.findElement(bySearch).click();
		if (!action.waitBy(By.xpath("//*[@id='pprSearchGrid']/div[5]/div/div[1]")))
			return;

		// click select
		driver.findElement(By.xpath("//*[@id='pprSearchGrid']/div[5]/div/div[1]")).click();
		driver.findElement(By.xpath("/html/body/div[14]/div[11]/div/button[1]")).click();

		if (!action.waitLogo())
			return;

		// take screen shot
		action.takeScreenShot(path, location, "NEW-PO-APL-0004.jpg");
		action.scrollTo(By.id("imui-footer"));

		// apply
		WebElement btnApply = driver.findElement(By.xpath("//div[contains(@class, 'imui-operation-parts')]/input[@type='button' and @value='Apply']"));
		btnApply.click();

		WebElement gbWindow = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.className("GB_frame")));
		if (!gbWindow.isDisplayed()) {
			return;
		}

		driver.switchTo().frame(gbWindow);
		driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
		driver.switchTo().frame(driver.findElement(By.id("IMW_PROC_MAIN")));

		WebElement wfForm = driver.findElement(By.id("allBlock"));
		if (!wfForm.isDisplayed()) {
			return;
		}

		WebElement wfElem = wfForm.findElement(By.xpath("//input[@type='text' and @name='matterName']"));
		wfElem.clear();
		wfElem.sendKeys("NEW | PO | " + location);
		data.put("po-description", "NEW | PO | " + location);

		action.takeScreenShot(path, location, "NEW-PO-APL-0006.jpg");
		btnApply = driver.findElement(By.xpath("//div[contains(@class, 'imui-operation-parts')]/input[@type='button' and @value='Apply']"));
		btnApply.click();

		WebElement btnOk = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'ui-dialog-buttonset')]/button[1]")));
		btnOk.click();

		driver.switchTo().defaultContent();

		action.takeScreenShot(path, location, "NEW-PO-APL-0007.jpg");

		// logout from system
		if (!action.logOut())
			return;

		// log to file
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(String.format(Const.FORMAT, Const.PO_DESC, data.get("po-description")));
		sb.append(String.format(Const.FORMAT, Const.APPLICANT, data.get("po-applicant.uid")));
		action.log(path, location, sb);
	}

	@Override
	public void draft(Map<String, Object> data) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void completed(Map<String, Object> data) throws Exception {
		// apply new data
		apply(data);

		if (!StringUtils.equals(role.get("po-applicant.uid").toString(), role.get("po-reviewer.uid").toString())) {
			// PO reviewer approved
			approve(data, "po-reviewer", "01");
		}

		// PO approved
		Thread.sleep(5000);
		approve(data, "po-approver", "02");

		//
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Const.FORMAT, Const.PO_DOC, data.get("po-document-no")));
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
