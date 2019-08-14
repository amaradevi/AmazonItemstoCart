package com.amazon;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;




public class BaseDriverUtil {
	
	
	public WebDriver d;
	String userdir=System.getProperty("user.dir");
	
	public Properties loginProp;
	public Logger Log;
	
	public BaseDriverUtil() {
		
		
	}
	
	public BaseDriverUtil(String clsname) {
		Log=Logger.getLogger(clsname);
		PropertyConfigurator.configure(userdir+"//resources//logger.properties");
		
	}
	
	public WebDriver basesetup(String browsername) throws Exception {
		
		loginProp = new Properties();
		FileInputStream fin=new FileInputStream(userdir+"//resources//Login.properties");
		loginProp.load(fin);
		if ( browsername.contentEquals("chrome" ) ) {
			chromeIntialize();
		}
		
	    if ( browsername.contentEquals("firefox" ) ) {
	    	firefoxIntialize();
		}
	
	    
		String url=loginProp.getProperty("url");
		d.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
	    d.get(url);
	    // To maximize the browser
	   d.manage().window().maximize();
	   d.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
	   
	   return d;
	   		
	}
	

	public void basetearDown() {
		d.close();
		
	}
	
	
	public void chromeIntialize() {
		 System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
		 d = new ChromeDriver();
		 
	}
	
	
	
	
	public void firefoxIntialize() {
		 System.setProperty("webdriver.gecko.driver", "D:\\geckodriver-v0.23.0-win64\\geckodriver.exe");
		 d = new FirefoxDriver();
		 
	}
	
	

}
