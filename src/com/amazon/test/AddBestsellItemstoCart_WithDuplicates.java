package com.amazon.test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.amazon.BaseDriverUtil;

import java.io.FileInputStream;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;



/**
 * @author adevinen
 * This usecase is to add Bestseller Items to cart  
 * 1.This tetcase will add best sellet items from page1 to cart ( including duplicates) 
 *
 */

public class AddBestsellItemstoCart_WithDuplicates {

	public WebDriver d;
	String userdir=System.getProperty("user.dir");
    BaseDriverUtil bu=new BaseDriverUtil(this.getClass().getName());
      
	Logger logger=bu.Log;
	Properties Objectrepo=new Properties();
	String browsername="chrome";
	
	  
   @BeforeTest
   public void setup() throws Exception {
			d=bu.basesetup(browsername);
			logger.info(browsername+"driver instantiated");
			FileInputStream objfile=new FileInputStream(userdir+"//resources//ObjectRepo.properties");
			Objectrepo.load(objfile);
   }
   
    @AfterTest
	public void tearDown() {
			bu.basetearDown();
	}
		

	
@Test(priority=0)
public void amazonlogin() throws InterruptedException {
	   //Login validation 
	
		String username=bu.loginProp.getProperty("username");
		String password=bu.loginProp.getProperty("password");
		
		logger.info(bu.loginProp.getProperty("url")+"log in page is opened");
	
		d.findElement(By.xpath(Objectrepo.getProperty("login"))).click();
		d.findElement(By.xpath(Objectrepo.getProperty("username"))).sendKeys(username);
		d.findElement(By.xpath(Objectrepo.getProperty("password"))).sendKeys(password);
		d.findElement(By.xpath(Objectrepo.getProperty("signinBtn"))).click();
		Assert.assertTrue(testlogin(),"Successfully login");
		logger.info("Logged in successfully with "+username);
}
 
@Test(priority=1)
public void amazonadditems() throws InterruptedException {
	
	    //Pass the search keyword aftet login 
		 String searchKey=bu.loginProp.getProperty("searchkeyword");	
		 logger.info("Started Searching "+searchKey);
		 d.findElement(By.xpath(Objectrepo.getProperty("searchText"))).sendKeys(searchKey);
		 d.findElement(By.xpath(Objectrepo.getProperty("search"))).click();
		 
		 
		 String parentwin=d.getWindowHandle();
		 
		 //Find all the Items categorized as best seller form page1  and open in new tabs 
         List<WebElement> bestsellers=d.findElements(By.xpath(Objectrepo.getProperty("bestSeller")));
	     for (WebElement bestseller: bestsellers) {
		    			 			
		    	 String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN);
		       	 bestseller.sendKeys(selectLinkOpeninNewTab);
		     }
		
	     
	    //Add all the betseller Items to cart from page1 
		Set<String> windowhandles=d.getWindowHandles();
		for ( String s:windowhandles) {
		if(! s.equals(parentwin)) {
		 d.switchTo().window(s);
         d.findElement(By.xpath(Objectrepo.getProperty("addCart"))).click();
 
    	 }
			d.switchTo().window(parentwin);	
	    }
		
		//close all the tabs except the main window 
		for ( String s:windowhandles) {
		if(! s.equals(parentwin)) {
		 d.switchTo().window(s);
	     d.close();
        }
			d.switchTo().window(parentwin);	
	 }

	d.switchTo().window(parentwin);	
    d.navigate().refresh();
    logger.info("Validating the  "+searchKey + "count in cart ");
    
    //validation of items  count  in cart with page1 best seller items  count from page1   
    d.findElement(By.xpath(Objectrepo.getProperty("cart"))).click();
    Assert.assertTrue(validateCartItems(bestsellers.size()),"Successfully Added to cart");	
    logger.info("Successfully validated  "+searchKey + "count in cart ");
}
	 
		
		public boolean testlogin() {
					
			String welcome=d.findElement(By.xpath(Objectrepo.getProperty("loginUser"))).getText();
			return welcome.contains(bu.loginProp.getProperty("LoginUserName"));
					
		}		

		public boolean validateCartItems(int count) {
					
			String item_count=d.findElement(By.xpath(Objectrepo.getProperty("cartItemscount"))).getText();
			return Integer.parseInt(item_count)==count;
						
		}
	 
}

