package com.nds.it;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

@Test
public class WebAppTest {

	private Selenium selenium;

	@BeforeTest
	public void beforeTests() {
		selenium = new DefaultSelenium("localhost", 4444, "*firefox", "http://localhost:8080/authentication/");
		selenium.start();
	}

	@AfterTest
	public void afterTests() {
		selenium.stop();
		selenium = null;
	}

	@Test
	public void testCallIndexPage() throws Exception {
		selenium.open("http://localhost:8080/authentication/index.html");
		selenium.waitForPageToLoad("60000");
		Assert.assertEquals("Hello World", selenium.getTitle());
		Assert.assertTrue(selenium.isTextPresent("Hello World!"));
	}
}
