/**
 * Author: Mike Mann
 * Date Created: 2.21.18
 * 
 * 
 * Script tests a CRM web application 
 * Tests log in, finding a person from the account list, selecting to record a call, building
 * a call report, saving the call report, and logging out.
 *
 * Tools used are Selenium WebDriver, TestNG, and Java
 * 
 */

package com.Mike_Mann;

import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * Test class for CRM web application
 * @author Mike Mann
 *
 */
public class MikeMannBugBliltzTest 
{

	private WebDriver driver;									//the webDriver interface
	private String url = "";									//base url to log in
	private String username = "";								//the username to use for login
	private String password = "";								//the password to use for login

	/**
	 * Logs into the salesforce application with the stored credentials
	 * Tests that we successfully login 
	 * @throws Exception
	 */
  @Test(priority = 1)
  public void logIn() throws Exception
  {
		
		//enter the username data member into the username text box
		driver.findElement(By.id("username")).sendKeys(username);						
		
		//enter the password data member into the password text box
		driver.findElement(By.id("password")).sendKeys(password);							
		
		//click Login button to complete login
		driver.findElement(By.id("Login")).click();	
		
		//Assert the login was successful
		//Determine it is successful if the userNavButton with logged in user name is displayed on the new page
		Assert.assertNotEquals(false, driver.findElement(By.id("userNavButton")).isDisplayed(), "Log in error\n");
	  
  }
  
  /**
   * Travels to the myAccounts tab
   * Goes to Adams, Bob account page
   * Then select record a call
   * @throws Exception
   */
  @Test(priority = 2)
  public void recordCall() throws Exception
  {
	  
	  	//follow the link of the "My Accounts" tab
		driver.findElement(By.linkText("My Accounts")).click();								
		
		//travel to a child frame to access the element with our account link
		driver.switchTo().frame("itarget");
		
		//locate and select the link for the specified account from the account list
		WebElement bob = driver.findElement(By.linkText("Adams, Bob"));
		bob.click();
		
		//switch out of our child frame
		driver.switchTo().defaultContent();
		
		
		/*
		 *Test that the actual name found in the name field on the account page
		 *contains our expected name. In this case "Bob Adams" is found in Dr. Bob Adams
		 *first convert each string to lower case and then check if the name is contained 
		 */
		
		//name of the person account we expected to travel to
		String expectedName = "Bob Adams";		
		//actual name in the new page name
		String actualName = driver.findElement(By.id("acc2_ileinner")).getText();
		
		Assert.assertEquals(true, actualName.toLowerCase().contains(expectedName.toLowerCase()), "Selected the Wrong Account");
		
		//assure that the "record a call" button is displayed before clicking
		WebElement recordTheCall = driver.findElement(By.name("record_a_call"));
		
		Assert.assertEquals(true, recordTheCall.isDisplayed(), "'Record a call' is not displayed.\n");
		
		//locate and click the "record a call" button
		driver.findElement(By.name("record_a_call")).click();
		
		//wait for the "Call Report" header to be available
		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"veeva-app\"]/div/div/form/div/div[1]/table/tbody/tr/td[1]/h2")));
		
		//Make sure it is the "Call Report" page
		Assert.assertEquals("Call Report", driver.findElement(
				By.xpath("//*[@id=\"veeva-app\"]/div/div/form/div/div[1]/table/tbody/tr/td[1]/h2")).getText(), 
				"Did not travel to Call Report Page.\n");
		
  }
  
  /**
   * Builds the call report
   * Selects 'Mass Add Promo Call," Cholecap, Labrinone, QNASL CoPay Card quantity of 2
   * saves the call report when its finished building it
   */
  @Test(priority = 3)
  public void buildCallReport() throws Exception
  {
	  //wait for the "Record Type" Dropdown box to be available
	  WebDriverWait wait = new WebDriverWait(driver, 15);
	  wait.until(ExpectedConditions.elementToBeClickable(By.id("RecordTypeId")));
	  
	  //select the "Record Type" dropdown box
	  Select recordDropDown = new Select(driver.findElement(By.id("RecordTypeId")));

	  //select the "Mass Add Promo Call" option by visibleText from the "Record Type" dropdown
	  recordDropDown.selectByVisibleText("Mass Add Promo Call");
	  
	  //assert "Mass Add Promo Call appears in the text box
	  //wait for the "Record Type dropdwon to be visible again after page reload
	  wait.until(ExpectedConditions.elementToBeClickable(By.id("RecordTypeId")));
	  //Assure that the "Mass Add Promo Call" page was navigated to
	  Assert.assertEquals("Mass Add Promo Call", driver.findElement(
			  By.xpath("//*[@id=\"veeva-app\"]/div/div/form/div/div[1]/table/tbody/tr/td[1]/h2")).getText(), 
			  "'Mass Add Promo Call' was not selected.\n");
	  
	  //store the Cholecap checkbox element
	  WebElement cholecap = driver.findElement(By.name("Cholecap"));
	  
	  //select Cholecap checkbox in the Detail Priority section
	  cholecap.click();
	  
	  //TODO assert it was clicked
	  Assert.assertTrue(cholecap.isSelected(), "Cholecap was not selected.\n");
	  
	  //store the labrinone checkbox element
	  WebElement labrinone = driver.findElement(By.name("Labrinone"));
	  
	  //select Labrinone checkbox in the Detail Priority section
	  labrinone.click();
	  
	  //TODO assert it was checked
	  Assert.assertTrue(labrinone.isSelected(), "Labrinone was ot selected.\n");
	  
	  //store the first 'Product' dropdown box in the 'Call Discussion' section
	  WebElement checkCholecap = driver.findElement(By.xpath("//*[@id=\"veeva-app\"]/div/div/form/div/div[2]/span[3]/div/div/div[2]/div/span/div/div/span/span/span/span/span/div/table/tbody/tr[2]/td/table/tbody/tr[1]/td[2]/div/div[1]/div[1]/div[2]/span/div/span/select"));
	
	  //select from our first dropdown box in the 'Call Discussion' section
	  Select firstDropDown = new Select(checkCholecap);
	  
	  //store the first element that is already visible in the text area of the dropdown box
	  WebElement dropDownElement = firstDropDown.getFirstSelectedOption();
	  
	  //Assure that the entry in the first dropdown is "Cholecap"
	  Assert.assertEquals("Cholecap", dropDownElement.getText(), "Cholecap is not ini the first dropdown box of 'Call Discussion' section.\n");
	 
	  
	
	  //store the second dropdown element in the "Call Discussion" section
	  WebElement checkLabrinone = driver.findElement(By.xpath("//*[@id=\"veeva-app\"]/div/div/form/div/div[2]/span[3]/div/div/div[2]/div/span/div/div/span/span/span/span/span/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]/div/div[1]/div[1]/div[2]/span/div/span/select"));
	  
	  //select our second dropdown box in the section
	  Select secondDropDown = new Select(checkLabrinone);
	  
	  //store the element selected and visible in the second drop down
	  dropDownElement = secondDropDown.getFirstSelectedOption();
	  
	  //Assure that "Labrinone" appears in the second dropdown by checking that the current selection has labrinone in it.
	  //this was done because it shows "Labrinone | lab Detail Group 1"
	  Assert.assertTrue(dropDownElement.getText().toLowerCase().contains("labrinone"), "Labrinone in not in the second dropdown box of 'Call Discussion' section\n");
	
	  //select QNASL Co-Pay Card checkbox
	  WebElement qnaslCoPay = driver.findElement(By.xpath("//*[@id=\"chka00U0000006DoX4IAK\"]"));
	  qnaslCoPay.click();
	  
	  //Assure that the "QNASL Co-Pay Card" was checked and selected
	  Assert.assertTrue(qnaslCoPay.isSelected());
	
	  //store the quantity text element for "QANSL Co-Pay Card"
	  WebElement quantity = driver.findElement(By.xpath("//*[@id=\"veeva-app\"]/div/div/form/div/div[2]/span[7]/div/div/div[2]/div/span/div/div/span/span/span/span/span/div/table/tbody/tr[2]/td/table/tbody[5]/tr[2]/td[3]/span/span/input"));
	
	  //clear the quantity field of the default 1
	  quantity.clear();
	
	  //enter 2 into the quantity text field
	  quantity.sendKeys("2");
	  
	  
	  //save the call report by clicking the save button
	  driver.findElement(By.name("Save")).click();
	  
	  //check that our status of the call is now "saved" in the "Status" field
	  Assert.assertEquals("Saved", driver.findElement(By.name("Status_vod__c")).getText(), "Call status is not saved.\n");
	  
	  //Assure that we have the correct quantity of 2 in the "Samples and Promotional Items" section
	  Assert.assertEquals("2", driver.findElement(By.name("Quantity_vod__c")).getText(), 
			  "Incorrect quantity of 'QNASL Co-Pay Card'; should be quantity of 2.\n");
	  
  }
  
  /**
   * Logs out of the application and tests that we are returned to the log in page 
   * by checking that the login button appears
   * @throws Exception
   */
  @Test(priority = 4)
  public void logOut() throws Exception
  {
	   
	  //click on the user menu button with the name of the user who is currently logged in
	  driver.findElement(By.id("userNavButton")).click();
			
	  //select logout from the list of options
	  driver.findElement(By.linkText("Logout")).click();
	  
	  //Test that we are back to the login page to show we have logged out
	  Assert.assertTrue(driver.findElement(By.id("Login")).isDisplayed());
	  
  }
  
  /**
   * Sets up with chromedriver
   */
  @BeforeClass
  public void setUpBrowser() 
  {
	  
	  	//locate the path that my chromedriver.exe is stored
		System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\chromedriver_win32\\chromedriver.exe");
		
		//create a new ChromeDriver 
		driver = new ChromeDriver();
		
		//maximize our browser window to full screen
		driver.manage().window().maximize();
		
		//wait to find an element in DOM for up to 30 seconds with implicit wait
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		//wait up to 30 seconds for a page to load
		driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
		
		//open up our url 
		driver.get(url);	
	  
  }

  /**
   * closes down our browser
   */
  @AfterClass
  public void closeBrowser() 
  {
	  
	  //close browser and window
	  driver.quit();
	  
  }

}
