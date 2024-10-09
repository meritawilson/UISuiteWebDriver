package webdrivermgr;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class webdriver_Test {
	public static void main(String[] args) {
		WebDriverManager.chromedriver().setup();
		// WebDriverManager.firefoxdriver().setup();
		// WebDriverManager.operadriver().setup();
		// WebDriverManager.phantomjs().setup();
		// WebDriverManager.edgedriver().setup();
		// WebDriverManager.iedriver().setup();
		WebDriver chdriver = new ChromeDriver();

		/*
		 * WebDriver IEdriver = new InternetExplorerDriver();
		 * IEdriver.get("https://seleniumhq.org/"); Thread.sleep(1000);
		 * IEdriver.close(); WebDriver driver = new FirefoxDriver();
		 * driver.get("http://localhost:3000/"); Thread.sleep(1000); driver.close();
		 */

	}
}
