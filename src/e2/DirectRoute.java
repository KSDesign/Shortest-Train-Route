package e2;

import java.util.Objects;

public class DirectRoute {
	private TrainCompany trainCompany;
	private String fromStation;
	private String toStation;
	private double price;
	
	public DirectRoute(TrainCompany trainCompany, String fromStation, String toStation, double price) {
		this.trainCompany = trainCompany;
		this.fromStation = fromStation;
		this.toStation = toStation;
		this.price = price;
	}


	public TrainCompany getTrainCompany() {
		return this.trainCompany;
	}
	
	public void setTrainCompany(TrainCompany trainCompany) {
		// Prevent train company from being null
		if(trainCompany == null){
			throw new IllegalArgumentException("Train Company cannot be NULL.");
		}
		this.trainCompany = trainCompany;
	}
	
	
	public String getFromStation() {
		return this.fromStation;
	}
	
	public void setFromStation(String fromStation) {
		// Prevent direct routes to the same station
		if(fromStation.equals(toStation)){
			throw new IllegalArgumentException("Route to duplicate stations cannot be made.");
		}
		this.fromStation = fromStation;
	}

	public String getToStation() {
		return this.toStation;
	}
	
	public void setToStation(String toStation) {
		// Prevent direct routes to the same station
		if(fromStation.equals(toStation)){
			throw new IllegalArgumentException("Route to duplicate stations cannot be made.");
		}
		this.toStation = toStation;
	}

	public double getPrice() {
		return this.price;
	}
	
	public void setPrice(double price) {
		// Price checking
		if(price < 0){
			throw new IllegalArgumentException("Price must be positive!");
		}
		this.price = price;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if((obj instanceof DirectRoute) == false){
			return false;
		}
		else{
			DirectRoute route = (DirectRoute) obj;
			return Objects.equals(this.trainCompany, route.trainCompany) 
					&& Objects.equals(this.fromStation, route.fromStation)
					&& Objects.equals(this.toStation, route.toStation)
					&& Objects.equals(this.price, route.price);
		}
	}
	
	
	@Override
	public String toString() {
		return String.format("%s from %s to %s, %.2f$", getTrainCompany().getName(), 
				getFromStation(), getToStation(), getPrice());
	}
}
