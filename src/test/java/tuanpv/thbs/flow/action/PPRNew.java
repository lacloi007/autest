package tuanpv.thbs.flow.action;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import tuanpv.thbs.utils.JSONUtils;
import tuanpv.thbs.web.action.WebAction;

public class PPRNew implements WFAction {
	private boolean isInitialize = false;
	private WebDriver driver;
	private WebAction action;
	private Map<String, Object> config, role, ppr;
	private String location, path;

	public PPRNew(WebDriver driver, WebAction action, String location) {
		try {
			this.driver = driver;
			this.action = action;
			this.config = JSONUtils.parse("input//pr-config.json");
			this.role = JSONUtils.parse("input//role.json");
			this.ppr = JSONUtils.parse("input//pr-data.json");
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
		String description = data.get("ppr-description").toString();
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

		// login
		if (!action.logIn(role.get("ppr-requester.uid").toString(), role.get("ppr-requester.pwd").toString()))
			return;
		data.put("ppr-requester.uid", role.get("ppr-requester.uid").toString());
		data.put("ppr-requester.pwd", role.get("ppr-requester.pwd").toString());
		action.takeScreenShot(path, location, "NEW-PR-APL-0001.jpg");

		// goto apply page
		if (!action.gotoUrl(config.get("apply").toString(), By.id(config.get("apply.found.id").toString())))
			return;
		action.takeScreenShot(path, location, "NEW-PR-APL-0002.jpg");

		// click link apply for PPR
		action.findLinkFromWorkflowText(config.get("ppr-flow-name").toString()).click();

		// goto apply screen
		if (!action.waitLogo())
			return;
		action.takeScreenShot(path, location, "NEW-PR-APL-0003.jpg");

		// input top
		WebElement element = driver.findElement(By.xpath("//input[@name='prHeader.purpose' and @type='text']"));
		element.sendKeys(ppr.get("purpose").toString());
		element = driver.findElement(By.xpath("//input[@name='prHeader.deliveryLocation' and @type='text']"));
		element.sendKeys(ppr.get("delivery").toString());
		element = driver.findElement(By.xpath("//input[@name='prHeader.reference' and @type='text']"));
		element.sendKeys(ppr.get("reference").toString());
		element = driver.findElement(By.xpath("//textarea[@name='prHeader.remark']"));
		element.sendKeys(ppr.get("remark").toString());

		// take screen shot
		action.takeScreenShot(path, location, "NEW-PR-APL-0004.jpg");
		action.scrollTo(By.id("imui-footer"));

		// input bottom
		element = driver.findElement(By.xpath("//label[@for='orderTypeI']"));
		element.click();
		element = driver.findElement(By.xpath("//input[@name='tbasPrHeader.projectModel' and @type='text']"));
		element.sendKeys(ppr.get("model").toString());
		action.inputAutocompleted(driver.findElement(By.id("accountId")), ppr.get("auto-account").toString());

		// add item
		WebElement lnkAdd = driver.findElement(By.xpath("//div[contains(@class, 'msr-table')]/div/div/div/ul/li/a[@class='imui-toolbar-icon']"));
		for (int idx = 1; idx < 3; idx++) {
			if (lnkAdd.isEnabled())
				lnkAdd.click();
			else
				continue;

			By byNewRow = By.xpath("(//table[@id='prDetailTable']/tbody/tr)[" + idx + "]");
			WebElement newRow = action.getWaiting().until(ExpectedConditions.elementToBeClickable(byNewRow));
			if (newRow.isDisplayed()) {
				newRow.findElement(By.xpath("td[5]/input")).sendKeys("BSP-VN-000" + idx);
				newRow.findElement(By.xpath("td[6]/div/div/input")).sendKeys("PRT-VN-000" + idx);
				newRow.findElement(By.xpath("td[7]/input")).sendKeys("PART-VN-000" + idx);
				action.inputAutocompleted(newRow.findElement(By.xpath("td[8]/span/select")), "door");
				newRow.findElement(By.xpath("td[9]/input")).sendKeys("RED-0" + idx);
				newRow.findElement(By.xpath("td[10]/input[@type='tel']")).sendKeys("250");
				action.inputAutocompleted(newRow.findElement(By.xpath("td[11]/span/select")), "box");
				newRow.findElement(By.xpath("td[12]/input[@type='tel']")).sendKeys("100");
				action.inputAutocompleted(newRow.findElement(By.xpath("td[14]/span/select")), "ttast");

				newRow.findElement(By.xpath("td[16]/input")).sendKeys("REMARK-000" + idx);
			}
		}
		action.takeScreenShot(path, location, "NEW-PR-APL-0005.jpg");

		// apply
		WebElement btnApply = driver.findElement(By.xpath("//div[contains(@class, 'imui-operation-parts')]/input[@type='button' and @value='Apply']"));
		btnApply.click();

		WebElement gbWindow = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.className("GB_frame")));
		if (gbWindow.isDisplayed()) {
			driver.switchTo().frame(gbWindow);
			driver.switchTo().frame(driver.findElement(By.id("GB_frame")));
			driver.switchTo().frame(driver.findElement(By.id("IMW_PROC_MAIN")));

			WebElement wfForm = driver.findElement(By.id("allBlock"));
			if (wfForm.isDisplayed()) {
				WebElement wfElem = wfForm.findElement(By.xpath("//input[@type='text' and @name='matterName']"));
				wfElem.clear();
				wfElem.sendKeys("NEW | PPR | " + location);
				data.put("ppr-description", "NEW | PPR | " + location);

				action.takeScreenShot(path, location, "NEW-PR-APL-0006.jpg");
				btnApply = driver.findElement(By.xpath("//div[contains(@class, 'imui-operation-parts')]/input[@type='button' and @value='Apply']"));
				btnApply.click();

				WebElement btnOk = action.getWaiting().until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'ui-dialog-buttonset')]/button[1]")));
				btnOk.click();
			}

			driver.switchTo().defaultContent();
		}

		action.takeScreenShot(path, location, "NEW-PR-APL-0007.jpg");

		// logout from system
		if (!action.logOut())
			return;

		// log to file
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Const.FORMAT, Const.PPR_DESC, data.get("ppr-description")));
		sb.append(String.format(Const.FORMAT, Const.APPLICANT, data.get("ppr-requester.uid")));
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
		// PPR reviewer approved
		approve(data, "ppr-reviewer", "01");

		// PPR reviewer approved
		if (!StringUtils.equals(role.get("ppr-reviewer.uid").toString(), role.get("ppr-approver.uid").toString())) {
			// PPR approved approved
			approve(data, "ppr-approver", "02");
		}

		// PPR reviewer approved
		approve(data, "prototype-reviewer", "03");

		// PPR reviewer approved
		approve(data, "prototype-approver", "04");

		//
		StringBuilder sb = new StringBuilder();
		sb.append(String.format(Const.FORMAT, Const.PPR_DOC, data.get("ppr-document-no")));
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
