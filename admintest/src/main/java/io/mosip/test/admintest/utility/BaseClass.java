package io.mosip.test.admintest.utility;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITest;
// Generated by Selenium IDE
//import org.junit.Test;
//import org.junit.Before;
//import org.junit.After;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class BaseClass {
	protected WebDriver driver;
	protected Map<String, Object> vars;
	protected JavascriptExecutor js;
	protected String langcode;
	protected String envPath = System.getProperty("path");
	protected String userid = System.getProperty("userid");
	protected String password = System.getProperty("password");
	protected String data = Commons.appendDate;
	public static ExtentSparkReporter html;
    public static    ExtentReports extent;
    public static    ExtentTest test;

	public void setLangcode(String langcode) throws Exception {
		this.langcode = Commons.getFieldData("langcode");
	}

  @BeforeMethod
    public void set() {
        extent=ExtentReportManager.getReports();
  }
  
	@BeforeMethod
	public void setUp() throws Exception {
		
		
		ChromeOptions options = new ChromeOptions();
		String headless=JsonUtil.JsonObjParsing(Commons.getTestData(),"headless");
		
	//System.out.println(WebDriverManager.getInstance().getBrowserPath());
//		options.addArguments("no-sandbox");
//		options.addArguments("disable-gpu");
		if(headless.equalsIgnoreCase("yes")) {
			options.addArguments("--headless=new");
		}
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver(options);
		
//		System.out.println(System.getProperty("user.dir"));
//		String configFilePath = System.getProperty("user.dir") + "\\chromedriver\\chromedriver.exe";
//		System.setProperty("webdriver.chrome.driver", configFilePath);	
//		ChromeOptions options = new ChromeOptions();
//		try {
//			String headless=JsonUtil.JsonObjParsing(Commons.getTestData(),"headless");
//			if(headless.equalsIgnoreCase("yes")) {
//				options.addArguments("--headless=new");
//			}
//		} catch (Exception e1) {
//			
//			e1.printStackTrace();
//		}
//		 
//		driver = new ChromeDriver(options);
		js = (JavascriptExecutor) driver;
		vars = new HashMap<String, Object>();
		driver.get(envPath);
		driver.manage().window().maximize();
		Thread.sleep(500);
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		
		String language1 = null;
		try {
			language1 = Commons.getFieldData("langcode");

			System.out.println(language1);
			if(!language1.equals("sin"))
			{Commons.click(test,driver, By.xpath("//*[@id='kc-locale-dropdown']"));
			String var = "//li/a[contains(text(),'" + language1 + "')]";
			Commons.click(test,driver, By.xpath(var));
			}

		} catch (Exception e) {
			e.getMessage();
		}
		driver.findElement(By.id("username")).sendKeys(userid);
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("//input[@name=\'login\']")).click();

	}

	@AfterMethod
	public void tearDown() {

		driver.quit();
		extent.flush();
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
			String langcode = JsonUtil.JsonObjParsing(Commons.getTestData(),"loginlang");
				
			File directoryPath = new File(System.getProperty("user.dir") + "\\BulkUploadFiles\\" + langcode + "\\");

			if (directoryPath.exists()) {

				contents = directoryPath.list();
				System.out.println("List of files and directories in the specified directory:");
				for (int i = 0; i < contents.length; i++) {
					System.out.println(contents[i]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return contents;
	}
}
