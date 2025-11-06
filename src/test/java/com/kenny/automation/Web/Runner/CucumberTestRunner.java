package com.kenny.automation.Web.Runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
	    features = "src/test/java/resources/features",
	    glue = {"com.kenny.automation.Web.StepDefinitions", "com.kenny.automation.Web.Hooks"},
	    plugin = {
	        "pretty",
	        "html:target/cucumber-reports.html",
	        "json:target/cucumber-report.json",
	        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
	    },
	    monochrome = true
	)
	public class CucumberTestRunner extends AbstractTestNGCucumberTests {
	}
