package io.mosip.testrig.adminui.utility;

import java.io.File;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Reporter;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.mosip.testrig.adminui.kernel.util.ConfigManager;
import io.mosip.testrig.adminui.kernel.util.KeycloakUserManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;


public class BaseClass {
	private static final Logger logger = Logger.getLogger(TestRunner.class);
	protected static WebDriver driver;
	protected Map<String, Object> vars;
	protected JavascriptExecutor js;
	protected String langcode;
	protected String envPath = ConfigManager.getiam_adminportal_path();
	protected String env=ConfigManager.getiam_apienvuser();
	public static String userid = KeycloakUserManager.moduleSpecificUser;
	protected String[] allpassword = ConfigManager.getIAMUsersPassword().split(",");
	protected String password = allpassword[0];
	protected  String data = Commons.appendDate;

	@BeforeMethod
	public void setUp() throws Exception {
		Reporter.log("BaseClass", true);
		logger.info("Start set up");
		if(System.getProperty("os.name").equalsIgnoreCase("Linux") && ConfigManager.getdocker().equals("yes") ) {
			logger.info("Docker start");
			String configFilePath ="/usr/bin/chromedriver";
			System.setProperty("webdriver.chrome.driver", configFilePath);

		}else {
			WebDriverManager.chromedriver().setup();
			logger.info("window chrome driver start");
		}
		ChromeOptions options = new ChromeOptions();
		String headless=ConfigManager.getheadless();
		if(headless.equalsIgnoreCase("yes")) {
			logger.info("Running is headless mode");
			options.addArguments("--headless", "--disable-gpu","--no-sandbox", "--window-size=1920x1080","--disable-dev-shm-usage");
		}
		driver=new ChromeDriver(options);
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();
		driver.get(envPath);
		logger.info("launch url --"+envPath);
		driver.manage().window().maximize();
		Commons.wait(500);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		String language1 = null;
		try {

			language1 = ConfigManager.getloginlang();
			String loginlang = null;
			System.out.println(language1);
			if(!language1.equals("sin")) {
				loginlang = JsonUtil.JsonObjArrayListParsing2(ConfigManager.getlangcode());
				Commons.click(driver, By.xpath("//*[@id='kc-locale-dropdown']"));
				String var = "//li/a[contains(text(),'" + loginlang + "')]";
				Commons.click(driver, By.xpath(var));
			}

		} catch (Exception e) {
			e.getMessage();
		}

		Commons.enter(driver, By.id("username"), userid); 
		Commons.enter(driver, By.id("password"), password);
		Commons.click(driver, By.xpath("//input[@name='login']")); 


	}


	@AfterMethod
	public void tearDown() {
		driver.quit();
	}


	@DataProvider(name = "data-provider")
	public Object[] dpMethod() {
		String listFilename[] = readFolderJsonList();
		String s[][] = null;
		String temp[] = null;
		for (int count = 0; count < listFilename.length; count++) {
			listFilename[count] = listFilename[count].replace(".csv", "");

		}

		return listFilename;
	}

	public static String[] readFolderJsonList() {
		String contents[] = null;
		try {
			String langcode = ConfigManager.getloginlang();

			File directoryPath = new File(TestRunner.getResourcePath()+ "//BulkUploadFiles//" + langcode + "//");

			if (directoryPath.exists()) {

				contents = directoryPath.list();
				logger.info("List of files and directories in the specified directory:");
				for (int i = 0; i < contents.length; i++) {
					logger.info(contents[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contents;
	}

}
