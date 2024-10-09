package webdrivermgr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.apache.hc.client5.http.impl.classic.MainClientExec;
import org.openqa.selenium.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.github.bonigarcia.wdm.WebDriverManager;

public class webdriver {
	static WebDriver chdriver;

	/* TO SEARCH AND SELECT A ROW */
	public static void loadHomePageAndSearch() throws InterruptedException {
//Go to HomePage and Search
		chdriver.get("http://10.65.145.210:3001/");
		chdriver.manage().window().fullscreen();
		getElementByXpath(chdriver, 10, "//input[@placeholder]").sendKeys("*");
		getElementByXpath(chdriver, 10, "//input[@placeholder]").sendKeys(Keys.ENTER);
		long startTim1 = System.currentTimeMillis();
		WebElement searchElement = getElementByXpath(chdriver, 10,
				"//tbody[@class='d-r35-table__body']//tr[10]//a[@class='cell-link']");
		long endTim1 = System.currentTimeMillis();
		System.out.println("total time is " + (endTim1 - startTim1) + " for getting search results");

//Move to Page Size 15
		WebElement pageSizeElement = getElementByXpath(chdriver, 10, "//select[@class='d-r35-select']");
		Select select = new Select(pageSizeElement);
		select.selectByValue("15");
		long startTi = System.currentTimeMillis();
		WebElement pgsizeElement = getElementByXpath(chdriver, 10,
				"//tbody[@class='d-r35-table__body']//tr[15]//a[@class='cell-link']");
		long endTi = System.currentTimeMillis();
		System.out.println("total time is " + (endTi - startTi) + " for changing page size to 15");

//change Offset	
		long stime3 = System.currentTimeMillis();
		WebElement nextArrowElement = getElementByXpath(chdriver, 10,
				"//i[@class='d-r35-pagination__page-selector__button__next']");
		nextArrowElement.click();
		WebElement lastRowEle = getElementByXpath(chdriver, 10,
				"//tbody[@class='d-r35-table__body']//tr[10]//a[@class='cell-link']");
		long eTime3 = System.currentTimeMillis();
		System.out.println("total time is " + (eTime3 - stime3) + " for changing Offset using arrow");

//move To Page Size 100
		WebElement pageSizeElement100 = getElementByXpath(chdriver, 10, "//select[@class='d-r35-select']");
		Select select100 = new Select(pageSizeElement);
		select100.selectByValue("100");
		long startTi100 = System.currentTimeMillis();
		WebElement pgsizeElement100 = getElementByXpath(chdriver, 10,
				"//tbody[@class='d-r35-table__body']//tr[100]//a[@class='cell-link']");
		long endTi100 = System.currentTimeMillis();
		System.out.println("total time is " + (endTi100 - startTi100) + " for changing page size to 100");

		// Thread.sleep(500);
		// chdriver.close();
	}

	/* to create new glossary */
	public static void createNewGlossary() throws InterruptedException {

		long startTime = System.currentTimeMillis();
		// System.out.println("Start time is " + startTime);
		chdriver.get("http://10.65.145.210:3001/");
		WebElement newElement = getElementByXpath(chdriver, 10, "//span[contains(text(),'New')]");
		newElement.click();
		WebElement myDynamicElement1 = getElementByXpath(chdriver, 10, "//h1[contains(text(),'Glossary')]");
		myDynamicElement1.click();
		WebElement myDynamicElement2 = getElementByXpath(chdriver, 10, "//input[@class='asset-name']");
		myDynamicElement2.sendKeys("aws");
		WebElement myDynamicElement3 = getElementByXpath(chdriver, 10, "//div[@class='ql-editor ql-blank']");
		myDynamicElement3.sendKeys("aws_glossary");
		WebElement myDynamicElement4 = getElementByXpath(chdriver, 10,
				"//div[@class='d-r35-input input-element ref']//input");
		myDynamicElement4.sendKeys("2345678");
		WebElement dropdown = getElementByXpath(chdriver, 10, "//div[3]//div[1]//div[1]//button[1]");
		dropdown.click();
		WebElement dropdownselect = getElementByXpath(chdriver, 10, "//div[@id='downshift-0-item-0']");
		dropdownselect.click();
		WebElement KDEcheckbox = getElementByXpath(chdriver, 10, "//input[@class='checkbox-element']");
		KDEcheckbox.click();
		long endTime = System.currentTimeMillis();
		// System.out.println("End time is " + endTime);
		System.out.println("total time is " + (endTime - startTime) + " for creating a glossary");

		// Thread.sleep(2000);
		// chdriver.close();
	}

	public static void selectGlossary() throws InterruptedException {
		chdriver.get("http://10.65.145.210:3001/");
		Thread.sleep(5000);
		WebElement GlossaryLabel = getElementByXpath(chdriver, 10,
				"//span[@class='browse-by-item-label'][contains(text(),'Glossary')]");
		Thread.sleep(10000);
		// GlossaryLabel.click();
		Actions actions = new Actions(chdriver);

		actions.moveToElement(GlossaryLabel).click().perform();

		long stime1 = System.currentTimeMillis();
		WebElement lastrow = getElementByXpath(chdriver, 10,
				"//tbody[@class='d-r35-table__body']//tr[10]//a[@class='cell-link']");
		long etime1 = System.currentTimeMillis();
		System.out.println("total time is " + (etime1 - stime1) + " for loading a glossary label");

		long startTi = System.currentTimeMillis();
		WebElement pageSizeElement = getElementByXpath(chdriver, 10, "//select[@class='d-r35-select']");
		Select select = new Select(pageSizeElement);
		select.selectByValue("15");
		WebElement pgsizeElement = getElementByXpath(chdriver, 10,
				"//tbody[@class='d-r35-table__body']//tr[10]//a[@class='cell-link']");
		long endTi = System.currentTimeMillis();
		System.out.println("total time is " + (endTi - startTi) + " for changing page size to 15");

		// pagesize 100
		WebElement pageSizeText = getElementByXpath(chdriver, 10, "//span[@class='d-r35-pagination__page-location']");
		String text = pageSizeText.getText();
		System.out.println(text);

		long startT2 = System.currentTimeMillis();
		WebElement pageSizeElement1 = getElementByXpath(chdriver, 10, "//select[@class='d-r35-select']");
		Select select1 = new Select(pageSizeElement1);
		select.selectByValue("100");
		WebElement pgsizeElement1 = getElementByXpath(chdriver, 10,
				"//tbody[@class='d-r35-table__body']//tr[10]//a[@class='cell-link']");
		long eTime2 = System.currentTimeMillis();
		System.out.println("total time is " + (eTime2 - startT2) + " for changing page size to 15");

		long stime3 = System.currentTimeMillis();
		WebElement nextArrowElement = getElementByXpath(chdriver, 10,
				"//i[@class='d-r35-pagination__page-selector__button__next']");
		nextArrowElement.click();
		WebElement lastRowEle = getElementByXpath(chdriver, 10,
				"//tbody[@class='d-r35-table__body']//tr[10]//a[@class='cell-link']");
		long eTime3 = System.currentTimeMillis();
		System.out.println("total time is " + (stime3 - eTime3) + " for changing Offset using arrow");

	}

	public static void loginPage() {
		var chromeDriver = new ChromeDriver();
		chromeDriver.manage().deleteAllCookies();
		chromeDriver.get("chrome://settings/clearBrowserData");
		chromeDriver.findElementByXPath("//settings-ui").sendKeys(Keys.ENTER);

		chromeDriver.get("https://dev-ma.devml.infaqa.com");
		WebElement userName = getElementByXpath(chdriver, 10, "//div[@id='username']//input[@class='infaField']");
		userName.sendKeys("tkhong@informatica.com");
		WebElement password = getElementByXpath(chdriver, 10, "//div[@id='password']//input[@class='infaField']");
		password.sendKeys("p@ssw0rd1234");
		WebElement logIn = getElementByXpath(chdriver, 10, "//span[contains(text(),'Log In')]");
		logIn.click();
	}

	public static void main(String[] args) throws InterruptedException, ParseException {
		WebDriverManager.chromedriver().setup();
		chdriver = new ChromeDriver();
		// chdriver.executeScript(script, args)
		// loginPage();
		loadHomePageAndSearch();
		createNewGlossary();
		// selectGlossary();
		// getPerformance();

	}

	public static void getPerformance() throws InterruptedException, ParseException {
		// System.setProperty("webdriver.chrome.driver",
		// "C:\\Users\\mewilson\\eclipse-workspace\\automationtest\\selenium_framwork\\drivers\\chrome_driver\\chromedriver.exe");
		var chromeDriver = new ChromeDriver();

		// clear cookie
		/*
		 * chromeDriver.manage().deleteAllCookies();
		 * chromeDriver.get("chrome://settings/clearBrowserData");
		 * chromeDriver.findElementByXPath("//settings-ui").sendKeys(Keys.ENTER);
		 */
		chromeDriver.get("https://dev-ma.devml.infaqa.com");
		Long navigationStart = Long
				.parseLong(chromeDriver.executeScript("return window.performance.timing.navigationStart").toString());
		Long responseStart = Long
				.parseLong(chromeDriver.executeScript("return window.performance.timing.responseStart").toString());
		Long responseEnd = Long
				.parseLong(chromeDriver.executeScript("return window.performance.timing.responseEnd").toString());
		Long domComplete = Long
				.parseLong(chromeDriver.executeScript("return window.performance.timing.domComplete").toString());

		System.out.println(navigationStart);
		getDateMS(navigationStart);
		System.out.println("Rs" + responseStart);
		System.out.println("RE" + responseEnd);
		System.out.println(domComplete);
		System.out.println("file time" + (responseEnd - responseStart));
		System.out.println("navigation time" + getDate(navigationStart));
		System.out.println("got response" + getDate(responseStart));
		System.out.println("dom creation done" + getDate(domComplete));
		// long epoch = new java.text.SimpleDateFormat("MM/dd/yyyy
		// HH:mm:ss").parse("01/01/1970 01:00:00").getTime()/ 1000;
		Long backendPerformance_calc = responseStart - navigationStart;

		Long frontendPerformance_calc = domComplete - responseEnd;
		System.out.println("Backend Time1 : " + backendPerformance_calc + "bg2" + (responseStart - navigationStart)
				+ " frontEnd Time : " + frontendPerformance_calc);
		System.out.println();

	}

	public static Date getDate(long epoch) {

		Date expiry = new Date(epoch);
		return expiry;
	}

	public static void getDateMS(long epoch) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date expiry = new Date(epoch);
		String Ms = sdf.format(expiry);
		System.out.println(Ms + "Millisec");
		System.out.println("in milliseconds: " + expiry.getTime());
	}

	public static WebElement getElementByXpath(WebDriver driver, long time, String xpath) {
		WebElement element = null;
		element = (new WebDriverWait(driver, 10)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

		return element;
	}
}
