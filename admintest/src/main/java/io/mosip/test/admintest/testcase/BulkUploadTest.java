package io.mosip.test.admintest.testcase;
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import io.mosip.test.admintest.utility.BaseClass;
import io.mosip.test.admintest.utility.Commons;
import io.mosip.test.admintest.utility.JsonUtil;
import io.mosip.test.admintest.utility.PropertiesUtil;
import io.mosip.test.admintest.utility.SetTestName;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.lang.reflect.Method;
public class BulkUploadTest extends BaseClass {
/*implements ITest{
 
 private String newTestName = "";

	 private void setTestName(String newTestName){
	     this.newTestName = newTestName;
	 }

	 public String getTestName() {

	     return newTestName;
	 }


	 @BeforeMethod(alwaysRun=true)
	 public void getTheNameFromParemeters(Method method, Object [] parameters){
		 
	     SetTestName setTestName = method.getAnnotation(SetTestName.class);
	     String testCaseName = (String) parameters[setTestName.idx()];
	     setTestName(testCaseName+"BulkUpload");
	 }
  
	  @SetTestName(idx=0) */
	 
	@Test (dataProvider = "data-provider",groups = "BU")
  public void bulkUploadCRUD(String table) throws Exception {
	
    	
    Commons.click(test,driver,By.id("admin/bulkupload"));
    Commons.click(test,driver,By.xpath("//a[@href='#/admin/bulkupload/masterdataupload']"));
    
    for(int count=0;count<=2;count++) {
    Commons.click(test,driver,By.id("Upload Data"));
 
    
  
   if(count==0) Commons.dropdown(test,driver,By.id("operation"),By.id("Insert"));
   if(count==1) Commons.dropdown(test,driver,By.id("operation"),By.id("Update"));
   if(count==2) Commons.dropdown(test,driver,By.id("operation"),By.id("Delete"));
    
    Commons.dropdown(test,driver,By.id("tableName"),By.id(table));
    // Commons.click(test,driver,By.xpath("//div[@class='custom-file-input']"));
    Commons.click(test,driver,By.xpath("//div[@class='custom-file-input']"));
    

    String filePath = System.getProperty("user.dir") + "\\BulkUploadFiles\\"+ JsonUtil.JsonObjParsing(Commons.getTestData(),"loginlang")+"\\"+table+".png";
   StringSelection ss = new StringSelection(filePath);
   Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
   
   Robot robot = new Robot();
  
   robot.delay(250);

   
   robot.keyPress(KeyEvent.VK_ENTER);
   robot.keyRelease(KeyEvent.VK_ENTER);
   robot.keyPress(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_V);
   robot.keyRelease(KeyEvent.VK_V);
   robot.delay(250);
   robot.keyRelease(KeyEvent.VK_CONTROL);
   robot.keyPress(KeyEvent.VK_ENTER);
   robot.delay(250);
   robot.keyRelease(KeyEvent.VK_ENTER);
   robot.delay(250);
    
    Commons.click(test,driver,By.xpath("//button[@id='createButton']"));
    Commons.click(test,driver,By.id("confirmpopup")); 
    test.log(Status.INFO, "Click on FileUploaded");
    
    String divText=driver.findElement(By.xpath("//div[@class='mat-dialog-content']//div")).getText();
    String divTextArr[]=divText.split(":");
    System.out.println(divTextArr[1].trim());
    
    Commons.click(test,driver,By.id("confirmmessagepopup")); //DONE
    Thread.sleep(Long.parseLong(JsonUtil.JsonObjParsing(Commons.getTestData(),"bulkwait")));

    String transId=driver.findElement(By.xpath("//table[@class='mat-table']//tr[2]//td[1]")).getText();
    String status=driver.findElement(By.xpath("//table[@class='mat-table']//tr[2]//td[5]")).getText();
  Assert.assertTrue(transId.equals(divTextArr[1].trim()));
  Assert.assertTrue(status.equalsIgnoreCase("COMPLETED"),"Status Should be COMPLETED");
}
 }
}
