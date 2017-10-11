package e2;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import util.Constants;
import util.Utils;

public class TrainCompanyTest {

	// An example of how to verify that an exception is thrown
	@Test(expected=IllegalArgumentException.class)
	public void cannotCreateCompanyWithNullName() {
		new TrainCompany(null);
	}
	
	@Test(timeout=3000)
	public void testTrainCompanyRoutes() {
		TrainCompany company =  new TrainCompany("Via");
		
		company.createOrUpdateDirectRoute(Constants.TORONTO, Constants.OTTAWA, 30);
		assertEquals(new DirectRoute(company, Constants.TORONTO, Constants.OTTAWA, 30), company.getDirectRoute(Constants.TORONTO, Constants.OTTAWA));
		company.createOrUpdateDirectRoute(Constants.TORONTO, "Montreal", 20);
		assertEquals(new DirectRoute(company, Constants.TORONTO, "Montreal", 20), company.getDirectRoute(Constants.TORONTO, "Montreal"));
		company.deleteDirectRoute(Constants.TORONTO, Constants.OTTAWA);
		assertEquals(1, company.getDirectRoutesFrom(Constants.TORONTO).size());
		assertEquals(0, company.getRoutesTo(Constants.OTTAWA).size());
		assertEquals(1, company.getDirectRoutesCount());
		assertEquals(2, company.getStationsCount());
		company.deleteDirectRoute(Constants.TORONTO, "Montreal");
		assertEquals(0, company.getStationsCount());
	}
	
	
	
	
}
