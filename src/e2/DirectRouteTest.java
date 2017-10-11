package e2;

import org.junit.Test;


import util.Constants;


public class DirectRouteTest {


	// A very basic example of a passing test
	@Test
	public void testCreateInstanceWithoutException(){
		new DirectRoute(new TrainCompany("Via"), Constants.TORONTO, Constants.OTTAWA, 37.5);
	}
	
}
