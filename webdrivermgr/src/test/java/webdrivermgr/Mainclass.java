package webdrivermgr;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.text.SimpleDateFormat;
import java.util.Date;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Mainclass {
	static ChromeDriver chdriver;

	public static void main(String[] args) throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		chdriver = new ChromeDriver();
		chdriver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);
		export_Result_to_Excel.writeHeaders();
		LoginPage();
		ServicePage();
		HomePage();
		searchAll();
		pageSize15();
		changeOffset();
		pageSize100("SearchAll");
		browseByAssetHeirarchy();
		relatedAssets();
		selectAsset();
		createAsset_glossary();
		createAsset_policy();
		addRelatedAsset();
	}

	public static void LoginPage() throws InterruptedException {
		chdriver.manage().window().fullscreen();
		chdriver.get(elements.testingURL);
		getElementByXpath(chdriver, 10, "//span[contains(text(),'Log In')]");
		Thread.sleep(2000);
		navigationTiming.theMain("LoginPage");
	}

	public static void ServicePage() throws InterruptedException {
		WebElement userName = getElementByXpath(chdriver, 10, elements.userName);
		userName.sendKeys("tkhong@informatica.com");
		WebElement password = getElementByXpath(chdriver, 10, elements.password);
		password.sendKeys("p@ssw0rd1234");
		WebElement logIn = getElementByXpath(chdriver, 10, elements.logInBttn);
		logIn.click();
		// getElementByXpath(chdriver, 10, "//span[contains(text(),'Data
		// Governance')]");
		Thread.sleep(2000);
		navigationTiming.theMain("ServicesPage");
	}

	public static void HomePage() throws InterruptedException {
		WebElement dataGovernance = getElementByXpath(chdriver, 10, elements.dataGovernanceBttn);
		dataGovernance.click();
		getElementByXpath(chdriver, 10, elements.searchTab);
		Thread.sleep(3000);
		navigationTiming.theMain("HomePage");
	}

	public static void searchAll() throws InterruptedException {
		chdriver.manage().window().fullscreen();
		getElementByXpath(chdriver, 10, elements.searchTab).sendKeys("*");
		getElementByXpath(chdriver, 10, elements.searchTab).sendKeys(Keys.ENTER);
		long startTi = System.currentTimeMillis();
		if (getPageSizetxt() != null) {
			WebElement pgsizeElement = getElementByXpath(chdriver, 10, elements.row10);
		}
		System.out.println("SearchAll " + (System.currentTimeMillis() - startTi));
		Thread.sleep(1000);
		navigationTiming.theMain("SearchAll");

	}

	private static void pageSize15() throws InterruptedException {
		WebElement pageSizeElement = getElementByXpath(chdriver, 10, elements.pageSizeDropdown);
		Select select = new Select(pageSizeElement);
		select.selectByValue("15");
		long startTi = System.currentTimeMillis();
		if (getPageSizetxt() != null) {
			WebElement pgsizeElement = getElementByXpath(chdriver, 10, elements.row15);
		}
		System.out.println("SearchPageSize15 " + (System.currentTimeMillis() - startTi));
		Thread.sleep(1000);
		navigationTiming.theMain("SearchPageSize15");
	}

	public static void changeOffset() throws InterruptedException {

		WebElement nextArrowElement = getElementByXpath(chdriver, 10, elements.nextArrowoffset);
		nextArrowElement.click();
		Thread.sleep(1000);
		if (getPageSizetxt() != null) {
			WebElement pgsizeElement = getElementByXpath(chdriver, 10, elements.row15);
		}
		navigationTiming.theMain("SearchChangeOffset");
	}

	public static void pageSize100(String action) throws InterruptedException {
		if (elementIsPresent(elements.pageSizeDropdown)) {
			WebElement pageSizeElement = getElementByXpath(chdriver, 10, elements.pageSizeDropdown);
			Select select100 = new Select(pageSizeElement);
			select100.selectByValue("100");
			Thread.sleep(1000);
			if (getPageSizetxt() != null) {
				WebElement pgsizeElement = getElementByXpath(chdriver, 10, elements.row100);
			}
			navigationTiming.theMain(action + "PageSize100");
		}

	}

	public static void browseByAssetHeirarchy() throws InterruptedException {
		WebElement closeTab = getElementByXpath(chdriver, 10,
				"//ul[@class='d-r35-shell__nav__links']//i[@class='d-r35-shell__nav__links__link__close-button__icon']");
		closeTab.click();
		WebElement GlossaryLabel = getElementByXpath(chdriver, 10, elements.GlossaryLabel);
		GlossaryLabel.click();
		if (getPageSizetxt() != null) {
			getElementByXpath(chdriver, 10, elements.row10);
		}
		Thread.sleep(1000);
		navigationTiming.theMain("GlossaryLabel");
	}

	private static void relatedAssets() throws InterruptedException {
		WebElement relatedAsset = getElementByXpath(chdriver, 10, elements.relatedAsset);
		relatedAsset.click();
		// "//div[@id='38923_Related Assets-breadcrumb']"
		// getElementByXpath(chdriver, 10, "//div[@id='74958_Related
		// Assets-breadcrumb']");
		Thread.sleep(1000);
		navigationTiming.theMain("RelatedAssets");
	}

	private static void selectAsset() throws InterruptedException {

		WebElement Asset = getElementByXpath(chdriver, 10, elements.row1);

		Asset.click();
		Thread.sleep(1000);
		navigationTiming.theMain("SelectingAssets");
	}

	public static void createAsset_glossary() throws InterruptedException {
		WebElement closeTab = getElementByXpath(chdriver, 10, elements.closeTab2);

		closeTab.click();
		WebElement closeTab1 = getElementByXpath(chdriver, 10, elements.closeTab1);

		closeTab1.click();
		WebElement newbttn = getElementByXpath(chdriver, 10, elements.newBttn);
		newbttn.click();
		WebElement Glossary = getElementByXpath(chdriver, 10, elements.newGlossary);
		Glossary.click();
		WebElement myDynamicElement2 = getElementByXpath(chdriver, 10, elements.AssetName);
		myDynamicElement2.sendKeys(getDate() + "_glossary");
		WebElement myDynamicElement3 = getElementByXpath(chdriver, 10, elements.AssetDescription);
		myDynamicElement3.sendKeys("performance aws_glossary");
		WebElement myDynamicElement4 = getElementByXpath(chdriver, 10, elements.AssetRefNumber);

		int random_num = ThreadLocalRandom.current().nextInt();
		myDynamicElement4.sendKeys(random_num + "");
		WebElement dropdown = getElementByXpath(chdriver, 10, elements.AssetformatTypeDropdown);
		dropdown.click();
		WebElement dropdownselect = getElementByXpath(chdriver, 10, elements.AssetformatTypeSelect);
		dropdownselect.click();
		WebElement example = getElementByXpath(chdriver, 10, elements.Assetexample);
		example.sendKeys("glossary eg");
		WebElement KDEcheckbox = getElementByXpath(chdriver, 10, elements.AssetisKDE);
		KDEcheckbox.click();
		/*
		 * WebElement Parent=getElementByXpath(chdriver,
		 * 10,"//div[@class='asset-picker-button']//img"); Parent.click(); WebElement
		 * SelectParent=getElementByXpath(chdriver,
		 * 10,"//tr[1]//td[2]//span[1]//div[1]"); SelectParent.click(); WebElement
		 * SelectBttn=getElementByXpath(chdriver,
		 * 10,"//span[contains(text(),'Select')]"); SelectBttn.click();
		 */
		WebElement save = getElementByXpath(chdriver, 10, elements.AssetSave);
		save.click();
		Thread.sleep(1000);
		navigationTiming.theMain("CreateAsset-Glossary");
	}

	public static void createAsset_policy() throws InterruptedException {
		WebElement DataGovernanceTab = getElementByXpath(chdriver, 10, elements.dataGovernanceBttn);
		DataGovernanceTab.click();

		WebElement newbttn = getElementByXpath(chdriver, 10, elements.newBttn);
		newbttn.click();
		WebElement policy = getElementByXpath(chdriver, 10, elements.newPolicy);
		policy.click();
		WebElement myDynamicElement2 = getElementByXpath(chdriver, 10, elements.AssetName);
		myDynamicElement2.sendKeys(getDate() + "_policy");
		WebElement myDynamicElement3 = getElementByXpath(chdriver, 10, elements.AssetDescription);
		myDynamicElement3.sendKeys("performance aws_policy");
		WebElement myDynamicElement4 = getElementByXpath(chdriver, 10, elements.AssetRefNumber);
		int random_num = ThreadLocalRandom.current().nextInt();
		myDynamicElement4.sendKeys(random_num + "");
		WebElement checkBoxElement = getElementByXpath(chdriver, 10, elements.checkBoxElement);
		checkBoxElement.click();
		WebElement example = getElementByXpath(chdriver, 10, elements.Assetexample);
		example.sendKeys("policy eg");
		/*
		 * WebElement Parent=getElementByXpath(chdriver,
		 * 10,"//div[@class='asset-picker-button']//img"); Parent.click(); WebElement
		 * SelectParent=getElementByXpath(chdriver,
		 * 10,"//tr[1]//td[2]//span[1]//div[1]"); SelectParent.click(); WebElement
		 * SelectBttn=getElementByXpath(chdriver,
		 * 10,"//span[contains(text(),'Select')]"); SelectBttn.click();
		 */
		WebElement save = getElementByXpath(chdriver, 10, elements.AssetSave);
		save.click();
		Thread.sleep(1000);
		navigationTiming.theMain("CreateAsset-Policy");
	}

	public static void addRelatedAsset() throws InterruptedException {
		WebElement DataGovernanceTab = getElementByXpath(chdriver, 10, elements.dataGovernanceBttn);
		DataGovernanceTab.click();
		// search for specific asset
		getElementByXpath(chdriver, 10, elements.searchTab).sendKeys(getDate());
		getElementByXpath(chdriver, 10, elements.searchTab).sendKeys(Keys.ENTER);
		WebElement selectAsset = getElementByXpath(chdriver, 10, elements.row1);
		// selcet Asset
		selectAsset.click();
		WebElement editAsset = getElementByXpath(chdriver, 10, elements.EditBttn);
		editAsset.click();

		// select Related Asset in Edit
		WebElement RelatedAssetBttn = getElementByXpath(chdriver, 10, elements.relatedAssetTab);
		RelatedAssetBttn.click();

		WebElement AddRelatedAssetBttn = getElementByXpath(chdriver, 10, elements.AddrelatedAssetBttn);
		AddRelatedAssetBttn.click();

		// clear previous requests
		navigationTiming.clearperformaceData(chdriver);

		addStakeholderGlossaryassociation();
		addParentGlossaryassociation();
		addGlossaryPolicyassociation();
		addRelatedGlossaries();

		// clear previous requests
		navigationTiming.clearperformaceData(chdriver);
		Thread.sleep(1000);
		WebElement saveBttn = getElementByXpath(chdriver, 10, "//button[contains(text(),'Save')]");
		saveBttn.click();
		Thread.sleep(2000);
		navigationTiming.theMain("EditRelatedAsset");
	}

	private static void selectRelationshipAsset() throws InterruptedException {
		// TODO Auto-generated method stub
		WebElement Asset = getElementByXpath(chdriver, 10, "//tr[1]//td[2]//span[1]//div[1]//input[1]");
		Asset.click();
		// WebElement Asset1 = getElementByXpath(chdriver,
		// 10,"//tr[2]//td[2]//span[1]//div[1]//input[1]");
		// Asset1.click();
		Thread.sleep(2000);
		WebElement seletctBttn = getElementByXpath(chdriver, 10, "//span[contains(text(),'Select')]");
		seletctBttn.click();

	}

	private static void addRelatedGlossaries() {
		// TODO Auto-generated method stub
		getPageSizetxt();
		WebElement DropdownRelatedAssetBttn = getElementByXpath(chdriver, 10, elements.relatedAssetDropdownBttn);
		DropdownRelatedAssetBttn.click();
		getElementByXpath(chdriver, 10, elements.RelatedGlossaries).click();
	}

	private static void addGlossaryPolicyassociation() throws InterruptedException {
		// TODO Auto-generated method stub

		getElementByXpath(chdriver, 10, elements.relatedAssetDropdownBttn).click();
		getElementByXpath(chdriver, 10, "//span[contains(text(),'Glossary Policy association')]").click();
		// select pagesize 100
		pageSize100("GlossaryPolicyassociation");
		// select 2 related glossaries
		selectRelationshipAsset();
	}

	private static void addParentGlossaryassociation() throws InterruptedException {
		// TODO Auto-generated method stub
		getElementByXpath(chdriver, 10, elements.relatedAssetDropdownBttn).click();
		getElementByXpath(chdriver, 10, "//span[contains(text(),'Parent Glossary association')]").click();
		pageSize100("ParentGlossaryassociation");
		// select 2 related glossaries
		selectRelationshipAsset();
	}

	private static void addStakeholderGlossaryassociation() throws InterruptedException {
		// TODO Auto-generated method stub
		getElementByXpath(chdriver, 10, elements.relatedAssetDropdownBttn).click();
		getElementByXpath(chdriver, 10, "//span[contains(text(),'Stakeholder Glossary association')]").click();
		pageSize100("StakeholderGlossaryassociatio");
		// select 2 related glossaries
		selectRelationshipAsset();
	}

	private static String getPageSizetxt() {
		// TODO Auto-generated method stub
		String pgsize = null;
		if (elementIsPresent("//span[@class='d-r35-pagination__page-location']")) {
			String pageSizeText = chdriver.findElementByXPath("//span[@class='d-r35-pagination__page-location']")
					.getText();
			String[] pgArr = pageSizeText.split("of");
			pgsize = pgArr[1].replaceAll(" ", "");
			System.out.println(pgsize);
			System.out.println(pageSizeText);

		}
		return pgsize;
	}

	public static Boolean elementIsPresent(String locator) {
		Boolean isPresent = chdriver.findElements(By.xpath(locator)).size() > 0;
		return isPresent;
	}

	private static String getDate() {
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
		Date date = new Date();
		String dates = formatter.format(date);
		System.out.println(formatter.format(date));
		return dates;
	}

	@SuppressWarnings("deprecation")
	public static WebElement getElementByXpath(WebDriver driver, long time, String xpath) {
		WebElement element = null;
		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

		return element;
	}
}
