package applogic;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.paulhammant.ngwebdriver.NgWebDriver;

import pages._pages_mngt.MainPageManager;
import util.Browser;
import util.FileUtilis;
import util.ParamsUtils;
import util.SelUtils;
import webdriver.WebDriverFactory;

public class ApplicationManager1 {

	private WebDriver driver;
	private NgWebDriver ngDriver;
	private WebDriverFactory webDriverFactory;
	private Logger log;

	private MainPageManager pages;

	public SelUtils su;
	public FileUtilis fileUtilis;
	public ParamsUtils paramsUtils;


	private LoginHelper1 loginHelper;


	public ApplicationManager1(boolean openBrowser, String testName, String pkg) {
		fileUtilis = new FileUtilis(this);
		paramsUtils = new ParamsUtils(this);

		webDriverFactory = new WebDriverFactory();

		if (openBrowser) {
			String downloadsFolder = paramsUtils.getPropValue(ParamsUtils.WORKSPACE) + File.separator + "Downloads" + File.separator + pkg
					+ File.separator + testName + File.separator;

			Browser browser = new Browser();
			browser.setName(paramsUtils.getPropValue(ParamsUtils.BROWSER_NAME));
			driver = webDriverFactory.getInstance(browser.getName(), downloadsFolder, this);
			ngDriver = new NgWebDriver((JavascriptExecutor) driver);
			changeWindowSize(browser.getName());
		}
	}

	public void initResources(Logger perTestLogger) {
		log = perTestLogger;

		fileUtilis.initLogger();
		su = new SelUtils(driver, log);
		pages = new MainPageManager(this);
		loginHelper = new LoginHelper1(this);

	}

	public WebDriver getDriver() {
		return driver;
	}

	public NgWebDriver getNgDriver() {
		return ngDriver;
	}

	public Logger getLogger() {
		return log;
	}

	public void changeWindowSize(String browser) {

		if (System.getProperty("os.name").toLowerCase().matches(".*windows.*")) {
			// if windows
			if (paramsUtils.getPropValue(ParamsUtils.HEADLESS_MODE).equals("true"))
				driver.manage().window().setSize(new Dimension(1920, 1080));
			else
				driver.manage().window().maximize();
		} else {
			// if linux
			driver.manage().window().setSize(new Dimension(1920, 1080));
		}

	}

	public MainPageManager pages() {
		return pages;
	}

	// HELPERS

	public LoginHelper1 getLoginHelper() {
		return loginHelper;
	}

	//Utils

	public ParamsUtils getParamsUtils() {
		return paramsUtils;
	}


	public void stop() {
		webDriverFactory.removeWebDriver();
	}

	public void captureScreenShot(String filePath) {
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File(filePath));
		} catch (IOException ioe) {
			log.info("Failed to save screenshot file '" + filePath + "': " + ioe.getMessage());
		}

		log.info("Application screenshot is saved in: " + filePath);

	}
}
