package de.abas.custom.spareParts.catalogue;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import de.abas.acceptanceTests.AbasAcceptanceRunner;
import de.abas.acceptanceTests.stepDefs.EDPOptions;

@RunWith(AbasAcceptanceRunner.class)
@EDPOptions(env = true)
@CucumberOptions(features = "src/integTest/resources/cucumber", glue = {
		"classpath:de.abas.acceptanceTests" }, plugin = { "de.abas.acceptanceTests.support.AbasFormatter", "pretty",
				"json:build/reports/cucumber.json", "html:build/reports/cucumber" }, strict = true, monochrome = false)
public class RunCukesTest {

}