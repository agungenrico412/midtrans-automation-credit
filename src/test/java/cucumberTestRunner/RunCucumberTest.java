package cucumberTestRunner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue= {"stepsDefinition"},
        plugin = { "pretty", "json:target/result/cucumber-json-report/cucumber.json" }
)
public class RunCucumberTest {
}
