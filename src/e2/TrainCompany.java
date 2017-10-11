package e2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TrainCompany {
	private String name;
	private static Set<String> trainCompanyNames = new HashSet<String>();
	private Map<String, Map<String, DirectRoute>> passage;
	
	public TrainCompany(String name) {
		this.name = name;
		passage = new HashMap<String, Map<String,DirectRoute>>();
	}
	
	@Override
	public String toString() {
		return String.format("%s, offering %d routes between %d stations", 
				getName(), getDirectRoutesCount(), getStationsCount());
	}
		
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		if(name.equals(this.name)){
			return;
		}
		if(trainCompanyNames.contains(name)){
			throw new IllegalArgumentException("Name is already taken - " + name);
		}
		
		// If we're changing the name, remove the old name from the set
		if(this.name != null){
			trainCompanyNames.remove(this.name);
		}
		this.name = name;
		trainCompanyNames.add(this.name);
	}
	
	/**
	 * @return The DirectRoute object that was created/updated.
	 */
	public DirectRoute createOrUpdateDirectRoute(String fromStation, String toStation, double price){
		DirectRoute route = getDirectRoute(fromStation, toStation);
		//Check to make sure route exists
		if(route != null){
			route.setPrice(price);
			return route;
		}
		route = new DirectRoute(this, fromStation, toStation, price);
		if(passage.containsKey(fromStation) == false){
			passage.put(fromStation, new HashMap<String, DirectRoute>());
		}
		passage.get(fromStation).put(toStation, route);
		return route;
	}
	
	/**
	 * Delete the specified route, if it exists.
	 */
	public void deleteDirectRoute(String fromStation, String toStation){
		if((passage.containsKey(fromStation) && passage.get(fromStation).containsKey(toStation)) == false){
		}
		passage.get(fromStation).remove(toStation);
	}
	
	
	/**
	 * @return null if there is no route from <code>fromStation</code> to
	 * 			<code>toStation</code> with this TrainCompany.
	 */
	public DirectRoute getDirectRoute(String fromStation, String toStation){
		if((passage.containsKey(fromStation) && passage.get(fromStation).containsKey(toStation)) == false){
			return null;
		} else {
			return passage.get(fromStation).get(toStation);
		}
	}
	
	public Collection<DirectRoute> getDirectRoutesFrom(String fromStation){
		Collection<DirectRoute> routes = new HashSet<DirectRoute>();
		if(passage.containsKey(fromStation) == true){
			routes.addAll(passage.get(fromStation).values());
			return routes;
		}
		return routes;
		
		
	}
	
	public Collection<DirectRoute> getRoutesTo(String toStation){
		//get all routes
		Collection<DirectRoute> routesWithToStation = new HashSet<DirectRoute>();
		for (Map<String,DirectRoute> myPassage : passage.values()){
			if(myPassage.containsKey(toStation)){
				routesWithToStation.add(myPassage.get(toStation));
			}
		}
		return routesWithToStation;
	}
	
	public Collection<DirectRoute> getAllDirectRoutes(){
		
//		List <DirectRoute> allDirectRoutes =  new ArrayList<DirectRoute>();
//		//Iterate through HashMap passage to get to find all the routes
//		for (String fromStation : passage.keySet()) {
//			List<DirectRoute> routesOfFromStation = passage.get(fromStation);
//			for (DirectRoute route : routesOfFromStation) {
//				allDirectRoutes.add(route);
//			}
//		}
		
		Collection<DirectRoute> allDirectRoutes = new HashSet<DirectRoute>();
		for (Map<String,DirectRoute> myPassage : passage.values()){
			allDirectRoutes.addAll(myPassage.values());
		}
		return allDirectRoutes;
		
		
	}
	
	public int getDirectRoutesCount(){
		return getAllDirectRoutes().size();
	}
	
	/**
	 * @return The number of stations with service by this TrainCompany.
	 * To be clearer:
	 * - Take the union of all stations (from and to) from this.getAllDirectRoutes()
	 * - Count the unique number of stations (i.e. You only count a station
	 *   once, even if there are multiple routes from/to this station) 
	 */
	public int getStationsCount(){
		List<DirectRoute> allDirectroutes = (List<DirectRoute>) getAllDirectRoutes();
		Set<String> uniqueStations = new HashSet<String>(); 
		//Iterate through the routes and add the corresponding stations
		for (DirectRoute route : allDirectroutes) {
			uniqueStations.add(route.getFromStation());
			uniqueStations.add(route.getToStation());
		}
		return uniqueStations.size();
	}
}