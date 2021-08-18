# OpenWeathermap - Automation Testing FrameWork

The Automation framework is built to implement and execute the UI automated tests

Content:
1. [Language/Framework](#Language-Framework)
2. [Adaptable Browsers](#Adaptable-Browsers)
3. [Execution Strategy](#Execution-Strategy)
4. [Framework Architecture](#Framework-Architecture)
5. [Execution Test Case](#Execution-Test-Case)
6. [View Report](#View-Report)
7. [CI/CD Integration](#CI/CD-Integration)
8. [Distribution Execution](#Distribution-Execution)



## Language/Framework

| **Description** | **Name** |
| --------------- | ------------------------------------------------------------------------------------------------- |
| Project type | Maven |
| Programming language | [Java 8](https://www.oracle.com/java/technologies/java8.html) |
| UI Browser Automation library | [Selenium Clients and WebDriver Java Bindings](https://www.selenium.dev/downloads) |
| Test Framework | [JUnit 4](https://github.com/junit-team/junit4/wiki) |
| TestRunner strategy | based on SerenityParameterizedRunner of [Serenity](https://serenity-bdd.github.io/theserenitybook/latest/junit-data-driven.html) |
| Report Format | **.html** format that is generated by the [Maven-Serenity-plugin](https://serenity-bdd.github.io/theserenitybook/latest/maven.html) |



## Adaptable Browsers
Via the Capacities config files under resources\configuration\Env_Capacities, the test case can be run on different browser - platform
* **WIN, MAC OS**: 
  * Chrome
  * Firefox
  * MS Edge
* **Android**: default Chrome



## Execution Strategy
* **Single Mode** - Test cases is run subsequently

* **Parallel** - Test cases are run concurrently 
 
* **Local mode** - Run test cases on the local machine’s platform

* **Distribution execution** - The test cases can be run via Selenium Grid by using the RemoteWebdriver

* **CI/CD integration** - The test cases in the CI/CD pipeline of Gitlab service



## Framework Architecture
The framework core is build based on the Page Object modal. the framework is separated into 3 main layers

**Layer 1**: Include the core, configuration classes such as:

* Configuration: read configuration info files
* Page object classes: following Page Object modal to define UI element and methods with different locators.
* Additional core method classes: define some support methods to work around during interacting with the UI element

**Layer 2**: Following Serenity library to design the Steps Definition methods reusing the core methods from the layer 1 so that the steps title can be shown on the report

**Layer 3**: Following the Serenity library and Junit testing framework to design the automated test case that would rather use the Steps Definitions from the Layer 2

**Main Concepts**: Along with the layers above, some main concepts are included as below:

* [Configuration](#configuration)
* [PageObject modal](#PageObject-modal)
* [Test Data Driven](#Test-Data-Driven)
* [Step Definition](#Step-Definition)
* [Hooks](#hooks)


### Configuration:
Having the reader classes reading the `property`, `xml`, `Json` files under directories `src/test/java/configuration`:

* `CapacitiesFactory`: Read the info capacities info
* `TestConfig`: Read the global configuration
* Under package `DriverConfig`:
    * `DriverBase`
    * `DriverCreator`: Determine the web browser driver based on platform(Desktop OS, mobile OS) for execution test case
    * `Mobile_Driver`: Define the desktop webdriver for Desktop OS extending from the DriverBase
    * `Web_Driver`: Define the appiumdriver for mobile device platform extending from the DriverBase

Configuration Info files are located under:

* **src/test/resources/configuration/Env_Capacities**: 

    * **`WebLocal_Caps.json`**: Specific the browser-platform for local execution.
    * **`WebRemote_Caps.json`**: Specific the browser-platform for remote execution.

Examples:
* This is capacities format for the `Win/MAC OS` platform

```
[
    {"caps":
		{
		"browserName": "Chrome"
		}
	 }
]
```
* OR this format is used for the `mobile device` platform

```
[
{
	"caps":
	{
		"automationName": "UiAutomator2",
		"platformName": "Android",
		"browserName": "Chrome",
		"deviceName": "Galaxy Nexus API 30",
		"platformVersion": "11.0",
		"chromedriverChromeMappingFile": "C:\Users\vinhl\OneDrive\Desktop\ChromeDrivers\DriverMappingFile.json",
		"chromedriverExecutableDir": "C:\Users\vinhl\OneDrive\Desktop\ChromeDrivers",
	}
}
]
```

* **src/test/resources/configuration/Execution_config**:
  * **`Exec_Config.properties`**: Store the global test case configuration file info like: `Execution modal`(local, remote), `Execution Environment`(QA, UAT), `Remote info`(Selenium Grid / Cloud), `headless mode`.

  * **`QA_TestData.xml`**: Store the test `execution environment` info like: (url, login info).


### PageObject modal:

In Page Object Model, consider each web page of an application as a class file. Each class file will contain only corresponding web page elements.

The classes are located under the `src/test/java/pages` to interact with Web UI and core methods follow PageObject modal. It is useful in reducing code duplication and improves test case maintenance. They also extend from the class `PageObjects` under `src/test/java/baseClasses`.


### Test Data Driven:

Having class `DataReader` under `src/test/java/TestDataParsing` to read the data from `data.json` under `src/test/resource/TestData` that is used for specific test case.


### Step Definition:

Define Step Library classes include method of end use behavior using @Step annotation so that can be read from the Report file.

All Step Definition classes are located under src/test/java/StepDefinition and extend from the base class `src/test/java/StepsBass.java`.

The Step Library's methods can be injected to the Test class via `@Steps` instance.


### Hooks: 

Using some main Hooks of `Junit4` and `Serenity` test framework:

* @TestData: this `Serenity` annotation is used to create series of browsers based on the `Env_Capacities` files (WebLocal_Caps.json Or WebRemote_Caps.json) so that the test can be run on cross browsers
* **@Before**: this `JUnit4` annotation is the Pre-condition that is run before each of `@Test` method
* **@After**: this `JUnit4` annotation is the Post-condition that is run after each of `@Test` method
* **@Steps**: This `Serenity` annotation tells Serenity to inject the `@Step` step library into the test
* **@Step**: this `Serenity` hook is Step Library are where we model the behaviour of the end users
* **@Test**: this `JUnit4` annotation is define a method as the automation test executed and reported 
* **@Title**: this `Serenity`annotation lets us provide own title for the test in the test reports
* **@Category**: This `Junit` annotation include the test method or test class into the suite being executed 



## Execution Test Case

### Single Test mode:

Run specific test class: `$mvn dest=<testclass> test`.

Example: `mvn dest=TC_008_010 test`

Test cases can also run in test suite:

* Using `@Category(E2E_suite.class)` in the test class.

If needing more suite, the 'suite_name' `interface` must be created under `src/test/java/testcases` before.

* Include profile in the `pom.xml` file:
  
E.g:
 
``` 
<profiles>
 <profile>
	   <id>E2E_suite</id>
	   <properties>
	       <testcase.groups>Category_E2E</testcase.groups>
	   </properties>
	</profile>
</profiles>
```

* Run with the : `mvn test -P <profileID>`

E.g: `mvn test -P E2E_suite`


### Parallel mode:
**Run cross different browsers:**
  * Using the `SerenityParameterizedRunner` and `@Concurrent(threads = "10x")` from `TestBase` class
  * Add Browser capacity in the files under `Env_Capacities`.
 
 E.g: 
 
```
[
    {"caps":
		{
		"browserName": "Chrome"
		}
	 },
	 
	  {"caps":
		{
		"browserName": "FireFox"
		}
	 },
]
```

**Run multiple tests class/methods concurrently:**

* Configure the parameters under `maven-surefire` plugin in `pom.xml`:
    * parallel: methods or classes   		
    * threadCount: number of threads
    
* Run: 

```bash
mvn test -P <profileID>
```
E.g: `mvn test -P E2E_suite`


## View Report
Using `mvn serenity:aggregate` supported by `Serenity-Maven-Plugin` to generate the full report then can view the report under the project directory path: `Openweathermap\SerenityReport\index.html` 


## CI/CD Integration
The Automated testing be included in the CI/CD pipeline so that Version Control System(VCS). Below is an solution so that can trigger running automation test case on Gitlab pipeline – Test phase

* GitRunner is installed in local machine
* Register the gitRunner token ID on the Automation Respository project setting
* Create job as stage test in the `Automation Project pipeline` “.gitlab-ci.yml”:

```
variables:
  JAVA_HOME: 'C:\Program Files\Java\jdk-15.0.1'

SmokeTest-job:
  stage: test
  #services:
  #  - selenium/standalone-chrome
  script:
    #- Start-Process "cmd.exe"
    - Set-Location "C:\Users\Admin\Automation Projects\openweathercity"
    - echo $env:JAVA_HOME
    - ./mvn test serenity: aggregate
```
* Create trigger job in the `Project pipeline` “.gitlab-ci.yml”:

```
test-E2E:
  stage: test
  trigger: 
    project: <https://github.com/vinhle870/Openweathermap>
    strategy: depend
    include: https://github.com/vinhle870/Openweathermap/.gitlab-ci.yml'
```



## Distribution Execution
The Automated testing be included in Distribution using the [Selenium Grid](https://www.selenium.dev/downloads/).
Below is an solution so that can trigger running automation test case via Selenium Grid:

**From Controller machine**: 
* Start the Sessions Map. E.g:
`java -jar selenium-server-4.0.0-alpha-2.jar sessions`
* Start the Distributor. E.g:
`java -jar selenium-server-4.0.0-alpha-2.jar router --sessions http://localhost:5556 --distributor http://localhost:5553`
* Start the Router. E.g: `java -jar selenium-server-4.0.0-alpha-2.jar router --sessions http://localhost:5556 --distributor http://localhost:5553`

**From the Agent machine**:
* Create a Node.E.g:
`java -jar selenium-server-4.0.0-alpha-2.jar node --detect-drivers`


































	
