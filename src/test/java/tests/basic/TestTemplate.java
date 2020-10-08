package tests.basic;


import org.testng.annotations.Test;

import tests.supers.TestBase;

public class TestTemplate extends TestBase {


	@Test
	public void test() {
		try {

			app.getDriver().get("http://selenium2.ru/");


			endTestSuccess();
		} catch (Throwable t) {
			onTestFailure(t);
		}
	}


}
