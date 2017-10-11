package e2;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* Helper functions are located at the bottom.
 *
 * Additional Notes: Used for-each loop notation; example:
 * String[] my_list = {"A", "B", "C"};
 * for (String letter : my_list) {
 * print(letter); }
 * Similar to Python; "for letter in my_list: print(letter)"
 */

public class MyTripAdvisor {
	private Set<TrainCompany> trainCompanies;
	
	public MyTripAdvisor() {
		// A HashSet assuming we don't retain the order of the set
		trainCompanies = new HashSet<TrainCompany>();
	}
	
	public void addTrainCompany(TrainCompany trainCompany){
		trainCompanies.add(trainCompany);
	}
	
	/**
	 * Return the price of a cheapest trip from <code>fromStation</code>
	 * to <code>toStation</code>.
	 * Return -1, if there is no trip between the two specified stations.
	 */
	public double getCheapestPrice(String fromStation, String toStation){
		if(fromStation.equals(toStation)){
			double cheapest = -1;
			for(DirectRoute dirRoute : routesFromStation(fromStation)){
				if(cheapest != -1 && cheapest < dirRoute.getPrice()){
					continue;
				}
				double price = dirRoute.getPrice() + getCheapestPrice(dirRoute.getToStation(), fromStation);
				if(cheapest == -1 || cheapest > price){
					cheapest = price;
				}
			}
			return cheapest;
		} else {
			Map<String, Double> myPrices = new HashMap<String, Double>();
			myPrices.put(fromStation, 0.0);
			Map<String, Double> prices = myPrices;
			
			String currentNode = null;
			String currentStation = fromStation;
			Set<String> visitedStations = new HashSet<String>();
			Set<String> unvisitedStations = new HashSet<String>();
			Collection<DirectRoute> routes = new HashSet<DirectRoute>();
		    
			// Dijkstra's algorithm (https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)
			while(currentStation != null && (visitedStations.contains(toStation) == false)){
				// Traverse though adjacent nodes
				for(TrainCompany myCompany : trainCompanies){
					for(DirectRoute myRoute : myCompany.getDirectRoutesFrom(fromStation)){
						if(visitedStations == null || (visitedStations.contains(myRoute.getToStation()) == false)){
							routes.add(myRoute);
						}
					}
				}
				
				for(DirectRoute dirRoute : routes){
					unvisitedStations.add(dirRoute.getToStation());
					double routePrice = myPrices.get(dirRoute.getFromStation()) + dirRoute.getPrice();
					if((myPrices.containsKey(dirRoute.getToStation()) == false) || 
							(myPrices.get(dirRoute.getToStation()) > routePrice)){
						
						myPrices.put(dirRoute.getToStation(), routePrice);
					}
					continue;
				}
				
				// Update the visited and unvisited nodes and find the next node to visit
				for(String unvisitedNode : unvisitedStations){
					if(currentNode == null || myPrices.get(unvisitedNode) < myPrices.get(currentNode)){
						currentNode = unvisitedNode;
					}
				}
				currentStation = currentNode;
				visitedStations.add(currentStation);
				unvisitedStations.remove(currentStation);
			}
			
			if (prices.containsKey(toStation)){
				return prices.get(toStation);
			}
			return -1;
		} // End of else clause
	} // End of getCheapestPrice
	
	/**
	 * Return a cheapest trip from <code>fromStation</code> to <code>toStation</code>,
	 * as a list of DirectRoute objects.
	 * Return null, if there is no trip between the two specified stations.
	 */
	public List<DirectRoute> getCheapestTrip(String fromStation, String toStation){
		List<DirectRoute> cheapestTrip = null;
		List<DirectRoute> tripList = new LinkedList<DirectRoute>(); // Direct Route head to tail
		Map<String, Double> myPrices = new HashMap<String, Double>();
		myPrices.put(fromStation, 0.0);
		
		String nextStation = toStation;
		String prevStation = fromStation;
		Set<String> visitedStations = new HashSet<String>();
		Set<String> unvisitedStations = new HashSet<String>();
		Collection<DirectRoute> routes = new HashSet<DirectRoute>();
		double totalPrice = 0.0;
		double cheaperPrice = 0.0;
		
		if(fromStation.equals(toStation)){
			for(DirectRoute dirRoute : routesFromStation(fromStation)){
				List<DirectRoute> dirRouteList = getCheapestTrip(dirRoute.getToStation(), fromStation);
				if(dirRouteList == null){
					continue;
				}
				dirRouteList.add(0, dirRoute);
				
				// Total price for direct route
				for(DirectRoute myRoute : dirRouteList){
					totalPrice += myRoute.getPrice();
				}
				// Total price for current (cheaper) route
				for(DirectRoute myRoute : cheapestTrip){
					cheaperPrice += myRoute.getPrice();
				}
				
				// Compare the two routes
				if(cheapestTrip == null || cheaperPrice > totalPrice){
					cheapestTrip = dirRouteList;
				}
			}
			return cheapestTrip;
			
		} else {
			// Dijkstra's algorithm (https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)
			while(prevStation != null && (visitedStations.contains(toStation) == false)){
				// Traverse though adjacent nodes
				for(DirectRoute r : unvisitedStations(prevStation, visitedStations)){
					unvisitedStations.add(r.getToStation());
					// Update the adjacent nodes
					double routePrice = myPrices.get(r.getFromStation()) + r.getPrice();
					if((myPrices.containsKey(r.getToStation()) == false) || 
							(myPrices.get(r.getToStation()) > routePrice)){
						
						myPrices.put(r.getToStation(), routePrice);
					}
					continue;
				}
				
				// Update the visited and unvisited nodes and find the next node to visit
				String currentNode = null;
				for(String unvisitedNode : unvisitedStations){
					if(currentNode == null || myPrices.get(unvisitedNode) < myPrices.get(currentNode)){
						currentNode = unvisitedNode;
					}
				}
				prevStation = currentNode;
				visitedStations.add(prevStation);
				unvisitedStations.remove(prevStation);
				
			}
			if(myPrices.containsKey(toStation) == false){
				return null;
			}
			
			while(nextStation.equals(fromStation) == false){
				double targetPrice = myPrices.get(nextStation);
				for(TrainCompany myCompany : trainCompanies){
					routes.addAll(myCompany.getRoutesTo(nextStation));
				}
				for(DirectRoute dirRoute : routes){
					if(myPrices.containsKey(dirRoute.getFromStation()) && 
							(myPrices.get(dirRoute.getFromStation()) + dirRoute.getPrice() == targetPrice)){
						tripList.add(0,  dirRoute);
						nextStation = dirRoute.getFromStation();
					}
				} 
			}
			return tripList;
		} // End of else clause
	} // End of getCheapestTrip
	
	// Helper Functions... unvisitedStations and routesFromStation
	
	// Keep track of all the unvisited nodes
	private Collection<DirectRoute> unvisitedStations(String fromStation, Set<String> visitedStations) {
		Collection<DirectRoute> myDirRoutes = new HashSet<DirectRoute>();
		for(TrainCompany myCompany : trainCompanies){
			for(DirectRoute myRoute : myCompany.getDirectRoutesFrom(fromStation)){
				if(visitedStations == null || (visitedStations.contains(myRoute.getToStation()) == false)){
					myDirRoutes.add(myRoute);
				}
			}
		}
		return myDirRoutes;
	}
	
	// When called, gets the direct route of unvisited nodes
	private Collection<DirectRoute> routesFromStation(String fromStation) {
		return unvisitedStations(fromStation, null);
	}
	
}
